/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

	private final List<@NonNull IResource> resources;

	private final Map<@NonNull IResource, @NonNull ISequence> sequenceMap;

	final List<@NonNull ISequenceElement> unusedElements = new ArrayList<>();

	/**
	 * Constructor taking a list of {@link IResource} instances. The {@link ISequence} instances will be created automatically. The resources list is copied to maintain internal consistency with the
	 * sequence map.
	 * 
	 * @param resources
	 */
	public Sequences(final @NonNull List<@NonNull IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = new ArrayList<>(resources);
		sequenceMap = new HashMap<>();
		for (final IResource resource : resources) {
			sequenceMap.put(resource, new ListSequence(new LinkedList<>()));
		}
	}

	/**
	 * Constructor taking both the ordered list of resources and a {@link Map} containing the initial sequences. References are maintained to both objects.
	 * 
	 * @param resources
	 * @param sequenceMap
	 */
	public Sequences(final List<@NonNull IResource> resources, final Map<@NonNull IResource, @NonNull ISequence> sequenceMap) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
	}

	/**
	 * Constructor which creates a deep copy of the input {@link ISequences} object. This includes creating new {@link ISequence} objects, but not the sequence elements.
	 * 
	 * @param sequences
	 *            Source {@link ISequences} object
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
	}

	@SuppressWarnings("null")
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
	public boolean equals(final Object obj) {

		if (obj instanceof Sequences) {
			return sequenceMap.equals(((Sequences) obj).sequenceMap);
		} else if (obj instanceof ISequences) {
			final ISequences seq = (ISequences) obj;
			if (size() != seq.size()) {
				return false;
			}

			if (!seq.getSequences().equals(seq.getSequences())) {
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

	@SuppressWarnings("null")
	@Override
	@NonNull
	public List<@NonNull ISequenceElement> getUnusedElements() {
		return Collections.unmodifiableList(unusedElements);
	}
}
