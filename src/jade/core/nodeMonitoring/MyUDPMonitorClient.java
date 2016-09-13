package jade.core.nodeMonitoring;

import java.io.IOException;
//#DOTNET_EXCLUDE_BEGIN
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import jade.util.Logger;

/**
 * An adjustment of the UDPMonitorClient in order to use it in test code
 * 
 * @author bzafiris
 *
 */
public class MyUDPMonitorClient {

	/**
	 * The <code>UDPMonitorClient</code> sends UDP ping messages in a specified
	 * interval to the main container.
	 *
	 * @author Roland Mungenast - Profactor
	 * @since JADE 3.3
	 */

	private boolean running = false;
	private boolean terminating = false;
	private boolean sendTerminationFlag = false;

	// #DOTNET_EXCLUDE_BEGIN
	private DatagramChannel channel;

	// #DOTNET_EXCLUDE_END

	/*
	 * #DOTNET_INCLUDE_BEGIN private UdpClient channel; private IPEndPoint
	 * receivePoint; #DOTNET_INCLUDE_END
	 */

	private String serverHost;
	private int serverPort;
	private ByteBuffer ping;
	private int pingDelay;
	private long key;
	private Thread sender;

	private Logger logger;

	/**
	 * Private class which sends ping messages in regular time intervals
	 * 
	 * @author Roland Mungenast - Profactor
	 * @since JADE 3.3
	 * @author Federico Pieri - ERXA
	 * @since JADE 3.3.NET
	 */
	private class Sender implements Runnable {

		public void run() {

			while (running) {
				updatePing();
				// #DOTNET_EXCLUDE_BEGIN
				try {
					try {
						channel.send(ping, new InetSocketAddress(serverHost, serverPort));
					} catch (IOException e) {
						logger.log(Logger.INFO, "Error sending UDP ping message to " + serverHost + ":" + serverPort);
						throw new PingFailedError();
					}
					Thread.sleep(pingDelay - 5);
				} catch (InterruptedException e) {
					// ignore --> the ping with the termination flag has to
					// be sent immediately
				}
				// #DOTNET_EXCLUDE_END
				/*
				 * #DOTNET_INCLUDE_BEGIN try { channel.Send(ping.getUByte(),
				 * ping.capacity(), serverHost, serverPort);
				 * Thread.sleep(pingDelay - 5); } catch (Exception e) {
				 * logger.log(Logger.WARNING,
				 * "Error sending UDP ping message to "
				 * +serverHost+":"+serverPort+" for node " + node.getName()); }
				 * #DOTNET_INCLUDE_END
				 */
			}

			try {
				// #DOTNET_EXCLUDE_BEGIN
				channel.close();
			} catch (IOException e) {
				// #DOTNET_EXCLUDE_END
				/*
				 * #DOTNET_INCLUDE_BEGIN channel.Close(); } catch (Exception e)
				 * { #DOTNET_INCLUDE_END
				 */
				if (logger.isLoggable(Logger.FINER))
					logger.log(Logger.FINER, "Error closing UDP channel");
			}
		}

		private void updatePing() {
			String nodeName = "mynode";
			ping = ByteBuffer.allocate(4 + nodeName.length() + 1);
			ping.position(0);
			ping.putInt(nodeName.length());
			ping.put(nodeName.getBytes());

			if (terminating && sendTerminationFlag) {
				ping.put((byte) 1);
			} else {
				ping.put((byte) 0);
			}

			if (terminating) {
				running = false;
			}

			ping.position(0);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param node
	 *            Node for which to send ping messages
	 * @param serverHost
	 *            hostname of the server
	 * @param serverPort
	 *            port on which the server is listening for ping messages
	 */
	public MyUDPMonitorClient(String serverHost, int serverPort, int pingDelay, long key) {
		logger = Logger.getMyLogger(this.getClass().getName());

		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.pingDelay = pingDelay;
		this.key = key;
	}

	public String getServerHost() {
		return serverHost;
	}

	public long getKey() {
		return key;
	}

	void setPingDelay(int delay) {
		pingDelay = delay;
		sender.interrupt();
	}

	/**
	 * Start sending UDP ping messages to the node failure server
	 * 
	 * @throws IOException
	 *             if the
	 */
	public void start() throws IOException {
		// #DOTNET_EXCLUDE_BEGIN
		channel = DatagramChannel.open();
		// #DOTNET_EXCLUDE_END
		/*
		 * #DOTNET_INCLUDE_BEGIN channel = new UdpClient(); #DOTNET_INCLUDE_END
		 */
		running = true;
		sender = new Thread(new Sender());
		sender.start();

		if (logger.isLoggable(Logger.CONFIG))
			logger.log(Logger.CONFIG, "UDP monitoring client started.");
	}

	/**
	 * Stop sending UDP ping messages
	 */
	public void stop(boolean sendTerminationFlag) {
		terminating = true;
		this.sendTerminationFlag = sendTerminationFlag;
		sender.interrupt();

		if (logger.isLoggable(Logger.CONFIG))
			logger.log(Logger.CONFIG, "UDP monitoring client stopped.");

	}

	boolean isActive() {
		return sender != null && sender.isAlive();
	}

}
