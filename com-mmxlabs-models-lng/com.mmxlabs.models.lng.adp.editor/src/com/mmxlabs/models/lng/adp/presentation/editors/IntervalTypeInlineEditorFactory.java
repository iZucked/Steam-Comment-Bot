/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class IntervalTypeInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final ETypedElement typedElement) {

		ArrayList<Object> objectsList = new ArrayList<>();
		for (final IntervalType type : IntervalType.values()) {
			final String name;
			switch (type) {
			case YEARLY:
				name = "Yearly";
				break;
			case MONTHLY:
				name = "Monthly";
				break;
			case QUARTERLY:
				name = "Quarterly";
				break;
			case WEEKLY:
				// Hide this for now
				continue;
//				name = "Weekly";
//				break;
			case BIMONTHLY:
				name = "Bimonthly";
				break;
			default:
				name = type.getName();
				break;
			}
			objectsList.add(name);
			objectsList.add(type);
		}
		return new EENumInlineEditor((EAttribute) typedElement, objectsList.toArray());
	}

}
