package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public interface IStartEndRequirementProviderEditor extends IStartEndRequirementProvider {
	public void setStartEndRequirements(IResource resource, IStartEndRequirement startRequirement, IStartEndRequirement endRequirement);
}
