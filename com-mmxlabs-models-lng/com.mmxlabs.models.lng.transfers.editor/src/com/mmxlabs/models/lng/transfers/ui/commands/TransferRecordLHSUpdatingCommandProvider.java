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
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class TransferRecordLHSUpdatingCommandProvider implements IModelCommandProvider {

	@Override
	public @Nullable Command provideAdditionalBeforeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, //
			final Set<EObject> editSet, final Class<? extends Command> commandClass, final CommandParameter parameter, final  Command input) {
		return null;
	}

	@Override
	public @Nullable Command provideAdditionalAfterCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, //
			final Set<EObject> editSet, final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		if (commandClass == SetCommand.class) {
			if (parameter.getEOwner() instanceof TransferRecord transferRecord) {
				if (parameter.getEStructuralFeature() == TransfersPackage.eINSTANCE.getTransferRecord_Lhs()) {
					if (parameter.getEValue() instanceof LoadSlot loadSlot) {
						int bufferDays = 0;
						if (transferRecord.getTransferAgreement() != null) {
							bufferDays = getBufferDays(transferRecord.getTransferAgreement());
						}
						if (loadSlot.getSchedulingTimeWindow().getStart() != null) {
							return createSetCommand(editingDomain, transferRecord, loadSlot.getSchedulingTimeWindow().getStart().toLocalDate().plusDays(bufferDays));
						} else {
							return createSetCommand(editingDomain, transferRecord, loadSlot.getWindowStart().plusDays(bufferDays));
						}
					}
				} else if (parameter.getEStructuralFeature() == TransfersPackage.eINSTANCE.getTransferRecord_TransferAgreement()) {
					if (parameter.getEValue() instanceof TransferAgreement transferAgreement) {
						int bufferDays = getBufferDays(transferAgreement);
						if (transferRecord.getLhs() instanceof LoadSlot loadSlot) {
							if (loadSlot.getSchedulingTimeWindow().getStart() != null) {
								return createSetCommand(editingDomain, transferRecord, loadSlot.getSchedulingTimeWindow().getStart().toLocalDate().plusDays(bufferDays));
								}
						} else {
							return createSetCommand(editingDomain, transferRecord, transferRecord.getPricingDate().plusDays(bufferDays));
						}
					}
				}
			}
		}
		return null;
	}
	
	private Command createSetCommand(final EditingDomain editingDomain, final Object owner, final Object value) {
		return SetCommand.create(editingDomain, owner, TransfersPackage.eINSTANCE.getTransferRecord_PricingDate(), value);
	}

	private int getBufferDays(final TransferAgreement transferAgreement) {
		int bufferDays = 0;
		if (transferAgreement.isSetBufferDays()) {
			bufferDays = transferAgreement.getBufferDays();
		}
		return bufferDays;
	}
}
