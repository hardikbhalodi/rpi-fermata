package fermataPC;

import javax.swing.UIManager;

import fermataPC.filtering.FilterHandler;
import fermataPC.server.ServerManager;
import fermataPC.ui.FermataFrame;

/**
 * Exists solely to start Fermata.
 * @author katzj2
 *
 */
public class Main
{		
	/**
	 * Starts Fermata and sets up the L&F for the GUI, as well as starts the GUI.
	 * @param args unused.
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
		
		FilterHandler fh = FilterHandler.getFilterHandler();
		
		@SuppressWarnings("unused")
		ServerManager sm = new ServerManager(9876, fh.generateFilterListString());
		System.out.println("Filter List: \n" + fh.generateFilterListString());
		
		// Not testing MIDI without a keyboard today.
		//DummyMIDISender dms = new DummyMIDISender();
		
		FermataFrame window = new FermataFrame(); // Creates the graphical
		// component of the software.
		window.setVisible(true);
		window.pack();
	}
}