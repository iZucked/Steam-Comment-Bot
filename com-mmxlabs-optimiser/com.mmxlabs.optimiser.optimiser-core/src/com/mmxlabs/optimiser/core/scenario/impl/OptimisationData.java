/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.impl;

import java.util.List;

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

	private List<IResource> resources;

	private List<ISequenceElement> sequenceElements;

	public OptimisationData() {
	}

	@Override
	public List<ISequenceElement> getSequenceElements() {
		return sequenceElements;
	}

	/**
	 * Set reference to the {@link List} of sequence elements contained in this optimisation.
	 * 
	 * @param sequenceElements
	 */
	public void setSequenceElements(final List<ISequenceElement> sequenceElements) {
		this.sequenceElements = sequenceElements;
	}

	@Override
	public List<IResource> getResources() {
		return resources;
	}

	/**
	 * Set reference to {@link List} of {@link IResource}s used by this optimisation.
	 * 
	 * @param resources
	 */
	public void setResources(final List<IResource> resources) {
		this.resources = resources;
	}

	@Override
	public void dispose() {

		resources = null;
		sequenceElements = null;
	}
}
