/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

/**
 */
public class PaperDealTypeValueListInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		
		ArrayList<Object> objectsList = new ArrayList<>();
		for (final PaperPricingType type : PaperPricingType.values()) {
			final String name;
			switch (type) {
			case PERIOD_AVG:
				name = "Period daily average";
				break;
			case CALENDAR:
				name = "Calendar";
				break;
			case INSTRUMENT:
				name = "Instrument";
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
