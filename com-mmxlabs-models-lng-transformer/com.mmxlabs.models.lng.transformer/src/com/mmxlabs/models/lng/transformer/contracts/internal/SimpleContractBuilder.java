/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts.internal;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;

/**
 * A builder for all the simple contract types. Needs a way to wire up DCPs after everything has finished.
 * 
 * @author hinton
 * 
 */
public class SimpleContractBuilder implements IBuilderExtension {


	@Override
	public Collection<Pair<String, IDataComponentProvider>> createDataComponentProviders(IOptimisationData optimisationData) {
		return Collections.emptySet();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void finishBuilding(IOptimisationData optimisationData) {
	}
}
