/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultInlineEditorFactory;

public class PortInlineEditorFactory extends DefaultInlineEditorFactory {

	public PortInlineEditorFactory() {
		super();
	}

	@Override
	public IInlineEditor createEditor(final EClass owner, final ETypedElement feature) {
		if (feature.isMany()) {
			return new PortMultiReferenceInlineEditor(feature);
		}
		return super.createEditor(owner, feature);
	}

}
