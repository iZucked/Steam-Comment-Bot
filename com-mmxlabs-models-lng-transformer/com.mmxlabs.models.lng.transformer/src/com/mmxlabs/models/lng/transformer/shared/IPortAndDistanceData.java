package com.mmxlabs.models.lng.transformer.shared;

import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

public interface IPortAndDistanceData {
	
	IPortProvider getPortProvider();

	IndexedMultiMatrixProvider<IPort, Integer> getDistanceMatrixProvider();
}
