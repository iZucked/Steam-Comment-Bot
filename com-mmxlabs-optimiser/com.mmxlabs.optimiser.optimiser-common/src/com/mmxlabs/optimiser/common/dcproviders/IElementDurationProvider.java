/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} to provide durations for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public interface IElementDurationProvider extends IDataComponentProvider {

	/**
	 * Return the duration set for the given element for the {@link IResource} If no duration has been set, then the default value {@link #getDefaultValue()} will be returned.
	 * 
	 * @param element
	 * @param resource
	 * @return
	 */
	int getElementDuration(ISequenceElement element, IResource resource);

	/**
	 * The default element duration.
	 * 
	 * @return
	 */
	int getDefaultValue();
}
