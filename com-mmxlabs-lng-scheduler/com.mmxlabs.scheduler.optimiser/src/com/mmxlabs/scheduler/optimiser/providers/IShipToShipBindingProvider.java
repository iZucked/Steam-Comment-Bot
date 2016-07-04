/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 */
public interface IShipToShipBindingProvider extends IDataComponentProvider {

	IPortSlot getConverseTransferElement(IPortSlot slot);

}
