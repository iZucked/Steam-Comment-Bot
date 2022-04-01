/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.shared;

import com.mmxlabs.scheduler.optimiser.shared.port.IDistanceMatrixProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

public interface IPortAndDistanceData {

	IPortProvider getPortProvider();

	IDistanceMatrixProvider getDistanceMatrixProvider();
}
