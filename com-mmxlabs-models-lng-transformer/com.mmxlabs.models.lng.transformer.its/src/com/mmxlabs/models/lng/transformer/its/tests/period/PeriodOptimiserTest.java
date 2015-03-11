/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.period;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.PeriodScenarioRunner;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.PortCapability;

public class PeriodOptimiserTest {

	/**
	 * Base case - all discharge slots should swap around for best P&L
	 */
	@Test
	@Ignore("Disabled in 3.5, requires 4-opt-2 fix")
	public void testSimpleDischargeSwap() {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();

		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());

		// Pass in null to disable opt range
		tester.optimise(null, null);

		Assert.assertEquals("20.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("5.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}

	@Test
	@Ignore("requires 4-opt-2 fix")
	public void testSimpleDischargeSwapPeriod() throws IOException {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();
		
		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());
		
		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
		
		// Pass in null to disable opt range
		tester.periodOptimise(DateAndCurveHelper.createDate(2013, 6, 15, 0, "UTC"), DateAndCurveHelper.createDate(2015, 10, 15, 0, "UTC"));
		
		Assert.assertEquals("20.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA3.getSlots().get(1).getPriceExpression());

		Assert.assertEquals("5.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}
	
	@Test
	@Ignore("requires 4-opt-2 fix")
	public void testSimpleDischargeSwapPeriodMiddleOnly() throws IOException {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();
		
		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());
		
		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
		
		// Pass in null to disable opt range
		tester.periodOptimise(DateAndCurveHelper.createDate(2014, 7, 15, 0, "UTC"), DateAndCurveHelper.createDate(2014, 8, 15, 0, "UTC"));
		
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());
		
		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}
	
	/**
	 * The discharges of the middle cargos should be free to swap
	 * @throws IOException 
	 */
	@Test
	@Ignore("requires 4-opt-2 fix")
	public void testSimpleDischargeSwapPeriodMiddleOnlyLoadsLocked() throws IOException {
		final PeriodOptimiserScenarioTester tester = new PeriodOptimiserScenarioTester();
		
		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());
		
		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
		
		// Pass in null to disable opt range
		tester.periodOptimise(DateAndCurveHelper.createDate(2014, 8, 2, 0, "UTC"), DateAndCurveHelper.createDate(2014, 8, 15, 0, "UTC"));
		
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(1).getPriceExpression());
		
		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(1).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(1).getPriceExpression());
	}
	
	/**
	 * The loads of the middle cargos should be free to swap. Note this will only work if the start valid followers fix has been implemented
	 * @throws IOException 
	 */
	@Test
	@Ignore("requires 4-opt-2 fix")
	public void testSimpleDischargeSwapPeriodMiddleOnlyDischargesLocked() throws IOException {
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
		tester.cargoA1.getSlots().get(0).getAllowedVessels().add(tester.vessels[1]);
		tester.cargoA2.getSlots().get(0).getAllowedVessels().add(tester.vessels[1]);
		tester.cargoA3.getSlots().get(0).getAllowedVessels().add(tester.vessels[1]);

		tester.cargoB1.getSlots().get(0).getAllowedVessels().add(tester.vessels[0]);
		tester.cargoB2.getSlots().get(0).getAllowedVessels().add(tester.vessels[0]);
		tester.cargoB3.getSlots().get(0).getAllowedVessels().add(tester.vessels[0]);


		// Check the prices are correct rather than specific slot instances.
		Assert.assertEquals("5.0", tester.cargoA1.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoA2.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoA3.getSlots().get(0).getPriceExpression());
		
		Assert.assertEquals("20.0", tester.cargoB1.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("5.0", tester.cargoB2.getSlots().get(0).getPriceExpression());
		Assert.assertEquals("20.0", tester.cargoB3.getSlots().get(0).getPriceExpression());
		
		// Pass in null to disable opt range
		tester.periodOptimise(DateAndCurveHelper.createDate(2014, 7, 30, 0, "UTC"), DateAndCurveHelper.createDate(2014, 8, 2, 0, "UTC"));
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

		public final Vessel[] vessels;
		public LNGScenarioModel scenario;

		public PeriodOptimiserScenarioTester() {
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
			vessels = csc.addVesselSimple("classOne", 2, fuelPrice, 25, 1000000, 10, 10, 0, 500, false);
			
			final VesselAvailability[] vesselAvailabilities = new VesselAvailability[vessels.length];
			for (int i = 0; i < vessels.length; ++i) {
				for (VesselAvailability vesselAvailability : csc.getPortfolioModel().getCargoModel().getVesselAvailabilities()) {
					if (vesselAvailability.getVessel() == vessels[i]) {
						vesselAvailabilities[i] = vesselAvailability;
					}
				}
			}

			// create two different cargoes
			cargoA1 = csc.addCargo("Cargo-A1", loadPort, dischargePort, 5, 5.0f, 20.f, csc.createDate(2014, 7, 1), 50);
			cargoA2 = csc.addCargo("Cargo-A2", loadPort, dischargePort, 5, 5.0f, 20.f, csc.createDate(2014, 8, 1), 50);
			cargoA3 = csc.addCargo("Cargo-A3", loadPort, dischargePort, 5, 5.0f, 20.f, csc.createDate(2014, 9, 1), 50);

			cargoB1 = csc.addCargo("Cargo-B1", loadPort2, dischargePort2, 5, 20.0f, 20.f, csc.createDate(2014, 7, 1), 50);
			cargoB2 = csc.addCargo("Cargo-B2", loadPort2, dischargePort2, 5, 20.0f, 20.f, csc.createDate(2014, 8, 1), 50);
			cargoB3 = csc.addCargo("Cargo-B3", loadPort2, dischargePort2, 5, 20.0f, 20.f, csc.createDate(2014, 9, 1), 50);

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
			cargoA1.getSlots().get(0).getAllowedVessels().add(vessels[0]);
			cargoA2.getSlots().get(0).getAllowedVessels().add(vessels[0]);
			cargoA3.getSlots().get(0).getAllowedVessels().add(vessels[0]);

			cargoB1.getSlots().get(0).getAllowedVessels().add(vessels[1]);
			cargoB2.getSlots().get(0).getAllowedVessels().add(vessels[1]);
			cargoB3.getSlots().get(0).getAllowedVessels().add(vessels[1]);

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
			scenario = csc.buildScenario();
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

		public void optimise(final Date start, final Date end) {
			final ScenarioRunner runner = new ScenarioRunner((LNGScenarioModel) scenario);
			try {

				final OptimiserSettings settings = ScenarioUtils.createDefaultSettings();

				settings.getRange().setOptimiseAfter(start);
				settings.getRange().setOptimiseBefore(end);

				runner.init(settings);

				save(runner.getScenario(), "c:/temp/scenario1.lingo");
				runner.run();
				runner.updateScenario();

				save(runner.getScenario(), "c:/temp/scenario2.lingo");
			} catch (final Exception er) {
				// this exception should not occur
				Assert.fail("Scenario runner failed to initialise scenario");
			}
		}
		
		public void periodOptimise(final Date start, final Date end) throws IOException {
			final PeriodScenarioRunner runner = new PeriodScenarioRunner((LNGScenarioModel) scenario);
			try {
				
				final OptimiserSettings settings = ScenarioUtils.createDefaultSettings();
				
				settings.getRange().setOptimiseAfter(start);
				settings.getRange().setOptimiseBefore(end);
				
				try {
					runner.init(settings);
				} catch (IncompleteScenarioException e) {
					e.printStackTrace();
					Assert.fail("Scenario runner failed to initialise scenario: " + e.getMessage());
				}
				
//				save(runner.getScenario(), "c:/temp/scenario1.lingo");
				runner.run();
				runner.updateScenario();
				
//				save(runner.getScenario(), "c:/temp/scenario2.lingo");
			} catch (final Exception er) {
				er.printStackTrace();
				// this exception should not occur
				Assert.fail("Scenario runner failed to initialise scenario: " + er.getMessage());
			}
		}
	}

	private void save(final LNGScenarioModel scenario, final String path) throws IOException {
		final String context = "com.mmxlabs.lingo";
		final int version = 26;
		ScenarioTools.storeToFile(EcoreUtil.copy(scenario), new File(path), context, version);
	}
}
