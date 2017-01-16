/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.editorfactories;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

/**
 */
public class PricingEventValueListInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		final ArrayList<Object> objectsList = new ArrayList<>(2 * (PricingEvent.values().length + 1));

		if (feature.isUnsettable()) {
			objectsList.add("<Not set>");
			objectsList.add(null);
		}

		for (final PricingEvent type : PricingEvent.values()) {
			final String name;
			switch (type) {
			case END_DISCHARGE:
				name = "End of Discharge";
				break;
			case END_LOAD:
				name = "End of Load";
				break;
			case START_DISCHARGE:
				name = "Start of Discharge";
				break;
			case START_LOAD:
				name = "Start of Load";
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
