/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;

public interface ITimeWindowDataComponentProviderEditor extends ITimeWindowDataComponentProvider {

	/**
	 * Set a {@link List} of {@link ITimeWindow}s for the given sequence element.
	 * 
	 * @param element
	 * @param timeWindows
	 */
	void setTimeWindows(ISequenceElement element, List<ITimeWindow> timeWindows);

}
