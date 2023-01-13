/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for CanalBookingSlot instances
 *
 * @generated NOT
 */
public class CanalBookingSlotComponentHelper extends DefaultComponentHelper {

	public CanalBookingSlotComponentHelper() {
		super(CargoPackage.Literals.CANAL_BOOKING_SLOT);

		// Ignore others?
//		add_bookingDateEditor(detailComposite, topClass);
//		add_canalEntranceEditor(detailComposite, topClass);
//		add_vesselEditor(detailComposite, topClass);
//		add_bookingCodeEditor(detailComposite, topClass);
//		add_notesEditor(detailComposite, topClass);

		ignoreFeatures.add(CargoPackage.Literals.CANAL_BOOKING_SLOT__ROUTE_OPTION);

		addEditor(CargoPackage.Literals.CANAL_BOOKING_SLOT__NOTES, topClass -> new MultiTextInlineEditor(CargoPackage.Literals.CANAL_BOOKING_SLOT__NOTES));
	}
}