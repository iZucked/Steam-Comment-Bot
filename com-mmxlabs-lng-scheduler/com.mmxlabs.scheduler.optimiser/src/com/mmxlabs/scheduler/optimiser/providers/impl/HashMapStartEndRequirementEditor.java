/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;

public class HashMapStartEndRequirementEditor implements IStartEndRequirementProviderEditor {

	protected HashMap<IResource, IStartEndRequirement> startRequirements = new HashMap<IResource, IStartEndRequirement>();
	protected HashMap<IResource, IStartEndRequirement> endRequirements = new HashMap<IResource, IStartEndRequirement>();

	protected HashMap<IResource, ISequenceElement> startElements = new HashMap<IResource, ISequenceElement>();
	protected HashMap<IResource, ISequenceElement> endElements = new HashMap<IResource, ISequenceElement>();

	private final String name;

	public HashMapStartEndRequirementEditor(final String name) {
		this.name = name;
	}

	@Override
	public IStartEndRequirement getStartRequirement(final IResource resource) {
		return startRequirements.get(resource);
	}

	@Override
	public IStartEndRequirement getEndRequirement(final IResource resource) {
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

		startElements.clear();
		endElements.clear();
	}

	@Override
	public void setStartEndRequirements(final IResource resource, final IStartEndRequirement startRequirement, final IStartEndRequirement endRequirement) {
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
