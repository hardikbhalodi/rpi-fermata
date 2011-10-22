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
	}	

}
