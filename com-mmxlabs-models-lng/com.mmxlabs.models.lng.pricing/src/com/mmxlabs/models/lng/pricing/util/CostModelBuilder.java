/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.RouteCost;

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

	public void setRouteCost(@NonNull VesselClass vesselClass, @NonNull Route route, int ladenCost, int ballastCost) {
		final RouteCost routeCost = PricingFactory.eINSTANCE.createRouteCost();
		routeCost.setRoute(route);
		routeCost.setLadenCost(ladenCost); // cost in dollars for a laden vessel
		routeCost.setBallastCost(ballastCost); // cost in dollars for a ballast vessel
		routeCost.setVesselClass(vesselClass);

		costModel.getRouteCosts().add(routeCost);
	}

	public @NonNull CooldownPrice createCooldownPrice(@NonNull String expression, boolean isLumpsum, @NonNull Collection<Port> ports) {

		final CooldownPrice cooldownPrice = PricingFactory.eINSTANCE.createCooldownPrice();
		cooldownPrice.setExpression(expression);
		cooldownPrice.setLumpsum(isLumpsum);
		cooldownPrice.getPorts().addAll(ports);

		costModel.getCooldownCosts().add(cooldownPrice);

		return cooldownPrice;
	}
}
