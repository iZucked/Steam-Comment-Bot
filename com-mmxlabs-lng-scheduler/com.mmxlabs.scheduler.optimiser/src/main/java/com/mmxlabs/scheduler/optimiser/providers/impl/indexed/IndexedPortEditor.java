package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import com.mmxlabs.common.indexedobjects.AutoSizingArrayList;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;

public class IndexedPortEditor<T extends IIndexedObject> implements IPortProviderEditor<T> {
	final AutoSizingArrayList<IPort> ports = new AutoSizingArrayList<IPort>();
	
	final String name;

	public IndexedPortEditor(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		ports.clear();
	}

	@Override
	public IPort getPortForElement(T element) {
		return ports.get(element.getIndex());
	}

	@Override
	public void setPortForElement(IPort port, T element) {
		ports.set(element.getIndex(), port);
	}

}
