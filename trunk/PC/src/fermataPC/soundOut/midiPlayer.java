package fermataPC.soundOut;

import java.util.HashSet;

import com.jsyn.instruments.ClassicSynthVoice;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.jsyn.util.VoiceFactory;

import fermataPC.filtering.FilterProcessor;

public final class midiPlayer
{
	private static VoiceAllocator vAllocator;
	private static VoiceFactory vFactory;
	//private static LineOut lineOut;
	
	private static midiPlayer self;
	
	private static boolean sustained;
	
	private static HashSet<Integer> noteOffQueue;
	private static HashSet<Integer> activeNotes;
	
	public midiPlayer()
	{
		if (self == null)
		{			
			vAllocator = new VoiceAllocator(10);
			vFactory = new CustomVoiceFactory();
			vAllocator.setVoiceFactory(vFactory);
			
			noteOffQueue = new HashSet<Integer>();
			activeNotes = new HashSet<Integer>();
			
			SineOscillator sin = new SineOscillator();
			
			FilterProcessor.synth.add(sin);
		}	
	}
	
	public static final void noteOn(int note, int velocity)
	{
		UnitVoice voice = vAllocator.allocate(note);
		double amplitude = velocity / 127.0;
		//System.out.println("Amplitude: " + amplitude);
		voice.noteOn(FilterProcessor.synth.createTimeStamp(),
				pitchToFreq(note),
				amplitude);
		activeNotes.add(note);
		if (noteOffQueue.contains(note))
			noteOffQueue.remove(note);
	}
	
	public static final void noteOff(int note, int velocity)
	{
		if (sustained)
		{
			noteOffQueue.add(note);
			return;
		}
		activeNotes.remove(note);
		UnitVoice voice = vAllocator.off(note);
		if (voice != null)
		{
			voice.noteOff(FilterProcessor.synth.createTimeStamp());
		}
	}
	
	public static final void silence()
	{
		for (Integer note : activeNotes)
		{
			midiPlayer.noteOff(note, 127);
		}
	}
	
	public static final void sustainOn()
	{
		sustained = true;
	}
	
	public static final void sustainOff()
	{
		sustained = false;
		for (Integer note : noteOffQueue)
		{
			midiPlayer.noteOff(note, 127);
		}
	}
	
	private static double pitchToFreq(double pitch)
	{
		return 440.0 * Math.pow(2.0, ((pitch - 69) * (1.0 / 12.0)) );
	}
	
	private class CustomVoiceFactory implements VoiceFactory
	{
		@Override
		public UnitVoice createVoice(int note)
		{
			ClassicSynthVoice voice = new ClassicSynthVoice();
			FilterProcessor.synth.add(voice);
			
			FilterProcessor.connectOutput(voice.getOutput());
			return voice;
		}
		
	}
}
