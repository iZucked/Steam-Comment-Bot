/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public interface IStartEndRequirementProvider<T> extends IDataComponentProvider {
	public IStartEndRequirement getStartRequirement(IResource resource);
	public IStartEndRequirement getEndRequirement(IResource resource);
	public T getEndElement(IResource resource);
	public T getStartElement(IResource resource);
}
