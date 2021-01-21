/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

/**
 * 19-10-2018
 * Not used at the moment due to user override.
 * @author SG & FM
 *
 */
/*
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
				final CompoundCommand command = new CompoundCommand();
				
				if(parameter.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY) {
					command.append(makeWindowNominationCommand(editingDomain, rootObject, parameter));
				}
				if(parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY) {
					command.append(makeVolumeNominationCommand(editingDomain, rootObject, parameter));
				}
				if(parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY) {
					command.append(makeVesselNominationCommand(editingDomain, rootObject, parameter));
				}
				if(parameter.getFeature() == CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE_UNITS
						|| parameter.getFeature() == CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_COUNTERPARTY) {
					command.append(makePortNominationCommand(editingDomain, rootObject, parameter));
				}
				
				if(!command.isEmpty()) {
					return command;
				}
			}
		}
		return null;
	}

	private CompoundCommand makeWindowNominationCommand(EditingDomain editingDomain, MMXRootObject rootObject, CommandParameter parameter) {
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
		boolean counterparty = contract.isWindowNominationCounterparty();
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
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY) {
			if (parameter.value instanceof Boolean) {
				counterparty = (Boolean) parameter.value;
			}
		}
		CompoundCommand command = new CompoundCommand();
		if(contract instanceof PurchaseContract){
			
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isWindowNominationIsDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		} else if(contract instanceof SalesContract){
			
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isWindowNominationIsDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		}
		return command;
	}
	
	private CompoundCommand makeVolumeNominationCommand(EditingDomain editingDomain, MMXRootObject rootObject, CommandParameter parameter) {
		final Contract contract = (Contract) parameter.getOwner();
		if (contract.getVolumeNominationSizeUnits() == null) return null;
		if (contract.getVolumeNominationSize() < 0) return null;
		if (contract.eContainer() == null) return null;
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		if(cargoModel == null) {
			return null;
		}

		int wnSize = contract.getVolumeNominationSize();
		TimePeriod wnUnits = contract.getVolumeNominationSizeUnits();
		boolean counterparty = contract.isVolumeNominationCounterparty();
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE) {
			if (parameter.value instanceof Integer) {
				wnSize = (int) parameter.value;
				if (wnSize == 0) {
					return null;
				}
			}
		}
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS) {
			if (parameter.value instanceof TimePeriod) {
				wnUnits = (TimePeriod) parameter.value;
			}
		}
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY) {
			if (parameter.value instanceof Boolean) {
				counterparty = (Boolean) parameter.value;
			}
		}
		CompoundCommand command = new CompoundCommand();
		if(contract instanceof PurchaseContract){
			
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isVolumeNominationDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VOLUME_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		} else if(contract instanceof SalesContract){
			
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isVolumeNominationDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VOLUME_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		}
		return command;
	}
	
	private CompoundCommand makeVesselNominationCommand(EditingDomain editingDomain, MMXRootObject rootObject, CommandParameter parameter) {
		final Contract contract = (Contract) parameter.getOwner();
		if (contract.getVesselNominationSizeUnits() == null) return null;
		if (contract.getVesselNominationSize() < 0) return null;
		if (contract.eContainer() == null) return null;
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		if(cargoModel == null) {
			return null;
		}

		int wnSize = contract.getVesselNominationSize();
		TimePeriod wnUnits = contract.getVesselNominationSizeUnits();
		boolean counterparty = contract.isVesselNominationCounterparty();
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE) {
			if (parameter.value instanceof Integer) {
				wnSize = (int) parameter.value;
				if (wnSize == 0) {
					return null;
				}
			}
		}
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS) {
			if (parameter.value instanceof TimePeriod) {
				wnUnits = (TimePeriod) parameter.value;
			}
		}
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY) {
			if (parameter.value instanceof Boolean) {
				counterparty = (Boolean) parameter.value;
			}
		}
		CompoundCommand command = new CompoundCommand();
		if(contract instanceof PurchaseContract){
			
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isVesselNominationDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VESSEL_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		} else if(contract instanceof SalesContract){
			
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isVesselNominationDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VESSEL_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		}
		return command;
	}
	
	private CompoundCommand makePortNominationCommand(EditingDomain editingDomain, MMXRootObject rootObject, CommandParameter parameter) {
		final Contract contract = (Contract) parameter.getOwner();
		if (contract.getPortNominationSizeUnits() == null) return null;
		if (contract.getPortNominationSize() < 0) return null;
		if (contract.eContainer() == null) return null;
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		if(cargoModel == null) {
			return null;
		}

		int wnSize = contract.getPortNominationSize();
		TimePeriod wnUnits = contract.getPortNominationSizeUnits();
		boolean counterparty = contract.isPortNominationCounterparty();
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE) {
			if (parameter.value instanceof Integer) {
				wnSize = (int) parameter.value;
				if (wnSize == 0) {
					return null;
				}
			}
		}
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE_UNITS) {
			if (parameter.value instanceof TimePeriod) {
				wnUnits = (TimePeriod) parameter.value;
			}
		}
		if (parameter.getFeature() == CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_COUNTERPARTY) {
			if (parameter.value instanceof Boolean) {
				counterparty = (Boolean) parameter.value;
			}
		}
		CompoundCommand command = new CompoundCommand();
		if(contract instanceof PurchaseContract){
			
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isPortNominationDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__PORT_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__PORT_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		} else if(contract instanceof SalesContract){
			
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				if(contract.equals(slot.getContract())) {
					if (!slot.isPortNominationDone()) {
						LocalDate wnDate = NominationUtils.computeNewDate(slot.getWindowStart(),wnUnits, -wnSize);
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__PORT_NOMINATION_DATE, wnDate));
						command.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__PORT_NOMINATION_COUNTERPARTY, counterparty));
					}
				}
			}
		}
		return command;
	}
}
*/