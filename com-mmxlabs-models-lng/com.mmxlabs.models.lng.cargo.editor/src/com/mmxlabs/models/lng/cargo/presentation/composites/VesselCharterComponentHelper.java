/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.time.LocalDateTime;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.TextualVesselReferenceInlineEditor;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.date.LocalDateTimeTextFormatter;
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
		
		final String dateString = DateTimeFormatsProvider.INSTANCE.getDateStringEdit();
		final String dateTimeString = DateTimeFormatsProvider.INSTANCE.getDateTimeStringEdit();

		addEditor(CargoPackage.Literals.VESSEL_CHARTER__VESSEL, topClass -> new TextualVesselReferenceInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__VESSEL));

		addEditor(CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, topClass -> 
		new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, 
				new LocalDateTimeTextFormatter(DateTimeFormatsProvider.INSTANCE.getDateTimeStringDisplay(),DateTimeFormatsProvider.INSTANCE.getDateStringDisplay())) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});

		addEditor(CargoPackage.Literals.VESSEL_CHARTER__START_BY, topClass -> 
		new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__START_BY, 
				new LocalDateTimeTextFormatter(dateTimeString, dateString)) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
		addEditor(CargoPackage.Literals.VESSEL_CHARTER__END_AFTER, topClass -> 
		new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__END_AFTER, 
				new LocalDateTimeTextFormatter(dateTimeString, dateString)) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
		addEditor(CargoPackage.Literals.VESSEL_CHARTER__END_BY, topClass -> 
		new LocalDateTimeInlineEditor(CargoPackage.Literals.VESSEL_CHARTER__END_BY, 
				new LocalDateTimeTextFormatter(dateTimeString, dateString)) {
			@Override
			protected Object getInitialUnsetValue() {
				return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
			}
		});
	}
}