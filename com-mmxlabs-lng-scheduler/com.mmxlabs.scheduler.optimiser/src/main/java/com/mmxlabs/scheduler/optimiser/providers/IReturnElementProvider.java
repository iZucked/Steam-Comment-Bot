/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator;

/**
 * Provides the return elements for {@link EndLocationSequenceManipulator}
 * 
 * @author hinton
 * 
 */
public interface IReturnElementProvider extends IDataComponentProvider {
	public ISequenceElement getReturnElement(IResource resource, IPort port);
}
