package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import com.mmxlabs.common.indexedobjects.AutoSizingArrayList;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class IndexedPortTypeEditor<T extends IIndexedObject> implements IPortTypeProviderEditor<T> {
	private final String name;
	private final AutoSizingArrayList<PortType> portTypes = 
		new AutoSizingArrayList<PortType>();
	
	public IndexedPortTypeEditor(String name) {
		super();
		this.name = name;
	}

	@Override
	public PortType getPortType(T sequenceElement) {
		return portTypes.get(sequenceElement);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		portTypes.clear();
	}

	@Override
	public void setPortType(T sequenceElement, PortType portType) {
		portTypes.set(sequenceElement, portType);
	}
}
