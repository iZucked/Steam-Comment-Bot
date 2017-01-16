/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class IndexedPortTypeEditor implements IPortTypeProviderEditor {
	private final IIndexMap<@NonNull ISequenceElement, @NonNull PortType> portTypes = new ArrayIndexMap<>();

	@Override
	@NonNull
	public PortType getPortType(final @NonNull ISequenceElement sequenceElement) {
		return portTypes.get(sequenceElement);
	}

	@Override
	public void setPortType(final @NonNull ISequenceElement sequenceElement, final @NonNull PortType portType) {
		portTypes.set(sequenceElement, portType);
	}
}
