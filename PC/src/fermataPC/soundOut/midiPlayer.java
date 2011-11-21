package fermataPC.soundOut;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.instruments.ClassicSynthVoice;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.PowerOfTwo;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.jsyn.util.VoiceFactory;

public final class midiPlayer
{
	private static Synthesizer synth;
	private static VoiceAllocator vAllocator;
	private static VoiceFactory vFactory;
	private static LineOut lineOut;
	private static PowerOfTwo powerOfTwo;
	
	private static midiPlayer self;
	
	private static boolean sustained;
	
	public static final void noteOn(int note, int velocity)
	{
		UnitVoice voice = vAllocator.allocate(note);
		double amplitude = velocity / (512.0);
		voice.noteOn(synth.createTimeStamp(),
				pitchToFreq(note),
				amplitude);
	}
	
	
	public midiPlayer()
	{
		if (self == null)
		{
			synth = JSyn.createSynthesizer();
			
			synth.add(lineOut = new LineOut());
			
			synth.add(powerOfTwo = new PowerOfTwo() );
			
			vAllocator = new VoiceAllocator(10);
			
			vFactory = new CustomVoiceFactory();
			
			vAllocator.setVoiceFactory(vFactory);
			
			
			synth.start();
			
			double timeNow = synth.getCurrentTime();
			
			double time = timeNow + .5;
			
			synth.startUnit(lineOut, time);
		}		
	}
	
	public static final void noteOff(int note, int velocity)
	{
		if (sustained)
			return;
		
		UnitVoice voice = vAllocator.off(note);
		if (voice != null)
		{
			voice.noteOff(synth.createTimeStamp());
		}
	}
	
	public static final void sustainOn()
	{
		sustained = true;
	}
	
	public static final void sustainOff()
	{
		sustained = false;
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
			synth.add(voice);
			
			voice.getOutput().connect(0, lineOut.input, 0);
			voice.getOutput().connect(0, lineOut.input, 1);
			
			return voice;
		}
		
	}
}
