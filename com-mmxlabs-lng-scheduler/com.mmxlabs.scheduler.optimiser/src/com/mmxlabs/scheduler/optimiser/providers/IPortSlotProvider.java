/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

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
	IPortSlot getPortSlot(ISequenceElement element);

	/**
	 * Returns the sequence element for the given {@link IPortSlot}.
	 * 
	 * @param portSlot
	 * @return
	 */
	ISequenceElement getElement(IPortSlot portSlot);
}
