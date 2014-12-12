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
	IStartRequirement getStartRequirement(IResource resource);

	IEndRequirement getEndRequirement(IResource resource);

	ISequenceElement getEndElement(IResource resource);

	ISequenceElement getStartElement(IResource resource);
}
