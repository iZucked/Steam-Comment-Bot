/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for VesselStateAttributes instances
 *
 * @generated NOT
 */
public class VesselStateAttributesComponentHelper extends DefaultComponentHelper {

	public VesselStateAttributesComponentHelper() {
		super(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES);

		ignoreFeatures.add(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE);
	}
}