/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Default implementation of {@link IModifiableSequences}. Uses {@link ListModifiableSequence} instances when required.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public final class ModifiableSequences implements IModifiableSequences {

	@NonNull
	private final List<@NonNull IResource> resources;

	@NonNull
	private final Map<@NonNull IResource, @NonNull IModifiableSequence> sequenceMap;

	@NonNull
	private final List<@NonNull ISequenceElement> unusedElements = new ArrayList<>();

	/**
	 * Constructor taking a list of {@link IResource} instances. The {@link IModifiableSequence} instances will be created automatically. The resources list is copied to maintain internal consistency
	 * with the sequence map.
	 * 
	 * @param resources
	 */
	public ModifiableSequences(@NonNull final List<@NonNull IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = new ArrayList<>(resources);
		sequenceMap = new LinkedHashMap<>();
		for (final IResource resource : resources) {
			sequenceMap.put(resource, new ListModifiableSequence(new LinkedList<>()));
		}
	}

	/**
	 * Constructor taking both the ordered list of resources and a {@link Map} containing the initial sequences. References are maintained to both objects.
	 * 
	 * @param resources
	 * @param sequenceMap
	 */
	public ModifiableSequences(@NonNull final List<@NonNull IResource> resources, @NonNull final Map<@NonNull IResource, @NonNull IModifiableSequence> sequenceMap) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
	}

	/**
	 * Constructor which creates a modifiable copy of the input {@link ISequences} object.
	 * 
	 * @param sequences
	 *            Source {@link ISequences} object
	 */
	public ModifiableSequences(@NonNull final ISequences sequences) {

		this.resources = new ArrayList<>(sequences.getResources());
		this.sequenceMap = new HashMap<>();
		for (final IResource r : resources) {

			// Get original sequence
			final ISequence seq = sequences.getSequence(r);

			// Create a modifiable sequence object
			final ListModifiableSequence modifiableSequence = new ListModifiableSequence(new ArrayList<>(seq.size()));

			// Set contents
			modifiableSequence.replaceAll(seq);

			// store the new object
			sequenceMap.put(r, modifiableSequence);
		}
		this.unusedElements.addAll(sequences.getUnusedElements());
	}

	@Override
	@NonNull
	public IModifiableSequence getModifiableSequence(@NonNull final IResource resource) {

		final IModifiableSequence seq = sequenceMap.get(resource);
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource");
		}
		return seq;
	}

	@Override
	@NonNull
	public IModifiableSequence getModifiableSequence(final int index) {
		final IModifiableSequence seq = sequenceMap.get(resources.get(index));
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
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
		final IModifiableSequence seq = sequenceMap.get(resource);
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource");
		}
		return seq;
	}

	@Override
	@NonNull
	public ISequence getSequence(final int index) {
		final IModifiableSequence seq = sequenceMap.get(resources.get(index));
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@Override
	@NonNull
	public Map<@NonNull IResource, @NonNull ISequence> getSequences() {

		// Create a copy so external modification does not affect internal
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<@NonNull IResource, @NonNull ISequence> map = new HashMap<>(sequenceMap);

		return map;
	}

	@Override
	@NonNull
	public Map<@NonNull IResource, @NonNull IModifiableSequence> getModifiableSequences() {

		// Create a copy so external modification does not affect internal
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<@NonNull IResource, @NonNull IModifiableSequence> map = new HashMap<>(sequenceMap);

		return map;
	}

	@Override
	public int size() {

		return sequenceMap.size();
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof ISequences) {
			final ISequences other = (ISequences) obj;
			if (size() != other.size()) {
				return false;
			}

			if (!this.getSequences().equals(other.getSequences())) {
				return false;
			}

			if (!new HashSet<>(this.getUnusedElements()).equals(new HashSet<>(other.getUnusedElements()))) {
				return false;
			}

			return true;

		}

		return false;
	}

	@Override
	public int hashCode() {

		return Objects.hashCode(this.resources, this.sequenceMap, new HashSet<>(this.unusedElements));
	}

	@SuppressWarnings("null")
	@Override
	@NonNull
	public List<@NonNull ISequenceElement> getUnusedElements() {
		return Collections.unmodifiableList(unusedElements);
	}

	@Override
	@NonNull
	public List<@NonNull ISequenceElement> getModifiableUnusedElements() {
		return unusedElements;
	}
}
