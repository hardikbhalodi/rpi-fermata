package fermataPC.server.bluetooth;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;


public class BTConnectionHandler implements Runnable
{
	
	private String[] inbound;
	private String[] outbound;
	private String greetingMessage;
	
	/**
	 * Default constructor does not do anything 
	 */
	public BTConnectionHandler(String[] inbound2, String[] outbound2, String greetingMessage)
	{
		this.inbound = inbound2;
		this.outbound = outbound2;
		this.greetingMessage = greetingMessage;
	}
	
	/**
	 * The thread runnable function starts the thread waiting for a connection
	 */
	@Override
	public void run() 
	{
		awaitConnection();
	}
	
	/**
	 * Wait for and receive a connection from a bluetooth device
	 */
	private void awaitConnection()
	{
		// retrieve the local Bluetooth device object
		LocalDevice local = null;

		StreamConnectionNotifier notifier;
		StreamConnection connection = null;

		// setup the server to listen for connection
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);

			UUID uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
			String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
			notifier = (StreamConnectionNotifier)Connector.open(url);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
       	       	// waiting for connection
		while(true) {
			try {
				System.out.println("waiting for connection...");
	                  	connection = notifier.acceptAndOpen();

	             System.out.println("connection established.");
	                  	
				Thread connectionProcessor = new Thread(new BTConnectionProcessor(connection, inbound, outbound, greetingMessage));
				connectionProcessor.start();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
