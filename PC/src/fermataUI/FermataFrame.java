package fermataUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import fermataMIDI.MidiHandler;

@SuppressWarnings("serial")
public class FermataFrame extends JFrame
{
	
	private JMenuBar fermataMenuBar;
	private JMenuItem fermataExit;
	private JMenu fermataMenu;
	private MidiDeviceBox mdb;
	private MidiHandler mh;
	
	public FermataFrame()
	{
		super();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeFrame();
		layoutFrame();
	}	
	
	private void initializeFrame()
	{
		this.setTitle("Fermata");
		
		MenuListener listen = new MenuListener(this);
		
		fermataMenuBar = new JMenuBar();
		fermataMenu = new JMenu("Fermata");
		fermataExit = new JMenuItem("Close Fermata");
		
		fermataExit.addActionListener(listen);
		
		mdb = new MidiDeviceBox();
		mh = new MidiHandler(mdb);
	}
	
	private void layoutFrame()
	{
		this.setJMenuBar(fermataMenuBar);
		fermataMenuBar.add(fermataMenu);
		fermataMenu.add(fermataExit);
		
		this.setContentPane(mdb);
	}
	
	private class MenuListener implements ActionListener
	{
		private JFrame hostFrame;
		
		public MenuListener(JFrame hostFrame)
		{
			this.hostFrame = hostFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == fermataExit)
			{
				hostFrame.dispose();
				System.exit(0);
			}
		}
	}
}
