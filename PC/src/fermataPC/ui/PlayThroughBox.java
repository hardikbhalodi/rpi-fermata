package fermataPC.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fermataPC.soundOut.MicListener;

@SuppressWarnings("serial")
public class PlayThroughBox extends JPanel
{
	private JCheckBox cbox;
	private JPanel subPane;
	private JLabel label;
	public PlayThroughBox()
	{
		super();
		initialize();
		layoutPanel();
	}
	
	public void initialize()
	{
		cbox = new JCheckBox("Microphone (line in) playthrough enabled?");
		cbox.addActionListener(new CheckListener());
		subPane = new JPanel();
		label = new JLabel("Configure microphone settings:");
	}
	
	public void layoutPanel()
	{
		this.add(label);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(subPane);
		subPane.add(cbox);
	}
	
	private class CheckListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if (cbox.isSelected())
			{
				MicListener.startListening();
			}
			else MicListener.stopListening();
		}
	}
}
