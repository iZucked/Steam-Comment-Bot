package com.mmxlabs.models.lng.transformer.period;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;

public class PeriodTransformerTest {

	@Test
	public void createPeriodRecordTest_EmptySettings() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final OptimiserSettings optimiserSettings = ParametersFactory.eINSTANCE.createOptimiserSettings();

		final PeriodRecord periodRecord = transformer.createPeriodRecord(optimiserSettings);

		Assert.assertNotNull(periodRecord);
		Assert.assertNull(periodRecord.lowerCutoff);
		Assert.assertNull(periodRecord.lowerBoundary);
		Assert.assertNull(periodRecord.upperBoundary);
		Assert.assertNull(periodRecord.upperCutoff);

	}

	@Test
	public void createPeriodRecordTest_OptimiseAfterOnly() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final OptimiserSettings optimiserSettings = ParametersFactory.eINSTANCE.createOptimiserSettings();
		final OptimisationRange range = ParametersFactory.eINSTANCE.createOptimisationRange();

		optimiserSettings.setRange(range);
		range.setOptimiseAfter(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(optimiserSettings);

		Assert.assertNotNull(periodRecord);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), periodRecord.lowerCutoff);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1), periodRecord.lowerBoundary);
		Assert.assertNull(periodRecord.upperBoundary);
		Assert.assertNull(periodRecord.upperCutoff);
	}

	@Test
	public void createPeriodRecordTest_OptimiseBeforeOnly() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final OptimiserSettings optimiserSettings = ParametersFactory.eINSTANCE.createOptimiserSettings();
		final OptimisationRange range = ParametersFactory.eINSTANCE.createOptimisationRange();

		optimiserSettings.setRange(range);
		range.setOptimiseBefore(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(optimiserSettings);

		Assert.assertNotNull(periodRecord);
		Assert.assertNull(periodRecord.lowerCutoff);
		Assert.assertNull(periodRecord.lowerBoundary);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1), periodRecord.upperBoundary);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), periodRecord.upperCutoff);
	}

	@Test
	public void createPeriodRecordTest() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final OptimiserSettings optimiserSettings = ParametersFactory.eINSTANCE.createOptimiserSettings();
		final OptimisationRange range = ParametersFactory.eINSTANCE.createOptimisationRange();

		optimiserSettings.setRange(range);
		range.setOptimiseAfter(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
		range.setOptimiseBefore(PeriodTestUtils.createDate(2014, Calendar.JULY, 1));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(optimiserSettings);

		Assert.assertNotNull(periodRecord);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), periodRecord.lowerCutoff);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1), periodRecord.lowerBoundary);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 1), periodRecord.upperBoundary);
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1), periodRecord.upperCutoff);
	}

	@Test
	public void updateVesselAvailabilitiesTest1() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// Vessel before period
		final Vessel vessel1 = PeriodTestUtils.createVessel(scenarioModel, "Vessel1");
		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel1);
		vesselAvailability1.setEndBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v1-cargo1", port1, PeriodTestUtils.createDate(2013, Calendar.NOVEMBER, 1), port2,
					PeriodTestUtils.createDate(2013, Calendar.DECEMBER, 1));
			c1.setAssignment(vessel1);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel1, c1));
		}

		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		// No change expected - vesselAvailability1
		Assert.assertTrue(vesselAvailability1.getStartAt().isEmpty());
		Assert.assertTrue(vesselAvailability1.getEndAt().isEmpty());
		Assert.assertNull(vesselAvailability1.getStartAfter());
		Assert.assertNull(vesselAvailability1.getStartBy());
		Assert.assertNull(vesselAvailability1.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), vesselAvailability1.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest2() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// Vessel across lower bounds
		final Vessel vessel2 = PeriodTestUtils.createVessel(scenarioModel, "Vessel2");
		final VesselAvailability vesselAvailability2 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel2);
		vesselAvailability2.getStartAt().add(port1);
		vesselAvailability2.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability2.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability2.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));
		vesselAvailability2.setEndBy(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v2-cargo1", port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), port2,
					PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v2-cargo2", port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));

			c1.setAssignment(vessel2);
			c2.setAssignment(vessel2);

			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)));
			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));

			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));
			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel2, c1, c2));

			mapCargoOrEventToVesselAvailability.put(c1, vesselAvailability2);
			mapCargoOrEventToVesselAvailability.put(c2, vesselAvailability2);
		}

		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		// Changed
		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability2.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), vesselAvailability2.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), vesselAvailability2.getStartBy());
		// Unchanged
		Assert.assertTrue(vesselAvailability2.getEndAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1), vesselAvailability2.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1), vesselAvailability2.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest3() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel completely in
		final Vessel vessel3 = PeriodTestUtils.createVessel(scenarioModel, "Vessel3");
		final VesselAvailability vesselAvailability3 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel3);
		vesselAvailability3.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));
		vesselAvailability3.setStartBy(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));
		vesselAvailability3.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1));
		vesselAvailability3.setEndBy(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v3-cargo1", port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1), port2,
					PeriodTestUtils.createDate(2014, Calendar.JUNE, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v3-cargo2", port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1));
			c1.setAssignment(vessel3);
			c2.setAssignment(vessel3);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));

			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1)));
			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel3, c1, c2));

			mapCargoOrEventToVesselAvailability.put(c1, vesselAvailability3);
			mapCargoOrEventToVesselAvailability.put(c2, vesselAvailability3);

		}
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		// No change expected
		Assert.assertTrue(vesselAvailability3.getStartAt().isEmpty());
		Assert.assertTrue(vesselAvailability3.getEndAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MAY, 1), vesselAvailability3.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MAY, 1), vesselAvailability3.getStartBy());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1), vesselAvailability3.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1), vesselAvailability3.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest4() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel across both bounds
		final Vessel vessel4 = PeriodTestUtils.createVessel(scenarioModel, "Vessel4");
		final VesselAvailability vesselAvailability4 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel4);
		vesselAvailability4.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability4.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability4.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		vesselAvailability4.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo1", port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), port2,
					PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo2", port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));
			final Cargo c3 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo3", port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.JUNE, 1));
			final Cargo c4 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo4", port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1));
			final Cargo c5 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo5", port3, PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
			final Cargo c6 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo6", port3, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

			c1.setAssignment(vessel4);
			c2.setAssignment(vessel4);
			c3.setAssignment(vessel4);
			c4.setAssignment(vessel4);
			c5.setAssignment(vessel4);
			c6.setAssignment(vessel4);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)));

			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));

			startConditionMap.put(c3, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));
			endConditionMap.put(c3, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));

			startConditionMap.put(c4, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1)));
			endConditionMap.put(c4, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));

			startConditionMap.put(c5, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1)));
			endConditionMap.put(c5, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1)));

			startConditionMap.put(c6, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)));
			endConditionMap.put(c6, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel4, c1, c2, c3, c4, c5, c6));

			mapCargoOrEventToVesselAvailability.put(c1, vesselAvailability4);
			mapCargoOrEventToVesselAvailability.put(c2, vesselAvailability4);
			mapCargoOrEventToVesselAvailability.put(c3, vesselAvailability4);
			mapCargoOrEventToVesselAvailability.put(c4, vesselAvailability4);
			mapCargoOrEventToVesselAvailability.put(c5, vesselAvailability4);
			mapCargoOrEventToVesselAvailability.put(c6, vesselAvailability4);

		}
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability4.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), vesselAvailability4.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), vesselAvailability4.getStartBy());
		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability4.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), vesselAvailability4.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), vesselAvailability4.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest5() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel across both bounds
		final Vessel vessel5 = PeriodTestUtils.createVessel(scenarioModel, "Vessel5");
		final VesselAvailability vesselAvailability5 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel5);
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo1", port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), port2,
					PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo2", port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));
			final Cargo c3 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo3", port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.JUNE, 1));
			final Cargo c4 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo4", port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1));
			final Cargo c5 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo5", port3, PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
			final Cargo c6 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo6", port3, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

			c1.setAssignment(vessel5);
			c2.setAssignment(vessel5);
			c3.setAssignment(vessel5);
			c4.setAssignment(vessel5);
			c5.setAssignment(vessel5);
			c6.setAssignment(vessel5);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)));

			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));

			startConditionMap.put(c3, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));
			endConditionMap.put(c3, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));

			startConditionMap.put(c4, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1)));
			endConditionMap.put(c4, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));

			startConditionMap.put(c5, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1)));
			endConditionMap.put(c5, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1)));

			startConditionMap.put(c6, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)));
			endConditionMap.put(c6, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel5, c1, c2, c3, c4, c5, c6));

			mapCargoOrEventToVesselAvailability.put(c1, vesselAvailability5);
			mapCargoOrEventToVesselAvailability.put(c2, vesselAvailability5);
			mapCargoOrEventToVesselAvailability.put(c3, vesselAvailability5);
			mapCargoOrEventToVesselAvailability.put(c4, vesselAvailability5);
			mapCargoOrEventToVesselAvailability.put(c5, vesselAvailability5);
			mapCargoOrEventToVesselAvailability.put(c6, vesselAvailability5);

		}
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability5.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), vesselAvailability5.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), vesselAvailability5.getStartBy());
		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability5.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), vesselAvailability5.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), vesselAvailability5.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest6() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel across upper bound
		final Vessel vessel6 = PeriodTestUtils.createVessel(scenarioModel, "Vessel3");
		final VesselAvailability vesselAvailability6 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel6);
		vesselAvailability6.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		vesselAvailability6.setStartBy(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		vesselAvailability6.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		vesselAvailability6.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v6-cargo1", port1, PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1), port2,
					PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v6-cargo2", port3, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), port4,
					PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
			c1.setAssignment(vessel6);
			c2.setAssignment(vessel6);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1)));

			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)));
			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel6, c1, c2));

			mapCargoOrEventToVesselAvailability.put(c1, vesselAvailability6);
			mapCargoOrEventToVesselAvailability.put(c2, vesselAvailability6);

		}
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		// No change expected
		Assert.assertTrue(vesselAvailability6.getStartAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1), vesselAvailability6.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1), vesselAvailability6.getStartBy());

		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability6.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), vesselAvailability6.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1), vesselAvailability6.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest7() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel after period
		final Vessel vessel7 = PeriodTestUtils.createVessel(scenarioModel, "Vessel7");
		final VesselAvailability vesselAvailability7 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel7);
		vesselAvailability7.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		// Cargoes with 1 month intervals
		// Initial Vessel availability 0.5 month intervals
		// Some with open bounds

		// Need vessel event and cooldown also

		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		// No change expected - vesselAvailability7
		Assert.assertTrue(vesselAvailability7.getStartAt().isEmpty());
		Assert.assertTrue(vesselAvailability7.getEndAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1), vesselAvailability7.getStartAfter());
		Assert.assertNull(vesselAvailability7.getStartBy());
		Assert.assertNull(vesselAvailability7.getEndAfter());
		Assert.assertNull(vesselAvailability7.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest_VesselEvent1() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		{
			final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1), 30);
			final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1), 30);
			event1.setAssignment(vessel);
			event2.setAssignment(vessel);

			startConditionMap.put(event1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1)));
			endConditionMap.put(event1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			startConditionMap.put(event2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(event2, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel, event1, event2));

			mapCargoOrEventToVesselAvailability.put(event1, vesselAvailability);
			mapCargoOrEventToVesselAvailability.put(event2, vesselAvailability);

		}

		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		Assert.assertTrue(vesselAvailability.getStartAt().isEmpty());
		Assert.assertTrue(vesselAvailability.getEndAt().isEmpty());
		Assert.assertNull(vesselAvailability.getStartAfter());
		Assert.assertNull(vesselAvailability.getStartBy());
		Assert.assertNull(vesselAvailability.getEndAfter());
		Assert.assertNull(vesselAvailability.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest_VesselEvent2() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MAY, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JUNE, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		{
			final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1), 30);
			final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1), 30);
			event1.setAssignment(vessel);
			event2.setAssignment(vessel);

			startConditionMap.put(event1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1)));
			endConditionMap.put(event1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			startConditionMap.put(event2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(event2, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel, event1, event2));

			mapCargoOrEventToVesselAvailability.put(event1, vesselAvailability);
			mapCargoOrEventToVesselAvailability.put(event2, vesselAvailability);

		}

		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		Assert.assertEquals(Collections.singletonList(port2), vesselAvailability.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JUNE, 1), vesselAvailability.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JUNE, 1), vesselAvailability.getStartBy());

		Assert.assertTrue(vesselAvailability.getEndAt().isEmpty());
		Assert.assertNull(vesselAvailability.getEndAfter());
		Assert.assertNull(vesselAvailability.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest_VesselEvent3() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JANUARY, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.MAY, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Port port1 = PeriodTestUtils.createPort(scenarioModel, "port1");
		final Port port2 = PeriodTestUtils.createPort(scenarioModel, "port2");
		final Port port3 = PeriodTestUtils.createPort(scenarioModel, "port3");
		final Port port4 = PeriodTestUtils.createPort(scenarioModel, "port4");

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();
		final Map<AssignableElement, VesselAvailability> mapCargoOrEventToVesselAvailability = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		{
			final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1), 30);
			final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1), 30);
			event1.setAssignment(vessel);
			event2.setAssignment(vessel);

			startConditionMap.put(event1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1)));
			endConditionMap.put(event1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			startConditionMap.put(event2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(event2, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vessel, event1, event2));

			mapCargoOrEventToVesselAvailability.put(event1, vesselAvailability);
			mapCargoOrEventToVesselAvailability.put(event2, vesselAvailability);

		}

		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, mapCargoOrEventToVesselAvailability);

		Assert.assertTrue(vesselAvailability.getStartAt().isEmpty());
		Assert.assertNull(vesselAvailability.getStartAfter());
		Assert.assertNull(vesselAvailability.getStartBy());

		Assert.assertEquals(Collections.singletonList(port2), vesselAvailability.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1), vesselAvailability.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1), vesselAvailability.getEndBy());
	}

	@Test
	public void updateStartConditionsTest_RealInclusionChecker() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final AssignableElement assignedObject1 = Mockito.mock(AssignableElement.class);
		final PortVisit portVisit1 = Mockito.mock(PortVisit.class);
		final Port port1 = Mockito.mock(Port.class, "port1");
		Mockito.when(portVisit1.getPort()).thenReturn(port1);
		Mockito.when(portVisit1.getHeelAtEnd()).thenReturn(10000);
		Mockito.when(portVisit1.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(portVisit1.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final AssignableElement assignedObject2 = Mockito.mock(AssignableElement.class);
		final PortVisit portVisit2 = Mockito.mock(PortVisit.class);
		final Port port2 = Mockito.mock(Port.class, "port2");
		Mockito.when(portVisit2.getPort()).thenReturn(port2);
		Mockito.when(portVisit2.getHeelAtEnd()).thenReturn(20000);
		Mockito.when(portVisit2.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 18));
		Mockito.when(portVisit2.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 19));

		final AssignableElement assignedObject3 = Mockito.mock(AssignableElement.class);
		final PortVisit portVisit3 = Mockito.mock(PortVisit.class);
		final Port port3 = Mockito.mock(Port.class, "port3");
		Mockito.when(portVisit3.getPort()).thenReturn(port3);
		Mockito.when(portVisit3.getHeelAtStart()).thenReturn(30000);
		Mockito.when(portVisit3.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 28));
		Mockito.when(portVisit3.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 29));

		final Map<AssignableElement, PortVisit> startConditionMap = Mockito.mock(Map.class);
		Mockito.when(startConditionMap.get(assignedObject1)).thenReturn(portVisit1);
		Mockito.when(startConditionMap.get(assignedObject2)).thenReturn(portVisit2);
		Mockito.when(startConditionMap.get(assignedObject3)).thenReturn(portVisit3);

		{
			final VesselAvailability vesselAvailability1 = CargoFactory.eINSTANCE.createVesselAvailability();
			vesselAvailability1.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());

			transformer.updateStartConditions(vesselAvailability1, assignedObject1, startConditionMap);
			transformer.updateStartConditions(vesselAvailability1, assignedObject2, startConditionMap);
			transformer.updateStartConditions(vesselAvailability1, assignedObject3, startConditionMap);

			Assert.assertEquals(Collections.singletonList(port3), vesselAvailability1.getStartAt());
			Assert.assertEquals(30000, vesselAvailability1.getStartHeel().getVolumeAvailable());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 28), vesselAvailability1.getStartBy());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 28), vesselAvailability1.getStartAfter());
		}
		// Same again, but reverse order. Should yield same result as before.
		{
			final VesselAvailability vesselAvailability2 = CargoFactory.eINSTANCE.createVesselAvailability();
			vesselAvailability2.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());

			transformer.updateStartConditions(vesselAvailability2, assignedObject3, startConditionMap);
			transformer.updateStartConditions(vesselAvailability2, assignedObject2, startConditionMap);
			transformer.updateStartConditions(vesselAvailability2, assignedObject1, startConditionMap);

			Assert.assertEquals(Collections.singletonList(port3), vesselAvailability2.getStartAt());
			Assert.assertEquals(30000, vesselAvailability2.getStartHeel().getVolumeAvailable());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 28), vesselAvailability2.getStartBy());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 28), vesselAvailability2.getStartAfter());
		}
	}

	@Test
	public void updateEndConditionsTest_RealInclusionChecker() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final AssignableElement assignedObject1 = Mockito.mock(AssignableElement.class);
		final PortVisit portVisit1 = Mockito.mock(PortVisit.class);
		final Port port1 = Mockito.mock(Port.class, "port1");
		Mockito.when(portVisit1.getPort()).thenReturn(port1);
		Mockito.when(portVisit1.getHeelAtStart()).thenReturn(10000);
		Mockito.when(portVisit1.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(portVisit1.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final AssignableElement assignedObject2 = Mockito.mock(AssignableElement.class);
		final PortVisit portVisit2 = Mockito.mock(PortVisit.class);
		final Port port2 = Mockito.mock(Port.class, "port2");
		Mockito.when(portVisit2.getPort()).thenReturn(port2);
		Mockito.when(portVisit2.getHeelAtStart()).thenReturn(20000);
		Mockito.when(portVisit2.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 18));
		Mockito.when(portVisit2.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 19));

		final AssignableElement assignedObject3 = Mockito.mock(AssignableElement.class);
		final PortVisit portVisit3 = Mockito.mock(PortVisit.class);
		final Port port3 = Mockito.mock(Port.class, "port3");
		Mockito.when(portVisit3.getPort()).thenReturn(port3);
		Mockito.when(portVisit3.getHeelAtStart()).thenReturn(30000);
		Mockito.when(portVisit3.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 28));
		Mockito.when(portVisit3.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 29));

		final Map<AssignableElement, PortVisit> endConditionMap = Mockito.mock(Map.class);
		Mockito.when(endConditionMap.get(assignedObject1)).thenReturn(portVisit1);
		Mockito.when(endConditionMap.get(assignedObject2)).thenReturn(portVisit2);
		Mockito.when(endConditionMap.get(assignedObject3)).thenReturn(portVisit3);

		{
			final VesselAvailability vesselAvailability1 = CargoFactory.eINSTANCE.createVesselAvailability();
			vesselAvailability1.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());

			transformer.updateEndConditions(vesselAvailability1, assignedObject1, endConditionMap);
			transformer.updateEndConditions(vesselAvailability1, assignedObject2, endConditionMap);
			transformer.updateEndConditions(vesselAvailability1, assignedObject3, endConditionMap);

			Assert.assertEquals(Collections.singletonList(port1), vesselAvailability1.getEndAt());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 8), vesselAvailability1.getEndBy());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 8), vesselAvailability1.getEndAfter());
		}
		// Same again, but reverse order. Should yield same result as before.
		{
			final VesselAvailability vesselAvailability2 = CargoFactory.eINSTANCE.createVesselAvailability();
			vesselAvailability2.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());

			transformer.updateEndConditions(vesselAvailability2, assignedObject3, endConditionMap);
			transformer.updateEndConditions(vesselAvailability2, assignedObject2, endConditionMap);
			transformer.updateEndConditions(vesselAvailability2, assignedObject1, endConditionMap);

			Assert.assertEquals(Collections.singletonList(port1), vesselAvailability2.getEndAt());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 8), vesselAvailability2.getEndBy());
			Assert.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 8), vesselAvailability2.getEndAfter());
		}
	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo1() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, "cargo", loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, "cargo", copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(cargo));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(dischargeSlot));

		// Removed from copy
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().isEmpty());
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().isEmpty());

		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(cargo);
		Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
		Mockito.verify(mapping).registerRemovedOriginal(dischargeSlot);

	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo2() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, "cargo", loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, "cargo", copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(cargo));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(dischargeSlot));

		// Still in copy
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));

		// Check copy flags changed
		Assert.assertTrue(copyCargo.isLocked());
		Assert.assertFalse(copyCargo.isAllowRewiring());

		// but not original
		Assert.assertFalse(cargo.isLocked());
		Assert.assertTrue(cargo.isAllowRewiring());

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo3() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JULY, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, "cargo", loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JULY, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, "cargo", copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(cargo));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(dischargeSlot));

		// Still in copy
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));

		// Check copy flags unchanged
		Assert.assertFalse(copyCargo.isLocked());
		Assert.assertTrue(copyCargo.isAllowRewiring());

		Assert.assertFalse(cargo.isLocked());
		Assert.assertTrue(cargo.isAllowRewiring());

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo4() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, "cargo", loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, "cargo", copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(cargo));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(dischargeSlot));

		// Still in copy
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));

		// Check copy flags changed
		Assert.assertTrue(copyCargo.isLocked());
		Assert.assertFalse(copyCargo.isAllowRewiring());

		// but not original
		Assert.assertFalse(cargo.isLocked());
		Assert.assertTrue(cargo.isAllowRewiring());

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo5() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, "cargo", loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, "cargo", copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(cargo));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(dischargeSlot));

		// Removed from copy
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().isEmpty());
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().isEmpty());

		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(cargo);
		Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
		Mockito.verify(mapping).registerRemovedOriginal(dischargeSlot);

	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo6() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, "cargo", loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, "cargo", copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(cargo));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(dischargeSlot));

		// Still in copy
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));

		// Check copy flags changed
		Assert.assertTrue(copyCargo.isLocked());
		Assert.assertFalse(copyCargo.isAllowRewiring());

		// but not original
		Assert.assertFalse(cargo.isLocked());
		Assert.assertTrue(cargo.isAllowRewiring());
	}

	@Test
	public void filterSlotsAndCargoesTest_Slot1() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
	}

	@Test
	public void filterSlotsAndCargoesTest_Slot2() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
	}

	@Test
	public void filterSlotsAndCargoesTest_Slot3() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.MAY, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No changes
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));

		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterSlotsAndCargoesTest_Slot4() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
	}

	@Test
	public void filterSlotsAndCargoesTest_Slot5() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);

		transformer.filterSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
	}

	@Test
	public void filterVesselEventsTest1() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent event = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event");
		event.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		event.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		event.setDurationInDays(10);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyEvent.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyEvent.setDurationInDays(10);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().contains(event));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().isEmpty());

		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(event);
	}

	@Test
	public void filterVesselEventsTest2() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent event = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event");
		event.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1));
		event.setStartBy(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1));
		event.setDurationInDays(20);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1));
		copyEvent.setStartBy(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1));
		copyEvent.setDurationInDays(20);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().contains(event));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().contains(copyEvent));

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterVesselEventsTest3() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent event = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event");
		event.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		event.setStartBy(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		event.setDurationInDays(20);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		copyEvent.setStartBy(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		copyEvent.setDurationInDays(20);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().contains(event));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().contains(copyEvent));

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterVesselEventsTest4() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent event = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event");
		event.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		event.setStartBy(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		event.setDurationInDays(10);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		copyEvent.setStartBy(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		copyEvent.setDurationInDays(10);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().contains(event));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().isEmpty());

		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(event);
	}

	@Test
	public void filterVesselAvailabilitiesTest1() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);
		vesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
		vesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(vesselAvailability));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().isEmpty());

		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(vesselAvailability);
	}

	@Test
	public void filterVesselAvailabilitiesTest2() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);
		vesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));
		vesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.APRIL, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterVesselAvailabilitiesTest3() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);
		vesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		vesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		vesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		vesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterVesselAvailabilitiesTest4() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);
		vesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		vesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		vesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		vesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(vesselAvailability));

		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().isEmpty());

		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(vesselAvailability);
	}

	@Test
	public void filterVesselAvailabilitiesTest5() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);
		vesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		vesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		vesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getPortfolioModel().getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	private PeriodTransformer createPeriodTransformer(final InclusionChecker inclusionChecker) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(InclusionChecker.class).toInstance(inclusionChecker);
			}
		});
		return injector.getInstance(PeriodTransformer.class);
	}
}
