/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} interface to provide {@link ITimeWindow} information for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public interface ITimeWindowDataComponentProvider extends IDataComponentProvider {

	/**
	 * Return a {@link List} of {@link ITimeWindow} for this sequence element, or an empty list if nothing has been set.
	 * 
	 * @param element
	 * @return
	 */
	@NonNull
	List<@NonNull ITimeWindow> getTimeWindows(@NonNull ISequenceElement element);

}
