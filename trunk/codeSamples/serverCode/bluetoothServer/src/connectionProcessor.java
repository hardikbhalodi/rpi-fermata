import java.io.InputStream;
import javax.microedition.io.StreamConnection;


public class connectionProcessor implements Runnable
{

	/**
	 * The constructor requires a given stream connection.
	 * @param connection
	 */
	public connectionProcessor(StreamConnection connection)
	{
		threadConnection = connection;
	}

	/**
	 * The thread runnable function receives 4 byte messages
	 */
	@Override
	public void run() 
	{
		try {
			// prepare to receive data
			InputStream inputStream = threadConnection.openInputStream();

			System.out.println("waiting for input");

			byte[] buffer = new byte[4];
			
			while (true) 
			{
				inputStream.read(buffer);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private StreamConnection threadConnection;

}
