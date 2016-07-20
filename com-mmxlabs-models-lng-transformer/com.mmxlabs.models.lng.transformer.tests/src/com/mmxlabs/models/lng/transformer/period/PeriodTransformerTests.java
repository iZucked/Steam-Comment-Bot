/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.TimePeriod;

@SuppressWarnings("unused")
public class PeriodTransformerTests {

	@Test
	public void createPeriodRecordTest_EmptySettings() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings);

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

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		settings.setPeriodStart(PeriodTestUtils.createYearMonth(2014, Calendar.FEBRUARY));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings);

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

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		settings.setPeriodEnd(PeriodTestUtils.createYearMonth(2014, Calendar.FEBRUARY));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings);

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

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		settings.setPeriodStart(PeriodTestUtils.createYearMonth(2014, Calendar.FEBRUARY));
		settings.setPeriodEnd(PeriodTestUtils.createYearMonth(2014, Calendar.JULY));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings);

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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// Vessel before period
		final Vessel vessel1 = PeriodTestUtils.createVessel(scenarioModel, "Vessel1");
		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel1);
		vesselAvailability1.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v1-cargo1", port1, PeriodTestUtils.createLocalDate(2013, Calendar.NOVEMBER, 1), port2,
					PeriodTestUtils.createLocalDate(2013, Calendar.DECEMBER, 1));
			c1.setVesselAssignmentType(vesselAvailability1);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability1, c1));
		}
		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		// No change expected - vesselAvailability1
		Assert.assertTrue(vesselAvailability1.getStartAt().isEmpty());
		Assert.assertTrue(vesselAvailability1.getEndAt().isEmpty());
		Assert.assertNull(vesselAvailability1.getStartAfter());
		Assert.assertNull(vesselAvailability1.getStartBy());
		Assert.assertNull(vesselAvailability1.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0), vesselAvailability1.getEndBy());
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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// Vessel across lower bounds
		final Vessel vessel2 = PeriodTestUtils.createVessel(scenarioModel, "Vessel2");
		final VesselAvailability vesselAvailability2 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel2);
		vesselAvailability2.getStartAt().add(port1);
		vesselAvailability2.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability2.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability2.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));
		vesselAvailability2.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v2-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1), port2,
					PeriodTestUtils.createLocalDate(2014, Calendar.FEBRUARY, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v2-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.MARCH, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.APRIL, 1));

			c1.setVesselAssignmentType(vesselAvailability2);
			c2.setVesselAssignmentType(vesselAvailability2);

			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)));
			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));

			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MARCH, 1)));
			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability2, c1, c2));
		}
		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		// Changed
		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability2.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability2.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability2.getStartBy());
		// Unchanged
		Assert.assertTrue(vesselAvailability2.getEndAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), vesselAvailability2.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), vesselAvailability2.getEndBy());
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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel completely in
		final Vessel vessel3 = PeriodTestUtils.createVessel(scenarioModel, "Vessel3");
		final VesselAvailability vesselAvailability3 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel3);
		vesselAvailability3.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0));
		vesselAvailability3.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0));
		vesselAvailability3.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0));
		vesselAvailability3.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v3-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1), port2,
					PeriodTestUtils.createLocalDate(2014, Calendar.JUNE, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v3-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.AUGUST, 1));
			c1.setVesselAssignmentType(vesselAvailability3);
			c2.setVesselAssignmentType(vesselAvailability3);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));

			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1)));
			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability3, c1, c2));
		}
		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		// No change expected
		Assert.assertTrue(vesselAvailability3.getStartAt().isEmpty());
		Assert.assertTrue(vesselAvailability3.getEndAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0), vesselAvailability3.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0), vesselAvailability3.getStartBy());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0), vesselAvailability3.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0), vesselAvailability3.getEndBy());
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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel across both bounds
		final Vessel vessel4 = PeriodTestUtils.createVessel(scenarioModel, "Vessel4");
		final VesselAvailability vesselAvailability4 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel4);
		vesselAvailability4.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability4.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability4.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		vesselAvailability4.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1), port2,
					PeriodTestUtils.createLocalDate(2014, Calendar.FEBRUARY, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.MARCH, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.APRIL, 1));
			final Cargo c3 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo3", port3, PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.JUNE, 1));
			final Cargo c4 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo4", port3, PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.AUGUST, 1));
			final Cargo c5 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo5", port3, PeriodTestUtils.createLocalDate(2014, Calendar.SEPTEMBER, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
			final Cargo c6 = PeriodTestUtils.createCargo(scenarioModel, "v4-cargo6", port3, PeriodTestUtils.createLocalDate(2014, Calendar.NOVEMBER, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));

			c1.setVesselAssignmentType(vesselAvailability4);
			c2.setVesselAssignmentType(vesselAvailability4);
			c3.setVesselAssignmentType(vesselAvailability4);
			c4.setVesselAssignmentType(vesselAvailability4);
			c5.setVesselAssignmentType(vesselAvailability4);
			c6.setVesselAssignmentType(vesselAvailability4);

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

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability4, c1, c2, c3, c4, c5, c6));
		}

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability4.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability4.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability4.getStartBy());
		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability4.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability4.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability4.getEndBy());
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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel across both bounds
		final Vessel vessel5 = PeriodTestUtils.createVessel(scenarioModel, "Vessel5");
		final VesselAvailability vesselAvailability5 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel5);
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1), port2,
					PeriodTestUtils.createLocalDate(2014, Calendar.FEBRUARY, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.MARCH, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.APRIL, 1));
			final Cargo c3 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo3", port3, PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.JUNE, 1));
			final Cargo c4 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo4", port3, PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.AUGUST, 1));
			final Cargo c5 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo5", port3, PeriodTestUtils.createLocalDate(2014, Calendar.SEPTEMBER, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
			final Cargo c6 = PeriodTestUtils.createCargo(scenarioModel, "v5-cargo6", port3, PeriodTestUtils.createLocalDate(2014, Calendar.NOVEMBER, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));

			c1.setVesselAssignmentType(vesselAvailability5);
			c2.setVesselAssignmentType(vesselAvailability5);
			c3.setVesselAssignmentType(vesselAvailability5);
			c4.setVesselAssignmentType(vesselAvailability5);
			c5.setVesselAssignmentType(vesselAvailability5);
			c6.setVesselAssignmentType(vesselAvailability5);

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

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability5, c1, c2, c3, c4, c5, c6));
		}

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability5.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability5.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability5.getStartBy());
		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability5.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability5.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability5.getEndBy());
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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel across upper bound
		final Vessel vessel6 = PeriodTestUtils.createVessel(scenarioModel, "Vessel3");
		final VesselAvailability vesselAvailability6 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel6);
		vesselAvailability6.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		vesselAvailability6.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		vesselAvailability6.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		vesselAvailability6.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		{
			final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v6-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.SEPTEMBER, 1), port2,
					PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
			final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v6-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.NOVEMBER, 1), port4,
					PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));
			c1.setVesselAssignmentType(vesselAvailability6);
			c2.setVesselAssignmentType(vesselAvailability6);

			startConditionMap.put(c1, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.OCTOBER, 1)));
			endConditionMap.put(c1, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 1)));

			startConditionMap.put(c2, PeriodTestUtils.createPortVisit(port4, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)));
			endConditionMap.put(c2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.NOVEMBER, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability6, c1, c2));
		}

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		// No change expected
		Assert.assertTrue(vesselAvailability6.getStartAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0), vesselAvailability6.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0), vesselAvailability6.getStartBy());

		Assert.assertEquals(Collections.singletonList(port3), vesselAvailability6.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability6.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability6.getEndBy());
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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		// vessel after period
		final Vessel vessel7 = PeriodTestUtils.createVessel(scenarioModel, "Vessel7");
		final VesselAvailability vesselAvailability7 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel7);
		vesselAvailability7.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		// Cargoes with 1 month intervals
		// Initial Vessel availability 0.5 month intervals
		// Some with open bounds

		// Need vessel event and cooldown also

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		// No change expected - vesselAvailability7
		Assert.assertTrue(vesselAvailability7.getStartAt().isEmpty());
		Assert.assertTrue(vesselAvailability7.getEndAt().isEmpty());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0), vesselAvailability7.getStartAfter());
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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		{
			final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), 30);
			final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), 30);
			event1.setVesselAssignmentType(vesselAvailability);
			event2.setVesselAssignmentType(vesselAvailability);

			startConditionMap.put(event1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1)));
			endConditionMap.put(event1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			startConditionMap.put(event2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(event2, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability, event1, event2));
		}

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		{
			final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), 30);
			final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), 30);
			event1.setVesselAssignmentType(vesselAvailability);
			event2.setVesselAssignmentType(vesselAvailability);

			startConditionMap.put(event1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1)));
			endConditionMap.put(event1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			startConditionMap.put(event2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(event2, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability, event1, event2));
		}

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		Assert.assertEquals(Collections.singletonList(port2), vesselAvailability.getStartAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), vesselAvailability.getStartAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), vesselAvailability.getStartBy());

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
		final List<CollectedAssignment> collectedAssignments = new ArrayList<>(7);

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		{
			final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), 30);
			final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), 30);
			event1.setVesselAssignmentType(vesselAvailability);
			event2.setVesselAssignmentType(vesselAvailability);

			startConditionMap.put(event1, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.JUNE, 1)));
			endConditionMap.put(event1, PeriodTestUtils.createPortVisit(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			startConditionMap.put(event2, PeriodTestUtils.createPortVisit(port3, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)));
			endConditionMap.put(event2, PeriodTestUtils.createPortVisit(port2, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)));

			collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability, event1, event2));
		}

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.updateVesselAvailabilities(periodRecord, collectedAssignments, startConditionMap, endConditionMap, new HashSet<Cargo>(), new HashSet<Event>(), objectToPortVisitMap);

		Assert.assertTrue(vesselAvailability.getStartAt().isEmpty());
		Assert.assertNull(vesselAvailability.getStartAfter());
		Assert.assertNull(vesselAvailability.getStartBy());

		Assert.assertEquals(Collections.singletonList(port2), vesselAvailability.getEndAt());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), vesselAvailability.getEndAfter());
		Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), vesselAvailability.getEndBy());
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
			Assert.assertEquals(30000.0, vesselAvailability1.getStartHeel().getVolumeAvailable(), 0.0);
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 28, 0), vesselAvailability1.getStartBy());
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 28, 0), vesselAvailability1.getStartAfter());
		}
		// Same again, but reverse order. Should yield same result as before.
		{
			final VesselAvailability vesselAvailability2 = CargoFactory.eINSTANCE.createVesselAvailability();
			vesselAvailability2.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());

			transformer.updateStartConditions(vesselAvailability2, assignedObject3, startConditionMap);
			transformer.updateStartConditions(vesselAvailability2, assignedObject2, startConditionMap);
			transformer.updateStartConditions(vesselAvailability2, assignedObject1, startConditionMap);

			Assert.assertEquals(Collections.singletonList(port3), vesselAvailability2.getStartAt());
			Assert.assertEquals(30000.0, vesselAvailability2.getStartHeel().getVolumeAvailable(), 0.0);
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 28, 0), vesselAvailability2.getStartBy());
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 28, 0), vesselAvailability2.getStartAfter());
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
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0), vesselAvailability1.getEndBy());
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0), vesselAvailability1.getEndAfter());
		}
		// Same again, but reverse order. Should yield same result as before.
		{
			final VesselAvailability vesselAvailability2 = CargoFactory.eINSTANCE.createVesselAvailability();
			vesselAvailability2.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());

			transformer.updateEndConditions(vesselAvailability2, assignedObject3, endConditionMap);
			transformer.updateEndConditions(vesselAvailability2, assignedObject2, endConditionMap);
			transformer.updateEndConditions(vesselAvailability2, assignedObject1, endConditionMap);

			Assert.assertEquals(Collections.singletonList(port1), vesselAvailability2.getEndAt());
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0), vesselAvailability2.getEndBy());
			Assert.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0), vesselAvailability2.getEndAfter());
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.FEBRUARY, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(seenSlots.contains(copyDischargeSlot));

		Assert.assertTrue(removedSlots.contains(copyLoadSlot));
		Assert.assertTrue(removedSlots.contains(copyDischargeSlot));
		Assert.assertTrue(removedCargoes.contains(copyCargo));

		// No change to copy scenario
		Assert.assertTrue(copyScenarioModel.getCargoModel().getCargoes().contains(copyCargo));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getLoadSlots().contains(copyLoadSlot));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo2_PartialLock() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.MARCH, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		final Vessel vessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability vesselA = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel);
		copyCargo.setVesselAssignmentType(vesselA);
		copyCargo.setAllowRewiring(true);

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(seenSlots.contains(copyDischargeSlot));

		Assert.assertFalse(removedSlots.contains(copyLoadSlot));
		Assert.assertFalse(removedSlots.contains(copyDischargeSlot));
		Assert.assertFalse(removedCargoes.contains(copyCargo));
		//
		//
		// // Verify relevant slots and cargoes marked as remove
		// Mockito.verify(seenSlots, Mockito.atLeastOnce()).add(copyLoadSlot);
		// Mockito.verify(seenSlots, Mockito.atLeastOnce()).add(copyDischargeSlot);
		// Mockito.verify(seenSlots).addAll(Matchers.anyList());
		// Mockito.verifyNoMoreInteractions(seenSlots);
		// Mockito.verifyNoMoreInteractions(removedSlots);
		// Mockito.verifyNoMoreInteractions(removedCargoes);
		// Mockito.verifyNoMoreInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// // Check copy flags changed

		// This should pass now
		Assert.assertTrue(copyLoadSlot.isLocked());
		Assert.assertFalse(copyDischargeSlot.isLocked());
		Assert.assertTrue(copyCargo.isAllowRewiring());
		Assert.assertFalse(copyCargo.isLocked());
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
		loadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(seenSlots.contains(copyDischargeSlot));

		Assert.assertFalse(removedSlots.contains(copyLoadSlot));
		Assert.assertFalse(removedSlots.contains(copyDischargeSlot));
		Assert.assertFalse(removedCargoes.contains(copyCargo));

		//
		// // Verify relevant slots and cargoes marked as remove
		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(seenSlots).add(copyDischargeSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyDischargeSlot);
		// Mockito.verify(removedCargoes).add(copyCargo);
		// Mockito.verifyZeroInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// Check copy flags unchanged
		Assert.assertFalse(copyCargo.isLocked());
		Assert.assertTrue(copyCargo.isAllowRewiring());
		//
		// Assert.assertFalse(cargo.isLocked());
		// Assert.assertTrue(cargo.isAllowRewiring());
		//
		// Registered objects as removed.
		// Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void filterSlotsAndCargoesTest_Cargo4_PartialLock() {
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
		loadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.AUGUST, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.AUGUST, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);
		final Vessel vessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability vesselA = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel);
		copyCargo.setVesselAssignmentType(vesselA);

		// // Check copy flags changed
		Assert.assertFalse(copyCargo.isLocked());
		Assert.assertTrue(copyCargo.isAllowRewiring());
		//
		// // but not original
		// Assert.assertFalse(cargo.isLocked());
		// Assert.assertTrue(cargo.isAllowRewiring());
		//
		// // Registered objects as removed.
		// Mockito.verifyNoMoreInteractions(mapping);
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
		loadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.NOVEMBER, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.NOVEMBER, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		// final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		//
		// Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		// Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		// Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);
		//
		// Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		// Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		// Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(seenSlots.contains(copyDischargeSlot));

		Assert.assertTrue(removedSlots.contains(copyLoadSlot));
		Assert.assertTrue(removedSlots.contains(copyDischargeSlot));
		Assert.assertTrue(removedCargoes.contains(copyCargo));

		//
		// // Verify relevant slots and cargoes marked as remove
		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(seenSlots).add(copyDischargeSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyDischargeSlot);
		// Mockito.verify(removedCargoes).add(copyCargo);
		// Mockito.verifyZeroInteractions(slotAllocationMap);

		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// // Registered objects as removed.
		// Mockito.verify(mapping).registerRemovedOriginal(cargo);
		// Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
		// Mockito.verify(mapping).registerRemovedOriginal(dischargeSlot);

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
		loadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1));
		final DischargeSlot dischargeSlot = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		dischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, loadSlot, dischargeSlot);
		cargo.setAllowRewiring(true);

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);
		//
		// final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		//
		// Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		// Mockito.when(mapping.getCopyFromOriginal(dischargeSlot)).thenReturn(copyDischargeSlot);
		// Mockito.when(mapping.getCopyFromOriginal(cargo)).thenReturn(copyCargo);
		//
		// Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);
		// Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot)).thenReturn(dischargeSlot);
		// Mockito.when(mapping.getOriginalFromCopy(copyCargo)).thenReturn(cargo);

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(seenSlots.contains(copyDischargeSlot));

		Assert.assertFalse(removedSlots.contains(copyLoadSlot));
		Assert.assertFalse(removedSlots.contains(copyDischargeSlot));
		Assert.assertFalse(removedCargoes.contains(copyCargo));

		//
		// // Verify relevant slots and cargoes marked as remove
		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(seenSlots).add(copyDischargeSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyDischargeSlot);
		// Mockito.verify(removedCargoes).add(copyCargo);
		// Mockito.verifyZeroInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// // Check copy flags changed
		Assert.assertTrue(copyCargo.isLocked());
		Assert.assertFalse(copyCargo.isAllowRewiring());
		//
		// // but not original
		// Assert.assertFalse(cargo.isLocked());
		// Assert.assertTrue(cargo.isAllowRewiring());
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1));

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(removedSlots.contains(copyLoadSlot));

		//
		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verifyZeroInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().isEmpty());
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().isEmpty());
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
		loadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.APRIL, 1));

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.APRIL, 1));

		// final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);
		//
		// Mockito.when(mapping.getCopyFromOriginal(loadSlot)).thenReturn(copyLoadSlot);
		// Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot)).thenReturn(loadSlot);

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(removedSlots.contains(copyLoadSlot));

		//
		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(seenSlots).add(copyDischargeSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyDischargeSlot);
		// Mockito.verify(removedCargoes).add(copyCargo);
		// Mockito.verifyZeroInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// // No change to original
		// Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		//
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		// // Registered objects as removed.
		// Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1));

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertFalse(removedSlots.contains(copyLoadSlot));

		// Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		// Assert.assertTrue(seenSlots.contains(copyDischargeSlot));
		//
		// Assert.assertTrue(removedSlots.contains(copyLoadSlot));
		// Assert.assertTrue(removedSlots.contains(copyDischargeSlot));
		// Assert.assertTrue(removedCargoes.contains(copyCargo));

		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(seenSlots).add(copyDischargeSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyDischargeSlot);
		// Mockito.verify(removedCargoes).add(copyCargo);
		// Mockito.verifyZeroInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// // No changes
		// Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		//
		// Mockito.verifyNoMoreInteractions(mapping);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.SEPTEMBER, 1));

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		Assert.assertTrue(removedSlots.contains(copyLoadSlot));
		//
		//
		// Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		// Assert.assertTrue(seenSlots.contains(copyDischargeSlot));
		//
		// Assert.assertTrue(removedSlots.contains(copyLoadSlot));
		// Assert.assertTrue(removedSlots.contains(copyDischargeSlot));
		// Assert.assertTrue(removedCargoes.contains(copyCargo));

		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(seenSlots).add(copyDischargeSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyDischargeSlot);
		// Mockito.verify(removedCargoes).add(copyCargo);
		// Mockito.verifyZeroInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// // No change to original
		// Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		//
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		// // Registered objects as removed.
		// Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
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
		loadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));

		final Set<Slot> seenSlots = new HashSet<>();
		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.findSlotsAndCargoesToRemove(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), seenSlots, removedSlots, removedCargoes,
				slotAllocationMap, objectToPortVisitMap);

		// Verify relevant slots and cargoes marked as remove
		Assert.assertTrue(seenSlots.contains(copyLoadSlot));
		// Assert.assertTrue(seenSlots.contains(copyDischargeSlot));

		Assert.assertTrue(removedSlots.contains(copyLoadSlot));
		// Assert.assertTrue(removedSlots.contains(copyDischargeSlot));
		// Assert.assertTrue(removedCargoes.contains(copyCargo));

		// Mockito.verify(seenSlots).add(copyLoadSlot);
		// Mockito.verify(seenSlots).add(copyDischargeSlot);
		// Mockito.verify(removedSlots).add(copyLoadSlot);
		// Mockito.verify(removedSlots).add(copyDischargeSlot);
		// Mockito.verify(removedCargoes).add(copyCargo);
		// Mockito.verifyZeroInteractions(slotAllocationMap);
		//
		// // No change to copy scenario
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getCargoes().contains(copyCargo));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(copyLoadSlot));
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().contains(copyDischargeSlot));
		//
		// // No change to original
		// Assert.assertTrue(scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().contains(loadSlot));
		//
		// Assert.assertTrue(copyScenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().isEmpty());
		// // Registered objects as removed.
		// Mockito.verify(mapping).registerRemovedOriginal(loadSlot);
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
		event.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		event.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		event.setDurationInDays(10);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyEvent.setDurationInDays(10);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		Schedule mockSchedule = Mockito.mock(Schedule.class);
		EList<Sequence> l = ECollections.emptyEList();
		Mockito.when(mockSchedule.getSequences()).thenReturn(l);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		Triple<Set<Cargo>, Set<Event>, Set<VesselEvent>> eventDependencies = transformer.findVesselEventsToRemoveAndDependencies(mockSchedule, periodRecord, copyScenarioModel.getCargoModel(),
				objectToPortVisitMap);
		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), eventDependencies.getThird(), copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselEvents().contains(event));

		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselEvents().isEmpty());

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
		event.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0));
		event.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0));
		event.setDurationInDays(20);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0));
		copyEvent.setDurationInDays(20);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		Schedule mockSchedule = Mockito.mock(Schedule.class);
		EList<Sequence> l = ECollections.emptyEList();
		Mockito.when(mockSchedule.getSequences()).thenReturn(l);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		Triple<Set<Cargo>, Set<Event>, Set<VesselEvent>> eventDependencies = transformer.findVesselEventsToRemoveAndDependencies(mockSchedule, periodRecord, copyScenarioModel.getCargoModel(),
				objectToPortVisitMap);
		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), eventDependencies.getThird(), copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselEvents().contains(event));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselEvents().contains(copyEvent));

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
		event.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		event.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		event.setDurationInDays(20);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		copyEvent.setDurationInDays(20);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		Schedule mockSchedule = Mockito.mock(Schedule.class);
		EList<Sequence> l = ECollections.emptyEList();
		Mockito.when(mockSchedule.getSequences()).thenReturn(l);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		Triple<Set<Cargo>, Set<Event>, Set<VesselEvent>> eventDependencies = transformer.findVesselEventsToRemoveAndDependencies(mockSchedule, periodRecord, copyScenarioModel.getCargoModel(),
				objectToPortVisitMap);
		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), eventDependencies.getThird(), copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselEvents().contains(event));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselEvents().contains(copyEvent));

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
		event.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		event.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		event.setDurationInDays(10);

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		copyEvent.setDurationInDays(10);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(event)).thenReturn(copyEvent);
		Mockito.when(mapping.getOriginalFromCopy(copyEvent)).thenReturn(event);

		Schedule mockSchedule = Mockito.mock(Schedule.class);
		EList<Sequence> l = ECollections.emptyEList();
		Mockito.when(mockSchedule.getSequences()).thenReturn(l);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		Triple<Set<Cargo>, Set<Event>, Set<VesselEvent>> eventDependencies = transformer.findVesselEventsToRemoveAndDependencies(mockSchedule, periodRecord, copyScenarioModel.getCargoModel(),
				objectToPortVisitMap);
		transformer.filterVesselEvents(PeriodTestUtils.createEditingDomain(copyScenarioModel), eventDependencies.getThird(), copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselEvents().contains(event));

		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselEvents().isEmpty());

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
		vesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.FEBRUARY, 1, 0));
		vesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.FEBRUARY, 1, 0));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.FEBRUARY, 1, 0));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.FEBRUARY, 1, 0));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping, objectToPortVisitMap);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));

		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().isEmpty());

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
		vesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));
		vesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);
		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping, objectToPortVisitMap);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

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
		vesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		vesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		vesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		vesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping, objectToPortVisitMap);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

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
		vesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		vesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		vesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		vesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping, objectToPortVisitMap);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));

		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().isEmpty());

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
		vesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		vesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		final Vessel copyVessel = PeriodTestUtils.createVessel(copyScenarioModel, "vessel");
		final VesselAvailability copyVesselAvailability = PeriodTestUtils.createVesselAvailability(copyScenarioModel, copyVessel);
		copyVesselAvailability.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyVesselAvailability.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyVesselAvailability.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		copyVesselAvailability.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(vesselAvailability)).thenReturn(copyVesselAvailability);
		Mockito.when(mapping.getOriginalFromCopy(copyVesselAvailability)).thenReturn(vesselAvailability);

		Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		transformer.filterVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping, objectToPortVisitMap);

		// No change to original
		Assert.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

		// Registered objects as removed.
		Mockito.verifyNoMoreInteractions(mapping);
	}

	@Test
	public void trimSpotMarketCurves() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2015, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2015, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2015, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2015, Calendar.SEPTEMBER, 15);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		scenarioModel.getReferenceModel().setSpotMarketsModel(PeriodTestUtils.createSpotMarkets(scenarioModel, "testSpots"));

		transformer.trimSpotMarketCurves(periodRecord, scenarioModel);

		for (SpotMarketGroup group : new SpotMarketGroup[] { scenarioModel.getReferenceModel().getSpotMarketsModel().getDesPurchaseSpotMarket(),
				scenarioModel.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket(), scenarioModel.getReferenceModel().getSpotMarketsModel().getFobPurchasesSpotMarket(),
				scenarioModel.getReferenceModel().getSpotMarketsModel().getFobSalesSpotMarket(), }) {
			EList<SpotMarket> markets = group.getMarkets();
			SpotMarket market = markets.get(0);
			SpotAvailability availability = market.getAvailability();
			Assert.assertTrue(availability.isSetConstant() == false);
			Assert.assertTrue(availability.getCurve().getPoints().size() > 0);
			for (IndexPoint<Integer> point : availability.getCurve().getPoints()) {
				YearMonth date = point.getDate();
				ZonedDateTime dateAsDateTime = date.atDay(1).atStartOfDay(ZoneId.of("UTC"));
				Assert.assertTrue(dateAsDateTime.isAfter(periodRecord.lowerCutoff));
				Assert.assertTrue(dateAsDateTime.isBefore(periodRecord.upperBoundary));
			}
			Assert.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 4)) == 5);
			Assert.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 5)) == 5);
			Assert.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 6)) == 2);
			Assert.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 6)) == 2);
		}
	}

	@Test
	public void generateStartAndEndConditionsMapTest_Cargo() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Schedule schedule = PeriodTestUtils.createSchedule(scenarioModel);

		final LoadSlot load = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		final DischargeSlot discharge = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, load, discharge);

		final SlotAllocation loadAllocation = PeriodTestUtils.createSlotAllocation(scenarioModel, load);
		final SlotAllocation dischargeAllocation = PeriodTestUtils.createSlotAllocation(scenarioModel, discharge);
		final SlotVisit loadVisit = PeriodTestUtils.createSlotVisit(scenarioModel, loadAllocation);
		final SlotVisit dischargeVisit = PeriodTestUtils.createSlotVisit(scenarioModel, dischargeAllocation);

		final Journey ladenJourney = PeriodTestUtils.createJourney();
		final Idle ladenIdle = PeriodTestUtils.createIdle();
		final Journey ballastJourney = PeriodTestUtils.createJourney();
		final Idle ballastIdle = PeriodTestUtils.createIdle();

		final PortVisit endVisit = PeriodTestUtils.createEndEvent();

		final CargoAllocation cargoAllocation = PeriodTestUtils.createCargoAllocation(scenarioModel, cargo, loadAllocation, dischargeAllocation, loadVisit, ladenJourney, ladenIdle, dischargeVisit,
				ballastJourney, ballastIdle);

		final Sequence sequence = PeriodTestUtils.createSequence(scenarioModel, PeriodTestUtils.createStartEvent(), PeriodTestUtils.createJourney(), PeriodTestUtils.createIdle(), loadVisit,
				ladenJourney, ladenIdle, dischargeVisit, ballastJourney, ballastIdle, endVisit);

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();

		transformer.generateStartAndEndConditionsMap(scenarioModel, startConditionMap, endConditionMap);

		Assert.assertSame(endVisit, startConditionMap.get(cargo));
		Assert.assertSame(loadVisit, endConditionMap.get(cargo));

	}

	@Test
	public void generateStartAndEndConditionsMapTest_CargoAndCooldown() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Schedule schedule = PeriodTestUtils.createSchedule(scenarioModel);

		final LoadSlot load = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		final DischargeSlot discharge = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge");
		final Cargo cargo = PeriodTestUtils.createCargo(scenarioModel, load, discharge);

		final SlotAllocation loadAllocation = PeriodTestUtils.createSlotAllocation(scenarioModel, load);
		final SlotAllocation dischargeAllocation = PeriodTestUtils.createSlotAllocation(scenarioModel, discharge);
		final SlotVisit loadVisit = PeriodTestUtils.createSlotVisit(scenarioModel, loadAllocation);
		final SlotVisit dischargeVisit = PeriodTestUtils.createSlotVisit(scenarioModel, dischargeAllocation);

		final Journey ladenJourney = PeriodTestUtils.createJourney();
		final Idle ladenIdle = PeriodTestUtils.createIdle();
		final Journey ballastJourney = PeriodTestUtils.createJourney();
		final Idle ballastIdle = PeriodTestUtils.createIdle();
		final Cooldown cooldown = PeriodTestUtils.createCooldown();

		final PortVisit endVisit = PeriodTestUtils.createEndEvent();

		final CargoAllocation cargoAllocation = PeriodTestUtils.createCargoAllocation(scenarioModel, cargo, loadAllocation, dischargeAllocation, loadVisit, ladenJourney, ladenIdle, dischargeVisit,
				ballastJourney, ballastIdle, cooldown);

		final Sequence sequence = PeriodTestUtils.createSequence(scenarioModel, PeriodTestUtils.createStartEvent(), PeriodTestUtils.createJourney(), PeriodTestUtils.createIdle(), loadVisit,
				ladenJourney, ladenIdle, dischargeVisit, ballastJourney, ballastIdle, cooldown, endVisit);

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();

		transformer.generateStartAndEndConditionsMap(scenarioModel, startConditionMap, endConditionMap);

		Assert.assertSame(endVisit, startConditionMap.get(cargo));
		Assert.assertSame(loadVisit, endConditionMap.get(cargo));

	}

	@Test
	public void generateStartAndEndConditionsMapTest_VesselEvent() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Schedule schedule = PeriodTestUtils.createSchedule(scenarioModel);

		final VesselEvent vesselEvent = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event");
		final VesselEventVisit vesselEventVisit = PeriodTestUtils.createVesselEventVisit(scenarioModel, vesselEvent);

		final Journey eventJourney = PeriodTestUtils.createJourney();
		final Idle eventIdle = PeriodTestUtils.createIdle();

		final PortVisit endVisit = PeriodTestUtils.createEndEvent();

		final Sequence sequence = PeriodTestUtils.createSequence(scenarioModel, PeriodTestUtils.createStartEvent(), PeriodTestUtils.createJourney(), PeriodTestUtils.createIdle(), vesselEventVisit,
				eventJourney, eventIdle, endVisit);

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();

		transformer.generateStartAndEndConditionsMap(scenarioModel, startConditionMap, endConditionMap);

		Assert.assertSame(endVisit, startConditionMap.get(vesselEvent));
		Assert.assertSame(vesselEventVisit, endConditionMap.get(vesselEvent));

	}

	@Ignore()
	@Test
	public void generateStartAndEndConditionsMapTest_CharterOutDifferentEndPort() {

		Assert.fail("Not yet implemented");

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final Schedule schedule = PeriodTestUtils.createSchedule(scenarioModel);

		final VesselEvent vesselEvent = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event");
		final VesselEventVisit vesselEventVisit = PeriodTestUtils.createVesselEventVisit(scenarioModel, vesselEvent);

		final Journey eventJourney = PeriodTestUtils.createJourney();
		final Idle eventIdle = PeriodTestUtils.createIdle();

		final PortVisit endVisit = PeriodTestUtils.createEndEvent();

		final Sequence sequence = PeriodTestUtils.createSequence(scenarioModel, PeriodTestUtils.createStartEvent(), PeriodTestUtils.createJourney(), PeriodTestUtils.createIdle(), vesselEventVisit,
				eventJourney, eventIdle, endVisit);

		final Map<AssignableElement, PortVisit> startConditionMap = new HashMap<>();
		final Map<AssignableElement, PortVisit> endConditionMap = new HashMap<>();

		transformer.generateStartAndEndConditionsMap(scenarioModel, startConditionMap, endConditionMap);

		Assert.assertSame(endVisit, startConditionMap.get(vesselEvent));
		Assert.assertSame(vesselEventVisit, endConditionMap.get(vesselEvent));

	}

	@Test
	public void removeExcludedSlotsAndCargoesTest() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot loadSlot1 = PeriodTestUtils.createLoadSlot(scenarioModel, "load1");
		final DischargeSlot dischargeSlot1 = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge1");
		final Cargo cargo1 = PeriodTestUtils.createCargo(scenarioModel, loadSlot1, dischargeSlot1);

		final LoadSlot loadSlot2 = PeriodTestUtils.createLoadSlot(scenarioModel, "load2");
		final DischargeSlot dischargeSlot2 = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge2");
		final Cargo cargo2 = PeriodTestUtils.createCargo(scenarioModel, loadSlot2, dischargeSlot2);

		final LoadSlot copyLoadSlot1 = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load1");
		final DischargeSlot copyDischargeSlot1 = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge1");
		final Cargo copyCargo1 = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot1, copyDischargeSlot1);

		final LoadSlot copyLoadSlot2 = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load2");
		final DischargeSlot copyDischargeSlot2 = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge2");
		final Cargo copyCargo2 = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot2, copyDischargeSlot2);

		final IScenarioEntityMapping mapping = Mockito.mock(IScenarioEntityMapping.class);

		Mockito.when(mapping.getCopyFromOriginal(loadSlot1)).thenReturn(copyLoadSlot1);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot1)).thenReturn(copyDischargeSlot1);
		Mockito.when(mapping.getCopyFromOriginal(cargo1)).thenReturn(copyCargo1);
		Mockito.when(mapping.getCopyFromOriginal(loadSlot2)).thenReturn(copyLoadSlot2);
		Mockito.when(mapping.getCopyFromOriginal(dischargeSlot2)).thenReturn(copyDischargeSlot2);
		Mockito.when(mapping.getCopyFromOriginal(cargo2)).thenReturn(copyCargo2);

		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot1)).thenReturn(loadSlot1);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot1)).thenReturn(dischargeSlot1);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo1)).thenReturn(cargo1);
		Mockito.when(mapping.getOriginalFromCopy(copyLoadSlot2)).thenReturn(loadSlot2);
		Mockito.when(mapping.getOriginalFromCopy(copyDischargeSlot2)).thenReturn(dischargeSlot2);
		Mockito.when(mapping.getOriginalFromCopy(copyCargo2)).thenReturn(cargo2);

		final Set<Slot> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();

		removedSlots.add(copyLoadSlot2);
		removedSlots.add(copyDischargeSlot2);
		removedCargoes.add(copyCargo2);

		transformer.removeExcludedSlotsAndCargoes(PeriodTestUtils.createEditingDomain(copyScenarioModel), mapping, removedSlots, removedCargoes);

		// Removed from copy
		Assert.assertTrue(copyScenarioModel.getCargoModel().getCargoes().contains(copyCargo1));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getLoadSlots().contains(copyLoadSlot1));
		Assert.assertTrue(copyScenarioModel.getCargoModel().getDischargeSlots().contains(copyDischargeSlot1));

		Assert.assertFalse(copyScenarioModel.getCargoModel().getCargoes().contains(copyCargo2));
		Assert.assertFalse(copyScenarioModel.getCargoModel().getLoadSlots().contains(copyLoadSlot2));
		Assert.assertFalse(copyScenarioModel.getCargoModel().getDischargeSlots().contains(copyDischargeSlot2));

		// Registered objects as removed.
		Mockito.verify(mapping).registerRemovedOriginal(cargo2);
		Mockito.verify(mapping).registerRemovedOriginal(loadSlot2);
		Mockito.verify(mapping).registerRemovedOriginal(dischargeSlot2);

	}

	@Test
	public void lockDownCargoDatesTest() {
		final InclusionChecker inclusionChecker = new InclusionChecker();
		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		// // Create a sample scenario
		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();
		final Schedule schedule = PeriodTestUtils.createSchedule(scenarioModel);

		final LoadSlot loadSlot1 = PeriodTestUtils.createLoadSlot(scenarioModel, "load1");
		final DischargeSlot dischargeSlot1 = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge1");
		final Cargo cargo1 = PeriodTestUtils.createCargo(scenarioModel, loadSlot1, dischargeSlot1);

		final Port port = PeriodTestUtils.createPort(scenarioModel, "port");
		port.setTimeZone("Etc/GMT-1");

		loadSlot1.setPort(port);
		dischargeSlot1.setPort(port);

		// set window start dsate, time and duration.
		loadSlot1.setWindowStart(PeriodTestUtils.createLocalDate(2014, 10, 1));
		dischargeSlot1.setWindowStart(PeriodTestUtils.createLocalDate(2014, 11, 1));

		loadSlot1.setWindowStartTime(5);
		dischargeSlot1.setWindowStartTime(10);

		loadSlot1.setWindowSize(2);
		loadSlot1.setWindowSizeUnits(TimePeriod.DAYS);
		dischargeSlot1.setWindowSize(3);
		dischargeSlot1.setWindowSizeUnits(TimePeriod.DAYS);

		// TODO: Should be part of the input?
		// loadSlot1.setDuration(5);
		// dischargeSlot1.setDuration(10);

		// set flags
		cargo1.setAllowRewiring(true);
		cargo1.setLocked(false);

		final SlotAllocation loadAllocation = PeriodTestUtils.createSlotAllocation(scenarioModel, loadSlot1);
		final SlotAllocation dischargeAllocation = PeriodTestUtils.createSlotAllocation(scenarioModel, dischargeSlot1);
		final CargoAllocation cargoAllocation = PeriodTestUtils.createCargoAllocation(scenarioModel, cargo1, loadAllocation, dischargeAllocation);

		final SlotVisit loadVisit = PeriodTestUtils.createSlotVisit(scenarioModel, loadAllocation);
		final SlotVisit dischargeVisit = PeriodTestUtils.createSlotVisit(scenarioModel, dischargeAllocation);

		loadVisit.setPort(port);
		dischargeVisit.setPort(port);

		loadVisit.setStart(PeriodTestUtils.createDate("UTC", 2014, 10, 5, 1));
		dischargeVisit.setStart(PeriodTestUtils.createDate("UTC", 2014, 11, 10, 2));

		final Map<Slot, SlotAllocation> slotAllocationMap = new HashMap<>();

		slotAllocationMap.put(loadSlot1, loadAllocation);
		slotAllocationMap.put(dischargeSlot1, dischargeAllocation);

		transformer.lockDownCargoDates(slotAllocationMap, cargo1);

		Assert.assertFalse(cargo1.isAllowRewiring());
		Assert.assertTrue(cargo1.isLocked());

		Assert.assertEquals(PeriodTestUtils.createLocalDate(2014, 10, 5), loadSlot1.getWindowStart());
		Assert.assertEquals(PeriodTestUtils.createLocalDate(2014, 11, 10), dischargeSlot1.getWindowStart());

		Assert.assertEquals(0, loadSlot1.getWindowSize());
		Assert.assertEquals(0, dischargeSlot1.getWindowSize());

		Assert.assertEquals(1, loadSlot1.getWindowStartTime());
		Assert.assertEquals(2, dischargeSlot1.getWindowStartTime());
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
