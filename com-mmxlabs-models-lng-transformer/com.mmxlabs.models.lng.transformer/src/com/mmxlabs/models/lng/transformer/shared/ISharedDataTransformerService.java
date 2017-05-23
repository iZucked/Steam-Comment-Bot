/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.shared;

import com.mmxlabs.models.lng.port.PortModel;

public interface ISharedDataTransformerService {

	IPortAndDistanceData getPortAndDistanceProvider(PortModel portModel);

}
