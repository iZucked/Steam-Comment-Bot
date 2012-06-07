package com.mmxlabs.models.lng.input.commands;

import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;

public class TimeSortingCommandProvider implements IModelCommandProvider {

	private boolean ignored = false;

	private int depth = 0;

	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Class<? extends Command> commandClass,
			final CommandParameter parameter, final Command input) {

		// TODO: ignored should really be a map of slot/cargo to change
		if (commandClass == SetCommand.class) {
			if (parameter.getEAttribute() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
				ignored = true;
			}
			if (parameter.getEAttribute() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
				ignored = true;
			}
		}

		if (!ignored && commandClass == SetCommand.class) {
			if (parameter.getEAttribute() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
				final EObject o = parameter.getEOwner();
				if (o instanceof LoadSlot) {
					final Cargo c = ((LoadSlot) o).getCargo();
					return fixTask(c, editingDomain, rootObject, overrides);
				} else if (o instanceof DischargeSlot) {
					final Cargo c = ((DischargeSlot) o).getCargo();
					return fixTask(c, editingDomain, rootObject, overrides);
				}
			} else if (parameter.getEAttribute() == FleetPackage.eINSTANCE.getVesselEvent_StartAfter() || parameter.getEAttribute() == FleetPackage.eINSTANCE.getVesselEvent_StartBy()
					|| parameter.getEAttribute() == FleetPackage.eINSTANCE.getVesselEvent_DurationInDays()) {
				final EObject o = parameter.getEOwner();
				if (o instanceof VesselEvent) {
					return fixTask((VesselEvent) o, editingDomain, rootObject, overrides);
				}
			}
		}
		return null;
	}

	private Command fixTask(final UUIDObject o, final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides) {
		InputModel input = rootObject.getSubModel(InputModel.class);
		if (input != null) {
			if (overrides.containsKey(input))
				input = (InputModel) overrides.get(input);
			final Assignment a = AssignmentEditorHelper.getAssignmentForTask(input, o);
			if (a != null)
				return AssignmentEditorHelper.taskReassigned(editingDomain, input, o, null, null, null, a);
		}
		return null;
	}

	@Override
	public void startCommandProvision() {
		if (depth == 0) {
			// Reset flag
			ignored = false;
		}
		depth++;
	}

	@Override
	public void endCommandProvision() {
		depth--;
	}

}
