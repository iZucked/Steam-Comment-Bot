/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ISharedDataModelType;

/**
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public final class LNGScenarioSharedModelTypes {

	public static final ISharedDataModelType<PortModel> DISTANCES = ISharedDataModelType.make("lingo-distances", makeDistanceProvider());

	// public static final ISharedDataModelType<PricingModel> MARKET_CURVES = ISharedDataModelType.make();

	public static Function<IScenarioDataProvider, Object> makeDistanceProvider() {
		return (scenarioDataProvider) -> {
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
			return new ModelDistanceProvider(portModel);
		};
	}

}
