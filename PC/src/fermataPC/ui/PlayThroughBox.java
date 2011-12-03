package fermataPC.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fermataPC.sound.MicListener;

/**
 * The PlayThroughBox is the GUI element enabling users to turn on and off
 * their microphones.
 * @author katzj2
 *
 */
@SuppressWarnings("serial")
public class PlayThroughBox extends JPanel
{
	private JCheckBox cbox;
	private JPanel subPane;
	private JLabel label;
	
	/**
	 * Creates the PlayThroughBox
	 */
	public PlayThroughBox()
	{
		super();
		initialize();
		layoutPanel();
	}
	
	/**
	 * Initializes all the fields of the box.
	 */
	public void initialize()
	{
		cbox = new JCheckBox("Microphone (line in) playthrough enabled?");
		cbox.addActionListener(new CheckListener());
		subPane = new JPanel();
		label = new JLabel("Configure microphone settings:");
	}
	
	/**
	 * Lays out all the fields of the box.
	 */
	public void layoutPanel()
	{
		this.add(label);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(subPane);
		subPane.add(cbox);
	}
	
	/**
	 * This listener knows when the checkbox is manipulated.
	 * @author katzj2
	 *
	 */
	private class CheckListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if (cbox.isSelected()) // Then they've checked it.
			{
				MicListener.getMicListener().startListening();
			}
			else MicListener.getMicListener().stopListening();	// They've unchecked it.
		}
	}
}
