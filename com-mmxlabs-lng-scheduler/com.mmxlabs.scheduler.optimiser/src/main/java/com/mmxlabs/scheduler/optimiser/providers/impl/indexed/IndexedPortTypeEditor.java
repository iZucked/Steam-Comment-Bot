/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class IndexedPortTypeEditor<T extends IIndexedObject> implements IPortTypeProviderEditor<T> {
	private final String name;
	private final IIndexMap<T,PortType> portTypes = 
		new ArrayIndexMap<T,PortType>();
	
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
