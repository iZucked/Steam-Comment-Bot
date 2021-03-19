/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;

/***
 * Note: "run as: JUnit plug-in ITS test" otherwise will not inject all classes needed and will fail.
 */
@ExtendWith(ShiroRunner.class)
public class CharterInVesselPositioningFeeTests extends AbstractMicroTestCase {

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();
		return sb.getScenarioDataProvider();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity(ScenarioBuilder.DEFAULT_ENTITY_NAME);
	}

	private static final String BALLAST_BONUS_LUMP_SUM = "12345";
	private static final String POSITIONING_FEE = "99999";
	private long internalPnlWithRepositioningFee = 0;
	private long internalPnlWithoutRepositioningFee = 0;

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoOnVessel() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		// Create Ballast Bonus Charter Contract with entity, no repositioningFee, and lump sum bonus.
		final GenericCharterContract ballastBonusCharterContract = createBallastBonusCharterContract(port1, "");

		// Create the required basic elements
		final CharterInMarket charterInMarket_1 = createChartInMarket();
		charterInMarket_1.setGenericCharterContract(ballastBonusCharterContract);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(charterInMarket_1, 0, 0).build();

		evaluateTest(null, null, scenarioRunner -> {
			validateStartIdleLoadEvents(scenarioRunner);

			internalPnlWithoutRepositioningFee = calculateInternalPnl(scenarioRunner);
		});

		// Create Ballast Bonus Charter Contract with entity, repositioningFee, and lump sum bonus.
		final GenericCharterContract ballastBonusCharterContract2 = createBallastBonusCharterContract(port1, POSITIONING_FEE);

		charterInMarket_1.setGenericCharterContract(ballastBonusCharterContract2);

		evaluateTest(null, null, scenarioRunner -> {
			validateStartIdleLoadEvents(scenarioRunner);

			validateRepositioningFeeSet(scenarioRunner);

			internalPnlWithRepositioningFee = calculateInternalPnl(scenarioRunner);
		});

		long expectedDeltaPnl = -Long.parseLong(POSITIONING_FEE) * 1000;
		assertEquals(expectedDeltaPnl, internalPnlWithRepositioningFee - internalPnlWithoutRepositioningFee);
	}

	private long calculateInternalPnl(LNGScenarioRunner scenarioRunner) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

		@NonNull
		IModifiableSequences initialSequences = new ModifiableSequences(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences());

		long[] metrics = MicroTestUtils.evaluateMetrics(scenarioToOptimiserBridge.getDataTransformer(), initialSequences);

		long internalPnl = metrics[MetricType.PNL.ordinal()];

		return internalPnl;
	}

	private void validateRepositioningFeeSet(LNGScenarioRunner scenarioRunner) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		// Check spot index has been updated
		final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule();
		final Sequence sequence = schedule.getSequences().get(0);
		final StartEvent startEvent = (StartEvent) sequence.getEvents().get(0);

		long expectedPosFee = Long.parseLong(POSITIONING_FEE);
		assertEquals(expectedPosFee, startEvent.getRepositioningFee());
		assertEquals(-expectedPosFee, startEvent.getGroupProfitAndLoss().getProfitAndLossPreTax());
	}

	private GenericCharterContract createBallastBonusCharterContract(Port port, String positioningFee) {
		final GenericCharterContract ballastBonusCharterContract = commercialModelBuilder.createLumpSumBallastBonusCharterContract(port, BALLAST_BONUS_LUMP_SUM, positioningFee);
		return ballastBonusCharterContract;
	}

	private void validateStartIdleLoadEvents(LNGScenarioRunner scenarioRunner) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		// Check spot index has been updated
		final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

		final Sequence sequence = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule().getSequences().get(0);
		final StartEvent startEvent = (StartEvent) sequence.getEvents().get(0);
		final Idle idle = (Idle) sequence.getEvents().get(1);

		// SlotVist equates to LoadEvent in the GUI.
		final SlotVisit loadEvent = (SlotVisit) sequence.getEvents().get(2);
		Assertions.assertEquals(0, startEvent.getDuration());
		Assertions.assertEquals(0, idle.getDuration());

		// Start zone date time of all three events should match.
		Assertions.assertEquals(startEvent.getStart(), idle.getStart());
		Assertions.assertEquals(startEvent.getStart(), loadEvent.getStart());

		int slotDuration = loadEvent.getSlotAllocation().getSlot().getDuration();
		Assertions.assertEquals(24, slotDuration);
	}

	private CharterInMarket createChartInMarket() {
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setMaxSpeed(15.0);
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);
		charterInMarket_1.setNominal(false);
		return charterInMarket_1;
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testNoCargoOnVessel() throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		@NonNull
		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		@NonNull
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		final CharterInMarket charterInMarket = createChartInMarket();
		charterInMarket.setMinDuration(30);
		charterInMarket.setCharterInRate("10000");

		// Create Ballast Bonus Charter Contract with entity, repositioningFee, and lump sum bonus.
		final GenericCharterContract ballastBonusCharterContract2 = createBallastBonusCharterContract(port1, POSITIONING_FEE);
		charterInMarket.setGenericCharterContract(ballastBonusCharterContract2);

		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		evaluateTest(null, null, scenarioRunner -> {

			Schedule schedule = ScenarioModelUtil.getScheduleModel(scenarioRunner.getScenarioDataProvider()).getSchedule();

			validateNoCargo(charterInMarket, schedule);

			validatePnLAsExpected(schedule);

			// Check internal pnl.
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			@NonNull
			IModifiableSequences initialSequences = new ModifiableSequences(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences());

			long[] metrics = MicroTestUtils.evaluateMetrics(scenarioToOptimiserBridge.getDataTransformer(), initialSequences);

			long internalPnl = metrics[MetricType.PNL.ordinal()];

			Assertions.assertEquals(0, internalPnl);
		});
	}

	private void validateNoCargo(final CharterInMarket charterInMarket, Schedule schedule) {
		EList<Sequence> sequences = schedule.getSequences();
		boolean found = false;
		for (Sequence sequence : sequences) {
			if (charterInMarket.equals(sequence.getCharterInMarket())) {
				found = true;
				break;
			}
		}
		assert found == false;
	}

	private void validatePnLAsExpected(Schedule schedule) {
		// Check pnl is zero.
		Assertions.assertEquals(0L, ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule));
	}
}