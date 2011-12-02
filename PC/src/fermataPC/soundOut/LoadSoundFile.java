package fermataPC.soundOut;

import java.io.File;

import javazoom.jl.converter.Converter;
 
/**
 * Supports the loading/conversion of sound files into formats which can be
 * played by the PlayAudio class.
 * @author Tyler, katzj2
 *
 */
public class LoadSoundFile 
{
	/**
	 * The path to the file.
	 */
    private File tempFile;
 
    // constructor that takes the name of an MP3 file
    public LoadSoundFile(String filename) 
    {
        try 
        {
        	tempFile = null;
        	if (filename.contains(".mp3"))
        	{
	        	tempFile = File.createTempFile("fermatawavtemp" , ".wav");
	        	tempFile.deleteOnExit();
	        	
	        	Converter conv = new Converter();
	        	conv.convert(filename, tempFile.getPath());	
        	}
        	else if (filename.contains(".wav"))
        	{
        		tempFile = new File(filename);
        	}
        	else
        	{
        		System.out.println("Unsupported file type");
        	}
        }
        catch (Exception e) 
        {
            System.out.println("Problem loading file " + filename);
            System.out.println(e);
        } 
    }
 
    /**
     * Attempts to play the audio file, doing conversions if necessary.
     * Implementation is incomplete, so we recommend only sending in .wavs
     */
    public void play() 
    {
        PlaySoundFile.playStream(tempFile);
    }
}
