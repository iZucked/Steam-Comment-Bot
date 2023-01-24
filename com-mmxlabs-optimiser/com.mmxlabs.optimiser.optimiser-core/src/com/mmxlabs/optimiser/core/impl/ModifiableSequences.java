/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;

/**
 * Default implementation of {@link IModifiableSequences}. Uses
 * {@link ListModifiableSequence} instances when required.
 * 
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public class ModifiableSequences implements IModifiableSequences {

	private final List<IResource> resources;

	private final Map<IResource, IModifiableSequence> sequenceMap;

	private final List<ISequenceElement> unusedElements = new ArrayList<>();

	private ISequencesAttributesProvider providers;

	/**
	 * Constructor taking a list of {@link IResource} instances. The
	 * {@link IModifiableSequence} instances will be created automatically. The
	 * resources list is copied to maintain internal consistency with the sequence
	 * map.
	 * 
	 * @param resources
	 */
	public ModifiableSequences(final List<IResource> resources) {
		// Copy the list as we do not track changes
		this.resources = new ArrayList<>(resources);
		sequenceMap = new LinkedHashMap<>();
		for (final IResource resource : resources) {
			sequenceMap.put(resource, new ListModifiableSequence(new LinkedList<>()));
		}
		this.providers = new SequencesAttributesProviderImpl();
	}

	/**
	 * Constructor taking both the ordered list of resources and a {@link Map}
	 * containing the initial sequences. References are maintained to both objects.
	 * 
	 * @param resources
	 * @param sequenceMap
	 */
	public ModifiableSequences(final List<IResource> resources, final Map<IResource, IModifiableSequence> sequenceMap) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
		this.providers = new SequencesAttributesProviderImpl();
	}

	public ModifiableSequences(final List<IResource> resources, final Map<IResource, IModifiableSequence> sequenceMap, final List<ISequenceElement> unusedElements) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
		this.unusedElements.addAll(unusedElements);
		this.providers = new SequencesAttributesProviderImpl();
	}

	public ModifiableSequences(final List<IResource> resources, final Map<IResource, IModifiableSequence> sequenceMap, final List<ISequenceElement> unusedElements, ISequencesAttributesProvider providers) {
		this.resources = resources;
		this.sequenceMap = sequenceMap;
		this.unusedElements.addAll(unusedElements);
		this.providers = providers;
	}

	/**
	 * Constructor which creates a modifiable copy of the input {@link ISequences}
	 * object.
	 * 
	 * @param sequences Source {@link ISequences} object
	 */
	public ModifiableSequences(final ISequences sequences) {

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
		this.providers = sequences.getProviders();

	}

	@Override
	public IModifiableSequence getModifiableSequence(final IResource resource) {

		final IModifiableSequence seq = sequenceMap.get(resource);
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource");
		}
		return seq;
	}

	@Override
	public IModifiableSequence getModifiableSequence(final int index) {
		final IModifiableSequence seq = sequenceMap.get(resources.get(index));
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@Override
	public List<IResource> getResources() {
		return Collections.unmodifiableList(resources);
	}
	
	@Override
	@NonNull
	public ISequence getSequence(final IResource resource) {
		final IModifiableSequence seq = sequenceMap.get(resource);
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource");
		}
		return seq;
	}

	@Override
	public ISequence getSequence(final int index) {
		final IModifiableSequence seq = sequenceMap.get(resources.get(index));
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@Override
	public Map<IResource, ISequence> getSequences() {

		// Create a copy so external modification does not affect internal
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<IResource, ISequence> map = new HashMap<>(sequenceMap);

		return map;
	}

	@Override
	public Map<IResource, IModifiableSequence> getModifiableSequences() {

		// Create a copy so external modification does not affect internal
		// state. We could call (@link {@link Collections#unmodifiableMap(Map)}
		// however the generics mismatch between ISequence and
		// IModifiableSequence still needs to be addressed.
		final Map<IResource, IModifiableSequence> map = new HashMap<>(sequenceMap);

		return map;
	}

	@Override
	public int size() {
		return sequenceMap.size();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj instanceof ISequences other) {
			if (size() != other.size()) {
				return false;
			}

			if (!Objects.equal(providers, other.getProviders())) {
				return false;
			}

			if (!this.getSequences().equals(other.getSequences())) {
				return false;
			}

			// Use a Set as order is not important for equality
			if (!new HashSet<>(this.getUnusedElements()).equals(new HashSet<>(other.getUnusedElements()))) {
				return false;
			}

			return true;

		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.resources, this.sequenceMap, new HashSet<>(this.unusedElements), this.providers);
	}

	@Override
	public List<ISequenceElement> getUnusedElements() {
		return Collections.unmodifiableList(unusedElements);
	}

	@Override
	public List<ISequenceElement> getModifiableUnusedElements() {
		return unusedElements;
	}

	@Override
	public ISequencesAttributesProvider getProviders() {
		return providers;
	}
}
