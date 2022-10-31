/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class VesselEventVesselsInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final ETypedElement feature) {
		return new VesselEventVesselsInlineEditor(feature);
	}
}
