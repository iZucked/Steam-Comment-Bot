/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	private final List<IResource> resources;

	private final Map<IResource, IModifiableSequence> sequenceMap;

	private final List<ISequenceElement> unusedElements = new ArrayList<ISequenceElement>();

	/**
	 * Constructor taking a list of {@link IResource} instances. The {@link IModifiableSequence} instances will be created automatically. The resources list is copied to maintain internal consistency
	 * with the sequence map.
	 * 
	 * @param resources
	 */
	public ModifiableSequences(final List<IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = new ArrayList<IResource>(resources);
		sequenceMap = new LinkedHashMap<IResource, IModifiableSequence>();
		for (final IResource resource : resources) {
			sequenceMap.put(resource, new ListModifiableSequence(new LinkedList<ISequenceElement>()));
		}
	}

	/**
	 * Constructor taking both the ordered list of resources and a {@link Map} containing the initial sequences. References are maintained to both objects.
	 * 
	 * @param resources
	 * @param sequenceMap
	 */
	public ModifiableSequences(final List<IResource> resources, final Map<IResource, IModifiableSequence> sequenceMap) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
	}

	/**
	 * Constructor which creates a modifiable copy of the input {@link ISequences} object.
	 * 
	 * @param sequences
	 *            Source {@link ISequences} object
	 */
	public ModifiableSequences(final ISequences sequences) {

		this.resources = new ArrayList<IResource>(sequences.getResources());

		this.sequenceMap = new HashMap<IResource, IModifiableSequence>();
		for (final IResource r : resources) {
			// Get original sequence
			final ISequence seq = sequences.getSequence(r);

			// Create a modifiable sequence object
			final ListModifiableSequence modifiableSequence = new ListModifiableSequence(new ArrayList<ISequenceElement>(seq.size()));

			// Set contents
			modifiableSequence.replaceAll(seq);

			// store the new object
			sequenceMap.put(r, modifiableSequence);
		}
		this.unusedElements.addAll(sequences.getUnusedElements());
	}

	@Override
	public IModifiableSequence getModifiableSequence(final IResource resource) {

		return sequenceMap.get(resource);
	}

	@Override
	public IModifiableSequence getModifiableSequence(final int index) {
		return sequenceMap.get(resources.get(index));
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
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<IResource, ISequence> map = new HashMap<IResource, ISequence>(sequenceMap);

		return map;
	}

	@Override
	public Map<IResource, IModifiableSequence> getModifiableSequences() {

		// Create a copy so external modification does not affect internal
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<IResource, IModifiableSequence> map = new HashMap<IResource, IModifiableSequence>(sequenceMap);

		return map;
	}

	@Override
	public int size() {

		return sequenceMap.size();
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof ModifiableSequences) {
			return sequenceMap.equals(((ModifiableSequences) obj).sequenceMap) && unusedElements.equals(((ModifiableSequences) obj).unusedElements);
		} else if (obj instanceof ISequences) {
			final ISequences seq = (ISequences) obj;
			if (size() != seq.size()) {
				return false;
			}

			if (!seq.getSequences().equals(seq.getSequences())) {
				return false;
			}

			if (!seq.getUnusedElements().equals(seq.getUnusedElements())) {
				// TODO this is suggesting order is important, which it shouldn't be.
				return false;
			}

			return true;

		}

		return false;
	}

	@Override
	public int hashCode() {

		return Objects.hashCode(this.resources, this.sequenceMap, this.unusedElements);
	}

	@Override
	public List<ISequenceElement> getUnusedElements() {
		return Collections.unmodifiableList(unusedElements);
	}

	@Override
	public List<ISequenceElement> getModifiableUnusedElements() {
		return unusedElements;
	}
}
