/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.indexedobjects.IIndexBits;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexBits;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

/**
 * Implementation of {@link IPhaseOptimisationData}
 * 
 * @author Simon Goodall
 * 
 */
public final class PhaseOptimisationData implements IPhaseOptimisationData {

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	private List<IResource> resources = new ArrayList<>();

	private List<ISequenceElement> sequenceElements = new ArrayList<>();

	private final List<@NonNull ISequenceElement> optionalList = new ArrayList<>();
	private final List<@NonNull ISequenceElement> softRequiredList = new ArrayList<>();
	private final IIndexBits<ISequenceElement> optionalElements = new ArrayIndexBits<>();

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
		for (ISequenceElement element : sequenceElements) {
			if (optionalElementsProvider.getSoftRequiredElements().contains(element)) {
				softRequiredList.add(element);
			}
			if (optionalElementsProvider.isElementOptional(element)) {
				optionalElements.set(element);
				optionalList.add(element);
			}
		}
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

	@Override
	public boolean isElementOptional(final ISequenceElement element) {
		return optionalElements.isSet(element);
	}

	@Override
	public boolean isElementRequired(final ISequenceElement element) {
		return !isElementOptional(element);
	}

	@Override
	public List<@NonNull ISequenceElement> getOptionalElements() {
		return Collections.unmodifiableList(optionalList);
	}

	@Override
	public List<@NonNull ISequenceElement> getSoftRequiredElements() {
		return Collections.unmodifiableList(softRequiredList);
	}
}
