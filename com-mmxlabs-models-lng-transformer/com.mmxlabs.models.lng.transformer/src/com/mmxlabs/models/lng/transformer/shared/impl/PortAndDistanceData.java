/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.shared.impl;

import javax.inject.Inject;

import com.mmxlabs.models.lng.transformer.shared.IPortAndDistanceData;
import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

public class PortAndDistanceData implements IPortAndDistanceData {
	@Inject
	private IPortProvider portProvider;

	@Inject
	private IDistanceMatrixProvider distanceMatrixProvider;

	public IDistanceMatrixProvider getDistanceMatrixProvider() {
		return distanceMatrixProvider;
	}

	public IPortProvider getPortProvider() {
		return portProvider;
	}
}
