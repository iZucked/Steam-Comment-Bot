/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;

public class IndexedPortEditor implements IPortProviderEditor {
	final IIndexMap<@NonNull ISequenceElement, @NonNull IPort> ports = new ArrayIndexMap<>();

	@Override
	public IPort getPortForElement(final @NonNull ISequenceElement element) {
		return ports.get(element);
	}

	@Override
	public void setPortForElement(final @NonNull IPort port, final @NonNull ISequenceElement element) {
		ports.set(element, port);
	}

}
