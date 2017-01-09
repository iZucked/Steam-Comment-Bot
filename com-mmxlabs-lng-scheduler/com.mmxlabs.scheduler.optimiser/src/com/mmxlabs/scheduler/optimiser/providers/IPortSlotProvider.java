/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IDataComponentProvider} interface to provide {@link IPortSlot} information for the given sequence elements.
 * 
 * @author Simon Goodall
 */
public interface IPortSlotProvider extends IDataComponentProvider {

	/**
	 * Returns the {@link IPortSlot} for the given element.
	 * 
	 * @param element
	 * @return
	 */
	@NonNull
	IPortSlot getPortSlot(@NonNull ISequenceElement element);

	/**
	 * Returns the sequence element for the given {@link IPortSlot}.
	 * 
	 * @param portSlot
	 * @return
	 */
	@NonNull
	ISequenceElement getElement(@NonNull IPortSlot portSlot);
}
