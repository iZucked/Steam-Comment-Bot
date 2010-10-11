package com.mmxlabs.optimiser.common.dcproviders;

import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

public interface ITimeWindowDataComponentProviderEditor<T> extends
		ITimeWindowDataComponentProvider<T> {


	/**
	 * Set a {@link List} of {@link ITimeWindow}s for the given sequence
	 * element.
	 * 
	 * @param element
	 * @param timeWindows
	 */
	void setTimeWindows(T element, List<ITimeWindow> timeWindows);

	
}
