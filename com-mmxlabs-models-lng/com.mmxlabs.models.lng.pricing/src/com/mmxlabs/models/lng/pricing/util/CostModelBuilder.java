/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.util.Collection;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;
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

	public void setAllBaseFuelCost(FleetModel fleetModel, String expr) {
		for (BaseFuel bf : fleetModel.getBaseFuels()) {
			createOrUpdateBaseFuelCost(bf, expr);
		}
	}
	
	public @NonNull BaseFuelCost createOrUpdateBaseFuelCost(@NonNull final BaseFuel baseFuel, @NonNull final String baseFuelExpression) {

		for (BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
			if (baseFuelCost.getFuel() == baseFuel) {
				baseFuelCost.setExpression(baseFuelExpression);
				return baseFuelCost;
			}
		}

		for (BaseFuelCost cost : costModel.getBaseFuelCosts()) {
			if (cost.getFuel() == baseFuel) {
				cost.setExpression(baseFuelExpression);
				return cost;
			}
		}

		final BaseFuelCost baseFuelCost = PricingFactory.eINSTANCE.createBaseFuelCost();
		baseFuelCost.setExpression(baseFuelExpression);
		baseFuelCost.setFuel(baseFuel);

		costModel.getBaseFuelCosts().add(baseFuelCost);

		return baseFuelCost;
	}

	public void createRouteCost(@NonNull final Vessel vessel, @NonNull final RouteOption routeOption, final int ladenCost, final int ballastCost) {
		final RouteCost routeCost = PricingFactory.eINSTANCE.createRouteCost();
		routeCost.setRouteOption(routeOption);
		routeCost.setLadenCost(ladenCost); // cost in dollars for a laden vessel
		routeCost.setBallastCost(ballastCost); // cost in dollars for a ballast vessel
		routeCost.getVessels().add(vessel);

		costModel.getRouteCosts().add(routeCost);
	}

	public void createRouteCost(@NonNull final AVesselSet<Vessel> vessel, @NonNull final RouteOption routeOption, final int ladenCost, final int ballastCost) {
		final RouteCost routeCost = PricingFactory.eINSTANCE.createRouteCost();
		routeCost.setRouteOption(routeOption);
		routeCost.setLadenCost(ladenCost); // cost in dollars for a laden vessel
		routeCost.setBallastCost(ballastCost); // cost in dollars for a ballast vessel
		routeCost.getVessels().add(vessel);

		costModel.getRouteCosts().add(routeCost);
	}

	public void setAllExistingCooldownCosts(boolean lumpsum, @NonNull final String expression) {
		for (CooldownPrice cost : costModel.getCooldownCosts()) {
			cost.setExpression(expression);
			cost.setLumpsum(lumpsum);
		}
	}

	public @NonNull CooldownPrice createCooldownPrice(@NonNull final String expression, final boolean isLumpsum, @NonNull final Collection<? extends APortSet<Port>> ports) {

		final CooldownPrice cooldownPrice = PricingFactory.eINSTANCE.createCooldownPrice();
		cooldownPrice.setExpression(expression);
		cooldownPrice.setLumpsum(isLumpsum);
		cooldownPrice.getPorts().addAll(ports);

		costModel.getCooldownCosts().add(cooldownPrice);

		return cooldownPrice;
	}

	public PortCost createPortCost(@NonNull Collection<APortSet<Port>> ports, Collection<PortCapability> capabilities, int cost) {
		PortCost portCost = PricingFactory.eINSTANCE.createPortCost();
		portCost.getPorts().addAll(ports);

		for (PortCapability portCapability : capabilities) {
			PortCostEntry portCostEntry = PricingFactory.eINSTANCE.createPortCostEntry();
			portCostEntry.setActivity(portCapability);
			portCostEntry.setCost(cost);
			portCost.getEntries().add(portCostEntry);
		}

		costModel.getPortCosts().add(portCost);

		return portCost;
	}

	public @NonNull CostModel getCostModel() {
		return costModel;
	}

	public SuezCanalTariff createSimpleSuezCanalTariff(double fixedCost) {

		SuezCanalTariff suezCanalTariff = PricingFactory.eINSTANCE.createSuezCanalTariff();
		SuezCanalTariffBand band = PricingFactory.eINSTANCE.createSuezCanalTariffBand();
		band.setBallastTariff(0.0);

		suezCanalTariff.getBands().add(band);
		suezCanalTariff.setFixedCosts(fixedCost);
		suezCanalTariff.setSdrToUSD("1");

		costModel.setSuezCanalTariff(suezCanalTariff);

		return suezCanalTariff;
	}

	public PanamaCanalTariff createSimplePanamaCanalTariff(double fixedCost) {

		PanamaCanalTariff panamaCanalTariff = PricingFactory.eINSTANCE.createPanamaCanalTariff();
		// Cost is m3 * band price. To make fixed cost apply cost only to first 1m3 of capacity
		{
			PanamaCanalTariffBand band = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
			band.setBandEnd(1);
			band.setLadenTariff(fixedCost);
			panamaCanalTariff.getBands().add(band);
		}
		// ... anything over this is uncosted
		{
			PanamaCanalTariffBand band = PricingFactory.eINSTANCE.createPanamaCanalTariffBand();
			band.setBandStart(1);
			band.setLadenTariff(0.0);
			panamaCanalTariff.getBands().add(band);
		}

		costModel.setPanamaCanalTariff(panamaCanalTariff);

		return panamaCanalTariff;
	}
}
