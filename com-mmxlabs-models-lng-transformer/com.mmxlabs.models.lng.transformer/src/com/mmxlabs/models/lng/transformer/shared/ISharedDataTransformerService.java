package com.mmxlabs.models.lng.transformer.shared;

import com.mmxlabs.models.lng.port.PortModel;

public interface ISharedDataTransformerService {

	IPortAndDistanceData getPortAndDistanceProvider(PortModel portModel);

}
