/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Implementation of {@link IOptimisationData}
 * 
 * @author Simon Goodall
 * 
 */
public final class OptimisationData implements IOptimisationData {

	private final Map<String, IDataComponentProvider> dataComponentProviders;

	private List<IResource> resources;

	private List<ISequenceElement> sequenceElements;

	public OptimisationData() {
		// Init hash map
		dataComponentProviders = new HashMap<String, IDataComponentProvider>();
	}

	@Override
	public <U extends IDataComponentProvider> U getDataComponentProvider(final String key, final Class<U> clazz) {

		if (dataComponentProviders.containsKey(key)) {
			final IDataComponentProvider dataComponentProvider = dataComponentProviders.get(key);
			if (clazz.isInstance(dataComponentProvider)) {
				return clazz.cast(dataComponentProvider);
			} else {
				throw new ClassCastException("Keyed data is not instance of " + clazz.getCanonicalName());
			}
		}

		// throw new NoSuchElementException();
		return null;
	}

	@Override
	public List<ISequenceElement> getSequenceElements() {
		return sequenceElements;
	}

	/**
	 * Set reference to the {@link List} of sequence elements contained in this optimisation.
	 * 
	 * @param sequenceElements
	 */
	public void setSequenceElements(final List<ISequenceElement> sequenceElements) {
		this.sequenceElements = sequenceElements;
	}

	@Override
	public List<IResource> getResources() {
		return resources;
	}

	/**
	 * Set reference to {@link List} of {@link IResource}s used by this optimisation.
	 * 
	 * @param resources
	 */
	public void setResources(final List<IResource> resources) {
		this.resources = resources;
	}

	public void addDataComponentProvider(final String key, final IDataComponentProvider dataComponentProvider) {
		this.dataComponentProviders.put(key, dataComponentProvider);
	}

	@Override
	public boolean hasDataComponentProvider(final String key) {
		return dataComponentProviders.containsKey(key);
	}

	@Override
	public void dispose() {

		// Dispose all IDataComponentProviders before clearing map.
		for (final IDataComponentProvider provider : dataComponentProviders.values()) {
			provider.dispose();
		}
		dataComponentProviders.clear();

		resources = null;
		sequenceElements = null;
	}

	@Override
	public Collection<String> getDataComponentProviders() {
		return dataComponentProviders.keySet();
	}
}
