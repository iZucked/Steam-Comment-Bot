/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.spacing.containers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.ortools.sat.IntervalVar;

@NonNullByDefault
public class ShippedCargoModellingContainer {

	private InPortContainer loadPortVariables;
	private InPortContainer dischargePortVariables;
	private TravelContainer ladenTravelVariables;
	private TravelContainer ballastTravelVariables;
	private IntervalVar cargoInterval;

	ShippedCargoModellingContainer(final InPortContainer loadPortVariables, final TravelContainer ladenTravelVariables, final InPortContainer dischargePortVariables,
			final TravelContainer ballastTravelVariables, final IntervalVar cargoInterval) {
		this.loadPortVariables = loadPortVariables;
		this.ladenTravelVariables = ladenTravelVariables;
		this.dischargePortVariables = dischargePortVariables;
		this.ballastTravelVariables = ballastTravelVariables;
		this.cargoInterval = cargoInterval;
	}

	public InPortContainer getLoadPortVariables() {
		return this.loadPortVariables;
	}
	
	public TravelContainer getLadenTravelVariables() {
		return this.ladenTravelVariables;
	}
	
	public InPortContainer getDischargePortVariables() {
		return this.dischargePortVariables;
	}
	
	public TravelContainer getBallastTravelVariables() {
		return this.ballastTravelVariables;
	}

	public IntervalVar getCargoInterval() {
		return this.cargoInterval;
	}

	public static ContainerBuilder startBuilding() {
		return new ContainerBuilder();
	}
}
