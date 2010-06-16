package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;

/**
 * The {@link ISequenceElement} is the object manipulated in the sequences
 * passed to the optimiser. It represents a single visit to a particular port.
 * {@link IDataComponentProvider}s will provide additional information for these
 * elements such as {@link ITimeWindow}s.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequenceElement {

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
