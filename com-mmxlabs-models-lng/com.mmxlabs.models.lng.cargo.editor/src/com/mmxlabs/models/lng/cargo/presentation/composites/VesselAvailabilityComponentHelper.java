/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
 * A component helper for VesselAvailability instances
 *
 * @generated NOT
 */
public class VesselAvailabilityComponentHelper extends DefaultComponentHelper {

	public VesselAvailabilityComponentHelper() {
		super(CargoPackage.Literals.VESSEL_AVAILABILITY);

		ignoreFeatures.add(CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL);
		ignoreFeatures.add(CargoPackage.Literals.VESSEL_AVAILABILITY__END_HEEL);
		ignoreFeatures.add(CargoPackage.Literals.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE);
		ignoreFeatures.add(CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT);

		addEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL, topClass -> new TextualVesselReferenceInlineEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL));

		addEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});

		addEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
		addEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
		addEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY, topClass -> new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
	}
}