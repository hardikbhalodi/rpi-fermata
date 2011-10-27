package fermataOSC;

import java.util.ArrayList;

import com.illposed.osc.OSCMessage;

import fermataUtil.Coordinate;

public class CoordinateMessage extends OSCMessage
{
	public CoordinateMessage(ArrayList<Coordinate> coordList)
	{
		this.setAddress("/fermataCoordinates:");
		
		for (Coordinate c : coordList)
		{
			this.addArgument(c.getFilterID());
			this.addArgument(c.getValue() * -1);
		}
	}
	
	public CoordinateMessage(Coordinate c)
	{
		this.setAddress("/fermataCoordinates:");
		
		this.addArgument(c.getFilterID());
		this.addArgument(c.getValue() * -1);
	}
	
}
