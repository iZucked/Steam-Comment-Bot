/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;

public class CostModelBuilder {

	private final @NonNull CostModel costModel;

	public CostModelBuilder(@NonNull final CostModel costModel) {
		this.costModel = costModel;
	}

	public @NonNull BaseFuelCost createBaseFuelCost(@NonNull final BaseFuel baseFuel, @NonNull final BaseFuelIndex baseFuelIndex) {

		final BaseFuelCost baseFuelCost = PricingFactory.eINSTANCE.createBaseFuelCost();
		baseFuelCost.setIndex(baseFuelIndex);
		baseFuelCost.setFuel(baseFuel);

		costModel.getBaseFuelCosts().add(baseFuelCost);

		return baseFuelCost;
	}
}
