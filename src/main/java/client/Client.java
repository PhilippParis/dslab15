package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import channels.Dispatcher;
import channels.IChannel;
import channels.TCPChannel;
import channels.UDPChannel;
import cli.Command;
import cli.Shell;
import domain.*;
import domain.messages.*;
import domain.responses.*;
import executors.IMessageExecutorFactory;
import service.ConnectionService;
import service.IConnectionService;
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
	private IConnectionService connectionService;
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
		connectionService = new ConnectionService(executorService, messageExecutorFactory);
		messageExecutorFactory = new ClientMessageExecutorFactory(userResponseStream, connectionService);

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
		serverChannel = new TCPChannel(socket, connectionService);
		connectionService.addChannel(serverChannel);
	}

	private void setupUDP(String host, int port) throws SocketException {
		DatagramSocket socket = new DatagramSocket();
		serverUDPAddress = new InetSocketAddress(host, port);
		udpChannel = new UDPChannel(connectionService, socket);
		connectionService.addChannel(udpChannel);
	}

	@Command
	@Override
	public String login(String username, String password) throws IOException {
		IMessage msg = new LoginMessage(username, password);

		try {
			LoginResponse response = (LoginResponse) connectionService.sendAndWait(msg, serverChannel);
			return response.getMessage();
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "Unexpected response received";
		}
	}

	@Command
	@Override
	public String logout() throws IOException {
		IMessage msg = new LogoutMessage();

		try {
			LogoutResponse response = (LogoutResponse) connectionService.sendAndWait(msg, serverChannel);
			return response.getMessage();
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "Unexpected response received";
		}
	}

	@Command
	@Override
	public String send(String message) throws IOException {
		IMessage msg = new SendMessage(message);

		try {
			SendResponse response = (SendResponse) connectionService.sendAndWait(msg, serverChannel);
			if (!response.isSuccessful()) {
				return response.getMessage();
			}
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "unexpected response received";
		}

		return null;
	}

	@Command
	@Override
	public String list() throws IOException {
		UDPMessage msg = new ListMessage();
		msg.setSocketAddress(serverUDPAddress);
		ListResponse response = null;

		try {
			response = (ListResponse) connectionService.sendAndWait(msg, udpChannel);
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "unexpected response received";
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
		LookupResponse response = null;
		try {
			response = (LookupResponse) connectionService.sendAndWait(lookupMessage, serverChannel);
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "unexpected response received";
		}

		if (!response.isSuccessful()) {
			return "Wrong username or user not reachable.";
		}

		// connect to client
		Socket socket = new Socket(response.getHost(), response.getPort());
		IChannel privateChannel = new TCPChannel(socket, connectionService);
		connectionService.addChannel(privateChannel);

		// send private message and wait for acknowledge
		try {
			connectionService.sendAndWait(privateMessage, privateChannel);
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "unexpected response received";
		}

		// close connection
		connectionService.closeChannel(privateChannel);
		return "'" + username +"' replied with !ack.";
	}

	@Command
	@Override
	public String lookup(String username) throws IOException {
		IMessage msg = new LookupMessage(username);
		LookupResponse response = null;

		try {
			response = (LookupResponse) connectionService.sendAndWait(msg, serverChannel);
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "unexpected response received";
		}

		return response.getHost() + ":" + response.getPort();
	}

	@Command
	@Override
	public String register(String privateAddress) throws IOException {
		String[] input = privateAddress.split(":");
		RegisterMessage msg = new RegisterMessage(input[0], Integer.valueOf(input[1]));
		RegisterResponse response = null;

		try {
			response = (RegisterResponse) connectionService.sendAndWait(msg, serverChannel);
		} catch (TimeoutException e) {
			return "timeout occurred: Server not reachable";
		} catch (ClassCastException e) {
			return "unexpected response received";
		}

		if (response.isSuccessful()) {
			// start tcp server and wait for incoming messages
			try {
				ServerSocket serverSocket = new ServerSocket(msg.getPort());
				dispatcher = new Dispatcher(connectionService, serverSocket);
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

		connectionService.closeAll();
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
