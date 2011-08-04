/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.window.Window;

import scenario.Scenario;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;
import com.mmxlabs.shiplingo.ui.detailview.editors.dialogs.CanalCostsDialog;

public class VesselClassCostEditor extends DialogInlineEditor {
	private IValueProviderProvider valueProviderProvider;

	public VesselClassCostEditor(EMFPath path, EStructuralFeature feature,
			final ICommandProcessor processor,
			final EditingDomain editingDomain,
			final IValueProviderProvider valueProviderProvider) {
		super(path, feature, editingDomain, processor);
		this.valueProviderProvider = valueProviderProvider;
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final CanalCostsDialog ccd = new CanalCostsDialog(getShell());

		if (ccd.open(
				((AdapterFactoryEditingDomain) editingDomain)
						.getAdapterFactory(),
				editingDomain,
				((Scenario) (valueProviderProvider.getModel())).getCanalModel(),
				(EObject) input, (EReference) feature) == Window.OK) {
			return ccd.getResult();
		}

		return null;
	}

	@Override
	protected String render(Object value) {
		return "";
	}
}
