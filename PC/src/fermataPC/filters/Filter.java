package fermataPC.filters;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;

import fermataPC.util.Coordinate;

/**
 * The Filter class exists to support filtering within the software; all
 * filters are subclasses of this class.
 * @author katzj2
 *
 */
public abstract class Filter
{
	/**
	 * Using JSyn's audio functionality, audio is transfered from input port
	 * to output port to input port, so on, and manipulated between input and
	 * output ports by UnitGenerators. The input port here is the port which
	 * audio should be passed to in this filter such that it be filtered.
	 */
	public UnitInputPort input;
	
	/**
	 * Using JSyn's audio functionality, audio is transfered from input port
	 * to output port to input port, so on, and manipulated between input and
	 * output ports by UnitGenerators. The output port here is the port which
	 * manipulated audio will be passed out of after this filter filters it.
	 */
	public UnitOutputPort output;
	
	/**
	 * The name of the filter
	 */
	protected String name;
	/**
	 * The unique ID of the filter, to be assigned by the FilterHandler. This
	 * is how we track which filters are active, etc, in the phone.
	 */
	protected int UID;
	
	/**
	 * The Coordinate most recently passed this filter.
	 */
	protected Coordinate coord;
	
	/**
	 * The axis this filter is active on. 0 is x, 1 is y.
	 */
	protected int axis;
	
	/**
	 * This function generates a string summary of the filter in a format which
	 * can be passed to the phone; concatenates the name fot he filter, its UID
	 * and its axis in a comma-separated string, ending with a semicolon.
	 * @return
	 */
	public final String generateStringSummary()
	{
		return new String(name + "," + UID + "," + axis +";");
	}
	
	/**
	 * Sets the current Coordinate on this filter.
	 * @param coord The Coordinate to set the filter to.
	 */
	public abstract void setCoordinate(Coordinate coord);

	/**
	 * Gets the axis this filter is active on
	 * @return 0 for x, 1 for y
	 */
	public final int getAxes()
	{
		return axis;
	}

	/**
	 * Sets the UID of this filter. This number should be UNIQUE from any
	 * other filter ID, although this won't be enforced. . . still, it will
	 * crash otherwise, so program carefully.
	 * @param UID The UID of this filter.
	 */
	public void setUID(int UID)
	{
		this.UID = UID;
	}
	
	/**
	 * Cancel the activity of this filter.
	 */
	public abstract void cancelFilter();
}