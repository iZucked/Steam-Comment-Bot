/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public interface IStartEndRequirementProviderEditor extends IStartEndRequirementProvider {
	public void setStartEndRequirements(IResource resource, IStartEndRequirement startRequirement, IStartEndRequirement endRequirement);

	public void setStartEndElements(IResource resource, ISequenceElement startElement, ISequenceElement endElement);
}
