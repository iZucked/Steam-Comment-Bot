/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProviderEditor;

public final class HashMapStartEndRequirementEditor implements IStartEndRequirementProviderEditor {

	private int notionalEndTime;

	protected HashMap<IResource, IStartRequirement> startRequirements = new HashMap<>();
	protected HashMap<IResource, IEndRequirement> endRequirements = new HashMap<>();

	protected HashMap<IResource, ISequenceElement> startElements = new HashMap<>();
	protected HashMap<IResource, ISequenceElement> endElements = new HashMap<>();

	@Override
	public IStartRequirement getStartRequirement(final @NonNull IResource resource) {
		IStartRequirement requirement = startRequirements.get(resource);
		assert requirement != null;
		return requirement;
	}

	@Override
	public IEndRequirement getEndRequirement(final IResource resource) {
		IEndRequirement requirement = endRequirements.get(resource);
		assert requirement != null;
		return requirement;
	}

	@Override
	public void setStartEndRequirements(final @NonNull IResource resource, final @NonNull IStartRequirement startRequirement, final @NonNull IEndRequirement endRequirement) {
		startRequirements.put(resource, startRequirement);
		endRequirements.put(resource, endRequirement);
	}

	@Override
	public ISequenceElement getEndElement(final @NonNull IResource resource) {
		return endElements.get(resource);
	}

	@Override
	public ISequenceElement getStartElement(final @NonNull IResource resource) {
		return startElements.get(resource);
	}

	@Override
	public void setStartEndElements(final IResource resource, final ISequenceElement startElement, final ISequenceElement endElement) {
		startElements.put(resource, startElement);
		endElements.put(resource, endElement);
	}

	@Override
	public int getNotionalEndTime() {
		return notionalEndTime;
	}

	@Override
	public void setNotionalEndTime(int endTime) {
		this.notionalEndTime = endTime;
	}
}
