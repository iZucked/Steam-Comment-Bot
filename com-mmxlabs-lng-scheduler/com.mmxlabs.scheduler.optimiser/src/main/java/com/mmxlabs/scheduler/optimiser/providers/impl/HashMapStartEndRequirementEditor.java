package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;

public class HashMapStartEndRequirementEditor implements
		IStartEndRequirementProviderEditor {

	protected HashMap<IResource, IStartEndRequirement> startRequirements = new HashMap<IResource, IStartEndRequirement>();
	protected HashMap<IResource, IStartEndRequirement> endRequirements = new HashMap<IResource, IStartEndRequirement>();
	private final String name;
	
	public HashMapStartEndRequirementEditor(String name) {
		this.name = name;
	}
	
	@Override
	public IStartEndRequirement getStartRequirement(IResource resource) {
		return startRequirements.get(resource);
	}

	@Override
	public IStartEndRequirement getEndRequirement(IResource resource) {
		return endRequirements.get(resource);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		startRequirements.clear();
		endRequirements.clear();
	}

	@Override
	public void setStartEndRequirements(IResource resource,
			IStartEndRequirement startRequirement,
			IStartEndRequirement endRequirement) {
		startRequirements.put(resource, startRequirement);
		endRequirements.put(resource, endRequirement);
	}
}
