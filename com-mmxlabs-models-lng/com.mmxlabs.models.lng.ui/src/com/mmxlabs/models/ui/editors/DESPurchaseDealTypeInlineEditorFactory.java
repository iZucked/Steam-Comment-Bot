/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class DESPurchaseDealTypeInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {

		final ArrayList<Object> objectsList = new ArrayList<>();
		for (final DESPurchaseDealType type : DESPurchaseDealType.values()) {
			final String name;
			switch (type) {
			case DEST_ONLY:
				name = "Dest only";
				break;
			case DEST_WITH_SOURCE:
				name = "Dest with source";
				break;
			case DIVERT_FROM_SOURCE:
				name = "Divert from source";
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
