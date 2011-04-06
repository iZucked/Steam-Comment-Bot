package scenario.presentation.cargoeditor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;

/**
 * A handler which rotates the discharge slots of a list of cargoes, so
 * 
 * <pre>
 *  L1 - D1
 *  L2 - D2
 *  L3 - D3
 * </pre>
 * 
 * becomes
 * 
 * <pre>
 * 	L1 - D3
 *  L2 - D1
 *  L3 - D2
 * </pre>
 * 
 * or similar for a different number of cargoes.
 * 
 * @author Tom Hinton
 * 
 */
public class SwapDischargeHandler extends AbstractHandler implements IHandler {

	private EditingDomain getEditingDomain(final IWorkbenchWindow wbw) {
		if (wbw == null || wbw.getActivePage() == null) {
			return null;
		}
		IWorkbenchPart part = wbw.getActivePage().getActivePart();
		return (part instanceof IEditingDomainProvider ? ((IEditingDomainProvider) part)
				.getEditingDomain() : null);
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			final Object[] selectedObjects = structuredSelection.toArray();

			final EditingDomain editingDomain = getEditingDomain(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow());

			final CompoundCommand rotate = new CompoundCommand();
			final EReference dischargeSlotFeature = CargoPackage.eINSTANCE
					.getCargo_DischargeSlot();
			Cargo previousCargo = (Cargo) selectedObjects[selectedObjects.length - 1];
			Slot previousSlot = previousCargo.getDischargeSlot();

			// we have to clear the discharge slot of the final element
			// so that when we set it in the next case it's not already contained
			// somewhere else
			final Command clearPreviousDischargeSlot = editingDomain
					.createCommand(SetCommand.class, new CommandParameter(
							previousCargo, dischargeSlotFeature, null));
			rotate.append(clearPreviousDischargeSlot);
			
			for (final Object o : selectedObjects) {
				final Cargo cargo = (Cargo) o;

				// set the discharge slot on cargo to whatever the previous slot was.
				final Command setDischargeSlotToPreviousSlot = editingDomain
						.createCommand(SetCommand.class, new CommandParameter(
								cargo, dischargeSlotFeature, previousSlot));

				rotate.append(setDischargeSlotToPreviousSlot);
				previousSlot = cargo.getDischargeSlot();
				previousCargo = cargo;
			}

			editingDomain.getCommandStack().execute(rotate);
		}
		return null;
	}

	@Override
	/**
	 * The action should only be enabled when more than one cargo is selected
	 */
	public boolean isEnabled() {
		final ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();

		if (selection != null && selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Object[] selectedObjects = structuredSelection.toArray();

			if (selectedObjects.length < 2)
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
