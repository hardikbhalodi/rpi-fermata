package fermataUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FermataFrame extends JFrame
{
	
	private JMenuBar fermataMenuBar;
	private JMenuItem fermataExit;
	private JMenu fermataMenu;
	
	public FermataFrame()
	{
		super();
		
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
	}
	
	private void layoutFrame()
	{
		this.setJMenuBar(fermataMenuBar);
		fermataMenuBar.add(fermataMenu);
		fermataMenu.add(fermataExit);
		
		JPanel temp = new JPanel();
		
		temp.setPreferredSize(new Dimension(180,180));
		
		this.setContentPane(temp);
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
