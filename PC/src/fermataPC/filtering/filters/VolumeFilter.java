package fermataPC.filtering.filters;

import fermataPC.filtering.Filter;
import fermataPC.filtering.FilterProcessor;
import fermataPC.util.Coordinate;
import com.jsyn.unitgen.Multiply;

/**
 * The VolumeFilter allows manipulation of the volume of the audio
 * from 0x to 1.275x (amplification).
 * @author Tyler Sammann
 *
 */
public class VolumeFilter extends Filter
{
	
	/**
	 * JSyn's Multiply can be used to do scale the signal strength.
	 */
	private Multiply volFilter;
	
	/**
	 * Constructs the volume filter, taking an axis as an argument
	 * to determine what axis is filtered on. Initializes
	 * necessary fields, and adds the Multiply to the synth.
	 * @param axis
	 */
	public VolumeFilter(int axis)
	{	
		super.axis = axis;
		super.name = "Volume ("+ (axis == 0 ? "x" : "y") + ")";
		volFilter = new Multiply();
		
		FilterProcessor.synth.add(volFilter);
		
		input = volFilter.inputA;
		output = volFilter.output;
		
	}
	@Override
	public void setCoordinate(Coordinate coord)
	{
		// TODO Auto-generated method stub
		double val = coord.getValue();
		val = val/200;
		
		volFilter.inputB.set(val);
	}

	@Override
	public void cancelFilter() 
	{		
		volFilter.inputA.disconnectAll(0);
		volFilter.output.disconnectAll(0);	
		
	}	
	
}



