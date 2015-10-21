package chatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import cli.Command;
import cli.Shell;
import domain.Dispatcher;
import domain.IChannel;
import domain.UDPChannel;
import executors.IMessageExecutorFactory;
import executors.ServerMessageExecutorFactory;
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
	private IChannelService channelService;
	private Dispatcher dispatcher;
	private IMessageService messageService;
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
		channelService = new ChannelService(executorService);
		messageExecutorFactory = new ServerMessageExecutorFactory(channelService, userService);
		messageService = new MessageService(messageExecutorFactory, executorService);

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
		dispatcher = new Dispatcher(channelService, messageService, serverSocket);
	}

	private void setupUDP(int port) {
		try {
			DatagramSocket socket = new DatagramSocket(port);
			IChannel udpChannel = new UDPChannel(messageService, socket);
			channelService.addChannel(udpChannel);
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

	@Command
	@Override
	public String users() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String exit() throws IOException {
		// TODO Auto-generated method stub
		return null;
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
