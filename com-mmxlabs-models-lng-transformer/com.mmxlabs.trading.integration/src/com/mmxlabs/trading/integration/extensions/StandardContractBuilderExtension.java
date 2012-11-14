/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.integration.extensions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.trading.optimiser.contracts.impl.BallastParameters;
import com.mmxlabs.trading.optimiser.contracts.impl.NetbackContract;
import com.mmxlabs.trading.optimiser.contracts.impl.ProfitSharingContract;
import com.mmxlabs.trading.optimiser.contracts.impl.RedirectionContract;

/**
 * Contract Transformer and Builder - this is the {@link IBuilderExtension} portion of the extension. See {@link StandardContractTransformerExtension} for the transformer / model side. This class creates the
 * internal optimiser contract implementations. Most of the work happens in the {@link #finishBuilding(IOptimisationData)} method where partially constructed objects can be completed as the
 * {@link IOptimisationData} structure should be almost complete by this stage.
 * 
 * @since 2.0
 */
public class StandardContractBuilderExtension implements IBuilderExtension {
	private final Map<NetbackContract, Map<AVesselClass, NotionalBallastParameters>> netbacks = new HashMap<NetbackContract, Map<AVesselClass, NotionalBallastParameters>>();

	@Inject
	private ModelEntityMap map;

	@Inject
	private Injector injector;

	public StandardContractBuilderExtension() {
	}

	public StandardContractBuilderExtension(final ModelEntityMap map) {
		this.map = map;
	}

	public ProfitSharingContract createProfitSharingContract(final ICurve actualMarket, final ICurve referenceMarket, final int margin, final int share, final Set<IPort> baseMarketPorts,
			final int salesPriceMultiplier) {
		return new ProfitSharingContract(actualMarket, referenceMarket, margin, share, baseMarketPorts, salesPriceMultiplier);
	}

	public NetbackContract createNetbackContract(final int buyersMargin, final int floorPrice, final Map<AVesselClass, NotionalBallastParameters> ballastParameters) {
		final NetbackContract result = new NetbackContract(buyersMargin, floorPrice);

		netbacks.put(result, ballastParameters);

		return result;
	}

	@Override
	public Collection<Pair<String, IDataComponentProvider>> createDataComponentProviders(final IOptimisationData optimisationData) {
		return Collections.emptySet();
	}

	@Override
	public void dispose() {
		netbacks.clear();
	}

	@Override
	public void finishBuilding(final IOptimisationData optimisationData) {
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = optimisationData.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class);
		for (final NetbackContract netback : netbacks.keySet()) {
			netback.setDistanceProvider(distanceProvider);
		}

		for (final Map.Entry<NetbackContract, Map<AVesselClass, NotionalBallastParameters>> entry : netbacks.entrySet()) {

			final Map<IVesselClass, BallastParameters> m = new HashMap<IVesselClass, BallastParameters>();
			for (final Map.Entry<AVesselClass, NotionalBallastParameters> e2 : entry.getValue().entrySet()) {
				final IVesselClass vc = map.getOptimiserObject(e2.getKey(), IVesselClass.class);

				final NotionalBallastParameters p = e2.getValue();

				final Integer speed = p.isSetSpeed() ? OptimiserUnitConvertor.convertToInternalSpeed(p.getSpeed()) : null;
				final Integer hireCost = p.isSetHireCost() ? (int) OptimiserUnitConvertor.convertToInternalHourlyCost(p.getHireCost()) : null;
				final Integer nboRate = p.isSetNboRate() ? OptimiserUnitConvertor.convertToInternalConversionFactor(p.getNboRate()) : null;
				final Integer baseFuelRate = p.isSetBaseConsumption() ? OptimiserUnitConvertor.convertToInternalConversionFactor(p.getBaseConsumption()) : null;

				final List<String> routes = new ArrayList<String>();
				for (final Route route : p.getRoutes()) {
					routes.add(route.getName());
				}

				final BallastParameters params = new BallastParameters(vc, speed, hireCost, nboRate, baseFuelRate, routes.toArray(new String[routes.size()]));

				m.put(vc, params);
			}
			entry.getKey().setBallastParameters(m);
		}
	}

	public ILoadPriceCalculator createRedirectionContract(final IPort baseMarketPort, final ICurve purchasePriceCurve, final ICurve salesPriceCurve, final int notionalSpeed) {

		final RedirectionContract contract = new RedirectionContract(purchasePriceCurve, salesPriceCurve, notionalSpeed, baseMarketPort);
		injector.injectMembers(contract);
		return contract;
	}
}
