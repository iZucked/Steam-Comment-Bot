/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;

/**
 * Replicates a cargo
 * 
 * @author hinton
 * 
 */
public class ReplicateCargoHandler extends AbstractHandler implements IHandler {

	private EditingDomain getEditingDomain(final IWorkbenchWindow wbw) {
		if (wbw == null || wbw.getActivePage() == null) {
			return null;
		}
		IWorkbenchPart part = wbw.getActivePage().getActivePart();
		return (part instanceof IEditingDomainProvider ? ((IEditingDomainProvider) part).getEditingDomain() : null);
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			final Object[] selectedObjects = structuredSelection.toArray();

			final EditingDomain editingDomain = getEditingDomain(PlatformUI.getWorkbench().getActiveWorkbenchWindow());

			// gather parameters

			final ReplicateCargoDialog dialog = new ReplicateCargoDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());

			if (dialog.open() == Window.OK) {
				final List<Calendar> calendars = new ArrayList<Calendar>();
				for (final Object object : selectedObjects) {
					if (object instanceof Cargo) {
						final Cargo cargo = (Cargo) object;
						calendars.add((Calendar) cargo.getLoadSlot().getLocalWindowStart());
						calendars.add((Calendar) cargo.getDischargeSlot().getLocalWindowStart());
					}
				}

				final List<List<Calendar>> newCalendars = dialog.repeatDates(calendars);

				int counter = 1;
				final CompoundCommand cc = new CompoundCommand();

				if (dialog.isReplicating()) {
					for (final List<Calendar> replica : newCalendars) {
						final Iterator<Calendar> it = replica.iterator();
						for (final Object object : selectedObjects) {
							if (object instanceof Cargo) {
								final Cargo orig = (Cargo) object;
								final Cargo copy = EcoreUtil.copy(orig);
								copy.getLoadSlot().setWindowStart(new DateAndOptionalTime(it.next().getTime(), orig.getLoadSlot().getWindowStart().isOnlyDate()));
								copy.getLoadSlot().setId(copy.getLoadSlot().getId() + "-replica-" + counter);
								copy.getDischargeSlot().setWindowStart(new DateAndOptionalTime(it.next().getTime(), orig.getDischargeSlot().getWindowStart().isOnlyDate()));
								copy.getDischargeSlot().setId(copy.getDischargeSlot().getId() + "-replica-" + counter);
								copy.setId(copy.getId() + "-replica-" + counter);
								cc.append(AddCommand.create(editingDomain, orig.eContainer(), orig.eContainingFeature(), copy));
							}
						}
						counter++;
					}
				} else {
					assert newCalendars.size() == 1;
					final List<Calendar> replica = newCalendars.get(0);
					final Iterator<Calendar> iterator = replica.iterator();
					for (final Object object : selectedObjects) {
						if (object instanceof Cargo) {
							final Cargo cargo = (Cargo) object;
							cc.append(SetCommand.create(editingDomain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), new DateAndOptionalTime(iterator.next().getTime(), cargo
									.getLoadSlot().getWindowStart().isOnlyDate())

							));
							cc.append(SetCommand.create(editingDomain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), new DateAndOptionalTime(iterator.next().getTime(), cargo
									.getDischargeSlot().getWindowStart().isOnlyDate())));
						}
					}
				}
				editingDomain.getCommandStack().execute(cc);
			}
		}
		return null;
	}

	@Override
	/**
	 * The action should only be enabled when more than one cargo is selected
	 */
	public boolean isEnabled() {
		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Object[] selectedObjects = structuredSelection.toArray();

			if (selectedObjects.length < 1)
				return false;
			for (final Object o : selectedObjects) {
				if (!(o instanceof Cargo)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
