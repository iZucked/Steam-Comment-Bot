/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.util.CostModelBuilder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class MultipleBaseFuelTestCase extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();

	final int ROUNDING_EPSILON = 100;
	Vessel vessel;
	VesselAvailability vesselAvailability1;
	Cargo cargo1;
	Port portA;
	Port portB;
	VesselStateAttributes attrLaden;
	VesselStateAttributes attrBal;
	BaseFuel baseFuel;
	BaseFuel idleBaseFuel;
	BaseFuel pilotLightBaseFuel;
	BaseFuel inPortBaseFuel;
	PricingModelBuilder pricingModelBuilder;
	CostModelBuilder costModelBuilder;

	private final boolean writeScenario = false;

	@BeforeClass
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterClass
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	@Before
	@Override
	public void constructor() throws Exception {
		super.constructor();

		portModelBuilder.setAllExistingPortsToUTC();

		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		final FleetModelBuilder fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();

		baseFuel = fleetModelBuilder.createBaseFuel("BASE", 13);
		idleBaseFuel = fleetModelBuilder.createBaseFuel("IDLE", 3);
		inPortBaseFuel = fleetModelBuilder.createBaseFuel("PORT", 10_000);
		pilotLightBaseFuel = fleetModelBuilder.createBaseFuel("PILOT", 2);

		vessel.setBaseFuel(baseFuel);
		vessel.setIdleBaseFuel(idleBaseFuel);
		vessel.setInPortBaseFuel(inPortBaseFuel);
		vessel.setPilotLightBaseFuel(pilotLightBaseFuel);

		attrBal = vessel.getBallastAttributes();
		attrBal.setInPortNBORate(0);
		attrBal.setInPortBaseRate(1_000);
		attrBal.setIdleBaseRate(50);
		vessel.setBallastAttributes(attrBal);

		attrLaden = vessel.getLadenAttributes();
		attrLaden.setInPortNBORate(0);
		attrLaden.setInPortBaseRate(1_000);
		attrLaden.setIdleBaseRate(50);
		vessel.setLadenAttributes(attrLaden);

		pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
		costModelBuilder = scenarioModelBuilder.getCostModelBuilder();

		costModelBuilder.createBaseFuelCost(baseFuel, pricingModelBuilder.createBaseFuelExpressionIndex("BASE", 100));
		costModelBuilder.createBaseFuelCost(idleBaseFuel, pricingModelBuilder.createBaseFuelExpressionIndex("IDLE", 3));
		costModelBuilder.createBaseFuelCost(pilotLightBaseFuel, pricingModelBuilder.createBaseFuelExpressionIndex("PILOT", 70));
		costModelBuilder.createBaseFuelCost(inPortBaseFuel, pricingModelBuilder.createBaseFuelExpressionIndex("PORT", 20));

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 7, 0, 0), LocalDateTime.of(2015, 12, 4, 13, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 05, 16, 0, 0, 0)).build();

		portA = portFinder.findPort("Point Fortin");

		portB = portFinder.findPort("Dominion Cove Point LNG");

		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "90") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 17), portB, null, entity, "90") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		vessel.setPilotLightRate(100.00);
	}

	@Test
	public void normalVoyage() {

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final IScenarioDataProvider optimiserDataProvider = scenarioToOptimiserBridge.getOptimiserScenario();

			if (writeScenario) {
				try {
					MicroCaseUtils.storeToFile(optimiserDataProvider, "MultiBase");
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			final Schedule schedule = ScenarioModelUtil.getScheduleModel(optimiserDataProvider).getSchedule();
			Assert.assertNotNull(schedule);

			final Vessel vessel = schedule.getSequences().get(0).getVesselAvailability().getVessel();

			final BaseFuel scenarioBaseFuel = vessel.getBaseFuel();
			final BaseFuel scenarioIdleBaseFuel = vessel.getIdleBaseFuel();
			final BaseFuel scenarioPilotLightBaseFuel = vessel.getPilotLightBaseFuel();
			final BaseFuel scenarioInPortBaseFuel = vessel.getInPortBaseFuel();

			Assert.assertEquals(baseFuel, scenarioBaseFuel);
			Assert.assertEquals(idleBaseFuel, scenarioIdleBaseFuel);
			Assert.assertEquals(pilotLightBaseFuel, scenarioPilotLightBaseFuel);
			Assert.assertEquals(inPortBaseFuel, scenarioInPortBaseFuel);

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation("L1", schedule);
			Assert.assertNotNull(cargoAllocation);
			final SimpleCargoAllocation SCA = new SimpleCargoAllocation(cargoAllocation);

			// System.out.println("LD: " + Arrays.toString(SCA.getLoadAllocation().getSlotVisit().getFuels().toArray()));
			// System.out.println("LJ: " + Arrays.toString(SCA.getLadenLeg().getFuels().toArray()));
			// System.out.println("LI: " + Arrays.toString(SCA.getLadenIdle().getFuels().toArray()));
			// System.out.println("DS: " + Arrays.toString(SCA.getDischargeAllocation().getSlotVisit().getFuels().toArray()));
			// System.out.println("DJ: " + Arrays.toString(SCA.getBallastLeg().getFuels().toArray()));
			// System.out.println("DI: " + Arrays.toString(SCA.getBallastIdle().getFuels().toArray()));

			// Load Event
			final EList<FuelQuantity> loadFuels = SCA.getLoadAllocation().getSlotVisit().getFuels();
			Assert.assertEquals(1, loadFuels.size());
			final FuelQuantity loadFuel = loadFuels.get(0);
			Assert.assertEquals(inPortBaseFuel, loadFuel.getBaseFuel());
			Assert.assertEquals(20 * loadFuel.getAmounts().get(0).getQuantity(), loadFuel.getCost(), ROUNDING_EPSILON);

			// Laden Journey Event
			final EList<FuelQuantity> ladenJourneyFuels = SCA.getLadenLeg().getFuels();
			Assert.assertEquals(2, ladenJourneyFuels.size());
			FuelQuantity ladenJourneyFuel = null;
			FuelAmount ladenJourneyFuel_MT = null;
			FuelQuantity ladenJourneyNBO = null;
			FuelAmount ladenJourneyNBO_M3 = null;
			for (FuelQuantity entry : ladenJourneyFuels) {
				if (entry.getBaseFuel() != null) {
					ladenJourneyFuel = entry;
					for (FuelAmount fa : entry.getAmounts()) {
						if (fa.getUnit() == FuelUnit.MT) {
							ladenJourneyFuel_MT = fa;
						}
					}
				} else {
					ladenJourneyNBO = entry;
					for (FuelAmount fa : entry.getAmounts()) {
						if (fa.getUnit() == FuelUnit.M3) {
							ladenJourneyNBO_M3 = fa;
						}
					}
				}

			}
			Assert.assertEquals(pilotLightBaseFuel, ladenJourneyFuel.getBaseFuel());
			Assert.assertEquals(null, ladenJourneyNBO.getBaseFuel());
			Assert.assertEquals(70 * ladenJourneyFuel_MT.getQuantity(), ladenJourneyFuel.getCost(), ROUNDING_EPSILON);
			Assert.assertEquals(90 * 22.8 * ladenJourneyNBO_M3.getQuantity(), ladenJourneyNBO.getCost(), ROUNDING_EPSILON);

			// Laden Idle Event
			final EList<FuelQuantity> ladenIdleFuels = SCA.getLadenIdle().getFuels();
			Assert.assertEquals(1, ladenIdleFuels.size());
			final FuelQuantity ladenIdleFuel = ladenIdleFuels.get(0);
			Assert.assertEquals(null, ladenIdleFuel.getBaseFuel());

			// Discharge Event
			final EList<FuelQuantity> dischargeFuels = SCA.getDischargeAllocation().getSlotVisit().getFuels();
			Assert.assertEquals(1, dischargeFuels.size());
			final FuelQuantity dischargeFuel = dischargeFuels.get(0);
			Assert.assertEquals(inPortBaseFuel, dischargeFuel.getBaseFuel());
			Assert.assertEquals(20 * dischargeFuel.getAmounts().get(0).getQuantity(), dischargeFuel.getCost(), ROUNDING_EPSILON);

			// Ballast Journey Event
			final EList<FuelQuantity> ballastJourneyFuels = SCA.getBallastLeg().getFuels();
			Assert.assertEquals(1, ballastJourneyFuels.size());
			final FuelQuantity ballastJourneyFuel = ballastJourneyFuels.get(0);
			Assert.assertEquals(baseFuel, ballastJourneyFuel.getBaseFuel());
			Assert.assertEquals(100 * ballastJourneyFuel.getAmounts().get(0).getQuantity(), ballastJourneyFuel.getCost(), ROUNDING_EPSILON);

			// Ballast Idle Event
			final EList<FuelQuantity> ballastIdleFuels = SCA.getBallastIdle().getFuels();
			Assert.assertEquals(1, ballastIdleFuels.size());
			final FuelQuantity ballastIdleFuel = ballastIdleFuels.get(0);
			Assert.assertEquals(idleBaseFuel, ballastIdleFuel.getBaseFuel());
			Assert.assertEquals(3 * ballastIdleFuel.getAmounts().get(0).getQuantity(), ballastIdleFuel.getCost(), ROUNDING_EPSILON);

		});

	}

}
