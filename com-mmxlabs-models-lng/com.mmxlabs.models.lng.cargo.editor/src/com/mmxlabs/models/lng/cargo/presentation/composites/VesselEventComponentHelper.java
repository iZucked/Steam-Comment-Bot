/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for VesselEvent instances
 *
 * @generated NOT
 */
public class VesselEventComponentHelper extends DefaultComponentHelper {

	public VesselEventComponentHelper() {
		super(CargoPackage.Literals.VESSEL_EVENT);

		// ignoreFeatures.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX);

		addEditor(CargoPackage.Literals.VESSEL_EVENT__PORT, topClass -> new TextualPortReferenceInlineEditor(CargoPackage.Literals.VESSEL_EVENT__PORT));
		addEditor(CargoPackage.Literals.VESSEL_EVENT__NOTES, topClass -> new MultiTextInlineEditor(CargoPackage.Literals.VESSEL_EVENT__NOTES));

	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// Move the notes editor to the end
		
		var feature = CargoPackage.Literals.VESSEL_EVENT__NOTES;
		// There may be multiple editors for the same feature, so gather them here...
		final List<IInlineEditor> editorsForFeature = new LinkedList<>();
		for (final var editor : editors) {
			if (editor.getFeature() == feature) {
				editorsForFeature.add(editor);
			}
		}
		// Then move them to the end of the editors list in order.
		if (!editorsForFeature.isEmpty()) {
			editors.removeAll(editorsForFeature);
		}
		editors.addAll(editorsForFeature);
	}
}