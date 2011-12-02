package fermataPC.soundOut;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;

import fermataPC.filtering.FilterProcessor;

/**
 * Supports playback of mono signed 16 bit PCM wav files
 * @author katzj2
 *
 */
public abstract class PlaySoundFile
{
	private static VariableRateDataReader samplePlayer;
	
	public static final void stop()
	{
		if (samplePlayer != null)
		{
			FilterProcessor.synth.stopUnit(samplePlayer);
			samplePlayer.output.disconnectAll(0);
			samplePlayer.dataQueue.clear();
			samplePlayer.stop();
		}
	}
	/**
	 * Attempts to play the file through the FilterProcessor. Success is only
	 * guaranteed when the file is a mono signed 16 bit PCM wav.
	 * @param audioFile the file to play.
	 */
	public static final void playStream(File audioFile)
	{
		FloatSample sample;
		try
		{	
			sample = SampleLoader.loadFloatSample(audioFile);
			
			boolean mono = true;
			
			System.out.println( "Sample has: " + sample.getChannelsPerFrame()
					+ " channels" );
			System.out.println( "            " + sample.getNumFrames()
					+ " frames" );
			
			if( sample.getChannelsPerFrame() == 1 )
			{
				FilterProcessor.synth.add( samplePlayer = new VariableRateMonoReader() );
			}
			else if( sample.getChannelsPerFrame() == 2 )
			{
				FilterProcessor.synth.add( samplePlayer = new VariableRateStereoReader() );
				mono = false;
			}
			else
			{
				System.out.println("Can only play mono or stereo samples.");
				return;
			}
			
			samplePlayer.rate.set(sample.getFrameRate());
			samplePlayer.dataQueue.queue(sample);
			
			if (mono)
			{
				FilterProcessor.connectOutput(samplePlayer.output);
			}
			else
			{
				samplePlayer.output.connect(0, FilterProcessor.lineOut.input, 0);
				samplePlayer.output.connect(1, FilterProcessor.lineOut.input, 1);
			}
		}
		catch (IOException e)
		{
			System.out.println("Can't read file.");
			e.printStackTrace();
		}
		catch (UnsupportedAudioFileException e)
		{
			System.out.println("Audio type unsupported");
			e.printStackTrace();
		}
	}	
}
