/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.util.CostModelBuilder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
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

@SuppressWarnings({ "unused", "null" })
public class MultipleBaseFuelTestCase extends AbstractLegacyMicroTestCase {

	final int ROUNDING_EPSILON = 100;
	private Vessel vessel;
	private VesselCharter vesselCharter1;
	private Cargo cargo1;
	private Port portA;
	private Port portB;
	private VesselStateAttributes attrLaden;
	private VesselStateAttributes attrBal;
	private BaseFuel baseFuel;
	private BaseFuel idleBaseFuel;
	private BaseFuel pilotLightBaseFuel;
	private BaseFuel inPortBaseFuel;
	private PricingModelBuilder pricingModelBuilder;
	private CostModelBuilder costModelBuilder;

	private final boolean writeScenario = false;
 
	@BeforeEach
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

		costModelBuilder.createOrUpdateBaseFuelCost(baseFuel, "100");
		costModelBuilder.createOrUpdateBaseFuelCost(idleBaseFuel, "3");
		costModelBuilder.createOrUpdateBaseFuelCost(pilotLightBaseFuel, "70");
		costModelBuilder.createOrUpdateBaseFuelCost(inPortBaseFuel, "20");

		vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 7, 0, 0), LocalDateTime.of(2015, 12, 4, 13, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 05, 16, 0, 0, 0)).build();

		portA = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		portB = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "90") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 17), portB, null, entity, "90") //
				.build() //
				.withVesselAssignment(vesselCharter1, 1) // -1 is nominal
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
			Assertions.assertNotNull(schedule);

			final Vessel vessel = schedule.getSequences().get(0).getVesselCharter().getVessel();

			final BaseFuel scenarioBaseFuel = vessel.getBaseFuel();
			final BaseFuel scenarioIdleBaseFuel = vessel.getIdleBaseFuel();
			final BaseFuel scenarioPilotLightBaseFuel = vessel.getPilotLightBaseFuel();
			final BaseFuel scenarioInPortBaseFuel = vessel.getInPortBaseFuel();

			Assertions.assertEquals(baseFuel, scenarioBaseFuel);
			Assertions.assertEquals(idleBaseFuel, scenarioIdleBaseFuel);
			Assertions.assertEquals(pilotLightBaseFuel, scenarioPilotLightBaseFuel);
			Assertions.assertEquals(inPortBaseFuel, scenarioInPortBaseFuel);

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation("L1", schedule);
			Assertions.assertNotNull(cargoAllocation);
			final SimpleCargoAllocation SCA = new SimpleCargoAllocation(cargoAllocation);

			// System.out.println("LD: " + Arrays.toString(SCA.getLoadAllocation().getSlotVisit().getFuels().toArray()));
			// System.out.println("LJ: " + Arrays.toString(SCA.getLadenLeg().getFuels().toArray()));
			// System.out.println("LI: " + Arrays.toString(SCA.getLadenIdle().getFuels().toArray()));
			// System.out.println("DS: " + Arrays.toString(SCA.getDischargeAllocation().getSlotVisit().getFuels().toArray()));
			// System.out.println("DJ: " + Arrays.toString(SCA.getBallastLeg().getFuels().toArray()));
			// System.out.println("DI: " + Arrays.toString(SCA.getBallastIdle().getFuels().toArray()));

			// Load Event
			final EList<FuelQuantity> loadFuels = SCA.getLoadAllocation().getSlotVisit().getFuels();
			Assertions.assertEquals(1, loadFuels.size());
			final FuelQuantity loadFuel = loadFuels.get(0);
			Assertions.assertEquals(inPortBaseFuel, loadFuel.getBaseFuel());
			Assertions.assertEquals(20 * loadFuel.getAmounts().get(0).getQuantity(), loadFuel.getCost(), ROUNDING_EPSILON);

			// Laden Journey Event
			final EList<FuelQuantity> ladenJourneyFuels = SCA.getLadenLeg().getFuels();
			Assertions.assertEquals(2, ladenJourneyFuels.size());
			FuelQuantity ladenJourneyFuel = null;
			FuelAmount ladenJourneyFuel_MT = null;
			FuelQuantity ladenJourneyNBO = null;
			FuelAmount ladenJourneyNBO_M3 = null;
			for (final FuelQuantity entry : ladenJourneyFuels) {
				if (entry.getBaseFuel() != null) {
					ladenJourneyFuel = entry;
					for (final FuelAmount fa : entry.getAmounts()) {
						if (fa.getUnit() == FuelUnit.MT) {
							ladenJourneyFuel_MT = fa;
						}
					}
				} else {
					ladenJourneyNBO = entry;
					for (final FuelAmount fa : entry.getAmounts()) {
						if (fa.getUnit() == FuelUnit.M3) {
							ladenJourneyNBO_M3 = fa;
						}
					}
				}

			}
			Assertions.assertEquals(pilotLightBaseFuel, ladenJourneyFuel.getBaseFuel());
			Assertions.assertEquals(null, ladenJourneyNBO.getBaseFuel());
			Assertions.assertEquals(70 * ladenJourneyFuel_MT.getQuantity(), ladenJourneyFuel.getCost(), ROUNDING_EPSILON);
			Assertions.assertEquals(90 * 22.8 * ladenJourneyNBO_M3.getQuantity(), ladenJourneyNBO.getCost(), ROUNDING_EPSILON);

			// Laden Idle Event
			final EList<FuelQuantity> ladenIdleFuels = SCA.getLadenIdle().getFuels();
			Assertions.assertEquals(2, ladenIdleFuels.size());
			final BaseFuel ladenIdleFuel_0 = ladenIdleFuels.get(0).getBaseFuel();
			final BaseFuel ladenIdleFuel_1 = ladenIdleFuels.get(1).getBaseFuel();
			Assertions.assertNotEquals(ladenIdleFuel_0, ladenIdleFuel_1);
			Assertions.assertTrue(pilotLightBaseFuel == ladenIdleFuel_0 || null == ladenIdleFuel_0);
			Assertions.assertTrue(pilotLightBaseFuel == ladenIdleFuel_1 || null == ladenIdleFuel_1);

			// Discharge Event
			final EList<FuelQuantity> dischargeFuels = SCA.getDischargeAllocation().getSlotVisit().getFuels();
			Assertions.assertEquals(1, dischargeFuels.size());
			final FuelQuantity dischargeFuel = dischargeFuels.get(0);
			Assertions.assertEquals(inPortBaseFuel, dischargeFuel.getBaseFuel());
			Assertions.assertEquals(20 * dischargeFuel.getAmounts().get(0).getQuantity(), dischargeFuel.getCost(), ROUNDING_EPSILON);

			// Ballast Journey Event
			final EList<FuelQuantity> ballastJourneyFuels = SCA.getBallastLeg().getFuels();
			Assertions.assertEquals(1, ballastJourneyFuels.size());
			final FuelQuantity ballastJourneyFuel = ballastJourneyFuels.get(0);
			Assertions.assertEquals(baseFuel, ballastJourneyFuel.getBaseFuel());
			Assertions.assertEquals(100 * ballastJourneyFuel.getAmounts().get(0).getQuantity(), ballastJourneyFuel.getCost(), ROUNDING_EPSILON);

			// Ballast Idle Event
			final EList<FuelQuantity> ballastIdleFuels = SCA.getBallastIdle().getFuels();
			Assertions.assertEquals(1, ballastIdleFuels.size());
			final FuelQuantity ballastIdleFuel = ballastIdleFuels.get(0);
			Assertions.assertEquals(idleBaseFuel, ballastIdleFuel.getBaseFuel());
			Assertions.assertEquals(3 * ballastIdleFuel.getAmounts().get(0).getQuantity(), ballastIdleFuel.getCost(), ROUNDING_EPSILON);

		});

	}

}
