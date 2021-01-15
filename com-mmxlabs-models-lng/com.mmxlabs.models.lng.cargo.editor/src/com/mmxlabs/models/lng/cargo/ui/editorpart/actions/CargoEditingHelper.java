/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.Activator;

public class CargoEditingHelper {

	private final CargoEditingCommands cec;

	private final LNGScenarioModel scenarioModel;

	private final EditingDomain editingDomain;
	private static final boolean VERIFY_CHANGES = true;

	public CargoEditingHelper(final @NonNull EditingDomain editingDomain, final @NonNull LNGScenarioModel lngScenarioModel) {
		this.editingDomain = editingDomain;
		this.scenarioModel = lngScenarioModel;
		this.cec = new CargoEditingCommands(editingDomain, lngScenarioModel, ScenarioModelUtil.getCargoModel(lngScenarioModel), ScenarioModelUtil.getCommercialModel(scenarioModel),
				Activator.getDefault().getModelFactoryRegistry());
	}

	public void assignNominatedVessel(@NonNull final String description, @NonNull final Slot slot, @Nullable final Vessel vessel) {
		final Object value = vessel == null ? SetCommand.UNSET_VALUE : vessel;

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			assert loadSlot.isDESPurchase();
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			assert dischargeSlot.isFOBSale();
		}

		final CompoundCommand cc = new CompoundCommand(description);
		final Command cmd = SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL, value);

		cc.append(cmd);
		assert cc.canExecute();

		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			assert slot.getNominatedVessel() == vessel;
		}

		verifyModel();

	}

	public void assignCargoToSpotCharterIn(@NonNull final String description, @NonNull final Cargo cargo, @NonNull final CharterInMarket charterInMarket, final int spotIndex) {

		// Validate shipped cargo
		for (final Slot<?> slot : cargo.getSlots()) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				assert !loadSlot.isDESPurchase();
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				assert !dischargeSlot.isFOBSale();
			}
		}

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, charterInMarket));
		cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, spotIndex));

		assert cc.canExecute();

		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			assert cargo.getVesselAssignmentType() == charterInMarket;
			assert cargo.getSpotIndex() == spotIndex;
		}

		verifyModel();

	}

	public void assignCargoToVesselAassignment(@NonNull final String description, @NonNull final Cargo cargo, @Nullable final VesselAssignmentType vesselAssignmentType, final int spotIndex) {
		if (vesselAssignmentType == null) {
			unassignCargoAssignment(description, cargo);
		} else if (vesselAssignmentType instanceof CharterInMarket) {
			assignCargoToSpotCharterIn(description, cargo, (CharterInMarket) vesselAssignmentType, spotIndex);
		} else if (vesselAssignmentType instanceof VesselAvailability) {
			assignCargoToVesselAvailability(description, cargo, (VesselAvailability) vesselAssignmentType);
		} else {
			assert false;
		}

		if (VERIFY_CHANGES) {
			assert cargo.getVesselAssignmentType() == vesselAssignmentType;
			assert cargo.getSpotIndex() == spotIndex;
		}
	}

	public void assignCargoToVesselAvailability(@NonNull final String description, @NonNull final Cargo cargo, @NonNull final VesselAvailability vesselAvailability) {

		// Validate shipped cargo
		for (final Slot<?> slot : cargo.getSlots()) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				assert !loadSlot.isDESPurchase();
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				assert !dischargeSlot.isFOBSale();
			}
		}

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vesselAvailability));
		cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));

		assert cc.canExecute();

		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			assert cargo.getVesselAssignmentType() == vesselAvailability;
			assert cargo.getSpotIndex() == -1;
		}

		verifyModel();
	}

	public void unassignCargoAssignment(@NonNull final String description, @NonNull final Cargo cargo) {

		// Validate shipped cargo
		for (final Slot<?> slot : cargo.getSlots()) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				assert !loadSlot.isDESPurchase();
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				assert !dischargeSlot.isFOBSale();
			}
		}

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, SetCommand.UNSET_VALUE));
		cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));

		assert cc.canExecute();

		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			assert cargo.getVesselAssignmentType() == null;
			assert cargo.getSpotIndex() == -1;
		}
		verifyModel();
	}

	public void assignVesselEventToVesselAvailability(@NonNull final String description, @NonNull final VesselEvent vesselEvent, @NonNull final VesselAvailability vesselAvailability) {

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vesselAvailability));
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));

		assert cc.canExecute();

		editingDomain.getCommandStack().execute(cc);
		if (VERIFY_CHANGES) {
			assert vesselEvent.getVesselAssignmentType() == vesselAvailability;
			assert vesselEvent.getSpotIndex() == -1;
		}
		verifyModel();
	}

	public void unassignVesselEventAssignment(@NonNull final String description, @NonNull final VesselEvent vesselEvent) {

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, SetCommand.UNSET_VALUE));
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));

		assert cc.canExecute();

		editingDomain.getCommandStack().execute(cc);
		if (VERIFY_CHANGES) {
			assert vesselEvent.getVesselAssignmentType() == null;
			assert vesselEvent.getSpotIndex() == -1;
		}
		verifyModel();
	}

	public void lockCargoAssignment(@NonNull final String description, @NonNull final Cargo cargo) {
		lockCargoesAssignment(description, Collections.singleton(cargo));
	}

	public void lockCargoesAssignment(@NonNull final String description, @NonNull final Collection<@NonNull Cargo> cargoes) {

		final CompoundCommand cc = new CompoundCommand(description);
		for (final Cargo cargo : cargoes) {
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.TRUE));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.FALSE));
		}
		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			for (final Cargo cargo : cargoes) {
				assert !cargo.isAllowRewiring();
				assert cargo.isLocked();
			}
		}

		verifyModel();
	}

	public void unlockCargoAssignment(@NonNull final String description, @NonNull final Cargo cargo) {
		unlockCargoesAssignment(description, Collections.singleton(cargo));
	}

	public void unlockCargoesAssignment(@NonNull final String description, @NonNull final Collection<@NonNull Cargo> cargoes) {

		final CompoundCommand cc = new CompoundCommand(description);

		for (final Cargo cargo : cargoes) {
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.TRUE));
		}
		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			for (final Cargo cargo : cargoes) {
				assert cargo.isAllowRewiring();
				assert !cargo.isLocked();
			}
		}

		verifyModel();
	}

	public void lockVesselEventAssignment(@NonNull final String description, @NonNull final VesselEvent vesselEvent) {

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.TRUE));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			assert vesselEvent.isLocked();
		}

		verifyModel();
	}

	public void unlockVesselEventAssignment(@NonNull final String description, @NonNull final VesselEvent vesselEvent) {

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);
		if (VERIFY_CHANGES) {
			assert !vesselEvent.isLocked();
		}
		verifyModel();
	}

	public void pairSlotsIntoCargo(@NonNull final String description, @NonNull final LoadSlot loadSlot, @NonNull final DischargeSlot dischargeSlot) {

		final Cargo loadCargo = loadSlot.getCargo();
		final Cargo dischargeCargo = dischargeSlot.getCargo();

		final List<Command> setCommands = new LinkedList<>();
		final List<EObject> deleteObjects = new LinkedList<>();

		cec.runWiringUpdate(setCommands, deleteObjects, loadSlot, dischargeSlot);

		final CompoundCommand currentWiringCommand = new CompoundCommand(description);
		// Process set before delete
		for (final Command c : setCommands) {
			currentWiringCommand.append(c);
		}
		if (!deleteObjects.isEmpty()) {
			currentWiringCommand.append(DeleteCommand.create(editingDomain, deleteObjects));
		}

		assert currentWiringCommand.canExecute();

		editingDomain.getCommandStack().execute(currentWiringCommand);

		if (VERIFY_CHANGES) {

			if (loadCargo != null) {
				// Re-use existing cargo
				assert loadSlot.getCargo() == loadCargo;
			}
			if (dischargeCargo != null) {
				// No longer used
				assert dischargeCargo.getSlots().isEmpty();
				assert dischargeCargo.eContainer() == null;
			}
			// Matching cargoes
			assert loadSlot.getCargo() == dischargeSlot.getCargo();
			final Cargo newCargo = loadSlot.getCargo();
			assert newCargo.getSlots().size() == 2;
			assert newCargo.eContainer() != null;

			final boolean nonShipped = loadSlot.isDESPurchase() || dischargeSlot.isFOBSale();
			if (nonShipped) {
				assert newCargo.getVesselAssignmentType() == null;
			}
		}

		verifyModel();
	}

	public void convertToFOBPurchase(@NonNull final String description, @NonNull final LoadSlot loadSlot) {
		assert loadSlot.isDESPurchase();
		assert loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE;

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE, Boolean.FALSE));
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL, SetCommand.UNSET_VALUE));
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION, 0));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);
		if (VERIFY_CHANGES) {
			assert !loadSlot.isDESPurchase();
			assert loadSlot.getNominatedVessel() == null;
			assert loadSlot.getSlotOrDelegateShippingDaysRestriction() == 0;
		}

		verifyModel();
	}

	public void convertToDESPurchase(@NonNull final String description, @NonNull final LoadSlot loadSlot) {
		assert !loadSlot.isDESPurchase();

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE, Boolean.TRUE));
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE_DEAL_TYPE, DESPurchaseDealType.DIVERT_FROM_SOURCE));
		final Cargo cargo = loadSlot.getCargo();
		if (cargo != null) {
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, SetCommand.UNSET_VALUE));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
		}
		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			assert loadSlot.isDESPurchase();
			assert loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE;
			if (cargo != null) {
				assert cargo.getVesselAssignmentType() == null;
				assert cargo.getSpotIndex() == -1;
				assert !cargo.isLocked();
			}
		}

		verifyModel();
	}

	public void convertToDESSale(@NonNull final String description, @NonNull final DischargeSlot dischargeSlot) {
		assert dischargeSlot.isFOBSale();
		assert dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST;

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE, Boolean.FALSE));
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL, SetCommand.UNSET_VALUE));
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION, 0));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);
		if (VERIFY_CHANGES) {
			assert !dischargeSlot.isFOBSale();
			assert dischargeSlot.getNominatedVessel() == null;
			assert dischargeSlot.getSlotOrDelegateShippingDaysRestriction() == 0;
		}

		verifyModel();
	}

	public void convertToFOBSale(@NonNull final String description, @NonNull final DischargeSlot dischargeSlot) {
		assert !dischargeSlot.isFOBSale();

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE, Boolean.TRUE));
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE, FOBSaleDealType.DIVERT_TO_DEST));
		final Cargo cargo = dischargeSlot.getCargo();
		if (cargo != null) {
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, SetCommand.UNSET_VALUE));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
		}
		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (VERIFY_CHANGES) {
			assert dischargeSlot.isFOBSale();
			assert dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST;
			if (cargo != null) {
				assert cargo.getVesselAssignmentType() == null;
				assert cargo.getSpotIndex() == -1;
				assert !cargo.isLocked();
			}
		}

		verifyModel();
	}

	public void verifyModel() {
		cec.verifyCargoModel(ScenarioModelUtil.getCargoModel(scenarioModel));
	}

	public <T extends SpotSlot & Slot<?>> void setSpotSlotWindow(@NonNull final T slot, @NonNull final LocalDate cal, Collection<Command> setComands) {
		// Set back to start of month
		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_START, cal.withDayOfMonth(1)));
		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_START_TIME, 0));

		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE, 1));
		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS, TimePeriod.MONTHS));
	}

	public void swapCargoLoads(String description, @NonNull List<Cargo> cargoes) {
		Slot<?> load1 = cargoes.get(0).getSortedSlots().get(0);
		Slot<?> load2 = cargoes.get(1).getSortedSlots().get(0);
		Slot<?> discharge1 = cargoes.get(0).getSortedSlots().get(1);
		Slot<?> discharge2 = cargoes.get(1).getSortedSlots().get(1);
		boolean cargo1Valid = true;
		boolean cargo2Valid = true;

		final CompoundCommand currentWiringCommand = new CompoundCommand(description);
		
		if ((load1 instanceof SpotSlot) && (discharge2 instanceof SpotSlot)) {
			currentWiringCommand.append(SetCommand.create(editingDomain, load1, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			currentWiringCommand.append(SetCommand.create(editingDomain, discharge2, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			cargo2Valid = false;
		} else {
			currentWiringCommand.append(SetCommand.create(editingDomain, load1, CargoPackage.Literals.SLOT__CARGO, load2.getCargo()));
		}
		
		if ((load2 instanceof SpotSlot) && (discharge1 instanceof SpotSlot)) {
			currentWiringCommand.append(SetCommand.create(editingDomain, load2, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			currentWiringCommand.append(SetCommand.create(editingDomain, discharge1, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			cargo1Valid = false;
		} else {
			currentWiringCommand.append(SetCommand.create(editingDomain, load2, CargoPackage.Literals.SLOT__CARGO, load1.getCargo()));
		}
		
		if (!cargo1Valid) {
			currentWiringCommand.append(DeleteCommand.create(editingDomain, load1.getCargo()));
		}
		if (!cargo2Valid) {
			currentWiringCommand.append(DeleteCommand.create(editingDomain, load2.getCargo()));
		}
		
		if (currentWiringCommand.isEmpty()) {
			return;
		}
		assert currentWiringCommand.canExecute();

		editingDomain.getCommandStack().execute(currentWiringCommand);

		verifyModel();
	}

	public void swapCargoVessels(String description, @NonNull List<Cargo> cargoes) {
		Cargo cargo0 = cargoes.get(0);
		Cargo cargo1 = cargoes.get(1);

		VesselAssignmentType vat0 = cargo0.getVesselAssignmentType();
		VesselAssignmentType vat1 = cargo1.getVesselAssignmentType();

		int idx0 = cargo0.getSpotIndex();
		int idx1 = cargo1.getSpotIndex();

		final CompoundCommand currentWiringCommand = new CompoundCommand(description);
		currentWiringCommand.append(SetCommand.create(editingDomain, cargo0, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vat1));
		currentWiringCommand.append(SetCommand.create(editingDomain, cargo0, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, idx1));

		currentWiringCommand.append(SetCommand.create(editingDomain, cargo1, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vat0));
		currentWiringCommand.append(SetCommand.create(editingDomain, cargo1, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, idx0));

		if (currentWiringCommand.isEmpty()) {
			return;
		}
		assert currentWiringCommand.canExecute();

		editingDomain.getCommandStack().execute(currentWiringCommand);

		verifyModel();
	}

	public void swapCargoDischarges(String description, @NonNull List<Cargo> cargoes) {
		Slot<?> load1 = cargoes.get(0).getSortedSlots().get(0);
		Slot<?> load2 = cargoes.get(1).getSortedSlots().get(0);
		Slot<?> discharge1 = cargoes.get(0).getSortedSlots().get(1);
		Slot<?> discharge2 = cargoes.get(1).getSortedSlots().get(1);
		boolean cargo1Valid = true;
		boolean cargo2Valid = true;

		final CompoundCommand currentWiringCommand = new CompoundCommand(description);
		
		if ((load1 instanceof SpotSlot) && (discharge2 instanceof SpotSlot)) {
			currentWiringCommand.append(SetCommand.create(editingDomain, load1, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			currentWiringCommand.append(SetCommand.create(editingDomain, discharge2, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			cargo1Valid = false;
		} else {
			currentWiringCommand.append(SetCommand.create(editingDomain, discharge2, CargoPackage.Literals.SLOT__CARGO, discharge1.getCargo()));
		}
		
		if ((load2 instanceof SpotSlot) && (discharge1 instanceof SpotSlot)) {
			currentWiringCommand.append(SetCommand.create(editingDomain, load2, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			currentWiringCommand.append(SetCommand.create(editingDomain, discharge1, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			cargo2Valid = false;
		} else {
			currentWiringCommand.append(SetCommand.create(editingDomain, discharge1, CargoPackage.Literals.SLOT__CARGO, discharge2.getCargo()));
		}
		
		if (!cargo1Valid) {
			currentWiringCommand.append(DeleteCommand.create(editingDomain, discharge1.getCargo()));
		}
		if (!cargo2Valid) {
			currentWiringCommand.append(DeleteCommand.create(editingDomain, discharge2.getCargo()));
		}

		if (currentWiringCommand.isEmpty()) {
			return;
		}
		assert currentWiringCommand.canExecute();

		editingDomain.getCommandStack().execute(currentWiringCommand);

		verifyModel();
	}

	public void unpairCargoes(String description, @NonNull Set<Cargo> cargoes) {

		final CompoundCommand currentWiringCommand = new CompoundCommand(description);

		unpairCargoes(currentWiringCommand, cargoes);

		if (currentWiringCommand.isEmpty()) {
			return;
		}
		assert currentWiringCommand.canExecute();

		editingDomain.getCommandStack().execute(currentWiringCommand);

		verifyModel();
	}

	public void unpairCargoes(final CompoundCommand currentWiringCommand, Set<Cargo> cargoes) {
		final List<Command> setCommands = new LinkedList<>();
		final List<EObject> deleteObjects = new LinkedList<>();
		for (Cargo cargo : cargoes) {
			LoadSlot loadSlot = (LoadSlot) cargo.getSortedSlots().get(0);
			cec.runWiringUpdate(setCommands, deleteObjects, loadSlot, null);
		}

		// Process set before delete
		for (final Command c : setCommands) {
			currentWiringCommand.append(c);
		}
		if (!deleteObjects.isEmpty()) {
			currentWiringCommand.append(DeleteCommand.create(editingDomain, deleteObjects));
		}
	}
}
