/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Extended {@link IPortTypeProvider} interface providing setter capabilities.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortTypeProviderEditor extends IPortTypeProvider {

	/**
	 * Set the {@link PortType} for this sequence element
	 * 
	 * @param sequenceElement
	 * @param portType
	 */
	void setPortType(ISequenceElement sequenceElement, PortType portType);
}
