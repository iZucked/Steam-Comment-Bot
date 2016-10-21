/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class SuezCanalTariffTugBandInlineEditorFactory implements IInlineEditorFactory {

	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		return new SuezCanalTariffTugBandInlineEditor(feature);
	}
}
