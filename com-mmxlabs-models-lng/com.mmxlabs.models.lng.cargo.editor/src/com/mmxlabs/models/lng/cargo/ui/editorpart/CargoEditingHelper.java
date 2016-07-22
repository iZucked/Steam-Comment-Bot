package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.SpotSlotUtils;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;

public class CargoEditingHelper {

	private final CargoEditingCommands cec;

	private final LNGScenarioModel scenarioModel;

	private final EditingDomain editingDomain;
	private final boolean verifyChanges = true;

	// private CargoModel cargoModel;
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

		if (verifyChanges) {
			assert slot.getNominatedVessel() == vessel;
		}

		verifyModel();

	}

	public void assignCargoToSpotCharterIn(@NonNull final String description, @NonNull final Cargo cargo, @NonNull final CharterInMarket charterInMarket, final int spotIndex) {

		// Validate shipped cargo
		for (final Slot slot : cargo.getSlots()) {
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

		if (verifyChanges) {
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

		if (verifyChanges) {
			assert cargo.getVesselAssignmentType() == vesselAssignmentType;
			assert cargo.getSpotIndex() == spotIndex;
		}
	}

	public void assignCargoToVesselAvailability(@NonNull final String description, @NonNull final Cargo cargo, @NonNull final VesselAvailability vesselAvailability) {

		// Validate shipped cargo
		for (final Slot slot : cargo.getSlots()) {
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

		if (verifyChanges) {
			assert cargo.getVesselAssignmentType() == vesselAvailability;
			assert cargo.getSpotIndex() == -1;
		}

		verifyModel();
	}

	public void unassignCargoAssignment(@NonNull final String description, @NonNull final Cargo cargo) {

		// Validate shipped cargo
		for (final Slot slot : cargo.getSlots()) {
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

		if (verifyChanges) {
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
		if (verifyChanges) {
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
		if (verifyChanges) {
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

		if (verifyChanges) {
			for (final Cargo cargo : cargoes) {
				assert cargo.isAllowRewiring() == false;
				assert cargo.isLocked() == true;
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

		if (verifyChanges) {
			for (final Cargo cargo : cargoes) {
				assert cargo.isAllowRewiring() == true;
				assert cargo.isLocked() == false;
			}
		}

		verifyModel();
	}

	public void lockVesselEventAssignment(@NonNull final String description, @NonNull final VesselEvent vesselEvent) {

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.TRUE));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (verifyChanges) {
			assert vesselEvent.isLocked() == true;
		}

		verifyModel();
	}

	public void unlockVesselEventAssignment(@NonNull final String description, @NonNull final VesselEvent vesselEvent) {

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, vesselEvent, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);
		if (verifyChanges) {
			assert vesselEvent.isLocked() == false;
		}
		verifyModel();
	}

	public void pairSlotsIntoCargo(@NonNull final String description, @NonNull final LoadSlot loadSlot, @NonNull final DischargeSlot dischargeSlot) {

		final Cargo loadCargo = loadSlot.getCargo();
		final Cargo dischargeCargo = dischargeSlot.getCargo();

		final List<Command> setCommands = new LinkedList<Command>();
		final List<Command> deleteCommands = new LinkedList<Command>();

		cec.runWiringUpdate(setCommands, deleteCommands, loadSlot, dischargeSlot);

		final CompoundCommand currentWiringCommand = new CompoundCommand(description);
		// Process set before delete
		for (final Command c : setCommands) {
			currentWiringCommand.append(c);
		}
		for (final Command c : deleteCommands) {
			currentWiringCommand.append(c);
		}

		assert currentWiringCommand.canExecute();

		editingDomain.getCommandStack().execute(currentWiringCommand);

		if (verifyChanges) {

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
		assert loadSlot.isDESPurchase() == true;
		assert loadSlot.isDivertible() == true;

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE, Boolean.FALSE));
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL, SetCommand.UNSET_VALUE));
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION, 0));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);
		if (verifyChanges) {
			assert loadSlot.isDESPurchase() == false;
			assert loadSlot.getNominatedVessel() == null;
			assert loadSlot.getShippingDaysRestriction() == 0;
		}

		verifyModel();
	}

	public void convertToDESPurchase(@NonNull final String description, @NonNull final LoadSlot loadSlot) {
		assert loadSlot.isDESPurchase() == false;

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE, Boolean.TRUE));
		cc.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.Literals.SLOT__DIVERTIBLE, Boolean.TRUE));
		final Cargo cargo = loadSlot.getCargo();
		if (cargo != null) {
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, SetCommand.UNSET_VALUE));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
		}
		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (verifyChanges) {
			assert loadSlot.isDESPurchase() == true;
			assert loadSlot.isDivertible() == true;
			if (cargo != null) {
				assert cargo.getVesselAssignmentType() == null;
				assert cargo.getSpotIndex() == -1;
				assert cargo.isLocked() == false;
			}
		}

		verifyModel();
	}

	public void convertToDESSale(@NonNull final String description, @NonNull final DischargeSlot dischargeSlot) {
		assert dischargeSlot.isFOBSale() == true;
		assert dischargeSlot.isDivertible() == true;

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE, Boolean.FALSE));
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL, SetCommand.UNSET_VALUE));
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION, 0));

		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);
		if (verifyChanges) {
			assert dischargeSlot.isFOBSale() == false;
			assert dischargeSlot.getNominatedVessel() == null;
			assert dischargeSlot.getShippingDaysRestriction() == 0;
		}

		verifyModel();
	}

	public void convertToFOBSale(@NonNull final String description, @NonNull final DischargeSlot dischargeSlot) {
		assert dischargeSlot.isFOBSale() == false;

		final CompoundCommand cc = new CompoundCommand(description);
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE, Boolean.TRUE));
		cc.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__DIVERTIBLE, Boolean.TRUE));
		final Cargo cargo = dischargeSlot.getCargo();
		if (cargo != null) {
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, SetCommand.UNSET_VALUE));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, -1));
			cc.append(SetCommand.create(editingDomain, cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED, Boolean.FALSE));
		}
		assert cc.canExecute();
		editingDomain.getCommandStack().execute(cc);

		if (verifyChanges) {
			assert dischargeSlot.isFOBSale() == true;
			assert dischargeSlot.isDivertible() == true;
			if (cargo != null) {
				assert cargo.getVesselAssignmentType() == null;
				assert cargo.getSpotIndex() == -1;
				assert cargo.isLocked() == false;
			}
		}

		verifyModel();
	}

	// public void createDESSaleSlotForFOBPurchase(@NonNull final String description, @NonNull final LoadSlot loadSlot) {
	// assert loadSlot.isDESPurchase() == false;
	//
	// final Cargo existingCargo = loadSlot.getCargo();
	// // These slots should have been un-paired from cargo
	// final List<Slot> slotsToCheck = new LinkedList<>();
	// if (verifyChanges) {
	// if (existingCargo != null) {
	// slotsToCheck.addAll(existingCargo.getSlots());
	// slotsToCheck.remove(loadSlot);
	// }
	// }
	//
	// final CompoundCommand cc = new CompoundCommand(description);
	// final List<Command> setCommands = new LinkedList<Command>();
	// final List<Command> deleteCommands = new LinkedList<Command>();
	//
	// final DischargeSlot dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, false);
	// setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__WINDOW_START, loadSlot.getWindowStart()));
	//
	// cec.runWiringUpdate(setCommands, deleteCommands, loadSlot, dischargeSlot);
	//
	// setCommands.forEach(c -> cc.append(c));
	// deleteCommands.forEach(c -> cc.append(c));
	//
	// assert cc.canExecute();
	// editingDomain.getCommandStack().execute(cc);
	//
	// if (verifyChanges) {
	//
	// }
	//
	// verifyModel();
	// }
	//
	// public void createSpotSaleSlotForPurchase(@NonNull final String description, @NonNull final LoadSlot loadSlot, @NonNull DESSalesMarket market) {
	// assert loadSlot.isDESPurchase() == false;
	//
	// final Cargo existingCargo = loadSlot.getCargo();
	// // These slots should have been un-paired from cargo
	// final List<Slot> slotsToCheck = new LinkedList<>();
	// if (verifyChanges) {
	// if (existingCargo != null) {
	// slotsToCheck.addAll(existingCargo.getSlots());
	// slotsToCheck.remove(loadSlot);
	// }
	// }
	//
	// final CompoundCommand cc = new CompoundCommand(description);
	// final List<Command> setCommands = new LinkedList<Command>();
	// final List<Command> deleteCommands = new LinkedList<Command>();
	//
	// final SpotDischargeSlot dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, market);
	//
	// // Get start of month and create full sized window
	// ZonedDateTime cal = loadSlot.getWindowStartWithSlotOrPortTime();
	// // Take into account travel time
	// if (loadSlot.isDESPurchase() && loadSlot.isDivertible()) {
	// final int travelTime = getTravelTime(loadSlot.getPort(), dischargeSlot.getPort(), loadSlot.getNominatedVessel());
	// cal = cal.plusHours(travelTime);
	// cal = cal.plusHours(loadSlot.getSlotOrPortDuration());
	// } else if (!loadSlot.isDESPurchase()) {
	//
	// AVesselSet<? extends Vessel> assignedVessel = null;
	// if (loadSlot.getCargo() != null) {
	// final VesselAssignmentType vesselAssignmentType = loadSlot.getCargo().getVesselAssignmentType();
	// if (vesselAssignmentType instanceof VesselAvailability) {
	// assignedVessel = ((VesselAvailability) vesselAssignmentType).getVessel();
	// } else if (vesselAssignmentType instanceof CharterInMarket) {
	// assignedVessel = ((CharterInMarket) vesselAssignmentType).getVesselClass();
	// }
	// }
	// final int travelTime = getTravelTime(loadSlot.getPort(), dischargeSlot.getPort(), assignedVessel);
	// cal = cal.plusHours(travelTime);
	// cal = cal.plusHours(loadSlot.getSlotOrPortDuration());
	// }
	//
	// // Get existing names
	//
	// if (dischargeSlot.isFOBSale()) {
	// setCommands.add(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.Literals.SLOT__PORT, loadSlot.getPort()));
	// }
	//
	// // Set back to start of month
	// cal = cal.withDayOfMonth(1).withHour(0);
	// final LocalDate dishargeCal = cal.toLocalDate();
	//
	// setSpotSlotWindow(dischargeSlot, dishargeCal, setCommands);
	//
	// final Set<String> usedIDStrings = SpotSlotUtils.getUsedDischargeNames(cargoModel);
	// @NonNull
	// String slotName = SpotSlotUtils.getSpotSlotName(market, dishargeCal, usedIDStrings);
	// setCommands.add(SetCommand.create(editingDomain, dischargeSlot, MMXCorePackage.Literals.NAMED_OBJECT__NAME, slotName));
	//
	// cec.runWiringUpdate(setCommands, deleteCommands, loadSlot, dischargeSlot);
	//
	// setCommands.forEach(c -> cc.append(c));
	// deleteCommands.forEach(c -> cc.append(c));
	//
	// assert cc.canExecute();
	// editingDomain.getCommandStack().execute(cc);
	//
	// if (verifyChanges) {
	//
	// }
	//
	// verifyModel();
	// }

	public void verifyModel() {
		cec.verifyCargoModel(ScenarioModelUtil.getCargoModel(scenarioModel));
	}

	// TODO: Move in to EMF Distnace PRoivider like API
	private int getTravelTime(final Port from, final Port to, final AVesselSet<? extends Vessel> assignedVessel) {

		double maxSpeed = 19.0;

		if (assignedVessel instanceof Vessel) {
			final Vessel vessel = (Vessel) assignedVessel;
			final VesselClass vesselClass = vessel.getVesselClass();
			if (vesselClass != null) {
				maxSpeed = vesselClass.getMaxSpeed();
			}
		}

		int distance = 0;
		LOOP_ROUTES: for (final Route route : scenarioModel.getReferenceModel().getPortModel().getRoutes()) {
			if (route.isCanal() == false) {
				for (final RouteLine dl : route.getLines()) {
					if (dl.getFrom().equals(from) && dl.getTo().equals(to)) {
						distance = dl.getDistance();
						break LOOP_ROUTES;
					}
				}

			}
		}

		final int travelTime = (int) Math.round((double) distance / maxSpeed);

		return travelTime;
	}

	public <T extends SpotSlot & Slot> void setSpotSlotWindow(@NonNull final T slot, @NonNull final LocalDate cal, Collection<Command> setComands) {
		// Set back to start of month
		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_START, cal.withDayOfMonth(1)));
		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_START_TIME, 0));

		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE, 1));
		setComands.add(SetCommand.create(editingDomain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS, TimePeriod.MONTHS));
	}
}
