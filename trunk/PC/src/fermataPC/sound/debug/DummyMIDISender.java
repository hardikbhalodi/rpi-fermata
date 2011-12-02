package fermataPC.sound.debug;

import fermataPC.sound.MidiPlayer;

public class DummyMIDISender implements Runnable
{
	public DummyMIDISender()
	{
		Thread t = new Thread(this);
		
		t.start();
	}

	@Override
	public void run()
	{
		try{
			for(;;)
			{
				System.out.println("note on");
				MidiPlayer.noteOn(69 , 63);
				
				Thread.sleep(4000);
				System.out.println("note off");
				MidiPlayer.noteOff(69, 63);
				
				Thread.sleep(4000);
				
				System.out.println("note on 2");
				
				MidiPlayer.noteOn(69 , 63);
				
				Thread.sleep(10000);
				System.out.println("note off 2");
				MidiPlayer.noteOff(69 , 63);
				
				Thread.sleep(10000);
			}
		}
		catch (Exception e)
		{
			System.out.println("Dummy stopped working");
			e.printStackTrace();
		}
	}
	
	
	
	
}