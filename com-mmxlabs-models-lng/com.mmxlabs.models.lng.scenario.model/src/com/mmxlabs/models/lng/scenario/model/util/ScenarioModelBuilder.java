/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.util.CommercialModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.lng.pricing.util.CostModelBuilder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;

/**
 * 
 * @author Simon Goodall
 *
 */
public final class ScenarioModelBuilder {

	private final @NonNull LNGScenarioModel lngScenarioModel;

	private final @NonNull PortModelBuilder portModelBuilder;
	private final @NonNull FleetModelBuilder fleetModelBuilder;
	private final @NonNull CommercialModelBuilder commercialModelBuilder;
	private final @NonNull PricingModelBuilder pricingModelBuilder;
	private final @NonNull CostModelBuilder costModelBuilder;
	private final @NonNull SpotMarketsModelBuilder spotMarketsModelBuilder;
	private final @NonNull CargoModelBuilder cargoModelBuilder;

	public ScenarioModelBuilder(final @NonNull LNGScenarioModel lngScenarioModel) {
		this.lngScenarioModel = lngScenarioModel;

		this.portModelBuilder = new PortModelBuilder(ScenarioModelUtil.getPortModel(lngScenarioModel));
		this.fleetModelBuilder = new FleetModelBuilder(ScenarioModelUtil.getFleetModel(lngScenarioModel));
		this.commercialModelBuilder = new CommercialModelBuilder(ScenarioModelUtil.getCommercialModel(lngScenarioModel));
		this.pricingModelBuilder = new PricingModelBuilder(ScenarioModelUtil.getPricingModel(lngScenarioModel));
		this.costModelBuilder = new CostModelBuilder(ScenarioModelUtil.getCostModel(lngScenarioModel));
		this.spotMarketsModelBuilder = new SpotMarketsModelBuilder(ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel));

		this.cargoModelBuilder = new CargoModelBuilder(ScenarioModelUtil.getCargoModel(lngScenarioModel));
	}

	@NonNull
	public LNGScenarioModel getLNGScenarioModel() {
		return lngScenarioModel;
	}

	@NonNull
	public CargoModelBuilder getCargoModelBuilder() {
		return cargoModelBuilder;
	}

	@NonNull
	public PortModelBuilder getPortModelBuilder() {
		return portModelBuilder;
	}

	@NonNull
	public FleetModelBuilder getFleetModelBuilder() {
		return fleetModelBuilder;
	}

	@NonNull
	public CommercialModelBuilder getCommercialModelBuilder() {
		return commercialModelBuilder;
	}

	@NonNull
	public PricingModelBuilder getPricingModelBuilder() {
		return pricingModelBuilder;
	}

	@NonNull
	public CostModelBuilder getCostModelBuilder() {
		return costModelBuilder;
	}

	@NonNull
	public SpotMarketsModelBuilder getSpotMarketsModelBuilder() {
		return spotMarketsModelBuilder;
	}
}
