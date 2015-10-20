package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import cli.Command;
import cli.Shell;
import domain.IChannel;
import domain.TCPChannel;
import service.ChannelService;
import service.IChannelService;
import util.Config;

public class Client implements IClientCli, Runnable {
	private final static Logger LOGGER = Logger.getLogger(Client.class.getName());

	private String componentName;
	private Config config;
	private InputStream userRequestStream;
	private PrintStream userResponseStream;

	// cli
	private Shell shell;

	// threading
	private ExecutorService executorService = Executors.newCachedThreadPool();

	// connection
	private IChannel serverChannel;

	// services
	private IChannelService channelService;

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

		// setup shell
		shell = new Shell(componentName, userRequestStream, userResponseStream);
		shell.register(this);
	}

	@Override
	public void run() {
		// start shell thread
		executorService.execute(shell);

		// connect to server
		connectToServer(config.getString("chatserver.host"), config.getInt("chatserver.tcp.port"));
	}

	private void connectToServer(String host, int port) {
		try {
			Socket socket = new Socket(host, port);
			serverChannel = new TCPChannel(socket);
			channelService.addChannel(serverChannel);
		} catch (IOException e) {
			LOGGER.log(Level.INFO, "could not connect to server");
			userResponseStream.println("could not connect to server");
			shutdown();
		}
	}

	/**
	 * shuts down the client: logs the user out, releases resources, closes open sockets
	 */
	private void shutdown() {
		// TODO
	}

	@Command
	@Override
	public String login(String username, String password) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String logout() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String send(String message) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String list() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String msg(String username, String message) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String lookup(String username) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Command
	@Override
	public String register(String privateAddress) throws IOException {
		// TODO Auto-generated method stub
		return null;
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
		shutdown();
		return "exiting";
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
