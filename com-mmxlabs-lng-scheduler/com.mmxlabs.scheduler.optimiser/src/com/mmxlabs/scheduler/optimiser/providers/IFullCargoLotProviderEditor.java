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
public interface IFullCargoLotProviderEditor extends IFullCargoLotProvider {

	/**
	 * Records the portslot with FCL flag.
	 * 
	 * @param portSlot
	 * @return
	 */
	public void addFCLSlot(@NonNull IPortSlot portSlot);
}
