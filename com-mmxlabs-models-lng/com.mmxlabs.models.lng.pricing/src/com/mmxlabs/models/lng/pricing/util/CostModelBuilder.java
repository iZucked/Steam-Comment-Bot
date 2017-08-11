/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.util.Collection;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.types.util.SetUtils;

public class CostModelBuilder {

	private final @NonNull CostModel costModel;

	public CostModelBuilder(@NonNull final CostModel costModel) {
		this.costModel = costModel;
	}

	public BiConsumer<Vessel, Vessel> copyRouteCosts() {
		return (source, destination) -> {
			for (final RouteCost routeCost : costModel.getRouteCosts()) {
				if (routeCost.getVessels().contains(source)) {
					final RouteCost newCost = EcoreUtil.copy(routeCost);
					newCost.getVessels().clear();
					newCost.getVessels().add(destination);
					costModel.getRouteCosts().add(newCost);
					return;
				}
			}
			for (final RouteCost routeCost : costModel.getRouteCosts()) {
				if (SetUtils.getObjects(routeCost.getVessels()).contains(source)) {
					final RouteCost newCost = EcoreUtil.copy(routeCost);
					newCost.getVessels().clear();
					newCost.getVessels().add(destination);
					costModel.getRouteCosts().add(newCost);
					return;
				}
			}
			throw new IllegalStateException("No existing route cost for vessel");
		};
	}

	public @NonNull BaseFuelCost createBaseFuelCost(@NonNull final BaseFuel baseFuel, @NonNull final BaseFuelIndex baseFuelIndex) {

		final BaseFuelCost baseFuelCost = PricingFactory.eINSTANCE.createBaseFuelCost();
		baseFuelCost.setIndex(baseFuelIndex);
		baseFuelCost.setFuel(baseFuel);

		costModel.getBaseFuelCosts().add(baseFuelCost);

		return baseFuelCost;
	}

	public void setRouteCost(@NonNull final Vessel vessel, @NonNull final RouteOption routeOption, final int ladenCost, final int ballastCost) {
		final RouteCost routeCost = PricingFactory.eINSTANCE.createRouteCost();
		routeCost.setRouteOption(routeOption);
		routeCost.setLadenCost(ladenCost); // cost in dollars for a laden vessel
		routeCost.setBallastCost(ballastCost); // cost in dollars for a ballast vessel
		routeCost.getVessels().add(vessel);

		costModel.getRouteCosts().add(routeCost);
	}

	public @NonNull CooldownPrice createCooldownPrice(@NonNull final String expression, final boolean isLumpsum, @NonNull final Collection<Port> ports) {

		final CooldownPrice cooldownPrice = PricingFactory.eINSTANCE.createCooldownPrice();
		cooldownPrice.setExpression(expression);
		cooldownPrice.setLumpsum(isLumpsum);
		cooldownPrice.getPorts().addAll(ports);

		costModel.getCooldownCosts().add(cooldownPrice);

		return cooldownPrice;
	}
}
