/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.inject.Injector;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@ExtendWith(ShiroRunner.class)
public class PanamaVesselGroupWaitingBookingTests extends AbstractMicroTestCase {

	private static final String TEST_VESSEL_GROUP1 = "TestVesselGroup1";
	private static final String TEST_VESSEL_GROUP2 = "TestVesselGroup2";

	private static final int DEFAULT_WAITING_DAYS = 10;
	private static final int VG1_WAITING_DAYS = 5;
	private static final int VG2_WAITING_DAYS = 8;
 
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void priceBasedTWSVesselGroupWaitingDaysTest() {
		runTestVesselGroupWaitingDaysTest(true);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void pnlBasedTWSVesselGroupWaitingDaysTest() {
		runTestVesselGroupWaitingDaysTest(false);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void priceBasedTWSDefaultWaitingDaysTest() {
		runTestDefaultWaitingDaysTest(true);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void pnlBasedTWSDefaultWaitingDaysTest() {
		runTestDefaultWaitingDaysTest(false);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void priceBasedVesselBookedTest() {
		runTestBookedByVesselTest(true);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void pnlBasedVesselBookedTest() {
		runTestBookedByVesselTest(false);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void priceBasedVesselGroupBookedTest() {
		runTestBookedByVesselGroupTest(true, InternalDataConstants.REF_VESSEL_STEAM_145, TEST_VESSEL_GROUP1);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void pnlBasedVesselGroupBookedTest() {
		runTestBookedByVesselGroupTest(false, InternalDataConstants.REF_VESSEL_STEAM_145, TEST_VESSEL_GROUP1);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void priceBasedVesselGroup2BookedTest() {
		runTestBookedByVesselGroupTest(true, InternalDataConstants.REF_VESSEL_STEAM_138, TEST_VESSEL_GROUP2);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void pnlBasedVesselGroup2BookedTest() {
		runTestBookedByVesselGroupTest(false, InternalDataConstants.REF_VESSEL_STEAM_138, TEST_VESSEL_GROUP2);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void priceBasedMultiVesselBookedTest() {
		runTestMultiBookedByVesselTest(true);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void pnlBasedMultiVesselBookedTest() {
		runTestMultiBookedByVesselTest(false);
	}
	
	
	@Override
	public IScenarioDataProvider importReferenceData() throws Exception {

		@NonNull
		final IScenarioDataProvider scenarioDataProvider = super.importReferenceData();

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		@NonNull
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		cargoModel.setCanalBookings(canalBookings);

		final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioDataProvider);

		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		final Route panama = potentialPanama.get();

		portModel.getPorts().stream().filter(p -> {
			return "Colon".equals(p.getName()) || "Balboa".equals(p.getName());
		}).forEach(p -> {
			final EntryPoint ep = PortFactory.eINSTANCE.createEntryPoint();
			ep.setPort(p);
			if (p.getName().equals("Colon")) {
				ep.setName("North");
				panama.setNorthEntrance(ep);
			} else {
				ep.setName("South");
				panama.setSouthEntrance(ep);
			}
		});

		
		final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		scenarioModel.setPromptPeriodStart(LocalDate.of(2017, 6, 1));
		scenarioModel.setPromptPeriodEnd(LocalDate.of(2017, 9, 1));

		return scenarioDataProvider;
	}
	
	@BeforeEach
	public void constructor() throws Exception {
		
		super.constructor();
		
		CanalBookings canalBookings = this.lngScenarioModel.getCargoModel().getCanalBookings();

		VesselGroup vg = FleetFactory.eINSTANCE.createVesselGroup();
		vg.setName(TEST_VESSEL_GROUP1);
		vg.getVessels().add(getVessel(InternalDataConstants.REF_VESSEL_STEAM_145));
		this.lngScenarioModel.getReferenceModel().getFleetModel().getVesselGroups().add(vg);

		VesselGroup vg2 = FleetFactory.eINSTANCE.createVesselGroup();
		vg2.setName(TEST_VESSEL_GROUP2);
		vg2.getVessels().add(getVessel(InternalDataConstants.REF_VESSEL_TFDE_160));
		vg2.getVessels().add(getVessel(InternalDataConstants.REF_VESSEL_STEAM_138));
		this.lngScenarioModel.getReferenceModel().getFleetModel().getVesselGroups().add(vg2);
		
		VesselGroupCanalParameters testGroupParams = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		testGroupParams.getVesselGroup().add(vg);
		canalBookings.getVesselGroupCanalParameters().add(testGroupParams);
		final PanamaSeasonalityRecord psr1 = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr1.setNorthboundWaitingDays(VG1_WAITING_DAYS);
		psr1.setSouthboundWaitingDays(VG1_WAITING_DAYS);
		psr1.setVesselGroupCanalParameter(testGroupParams);
		canalBookings.getPanamaSeasonalityRecords().add(psr1);
		
		VesselGroupCanalParameters testGroupParams2 = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		testGroupParams2.getVesselGroup().add(vg2);
		canalBookings.getVesselGroupCanalParameters().add(testGroupParams2);
		final PanamaSeasonalityRecord psr2 = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		psr2.setNorthboundWaitingDays(VG2_WAITING_DAYS);
		psr2.setSouthboundWaitingDays(VG2_WAITING_DAYS);
		psr2.setVesselGroupCanalParameter(testGroupParams2);
		canalBookings.getPanamaSeasonalityRecords().add(psr2);
		
		VesselGroupCanalParameters defaultParams = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		//Default params group has no vessel group or vessels set.
		canalBookings.getVesselGroupCanalParameters().add(defaultParams);
		final PanamaSeasonalityRecord dpsr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		dpsr.setNorthboundWaitingDays(10);
		dpsr.setSouthboundWaitingDays(10);
		dpsr.setVesselGroupCanalParameter(defaultParams);
		canalBookings.getPanamaSeasonalityRecords().add(dpsr);
		
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();
	}
	
	private Cargo createFobDesCargo(int num, final VesselCharter vesselCharter) {	
		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		return createFobDesCargo(num, vesselCharter, loadDate);
	}

	private Cargo createFobDesCargo(int num, final VesselCharter vesselCharter, final LocalDateTime loadDate) {
		
		@NonNull
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);

		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_QUINTERO);

		final LocalDateTime dischargeDate = loadDate.plusDays(13);
		
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase(String.format("L-%d", num), loadDate.toLocalDate(), loadPort, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale(String.format("D-%d", num), dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(5, TimePeriod.DAYS) //
				.build() //
				//
				.withVesselAssignment(vesselCharter, 1) //
				.build();
		return cargo;
	}
	
	private VesselGroup getVesselGroup(String vesselGroupName) {
		return this.lngScenarioModel.getReferenceModel().getFleetModel().getVesselGroups().stream().filter(vg -> vg.getName().equals(vesselGroupName)).findFirst().get();
	}
	
	private Vessel getVessel(String vesselName) {
		final Vessel vessel = fleetModelFinder.findVessel(vesselName);
		vessel.setMaxSpeed(16.0);
		return vessel;
	}
	
	private VesselCharter getVesselCharter(String vesselName) {
		final Vessel vessel = getVessel(vesselName);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withCharterRate("100000") //Make expensive to make schedule longer.
				.build();
		return vesselCharter;
	}

	/**
	 * Test we get the vessel group waiting days.
	 * @param twsType - true for price based time windows scheduler, false for PNL based TWS.
	 */
	private void runTestVesselGroupWaitingDaysTest(boolean twsType) {

		final VesselCharter vesselCharter = getVesselCharter(InternalDataConstants.REF_VESSEL_STEAM_145);
		
		final Cargo cargo = createFobDesCargo(1, vesselCharter);

		testNoBookingWaitingDaysRespected(twsType, vesselCharter, cargo, VG1_WAITING_DAYS);
	}
	
	/**
	 * Test we get the vessel group waiting days.
	 * @param twsType - true for price based time windows scheduler, false for PNL based TWS.
	 */
	private void runTestBookedByVesselTest(boolean twsType) {

		final VesselCharter vesselCharter = getVesselCharter(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Cargo cargo = createFobDesCargo(1, vesselCharter);

		CanalBookingSlot booking = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 8), vesselCharter.getVessel());

		testSinglePanamaBookingVoyage(twsType, vesselCharter, cargo, booking);
	}

	/**
	 * Test we get the vessel group waiting days.
	 * @param twsType - true for price based time windows scheduler, false for PNL based TWS.
	 * @param testVesselGroup2 
	 * @param string 
	 */
	private void runTestMultiBookedByVesselTest(boolean twsType) {

		final VesselCharter vesselCharter = getVesselCharter(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Cargo cargo1 = createFobDesCargo(1, vesselCharter, LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0));
		final Cargo cargo2 = createFobDesCargo(1, vesselCharter, LocalDateTime.of(2017, Month.JULY, 1, 0, 0, 0));
		final Cargo cargo3 = createFobDesCargo(1, vesselCharter, LocalDateTime.of(2017, Month.AUGUST, 1, 0, 0, 0));

		CanalBookingSlot booking1 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 8), vesselCharter.getVessel());
		CanalBookingSlot booking2 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.SOUTHSIDE, LocalDate.of(2017, Month.JUNE, 24), vesselCharter.getVessel());
		CanalBookingSlot booking3 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JULY, 8), vesselCharter.getVessel());
		CanalBookingSlot booking4 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.SOUTHSIDE, LocalDate.of(2017, Month.JULY, 24), vesselCharter.getVessel());
		CanalBookingSlot booking5 = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.AUGUST, 8), vesselCharter.getVessel());

		// All discharge window of final cargo should be respected, as bookings for each Panama leg.
		assertTrue(cargo3.getSlots().size() == 2);
		Slot<?> ds = cargo3.getSlots().get(cargo3.getSlots().size() - 1);
		assertTrue(ds instanceof DischargeSlot);
		ZonedDateTime dsZdt = ds.getSchedulingTimeWindow().getStart();
		int toleranceHours = ds.getWindowFlex() + ds.getSchedulingTimeWindow().getSizeInHours();
		
		evaluateWithLSOTest(scenarioRunner -> {

			// Run the scheduler and check the last discharge time is as expected.
			List<IRouteOptionBooking> oBookingsUsed = testDischargeZDTStartTime(dsZdt, toleranceHours, twsType, vesselCharter, scenarioRunner, cargo1, cargo2, cargo3);

			// All bookings should be utilised.
			checkBookingsUsed(scenarioRunner, oBookingsUsed, booking1, booking2, booking3, booking4, booking5);
		});
	}
	
	/**
	 * Test we get the vessel group waiting days.
	 * @param twsType - true for price based time windows scheduler, false for PNL based TWS.
	 */
	private void runTestBookedByVesselGroupTest(boolean twsType, String vesselName, String vesselGroup) {

		final VesselCharter vesselCharter = getVesselCharter(vesselName);
		final Cargo cargo = createFobDesCargo(1, vesselCharter);
		final VesselGroup vg = this.getVesselGroup(vesselGroup);
		
		CanalBookingSlot booking = cargoModelBuilder.makeCanalBooking(RouteOption.PANAMA, CanalEntry.NORTHSIDE, LocalDate.of(2017, Month.JUNE, 8), vg);

		testSinglePanamaBookingVoyage(twsType, vesselCharter, cargo, booking);
	}

	private void testSinglePanamaBookingVoyage(boolean twsType, final VesselCharter vesselCharter, final Cargo cargo, CanalBookingSlot booking) {
		assertTrue(cargo.getSlots().size() == 2);
		Slot<?> ds = cargo.getSlots().get(cargo.getSlots().size() - 1);
		assertTrue(ds instanceof DischargeSlot);
		ZonedDateTime dsZdt = ds.getSchedulingTimeWindow().getStart();
		int toleranceHours = ds.getWindowFlex() + ds.getSchedulingTimeWindow().getSizeInHours();
	
		evaluateWithLSOTest(scenarioRunner -> {
			
			List<IRouteOptionBooking> oBookingsUsed = testDischargeZDTStartTime(dsZdt, toleranceHours, twsType, vesselCharter, scenarioRunner, cargo);
			
			checkBookingsUsed(scenarioRunner, oBookingsUsed, booking);
		});
	}

	private void checkBookingsUsed(LNGScenarioRunner scenarioRunner, List<IRouteOptionBooking> oBookingsUsed, CanalBookingSlot...bookings) {
		boolean present = false;
		for (CanalBookingSlot booking : bookings) {
			for (IRouteOptionBooking oBookingUsed : oBookingsUsed) {
				CanalBookingSlot bookingUsed = scenarioRunner.getScenarioToOptimiserBridge().getDataTransformer().getModelEntityMap().getModelObjectNullChecked(oBookingUsed, CanalBookingSlot.class);
				if (Objects.equals(booking, bookingUsed)) {
					present = true;
				}
			}
		}
		assertTrue(present);
	}
	
	int getTravelTimeInHoursAtMaxSpeedViaPanama(LNGScenarioRunner scenarioRunner, Vessel vessel, Port from, Port to) {
		double maxSpeedKnots = vessel.getMaxSpeed();
		IDistanceProvider distanceProvider = scenarioRunner.getScenarioToOptimiserBridge().getDataTransformer().getInjector().getInstance(IDistanceProvider.class);
		ModelEntityMap objectConverter = scenarioRunner.getScenarioToOptimiserBridge().getDataTransformer().getModelEntityMap();
		IVessel oVessel = objectConverter.getOptimiserObjectNullChecked(vessel, IVessel.class);
		IPort oFrom = objectConverter.getOptimiserObjectNullChecked(from, IPort.class);
		IPort oTo = objectConverter.getOptimiserObjectNullChecked(to, IPort.class);
		int distance = distanceProvider.getDistance(ERouteOption.PANAMA, oFrom, oTo, oVessel);
		assertTrue(distance != Integer.MAX_VALUE);
		return (int)Math.ceil(distance / maxSpeedKnots);
	}
	
	/**
	 * Run the test with default waiting days vessel (vessel different in this one).
	 * @param twsType - true for price based time windows scheduler, false for PNL based TWS.
	 */
	private void runTestDefaultWaitingDaysTest(boolean twsType) {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		//Start anywhere.
		final VesselCharter vesselCharter = this.getVesselCharter(InternalDataConstants.REF_VESSEL_STEAM_150);

		final Cargo cargo = createFobDesCargo(1, vesselCharter);
				
		testNoBookingWaitingDaysRespected(twsType, vesselCharter, cargo, DEFAULT_WAITING_DAYS);
	}

	private void testNoBookingWaitingDaysRespected(boolean twsType, final VesselCharter vesselCharter, final Cargo cargo, int waitingDays) {
		// Compute time from start port to end port via Panama at max vessel speed.
		assertTrue(cargo.getSlots().size() == 2);

		Slot<?> ls = cargo.getSlots().get(0);
		assertTrue(ls instanceof LoadSlot);
		Port loadPort = ls.getPort();
		
		Slot<?> ds = cargo.getSlots().get(cargo.getSlots().size() - 1);
		assertTrue(ds instanceof DischargeSlot);
		Port discPort = ds.getPort();
		
		Vessel vessel = vesselCharter.getVessel();
			
		evaluateWithLSOTest(scenarioRunner -> {
			
			int hoursToPanama = this.getTravelTimeInHoursAtMaxSpeedViaPanama(scenarioRunner, vessel, loadPort, discPort);
			
			// Add on the waiting time for queuing to go through.
			int totalTravelTime = hoursToPanama + (24 * waitingDays);
			
			// Should reach destination later or equal to end of load + total.
			ZonedDateTime lsZdt = ls.getSchedulingTimeWindow().getEnd().plusHours(ls.getDuration());
			ZonedDateTime expectedZdt = lsZdt.plusHours(totalTravelTime);
			int slackInSchedule = Hours.between(expectedZdt, ds.getSchedulingTimeWindow().getEndWithFlex());
			int toleranceHours = (slackInSchedule > 0 ? slackInSchedule : 24);

			List<IRouteOptionBooking> bookingsUsed = testDischargeZDTStartTime(expectedZdt, toleranceHours, twsType, vesselCharter, scenarioRunner, cargo);
			assertTrue(bookingsUsed.size() == 0);
		});
	}

	private List<IRouteOptionBooking> testDischargeZDTStartTime(ZonedDateTime dischargeTime, int toleranceHours, boolean twsType, final VesselCharter vesselCharter, LNGScenarioRunner scenarioRunner, final Cargo ...cargoes) {
		DateAndCurveHelper dtHelper = scenarioRunner.getScenarioToOptimiserBridge().getDataTransformer().getInjector().getInstance(DateAndCurveHelper.class);
		int dischargeTimeOptimiserHours = dtHelper.convertTime(dischargeTime);
		return testDischargeStartTime(dischargeTimeOptimiserHours, toleranceHours, twsType, vesselCharter, scenarioRunner, cargoes);
	}
	
	private List<IRouteOptionBooking> testDischargeStartTime(int expectedDischargeStartTime, int toleranceHours, boolean twsType, final VesselCharter vesselCharter, LNGScenarioRunner scenarioRunner, final Cargo ...cargoes) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		
		scenarioRunner.evaluateInitialState();
		
		List<IRouteOptionBooking> bookingsUsed = new ArrayList<>();
		
		Slot<?> lastDS = cargoes[cargoes.length-1].getSlots().get(1);
//		SlotAllocation ds = ca.getSlotAllocations().get(1);		
		
		final Injector injector = MicroTestUtils.createEvaluationInjector(scenarioToOptimiserBridge.getDataTransformer());
		try {
			final ISequencesManipulator sequencesManipulator = injector.getInstance(ISequencesManipulator.class);
			@NonNull
			final IModifiableSequences manipulatedSequences = sequencesManipulator
					.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), vesselCharter, cargoes));

			final TimeWindowScheduler scheduler = injector.getInstance(TimeWindowScheduler.class);
			scheduler.setUseCanalBasedWindowTrimming(true);
			scheduler.setUsePriceBasedWindowTrimming(twsType);
			scheduler.setUsePNLBasedWindowTrimming(!twsType);
			//final ScheduledTimeWindows schedule = scheduler.calculateTrimmedWindows(manipulatedSequences);
			final Map<IResource, List<IPortTimesRecord>> records = scheduler.schedule(manipulatedSequences);
			//final Map<IResource, List<IPortTimeWindowsRecord>> records = schedule.getTrimmedTimeWindowsMap();

			final IResource r0 = manipulatedSequences.getResources().get(0);
			List<IPortTimesRecord> portTimesRecords = records.get(r0);
			IPortTimesRecord last = null;
			IPortSlot lastSlot = null;
			
			//find the last discharge slot.
			for (IPortTimesRecord ptr : portTimesRecords) {	
				if (ptr.getSlots().size() > 0) {
					for (IPortSlot slot : ptr.getSlots()) {
						if (ptr.getRouteOptionBooking(slot) != null) {
							bookingsUsed.add(ptr.getRouteOptionBooking(slot));
						}
						if (slot.getId().endsWith(lastDS.getName())) {
							last = ptr;
							lastSlot = slot;
						}
					}
				}
			}
			
			//Get the time for it.
			if (lastSlot != null) {
				int time = last.getSlotTime(lastSlot);	
				assertTrue(time >= expectedDischargeStartTime && time <= expectedDischargeStartTime + toleranceHours);
			}
			else {
				System.err.println("Bug in test. Check cargoes set up correctly.");
				assertTrue(false);
			}
		}
		catch (Exception ioe) {
			ioe.printStackTrace(System.err);
		}		
		
		return bookingsUsed;
	}
}
