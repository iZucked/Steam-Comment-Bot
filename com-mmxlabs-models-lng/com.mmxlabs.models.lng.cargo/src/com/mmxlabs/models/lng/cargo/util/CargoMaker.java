/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.CargoMaker.CargoMakerSlotMaker;
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

		public CargoMakerSlotMaker(@NonNull CargoModelBuilder cargoModelBuilder) {
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

	}

	public CargoMakerSlotMaker makeFOBPurchase(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final PurchaseContract purchaseContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withFOBPurchase(name, windowStart, port, purchaseContract, entity, priceExpression);
	}

	public CargoMakerSlotMaker makeDESPurchase(@NonNull final String name, boolean divertible, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withDESPurchase(name, divertible, windowStart, port, purchaseContract, entity, priceExpression, nominatedVessel);
	}

	public CargoMakerSlotMaker makeMarketFOBPurchase(@NonNull final String name, @NonNull final FOBPurchasesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketFOBPurchase(name, market, windowStart, port, purchaseContract, entity, priceExpression);
	}

	public CargoMakerSlotMaker makeMarketDESPurchase(@NonNull final String name, @NonNull final DESPurchaseMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketDESPurchase(name, market, windowStart, port, purchaseContract, entity, priceExpression);
	}

	public CargoMakerSlotMaker makeDESSale(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final SalesContract salesContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withDESSale(name, windowStart, port, salesContract, entity, priceExpression);
	}

	public CargoMakerSlotMaker makeFOBSale(@NonNull final String name, boolean divertible, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final SalesContract salesContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withFOBSale(name, divertible, windowStart, port, salesContract, entity, priceExpression, nominatedVessel);
	}

	public CargoMakerSlotMaker makeMarketDESSale(@NonNull final String name, @NonNull final DESSalesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketDESSale(name, market, windowStart, port, salesContract, entity, priceExpression);
	}

	public CargoMakerSlotMaker makeMarketFOBSale(@NonNull final String name, @NonNull final FOBSalesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final CargoMakerSlotMaker slotMaker = new CargoMakerSlotMaker(cargoModelBuilder);
		return slotMaker.withMarketFOBSale(name, market, windowStart, port, salesContract, entity, priceExpression);
	}

	public CargoMaker withVesselAssignment(@NonNull final VesselAvailability vesselAvailability, final int sequenceHint) {

		cargoModelBuilder.configureCargoVesselAssignment(cargo, vesselAvailability, sequenceHint);

		return this;
	}

	public CargoMaker withAssignmentFlags(boolean allowRewiring, boolean lockedToAssignment) {

		assert allowRewiring || lockedToAssignment;
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
