/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.presentation.composites;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for CargoActuals instances
 *
 * @generated NOT
 */
public class CargoActualsComponentHelper extends DefaultComponentHelper {
	public CargoActualsComponentHelper() {
		super(ActualsPackage.Literals.CARGO_ACTUALS);
		
		
		addDefaultEditorForLicenseFeature("features:cargo-actual-additional-attributes", ActualsPackage.Literals.CARGO_ACTUALS__CONTRACT_YEAR);
		addDefaultEditorForLicenseFeature("features:cargo-actual-additional-attributes", ActualsPackage.Literals.CARGO_ACTUALS__OPERATION_NUMBER);
		addDefaultEditorForLicenseFeature("features:cargo-actual-additional-attributes", ActualsPackage.Literals.CARGO_ACTUALS__SUB_OPERATION_NUMBER);
		addDefaultEditorForLicenseFeature("features:cargo-actual-additional-attributes", ActualsPackage.Literals.CARGO_ACTUALS__SELLER_ID);
		addDefaultEditorForLicenseFeature("features:cargo-actual-additional-attributes", ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE);
		addDefaultEditorForLicenseFeature("features:cargo-actual-additional-attributes", ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE_SELLER);
	}
}