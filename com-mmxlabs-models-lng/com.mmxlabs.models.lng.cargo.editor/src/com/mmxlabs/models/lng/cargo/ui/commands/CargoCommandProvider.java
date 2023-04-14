package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.util.CargoTransferUtil;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class CargoCommandProvider implements IModelCommandProvider {

	@Override
	public Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		return null;
	}
	
	@Override
	public Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (rootObject instanceof final LNGScenarioModel sm) {
			final CompoundCommand cc = new CompoundCommand();
			if (commandClass == AddCommand.class) {
				//Check if slot has been renamed.
				if (parameter.getEOwner() instanceof final Cargo cargo) {
					cargo.getSlots().forEach( s -> {
						if (!(s instanceof SpotSlot)) {
							final List<EObject> trs = CargoTransferUtil.getTransferRecordsForSlot(s, sm);
							if (trs != null) {
								trs.forEach(tr -> {
									if (tr != null) {
										cc.append(SetCommand.create(editingDomain, tr, TransfersPackage.eINSTANCE.getTransferRecord_Stale(), Boolean.TRUE));
									}});
							}
						}
					});
					
				}
			}
			if (!cc.isEmpty()) {
				return cc;
			}
		}
		return null;
	}
	
}
