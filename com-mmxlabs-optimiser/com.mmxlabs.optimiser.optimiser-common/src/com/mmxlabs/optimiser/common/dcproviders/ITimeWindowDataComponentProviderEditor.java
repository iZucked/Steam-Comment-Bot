/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;

public interface ITimeWindowDataComponentProviderEditor extends ITimeWindowDataComponentProvider {

	/**
	 * Set a {@link List} of {@link ITimeWindow}s for the given sequence element.
	 * 
	 * @param element
	 * @param timeWindows
	 */
	void setTimeWindows(@NonNull ISequenceElement element, @Nullable List<@NonNull ITimeWindow> timeWindows);

}
