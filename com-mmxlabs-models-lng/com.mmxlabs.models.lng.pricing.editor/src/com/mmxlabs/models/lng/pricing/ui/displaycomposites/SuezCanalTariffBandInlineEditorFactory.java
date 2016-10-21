package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class SuezCanalTariffBandInlineEditorFactory implements IInlineEditorFactory {

	@Override
	public IInlineEditor createEditor(EClass owner, EStructuralFeature feature) {
		return new SuezCanalTariffBandInlineEditor(feature);
	}
}
