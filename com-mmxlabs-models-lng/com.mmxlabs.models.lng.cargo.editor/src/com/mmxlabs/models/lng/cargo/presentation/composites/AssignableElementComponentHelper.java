/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.SpotIndexInlineEditor;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.AssignableElementEditorWrapper;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.AssignmentInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for AssignableElement instances
 *
 * @generated NOT
 */
public class AssignableElementComponentHelper extends DefaultComponentHelper {

	public AssignableElementComponentHelper() {
		super(CargoPackage.Literals.ASSIGNABLE_ELEMENT);

		// Hide this feature
		ignoreFeatures.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT);

		// Set custom editors
		addEditor(CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, topClass -> {
			if (topClass == CargoPackage.Literals.CARGO) {
				return new AssignableElementEditorWrapper(new SpotIndexInlineEditor(CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX));
			}
			return null;
		});

		addDefaultEditorWithWrapper(CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, AssignableElementEditorWrapper::new);

		addEditor(CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE,
				topClass -> new AssignableElementEditorWrapper(new AssignmentInlineEditor(CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE)));
	}
}