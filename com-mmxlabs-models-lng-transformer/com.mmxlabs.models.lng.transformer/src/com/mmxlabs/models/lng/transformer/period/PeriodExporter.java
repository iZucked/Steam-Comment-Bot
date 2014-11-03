package com.mmxlabs.models.lng.transformer.period;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.timezone.TimeZoneHelper;
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

/***
 * 
 * @author Simon Goodall
 * 
 */
public class PeriodExporter {

	public Command updateOriginal(final EditingDomain editingDomain, final LNGScenarioModel originalScenario, final LNGScenarioModel periodScenario, final IScenarioEntityMapping mapping) {

		final CompoundCommand cmd = new CompoundCommand("Update original scenario");

		// First the easy part, update vessel events. (assigned vessel).
		{
			final CargoModel newCargoModel = periodScenario.getPortfolioModel().getCargoModel();
			for (final VesselEvent newVesselEvent : newCargoModel.getVesselEvents()) {

				final VesselEvent oldVesselEvent = mapping.getOriginalFromCopy(newVesselEvent);
				if (oldVesselEvent == null) {
					continue;
				}
				// Update vessel assignment bits.
				cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, mapping.getOriginalFromCopy(newVesselEvent.getAssignment())));
				if (newVesselEvent.isSetSpotIndex()) {
					cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, newVesselEvent.getSpotIndex()));
				} else {
					cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
				}
				cmd.append(SetCommand.create(editingDomain, oldVesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, newVesselEvent.getSequenceHint()));
			}

		}

		// Second the harder part, reconcile cargo model changes
		{
			final CargoModel oldCargoModel = originalScenario.getPortfolioModel().getCargoModel();
			final CargoModel newCargoModel = periodScenario.getPortfolioModel().getCargoModel();

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
					}
					if (newSlot instanceof SpotSlot) {
						// Clone spot details
						if (newSlot instanceof LoadSlot && ((LoadSlot) newSlot).isDESPurchase()) {
							cmd.append(SetCommand.create(editingDomain, oldSlot, CargoPackage.Literals.SLOT__PORT, mapping.getOriginalFromCopy(newSlot.getPort())));
						} else if (newSlot instanceof DischargeSlot && ((DischargeSlot) newSlot).isFOBSale()) {
							cmd.append(SetCommand.create(editingDomain, oldSlot, CargoPackage.Literals.SLOT__PORT, mapping.getOriginalFromCopy(newSlot.getPort())));
						}
						cmd.append(SetCommand.create(editingDomain, oldSlot, CargoPackage.Literals.SLOT__WINDOW_START,
								TimeZoneHelper.createTimeZoneShiftedDate(oldSlot.getWindowStart(), oldSlot.getPort().getTimeZone(), newSlot.getPort().getTimeZone())));
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
				cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, mapping.getOriginalFromCopy(newCargo.getAssignment())));
				if (newCargo.isSetSpotIndex()) {
					cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, newCargo.getSpotIndex()));
				} else {
					cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
				}
				cmd.append(SetCommand.create(editingDomain, oldCargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, newCargo.getSequenceHint()));
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
		cmd.append(SetCommand.create(editingDomain, originalScenario.getPortfolioModel(), LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_Parameters(),
				EcoreUtil.copy(periodScenario.getPortfolioModel().getParameters())));

		if (cmd.isEmpty()) {
			return IdentityCommand.INSTANCE;
		}

		return cmd;
	}

}
