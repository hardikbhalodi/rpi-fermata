import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;


public class connectionHandler implements Runnable{

	/**
	 * Default constructor does not do anything 
	 */
	public connectionHandler()
	{}
	
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

			UUID uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
			String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
			System.out.println(url);
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

				Thread connectionProcessor = new Thread(new connectionProcessor(connection));
				connectionProcessor.start();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
