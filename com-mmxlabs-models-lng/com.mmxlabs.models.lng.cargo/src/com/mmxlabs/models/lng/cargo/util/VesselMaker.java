/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.port.RouteOption;

public class VesselMaker {
	@NonNull
	private final FleetModelBuilder fleeetModelBuilder;

	@NonNull
	private final Vessel vessel;

	public VesselMaker(@NonNull final FleetModelBuilder fleetModelBuilder) {
		fleeetModelBuilder = fleetModelBuilder;
		this.vessel = FleetFactory.eINSTANCE.createVessel();
	}

	public VesselMaker withInaccessibleRoutes(final Collection<RouteOption> routeOptions) {
		EList<RouteOption> inaccessibleRoutes = vessel.getVesselOrDelegateInaccessibleRoutes();
		inaccessibleRoutes.clear();
		inaccessibleRoutes.addAll(routeOptions);
		return this;
	}

	@NonNull
	public Vessel build() {

		fleeetModelBuilder.addVessel(vessel);

		return vessel;
	}
}
