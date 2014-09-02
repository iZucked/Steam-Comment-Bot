/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;

public class HashMapStartEndRequirementEditor implements IStartEndRequirementProviderEditor {

	protected HashMap<IResource, IStartRequirement> startRequirements = new HashMap<>();
	protected HashMap<IResource, IEndRequirement> endRequirements = new HashMap<>();

	protected HashMap<IResource, ISequenceElement> startElements = new HashMap<IResource, ISequenceElement>();
	protected HashMap<IResource, ISequenceElement> endElements = new HashMap<IResource, ISequenceElement>();

	@Override
	public IStartRequirement getStartRequirement(final IResource resource) {
		return startRequirements.get(resource);
	}

	@Override
	public IEndRequirement getEndRequirement(final IResource resource) {
		return endRequirements.get(resource);
	}

	@Override
	public void setStartEndRequirements(final IResource resource, final IStartRequirement startRequirement, final IEndRequirement endRequirement) {
		startRequirements.put(resource, startRequirement);
		endRequirements.put(resource, endRequirement);
	}

	@Override
	public ISequenceElement getEndElement(final IResource resource) {
		return endElements.get(resource);
	}

	@Override
	public ISequenceElement getStartElement(final IResource resource) {
		return startElements.get(resource);
	}

	@Override
	public void setStartEndElements(final IResource resource, final ISequenceElement startElement, final ISequenceElement endElement) {
		startElements.put(resource, startElement);
		endElements.put(resource, endElement);
	}
}
