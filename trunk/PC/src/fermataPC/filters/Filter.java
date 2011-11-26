package fermataPC.filters;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;

import fermataPC.util.Coordinate;

public abstract class Filter
{
	public UnitInputPort input;
	public UnitOutputPort output;
	
	protected String name;
	protected int UID;
	protected Coordinate coord;
	protected int axis;
	
	public final String generateStringSummary()
	{
		return new String(name + "," + UID + "," + axis +";");
	}
	
	public abstract void setCoordinate(Coordinate coord);

	public final int getAxes()
	{
		return axis;
	}

	public void setUID(int UID)
	{
		this.UID = UID;
	}
	
	public abstract void cancelFilter();
}