package chatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import channels.OnCloseListener;
import cli.Command;
import cli.Shell;
import channels.Dispatcher;
import channels.IChannel;
import channels.UDPChannel;
import dao.ConfigUserDAO;
import dao.IUserDAO;
import domain.User;
import executors.IMessageExecutorFactory;
import service.*;
import util.Config;

public class Chatserver implements IChatserverCli, Runnable {
	private final static Logger LOGGER = Logger.getLogger(Chatserver.class.getName());
	private final Level globalLoggingLevel = Level.OFF;

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

	// dao
	private IUserDAO userDAO;

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

		// logging
		LOGGER.getParent().setLevel(globalLoggingLevel);

		//setup daos
		userDAO = new ConfigUserDAO(new Config("user"));

		// setup services
		userService = new UserService(userDAO);
		connectionService = new ConnectionService(executorService);
		messageExecutorFactory = new ServerMessageExecutorFactory(userService, connectionService);

		connectionService.setMessageExecutorFactory(messageExecutorFactory);
		connectionService.setOnChannelCloseListener(new OnCloseListener() {
			@Override
			public void onClose(IChannel channel) {
				userService.logoutUser(userService.getUser(channel));
			}
		});

		// setup shell
		shell = new Shell(componentName, userRequestStream, userResponseStream);
		shell.register(this);
	}

	public void setupTCPServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		dispatcher = new Dispatcher(connectionService, serverSocket);
	}

	private void setupUDP(int port) throws IOException {
		DatagramSocket socket = new DatagramSocket(port);
		IChannel udpChannel = new UDPChannel(connectionService, socket);
		connectionService.addChannel(udpChannel);
	}

	@Override
	public void run() {
		try {
			setupTCPServer(config.getInt("tcp.port"));
			setupUDP(config.getInt("udp.port"));

			executorService.execute(shell);
			executorService.execute(dispatcher);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "failed to setup tcp and udp sockets", e);
			try {
				shell.writeLine("failed to setup tcp and udp sockets: exiting...");
				exit();
			} catch (IOException e2) {
				LOGGER.log(Level.SEVERE, "failed to write to shell", e2);
			}
		}
	}

	@Command
	@Override
	public String users() throws IOException {
		String output = "Online Users:\n";
		List<User> users = new ArrayList<>(userService.getAllUsers());

		// sort users alphabetically
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.username().compareTo(o2.username());
			}
		});
		
		for (User user : users) {
			output += "* " + user.username() + " " + (user.isLoggedIn()? "online" : "offline") + "\n";
		}
		return  output;
	}

	@Command
	@Override
	public String exit() throws IOException {
		LOGGER.info("shutdown server");

		// disable new task from being submitted
		executorService.shutdown();

		// streams
		userResponseStream.close();
		try {
			userRequestStream.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "failed to close userRequestStream", e);
		}

		// threads / sockets
		if (dispatcher != null) {
			dispatcher.stop();
		}

		// close all connections
		connectionService.closeAll();

		// close shell
		shell.close();

		try {
			// wait for all task to terminate
			if(!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
				// cancel currently running task
				executorService.shutdownNow();
				// wait for task to be canceled
				if(!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
					System.err.println("ExecutorService did not terminate");
					LOGGER.log(Level.SEVERE, "ExecutorService did not terminate");
				}
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}

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
