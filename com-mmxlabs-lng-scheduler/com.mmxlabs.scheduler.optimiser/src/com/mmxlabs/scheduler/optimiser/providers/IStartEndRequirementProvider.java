/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public interface IStartEndRequirementProvider extends IDataComponentProvider {
	public IStartRequirement getStartRequirement(IResource resource);

	public IEndRequirement getEndRequirement(IResource resource);

	public ISequenceElement getEndElement(IResource resource);

	public ISequenceElement getStartElement(IResource resource);
}
