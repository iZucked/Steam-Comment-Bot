/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Default implementation of {@link ISequences}. Uses {@link ListSequence} instances when required.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public final class Sequences implements ISequences {

	private final ImmutableList<@NonNull IResource> resources;

	private final ImmutableMap<@NonNull IResource, @NonNull ISequence> sequenceMap;

	private final ImmutableList<@NonNull ISequenceElement> unusedElements;

	/**
	 * Constructor taking a list of {@link IResource} instances. The {@link ISequence} instances will be created automatically. The resources list is copied to maintain internal consistency with the
	 * sequence map.
	 * 
	 * @param resources
	 */
	public Sequences(final @NonNull List<@NonNull IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = ImmutableList.copyOf(new ArrayList<>(resources));
		Map<@NonNull IResource, @NonNull ISequence> tmpSequenceMap = new HashMap<>();
		for (final IResource resource : resources) {
			tmpSequenceMap.put(resource, new ListSequence(new LinkedList<>()));
		}
		sequenceMap = ImmutableMap.copyOf(tmpSequenceMap);
		this.unusedElements = ImmutableList.of();

	}

	/**
	 * Constructor taking both the ordered list of resources and a {@link Map} containing the initial sequences. References are maintained to both objects.
	 * 
	 * @param resources
	 * @param sequenceMap
	 */
	public Sequences(final List<@NonNull IResource> resources, final Map<@NonNull IResource, @NonNull ISequence> sequenceMap) {
		this.resources = ImmutableList.copyOf(new ArrayList<>(resources));
		this.sequenceMap = ImmutableMap.copyOf(new HashMap<>(sequenceMap));
		this.unusedElements = ImmutableList.of();
	}

	public Sequences(final List<@NonNull IResource> resources, final Map<@NonNull IResource, @NonNull ISequence> sequenceMap, final @NonNull List<@NonNull ISequenceElement> unusedElements) {
		this.resources = ImmutableList.copyOf(new ArrayList<>(resources));
		this.sequenceMap = ImmutableMap.copyOf(new HashMap<>(sequenceMap));
		this.unusedElements = ImmutableList.copyOf(new ArrayList<>(unusedElements));
	}

	/**
	 * Constructor which creates a deep copy of the input {@link ISequences} object. This includes creating new {@link ISequence} objects, but not the sequence elements.
	 * 
	 * @param sequences
	 *            Source {@link ISequences} object
	 */
	public Sequences(final ISequences sequences) {

		this.resources = ImmutableList.copyOf(new ArrayList<>(sequences.getResources()));

		Map<@NonNull IResource, @NonNull ISequence> tmpSequenceMap = new HashMap<>();
		for (final IResource resource : resources) {
			// Get original sequence
			final ISequence seq = sequences.getSequence(resource);

			// Create a copied sequence object
			final ListSequence sequence = new ListSequence(seq);

			// store the new object
			tmpSequenceMap.put(resource, sequence);
		}
		sequenceMap = ImmutableMap.copyOf(tmpSequenceMap);
		this.unusedElements = ImmutableList.copyOf(new ArrayList<>(sequences.getUnusedElements()));

	}

	@Override
	@NonNull
	public ImmutableList<@NonNull IResource> getResources() {
		return ImmutableList.copyOf(resources);
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
	public ImmutableMap<@NonNull IResource, @NonNull ISequence> getSequences() {

		// Create a copy so external modification does not affect internal
		// state.
		return ImmutableMap.copyOf(sequenceMap);
	}

	@Override
	public int size() {

		return sequenceMap.size();
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof Sequences) {
			return sequenceMap.equals(((Sequences) obj).sequenceMap);
		} else if (obj instanceof ISequences) {
			final ISequences seq = (ISequences) obj;
			if (size() != seq.size()) {
				return false;
			}

			if (!seq.getSequences().equals(this.getSequences())) {
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
	public ImmutableList<@NonNull ISequenceElement> getUnusedElements() {
		return ImmutableList.copyOf(unusedElements);
	}
}
