/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVRangeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class IndexedPortCVRangeEditor implements IPortCVRangeProviderEditor {
	private final IIndexMap<IPort, Integer> portMinCVMap = new ArrayIndexMap<>();
	private final IIndexMap<IPort, Integer> portMaxCVMap = new ArrayIndexMap<>();
	@Override
	public int getPortMinCV(IPort port) {
		return portMinCVMap.get(port);
	}
	@Override
	public int getPortMaxCV(IPort port) {
		return portMaxCVMap.get(port);
	}
	@Override
	public void setPortMinCV(IPort port, int cv) {
		portMinCVMap.set(port,cv);
	}
	@Override
	public void setPortMaxCV(IPort port, int cv) {
		portMaxCVMap.set(port,cv);		
	}


}
