/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import java.util.Date;
import java.util.Random;

import javax.management.timer.Timer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.presentation.cargoeditor.LockableAction;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;

/**
 * A debug-mode action to jiggle cargoes around
 * 
 * @author hinton
 *
 */
public class PerturbCargoesAction extends LockableAction implements ISelectionChangedListener {
	public PerturbCargoesAction() {
		setToolTipText("Perturb load and discharge times");
		setText("Perturb");
		setEnabled(false);
	}

	@Override
	public void run() {
		final InputDialog input = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Perturb Cargoes", "Enter the number of hours of perturbation (+/- half this value will be added)", "24",
				new IInputValidator() {

					@Override
					public String isValid(String newText) {
						try {
							Integer.parseInt(newText);
						} catch (NumberFormatException e) {
							return newText + " is not a number";
						}
						;
						return null;
					}
				});
		if (input.open() == Window.OK) {
			final int value = Integer.parseInt(input.getValue());
			final CompoundCommand command = new CompoundCommand();
			final EditingDomain editingDomain = getEditingDomain(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			final Random r = new Random();
			r.setSeed(System.currentTimeMillis());
			assert editingDomain != null;
			for (final Object o : theSelection.toArray()) {
				if (o instanceof Cargo) {
					final int r1 = RandomHelper.nextIntBetween(r, -value/2, value/2);
					final int r2 = RandomHelper.nextIntBetween(r, -value/2, value/2);
					final Cargo c = (Cargo) o;
					final LoadSlot l = c.getLoadSlot();
					final Slot d = c.getDischargeSlot();
					final Date ld = 
							new DateAndOptionalTime(
									new Date(l.getWindowStart().getTime() + Timer.ONE_HOUR * r1), false);
					final Date dd = 
							new DateAndOptionalTime(
									new Date(d.getWindowStart().getTime() + Timer.ONE_HOUR * r2), false);
					
					command.append(SetCommand.create(editingDomain, l, CargoPackage.eINSTANCE.getSlot_WindowStart(), ld));
					command.append(SetCommand.create(editingDomain, d, CargoPackage.eINSTANCE.getSlot_WindowStart(), dd));
				}
			}
			
			editingDomain.getCommandStack().execute(command);
		}
	}

	private IStructuredSelection theSelection = null;
	
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			setEnabled(ss.size() > 0);
			theSelection = ss;
		} else {
			setEnabled(false);
		}
	}

	private EditingDomain getEditingDomain(final IWorkbenchWindow wbw) {
		if (wbw == null || wbw.getActivePage() == null) {
			return null;
		}
		IWorkbenchPart part = wbw.getActivePage().getActivePart();
		return (part instanceof IEditingDomainProvider ? ((IEditingDomainProvider) part)
				.getEditingDomain() : null);
	}
}
