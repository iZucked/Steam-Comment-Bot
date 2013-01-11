/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * Editor interface for {@link IPortSlotProvider}
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortSlotProviderEditor extends IPortSlotProvider {

	/**
	 * Set the {@link IPortSlot} for the given sequence element
	 * 
	 * @param element
	 * @param portSlot
	 */
	void setPortSlot(ISequenceElement element, IPortSlot portSlot);
}
