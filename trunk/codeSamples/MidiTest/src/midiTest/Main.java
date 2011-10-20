package midiTest;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;

public class Main
{	
	public static Vector<MidiDevice> validDevices;
	public static MidiDevice activeDev;
	public static Transmitter activeTransmit;
	public static Vector<Info> midiInfo;
	public static midireceiver mr;
	
	public static void main(String[] args)
	{
		midiInfo = new Vector<Info>(Arrays.asList(MidiSystem.getMidiDeviceInfo()));
		Transmitter activeTransmit = null;
		
		mr = new midireceiver();
		
		validDevices = new Vector<MidiDevice>();
		
		Thread scanThread = new Thread(new midiSweeper());

		scanThread.start();
		
		Scanner in = new Scanner(System.in);
		
		while (!in.nextLine().toLowerCase().equals("quit"))
		{
			System.out.println("To quit, enter 'quit'");
		}
		if (activeTransmit != null)
			activeTransmit.close();
		System.exit(0);
	}
}

class midireceiver implements Receiver
{
	@Override
	public void close()
	{
		System.out.println("Closed?");
	}

	@Override
	public void send(MidiMessage message , long timeStamp)
	{
		System.out.println("Message begin: (Stamp: " + timeStamp + ")");
		
		for (Byte b : message.getMessage())
		{
			System.out.print("byte: " + b.byteValue() + " ");
		}
		System.out.print("\n");
	}	
}

class midiSweeper implements Runnable
{
	@Override
	public void run()
	{
		for(;;)
		{
			Vector<MidiDevice> tempDev = new Vector<MidiDevice>();
			Main.midiInfo = new Vector<Info>(Arrays.asList(MidiSystem.getMidiDeviceInfo()));
			for (Info i: Main.midiInfo)
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
			
			Main.validDevices = tempDev;
			
			
			if (Main.validDevices.size() > 0 && Main.activeDev != null)
			{
				if (Main.validDevices.get(0) != Main.activeDev)
				{
					System.out.println("Device changed");
					Main.activeTransmit.close();
					Main.activeDev.close();
					Main.activeDev = Main.validDevices.get(0);
					
					try
					{
						Main.activeTransmit = Main.activeDev.getTransmitter();
						Main.activeTransmit.setReceiver(Main.mr);
					}
					catch (MidiUnavailableException e)
					{
						e.printStackTrace();
					}
				}	
			}
			else if (Main.validDevices.size() > 0)
			{
				System.out.println("Device connected");
				Main.activeDev = Main.validDevices.get(0);
				
				try
				{
					Main.activeTransmit = Main.activeDev.getTransmitter();
					Main.activeTransmit.setReceiver(Main.mr);
					Main.activeDev.open();
				}
				catch (MidiUnavailableException e)
				{
					e.printStackTrace();
				}
			}
			else if(Main.activeDev != null)
			{
				System.out.println("Device disconnected");
				Main.activeTransmit.close();
				Main.activeDev.close();
				Main.activeDev = null;
			}
			
			
			
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