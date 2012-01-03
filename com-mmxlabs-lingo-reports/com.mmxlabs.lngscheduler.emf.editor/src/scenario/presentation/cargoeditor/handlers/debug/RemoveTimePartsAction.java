/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.debug;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import scenario.ScenarioPackage;
import scenario.presentation.cargoeditor.handlers.ScenarioModifyingAction;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;

/**
 * An action which iterates through all the attributes in the selection, finds any {@link DateAndOptionalTime} attributes, and clears the optional time part of those values
 * 
 * does not work on attributes with multiplicity > 1
 * 
 * @author hinton
 *
 */
public class RemoveTimePartsAction extends ScenarioModifyingAction {

	public RemoveTimePartsAction() {
		super();
		setText("Clear Times");
		setToolTipText("Clear the time part of any dates with port-default times");
		setEnabled(false);
	}

	@Override
	protected boolean isApplicableToSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			for (final Object o : ((IStructuredSelection) selection).toList()) {
				if (o instanceof EObject) return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		final ISelection selection = getLastSelection();
		if (selection instanceof IStructuredSelection) {
			for (final Object o : ((IStructuredSelection) selection).toList()) {
				if (o instanceof EObject) 
					snap((EObject) o);
			}
		}
	}

	/**
	 * Clear the time part of any date and optional times in o
	 * @param o
	 */
	private void snap(EObject o) {
		final CompoundCommand cc = new CompoundCommand();
		final EditingDomain ed = getEditingDomain(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		final TreeIterator<EObject> iterator = o.eAllContents();
		
		while (o != null) {
			for (final EAttribute attribute : o.eClass().getEAllAttributes()) {
				if (attribute.getEAttributeType() == ScenarioPackage.eINSTANCE.getDateAndOptionalTime()) {
					if (attribute.isMany()) continue;
					final Object daot = o.eGet(attribute);
					if (daot instanceof DateAndOptionalTime) {
						cc.append(SetCommand.create(ed, o, attribute, new DateAndOptionalTime((DateAndOptionalTime) daot, true)));
					}
				}
			}
			o = iterator.hasNext() ? iterator.next() : null;
		}
		
		ed.getCommandStack().execute(cc);
	}
}
