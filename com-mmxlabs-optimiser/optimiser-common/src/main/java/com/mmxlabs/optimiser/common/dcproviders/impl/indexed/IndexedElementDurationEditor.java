/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.core.IResource;

public final class IndexedElementDurationEditor<T extends IIndexedObject> implements
		IElementDurationProviderEditor<T> {

	private final IIndexMap<IResource, IIndexMap<T, Integer>>
		durationByElementByResource = new ArrayIndexMap<IResource,IIndexMap<T, Integer>>();
	
	private final IIndexMap<T, Integer> durationByElement = 
		new ArrayIndexMap<T, Integer>();
	
	private int defaultDuration;
	
	private final String name;
	
	
	public IndexedElementDurationEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public int getElementDuration(final T element, final IResource resource) {
		final IIndexMap<T, Integer> byElementForResource = 
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
		IIndexMap<T, Integer> byElementForResource = 
			durationByElementByResource.maybeGet(resource);
		if (byElementForResource == null) {
			byElementForResource = new ArrayIndexMap<T,Integer>();
			durationByElementByResource.set(resource, byElementForResource);
		}
		
		byElementForResource.set(element, duration);
	}

	@Override
	public void setDefaultValue(final int defaultValue) {
		defaultDuration = defaultValue;
	}

	@Override
	public void setElementDuration(final T element, final int durationHours) {
		durationByElement.set(element, durationHours);
	}
}
