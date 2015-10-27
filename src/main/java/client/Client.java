package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import cli.Command;
import cli.Shell;
import domain.*;
import domain.messages.*;
import domain.responses.*;
import executors.ClientMessageExecutorFactory;
import executors.IMessageExecutorFactory;
import service.ChannelService;
import service.IChannelService;
import service.IMessageService;
import service.MessageService;
import util.Config;

public class Client implements IClientCli, Runnable {
	private final static Logger LOGGER = Logger.getLogger(Client.class.getName());

	private String componentName;
	private Config config;
	private InputStream userRequestStream;
	private PrintStream userResponseStream;
	private Shell shell;

	// threading
	private ExecutorService executorService = Executors.newCachedThreadPool();

	// connection
	private IChannel serverChannel;
	private IChannel udpChannel;
	private SocketAddress serverUDPAddress;
	private Dispatcher dispatcher;

	// services
	private IChannelService channelService;
	private IMessageService messageService;
	private IMessageExecutorFactory messageExecutorFactory;

	/**
	 * @param componentName
	 *            the name of the component - represented in the prompt
	 * @param config
	 *            the configuration to use
	 * @param userRequestStream
	 *            the input stream to read user input from
	 * @param userResponseStream
	 *            the output stream to write the console output to
	 */
	public Client(String componentName, Config config,
			InputStream userRequestStream, PrintStream userResponseStream) {
		this.componentName = componentName;
		this.config = config;
		this.userRequestStream = userRequestStream;
		this.userResponseStream = userResponseStream;

		// setup services
		channelService = new ChannelService(executorService);
		messageExecutorFactory = new ClientMessageExecutorFactory(userResponseStream, channelService);
		messageService = new MessageService(messageExecutorFactory, executorService);

		// setup shell
		shell = new Shell(componentName, userRequestStream, userResponseStream);
		shell.register(this);
	}

	@Override
	public void run() {
		// connect to server
		try {
			setupTCP(config.getString("chatserver.host"), config.getInt("chatserver.tcp.port"));
			setupUDP(config.getString("chatserver.host"), config.getInt("chatserver.udp.port"));
		} catch (IOException e) {
			LOGGER.log(Level.INFO, "Connection buildup failed");
			userResponseStream.println("Connection buildup failed");
			return;
		}

		// start shell thread
		executorService.execute(shell);
	}

	private void setupTCP(String host, int port) throws IOException {
		Socket socket = new Socket(host, port);
		serverChannel = new TCPChannel(socket, messageService);
		channelService.addChannel(serverChannel);
	}

	private void setupUDP(String host, int port) throws SocketException {
		DatagramSocket socket = new DatagramSocket();
		serverUDPAddress = new InetSocketAddress(host, port);
		udpChannel = new UDPChannel(messageService, socket);
		channelService.addChannel(udpChannel);
	}

	@Command
	@Override
	public String login(String username, String password) throws IOException {
		IMessage msg = new LoginMessage(username, password);
		LoginResponse response = (LoginResponse) serverChannel.sendAndWait(msg);

		if (response == null) {
			return "timeout occurred; response not received";
		}
		return response.getMessage();
	}

	@Command
	@Override
	public String logout() throws IOException {
		IMessage msg = new LogoutMessage();
		LogoutResponse response = (LogoutResponse) serverChannel.sendAndWait(msg);

		if (response == null) {
			return "timeout occurred; response not received";
		}
		return response.getMessage();
	}

	@Command
	@Override
	public String send(String message) throws IOException {
		IMessage msg = new SendMessage(message);
		SendResponse response = (SendResponse) serverChannel.sendAndWait(msg);

		if (response == null) {
			return "timeout occurred; response not received";
		}
		if (!response.isSuccessful()) {
			return response.getMessage();
		}
		return null;
	}

	@Command
	@Override
	public String list() throws IOException {
		UDPMessage msg = new ListMessage();
		msg.setSocketAddress(serverUDPAddress);
		ListResponse response = (ListResponse) udpChannel.sendAndWait(msg);

		if (response == null) {
			return "timeout occurred; response not received";
		}

		String output = "Online users: \n";
		for (String user : response.getOnlineUsers()) {
			output += "* " + user + "\n";
		}
		return output;
	}

	@Command
	@Override
	public String msg(String username, String message) throws IOException {
		PrivateMessage privateMessage = new PrivateMessage(username, message);

		// do lookup
		LookupMessage lookupMessage = new LookupMessage(username);
		LookupResponse response = (LookupResponse) serverChannel.sendAndWait(lookupMessage);

		if (response == null || !response.isSuccessful()) {
			return "Wrong username or user not reachable.";
		}

		// connect to client
		Socket socket = new Socket(response.getHost(), response.getPort());
		IChannel privateChannel = new TCPChannel(socket, messageService);
		channelService.addChannel(privateChannel);

		// send private message and wait for acknowledge
		AckResponse acknowledge = (AckResponse) privateChannel.sendAndWait(privateMessage);

		if (acknowledge == null) {
			return "timeout ocurred; !ack not received";
		}

		// close connection
		channelService.closeChannel(privateChannel);
		return "'" + username +"' replied with !ack.";
	}

	@Command
	@Override
	public String lookup(String username) throws IOException {
		IMessage msg = new LookupMessage(username);
		LookupResponse response = (LookupResponse) serverChannel.sendAndWait(msg);

		if (response == null) {
			return "* timeout occurred; response not received *";
		}

		return response.getHost() + ":" + response.getPort();
	}

	@Command
	@Override
	public String register(String privateAddress) throws IOException {
		String[] input = privateAddress.split(":");
		RegisterMessage msg = new RegisterMessage(input[0], Integer.valueOf(input[1]));
		RegisterResponse response = (RegisterResponse) serverChannel.sendAndWait(msg);

		if (response == null) {
			return "timeout ocurred; response not received";
		}

		if (response.isSuccessful()) {
			// start tcp server and wait for incoming messages
			try {
				ServerSocket serverSocket = new ServerSocket(msg.getPort());
				dispatcher = new Dispatcher(channelService, messageService, serverSocket);
				executorService.execute(dispatcher);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "error creating server socket (TCP)");
				e.printStackTrace();
			}
		}

		return response.getMessage();
	}

	@Command
	@Override
	public String lastMsg() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String exit() throws IOException {
		LOGGER.info("shutdown client");

		// streams
		userResponseStream.close();
		try {
			userRequestStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// threads / sockets
		if (dispatcher != null) {
			dispatcher.stop();
		}

		channelService.closeAll();
		shell.close();
		executorService.shutdown();
		return null;
	}

	/**
	 * @param args
	 *            the first argument is the name of the {@link Client} component
	 */
	public static void main(String[] args) {
		Client client = new Client(args[0], new Config("client"), System.in,
				System.out);
		client.run();
	}

	// --- Commands needed for Lab 2. Please note that you do not have to
	// implement them for the first submission. ---

	@Override
	public String authenticate(String username) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
