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
	@Override
	public Command provideAdditionalCommand(EditingDomain editingDomain,MMXRootObject rootObject, Map<EObject, EObject> overrides,Class<? extends Command> commandClass,CommandParameter parameter, Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getEAttribute() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
				final EObject o = parameter.getEOwner();
				if (o instanceof LoadSlot) {
					final Cargo c = ((LoadSlot) o).getCargo();
					return fixTask(c, editingDomain, rootObject, overrides);
				} else if (o instanceof DischargeSlot) {
					final Cargo c = ((DischargeSlot) o).getCargo();
					return fixTask(c, editingDomain, rootObject, overrides);
				}
			} else if (parameter.getEAttribute() == FleetPackage.eINSTANCE.getVesselEvent_StartAfter() 
					|| parameter.getEAttribute() == FleetPackage.eINSTANCE.getVesselEvent_StartBy()
					|| parameter.getEAttribute() == FleetPackage.eINSTANCE.getVesselEvent_DurationInDays()) {
				final EObject o = parameter.getEOwner();
				if (o instanceof VesselEvent) {
					return fixTask((VesselEvent) o, editingDomain, rootObject, overrides);
				}
			}
		}
		return null;
	}



	private Command fixTask(UUIDObject o, EditingDomain editingDomain,
			MMXRootObject rootObject, Map<EObject, EObject> overrides) {
		InputModel input = rootObject.getSubModel(InputModel.class);
		if (input != null) {
			if (overrides.containsKey(input)) input = (InputModel) overrides.get(input);
			final Assignment a = AssignmentEditorHelper.getAssignmentForTask(input, o);
			if (a != null) return AssignmentEditorHelper.taskReassigned(editingDomain, input, o, null, null, null, a);
		}
		return null;
	}

	@Override
	public void startCommandProvision() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endCommandProvision() {
		// TODO Auto-generated method stub
		
	}
	
}
