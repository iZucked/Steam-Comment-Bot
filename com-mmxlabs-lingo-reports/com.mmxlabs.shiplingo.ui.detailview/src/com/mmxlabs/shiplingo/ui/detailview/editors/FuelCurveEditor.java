/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;

import scenario.fleet.VesselStateAttributes;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.shiplingo.ui.detailview.editors.dialogs.VesselStateAttributesDialog;

public class FuelCurveEditor extends DialogInlineEditor {

	public FuelCurveEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain,final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final VesselStateAttributesDialog vsaDialog = new VesselStateAttributesDialog(getShell(),
				(SWT.DIALOG_TRIM & ~SWT.CLOSE)
				| SWT.APPLICATION_MODAL);
		
		final VesselStateAttributes output = vsaDialog.open((VesselStateAttributes) input, true);
		
		if (output == null) return null;
		return output.getFuelConsumptionCurve();
	}

	@Override
	protected String render(Object value) {
		return "";
	}
}
