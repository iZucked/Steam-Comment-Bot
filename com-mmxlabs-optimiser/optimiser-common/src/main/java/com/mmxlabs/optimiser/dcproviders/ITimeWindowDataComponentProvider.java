package com.mmxlabs.optimiser.dcproviders;

import java.util.List;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} interface to provide {@link ITimeWindow}
 * information for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public interface ITimeWindowDataComponentProvider extends
		IDataComponentProvider {

	/**
	 * Return a {@link List} of {@link ITimeWindow} for this sequence element,
	 * or an empty list if nothing has been set.
	 * 
	 * @param element
	 * @return
	 */
	List<ITimeWindow> getTimeWindows(Object element);

}
