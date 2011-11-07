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
		Integer newUID = availableFilters.size();
		newFilter.setUID(newUID);
		availableFilters.add(newFilter);
	}
	
	public static void applyCoordinate(Coordinate coord)
	{
		Integer filterID = coord.getFilterID();
		if (filterID >= 0 && filterID < availableFilters.size())
		{
			Filter f = availableFilters.get(filterID);
			f.setCoordinate(coord);
			activateFilter(coord.getFilterID());
		}
	}
	
	private static void activateFilter(int UID)
	{
		Filter f = availableFilters.get(UID);
		FilterProcessor.activateFilter(f);
	}
	
	public static String generateFilterListString()
	{
		StringBuffer filterList = new StringBuffer();
		for (Filter f : availableFilters)
		{
			String sum = f.generateStringSummary();
			filterList.append(sum);
		}
		return filterList.toString();
	}
}
