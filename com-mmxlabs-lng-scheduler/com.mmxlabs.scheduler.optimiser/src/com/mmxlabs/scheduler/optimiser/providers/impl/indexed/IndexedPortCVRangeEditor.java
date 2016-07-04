/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVRangeProviderEditor;

public class IndexedPortCVRangeEditor implements IPortCVRangeProviderEditor {
	private final IIndexMap<@NonNull IPort, Integer> portMinCVMap = new ArrayIndexMap<>();
	private final IIndexMap<@NonNull IPort, Integer> portMaxCVMap = new ArrayIndexMap<>();

	@Override
	public int getPortMinCV(@NonNull final IPort port) {
		if (portMinCVMap.maybeGet(port) != null) {
			return portMinCVMap.get(port);
		} else {
			return 0;
		}
	}

	@Override
	public int getPortMaxCV(@NonNull final IPort port) {
		if (portMinCVMap.maybeGet(port) != null) {
			return portMaxCVMap.get(port);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public void setPortMinCV(@NonNull final IPort port, final int cv) {
		portMinCVMap.set(port, cv);
	}

	@Override
	public void setPortMaxCV(@NonNull final IPort port, final int cv) {
		portMaxCVMap.set(port, cv);
	}

}
