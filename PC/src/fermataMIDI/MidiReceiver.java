package fermataMIDI;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public final class MidiReceiver implements Receiver
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
		
		parseMessage(message);
	}
	
	public void parseMessage(MidiMessage message)
	{
		byte[] bitRay = message.getMessage();
		
		Byte status = bitRay[0];
		Byte data1 = bitRay[1];
		Byte data2 = bitRay[2];
		
		Byte command = (byte) (status >>> 4);
		Byte chan = (byte) ((status << 4) >>> 4);
		
		switch (command)
		{
		case 8:
			System.out.println("Note off:" + data1);
		case 9:
			System.out.println("Note on: " + data1 + "; Velocity: " + data2);
		case 10:
			System.out.println("Aftertouch? Pfft.");
		case 11: 
			System.out.println("Channel property change.");
		case 13:
			System.out.println("Channel aftertouch.");
		case 14:
			System.out.println("Pitch wheel change");
		default:
			System.out.println("What does this instruction mean?");
		}		
	}
}
