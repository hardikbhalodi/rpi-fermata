package fermataPC.server.bluetooth;


/**
 * A class to implement a threaded bluetoothServer
 * @author Patrick Phipps
 *
 */

public class BluetoothServer
{
	public BluetoothServer(String[] inbound, String[] outbound, String greetingMessage)
	{
		Thread connectionHandler = new Thread(new BTConnectionHandler(inbound, outbound, greetingMessage));
		connectionHandler.start();
	}
}
