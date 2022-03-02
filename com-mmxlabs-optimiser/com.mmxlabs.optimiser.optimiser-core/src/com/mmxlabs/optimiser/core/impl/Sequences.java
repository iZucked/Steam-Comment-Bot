/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;

/**
 * Default implementation of {@link ISequences}. Uses {@link ListSequence}
 * instances when required.
 * 
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class Sequences implements ISequences {

	private final List<IResource> resources;

	private final Map<IResource, ISequence> sequenceMap;

	private final List<ISequenceElement> unusedElements = new ArrayList<>();

	private final ISequencesAttributesProvider providers;

	public Sequences(final List<IResource> resources, final Map<IResource, ISequence> sequenceMap, final List<ISequenceElement> unusedElements, ISequencesAttributesProvider providers) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
		this.unusedElements.addAll(unusedElements);
		this.providers = providers;
	}

	/**
	 * Constructor which creates a deep copy of the input {@link ISequences} object.
	 * This includes creating new {@link ISequence} objects, but not the sequence
	 * elements.
	 * 
	 * @param sequences Source {@link ISequences} object
	 */
	public Sequences(final ISequences sequences) {

		this.resources = new ArrayList<>(sequences.getResources());

		this.sequenceMap = new HashMap<>();
		for (final IResource r : resources) {
			// Get original sequence
			final ISequence seq = sequences.getSequence(r);

			// Create a copied sequence object
			final ListSequence sequence = new ListSequence(seq);

			// store the new object
			sequenceMap.put(r, sequence);
		}
		this.unusedElements.addAll(sequences.getUnusedElements());
		this.providers = sequences.getProviders();
	}

	@Override
	@NonNull
	public List<@NonNull IResource> getResources() {
		return Collections.unmodifiableList(resources);
	}

	@Override
	@NonNull
	public ISequence getSequence(@NonNull final IResource resource) {
		final ISequence sequence = sequenceMap.get(resource);
		if (sequence == null) {
			throw new IllegalArgumentException("Unknown resource");
		}
		return sequence;
	}

	@Override
	@NonNull
	public ISequence getSequence(final int index) {
		final ISequence seq = sequenceMap.get(resources.get(index));
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@SuppressWarnings("null")
	@Override
	@NonNull
	public Map<@NonNull IResource, @NonNull ISequence> getSequences() {

		// Create a copy so external modification does not affect internal
		// state.
		return Collections.unmodifiableMap(sequenceMap);
	}

	@Override
	public int size() {

		return sequenceMap.size();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {

		if (obj instanceof ISequences) {
			final ISequences other = (ISequences) obj;
			if (size() != other.size()) {
				return false;
			}

			if (!Objects.equal(providers, other.getProviders())) {
				return false;
			}
			if (!Objects.equal(getSequences(), other.getSequences())) {
				return false;
			}

			return true;

		}

		return false;
	}

	@Override
	public int hashCode() {
		return sequenceMap.hashCode();
	}

	@Override
	@NonNull
	public List<@NonNull ISequenceElement> getUnusedElements() {
		return Collections.unmodifiableList(unusedElements);
	}

	@Override
	public ISequencesAttributesProvider getProviders() {
		return providers;
	}
}
