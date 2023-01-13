/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class ProfileVolumeUnitInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final ETypedElement typedElement) {

		final ArrayList<Object> objectsList = new ArrayList<>();
		for (final LNGVolumeUnit type : LNGVolumeUnit.values()) {
			final String name;
			switch (type) {
			case M3:
				name = "m³";
				break;
			case MMBTU:
				name = "mmBtu";
				break;
			case MT:
				name = "mt";
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
