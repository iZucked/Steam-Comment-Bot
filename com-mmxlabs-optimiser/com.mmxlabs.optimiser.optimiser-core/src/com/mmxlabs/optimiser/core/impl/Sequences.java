/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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

	private final List<IResource> resources;

	private final Map<IResource, ISequence> sequenceMap;

	final List<ISequenceElement> unusedElements = new ArrayList<ISequenceElement>();

	/**
	 * Constructor taking a list of {@link IResource} instances. The {@link ISequence} instances will be created automatically. The resources list is copied to maintain internal consistency with the
	 * sequence map.
	 * 
	 * @param resources
	 */
	public Sequences(final List<IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = new ArrayList<IResource>(resources);
		sequenceMap = new HashMap<IResource, ISequence>();
		for (final IResource resource : resources) {
			sequenceMap.put(resource, new ListSequence(new LinkedList<ISequenceElement>()));
		}
	}

	/**
	 * Constructor taking both the ordered list of resources and a {@link Map} containing the initial sequences. References are maintained to both objects.
	 * 
	 * @param resources
	 * @param sequenceMap
	 */
	public Sequences(final List<IResource> resources, final Map<IResource, ISequence> sequenceMap) {
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

		this.resources = new ArrayList<IResource>(sequences.getResources());

		this.sequenceMap = new HashMap<IResource, ISequence>();
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

	@Override
	public List<IResource> getResources() {
		return Collections.unmodifiableList(resources);
	}

	@Override
	public ISequence getSequence(final IResource resource) {
		return sequenceMap.get(resource);
	}

	@Override
	public ISequence getSequence(final int index) {
		return sequenceMap.get(resources.get(index));
	}

	@Override
	public Map<IResource, ISequence> getSequences() {

		// Create a copy so external modification does not affect internal
		// state.
		final Map<IResource, ISequence> map = Collections.unmodifiableMap(sequenceMap);
		return map;
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

	@Override
	public List<ISequenceElement> getUnusedElements() {
		return Collections.unmodifiableList(unusedElements);
	}
}
