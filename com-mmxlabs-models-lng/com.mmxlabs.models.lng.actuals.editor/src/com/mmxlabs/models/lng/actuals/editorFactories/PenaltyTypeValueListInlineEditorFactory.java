/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.editorFactories;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.actuals.PenaltyType;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

/**
 */
public class PenaltyTypeValueListInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		
		ArrayList<Object> objectsList = new ArrayList<>();
		for (final PenaltyType type : PenaltyType.values()) {
			final String name;
			switch (type) {
			case TOP:
				name = "ToP";
				break;
			case NOT_TOP:
				name = "No ToP";
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
