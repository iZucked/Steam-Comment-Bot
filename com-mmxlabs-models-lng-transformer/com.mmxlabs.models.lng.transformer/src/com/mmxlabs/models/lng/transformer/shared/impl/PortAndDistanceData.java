package com.mmxlabs.models.lng.transformer.shared.impl;

import javax.inject.Inject;

import com.mmxlabs.models.lng.transformer.shared.IPortAndDistanceData;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

public class PortAndDistanceData implements IPortAndDistanceData {
	@Inject
	private IPortProvider portProvider;

	@Inject
	private IndexedMultiMatrixProvider<IPort, Integer> distanceMatrixProvider;

	public IndexedMultiMatrixProvider<IPort, Integer> getDistanceMatrixProvider() {
		return distanceMatrixProvider;
	}

	public IPortProvider getPortProvider() {
		return portProvider;
	}
}
