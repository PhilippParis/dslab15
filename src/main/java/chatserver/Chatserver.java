package chatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import channels.OnCloseListener;
import cli.Command;
import cli.Shell;
import channels.Dispatcher;
import channels.IChannel;
import channels.UDPChannel;
import domain.User;
import executors.IMessageExecutorFactory;
import service.*;
import util.Config;

public class Chatserver implements IChatserverCli, Runnable {
	private final static Logger LOGGER = Logger.getLogger(Chatserver.class.getName());

	private String componentName;
	private Config config;
	private InputStream userRequestStream;
	private PrintStream userResponseStream;
	private Shell shell;

	// threading
	private ExecutorService executorService = Executors.newCachedThreadPool();

	// services
	private IUserService userService;
	private IConnectionService connectionService;
	private Dispatcher dispatcher;
	private IMessageExecutorFactory messageExecutorFactory;

	// connection
	private ServerSocket serverSocket;


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
	public Chatserver(String componentName, Config config,
			InputStream userRequestStream, PrintStream userResponseStream) {
		this.componentName = componentName;
		this.config = config;
		this.userRequestStream = userRequestStream;
		this.userResponseStream = userResponseStream;

		// setup services
		userService = new UserService(new Config("user"));
		connectionService = new ConnectionService(executorService, messageExecutorFactory);
		connectionService.setOnChannelCloseListener(new OnCloseListener() {
			@Override
			public void onClose(IChannel channel) {
				userService.logoutUser(userService.getUser(channel));
			}
		});

		messageExecutorFactory = new ServerMessageExecutorFactory(userService);

		// setup shell
		shell = new Shell(componentName, userRequestStream, userResponseStream);
		shell.register(this);

		setupTCPServer(config.getInt("tcp.port"));
		setupUDP(config.getInt("udp.port"));
	}

	public void setupTCPServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "error creating server socket (TCP)");
			e.printStackTrace();
		}

		// create dispatcher
		dispatcher = new Dispatcher(connectionService, serverSocket);
	}

	private void setupUDP(int port) {
		try {
			DatagramSocket socket = new DatagramSocket(port);
			IChannel udpChannel = new UDPChannel(connectionService, socket);
			connectionService.addChannel(udpChannel);
		} catch (SocketException e) {
			LOGGER.log(Level.INFO, "setting up udp socket failed");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		executorService.execute(shell);
		executorService.execute(dispatcher);
	}

	/**
	 * shuts down the server: stops threads and closes open sockets
	 */
	private void shutdown() {
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
		executorService.shutdown();
		shell.close();
	}

	@Command
	@Override
	public String users() throws IOException {
		String output = "Online Users:\n";
		for (User user : userService.getAllUsers()) {
			output += user.username() + " " + (user.isLoggedIn()? "online" : "offline") + "\n";
		}
		return  output;
	}

	@Command
	@Override
	public String exit() throws IOException {
		shutdown();
		return "exiting...";
	}

	/**
	 * @param args
	 *            the first argument is the name of the {@link Chatserver}
	 *            component
	 */
	public static void main(String[] args) {
		Chatserver chatserver = new Chatserver(args[0],
				new Config("chatserver"), System.in, System.out);
		chatserver.run();
	}

}
