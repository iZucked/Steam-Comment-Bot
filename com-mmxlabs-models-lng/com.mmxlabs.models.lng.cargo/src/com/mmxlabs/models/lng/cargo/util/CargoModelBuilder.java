/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;

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

	public @NonNull EndHeelOptions createEndHeelOptions(int minVolumeInM3, int maxVolumeInM3, @NonNull EVesselTankState state) {
		final EndHeelOptions result = CommercialFactory.eINSTANCE.createEndHeelOptions();

		if (minVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}

		if (maxVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}
		if (minVolumeInM3 > maxVolumeInM3) {
			throw new IllegalArgumentException();
		}

		if (state == EVesselTankState.MUST_BE_WARM) {
			if (minVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
			if (maxVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
		}

		result.setMinimumEndHeel(minVolumeInM3);
		result.setMaximumEndHeel(maxVolumeInM3);
		result.setTankState(state);

		return result;
	}

	private void configureSlot(@NonNull final Slot slot, @NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final Contract contract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		if (slot instanceof SpotSlot) {
			if (contract != null) {
				throw new IllegalArgumentException("Contract must be null for a spot slot");
			}
			if (entity != null) {
				throw new IllegalArgumentException("Entity must be null for a spot slot");
			}

			if (priceExpression != null) {
				throw new IllegalArgumentException("Price Expression must be null for a spot slot");
			}
		} else if (contract == null && entity == null) {
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
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, final @Nullable Double cv) {

		validatePortCapability(port, PortCapability.LOAD);

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		configureSlot(slot, name, windowStart, port, purchaseContract, entity, priceExpression);
		if (cv != null) {
			slot.setCargoCV(cv);
		}

		cargoModel.getLoadSlots().add(slot);
		return slot;
	}

	public @NonNull LoadSlot createDESPurchase(@NonNull final String name, final DESPurchaseDealType dealType, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Double cv,
			@Nullable final Vessel nominatedVessel) {

		validatePortCapability(port, (dealType == DESPurchaseDealType.DEST_ONLY || dealType == DESPurchaseDealType.DIVERTIBLE) ? PortCapability.DISCHARGE : PortCapability.LOAD);

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		configureSlot(slot, name, windowStart, port, purchaseContract, entity, priceExpression);
		if (cv != null) {
			slot.setCargoCV(cv);
		}

		slot.setDESPurchase(true);
		slot.setDesPurchaseDealType(dealType);
		if (nominatedVessel != null) {
			slot.setNominatedVessel(nominatedVessel);
		}
		cargoModel.getLoadSlots().add(slot);
		return slot;
	}

	public @NonNull SpotLoadSlot createSpotFOBPurchase(@NonNull final String name, @NonNull final FOBPurchasesMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {

		validatePortCapability(port, PortCapability.LOAD);

		final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
		configureSlot(slot, name, windowStart.atDay(1), port, null, null, null);
		slot.setMarket(market);
		slot.setOptional(true);
		slot.setWindowStartTime(0);
		slot.setWindowSize(1);
		slot.setWindowSizeUnits(TimePeriod.MONTHS);

		cargoModel.getLoadSlots().add(slot);
		return slot;
	}

	public @NonNull SpotLoadSlot createSpotDESPurchase(@NonNull final String name, @NonNull final DESPurchaseMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {

		validatePortCapability(port, PortCapability.DISCHARGE);

		final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
		configureSlot(slot, name, windowStart.atDay(1), port, null, null, null);
		slot.setDESPurchase(true);
		slot.setMarket(market);
		slot.setOptional(true);
		slot.setWindowStartTime(0);
		slot.setWindowSize(1);
		slot.setWindowSizeUnits(TimePeriod.MONTHS);

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

	public @NonNull DischargeSlot createFOBSale(@NonNull final String name, final FOBSaleDealType dealType, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {

		if (dealType == FOBSaleDealType.SOURCE_ONLY) {
			validatePortCapability(port, PortCapability.LOAD);
		}

		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
		configureSlot(slot, name, windowStart, port, salesContract, entity, priceExpression);
		if (nominatedVessel != null) {
			slot.setNominatedVessel(nominatedVessel);
		}
		slot.setFOBSale(true);
		slot.setFobSaleDealType(dealType);
		cargoModel.getDischargeSlots().add(slot);

		return slot;
	}

	@NonNull
	public SlotMaker<LoadSlot> makeFOBPurchase(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final PurchaseContract purchaseContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable Double cv) {

		return new SlotMaker<LoadSlot>(this).withFOBPurchase(name, windowStart, port, purchaseContract, entity, priceExpression, cv);
	}

	@NonNull
	public SlotMaker<LoadSlot> makeDESPurchase(@NonNull final String name, final DESPurchaseDealType dealType, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable Double cv,
			@Nullable final Vessel nominatedVessel) {

		return new SlotMaker<LoadSlot>(this).withDESPurchase(name, dealType, windowStart, port, purchaseContract, entity, priceExpression, cv, nominatedVessel);
	}

	@NonNull
	public SlotMaker<DischargeSlot> makeDESSale(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final SalesContract salesContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		return new SlotMaker<DischargeSlot>(this).withDESSale(name, windowStart, port, salesContract, entity, priceExpression);
	}

	@NonNull
	public SlotMaker<DischargeSlot> makeFOBSale(@NonNull final String name, final FOBSaleDealType dealType, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {

		return new SlotMaker<DischargeSlot>(this).withFOBSale(name, dealType, windowStart, port, salesContract, entity, priceExpression, nominatedVessel);
	}

	public @NonNull SpotDischargeSlot createSpotDESSale(@NonNull final String name, @NonNull final DESSalesMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {

		validatePortCapability(port, PortCapability.DISCHARGE);

		final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
		configureSlot(slot, name, windowStart.atDay(1), port, null, null, null);
		slot.setMarket(market);
		slot.setOptional(true);
		slot.setWindowStartTime(0);
		slot.setWindowSize(1);
		slot.setWindowSizeUnits(TimePeriod.MONTHS);

		cargoModel.getDischargeSlots().add(slot);

		return slot;
	}

	public @NonNull SpotDischargeSlot createSpotFOBSale(@NonNull final String name, @NonNull final FOBSalesMarket market, @NonNull final YearMonth windowStart, @NonNull final Port port) {

		validatePortCapability(port, PortCapability.LOAD);

		final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
		configureSlot(slot, name, windowStart.atDay(1), port, null, null, null);
		slot.setMarket(market);
		slot.setFOBSale(true);
		slot.setOptional(true);
		slot.setWindowStartTime(0);
		slot.setWindowSize(1);
		slot.setWindowSizeUnits(TimePeriod.MONTHS);

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

	public void configureCargoVesselAssignment(@NonNull final Cargo cargo, @NonNull final VesselCharter vesselCharter, final int sequenceHint) {
		if (cargo.getCargoType() != CargoType.FLEET) {
			throw new IllegalArgumentException("VesselCharter can only be set of Fleet/Shipped Cargoes");
		}
		cargo.setVesselAssignmentType(vesselCharter);
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
			throw new IllegalArgumentException("Port does not have required capability " + portCapability);
		}
	}

	@NonNull
	public VesselCharterMaker makeVesselCharter(final @NonNull Vessel vessel, @NonNull final BaseLegalEntity entity) {
		return new VesselCharterMaker(this, vessel, entity);
	}

	@NonNull
	public CharterOutEventMaker makeCharterOutEvent(@NonNull final String name, @NonNull final LocalDateTime startAfter, @NonNull final LocalDateTime startBy, @NonNull final Port startPort) {

		return new CharterOutEventMaker(name, startPort, startAfter, startBy, this);
	}

	@NonNull
	public DryDockEventMaker makeDryDockEvent(@NonNull final String name, @NonNull final LocalDateTime startAfter, @NonNull final LocalDateTime startBy, @NonNull final Port startPort) {

		return new DryDockEventMaker(name, startPort, startAfter, startBy, this);
	}

	@NonNull
	public MaintenanceEventMaker makeMaintenanceEvent(@NonNull final String name, @NonNull final LocalDateTime startAfter, @NonNull final LocalDateTime startBy, @NonNull final Port startPort) {

		return new MaintenanceEventMaker(name, startPort, startAfter, startBy, this);
	}

	public @NonNull SlotMaker<SpotLoadSlot> makeSpotFOBPurchase(String name, @NonNull YearMonth windowStart, @NonNull FOBPurchasesMarket market) {
		return new SlotMaker<SpotLoadSlot>(this).withMarketFOBPurchase(name, market, windowStart);
	}

	public @NonNull SlotMaker<SpotLoadSlot> makeSpotDESPurchase(String name, @NonNull YearMonth windowStart, @NonNull DESPurchaseMarket market, @NonNull Port port) {
		return new SlotMaker<SpotLoadSlot>(this).withMarketDESPurchase(name, market, windowStart, port);
	}

	public @NonNull SlotMaker<SpotDischargeSlot> makeSpotDESSale(String name, @NonNull YearMonth windowStart, @NonNull DESSalesMarket market) {
		return new SlotMaker<SpotDischargeSlot>(this).withMarketDESSale(name, market, windowStart, market.getNotionalPort());
	}

	public @NonNull SlotMaker<SpotDischargeSlot> makeSpotFOBSale(String name, @NonNull YearMonth windowStart, @NonNull FOBSalesMarket market, @NonNull Port port) {
		return new SlotMaker<SpotDischargeSlot>(this).withMarketFOBSale(name, market, windowStart, port);
	}
	
	public @NonNull CanalBookingSlot makeCanalBooking(final @NonNull RouteOption routeOption, final @NonNull CanalEntry canalEntrance, final @NonNull LocalDate date, @Nullable AVesselSet<Vessel> vessel) {
		final CanalBookingSlot booking = CargoFactory.eINSTANCE.createCanalBookingSlot();
		booking.setRouteOption(routeOption);
		booking.setCanalEntrance(canalEntrance);
		booking.setBookingDate(date);
		
		if (vessel != null) {
			booking.setVessel(vessel);
		}
		
		if (cargoModel.getCanalBookings() == null) {
			cargoModel.setCanalBookings(CargoFactory.eINSTANCE.createCanalBookings());
		}

		cargoModel.getCanalBookings().getCanalBookingSlots().add(booking);

		return booking;
	}
	
	/**
	 * Initialise the Panama bookings
	 * Create the "default" vessel group
	 * And create a year-round seasonality entries for that vessel group
	 */
	public void initCanalBookings() {
		final CanalBookings cb = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModel.setCanalBookings(cb);
		
		final VesselGroupCanalParameters vgcp = createVesselGroupCanalParameters("default");
		cb.getVesselGroupCanalParameters().add(vgcp);
		cb.getPanamaSeasonalityRecords().add(createPanamaSeasonalityRecord(vgcp, 0, 0, 0, 0, 0));
	}
	
	/**
	 * Creates and returns a vessel group with a given name
	 * @param name
	 * @return
	 */
	public @NonNull VesselGroupCanalParameters createVesselGroupCanalParameters(String name) {
		final VesselGroupCanalParameters vgcp = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		vgcp.setName(name);
		return vgcp;
	}
	
	/**
	 * Creates and returns a panama seasonality record for a given vessel group
	 * @param vgcp
	 * @param startDay
	 * @param startMonth
	 * @param startYear
	 * @param nb
	 * @param sb
	 * @return
	 */
	public @NonNull PanamaSeasonalityRecord createPanamaSeasonalityRecord(final VesselGroupCanalParameters vgcp, int startDay, int startMonth, int startYear, int nb, int sb) {
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr.setVesselGroupCanalParameter(vgcp);
		psr.setStartDay(startDay);
		psr.setStartMonth(startMonth);
		psr.setStartYear(startYear);
		psr.setNorthboundWaitingDays(0);
		psr.setSouthboundWaitingDays(0);
		return psr;
	}
}
