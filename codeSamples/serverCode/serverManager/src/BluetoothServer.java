import java.util.concurrent.BlockingQueue;

/**
 * A class to implement a threaded bluetoothServer
 * @author Patrick Phipps
 *
 */

public class BluetoothServer
{

	public BluetoothServer(BlockingQueue<String> inbound, BlockingQueue<String> outbound, String greetingMessage)
	{
		Thread connectionHandler = new Thread(new BTConnectionHandler(inbound, outbound, greetingMessage));
		connectionHandler.start();
	}

}
