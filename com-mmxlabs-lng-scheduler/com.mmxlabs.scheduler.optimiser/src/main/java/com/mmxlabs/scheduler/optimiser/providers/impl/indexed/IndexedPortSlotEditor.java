/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import java.util.HashMap;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;

public final class IndexedPortSlotEditor<T extends IIndexedObject> implements IPortSlotProviderEditor<T> {
	private final String name;
	private IIndexMap<T, IPortSlot> slots = new ArrayIndexMap<T, IPortSlot>();
	
	//TODO maybe make this indexed as well, although the getElement method is not called in the main loop 
	private HashMap<IPortSlot, T> elements = new HashMap<IPortSlot, T>();
	
	public IndexedPortSlotEditor(String name) {
		super();
		this.name = name;
	}

	@Override
	public final IPortSlot getPortSlot(T element) {
		return slots.maybeGet(element);
	}

	@Override
	public final T getElement(IPortSlot portSlot) {
		return elements.get(portSlot);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		elements.clear();
		slots.clear();
	}

	@Override
	public final void setPortSlot(T element, IPortSlot portSlot) {
		slots.set(element, portSlot);
		elements.put(portSlot, element);
	}
}
