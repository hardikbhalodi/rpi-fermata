package fermataMIDI;

import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import fermataUI.MidiDeviceBox;

public abstract class MidiHandler
{
	private static Vector<MidiDevice> validDevices;
	private static MidiDevice activeDev;
	private static Transmitter activeTransmit;
	
	private static MidiReceiver mr;
	@SuppressWarnings("unused")
	private static MidiSweeper ms;
	private static MidiDeviceBox mdb;
	
	private static Boolean started = false;
	
	public static void startMIDIService()
	{
		if (started)
			return;
		System.out.println("Midihandler is being constructed even though that's literally impossible");
		
		validDevices = new Vector<MidiDevice>();		
		
		mr = new MidiReceiver();
		
		ms = new MidiSweeper();
		
		started = true;
	}
	
	@SuppressWarnings("unchecked")
	public static void updateDeviceList(Vector<MidiDevice> deviceList)
	{
		if (!validDevices.equals(deviceList))
		{
			System.out.println("Updating midi device selection box");
			if (mdb != null)
				mdb.updateList(deviceList);
			validDevices = (Vector<MidiDevice>) deviceList.clone();
		}
	}
	
	public static void setDeviceBox(MidiDeviceBox mdb)
	{
		MidiHandler.mdb = mdb;
		mdb.updateList(validDevices);
	}
	
	public static void setActiveDevice(MidiDevice md)
	{
		System.out.println("Setting active device");
		if (md == null)
		{
			if (activeDev != null)
			{
				activeDev.close();
				mr.close();
				activeDev = null;
			}
			return;
		}
		
		if (activeDev == null)
		{
			activeDev = md;
			try
			{
				activeTransmit = md.getTransmitter();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Transmitter activation fail");
				e.printStackTrace();
			}
			activeTransmit.setReceiver(mr);
			try
			{
				activeDev.open();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Could not open device");
				e.printStackTrace();
			}
		}
		else
		{
			activeDev.close();
			mr.close();
			
			activeDev = md;
			try
			{
				activeTransmit = md.getTransmitter();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Transmitter activation fail");
				e.printStackTrace();
			}
			activeTransmit.setReceiver(mr);
			try
			{
				activeDev.open();
			}
			catch (MidiUnavailableException e)
			{
				System.out.println("Could not open device");
				e.printStackTrace();
			}
		}
	}
}
