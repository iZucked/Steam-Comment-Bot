/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

/**
 * @author hinton
 *
 */
public class TimeInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(EClass owner, EStructuralFeature feature) {
		return new TimeOfDayInlineEditor(feature);
	}
}
