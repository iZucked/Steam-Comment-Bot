/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
  * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import com.mmxlabs.models.lng.pricing.presentation.composites.CurveInlineEditor;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for SpotAvailability instances
 *
 * @generated NOT
 */
public class SpotAvailabilityComponentHelper extends DefaultComponentHelper {

	public SpotAvailabilityComponentHelper() {
		super(SpotMarketsPackage.Literals.SPOT_AVAILABILITY);

		addEditor(SpotMarketsPackage.Literals.SPOT_AVAILABILITY__CURVE, topClass -> new CurveInlineEditor(SpotMarketsPackage.Literals.SPOT_AVAILABILITY__CURVE));
	}

}