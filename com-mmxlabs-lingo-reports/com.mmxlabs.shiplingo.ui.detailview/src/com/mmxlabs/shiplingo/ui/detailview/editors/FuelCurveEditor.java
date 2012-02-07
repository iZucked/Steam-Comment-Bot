/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.window.Window;

import scenario.fleet.VesselStateAttributes;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.shiplingo.ui.detailview.editors.dialogs.VesselStateAttributesDialog2;

public class FuelCurveEditor extends DialogInlineEditor {

	public FuelCurveEditor(final EMFPath path, final EStructuralFeature feature, final EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final VesselStateAttributesDialog2 dialog = new VesselStateAttributesDialog2(getShell());

		if (dialog.open((VesselStateAttributes) input, true) == Window.OK) {
			return dialog.getResult().getFuelConsumptionCurve();
		} else {
			return null;
		}
	}

	@Override
	protected String render(final Object value) {
		return "";
	}
}
