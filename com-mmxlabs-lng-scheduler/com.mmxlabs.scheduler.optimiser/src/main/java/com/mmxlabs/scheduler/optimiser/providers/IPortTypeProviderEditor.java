/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers;

/**
 * Extended {@link IPortTypeProvider} interface providing setter capabilities.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IPortTypeProviderEditor<T> extends IPortTypeProvider<T> {

	/**
	 * Set the {@link PortType} for this sequence element
	 * 
	 * @param sequenceElement
	 * @param portType
	 */
	void setPortType(T sequenceElement, PortType portType);
}
