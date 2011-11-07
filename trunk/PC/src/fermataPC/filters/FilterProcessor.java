package fermataPC.filters;

public abstract class FilterProcessor
{	
	private static Filter xFilter, yFilter;
	
	public static Boolean activateFilter(Filter f)
	{
		if (f.getAxes() == 0 || f.getAxes() == 2)
		{
			deactivateFilter(xFilter);
			xFilter = f;
		}
		if (f.getAxes() == 1 || f.getAxes() == 2);
		{
			deactivateFilter(yFilter);
			yFilter = f;
		}
		return true; // false if failed. It can't fail yet, though.
	}
	
	public static Boolean acceptAudioStream(/* some kind of audio stream */)
	{
		//TODO anything.
		return true;
	}
	
	private static Boolean deactivateFilter(Filter f)
	{
		//TODO anything.
		return true;
	}
}
