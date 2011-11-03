package fermata.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import fermata.midi.MidiHandler;

@SuppressWarnings("serial")
public final class MidiDeviceBox extends JPanel
{
	private Vector<MidiDevice> validDevices;
	private MidiDevice selectedDevice;
	private JComboBox comboBox;
	private JCheckBox checkBox;
	
	public MidiDeviceBox()
	{
		super();
		initialize();
		layoutPanel();
	}

	private void initialize()
	{
		comboBox = new JComboBox();
		comboBox.setRenderer(new CustomRenderer());
		comboBox.setEnabled(false);
		comboBox.setEditable(false);
		
		checkBox = new JCheckBox("Enable MIDI");
		checkBox.setSelected(false);
		checkBox.setEnabled(true);
		
		customListener cl = new customListener();
		
		checkBox.addActionListener(cl);
		comboBox.addActionListener(cl);
		
		validDevices = new Vector<MidiDevice>();

		MidiHandler.setDeviceBox(this);
	}
	
	private void layoutPanel()
	{
		this.setPreferredSize(new Dimension(300,50));
		
		this.setLayout(new BorderLayout());
		this.add(checkBox, BorderLayout.LINE_START);
		this.add(comboBox, BorderLayout.CENTER);
	}
	
	public void updateList(Vector<MidiDevice> newDevices)
	{
		if (validDevices.equals(newDevices))
			return;
		else if (newDevices.contains(selectedDevice))
		{
			comboBox.removeAllItems();
			
			for (MidiDevice m : newDevices)
			{
				comboBox.addItem(m);
			}		
			comboBox.setSelectedItem(selectedDevice);					
		}
		else
		{
			comboBox.removeAllItems();
			for (MidiDevice m : newDevices)
			{
				comboBox.addItem(m);
			}
			
			if (newDevices.size() > 0)
				comboBox.setSelectedIndex(0);
		}
		
		if (newDevices.size() > 0)
		{
			comboBox.setEnabled(true);
		}
		else
		{
			comboBox.setEnabled(false);
		}
		
		validDevices.clear();
		validDevices.addAll(newDevices);
	}
	
	private class CustomRenderer implements ListCellRenderer
	{
		@Override
		public Component getListCellRendererComponent(
				JList list ,
				Object item ,
				int index ,
				boolean isSelected ,
				boolean cellHasFocus)
		{
			MidiDevice md = (MidiDevice) item;
			if (md == null)
			{
				return new JLabel("No devices");
			}
			JLabel toReturn = new JLabel(md.getDeviceInfo().getName());
			
			toReturn.setOpaque(true);
			
			if(isSelected)
			{
				toReturn.setBackground(list.getSelectionBackground());
				toReturn.setForeground(list.getSelectionForeground());
			}
			else
			{
				toReturn.setBackground(list.getBackground());
				toReturn.setForeground(list.getForeground());
			}
			return toReturn;
		}
	}
	
	private class customListener implements ActionListener
	{		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == comboBox)
			{
				if (checkBox.isSelected())
					MidiHandler.setActiveDevice((MidiDevice) comboBox.getSelectedItem());
			}
			else if (e.getSource() == checkBox)
			{
				if (checkBox.isSelected())
					MidiHandler.setActiveDevice((MidiDevice) comboBox.getSelectedItem());			
				else
					MidiHandler.setActiveDevice(null);
			}
		}
	}
}
