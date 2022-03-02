/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public class CargoMaker {
	@NonNull
	private final CargoModelBuilder cargoModelBuilder;

	@NonNull
	private final Cargo cargo;
	@NonNull
	private final List<Slot<?>> slots = new LinkedList<>();

	public class CargoMakerSlotMaker extends AbstractSlotMaker<CargoMakerSlotMaker> {

		public CargoMakerSlotMaker(@NonNull final CargoModelBuilder cargoModelBuilder) {
			super(cargoModelBuilder);
		}

		@NonNull
		public CargoMaker build() {
			CargoMaker.this.slots.add(slot);
			return CargoMaker.this;
		}

		public CargoMakerSlotMaker withWindowCounterParty(boolean counterPartyWindow) {
			this.slot.setWindowCounterParty(counterPartyWindow);
			return this;
		}

		public CargoMakerSlotMaker withSalesDeliveryType(CargoDeliveryType sdt) {
			if (this.slot instanceof LoadSlot) {
				LoadSlot ls = (LoadSlot) slot;
				ls.setSalesDeliveryType(sdt);
			} else {
				throw new IllegalArgumentException("Slot is not a load slot, so cannot set salesDeliveryType");
			}
			return this;
		}

		public CargoMakerSlotMaker withPurchaseDeliveryType(CargoDeliveryType sdt) {
			if (this.slot instanceof DischargeSlot) {
				DischargeSlot ds = (DischargeSlot) slot;
				ds.setPurchaseDeliveryType(sdt);
			} else {
				throw new IllegalArgumentException("Slot is not a discharge slot, so cannot set purchaseDeliveryType");
			}
			return this;
		}
	}

	public CargoMaker(@NonNull final CargoModelBuilder cargoModelBuilder) {
		this.cargoModelBuilder = cargoModelBuilder;
		this.cargo = CargoFactory.eINSTANCE.createCargo();
		this.cargo.setAllowRewiring(true);
	}

	public CargoMakerSlotMaker makeFOBPurchase(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final PurchaseContract purchaseContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withFOBPurchase(name, windowStart, port, purchaseContract, entity, priceExpression, null);
	}

	public CargoMakerSlotMaker makeFOBPurchase(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final PurchaseContract purchaseContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, final @Nullable Double cv) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withFOBPurchase(name, windowStart, port, purchaseContract, entity, priceExpression, cv);
	}

	public CargoMakerSlotMaker makeDESPurchase(@NonNull final String name, final DESPurchaseDealType dealType, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, final @Nullable Double cv,
			@Nullable final Vessel nominatedVessel) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withDESPurchase(name, dealType, windowStart, port, purchaseContract, entity, priceExpression, cv, nominatedVessel);
	}

	public CargoMakerSlotMaker makeMarketFOBPurchase(@NonNull final String name, @NonNull final FOBPurchasesMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketFOBPurchase(name, market, windowStart, port);
	}

	public CargoMakerSlotMaker makeMarketDESPurchase(@NonNull final String name, @NonNull final DESPurchaseMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketDESPurchase(name, market, windowStart, port);
	}

	public CargoMakerSlotMaker makeDESSale(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final SalesContract salesContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withDESSale(name, windowStart, port, salesContract, entity, priceExpression);
	}

	public CargoMakerSlotMaker makeFOBSale(@NonNull final String name, final FOBSaleDealType dealType, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withFOBSale(name, dealType, windowStart, port, salesContract, entity, priceExpression, nominatedVessel);
	}

	public CargoMakerSlotMaker makeMarketDESSale(@NonNull final String name, @NonNull final DESSalesMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketDESSale(name, market, windowStart, port);
	}

	public CargoMakerSlotMaker makeMarketDESSale(@NonNull final String name, @NonNull final DESSalesMarket market, @NonNull final YearMonth windowStart) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		final Port port = market.getNotionalPort();
		if (port == null) {
			throw new IllegalArgumentException();
		}
		return slotMaker.withMarketDESSale(name, market, windowStart, port);
	}

	public CargoMakerSlotMaker makeMarketFOBSale(@NonNull final String name, @NonNull final FOBSalesMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketFOBSale(name, market, windowStart, port);
	}

	public CargoMaker withVesselAssignment(@NonNull final VesselAvailability vesselAvailability, final int sequenceHint) {

		cargoModelBuilder.configureCargoVesselAssignment(cargo, vesselAvailability, sequenceHint);

		return this;
	}

	public CargoMaker withAssignmentFlags(final boolean allowRewiring, final boolean lockedToAssignment) {

		assert !lockedToAssignment || (!allowRewiring && lockedToAssignment);
		cargo.setLocked(lockedToAssignment);
		cargo.setAllowRewiring(allowRewiring);

		return this;
	}

	public CargoMaker withVesselAssignment(@NonNull final CharterInMarket charterInMarket, final int spotIndex, final int sequenceHint) {

		cargoModelBuilder.configureCargoVesselAssignment(cargo, charterInMarket, spotIndex, sequenceHint);

		return this;
	}

	public Cargo build() {

		assert slots.size() >= 2;

		cargo.getSlots().addAll(slots);
		cargoModelBuilder.getCargoModel().getCargoes().add(cargo);

		return cargo;
	}

	public CargoMaker withAssignmentType(VesselAssignmentType vesselAssignmentType) {
		cargo.setVesselAssignmentType(vesselAssignmentType);
		return this;
	}
}
