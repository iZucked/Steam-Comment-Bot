/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class VolumeLimitUnitsInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {

		ArrayList<Object> objectsList = new ArrayList<>();
		for (final VolumeUnits type : VolumeUnits.values()) {
			final String name;
			switch (type) {
			case M3:
				name = "m³";
				break;
			case MMBTU:
				name = "mmBtu";
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
