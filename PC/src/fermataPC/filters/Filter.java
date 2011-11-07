package fermataPC.filters;

import fermataPC.util.Coordinate;

public abstract class Filter
{
	protected String name;
	protected int UID;
	protected Coordinate coord;
	protected int axis;
	

	public final String generateStringSummary()
	{
		return new String(name + "," + UID + "," + axis +";");
	}
	
	public final void setCoordinate(Coordinate coord)
	{
		this.coord = coord;
	}
	
	public abstract void filterStream();

	public final int getAxes()
	{
		return axis;
	}

	public void setUID(int UID)
	{
		this.UID = UID;
	}
}
