/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class IndexedPortTypeEditor implements IPortTypeProviderEditor {
	private final String name;
	private final IIndexMap<ISequenceElement, PortType> portTypes = new ArrayIndexMap<ISequenceElement, PortType>();

	public IndexedPortTypeEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public PortType getPortType(final ISequenceElement sequenceElement) {
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
	public void setPortType(final ISequenceElement sequenceElement, final PortType portType) {
		portTypes.set(sequenceElement, portType);
	}
}
