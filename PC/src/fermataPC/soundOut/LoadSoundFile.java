package fermataPC.soundOut;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


import javazoom.jl.player.Player;
import javazoom.jl.converter.Converter;
 
public class LoadSoundFile 
{
    private String filename;
    private Player player;
 
    // constructor that takes the name of an MP3 file
    public LoadSoundFile(String filename) 
    {
        this.filename = filename;
    }
 
    // play the MP3 file
    public void play() 
    {
        try 
        {
        	File tempFile = null;
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
        	PlaySoundFile.playStream(tempFile);
        
      //      player = new Player(bis);
        //    player.play();
        }
        catch (Exception e) 
        {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }
 
    } 
}
