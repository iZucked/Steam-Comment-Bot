/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

public class CargoMaker {
	@NonNull
	private final CargoModelBuilder cargoModelBuilder;

	@NonNull
	private final Cargo cargo;
	@NonNull
	private final List<Slot> slots = new LinkedList<>();

	public class CargoMakerSlotMaker extends AbstractSlotMaker<CargoMakerSlotMaker> {

		public CargoMakerSlotMaker(@NonNull final CargoModelBuilder cargoModelBuilder) {
			super(cargoModelBuilder);
		}

		@NonNull
		public CargoMaker build() {

			CargoMaker.this.slots.add(slot);
			return CargoMaker.this;
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

	public CargoMakerSlotMaker makeDESPurchase(@NonNull final String name, final boolean divertible, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withDESPurchase(name, divertible, windowStart, port, purchaseContract, entity, priceExpression, null, nominatedVessel);
	}

	public CargoMakerSlotMaker makeDESPurchase(@NonNull final String name, final boolean divertible, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, final @Nullable Double cv,
			@Nullable final Vessel nominatedVessel) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withDESPurchase(name, divertible, windowStart, port, purchaseContract, entity, priceExpression, cv, nominatedVessel);
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

	public CargoMakerSlotMaker makeFOBSale(@NonNull final String name, final boolean divertible, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withFOBSale(name, divertible, windowStart, port, salesContract, entity, priceExpression, nominatedVessel);
	}

	public CargoMakerSlotMaker makeMarketDESSale(@NonNull final String name, @NonNull final DESSalesMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
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

		assert !lockedToAssignment || (allowRewiring && lockedToAssignment);
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
}
