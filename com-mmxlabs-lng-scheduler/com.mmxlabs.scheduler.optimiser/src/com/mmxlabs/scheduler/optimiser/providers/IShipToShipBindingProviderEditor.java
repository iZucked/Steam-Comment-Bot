/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;


/**
 */
public interface IShipToShipBindingProviderEditor extends IShipToShipBindingProvider {

	void setConverseTransferElement(IPortSlot portSlot, IPortSlot iPortSlot);

}
