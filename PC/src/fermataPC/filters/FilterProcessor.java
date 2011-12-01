package fermataPC.filters;

import java.util.Vector;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.LineOut;

public final class FilterProcessor
{	
	private static FilterProcessor self;
	//public static UnitInputPort input;
	public static final Synthesizer synth = JSyn.createSynthesizer();
	
//	private static UnitOutputPort filterInput;
	private static Vector<UnitOutputPort> filterInputs;
	
	public static final LineOut lineOut = new LineOut();
	
	private static Filter xFilter, yFilter;
	
	private FilterProcessor()
	{
		
		super();
		
		synth.add(lineOut);
		
		synth.start();
		
		double timeNow = synth.getCurrentTime();
		
		double time = timeNow + .5;
		
		synth.startUnit(lineOut, time);
		
		self = this;
		
		filterInputs = new Vector<UnitOutputPort>();
		
	}
	
	public static FilterProcessor getFilterProcessor()
	{
		if (self == null)
		{
			self = new FilterProcessor();
		}
		return self;
	}
	
	public static void connectOutput(UnitOutputPort listenTo)
	{
		filterInputs.add(listenTo);
	//	filterInput = listenTo;
		reRouteFilters();
	}
	
	public Boolean activateFilter(Filter f)
	{
		if (xFilter == f || yFilter == f)
			return false;
		
		if (f.getAxes() == 0)
		{
			deactivateFilter(xFilter);
			xFilter = f;
			reRouteFilters();
		}
		else if (f.getAxes() == 1)
		{
			deactivateFilter(yFilter);
			yFilter = f;
			reRouteFilters();
		}
		else if (f.getAxes() == 2)
		{
			deactivateFilter(xFilter);
			deactivateFilter(yFilter);
			xFilter = f;
			reRouteFilters();
		}
		
		return true; // false if failed. It can't fail yet, though.
	}
	
	private static void reRouteFilters()
	{
		System.out.println("xFilter is " + xFilter.name);
		if (yFilter != null)
			System.out.println("yfilter is " + yFilter.name);
		System.out.println("Re-routing filters");
		
		if (filterInputs.size() == 0)
			return;
		
		Vector<UnitOutputPort> badInputs = new Vector<UnitOutputPort>();
		for (UnitOutputPort filterInput : filterInputs)
		{
			if (filterInput == null || filterInput.getUnit() == null)
			{
				badInputs.add(filterInput);
				continue;
			}
			
			filterInput.disconnectAll(0);		
			filterInput.connect(0, xFilter.input, 0);
		}
		
		for (UnitOutputPort badInput : badInputs)
		{
			filterInputs.remove(badInput);
		}
		

		lineOut.input.disconnectAll(0);
		lineOut.input.disconnectAll(1);
		
		if (yFilter == null || xFilter.getAxes() == 2)
		{
			xFilter.output.connect(0, lineOut.input, 0);
			xFilter.output.connect(0, lineOut.input, 1);
		}
		else
		{
			xFilter.output.connect(0, yFilter.input, 0);
			
			yFilter.output.connect(0, lineOut.input, 0);
			yFilter.output.connect(0, lineOut.input, 1);
		}
	}
	
	private static Boolean deactivateFilter(Filter f)
	{
		if (f != null)
		{
			f.cancelFilter();
			return true;
		}
		return false;
	}
}
