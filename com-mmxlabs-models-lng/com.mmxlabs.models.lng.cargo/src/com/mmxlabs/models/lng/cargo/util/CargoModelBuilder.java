/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.types.PortCapability;

public class CargoModelBuilder {
	private final @NonNull CargoModel cargoModel;

	public CargoModelBuilder(final @NonNull CargoModel cargoModel) {
		this.cargoModel = cargoModel;
	}

	@NonNull
	public CargoModel getCargoModel() {
		return cargoModel;
	}

	public CargoMaker makeCargo() {
		return new CargoMaker(this);
	}

	public @NonNull EndHeelOptions createEndHeelOptions(@Nullable final Integer targetEndHeelInM3) {
		final EndHeelOptions result = CargoFactory.eINSTANCE.createEndHeelOptions();
		if (targetEndHeelInM3 != null) {
			result.setTargetEndHeel(targetEndHeelInM3);
		}
		return result;
	}

	private void configureSlot(@NonNull final Slot slot, @NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final Contract contract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		if (contract == null && entity == null) {
			throw new IllegalArgumentException("Contract or Entity must be set");
		}

		slot.setName(name);
		slot.setPort(port);
		slot.setWindowStart(windowStart);
		if (contract != null) {
			slot.setContract(contract);
		}
		if (entity != null) {
			slot.setEntity(entity);
		}
		if (priceExpression != null) {
			slot.setPriceExpression(priceExpression);
		}
	}

	public @NonNull LoadSlot createFOBPurchase(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final PurchaseContract purchaseContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		validatePortCapability(port, PortCapability.LOAD);

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		configureSlot(slot, name, windowStart, port, purchaseContract, entity, priceExpression);
		cargoModel.getLoadSlots().add(slot);
		return slot;
	}

	public @NonNull LoadSlot createDESPurchase(@NonNull final String name, final boolean divertable, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {

		validatePortCapability(port, divertable ? PortCapability.LOAD : PortCapability.DISCHARGE);

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		configureSlot(slot, name, windowStart, port, purchaseContract, entity, priceExpression);
		slot.setDESPurchase(true);
		slot.setDivertible(divertable);
		if (nominatedVessel != null) {
			slot.setNominatedVessel(nominatedVessel);
		} else if (divertable) {
			throw new IllegalArgumentException("Divertable DES Purchases need a nominated vessel");
		}
		cargoModel.getLoadSlots().add(slot);
		return slot;
	}

	public @NonNull SpotLoadSlot createSpotFOBPurchase(@NonNull final String name, @NonNull final FOBPurchasesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		validatePortCapability(port, PortCapability.LOAD);

		final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
		configureSlot(slot, name, windowStart, port, purchaseContract, entity, priceExpression);
		slot.setMarket(market);
		slot.setOptional(true);

		cargoModel.getLoadSlots().add(slot);
		return slot;
	}

	public @NonNull SpotLoadSlot createSpotDESPurchase(@NonNull final String name, @NonNull final DESPurchaseMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		validatePortCapability(port, PortCapability.DISCHARGE);

		final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
		configureSlot(slot, name, windowStart, port, purchaseContract, entity, priceExpression);
		slot.setDESPurchase(true);
		slot.setMarket(market);
		slot.setOptional(true);

		cargoModel.getLoadSlots().add(slot);
		return slot;
	}

	public @NonNull DischargeSlot createDESSale(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final SalesContract salesContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		validatePortCapability(port, PortCapability.DISCHARGE);

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
		configureSlot(slot, name, windowStart, port, salesContract, entity, priceExpression);

		cargoModel.getDischargeSlots().add(slot);

		return slot;
	}

	public @NonNull DischargeSlot createFOBSale(@NonNull final String name, final boolean divertable, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {

		if (!divertable) {
			validatePortCapability(port, PortCapability.LOAD);
		}

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
		configureSlot(slot, name, windowStart, port, salesContract, entity, priceExpression);
		if (nominatedVessel != null) {
			slot.setNominatedVessel(nominatedVessel);
		} else if (divertable) {
			throw new IllegalArgumentException("Divertable FOB sale need a nominated vessel");
		}
		slot.setFOBSale(true);
		cargoModel.getDischargeSlots().add(slot);

		return slot;
	}

	public @NonNull SpotDischargeSlot createSpotDESSale(@NonNull final String name, @NonNull final DESSalesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		validatePortCapability(port, PortCapability.DISCHARGE);

		final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
		configureSlot(slot, name, windowStart, port, salesContract, entity, priceExpression);
		slot.setMarket(market);
		slot.setOptional(true);

		cargoModel.getDischargeSlots().add(slot);

		return slot;
	}

	public @NonNull SpotDischargeSlot createSpotFOBSale(@NonNull final String name, @NonNull final FOBSalesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		validatePortCapability(port, PortCapability.LOAD);

		final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
		configureSlot(slot, name, windowStart, port, salesContract, entity, priceExpression);
		slot.setMarket(market);
		slot.setFOBSale(true);
		slot.setOptional(true);
		cargoModel.getDischargeSlots().add(slot);

		return slot;
	}

	@NonNull
	public Cargo createCargo(@NonNull final Slot... slots) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		for (final Slot slot : slots) {
			cargo.getSlots().add(slot);
		}

		cargoModel.getCargoes().add(cargo);

		return cargo;
	}

	@NonNull
	public Cargo createCargo(@NonNull final Iterable<Slot> slots) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		for (final Slot slot : slots) {
			cargo.getSlots().add(slot);
		}

		cargoModel.getCargoes().add(cargo);

		return cargo;
	}

	public void configureCargoVesselAssignment(@NonNull final Cargo cargo, @NonNull final VesselAvailability vesselAvailability, final int sequenceHint) {
		if (cargo.getCargoType() != CargoType.FLEET) {
			throw new IllegalArgumentException("VesselAvailability can only be set of Fleet/Shipped Cargoes");
		}
		cargo.setVesselAssignmentType(vesselAvailability);
		cargo.setSequenceHint(sequenceHint);
		cargo.setSpotIndex(0);
	}

	public void configureCargoVesselAssignment(@NonNull final Cargo cargo, @NonNull final CharterInMarket charterInMarket, final int spotIndex, final int sequenceHint) {
		if (cargo.getCargoType() != CargoType.FLEET) {
			throw new IllegalArgumentException("CharterInMarket can only be set of Fleet/Shipped Cargoes");
		}
		cargo.setVesselAssignmentType(charterInMarket);
		cargo.setSpotIndex(spotIndex);
		cargo.setSequenceHint(sequenceHint);
	}

	private void validatePortCapability(@NonNull final Port port, @NonNull final PortCapability portCapability) {
		if (!port.getCapabilities().contains(portCapability)) {
			throw new IllegalArgumentException("Port does not have required capability");
		}
	}

	@NonNull
	public VesselAvailabilityMaker makeVesselAvailability(final @NonNull Vessel vessel) {
		return new VesselAvailabilityMaker(this, vessel);
	}

	@NonNull
	public CharterOutEventMaker makeCharterOutEvent(@NonNull String name, @NonNull LocalDateTime startAfter, @NonNull LocalDateTime startBy, @NonNull Port startPort) {

		return new CharterOutEventMaker(name, startPort, startAfter, startBy, this);
	}
}
