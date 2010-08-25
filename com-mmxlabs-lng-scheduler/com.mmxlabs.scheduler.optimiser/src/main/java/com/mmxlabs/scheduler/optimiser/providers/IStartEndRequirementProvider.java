package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;

public interface IStartEndRequirementProvider extends IDataComponentProvider {
	public IStartEndRequirement getStartRequirement(IResource resource);
	public IStartEndRequirement getEndRequirement(IResource resource);
}
