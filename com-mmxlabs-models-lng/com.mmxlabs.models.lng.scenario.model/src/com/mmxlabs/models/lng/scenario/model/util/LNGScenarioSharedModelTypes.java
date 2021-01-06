/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
	public static final ISharedDataModelType<PortModel> PORT_GROUPS = ISharedDataModelType.make("lingo-port-groups", null);

	public static final ISharedDataModelType<PricingModel> MARKET_CURVES = ISharedDataModelType.make("lingo-market-curves", makeMarketCurvesProvider());

	public static final ISharedDataModelType<PricingModel> SETTLED_PRICES = ISharedDataModelType.make("lingo-settled-prices", null);

	public static final ISharedDataModelType<FleetModel> FLEET = ISharedDataModelType.make("lingo-fleet", null);
	public static final ISharedDataModelType<FleetModel> VESSEL_GROUPS = ISharedDataModelType.make("lingo-vessel-groups", null);
	public static final ISharedDataModelType<FleetModel> BUNKER_FUELS = ISharedDataModelType.make("lingo-bunker-fuels", null);

	public static Function<IScenarioDataProvider, Object> makeDistanceProvider() {
		return sdp -> ModelDistanceProvider.getOrCreate(ScenarioModelUtil.getPortModel(sdp));
	}

	public static Function<IScenarioDataProvider, Object> makeMarketCurvesProvider() {
		return sdp -> ModelMarketCurveProvider.getOrCreate(ScenarioModelUtil.getPricingModel(sdp));
	}
}
