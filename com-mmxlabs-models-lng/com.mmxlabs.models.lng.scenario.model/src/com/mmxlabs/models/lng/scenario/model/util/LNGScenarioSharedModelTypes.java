/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
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
	public static final ISharedDataModelType<PortModel> LOCATIONS = ISharedDataModelType.make("lingo-locations", null);

	public static final ISharedDataModelType<PricingModel> MARKET_CURVES = ISharedDataModelType.make("lingo-market-curves", makeMarketCurvesProvider());

	public static final ISharedDataModelType<FleetModel> FLEET = ISharedDataModelType.make("lingo-fleet", null);

	public static Function<IScenarioDataProvider, Object> makeDistanceProvider() {
		return (scenarioDataProvider) -> {
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);
			return new ModelDistanceProvider(portModel);
		};
	}

	public static Function<IScenarioDataProvider, Object> makeMarketCurvesProvider() {
		return (scenarioDataProvider) -> {
			final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
			return new ModelMarketCurveProvider(pricingModel);
		};
	}

}
