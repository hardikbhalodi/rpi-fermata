package fermataMIDI;

import java.util.Arrays;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiDevice.Info;

public final class MidiSweeper implements Runnable
{
	
	private MidiHandler mh;
	private Vector<Info> midiInfo;
	
	public MidiSweeper(MidiHandler mh)
	{
		this.mh = mh;
		
		Thread t = new Thread(this);
		
		t.start();
	}
	
	@Override
	public void run()
	{
		Vector<MidiDevice> tempDev = new Vector<MidiDevice>();
		for(;;)
		{
			midiInfo = new Vector<Info>(Arrays.asList(MidiSystem.getMidiDeviceInfo()));
			for (Info i: midiInfo)
			{			
				MidiDevice md;
				try
				{
					md = MidiSystem.getMidiDevice(i);
					if (md.getMaxTransmitters() != 0 && !md.getDeviceInfo().getName().equals("Real Time Sequencer"))
					{
						tempDev.add(md);
					}
				}
				catch (MidiUnavailableException e)
				{
					e.printStackTrace();
				}
			}
			
			mh.updateDeviceList(tempDev);
			tempDev.clear();
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}