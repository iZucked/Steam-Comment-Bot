/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.Collection;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.window.Window;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.impl.DialogInlineEditor;

public class RouteCostInlineEditor extends DialogInlineEditor {
	private MMXRootObject rootObject;

	public RouteCostInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IScenarioEditingLocation location, MMXRootObject context, EObject input, Collection<EObject> range) {
		this.rootObject = context;
		super.display(location, context, input, range);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final CanalCostsDialog ccd = new CanalCostsDialog(getShell());
		if (ccd.open(new AdapterFactoryImpl(), commandHandler.getEditingDomain(), rootObject, input, (EReference) feature) == Window.OK) {
			return ccd.getResult();
		}
		return null;
	}

	@Override
	protected String render(Object value) {
		return "";
	}
}
