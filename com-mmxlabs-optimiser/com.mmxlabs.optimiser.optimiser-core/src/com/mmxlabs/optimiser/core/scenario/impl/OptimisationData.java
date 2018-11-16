/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Implementation of {@link IOptimisationData}
 * 
 * @author Simon Goodall
 * 
 */
public final class OptimisationData implements IOptimisationData {

	private ImmutableList<IResource> resources;

	private ImmutableList<ISequenceElement> sequenceElements;

	private ImmutableList<ISequenceElement> consideredAsOptionalElements;

	@Override
	public ImmutableList<ISequenceElement> getSequenceElements() {
		return sequenceElements;
	}

	/**
	 * Set reference to the {@link List} of sequence elements contained in this optimisation.
	 * 
	 * @param sequenceElements
	 */
	public void setSequenceElements(final ImmutableList<ISequenceElement> sequenceElements) {
		this.sequenceElements = sequenceElements;
	}

	@Override
	public ImmutableList<IResource> getResources() {
		return resources;
	}

	/**
	 * Set reference to {@link List} of {@link IResource}s used by this optimisation.
	 * 
	 * @param resources
	 */
	public void setResources(final ImmutableList<IResource> resources) {
		this.resources = resources;
	}

	public void setConsideredAsOptionalElements(ImmutableList<ISequenceElement> consideredAsOptionalElements) {
		this.consideredAsOptionalElements = consideredAsOptionalElements;
	}

	@Override
	public ImmutableList<ISequenceElement> getConsideredAsOptionalElements() {
		return consideredAsOptionalElements;
	}
}
