package com.mmxlabs.optimiser.dcproviders;

import java.util.List;

import com.mmxlabs.optimiser.components.ITimeWindow;

public interface ITimeWindowDataComponentProviderEditor extends
		ITimeWindowDataComponentProvider {


	/**
	 * Set a {@link List} of {@link ITimeWindow}s for the given sequence
	 * element.
	 * 
	 * @param element
	 * @param timeWindows
	 */
	void setTimeWindows(Object element, List<ITimeWindow> timeWindows);

	
}
