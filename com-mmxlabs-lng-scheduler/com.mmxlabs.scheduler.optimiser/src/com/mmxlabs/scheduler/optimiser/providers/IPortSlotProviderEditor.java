/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

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
	void setPortSlot(@NonNull ISequenceElement element, @NonNull IPortSlot portSlot);
}
