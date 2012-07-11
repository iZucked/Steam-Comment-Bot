/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.trading.optimiser.contracts.impl.BallastParameters;
import com.mmxlabs.trading.optimiser.contracts.impl.NetbackContract;
import com.mmxlabs.trading.optimiser.contracts.impl.ProfitSharingContract;

public class StandardContractBuilder implements IBuilderExtension {
	private final Map<NetbackContract, Map<AVesselClass, NotionalBallastParameters>> netbacks = new HashMap<NetbackContract, Map<AVesselClass, NotionalBallastParameters>>();
	private final ModelEntityMap map;

	public StandardContractBuilder(final ModelEntityMap map) {
		this.map = map;
	}

	public ProfitSharingContract createProfitSharingContract(final ICurve actualMarket, final ICurve referenceMarket, final int margin, final int share, final Set<IPort> baseMarketPorts) {
		return new ProfitSharingContract(actualMarket, referenceMarket, margin, share, baseMarketPorts);
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

				final Integer speed = p.isSetSpeed() ? Calculator.scaleToInt(p.getSpeed()) : null;
				final Integer hireCost = p.isSetSpeed() ? Calculator.scaleToInt(p.getSpeed()) : null;
				final Integer nboRate = p.isSetNboRate() ? Calculator.scaleToInt(p.getNboRate()) : null;
				final Integer baseFuelRate = p.isSetBaseConsumption() ? Calculator.scaleToInt(p.getBaseConsumption()) : null;

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
}
