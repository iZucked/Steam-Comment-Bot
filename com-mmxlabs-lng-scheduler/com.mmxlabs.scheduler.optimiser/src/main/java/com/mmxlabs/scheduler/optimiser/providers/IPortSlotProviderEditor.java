/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * Editor interface for {@link IPortSlotProvider}
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IPortSlotProviderEditor<T> extends IPortSlotProvider<T> {

	/**
	 * Set the {@link IPortSlot} for the given sequence element
	 * 
	 * @param element
	 * @param portSlot
	 */
	void setPortSlot(T element, IPortSlot portSlot);
}
