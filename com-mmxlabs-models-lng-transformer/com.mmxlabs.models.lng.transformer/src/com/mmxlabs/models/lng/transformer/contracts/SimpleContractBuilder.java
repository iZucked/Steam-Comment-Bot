/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.NetbackContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.ProfitSharingContract;

/**
 * A builder for all the simple contract types. Needs a way to wire up DCPs after everything has finished.
 * 
 * @author hinton
 * 
 */
public class SimpleContractBuilder implements IBuilderExtension {
	private final List<NetbackContract> netbacks = new LinkedList<NetbackContract>();

	FixedPriceContract createFixedPriceContract(final int pricePerMMBTU) {
		return new FixedPriceContract(pricePerMMBTU);
	}

	MarketPriceContract createMarketPriceContract(final ICurve index, final int offset, final int multiplier) {
		return new MarketPriceContract(index, offset, multiplier);
	}

	ProfitSharingContract createProfitSharingContract(final ICurve actualMarket, final ICurve referenceMarket, final int margin, final int share, Set<IPort> baseMarketPorts) {
		return new ProfitSharingContract(actualMarket, referenceMarket, margin, share, baseMarketPorts);
	}

	// ILoadPriceCalculator2 createNetbackContract(final int buyersMargin) {
	// final NetbackContract result = new NetbackContract();
	//
	// result.setMarginScaled(buyersMargin);
	//
	// netbacks.add(result);
	//
	// return result;
	// }

	@Override
	public Collection<Pair<String, IDataComponentProvider>> createDataComponentProviders(IOptimisationData optimisationData) {
		return Collections.emptySet();
	}

	@Override
	public void dispose() {
		netbacks.clear();
	}

	@Override
	public void finishBuilding(IOptimisationData optimisationData) {
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = optimisationData.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class);
		for (final NetbackContract netback : netbacks) {
			netback.setDistanceProvider(distanceProvider);
		}
	}
}
