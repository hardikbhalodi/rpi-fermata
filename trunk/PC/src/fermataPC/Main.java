package fermataPC;

import javax.swing.UIManager;

import fermataOSC.OSCSender;
import fermataUI.FermataFrame;

public class Main
{		
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
		
		OSCSender oscSend = new OSCSender();
		FermataFrame window = new FermataFrame();
		window.setVisible(true);
		window.pack();
	}
}