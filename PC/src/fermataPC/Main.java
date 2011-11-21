package fermataPC;

import javax.swing.UIManager;

import fermataPC.filters.FilterHandler;
import fermataPC.midi.MidiHandler;
import fermataPC.osc.OSCSender;
import fermataPC.server.ServerManager;
import fermataPC.soundOut.midiPlayer;
import fermataPC.soundOut.debug.DummyMIDISender;
import fermataPC.ui.FermataFrame;

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
		
		
		midiPlayer mp = new midiPlayer();
		FilterHandler.startService(); //So things can be filtered.
		OSCSender.startOSCService(); //so OSC messages can be sent.
		MidiHandler.startMIDIService(); // So MIDI events can be handled.
		
		@SuppressWarnings("unused")
		ServerManager sm = new ServerManager(9876, FilterHandler.generateFilterListString());
		System.out.println("Filter List: \n" + FilterHandler.generateFilterListString());
		
		FermataFrame window = new FermataFrame(); // Creates the graphical
		
		//DummyMIDISender dms = new DummyMIDISender();
		// component of the software.
		window.setVisible(true);
		window.pack();
	}
}