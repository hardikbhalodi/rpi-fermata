package midiTest;

import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;

public class Main
{	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Info[] midiInfo = MidiSystem.getMidiDeviceInfo();
		
		ArrayList<MidiDevice> validDevices = new ArrayList<MidiDevice>();
		
		for (Info i: midiInfo)
		{			
			MidiDevice md;
			
			try
			{
				md = MidiSystem.getMidiDevice(i);
				if (md.getMaxTransmitters() != 0 && !md.getDeviceInfo().getName().equals("Real Time Sequencer"))
				{
					validDevices.add(md);
				}
			}
			catch (MidiUnavailableException e)
			{
				e.printStackTrace();
			}
		}	
		
		for (MidiDevice md : validDevices)
		{
			Info i = md.getDeviceInfo();
			
			System.out.println("Name: " + i.getName());
			System.out.println("Description " + i.getDescription());
			System.out.println("Vendor: " + i.getVendor());
			System.out.println("Version: " + i.getVersion());
			
			System.out.println("Transmitters: " + md.getMaxTransmitters() + "\n\n");
		}

		MidiDevice md = validDevices.get(0);
		
		Transmitter tm = null;
		try
		{
			tm = md.getTransmitter();
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
			System.out.println("Transmitter get fail");
		}
		try
		{
			md.open();
		}
		catch (MidiUnavailableException e)
		{
			System.out.println("md open fail");
			e.printStackTrace();
		}
		tm.setReceiver(new midireceiver());
		

		Scanner in = new Scanner(System.in);
		
		while (!in.nextLine().toLowerCase().equals("quit") && !in.nextLine().toLowerCase().equals("q"))
		{
			System.out.println("To quit, enter 'quit' or 'q'");
		}
		tm.close();
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