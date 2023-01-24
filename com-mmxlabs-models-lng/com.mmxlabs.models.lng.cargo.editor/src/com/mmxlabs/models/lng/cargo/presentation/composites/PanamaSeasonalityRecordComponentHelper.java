/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.dates.MonthInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for PanamaSeasonalityRecord instances
 *
 * @generated NOT
 */
public class PanamaSeasonalityRecordComponentHelper extends DefaultComponentHelper {

	public PanamaSeasonalityRecordComponentHelper() {
		super(CargoPackage.Literals.PANAMA_SEASONALITY_RECORD);

		addEditor(CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_MONTH, topClass -> new MonthInlineEditor(CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_MONTH));
	}
}