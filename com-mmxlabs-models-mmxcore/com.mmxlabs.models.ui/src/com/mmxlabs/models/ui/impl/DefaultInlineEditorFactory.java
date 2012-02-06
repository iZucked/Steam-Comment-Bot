package com.mmxlabs.models.ui.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

/**
 * Utterly standard inline editor factory.
 * 
 * @author hinton
 *
 */
public class DefaultInlineEditorFactory implements IInlineEditorFactory {

	@Override
	public IInlineEditor createEditor(EClass owner, EStructuralFeature feature) {

		return null;
	}

}
