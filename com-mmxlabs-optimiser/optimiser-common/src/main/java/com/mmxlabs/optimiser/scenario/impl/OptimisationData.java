package com.mmxlabs.optimiser.scenario.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.IResourceType;

public final class OptimisationData<T> implements IOptimisationData<T> {

	private List<IResource> resources;

	private final Map<String, IDataComponentProvider> dataComponentProviders;

	private List<T> sequenceElements;

	public OptimisationData() {
		dataComponentProviders = new HashMap<String, IDataComponentProvider>();
	}

	@Override
	public <U extends IDataComponentProvider> U getDataComponentProvider(
			final Class<U> clazz, final String key) {

		if (dataComponentProviders.containsKey(key)) {
			final IDataComponentProvider dataComponentProvider = dataComponentProviders
					.get(key);
			if (clazz.isInstance(dataComponentProvider)) {
				return clazz.cast(dataComponentProvider);
			} else {
				throw new RuntimeException("Keyed data is not instance of "
						+ clazz.getCanonicalName());
			}
		}

		// throw new NoSuchElementException();
		return null;
	}

	@Override
	public List<T> getSequenceElements() {
		return sequenceElements;
	}

	public void setSequenceElements(List<T> sequenceElements) {
		this.sequenceElements = sequenceElements;
	}

	@Override
	public Collection<IResourceType> getResourceTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IResource> getResources() {
		return resources;
	}

	public void setResources(List<IResource> resources) {
		this.resources = resources;
	}

	public void addDataComponentProvider(String key,
			IDataComponentProvider dataComponentProvider) {
		this.dataComponentProviders.put(key, dataComponentProvider);
	}
}
