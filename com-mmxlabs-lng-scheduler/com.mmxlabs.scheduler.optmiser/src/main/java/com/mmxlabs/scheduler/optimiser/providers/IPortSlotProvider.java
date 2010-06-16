package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IDataComponentProvider} interface to provide {@link IPortSlot}
 * information for the given sequence elements.
 * 
 * @author Simon Goodall
 * @param <T>
 *            Sequence element type
 */
public interface IPortSlotProvider<T> extends IDataComponentProvider {

	/**
	 * Returns the {@link IPortSlot} for the given element.
	 * 
	 * @param element
	 * @return
	 */
	IPortSlot getPortSlot(T element);

	/**
	 * Returns the sequence element for the given {@link IPortSlot}.
	 * 
	 * @param portSlot
	 * @return
	 */
	T getElement(IPortSlot portSlot);
}
