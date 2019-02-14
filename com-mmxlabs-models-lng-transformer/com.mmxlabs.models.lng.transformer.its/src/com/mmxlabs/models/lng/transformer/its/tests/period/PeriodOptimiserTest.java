/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.period;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

@RunWith(value = ShiroRunner.class)
public class PeriodOptimiserTest {

	public static final boolean OUTPUT_SCENARIOS = false;

	/**
	 * Base case - all discharge slots should swap around for best P&L
	 */
	@Test
	public void testSimpleDischargeSwap() throws Exception {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();

		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());

		// Pass in null to disable opt range
		tester.optimise();

		Assert.assertEquals("20.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("5.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}

	@Test
	public void testSimpleDischargeSwapPeriod() throws Exception {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();

		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());

		// Pass in null to disable opt range
		tester.periodOptimise(LocalDate.of(2013, 6, 1), DateAndCurveHelper.createYearMonth(2015, 10));

		Assert.assertEquals("20.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("5.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}

	@Test
	public void testSimpleDischargeSwapPeriodMiddleOnly() throws Exception {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester(DefaultScenarioCreator.createLocalDateTime(2014, 7, 1, 0),
				DefaultScenarioCreator.createLocalDateTime(2014, 7, 30, 0), DefaultScenarioCreator.createLocalDateTime(2014, 9, 1, 0));

		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());

		// Pass in null to disable opt range
		tester.periodOptimise(LocalDate.of(2014, 9, 1), DateAndCurveHelper.createYearMonth(2014, 10));

		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}

	/**
	 * The discharges of the middle cargos should be free to swap
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSimpleDischargeSwapPeriodMiddleOnlyLoadsLocked() throws Exception {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();

		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());

		// Pass in null to disable opt range
		tester.periodOptimise(LocalDate.of(2014, 9, 1), DateAndCurveHelper.createYearMonth(2014, 10));

		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}

	/**
	 * The loads of the middle cargos should be free to swap. Note this will only work if the start valid followers fix has been implemented
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSimpleDischargeSwapPeriodMiddleOnlyDischargesLocked() throws Exception {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();
		tester.cargoA2.getSortedSlots().get(0).setPriceExpression("20.0");
		tester.cargoA2.getSortedSlots().get(1).setPriceExpression("20.0");
		tester.cargoA2.getSortedSlots().get(0).setMaxQuantity(10000);
		tester.cargoB1.getSortedSlots().get(0).setPriceExpression("20.0");
		tester.cargoB2.getSortedSlots().get(0).setPriceExpression("5.0");
		tester.cargoB2.getSortedSlots().get(0).setMaxQuantity(100000);
		tester.cargoB2.getSortedSlots().get(1).setPriceExpression("5.0");
		tester.cargoB3.getSortedSlots().get(0).setPriceExpression("20.0");

		// Fix loads to vessel
		tester.cargoA1.getSlots().get(0).getAllowedVessels().add(tester.vesselAvailabilities[1].getVessel());
		tester.cargoA2.getSlots().get(0).getAllowedVessels().add(tester.vesselAvailabilities[1].getVessel());
		tester.cargoA3.getSlots().get(0).getAllowedVessels().add(tester.vesselAvailabilities[1].getVessel());

		tester.cargoB1.getSlots().get(0).getAllowedVessels().add(tester.vesselAvailabilities[0].getVessel());
		tester.cargoB2.getSlots().get(0).getAllowedVessels().add(tester.vesselAvailabilities[0].getVessel());
		tester.cargoB3.getSlots().get(0).getAllowedVessels().add(tester.vesselAvailabilities[0].getVessel());

		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(0).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(0).getPriceExpression());

		// Pass in null to disable opt range
		tester.periodOptimise(LocalDate.of(2014, 8, 1), DateAndCurveHelper.createYearMonth(2014, 9));
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(0).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(0).getPriceExpression());
	}

	/**
	 * Small scenario creator. Create 6 cargoes, 3 pairs of equivalent slots with volume/pricing adjusted to ensure swapping discharge is best result.
	 * 
	 */
	public class PeriodOptimiserScenarioTester {

		public Port loadPort;
		public Port loadPort2;
		public Port dischargePort;
		public Port dischargePort2;

		public Cargo cargoA1, cargoA2, cargoA3;
		public Cargo cargoB1, cargoB2, cargoB3;

		public final VesselAvailability[] vesselAvailabilities;
		public IScenarioDataProvider scenarioDataProvider;

		public PeriodOptimiserScenarioTester() {
			this(DefaultScenarioCreator.createLocalDateTime(2014, 7, 1, 0), DefaultScenarioCreator.createLocalDateTime(2014, 8, 1, 0), DefaultScenarioCreator.createLocalDateTime(2014, 9, 1, 0));
		}

		public PeriodOptimiserScenarioTester(LocalDateTime dateA, LocalDateTime dateB, LocalDateTime dateC) {
			final CustomScenarioCreator csc = new CustomScenarioCreator(5.0f);
			final int fuelPrice = 1;
			final int smallQty = 50000;
			final int bigQty = 100000;

			loadPort = ScenarioTools.createPort("load-port");
			loadPort.getCapabilities().add(PortCapability.LOAD);

			loadPort2 = ScenarioTools.createPort("load-port2");
			loadPort2.getCapabilities().add(PortCapability.LOAD);

			dischargePort = ScenarioTools.createPort("discharge-port");
			dischargePort.getCapabilities().add(PortCapability.DISCHARGE);

			dischargePort2 = ScenarioTools.createPort("discharge-port2");
			dischargePort2.getCapabilities().add(PortCapability.DISCHARGE);

			loadPort.setDefaultWindowSize(1);
			loadPort2.setDefaultWindowSize(1);
			dischargePort.setDefaultWindowSize(1);
			dischargePort2.setDefaultWindowSize(1);

			loadPort.setCvValue(20.f);
			loadPort.setLoadDuration(1);
			loadPort2.setCvValue(20.f);
			loadPort2.setLoadDuration(1);
			dischargePort.setDischargeDuration(1);
			dischargePort2.setDischargeDuration(1);

			// a list of ports to use in the scenario
			final Port[] ports = new Port[] { loadPort, dischargePort, loadPort2, dischargePort2 };

			// Add the ports, and set the distances.
			setEquidistantDistances(csc, ports, 100);

			// create a few vessels and add them to the list of vessels created.
			// createVessels creates and adds the vessels to the scenario.
			vesselAvailabilities = new VesselAvailability[2];
			vesselAvailabilities[0] = csc.addVesselSimple("classOne", 1, fuelPrice, 25, 1000000, 10, 10, 0, 500, false)[0];
			vesselAvailabilities[1] = csc.addVesselSimple("classTwo", 1, fuelPrice, 25, 1000000, 10, 10, 0, 500, false)[0];

			// create two different cargoes
			cargoA1 = csc.addCargo("Cargo-A1", loadPort, dischargePort, 5, 5.0f, 20.f, dateA, 50);
			cargoA2 = csc.addCargo("Cargo-A2", loadPort, dischargePort, 5, 5.0f, 20.f, dateB, 50);
			cargoA3 = csc.addCargo("Cargo-A3", loadPort, dischargePort, 5, 5.0f, 20.f, dateC, 50);

			cargoB1 = csc.addCargo("Cargo-B1", loadPort2, dischargePort2, 5, 20.0f, 20.f, dateA, 50);
			cargoB2 = csc.addCargo("Cargo-B2", loadPort2, dischargePort2, 5, 20.0f, 20.f, dateB, 50);
			cargoB3 = csc.addCargo("Cargo-B3", loadPort2, dischargePort2, 5, 20.0f, 20.f, dateC, 50);

			// // make sure they can be rewired
			cargoA1.setAllowRewiring(true);
			cargoA2.setAllowRewiring(true);
			cargoA3.setAllowRewiring(true);

			cargoB1.setAllowRewiring(true);
			cargoB2.setAllowRewiring(true);
			cargoB3.setAllowRewiring(true);

			// Set current assignment
			cargoA1.setVesselAssignmentType(vesselAvailabilities[0]);
			cargoA2.setVesselAssignmentType(vesselAvailabilities[0]);
			cargoA3.setVesselAssignmentType(vesselAvailabilities[0]);

			cargoB1.setVesselAssignmentType(vesselAvailabilities[1]);
			cargoB2.setVesselAssignmentType(vesselAvailabilities[1]);
			cargoB3.setVesselAssignmentType(vesselAvailabilities[1]);

			// Fix loads to vessel
			cargoA1.getSlots().get(0).getAllowedVessels().add(vesselAvailabilities[0].getVessel());
			cargoA2.getSlots().get(0).getAllowedVessels().add(vesselAvailabilities[0].getVessel());
			cargoA3.getSlots().get(0).getAllowedVessels().add(vesselAvailabilities[0].getVessel());

			cargoB1.getSlots().get(0).getAllowedVessels().add(vesselAvailabilities[1].getVessel());
			cargoB2.getSlots().get(0).getAllowedVessels().add(vesselAvailabilities[1].getVessel());
			cargoB3.getSlots().get(0).getAllowedVessels().add(vesselAvailabilities[1].getVessel());

			// Set volumes
			cargoA1.getSlots().get(0).setMaxQuantity(bigQty);
			cargoA2.getSlots().get(0).setMaxQuantity(bigQty);
			cargoA3.getSlots().get(0).setMaxQuantity(bigQty);

			cargoB1.getSlots().get(0).setMaxQuantity(smallQty);
			cargoB2.getSlots().get(0).setMaxQuantity(smallQty);
			cargoB3.getSlots().get(0).setMaxQuantity(smallQty);

			cargoA1.getSlots().get(1).setMaxQuantity(bigQty);
			cargoA2.getSlots().get(1).setMaxQuantity(bigQty);
			cargoA3.getSlots().get(1).setMaxQuantity(bigQty);

			cargoB1.getSlots().get(1).setMaxQuantity(bigQty);
			cargoB2.getSlots().get(1).setMaxQuantity(bigQty);
			cargoB3.getSlots().get(1).setMaxQuantity(bigQty);

			// Prep cooldown to avoid validation warnings if scenario used in real app
			csc.setupCooldown(20.0);

			// build and run the scenario.
			scenarioDataProvider = csc.getScenarioDataProvider();
		}

		public void setEquidistantDistances(final CustomScenarioCreator csc, final Port[] ports, int distance) {

			for (final Port portX : ports) {
				for (final Port portY : ports) {
					if (!portX.equals(portY)) {
						csc.addPorts(portX, portY, distance);
					}
				}
			}
		}

		public void optimise() throws Exception {
			final CleanableExecutorService executorService = LNGScenarioChainBuilder.createExecutorService(1);
			try {

				final OptimisationPlan plan = getPlan();

				LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
						.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
						.withOptimiseHint() //
						.withOptimisationPlan(plan)//
						.buildDefaultRunner();

				runner.evaluateInitialState();
				if (OUTPUT_SCENARIOS) {
					save(runner.getScenarioRunner().getScenarioDataProvider(), "c:/temp/scenario1.lingo");
				}

				runner.run(true);

				if (OUTPUT_SCENARIOS) {
					save(runner.getScenarioRunner().getScenarioDataProvider(), "c:/temp/scenario2.lingo");
				}
			} catch (final Exception er) {
				// this exception should not occur
				// Assert.fail("Scenario runner failed to initialise scenario");
				throw er;
			} finally {
				executorService.shutdownNow();
			}
		}

		private OptimisationPlan getPlan() {
			final OptimisationPlan plan = LNGScenarioRunnerUtils.createDefaultOptimisationPlan();
			for (OptimisationStage stage : plan.getStages()) {
				if (stage instanceof LocalSearchOptimisationStage) {
					LocalSearchOptimisationStage lsoStage = (LocalSearchOptimisationStage) stage;
					lsoStage.getAnnealingSettings().setIterations(10_000);
				}
			}
			return plan;
		}

		public void periodOptimise(final LocalDate start, final YearMonth end) throws Exception {
			final CleanableExecutorService executorService = LNGScenarioChainBuilder.createExecutorService(1);

			try {

				final OptimisationPlan plan = getPlan();

				plan.getUserSettings().setPeriodStartDate(start);
				plan.getUserSettings().setPeriodEnd(end);

				LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
						.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
						.withOptimiseHint() //
						.withOptimisationPlan(plan)//
						.buildDefaultRunner();

				runner.evaluateInitialState();

				if (OUTPUT_SCENARIOS) {
					save(runner.getScenarioRunner().getScenarioDataProvider(), "c:/temp/scenario1p.lingo");
				}
				runner.run(true);

				if (OUTPUT_SCENARIOS) {
					save(runner.getScenarioRunner().getScenarioDataProvider(), "c:/temp/scenario2p.lingo");
				}
			} catch (final Exception er) {
				er.printStackTrace();
				// this exception should not occur
				// Assert.fail("Scenario runner failed to initialise scenario: " + er.getMessage());
				throw er;
			} finally {
				executorService.shutdownNow();
			}
		}
	}

	@SuppressWarnings("unused")
	private void save(final IScenarioDataProvider scenarioDataProvider, final String path) throws IOException {
		ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, new File(path));
	}
}
