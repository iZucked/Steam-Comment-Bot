/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;

import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselStateAttributes;
import scenario.presentation.cargoeditor.celleditors.VesselStateAttributesDialog;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class FuelCurveEditor extends DialogInlineEditor {

	public FuelCurveEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain) {
		super(path, feature, editingDomain);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final VesselStateAttributesDialog vsaDialog = new VesselStateAttributesDialog(getShell(),
				(SWT.DIALOG_TRIM & ~SWT.CLOSE)
				| SWT.APPLICATION_MODAL);
		
		final VesselStateAttributes output = vsaDialog.open((VesselStateAttributes) input, true);
		
		if (output == null) return null;
		final ArrayList<FuelConsumptionLine> result = new ArrayList<FuelConsumptionLine>();
		
		for (final FuelConsumptionLine fcl : output.getFuelConsumptionCurve()) {
			result.add(fcl);
		}
		
		output.getFuelConsumptionCurve().removeAll(result);
		
		return result;
	}

	@Override
	protected String render(Object value) {
		return "";
	}

	@Override
	protected Command createSetCommand(final Object value) {
		final CompoundCommand cc = new CompoundCommand(
				CompoundCommand.LAST_COMMAND_ALL);
//		for (final Object o : (EList)getValue()) {
			Command remove = editingDomain.createCommand(
					DeleteCommand.class,
					new CommandParameter(input, feature, (Collection) getValue())
					);
			cc.append(remove);
//		}
		
		Command add = editingDomain.createCommand(AddCommand.class,
				new CommandParameter(input, feature, (Collection<?>) value));
		
		cc.append(add);
		
		return cc;
	}	
}
