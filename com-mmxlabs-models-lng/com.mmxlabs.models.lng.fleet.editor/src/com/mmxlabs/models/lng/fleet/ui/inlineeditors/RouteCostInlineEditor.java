package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.window.Window;

import com.mmxlabs.models.ui.editors.impl.DialogInlineEditor;

public class RouteCostInlineEditor extends DialogInlineEditor {
	public RouteCostInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final CanalCostsDialog ccd = new CanalCostsDialog(getShell());
		if (ccd.open(new AdapterFactoryImpl(), commandHandler.getEditingDomain(), input, (EReference) feature) == Window.OK) {
			return ccd.getResult();
		}
		return null;
	}

	@Override
	protected String render(Object value) {
		return "";
	}
}
