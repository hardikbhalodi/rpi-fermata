package fermataPC.server;

import fermataPC.filtering.FilterHandler;
import fermataPC.util.Coordinate;

public class ServerScanner implements Runnable
{
	private ServerManager theServer;
	
	static int UID1, UID2, val1, val2;
	
	
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
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
		}
	}
	
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

			Coordinate c1 = new Coordinate(newUID1, scalar1);
			FilterHandler.applyCoordinate(c1);
		}
		
		if (UID2 != newUID2 || scalar2 != val2)
		{
			Coordinate c2 = new Coordinate(newUID2, scalar2);
			FilterHandler.applyCoordinate(c2);
		}
	//	System.out.println("UID1: " + UID1 + "; scalar: " + scalar1 + ";UID2: " + UID2 + "; scalar: " + scalar2);
	}
}
