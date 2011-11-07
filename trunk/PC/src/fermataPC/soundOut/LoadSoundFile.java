package fermataPC.soundOut;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
 
import javazoom.jl.player.Player;
 
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
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            player.play();
        }
        catch (Exception e) 
        {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }
 
    }
 
/*    public static void main(String[] args) {
 
        //plays 07.mp3 file located at C drive
    	LoadSoundFile mp3 = new LoadSoundFile("c:/07.mp3");
        mp3.play();
 
    }*/
 
}
