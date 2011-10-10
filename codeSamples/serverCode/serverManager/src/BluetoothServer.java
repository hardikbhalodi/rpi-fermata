import java.util.concurrent.BlockingQueue;

/**
 * A class to implement a threaded bluetoothServer
 * @author Patrick Phipps
 *
 */

public class BluetoothServer {

	public BluetoothServer(BlockingQueue<byte []> inbound, BlockingQueue<byte []> outbound)
	{
		Thread connectionHandler = new Thread(new BTConnectionHandler(inbound, outbound));
		connectionHandler.start();
	}

}
