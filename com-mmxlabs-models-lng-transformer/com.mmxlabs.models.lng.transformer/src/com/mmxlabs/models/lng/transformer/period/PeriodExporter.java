/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

/***
 * 
 * @author Simon Goodall
 * 
 */
public class PeriodExporter {

	public Command updateOriginal(final EditingDomain editingDomain, final LNGScenarioModel originalScenario, final LNGScenarioModel periodScenario, final IScenarioEntityMapping mapping) {

		final CompoundCommand cmd = new CompoundCommand("Update original scenario");
		// Renumber sequence hints
		{
			for (Cargo oldCargo : originalScenario.getCargoModel().getCargoes()) {
				cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, oldCargo.getSequenceHint() - 100));
			}
			for (VesselEvent oldVesselEvent : originalScenario.getCargoModel().getVesselEvents()) {
				cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, oldVesselEvent.getSequenceHint() - 100));
			}
		}

		// First the easy part, update vessel events. (assigned vessel).
		{
			final CargoModel newCargoModel = periodScenario.getCargoModel();
			for (final VesselEvent newVesselEvent : newCargoModel.getVesselEvents()) {

				final VesselEvent oldVesselEvent = mapping.getOriginalFromCopy(newVesselEvent);
				if (oldVesselEvent == null) {
					continue;
				}
				// Update vessel assignment bits.

				final VesselAssignmentType vesselAssignmentType = newVesselEvent.getVesselAssignmentType();
				cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE,
						mapping.getOriginalFromCopy(newVesselEvent.getVesselAssignmentType())));
				cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, newVesselEvent.getSequenceHint()));

				if (vesselAssignmentType instanceof CharterInMarket) {
					cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, newVesselEvent.getSpotIndex()));
				} else {
					cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
				}
			}

		}

		// Second the harder part, reconcile cargo model changes
		{

			final CargoModel oldCargoModel = originalScenario.getCargoModel();
			final CargoModel newCargoModel = periodScenario.getCargoModel();

			// Grab existing slot ids
			final Set<String> usedIDStrings = new HashSet<String>();

			{
				for (final LoadSlot loadSlot : oldCargoModel.getLoadSlots()) {
					usedIDStrings.add(loadSlot.getName());
				}
				for (final DischargeSlot dischargeSlot : oldCargoModel.getDischargeSlots()) {
					usedIDStrings.add(dischargeSlot.getName());
				}
			}

			// First pass, update wiring. create list of paired slots and work out which slots are no longer paired
			final Set<Cargo> seenCargoes = new HashSet<>();
			final Set<Slot> seenSlots = new HashSet<>();

			for (final Cargo newCargo : newCargoModel.getCargoes()) {
				Cargo oldCargo = mapping.getOriginalFromCopy(newCargo);
				if (oldCargo == null) {
					// Create new cargo.
					oldCargo = ObjectCopier.copyCargo(newCargo, mapping);
					cmd.append(AddCommand.create(editingDomain, oldCargoModel, CargoPackage.Literals.CARGO_MODEL__CARGOES, oldCargo));
				}
				final List<Slot> newCargoSlots = new ArrayList<>(newCargo.getSlots().size());
				for (final Slot newSlot : newCargo.getSortedSlots()) {
					Slot oldSlot = mapping.getOriginalFromCopy(newSlot);
					if (oldSlot == null) {
						oldSlot = ObjectCopier.copySlot(newSlot, mapping);
						if (oldSlot instanceof LoadSlot) {
							cmd.append(AddCommand.create(editingDomain, oldCargoModel, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, oldSlot));
						} else {
							assert oldSlot instanceof DischargeSlot;
							cmd.append(AddCommand.create(editingDomain, oldCargoModel, CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS, oldSlot));
						}

						if (oldSlot instanceof SpotSlot) {
							if (usedIDStrings.contains(oldSlot.getName())) {

								// Expect string in form MARKET_NAME-YYYY-MM-n. Strip off the "n" part.
								final Pattern pattern = Pattern.compile("(.*-[0-9][0-9][0-9][0-9]-[0-1][0-9]-)[0-9]*");
								final Matcher matcher = pattern.matcher(oldSlot.getName());

								final String idPrefix;
								if (matcher.find()) {
									idPrefix = matcher.group(1);
								} else {
									// Fallback and use whole name as a prefix
									idPrefix = oldSlot.getName();
								}

								// Avoid ID clash
								int offset = 0;
								String id = idPrefix + (offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (++offset);
								}
								oldSlot.setName(id);
							}
							usedIDStrings.add(oldSlot.getName());
						}
					}
					if (newSlot instanceof SpotSlot) {
						// Clone spot details
						if (newSlot instanceof LoadSlot && ((LoadSlot) newSlot).isDESPurchase()) {
							cmd.append(SetCommand.create(editingDomain, oldSlot, CargoPackage.Literals.SLOT__PORT, mapping.getOriginalFromCopy(newSlot.getPort())));
						} else if (newSlot instanceof DischargeSlot && ((DischargeSlot) newSlot).isFOBSale()) {
							cmd.append(SetCommand.create(editingDomain, oldSlot, CargoPackage.Literals.SLOT__PORT, mapping.getOriginalFromCopy(newSlot.getPort())));
						}
						cmd.append(SetCommand.create(editingDomain, oldSlot, CargoPackage.Literals.SLOT__WINDOW_START, oldSlot.getWindowStart()));
						cmd.append(SetCommand.create(editingDomain, oldSlot, CargoPackage.Literals.SLOT__WINDOW_START_TIME, 0));
					}
					newCargoSlots.add(oldSlot);
				}
				seenCargoes.add(oldCargo);

				// Bind slots to cargo
				for (final Slot slot : newCargoSlots) {
					seenSlots.add(slot);
					if (slot.getCargo() != oldCargo) {
						cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__CARGO, oldCargo));
					}
				}

				// Update vessel assignment bits.

				final VesselAssignmentType vesselAssignmentType = newCargo.getVesselAssignmentType();
				cmd.append(
						SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, mapping.getOriginalFromCopy(newCargo.getVesselAssignmentType())));
				cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, newCargo.getSequenceHint()));

				if (vesselAssignmentType instanceof CharterInMarket) {

					final int spotIndex = mapping.getSpotCharterInMappingFromPeriod((CharterInMarket) vesselAssignmentType, newCargo.getSpotIndex());
					cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, spotIndex));
				} else {
					cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
				}

			}

			final List<Cargo> originalCargoes = new LinkedList<>();
			final List<LoadSlot> originalLoadSlots = new LinkedList<>();
			final List<DischargeSlot> originalDischargeSlots = new LinkedList<>();
			for (final EObject eObj : mapping.getUsedOriginalObjects()) {
				if (eObj instanceof Cargo) {
					originalCargoes.add((Cargo) eObj);
				} else if (eObj instanceof LoadSlot) {
					originalLoadSlots.add((LoadSlot) eObj);
				} else if (eObj instanceof DischargeSlot) {
					originalDischargeSlots.add((DischargeSlot) eObj);
				}
			}

			// Finally remove newly unpaired slot to cargo references and remove if spot
			for (final LoadSlot loadSlot : originalLoadSlots) {
				if (!seenSlots.contains(loadSlot)) {
					cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.SLOT__CARGO, SetCommand.UNSET_VALUE));

					if (loadSlot instanceof SpotSlot) {
						// Spot slot was in original case, but not longer. Remove it.
						cmd.append(DeleteCommand.create(editingDomain, loadSlot));
					}
				}
			}
			for (final DischargeSlot dischargeSlot : originalDischargeSlots) {
				if (!seenSlots.contains(dischargeSlot)) {
					cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__CARGO, SetCommand.UNSET_VALUE));

					if (dischargeSlot instanceof SpotSlot) {
						// Spot slot was in original case, but not longer. Remove it.
						cmd.append(DeleteCommand.create(editingDomain, dischargeSlot));
					}
				}
			}
			// NOTE: Keep cargo deletion after slot unset SLOT_CARGO feature otherwise is caused lots of issues with commands.
			// Loop through new loads & discharges looking for unused slots which were part of a cargo originally and break up the original cargo.
			for (final Cargo cargo : originalCargoes) {
				if (!seenCargoes.contains(cargo)) {
					// Cargo was in original case, but not longer. Remove it.
					cmd.append(DeleteCommand.create(editingDomain, cargo));
				}
			}

		}

		// Post process
		{
			// Make sure spot slot Ids are unique. ...
		}

		// Copy params model
//		cmd.append(SetCommand.create(editingDomain, originalScenario, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_Parameters(), EcoreUtil.copy(periodScenario.getParameters())));
//		cmd.append(SetCommand.create(editingDomain, originalScenario, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_Parameters(), EcoreUtil.copy(periodScenario.getParameters())));

		if (cmd.isEmpty()) {
			return IdentityCommand.INSTANCE;
		}

		return cmd;
	}
}
