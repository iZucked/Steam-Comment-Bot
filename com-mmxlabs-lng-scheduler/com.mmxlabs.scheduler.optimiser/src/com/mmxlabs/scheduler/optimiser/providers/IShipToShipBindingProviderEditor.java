/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;


/**
 * @since 5.0
 */
public interface IShipToShipBindingProviderEditor extends IShipToShipBindingProvider {

	void setConverseTransferElement(IPortSlot portSlot, IPortSlot iPortSlot);

}
