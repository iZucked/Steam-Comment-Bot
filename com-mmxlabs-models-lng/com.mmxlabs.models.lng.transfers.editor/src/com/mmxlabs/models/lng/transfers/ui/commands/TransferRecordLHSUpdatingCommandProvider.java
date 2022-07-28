package com.mmxlabs.models.lng.transfers.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class TransferRecordLHSUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public @Nullable Command provideAdditionalBeforeCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet,
			Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		return null;
	}

	@Override
	public @Nullable Command provideAdditionalAfterCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet,
			Class<? extends Command> commandClass, CommandParameter parameter, Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getEOwner() instanceof TransferRecord transferRecord) {
				if (parameter.getEStructuralFeature() == TransfersPackage.eINSTANCE.getTransferRecord_Lhs()) {
					if (parameter.getEValue() instanceof LoadSlot loadSlot) {
						if (loadSlot.getSchedulingTimeWindow().getStart() != null) {
						return SetCommand.create(editingDomain, transferRecord, TransfersPackage.eINSTANCE.getTransferRecord_PricingDate(), //
								loadSlot.getSchedulingTimeWindow().getStart().toLocalDate());
						}
					}
				}
			}
		}
		return null;
	}

}
