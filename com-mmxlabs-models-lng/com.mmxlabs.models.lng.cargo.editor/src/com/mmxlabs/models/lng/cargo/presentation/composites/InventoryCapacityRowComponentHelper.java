/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for InventoryCapacityRow instances
 *
 * @generated NOT
 */
public class InventoryCapacityRowComponentHelper extends DefaultComponentHelper {

	public InventoryCapacityRowComponentHelper() {
		super(CargoPackage.Literals.INVENTORY_CAPACITY_ROW);
		if(!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_CV_MODEL)) {
			ignoreFeatures.add(CargoPackage.Literals.INVENTORY_CAPACITY_ROW__MIN_CV);
			ignoreFeatures.add(CargoPackage.Literals.INVENTORY_CAPACITY_ROW__MAX_CV);
		}
	}
}