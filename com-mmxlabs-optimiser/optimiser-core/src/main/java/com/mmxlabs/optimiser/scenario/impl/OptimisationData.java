package com.mmxlabs.optimiser.scenario.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.scenario.IOptimisationData;

/**
 * Implementation of {@link IOptimisationData}
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class OptimisationData<T> implements IOptimisationData<T> {

	private final Map<String, IDataComponentProvider> dataComponentProviders;

	private List<IResource> resources;

	private List<T> sequenceElements;

	public OptimisationData() {
		// Init hash map
		dataComponentProviders = new HashMap<String, IDataComponentProvider>();
	}

	@Override
	public <U extends IDataComponentProvider> U getDataComponentProvider(
			final String key, final Class<U> clazz) {

		if (dataComponentProviders.containsKey(key)) {
			final IDataComponentProvider dataComponentProvider = dataComponentProviders
					.get(key);
			if (clazz.isInstance(dataComponentProvider)) {
				return clazz.cast(dataComponentProvider);
			} else {
				throw new ClassCastException("Keyed data is not instance of "
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

	/**
	 * Set reference to the {@link List} of sequence elements contained in this
	 * optimisation.
	 * 
	 * @param sequenceElements
	 */
	public void setSequenceElements(final List<T> sequenceElements) {
		this.sequenceElements = sequenceElements;
	}

	@Override
	public List<IResource> getResources() {
		return resources;
	}

	/**
	 * Set reference to {@link List} of {@link IResource}s used by this
	 * optimisation.
	 * 
	 * @param resources
	 */
	public void setResources(final List<IResource> resources) {
		this.resources = resources;
	}

	public void addDataComponentProvider(final String key,
			final IDataComponentProvider dataComponentProvider) {
		this.dataComponentProviders.put(key, dataComponentProvider);
	}

	@Override
	public boolean hasDataComponentProvider(final String key) {
		return dataComponentProviders.containsKey(key);
	}

	@Override
	public void dispose() {

		// Dispose all IDataComponentProviders before clearing map.
		for (final IDataComponentProvider provider : dataComponentProviders
				.values()) {
			provider.dispose();
		}
		dataComponentProviders.clear();

		resources = null;
		sequenceElements = null;
	}
}
