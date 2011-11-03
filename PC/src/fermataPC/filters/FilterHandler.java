package fermataPC.filters;

import java.util.ArrayList;

public abstract class FilterHandler
{
	private static ArrayList<Filter> availableFilters;
	private static Filter xFilter;
	private static Filter yFilter;
	
	public static void startService()
	{
		//Add available filters here as we create them.
		addFilter(new OSCFilter(0));
		addFilter(new OSCFilter(1));
	
		//
		activateFilter(0);
		activateFilter(1);
	}
	
	private static void addFilter(Filter newFilter)
	{
		newFilter.setUID(availableFilters.size());
		availableFilters.add(newFilter);
	}
	
	public static void activateFilter(int UID)
	{
		Filter f = availableFilters.get(UID);
		
		switch (f.getAxes())
		{
		case 0:
			xFilter = f;
			break;
		case 1:
			yFilter = f;
			break;
		case 2:
			xFilter = yFilter = f;
			break;
		}
	}
	
	
}
