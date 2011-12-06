package fermataPC.server;

import fermataPC.filtering.FilterHandler;
import fermataPC.util.Coordinate;

/**
 * The ServerScanner checks periodically the latest input from the phone
 * and parses it. If the coordinate or filter are new, ServerScanner notifies
 * FilterHandler with a Coordinate containing the new scalar and the UID of the
 * filter it applies to. It is a runnable in and of itself; and constructing it
 * starts it running. Running more than one will work, but be frivolous, so it
 * is best avoided. Instead, reduce the interval it checks at.
 * 
 * @author katzj2
 *
 */
public class ServerScanner implements Runnable
{
	/**
	 * The server manager to reference.
	 */
	private ServerManager theServer;
	
	/**
	 * The period which passes between checks.
	 */
	private static final int PERIOD = 10;
	
	static int UID1, UID2, val1, val2;
	
	
	/**
	 * Constructs the ServerScanner and begins to run it.
	 * @param theServer The ServerManager to pull messages from.
	 */
	public ServerScanner(ServerManager theServer)
	{
		this.theServer = theServer;
		
		Thread t = new Thread(this);
		
		t.start();
	}
	
	@Override
	public void run()
	{
		String temp;
		
		for(;;)
		{
			temp = theServer.getTopInbound();
			if (temp != null)
			{
				parseMessage(temp);
			}
			
			try
			{
				Thread.sleep(PERIOD);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Parses the string message into filter UIDS and scalars by the following
	 * format: UID1,SCALAR1;UID2,SCALAR2;
	 * @param message The message to parse.
	 */
	private static void parseMessage(String message)
	{
		String[] messages = message.split(";");
		String[] coord1 = messages[0].split(",");
		String[] coord2 = messages[1].split(",");
		
		Integer newUID1 = Integer.parseInt(coord1[0]);
		Integer newUID2 = Integer.parseInt(coord2[0]);
		
		Integer scalar1 = Math.max(0 , Math.min(255, Integer.parseInt(coord1[1])));
		Integer scalar2 = Math.max(0, Math.min(255, 255 - Integer.parseInt(coord2[1])));
		
		if (UID1 != newUID1 || scalar1 != val1)
		{
			UID1 = newUID1;
			val1 = scalar1;

			Coordinate c1 = new Coordinate(newUID1, scalar1);
			FilterHandler.getFilterHandler().applyCoordinate(c1);
		//	System.out.println("Update:\tUID1: " + UID1 + ";\tscalar: " + scalar1);
		}
		
		if (UID2 != newUID2 || scalar2 != val2)
		{
			UID2 = newUID2;
			val2 = scalar2;
			Coordinate c2 = new Coordinate(newUID2, scalar2);
			FilterHandler.getFilterHandler().applyCoordinate(c2);
		//	System.out.println("Update:\tUID2: " + UID2 + ";\tscalar: " + scalar2);
		}
	//	System.out.println("UID1: " + UID1 + "; scalar: " + scalar1 + ";UID2: " + UID2 + "; scalar: " + scalar2);
	}
}
