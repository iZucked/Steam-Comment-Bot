/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.anonymisation;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class AnonymisationHandler {

	private static final String VesselID = "Vessel";
	private static final String VesselShortID = "VSL";
	private static final String BuyID = "Purchase";
	private static final String SellID = "Sale";
	private static final String BuyContractID = "Purchase contract";
	private static final String SellContractID = "Sales contract";

	public CompoundCommand createAnonymisationCommand(final @NonNull LNGScenarioModel scenarioModel, final EditingDomain ed, final Set<String> usedIDStrings, //
			final List<AnonymisationRecord> records, final boolean anonymise, final @NonNull File anonyMap, final boolean stripNotes) {
		final CompoundCommand cmd = new CompoundCommand("Toggle anonymisation");

		cmd.append(SetCommand.create(ed, scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_Anonymised(), anonymise));
		if (anonymise) {
			renameVessels(scenarioModel, ed, records, cmd, usedIDStrings, AnonymisationUtils::rename, stripNotes);
			renameSlots(scenarioModel, ed, records, cmd, usedIDStrings, AnonymisationUtils::rename, stripNotes);
			renameContracts(scenarioModel, ed, records, cmd, usedIDStrings, AnonymisationUtils::rename, stripNotes);
			AnonymisationMapIO.write(records, anonyMap);
		} else {
			if (records.isEmpty()) {
				records.addAll(AnonymisationMapIO.read(anonyMap));
			}
			renameVessels(scenarioModel, ed, records, cmd, usedIDStrings, AnonymisationUtils::renameToOriginal, false);
			renameSlots(scenarioModel, ed, records, cmd, usedIDStrings, AnonymisationUtils::renameToOriginal, false);
			renameContracts(scenarioModel, ed, records, cmd, usedIDStrings, AnonymisationUtils::renameToOriginal, false);
		}
		return cmd;
	}

	private void renameVessels(final @NonNull LNGScenarioModel currentModel, final EditingDomain editingDomain, final List<AnonymisationRecord> records, //
			final CompoundCommand renameCommand, final Set<String> usedIDStrings, final Function<AnonymisationEntry, Command> renameFunction, boolean stripNotes) {
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(currentModel);
		for (final Vessel v : fleetModel.getVessels()) {
			renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, v, v.getName(), //
					MMXCorePackage.Literals.NAMED_OBJECT__NAME, VesselID, AnonymisationRecordType.VesselID, usedIDStrings)));
			
			renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, v, v.getShortName(), //
					FleetPackage.Literals.VESSEL__SHORT_NAME, VesselShortID, AnonymisationRecordType.VesselShortID, usedIDStrings)));
			
			if (stripNotes) {
				renameCommand.append(SetCommand.create(editingDomain, v, FleetPackage.Literals.VESSEL__NOTES, SetCommand.UNSET_VALUE));
			}
		}
	}

	private void renameSlots(final @NonNull LNGScenarioModel currentModel, final EditingDomain editingDomain, final List<AnonymisationRecord> records, //
			final CompoundCommand renameCommand, final Set<String> usedIDStrings, final Function<AnonymisationEntry, Command> renameFunction, final boolean stripNotes) {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(currentModel);
		final Set<Slot<?>> usedSlots = new HashSet();
		for (final Cargo c : cargoModel.getCargoes()) {
			for (final Slot<?> s : c.getSlots()) {
				if (s instanceof LoadSlot) {
					renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, s, s.getName(), //
							MMXCorePackage.Literals.NAMED_OBJECT__NAME, BuyID, AnonymisationRecordType.BuyID, usedIDStrings)));
					usedSlots.add(s);
				} else {
					renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, s, s.getName(), //
							MMXCorePackage.Literals.NAMED_OBJECT__NAME, SellID, AnonymisationRecordType.SellID, usedIDStrings)));
					usedSlots.add(s);
				}

			}
		}
		for (final Slot<?> s : cargoModel.getLoadSlots()) {
			if (stripNotes) {
				renameCommand.append(SetCommand.create(editingDomain, s, CargoPackage.Literals.SLOT__NOTES, SetCommand.UNSET_VALUE));
			}
			if (!(usedSlots.contains(s))) {
				renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, s, s.getName(), //
						MMXCorePackage.Literals.NAMED_OBJECT__NAME, BuyID, AnonymisationRecordType.BuyID, usedIDStrings)));
				usedSlots.add(s);
			}
		}
		for (final Slot<?> s : cargoModel.getDischargeSlots()) {
			if (stripNotes) {
				renameCommand.append(SetCommand.create(editingDomain, s, CargoPackage.Literals.SLOT__NOTES, SetCommand.UNSET_VALUE));
			}
			if (!(usedSlots.contains(s))) {
				renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, s, s.getName(), //
						MMXCorePackage.Literals.NAMED_OBJECT__NAME, SellID, AnonymisationRecordType.SellID, usedIDStrings)));
				usedSlots.add(s);
			}
		}
	}

	private void renameContracts(final @NonNull LNGScenarioModel currentModel, final EditingDomain editingDomain, final List<AnonymisationRecord> records, //
			final CompoundCommand renameCommand, final Set<String> usedIDStrings, final Function<AnonymisationEntry, Command> renameFunction, final boolean stripNotes) {
		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(currentModel);
		for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
			renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, c, c.getName(), //
					MMXCorePackage.Literals.NAMED_OBJECT__NAME, BuyContractID, AnonymisationRecordType.BuyContractID, usedIDStrings)));

			if (stripNotes) {
				renameCommand.append(SetCommand.create(editingDomain, c, CommercialPackage.Literals.CONTRACT__NOTES, SetCommand.UNSET_VALUE));
			}
		}
		for (final SalesContract c : commercialModel.getSalesContracts()) {
			renameCommand.append(renameFunction.apply(new AnonymisationEntry(editingDomain, records, c, c.getName(), //
					MMXCorePackage.Literals.NAMED_OBJECT__NAME, SellContractID, AnonymisationRecordType.SellContractID, usedIDStrings)));
			if (stripNotes) {
				renameCommand.append(SetCommand.create(editingDomain, c, CommercialPackage.Literals.CONTRACT__NOTES, SetCommand.UNSET_VALUE));
			}
		}
	}
}
