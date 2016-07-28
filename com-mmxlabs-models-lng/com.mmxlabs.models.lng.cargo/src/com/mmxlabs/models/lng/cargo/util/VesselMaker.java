/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.port.RouteOption;

public class VesselMaker {
	@NonNull
	private final FleetModelBuilder fleeetModelBuilder;

	@NonNull
	private final Vessel vessel;

	public VesselMaker(@NonNull final FleetModelBuilder fleetModelBuilder, @NonNull VesselClass vesselClass) {
		fleeetModelBuilder = fleetModelBuilder;
		this.vessel = FleetFactory.eINSTANCE.createVessel();
	}

	public VesselMaker withOverrideInaccessibleRoutes(final boolean overrider) {
		vessel.setOverrideInaccessibleRoutes(overrider);
		return this;
	}

	public VesselMaker withInaccessibleRoutes(final Collection<RouteOption> routeOptions) {
		EList<RouteOption> inaccessibleRoutes = vessel.getInaccessibleRoutes();
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
