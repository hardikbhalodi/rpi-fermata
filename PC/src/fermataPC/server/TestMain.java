package fermataPC.server;


public class TestMain 
{
	public static void main(String[] args)
	{
		ServerManager theServer = new ServerManager(9876, "Dicks, wangs, bigger dicks, larger wangs");
		
		while(true)
		{
			String temp = theServer.getTopInbound();
			if(temp != null)
				System.out.println(temp);
		}
	}
}