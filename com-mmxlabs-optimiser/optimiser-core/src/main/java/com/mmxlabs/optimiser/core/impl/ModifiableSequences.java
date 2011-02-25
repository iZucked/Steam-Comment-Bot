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

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Default implementation of {@link IModifiableSequences}. Uses
 * {@link ListModifiableSequence} instances when required.
 * 
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class ModifiableSequences<T> implements IModifiableSequences<T> {

	private final List<IResource> resources;

	private final Map<IResource, IModifiableSequence<T>> sequenceMap;

	/**
	 * Constructor taking a list of {@link IResource} instances. The
	 * {@link IModifiableSequence} instances will be created automatically. The
	 * resources list is copied to maintain internal consistency with the
	 * sequence map.
	 * 
	 * @param resources
	 */
	public ModifiableSequences(final List<IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = new ArrayList<IResource>(resources);
		sequenceMap = new HashMap<IResource, IModifiableSequence<T>>();
		for (final IResource resource : resources) {
			sequenceMap.put(resource, new ListModifiableSequence<T>(
					new LinkedList<T>()));
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
	public ModifiableSequences(final List<IResource> resources,
			final Map<IResource, IModifiableSequence<T>> sequenceMap) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
	}

	/**
	 * Constructor which creates a modifiable copy of the input
	 * {@link ISequences} object.
	 * 
	 * @param sequences
	 *            Source {@link ISequences} object
	 */
	public ModifiableSequences(final ISequences<T> sequences) {

		this.resources = new ArrayList<IResource>(sequences.getResources());

		this.sequenceMap = new HashMap<IResource, IModifiableSequence<T>>();
		for (final IResource r : resources) {
			// Get original sequence
			final ISequence<T> seq = sequences.getSequence(r);

			// Create a modifiable sequence object
			final ListModifiableSequence<T> modifiableSequence = new ListModifiableSequence<T>(
					new ArrayList<T>(seq.size()));

			// Set contents
			modifiableSequence.replaceAll(seq);

			// store the new object
			sequenceMap.put(r, modifiableSequence);
		}
	}

	@Override
	public IModifiableSequence<T> getModifiableSequence(final IResource resource) {

		return sequenceMap.get(resource);
	}

	@Override
	public IModifiableSequence<T> getModifiableSequence(final int index) {
		return sequenceMap.get(resources.get(index));
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
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<IResource, ISequence<T>> map = new HashMap<IResource, ISequence<T>>(
				sequenceMap);

		return map;
	}

	@Override
	public Map<IResource, IModifiableSequence<T>> getModifiableSequences() {

		// Create a copy so external modification does not affect internal
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<IResource, IModifiableSequence<T>> map = new HashMap<IResource, IModifiableSequence<T>>(
				sequenceMap);

		return map;
	}
	
	@Override
	public int size() {

		return sequenceMap.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ModifiableSequences) {
			return sequenceMap.equals(((ModifiableSequences) obj).sequenceMap);
		} else if (obj instanceof ISequences) {
			ISequences seq = (ISequences) obj;
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
}
