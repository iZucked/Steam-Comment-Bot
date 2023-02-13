/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for VesselEvent instances
 *
 * @generated NOT
 */
public class VesselEventComponentHelper extends DefaultComponentHelper {

	public VesselEventComponentHelper() {
		super(CargoPackage.Literals.VESSEL_EVENT);

//		ignoreFeatures.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX);

		
		addEditor(CargoPackage.Literals.VESSEL_EVENT__PORT, topClass -> new TextualPortReferenceInlineEditor(CargoPackage.Literals.VESSEL_EVENT__PORT));
	}
}