/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public interface IStartEndRequirementProviderEditor<T> extends IStartEndRequirementProvider<T> {
	public void setStartEndRequirements(IResource resource, IStartEndRequirement startRequirement, IStartEndRequirement endRequirement);
	public void setStartEndElements(IResource resource, T startElement, T endElement);
}
