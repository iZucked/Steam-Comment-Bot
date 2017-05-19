package com.mmxlabs.models.lng.transformer.shared;

import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

public interface IPortAndDistanceData {

	IPortProvider getPortProvider();

	IDistanceMatrixProvider getDistanceMatrixProvider();
}
