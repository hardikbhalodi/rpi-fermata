package fermataPC.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fermataPC.osc.OSCSender;

@SuppressWarnings("serial")
public class OSCSendBox extends JPanel
{
	private JFormattedTextField ipField;
	private JFormattedTextField portField;
	
	private JCheckBox oscEnableBox;
	private JCheckBox localHostBox;
	
	private String oldIP = "localhost";
	private int oldPort = OSCSender.DEFAULT_PORT;
	
	private final OSCSender oscSend = OSCSender.getOSCSender(); 
	
	public OSCSendBox()
	{
		super();
		
		initialize();
		layoutPanel();
	}
	
	private void initialize()
	{
		ipField = new JFormattedTextField();
		portField = new JFormattedTextField();

		portField.setValue(OSCSender.DEFAULT_PORT);
		
		oscEnableBox = new JCheckBox("Enable OSC pass-through", false);
		localHostBox = new JCheckBox("Use LocalHost", true);
		
		CustomListener cl = new CustomListener();
		ipField.addActionListener(cl);
		portField.addActionListener(cl);
		localHostBox.addActionListener(cl);
		oscEnableBox.addActionListener(cl);
		
		ipField.addFocusListener(cl);
		portField.addFocusListener(cl);
	}
	
	public void layoutPanel()
	{
		this.setPreferredSize(new Dimension(400,150));
		
		this.setLayout(new BorderLayout());
		
		this.add(oscEnableBox, BorderLayout.PAGE_START);
		
		JPanel innerPane = new JPanel();
		
		this.add(innerPane, BorderLayout.CENTER);
		
		JPanel portPane = new JPanel();
		portPane.setLayout(new BorderLayout());
		portPane.add(new JLabel("OSC Port: "), BorderLayout.LINE_START);
		portPane.add(portField, BorderLayout.PAGE_END);
		
		portPane.setPreferredSize(new Dimension(150,100));
		innerPane.add(portPane);
		
		JPanel ipPane = new JPanel();
		
		ipPane.setLayout(new BorderLayout());
		ipPane.add(localHostBox, BorderLayout.PAGE_START);
		ipPane.add(new JLabel("Enter IP: "), BorderLayout.LINE_START);
		ipPane.add(ipField, BorderLayout.PAGE_END);
		
		ipPane.setPreferredSize(new Dimension(150,100));
		
		portField.setEnabled(false);
		ipField.setEnabled(false);
		
		
		localHostBox.setEnabled(false);
		
		portChanged();
		ipChanged();
		
		innerPane.add(ipPane);
	}

	private void portChanged()
	{			
		int newPort = (Integer) portField.getValue();

		if (newPort == oldPort)
			return;
		else
		{
			oldPort = newPort;
			newPort = oscSend.setPort(newPort);
		
			portField.setValue(newPort);
		}
	}
	private void ipChanged()
	{			
		String newIP = ipField.getText();
		
		if (newIP.equals(oldIP))
			return;
		else
		{
			oldIP = newIP;
			newIP = oscSend.setIP(newIP);
			
			ipField.setText(newIP);
		}
	}
	
	private class CustomListener implements ActionListener, FocusListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == oscEnableBox)
			{
				if (oscEnableBox.isSelected())
				{
					oscSend.enableOSC();
					
					portField.setEnabled(true);
					localHostBox.setEnabled(true);
					ipField.setEnabled(!localHostBox.isSelected());
				}
				else
				{
					oscSend.disableOSC();
					
					portField.setEnabled(false);
					localHostBox.setEnabled(false);
					ipField.setEnabled(false);
				}
			}			
			else if (e.getSource() == localHostBox)
			{
				Boolean useLocalHost = localHostBox.isSelected();
				ipField.setEnabled(!useLocalHost);
				
				if (useLocalHost)
				{
					oscSend.setIP("localhost");
				}
				else
				{
					ipField.setText(oscSend.setIP(ipField.getText()));
				}
			}
			else if (e.getSource() == portField)
				portChanged();
			else if (e.getSource() == ipField)
				ipChanged();
		}
		
		@Override
		public void focusGained(FocusEvent e)
		{
			//We really don't care about this one.
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			if (e.getSource() == portField)
				portChanged();
			else if (e.getSource() == ipField)
				ipChanged();
		}
	}
}
