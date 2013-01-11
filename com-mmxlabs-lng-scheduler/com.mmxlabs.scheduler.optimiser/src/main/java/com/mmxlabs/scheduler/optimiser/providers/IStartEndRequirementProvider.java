/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public interface IStartEndRequirementProvider extends IDataComponentProvider {
	public IStartEndRequirement getStartRequirement(IResource resource);

	public IStartEndRequirement getEndRequirement(IResource resource);

	public ISequenceElement getEndElement(IResource resource);

	public ISequenceElement getStartElement(IResource resource);
}
