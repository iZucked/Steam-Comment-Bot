/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IDataComponentProvider} interface to provide {@link IPortSlot} information for the given sequence elements.
 * 
 * @author Simon Goodall, FM
 */
public interface IFullCargoLotProvider extends IDataComponentProvider {

	/**
	 * Returns the boolean flag for the given element.
	 * 
	 * @param portSlot
	 * @return
	 */
	boolean hasFCLRequirment(@NonNull IPortSlot portSlot);
}
