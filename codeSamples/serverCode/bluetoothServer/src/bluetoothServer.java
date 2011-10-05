/**
 * A class to implement a threaded bluetoothServer
 * @author Patrick Phipps
 *
 */

public class bluetoothServer {

	public bluetoothServer()
	{
		Thread connectionHandler = new Thread(new connectionHandler());
		connectionHandler.start();
	}

}
