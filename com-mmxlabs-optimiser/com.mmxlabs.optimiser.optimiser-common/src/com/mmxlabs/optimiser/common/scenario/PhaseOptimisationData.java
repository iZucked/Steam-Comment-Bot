/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.mmxlabs.common.indexedobjects.IIndexBits;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexBits;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

/**
 * Implementation of {@link IOptimisationData}
 * 
 * @author Simon Goodall
 * 
 */
public final class PhaseOptimisationData implements IPhaseOptimisationData {

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;
	private ImmutableList<@NonNull ISequenceElement> combinedOptionalElements = null;

	private ImmutableList<IResource> resources = ImmutableList.of();

	private ImmutableList<ISequenceElement> sequenceElements = ImmutableList.of();

	private ImmutableList<@NonNull ISequenceElement> consideredAsOptionalElementsList = ImmutableList.of();
	private final IIndexBits<ISequenceElement> consideredAsOptionalElements = new ArrayIndexBits<>();

	@Override
	public ImmutableList<ISequenceElement> getSequenceElements() {
		return sequenceElements;
	}

	/**
	 * Set reference to the {@link List} of sequence elements contained in this optimisation.
	 * 
	 * @param sequenceElements
	 */
	public void setSequenceElements(final List<ISequenceElement> sequenceElements) {
		this.sequenceElements = ImmutableList.copyOf(new ArrayList<>(sequenceElements));
	}

	public void setConsideredAsOptionalElements(final Collection<ISequenceElement> elements) {

		// Update list
		consideredAsOptionalElementsList = ImmutableList.copyOf(new ArrayList<>(elements));

		// Update fast lookup thing
		consideredAsOptionalElements.clearAll();
		consideredAsOptionalElementsList.forEach(consideredAsOptionalElements::set);

		final List<ISequenceElement> combinedList = new LinkedList<>(optionalElementsProvider.getOptionalElements());
		combinedList.addAll(consideredAsOptionalElementsList);

		combinedOptionalElements = ImmutableList.copyOf(combinedList);
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
	public void setResources(final List<IResource> resources) {
		this.resources = ImmutableList.copyOf(new ArrayList<>(resources));
	}

	@Override
	public ImmutableList<@NonNull ISequenceElement> getOptionalElements() {
		return optionalElementsProvider.getOptionalElements();
	}

	@Override
	public ImmutableList<@NonNull ISequenceElement> getConsideredAsOptionalElements() {
		return consideredAsOptionalElementsList;
	}

	@Override
	public ImmutableList<@NonNull ISequenceElement> getAllElementsConsideredOptional() {
		return combinedOptionalElements == null ? getOptionalElements() : combinedOptionalElements;
	}

	@Override
	public boolean isOptionalElement(@NonNull final ISequenceElement element) {
		return optionalElementsProvider.isElementOptional(element);
	}

	@Override
	public boolean isConsideredAsOptionalElement(@NonNull final ISequenceElement element) {
		return consideredAsOptionalElements.isSet(element);
	}

	@Override
	public boolean isOptionalOrConsideredOptionalElement(@NonNull final ISequenceElement element) {
		return optionalElementsProvider.isElementOptional(element) || consideredAsOptionalElements.isSet(element);
	}
}
