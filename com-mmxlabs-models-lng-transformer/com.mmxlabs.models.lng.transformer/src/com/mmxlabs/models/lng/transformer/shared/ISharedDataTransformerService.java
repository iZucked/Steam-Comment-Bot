/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.shared;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public interface ISharedDataTransformerService {

	IPortAndDistanceData getPortAndDistanceProvider(@NonNull IScenarioDataProvider dataProvider);

}
