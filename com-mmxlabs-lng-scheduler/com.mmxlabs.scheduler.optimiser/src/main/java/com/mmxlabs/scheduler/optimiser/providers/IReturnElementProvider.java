/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator;

/**
 * Provides the return elements for {@link EndLocationSequenceManipulator}
 * @author hinton
 *
 * @param <T>
 */
public interface IReturnElementProvider<T> extends IDataComponentProvider {
	public T getReturnElement(final IPort port);
}
