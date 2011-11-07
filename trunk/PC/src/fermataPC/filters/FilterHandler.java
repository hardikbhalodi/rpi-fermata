package fermataPC.filters;

import java.util.Vector;

import fermataPC.util.Coordinate;

public abstract class FilterHandler
{
	private static Vector<Filter> availableFilters;
	
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
		if (coord.getFilterID() >= 0 && coord.getFilterID() < availableFilters.size())
		{
			availableFilters.get(coord.getFilterID()).setCoordinate(coord);
			activateFilter(coord.getFilterID());
		}
	}
	
	private static void activateFilter(int UID)
	{
		FilterProcessor.activateFilter(availableFilters.get(UID));
	}
	
	public static String generateFilterListString()
	{
		StringBuffer bf = new StringBuffer();
		for (Filter f : availableFilters)
		{
			bf.append(f.generateStringSummary());
		}
		return bf.toString();
	}
}
