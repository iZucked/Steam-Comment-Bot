/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
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
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * 19-10-2018
 * Not used at the moment due to user override.
 * @author SG & FM
 *
 */
public class ContractNominationCommandProvider extends AbstractModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalBeforeCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet, Class<? extends Command> commandClass,
			CommandParameter parameter, Command input) {
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public Command provideAdditionalAfterCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet, Class<? extends Command> commandClass,
			CommandParameter parameter, Command input) {
		
		if (LicenseFeatures.isPermitted("features:nominations")) {
			return null;
		}
		
		if (parameter.getOwner() instanceof Contract) {
			if (rootObject instanceof LNGScenarioModel) {

				final Contract contract = (Contract) parameter.getOwner();
				if (contract.getWindowNominationSizeUnits() == null) return null;
				if (contract.getWindowNominationSize() < 0) return null;
				if (contract.eContainer() == null) return null;
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
				final CargoModel cargoModel = scenarioModel.getCargoModel();
				if(cargoModel == null) {
					return null;
				}

				int wnSize = contract.getWindowNominationSize();
				TimePeriod wnUnits = contract.getWindowNominationSizeUnits();
				if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE) {
					if (parameter.value instanceof Integer) {
						wnSize = (int) parameter.value;
						if (wnSize == 0) {
							return null;
						}
					}
				}
				if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS) {
					if (parameter.value instanceof TimePeriod) {
						wnUnits = (TimePeriod) parameter.value;
					}
				}
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
		}
		return null;
	}
}
