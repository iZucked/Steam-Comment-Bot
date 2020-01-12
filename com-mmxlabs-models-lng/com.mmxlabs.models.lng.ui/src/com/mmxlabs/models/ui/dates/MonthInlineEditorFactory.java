/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class MonthInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(EClass owner, EStructuralFeature feature) {
		return new MonthInlineEditor(feature);
	}
}
