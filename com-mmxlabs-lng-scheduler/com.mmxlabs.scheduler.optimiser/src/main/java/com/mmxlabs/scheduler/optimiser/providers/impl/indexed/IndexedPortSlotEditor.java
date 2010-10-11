package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import java.util.HashMap;

import com.mmxlabs.common.indexedobjects.AutoSizingArrayList;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;

public final class IndexedPortSlotEditor<T extends IIndexedObject> implements IPortSlotProviderEditor<T> {
	private final String name;
	private AutoSizingArrayList<IPortSlot> slots = new AutoSizingArrayList<IPortSlot>();
	
	//TODO maybe make this indexed as well, although the getElement method is not called in the main loop 
	private HashMap<IPortSlot, T> elements = new HashMap<IPortSlot, T>();
	
	public IndexedPortSlotEditor(String name) {
		super();
		this.name = name;
	}

	@Override
	public final IPortSlot getPortSlot(T element) {
		return slots.get(element.getIndex());
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
		slots.set(element.getIndex(), portSlot);
		elements.put(portSlot, element);
	}
}
