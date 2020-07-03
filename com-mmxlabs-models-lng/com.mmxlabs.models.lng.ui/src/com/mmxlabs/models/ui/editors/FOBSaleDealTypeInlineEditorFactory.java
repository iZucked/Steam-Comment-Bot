/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class FOBSaleDealTypeInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {

		final ArrayList<Object> objectsList = new ArrayList<>();
		for (final FOBSaleDealType type : FOBSaleDealType.values()) {
			final String name;
			switch (type) {
			case SOURCE_ONLY:
				name = "Source only";
				break;
			case SOURCE_WITH_DEST:
				name = "Source with dest";
				break;
			case DIVERT_TO_DEST:
				name = "Divert to dest";
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
