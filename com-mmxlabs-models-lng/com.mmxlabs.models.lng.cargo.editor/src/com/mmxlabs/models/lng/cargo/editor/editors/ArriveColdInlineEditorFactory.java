/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class ArriveColdInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(EClass owner, ETypedElement feature) {
		return new ArriveColdInlineEditor((EAttribute) feature);
	}
}
