import java.io.InputStream;
import javax.microedition.io.StreamConnection;

public class BTConnectionProcessor implements Runnable
{

	private StreamConnection threadConnection;
	/**
	 * The constructor requires a given stream connection.
	 * @param connection
	 */
	
	public BTConnectionProcessor(StreamConnection connection)
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
			while (true) 
			{
				//System.out.println(inputStream.read());
				StringBuffer bufarray = new StringBuffer();
				byte[] buffer = new byte[8];
				for(int i=0; i<8; i++){
					
					bufarray.append((char)inputStream.read());
				}
				//buffer = bufarray.toString();
				
				System.out.println(bufarray);
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//private StreamConnection threadConnection;
	
}
