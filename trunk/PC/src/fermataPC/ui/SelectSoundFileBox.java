package fermataPC.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fermataPC.soundOut.LoadSoundFile;


@SuppressWarnings("serial")
public class SelectSoundFileBox extends JPanel implements ActionListener
{
	private String filename;
	private String absPath;
	private JButton playButton;
	private JButton browseButton;
	private JLabel soundFileLabel;
	private JFileChooser fc;
	
	public SelectSoundFileBox()
	{
		super();
		initialize();
		layoutPanel();
	}
	
	public void initialize()
	{
		
		playButton = new JButton("Play");
		this.add(playButton);
		playButton.addActionListener(this);
		
		browseButton = new JButton("Browse");
		this.add(browseButton);
		browseButton.addActionListener(this);
		
		soundFileLabel = new JLabel("browse for an mp3 file on hard disk");
		this.add(soundFileLabel);
		
		fc = new JFileChooser();
	}
	
	public void layoutPanel()
	{
		//JFileChooser fc = new JFileChooser(new File(filename));
		//fc.showOpenDialog(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == browseButton)
		{
			if(fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				filename = file.getName();
				absPath = file.getAbsolutePath();
				soundFileLabel.setText(filename);
			}
		}
		if(e.getSource() == playButton)
		{
			LoadSoundFile mp3 = new LoadSoundFile(absPath);
			mp3.play();
		}
	}
}





