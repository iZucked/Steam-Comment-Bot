/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.window.Window;

import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.dialogs.CanalCostsDialog;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class VesselClassCostEditor extends DialogInlineEditor {

	private ScenarioEditor part;

	public VesselClassCostEditor(EMFPath path, EStructuralFeature feature,
			final ICommandProcessor processor, final ScenarioEditor part) {
		super(path, feature, part.getEditingDomain(), processor);
		this.part = part;
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final CanalCostsDialog ccd = new CanalCostsDialog(getShell());

		if (ccd.open(part.getAdapterFactory(), part.getEditingDomain(), part
				.getScenario().getCanalModel(), (EObject) input,
				(EReference) feature) == Window.OK) {
			return ccd.getResult(); // ????
		}

		return null;
	}

	@Override
	protected String render(Object value) {
		return "";
	}
}
