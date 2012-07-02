package com.mmxlabs.models.lng.port.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultInlineEditorFactory;

public class PortInlineEditorFactory extends DefaultInlineEditorFactory {

	public PortInlineEditorFactory() {
		super();
	}

	@Override
	public IInlineEditor createEditor(EClass owner, EStructuralFeature feature) {

		return new PortMultiReferenceInlineEditor(feature);
	}

}
