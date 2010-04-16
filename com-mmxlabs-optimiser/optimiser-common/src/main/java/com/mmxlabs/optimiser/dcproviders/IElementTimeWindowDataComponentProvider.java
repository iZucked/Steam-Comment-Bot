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
public interface IElementTimeWindowDataComponentProvider extends
		IDataComponentProvider {

	/**
	 * Return a {@link List} of {@link ITimeWindow} for this sequence element,
	 * or null if nothing has been set.
	 * 
	 * @param element
	 * @return
	 */
	List<ITimeWindow> getTimeWindows(Object element);

	/**
	 * Set a {@link List} of {@link ITimeWindow}s for the given sequence
	 * element.
	 * 
	 * @param element
	 * @param timeWindows
	 */
	void setTimeWindows(Object element, List<ITimeWindow> timeWindows);

}
