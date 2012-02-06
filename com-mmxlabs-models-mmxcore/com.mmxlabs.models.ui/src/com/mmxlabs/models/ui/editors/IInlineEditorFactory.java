package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface IInlineEditorFactory {
	IInlineEditor createEditor(EClass owner, EStructuralFeature feature);
}
