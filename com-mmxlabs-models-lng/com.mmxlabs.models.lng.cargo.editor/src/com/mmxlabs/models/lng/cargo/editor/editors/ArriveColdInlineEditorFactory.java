/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class ArriveColdInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(EClass owner, EStructuralFeature feature) {
		return new ArriveColdInlineEditor((EAttribute) feature);
	}
}
