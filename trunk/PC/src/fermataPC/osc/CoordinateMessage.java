package fermataPC.osc;

import java.util.ArrayList;

import com.illposed.osc.OSCMessage;

import fermataPC.util.Coordinate;

/**
 * This class automates the process of making an OSCMessage to contain
 * Fermata style Coordinates and send them to other programs,
 * wrapping the filter IDs and scalars into the message. The messages
 * can contain one or more coordinates. 
 * @author katzj2
 *
 */
public class CoordinateMessage extends OSCMessage
{
	/**
	 * This constructor creates the message based on a list of Coordinates,
	 * packing each into the message. The message address is given as
	 * "/fermataCoordinates" and then coordinates added in order, arg values
	 * alternating between filter IDs and the new scalar. Scalars are given
	 * as negative numbers to differentiate them from filter IDs.
	 * 
	 * @param coordList The Coordinate list to pack into the message. 
	 */
	public CoordinateMessage(ArrayList<Coordinate> coordList)
	{
		this.setAddress("/fermataCoordinates");
		
		for (Coordinate c : coordList)
		{
			this.addArgument(c.getFilterID());
			this.addArgument((c.getValue() * -1) - 1);
		}
	}
	
	/**
	 * This constructor creates the message based on a single Coordinate,
	 * packing it into the message. All other details are the same.
	 * @param c The coordinate to pack in.
	 */
	public CoordinateMessage(Coordinate c)
	{
		this.setAddress("/fermataCoordinates");
		
		this.addArgument(c.getFilterID());
		this.addArgument((c.getValue() * -1) - 1);
	}
	
}
