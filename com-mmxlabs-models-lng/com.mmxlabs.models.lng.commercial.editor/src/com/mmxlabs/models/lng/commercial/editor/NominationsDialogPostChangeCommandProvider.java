/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.editor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.util.NominationUtils;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogPostChangeCommandProvider;

/**
 * 19-10-2018
 * Not used at the moment due to user override.
 * @author SG & FM
 *
 */
public class NominationsDialogPostChangeCommandProvider implements IDialogPostChangeCommandProvider {

	@SuppressWarnings("unused")
	@Override
	public Command provideExtraCommand(final EditingDomain editingDomain, final Command baseCommand, final MMXRootObject modelRoot, final Collection<EObject> roots) {

		if (!LicenseFeatures.isPermitted("features:nominations")) {
			return baseCommand;
		}
		
		// Do we have any target elements? If not, do not bother to wrap the command.
		boolean hasContract = false;
		for (final EObject root : roots) {
			if (root instanceof Contract) {
				hasContract = true;
			}
		}

		if (!hasContract) {
			return baseCommand;
		}

		// Create a compound command to listen for changes to the contracts as the basecommand executes.
		// If the target features are touched, then create a new command to change the linked objects.
		final CompoundCommand cc = new CompoundCommand() {

			private final Collection<EObject> contracts = new LinkedHashSet<>();
			private final EContentAdapter adapter = new EContentAdapter() {
				@Override
				public void notifyChanged(final Notification notification) {
					super.notifyChanged(notification);
					if (notification.isTouch()) {
						return;
					}
					if (notification.getNotifier() instanceof Contract) {
						if (notification.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE
								|| notification.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS) {
							contracts.add((EObject) notification.getNotifier());
						}
					}
				}
			};

			@Override
			public void execute() {
				// Ensure collection is empty
				contracts.clear();
				// Attach  listener before command execution
				for (final EObject r : roots) {
					r.eAdapters().add(adapter);
				}

				super.execute();
				// 	Remove listener after command execution.
				for (final EObject r : roots) {
					r.eAdapters().remove(adapter);
				}
				// For each applicable contract, create the related data model change command
				for (final EObject contract : contracts) {
					final Command c = createCommand(editingDomain, contract, modelRoot);
					if (c != null) {
						appendAndExecute(c);
					}
				}
				// Clear state
				contracts.clear();
			}
		};

		cc.append(baseCommand);

		return cc;
	}

	private Command createCommand(final EditingDomain editingDomain, final EObject contract, final MMXRootObject rootObject) {

		if ((contract == null) || !(contract instanceof Contract)) {
			return null;
		}
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
			final int wnSize = ((Contract) contract).getWindowNominationSize();
			if (wnSize == 0) {
				return null;
			}
			final TimePeriod wnUnits = ((Contract) contract).getWindowNominationSizeUnits();
			CompoundCommand command = new CompoundCommand();
			if(contract instanceof PurchaseContract){
				
				for (final LoadSlot slot : cargoModel.getLoadSlots()) {
					if(contract.equals(slot.getContract())) {
						if (!slot.isWindowNominationIsDone()) {
							LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
							command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE, wnDate));
						}
					}
				}
			} else if(contract instanceof SalesContract){
				
				for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
					if(contract.equals(slot.getContract())) {
						if (!slot.isWindowNominationIsDone()) {
							LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
							command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE, wnDate));
						}
					}
				}
			}
			if(!command.isEmpty()) {
				return command;
			}
		}

		return null;
	}

}
