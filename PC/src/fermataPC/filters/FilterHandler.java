package fermataPC.filters;

import java.util.Vector;

import fermataPC.util.Coordinate;

public abstract class FilterHandler
{
	private static Vector<Filter> availableFilters;
	private static Filter xFilter;
	private static Filter yFilter;
	
	public static void startService()
	{
		availableFilters = new Vector<Filter>();
		
		//Add available filters here as we create them.
		addFilter(new OSCFilter(0));
		addFilter(new OSCFilter(1));
	
		// Default filters are just OSC.
		activateFilter(0);
		activateFilter(1);
	}
	
	private static void addFilter(Filter newFilter)
	{
		newFilter.setUID(availableFilters.size());
		availableFilters.add(newFilter);
	}
	
	public static void applyCoordinate(Coordinate coord)
	{
		if (coord.getFilterID() <= 0 && coord.getFilterID() < availableFilters.size())
		{
			availableFilters.get(coord.getFilterID()).setCoordinate(coord);
			activateFilter(coord.getFilterID());
		}
	}
	
	private static void activateFilter(int UID)
	{
		Filter f = availableFilters.get(UID);
		
		switch (f.getAxes())
		{
		case 0:
			deactivateFilter(xFilter);
			xFilter = f;
			//TODO more stuff.
			break;
		case 1:
			deactivateFilter(yFilter);
			yFilter = f;
			//TODO more stuff
			break;
		case 2:
			deactivateFilter(xFilter);
			xFilter = yFilter = f;
			//TODO more stuff.
			break;
		}
	}
	
	private static void deactivateFilter(Filter f)
	{
		//TODO
	}
	
	public static Vector<Filter> getAvailableFilters()
	{
		return availableFilters;
	}
	
	public static Filter getFilter(int UID)
	{
		return availableFilters.get(UID);
	}
	
}
