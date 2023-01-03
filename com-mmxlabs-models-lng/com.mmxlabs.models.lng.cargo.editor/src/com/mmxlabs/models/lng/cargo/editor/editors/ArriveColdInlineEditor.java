/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.editors.impl.YesNoInlineEditor;

public class ArriveColdInlineEditor extends YesNoInlineEditor {

	public ArriveColdInlineEditor(final EAttribute feature) {
		super(feature);
	}

	@Override
	protected Object getValue() {
		if (typedElement instanceof EStructuralFeature feature) {
			if (input != null && (!feature.isUnsettable() || input.eIsSet(feature))) {
				return super.getValue();
			} else {
				if (input instanceof MMXObject) {
					final Object unsetValue = ((MMXObject) input).getUnsetValue(feature);
					if (unsetValue instanceof Boolean) {
						// Arrive cold override allow cooldown - the boolean inference is reversed
						final Boolean b = (Boolean) unsetValue;
						return !b;
					}
					return unsetValue;
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
}
