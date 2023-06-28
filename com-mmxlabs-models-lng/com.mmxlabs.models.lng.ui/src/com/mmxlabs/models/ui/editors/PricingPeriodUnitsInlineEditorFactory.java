/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.lng.types.PricingPeriod;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class PricingPeriodUnitsInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final ETypedElement feature) {

		final ArrayList<Object> objectsList = new ArrayList<>();
		for (final PricingPeriod type : PricingPeriod.values()) {
			final String name;
			switch (type) {
			case DAYS:
				name = "Days";
				break;
			case WEEKS:
				name = "Weeks";
				break;
			case MONTHS:
				name = "Months";
				break;
			case QUARTERS:
				name = "Quarters";
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
