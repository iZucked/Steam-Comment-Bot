/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	@Nullable ISequenceElement getReturnElement(@NonNull IResource resource, @NonNull IPort port);
}
