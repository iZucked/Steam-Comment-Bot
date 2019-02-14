/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class TimePeriodUnitsInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {

		ArrayList<Object> objectsList = new ArrayList<>();
		for (final TimePeriod type : TimePeriod.values()) {
			if ((type == TimePeriod.HOURS) && (feature == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS
					|| feature == CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS
					|| feature == CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS
					|| feature == CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE_UNITS)) {
				continue;
			}
			final String name;
			switch (type) {
			case HOURS:
				name = "Hours";
				break;
			case DAYS:
				name = "Days";
				break;
			case MONTHS:
				name = "Months";
				break;
			default:
				name = type.getName();
				break;
			}
			objectsList.add(name);
			objectsList.add(type);
		}
		return new EENumInlineEditor((EAttribute) feature, objectsList.toArray());
	}

}
