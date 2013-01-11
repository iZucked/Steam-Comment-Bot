/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;

public class IndexedPortEditor implements IPortProviderEditor {
	final IIndexMap<ISequenceElement, IPort> ports = new ArrayIndexMap<ISequenceElement, IPort>();

	final String name;

	public IndexedPortEditor(final String name) {
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
	public IPort getPortForElement(final ISequenceElement element) {
		return ports.maybeGet(element);
	}

	@Override
	public void setPortForElement(final IPort port, final ISequenceElement element) {
		ports.set(element, port);
	}

}
