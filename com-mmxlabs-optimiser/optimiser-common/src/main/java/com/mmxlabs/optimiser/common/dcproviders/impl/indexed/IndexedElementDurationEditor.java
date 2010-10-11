package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.AutoSizingArrayList;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.core.IResource;

public class IndexedElementDurationEditor<T extends IIndexedObject> implements
		IElementDurationProviderEditor<T> {

	private final AutoSizingArrayList<AutoSizingArrayList<Integer>>
		durationByElementByResource = new AutoSizingArrayList<AutoSizingArrayList<Integer>>();
	
	private final AutoSizingArrayList<Integer> durationByElement = 
		new AutoSizingArrayList<Integer>();
	
	private int defaultDuration;
	
	private final String name;
	
	
	public IndexedElementDurationEditor( String name) {
		super();
		this.name = name;
	}

	@Override
	public int getElementDuration(final T element, final IResource resource) {
		final AutoSizingArrayList<Integer> byElementForResource = 
			durationByElementByResource.maybeGet(resource);
		
		if (byElementForResource != null) {
			final Integer i = byElementForResource.maybeGet(element);
			if (i != null) {
				return i.intValue();
			}
		}
		
		final Integer i = durationByElement.maybeGet(element);
		if (i != null) {
			return i.intValue();
		}
		
		return defaultDuration;
	}

	@Override
	public int getDefaultValue() {
		return defaultDuration;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		durationByElement.clear();
		durationByElementByResource.clear();
	}

	@Override
	public void setElementDuration(T element, IResource resource, int duration) {
		AutoSizingArrayList<Integer> byElementForResource = 
			durationByElementByResource.maybeGet(resource);
		if (byElementForResource == null) {
			byElementForResource = new AutoSizingArrayList<Integer>();
			durationByElementByResource.set(resource, byElementForResource);
		}
		
		byElementForResource.set(element, duration);
	}

	@Override
	public void setDefaultValue(final int defaultValue) {
		defaultDuration = defaultValue;
	}

	@Override
	public void setElementDuration(T element, int durationHours) {
		durationByElement.set(element, durationHours);
	}
}
