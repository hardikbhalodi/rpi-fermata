package fermata.pc;

import javax.swing.UIManager;

import fermata.midi.MidiHandler;
import fermata.osc.OSCSender;
import fermata.soundOut.LoadSoundFile;
import fermata.ui.FermataFrame;

/**
 * Exists solely to start Fermata.
 * @author katzj2
 *
 */
public class Main
{		
	/**
	 * Starts Fermata and calls a few functions to set up various "service"
	 * style classes. Also sets up the L&F for the GUI.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.out.println("System L&F fail. Using default/cross-platform L&F");
		}
		
		OSCSender.startOSCService(); //so OSC messages can be sent.
		MidiHandler.startMIDIService(); // So MIDI events can be handled.
		FermataFrame window = new FermataFrame(); // Creates the graphical
		
		// component of the software.
		window.setVisible(true);
		window.pack();
		
		
		//Sound file playback
		LoadSoundFile mp3 = new LoadSoundFile("/home/tyler/Desktop/BrainChild.mp3");
        mp3.play();
        
        //System.out.println("here");
	}
}