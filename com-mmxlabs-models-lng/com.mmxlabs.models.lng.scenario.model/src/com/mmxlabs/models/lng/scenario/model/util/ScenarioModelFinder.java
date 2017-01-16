/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.CostModelFinder;
import com.mmxlabs.models.lng.pricing.util.PricingModelFinder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelFinder;

/**
 * 
 * @author Simon Goodall
 *
 */
public final class ScenarioModelFinder {

	private final @NonNull LNGScenarioModel lngScenarioModel;

	private final @NonNull PortModelFinder portModelFinder;
	private final @NonNull FleetModelFinder fleetModelFinder;
	private final @NonNull CommercialModelFinder commercialModelFinder;
	private final @NonNull PricingModelFinder pricingModelFinder;
	private final @NonNull CostModelFinder costModelFinder;
	private final @NonNull SpotMarketsModelFinder spotMarketsModelFinder;
	private final @NonNull CargoModelFinder cargoModelFinder;
	// private final @NonNull ActualsModelFinder actualsModelFinder;

	public ScenarioModelFinder(final @NonNull LNGScenarioModel lngScenarioModel) {
		this.lngScenarioModel = lngScenarioModel;

		this.portModelFinder = new PortModelFinder(ScenarioModelUtil.getPortModel(lngScenarioModel));
		this.fleetModelFinder = new FleetModelFinder(ScenarioModelUtil.getFleetModel(lngScenarioModel));
		this.commercialModelFinder = new CommercialModelFinder(ScenarioModelUtil.getCommercialModel(lngScenarioModel));
		this.pricingModelFinder = new PricingModelFinder(ScenarioModelUtil.getPricingModel(lngScenarioModel));
		this.costModelFinder = new CostModelFinder(ScenarioModelUtil.getCostModel(lngScenarioModel));
		this.spotMarketsModelFinder = new SpotMarketsModelFinder(ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel));
		this.cargoModelFinder = new CargoModelFinder(ScenarioModelUtil.getCargoModel(lngScenarioModel));
	}

	@NonNull
	public LNGScenarioModel getLNGScenarioModel() {
		return lngScenarioModel;
	}

	@NonNull
	public PortModelFinder getPortModelFinder() {
		return portModelFinder;
	}

	@NonNull
	public CargoModelFinder getCargoModelFinder() {
		return cargoModelFinder;
	}

	@NonNull
	public FleetModelFinder getFleetModelFinder() {
		return fleetModelFinder;
	}

	@NonNull
	public CommercialModelFinder getCommercialModelFinder() {
		return commercialModelFinder;
	}

	@NonNull
	public PricingModelFinder getPricingModelFinder() {
		return pricingModelFinder;
	}

	@NonNull
	public CostModelFinder getCostModelFinder() {
		return costModelFinder;
	}

	@NonNull
	public SpotMarketsModelFinder getSpotMarketsModelFinder() {
		return spotMarketsModelFinder;
	}

}
