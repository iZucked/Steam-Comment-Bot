/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class RouteParametersInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(EClass owner, EStructuralFeature feature) {
		return new RouteParametersInlineEditor(feature);
	}
}
