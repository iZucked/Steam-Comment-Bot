/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;

public interface IStartEndRequirementProviderEditor extends IStartEndRequirementProvider {

	void setStartEndRequirements(IResource resource, IStartRequirement startRequirement, IEndRequirement endRequirement);

	void setStartEndElements(IResource resource, ISequenceElement startElement, ISequenceElement endElement);
}
