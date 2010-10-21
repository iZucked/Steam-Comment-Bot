/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * The {@link ISequenceElement} is the object manipulated in the sequences
 * passed to the optimiser. It represents a single visit to a particular port.
 * {@link IDataComponentProvider}s will provide additional information for these
 * elements such as {@link ITimeWindow}s.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequenceElement extends IIndexedObject {

	/**
	 * A name for this element.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the {@link IPortSlot} associated with this element.
	 * 
	 * @return
	 */
	IPortSlot getPortSlot();
}
