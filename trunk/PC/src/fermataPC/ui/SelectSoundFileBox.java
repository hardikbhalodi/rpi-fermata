package fermataPC.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import fermataPC.sound.LoadSoundFile;
import fermataPC.sound.PlaySoundFile;

/**
 * Adds ui elements which allows for selection of a sound file, and starting and stopping playback of that file.
 * This class also enforces sound file format (mono 16 signed WAV), and swaps play button and stop button
 * when necessary.
 * @author Tyler Sammann, katzj2
 * 
 */

@SuppressWarnings("serial")
public class SelectSoundFileBox extends JPanel implements ActionListener
{
	private String filename;
	private String absPath;
	private JButton playButton;
	private JButton browseButton;
	private JLabel soundFileLabel;
	private JFileChooser fc;
	
	private boolean playing = false;
	
	public SelectSoundFileBox()
	{
		super();
		initialize();
		layoutPanel();
	}
	
	public void initialize()
	{
		playButton = new JButton("Play");
		playButton.setEnabled(false);
		this.add(playButton);
		playButton.addActionListener(this);
		
		browseButton = new JButton("Browse");
		this.add(browseButton);
		browseButton.addActionListener(this);
		
		soundFileLabel = new JLabel("browse for mono signed 16 bit PCM *.wav file on hard disk");
		this.add(soundFileLabel);
		
		fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Audio files", "wav"));
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
				playButton.setEnabled(true);
			}
		}
		if(e.getSource() == playButton)
		{
			if (!playing)
			{
				LoadSoundFile soundLoader = new LoadSoundFile(absPath);
				soundLoader.play();
				playing = true;
				playButton.setText("Stop");
			}
			else
			{
				PlaySoundFile.stop();
				playing = false;
				
				playButton.setText("Play");
			}
			
		}
	}
}