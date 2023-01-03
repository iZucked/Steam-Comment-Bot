/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.time.LocalDateTime;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.TextualVesselReferenceInlineEditor;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.LocalDateTimeInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for VesselCharter instances
 *
 * @generated NOT
 */
public class VesselCharterComponentHelper extends DefaultComponentHelper {

	public VesselCharterComponentHelper() {
		super(CargoPackage.Literals.VESSEL_CHARTER);

		ignoreFeatures.add(CargoPackage.Literals.VESSEL_CHARTER__START_HEEL);
		ignoreFeatures.add(CargoPackage.Literals.VESSEL_CHARTER__END_HEEL);
		ignoreFeatures.add(CargoPackage.Literals.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE);
		ignoreFeatures.add(CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT);

		addEditor(CargoPackage.Literals.VESSEL_CHARTER__VESSEL, topClass -> new TextualVesselReferenceInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__VESSEL));

		addEditor(CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__START_AFTER) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});

		addEditor(CargoPackage.Literals.VESSEL_CHARTER__START_BY, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__START_BY) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
		addEditor(CargoPackage.Literals.VESSEL_CHARTER__END_AFTER, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__END_AFTER) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
		addEditor(CargoPackage.Literals.VESSEL_CHARTER__END_BY, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__END_BY) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
	}
}