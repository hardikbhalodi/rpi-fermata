package fermataPC.sound;

import java.util.HashSet;

import com.jsyn.instruments.ClassicSynthVoice;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.jsyn.util.VoiceFactory;

import fermataPC.filtering.FilterProcessor;

public final class MidiPlayer
{
	private VoiceAllocator vAllocator;
	private VoiceFactory vFactory;
	
	private boolean sustained;
	
	private HashSet<Integer> noteOffQueue;
	private HashSet<Integer> activeNotes;
	
	private static final MidiPlayer self = new MidiPlayer();
	
	private MidiPlayer()
	{
			vAllocator = new VoiceAllocator(10);
			vFactory = new CustomVoiceFactory();
			vAllocator.setVoiceFactory(vFactory);
			
			noteOffQueue = new HashSet<Integer>();
			activeNotes = new HashSet<Integer>();
			
			SineOscillator sin = new SineOscillator();
			
			FilterProcessor.synth.add(sin);
	}
	
	public static MidiPlayer getMidiPlayer()
	{
		return self;
	}
	
	public final void noteOn(int note, int velocity)
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
	
	public final void noteOff(int note, int velocity)
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
	
	public final void silence()
	{
		for (Integer note : activeNotes)
		{
			noteOff(note, 127);
		}
	}
	
	public final void sustainOn()
	{
		sustained = true;
	}
	
	public final void sustainOff()
	{
		sustained = false;
		for (Integer note : noteOffQueue)
		{
			noteOff(note, 127);
		}
	}
	
	private double pitchToFreq(double pitch)
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
			
			FilterProcessor.getFilterProcessor().connectOutput(voice.getOutput());
			return voice;
		}
		
	}
}
