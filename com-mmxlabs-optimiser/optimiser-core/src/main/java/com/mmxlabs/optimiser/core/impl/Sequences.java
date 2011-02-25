/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Default implementation of {@link ISequences}. Uses {@link ListSequence}
 * instances when required.
 * 
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class Sequences<T> implements ISequences<T> {

	private final List<IResource> resources;

	private final Map<IResource, ISequence<T>> sequenceMap;

	/**
	 * Constructor taking a list of {@link IResource} instances. The
	 * {@link ISequence} instances will be created automatically. The resources
	 * list is copied to maintain internal consistency with the sequence map.
	 * 
	 * @param resources
	 */
	public Sequences(final List<IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = new ArrayList<IResource>(resources);
		sequenceMap = new HashMap<IResource, ISequence<T>>();
		for (final IResource resource : resources) {
			sequenceMap.put(resource, new ListSequence<T>(new LinkedList<T>()));
		}
	}

	/**
	 * Constructor taking both the ordered list of resources and a {@link Map}
	 * containing the initial sequences. References are maintained to both
	 * objects.
	 * 
	 * @param resources
	 * @param sequenceMap
	 */
	public Sequences(final List<IResource> resources,
			final Map<IResource, ISequence<T>> sequenceMap) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
	}

	/**
	 * Constructor which creates a deep copy of the input {@link ISequences}
	 * object. This includes creating new {@link ISequence} objects, but not the
	 * sequence elements.
	 * 
	 * @param sequences
	 *            Source {@link ISequences} object
	 */
	public Sequences(final ISequences<T> sequences) {

		this.resources = new ArrayList<IResource>(sequences.getResources());

		this.sequenceMap = new HashMap<IResource, ISequence<T>>();
		for (final IResource r : resources) {
			// Get original sequence
			final ISequence<T> seq = sequences.getSequence(r);

			// Create a copied sequence object
			final ListSequence<T> sequence = new ListSequence<T>(seq);

			// store the new object
			sequenceMap.put(r, sequence);
		}
	}

	@Override
	public List<IResource> getResources() {
		return Collections.unmodifiableList(resources);
	}

	@Override
	public ISequence<T> getSequence(final IResource resource) {
		return sequenceMap.get(resource);
	}

	@Override
	public ISequence<T> getSequence(final int index) {
		return sequenceMap.get(resources.get(index));
	}

	@Override
	public Map<IResource, ISequence<T>> getSequences() {

		// Create a copy so external modification does not affect internal
		// state.
		final Map<IResource, ISequence<T>> map = Collections
				.unmodifiableMap(sequenceMap);
		return map;
	}

	@Override
	public int size() {

		return sequenceMap.size();
	}

	@SuppressWarnings("rawtypes")
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
}
