/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer.InclusionRecord;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer.Status;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;

@SuppressWarnings({ "unused", "null" })
public class PeriodTransformerTests {

	@Test
	public void createPeriodRecordTest_EmptySettings() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings, null);

		Assertions.assertNotNull(periodRecord);
		Assertions.assertNull(periodRecord.lowerCutoff);
		Assertions.assertNull(periodRecord.lowerBoundary);
		Assertions.assertNull(periodRecord.upperBoundary);
		Assertions.assertNull(periodRecord.upperCutoff);

	}

	@Test
	public void createPeriodRecordTest_OptimiseAfterOnly() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		settings.setPeriodStartDate(PeriodTestUtils.createLocalDate(2014, Calendar.FEBRUARY, 1));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings, null);

		Assertions.assertNotNull(periodRecord);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), periodRecord.lowerCutoff);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1), periodRecord.lowerBoundary);
		Assertions.assertNull(periodRecord.upperBoundary);
		Assertions.assertNull(periodRecord.upperCutoff);
	}

	@Test
	public void createPeriodRecordTest_OptimiseBeforeOnly() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		settings.setPeriodEnd(PeriodTestUtils.createYearMonth(2014, Calendar.FEBRUARY));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings, null);

		Assertions.assertNotNull(periodRecord);
		Assertions.assertNull(periodRecord.lowerCutoff);
		Assertions.assertNull(periodRecord.lowerBoundary);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1), periodRecord.upperBoundary);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.MARCH, 1), periodRecord.upperCutoff);
	}

	@Test
	public void createPeriodRecordTest() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final UserSettings settings = ParametersFactory.eINSTANCE.createUserSettings();

		settings.setPeriodStartDate(PeriodTestUtils.createLocalDate(2014, Calendar.FEBRUARY, 1));
		settings.setPeriodEnd(PeriodTestUtils.createYearMonth(2014, Calendar.JULY));

		final PeriodRecord periodRecord = transformer.createPeriodRecord(settings, null);

		Assertions.assertNotNull(periodRecord);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1), periodRecord.lowerCutoff);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.FEBRUARY, 1), periodRecord.lowerBoundary);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JULY, 1), periodRecord.upperBoundary);
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1), periodRecord.upperCutoff);
	}

	@Test
	public void updateVesselAvailabilitiesTest1() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);
		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		// Vessel before period
		final Vessel vessel1 = PeriodTestUtils.createVessel(scenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel1);
		vesselAvailability1.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));

		final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v1-cargo1", port1, PeriodTestUtils.createLocalDate(2013, Calendar.NOVEMBER, 1), port2,
				PeriodTestUtils.createLocalDate(2013, Calendar.DECEMBER, 1));
		c1.setVesselAssignmentType(vesselAvailability1);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)) //
				.forCargo(c1) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability1, c1);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		// records.put(c1, null)
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		// No change expected - vesselAvailability1
		Assertions.assertNull(vesselAvailability1.getStartAt());
		Assertions.assertTrue(vesselAvailability1.getEndAt().isEmpty());
		Assertions.assertNull(vesselAvailability1.getStartAfter());
		Assertions.assertNull(vesselAvailability1.getStartBy());
		Assertions.assertNull(vesselAvailability1.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0), vesselAvailability1.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest2() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		// Vessel across lower bounds
		final Vessel vessel2 = PeriodTestUtils.createVessel(scenarioModel, "Vessel2");
		final VesselAvailability vesselAvailability2 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel2);
		vesselAvailability2.setStartAt(port1);
		vesselAvailability2.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability2.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability2.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));
		vesselAvailability2.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0));

		final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v2-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1), port2,
				PeriodTestUtils.createLocalDate(2014, Calendar.FEBRUARY, 1));
		final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v2-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.MARCH, 1), port4,
				PeriodTestUtils.createLocalDate(2014, Calendar.APRIL, 1));

		c1.setVesselAssignmentType(vesselAvailability2);
		c2.setVesselAssignmentType(vesselAvailability2);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability2) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(c1) //
				.forCargo(c2) //
				.withEndEvent(port4, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability2, c1, c2);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		// Changed
		Assertions.assertEquals(port3, vesselAvailability2.getStartAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability2.getStartAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability2.getStartBy());
		// Unchanged
		Assertions.assertTrue(vesselAvailability2.getEndAt().isEmpty());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), vesselAvailability2.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), vesselAvailability2.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest3() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		// vessel completely in
		final Vessel vessel3 = PeriodTestUtils.createVessel(scenarioModel, "Vessel3");
		final VesselAvailability vesselAvailability3 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel3);
		vesselAvailability3.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0));
		vesselAvailability3.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0));
		vesselAvailability3.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0));
		vesselAvailability3.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0));

		final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v3-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1), port2,
				PeriodTestUtils.createLocalDate(2014, Calendar.JUNE, 1));
		final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v3-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1), port4,
				PeriodTestUtils.createLocalDate(2014, Calendar.AUGUST, 1));
		c1.setVesselAssignmentType(vesselAvailability3);
		c2.setVesselAssignmentType(vesselAvailability3);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability3) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.MAY, 1)) //
				.forCargo(c1) //
				.forCargo(c2) //
				.withEndEvent(port4, PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability3, c1, c2);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		// No change expected
		Assertions.assertNull(vesselAvailability3.getStartAt());
		Assertions.assertTrue(vesselAvailability3.getEndAt().isEmpty());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0), vesselAvailability3.getStartAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MAY, 1, 0), vesselAvailability3.getStartBy());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0), vesselAvailability3.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.AUGUST, 1, 0), vesselAvailability3.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest4() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		// vessel across both bounds
		final Vessel vessel4 = PeriodTestUtils.createVessel(scenarioModel, "Vessel4");
		final VesselAvailability vesselAvailability4 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel4);
		vesselAvailability4.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability4.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		vesselAvailability4.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		vesselAvailability4.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability4) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(c1) //
				.forCargo(c2) //
				.forCargo(c3) //
				.forCargo(c4) //
				.forCargo(c5) //
				.forCargo(c6) //
				.withEndEvent(port3, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability4, c1, c2, c3, c4, c5, c6);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		Assertions.assertEquals(port3, vesselAvailability4.getStartAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability4.getStartAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability4.getStartBy());
		Assertions.assertEquals(Collections.singletonList(port3), vesselAvailability4.getEndAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability4.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability4.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest5() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		// vessel across both bounds
		final Vessel vessel5 = PeriodTestUtils.createVessel(scenarioModel, "Vessel5");
		final VesselAvailability vesselAvailability5 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel5);

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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability5) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(c1) //
				.forCargo(c2) //
				.forCargo(c3) //
				.forCargo(c4) //
				.forCargo(c5) //
				.forCargo(c6) //
				.withEndEvent(port3, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability5, c1, c2, c3, c4, c5, c6);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		Assertions.assertEquals(port3, vesselAvailability5.getStartAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability5.getStartAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0), vesselAvailability5.getStartBy());
		Assertions.assertEquals(Collections.singletonList(port3), vesselAvailability5.getEndAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability5.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability5.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest6() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		// vessel across upper bound
		final Vessel vessel6 = PeriodTestUtils.createVessel(scenarioModel, "Vessel3");
		final VesselAvailability vesselAvailability6 = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel6);
		vesselAvailability6.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		vesselAvailability6.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		vesselAvailability6.setEndAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));
		vesselAvailability6.setEndBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0));

		final Cargo c1 = PeriodTestUtils.createCargo(scenarioModel, "v6-cargo1", port1, PeriodTestUtils.createLocalDate(2014, Calendar.SEPTEMBER, 1), port2,
				PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
		final Cargo c2 = PeriodTestUtils.createCargo(scenarioModel, "v6-cargo2", port3, PeriodTestUtils.createLocalDate(2014, Calendar.NOVEMBER, 1), port4,
				PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));
		c1.setVesselAssignmentType(vesselAvailability6);
		c2.setVesselAssignmentType(vesselAvailability6);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability6) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(c1) //
				.forCargo(c2) //
				.withEndEvent(port3, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability6, c1, c2);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		// No change expected
		Assertions.assertNull(vesselAvailability6.getStartAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0), vesselAvailability6.getStartAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0), vesselAvailability6.getStartBy());

		Assertions.assertEquals(Collections.singletonList(port3), vesselAvailability6.getEndAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability6.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.NOVEMBER, 1, 0), vesselAvailability6.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest7() {
		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability7) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.withEndEvent(port4, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability7);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		// No change expected - vesselAvailability7
		Assertions.assertNull(vesselAvailability7.getStartAt());
		Assertions.assertTrue(vesselAvailability7.getEndAt().isEmpty());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.DECEMBER, 1, 0), vesselAvailability7.getStartAfter());
		Assertions.assertNull(vesselAvailability7.getStartBy());
		// Expect to match end event
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1).toLocalDateTime(), vesselAvailability7.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1).toLocalDateTime(), vesselAvailability7.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest_VesselEvent1() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		// {
		final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), 30);
		final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), 30);
		event1.setVesselAssignmentType(vesselAvailability);
		event2.setVesselAssignmentType(vesselAvailability);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)) //
				.forVesselEvent(event1) //
				.forVesselEvent(event2) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JUNE, 30)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability, event1, event2);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		Assertions.assertNull(vesselAvailability.getStartAt());
		Assertions.assertTrue(vesselAvailability.getEndAt().isEmpty());
		Assertions.assertNull(vesselAvailability.getStartAfter());
		Assertions.assertNull(vesselAvailability.getStartBy());
		
		// Expect to match end event
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JUNE, 30).toLocalDateTime(), vesselAvailability.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JUNE, 30).toLocalDateTime(), vesselAvailability.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest_VesselEvent2() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port1, PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 1, 0), 30);
		final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), 30);
		event1.setVesselAssignmentType(vesselAvailability);
		event2.setVesselAssignmentType(vesselAvailability);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)) //
				.forVesselEvent(event1) //
				.forVesselEvent(event2) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JUNE, 30)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability, event1, event2);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		Assertions.assertEquals(port2, vesselAvailability.getStartAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), vesselAvailability.getStartAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), vesselAvailability.getStartBy());

		Assertions.assertTrue(vesselAvailability.getEndAt().isEmpty());
		// Expect to match end event
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JUNE, 30).toLocalDateTime(), vesselAvailability.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createDate(2014, Calendar.JUNE, 30).toLocalDateTime(), vesselAvailability.getEndBy());
	}

	@Test
	public void updateVesselAvailabilitiesTest_VesselEvent3() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

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

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");
		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		final VesselEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event1", port4, PeriodTestUtils.createLocalDateTime(2014, Calendar.APRIL, 2, 0), 30);
		final VesselEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "event2", port2, PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), 30);
		event1.setVesselAssignmentType(vesselAvailability);
		event2.setVesselAssignmentType(vesselAvailability);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)) //
				.forVesselEvent(event1) //
				.forVesselEvent(event2) //
				.withEndEvent(port3, PeriodTestUtils.createDate(2014, Calendar.JULY, 30)) //
				.make() //
				.make();

		final CollectedAssignment collectedAssignment = PeriodTestUtils.createCollectedAssignment(vesselAvailability, event1, event2);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		final EndEvent endEvent = (EndEvent) schedule.getSequences().get(0).getEvents().get(schedule.getSequences().get(0).getEvents().size() - 1);
		transformer.updateVesselAvailability(collectedAssignment, endEvent, records, periodRecord, mapping);

		Assertions.assertNull(vesselAvailability.getStartAt());
		Assertions.assertNull(vesselAvailability.getStartAfter());
		Assertions.assertNull(vesselAvailability.getStartBy());

		// Should match start of event 2
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), vesselAvailability.getEndAfter());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JUNE, 1, 0), vesselAvailability.getEndBy());
		Assertions.assertEquals(Collections.singletonList(port2), vesselAvailability.getEndAt());
	}

	@Test
	public void updateStartConditionsTest_RealInclusionChecker() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

		final Port port1 = Mockito.mock(Port.class, "port1");
		final Port port2 = Mockito.mock(Port.class, "port2");
		final Port port3 = Mockito.mock(Port.class, "port3");

		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final CharterOutEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "C1");
		event1.setPort(port1);
		event1.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JULY, 8).toLocalDateTime());
		event1.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JULY, 8).toLocalDateTime());
		event1.setDurationInDays(1);

		final CharterOutEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "C2");
		event2.setPort(port2);
		event2.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JULY, 18).toLocalDateTime());
		event2.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JULY, 18).toLocalDateTime());
		event2.setDurationInDays(1);

		final CharterOutEvent event3 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "C3");
		event3.setPort(port3);
		event3.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JULY, 28).toLocalDateTime());
		event3.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JULY, 28).toLocalDateTime());
		event3.setDurationInDays(1);

		final VesselAvailability vesselAvailability1 = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability1.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
		vesselAvailability1.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

		event1.setVesselAssignmentType(vesselAvailability1);
		event2.setVesselAssignmentType(vesselAvailability1);
		event3.setVesselAssignmentType(vesselAvailability1);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JULY, 1)) //
				.forVesselEvent(event1, vev -> vev.setHeelAtStart(10000)) //
				.forVesselEvent(event2, vev -> vev.setHeelAtStart(20000)) //
				.forVesselEvent(event3, vev -> vev.setHeelAtStart(30000)) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.AUGUST, 30)) //
				.make() // Sequence
				.make(); // Schedule

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.AUGUST, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.SEPTEMBER, 30);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());

		final List<CollectedAssignment> collectedAssignments = new LinkedList<>();
		collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability1, event1, event2, event3));

		transformer.updateVesselAvailabilities(collectedAssignments, schedule, records, periodRecord, mapping);

		Assertions.assertEquals(port3, vesselAvailability1.getStartAt());
		Assertions.assertEquals(30000.0, vesselAvailability1.getStartHeel().getMinVolumeAvailable(), 0.001);
		Assertions.assertEquals(30000.0, vesselAvailability1.getStartHeel().getMaxVolumeAvailable(), 0.001);
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 28, 0), vesselAvailability1.getStartBy());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 28, 0), vesselAvailability1.getStartAfter());

	}

	@Test
	public void updateEndConditionsTest_RealInclusionChecker() {

		final InclusionChecker inclusionChecker = new InclusionChecker();

		final PeriodTransformer transformer = createPeriodTransformer(inclusionChecker);

		final IScenarioEntityMapping mapping = new ScenarioEntityMapping();

		final Port port1 = Mockito.mock(Port.class, "port1");
		final Port port2 = Mockito.mock(Port.class, "port2");
		final Port port3 = Mockito.mock(Port.class, "port3");

		final LNGScenarioModel scenarioModel = PeriodTestUtils.createBasicScenario();

		final CharterOutEvent event1 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "C1");
		event1.setPort(port1);
		event1.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JULY, 8).toLocalDateTime());
		event1.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JULY, 8).toLocalDateTime());
		event1.setDurationInDays(1);

		final CharterOutEvent event2 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "C2");
		event2.setPort(port2);
		event2.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JULY, 18).toLocalDateTime());
		event2.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JULY, 18).toLocalDateTime());
		event2.setDurationInDays(1);

		final CharterOutEvent event3 = PeriodTestUtils.createCharterOutEvent(scenarioModel, "C3");
		event3.setPort(port3);
		event3.setStartAfter(PeriodTestUtils.createDate(2014, Calendar.JULY, 28).toLocalDateTime());
		event3.setStartBy(PeriodTestUtils.createDate(2014, Calendar.JULY, 28).toLocalDateTime());
		event3.setDurationInDays(1);

		final VesselAvailability vesselAvailability1 = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability1.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
		vesselAvailability1.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

		event1.setVesselAssignmentType(vesselAvailability1);
		event2.setVesselAssignmentType(vesselAvailability1);
		event3.setVesselAssignmentType(vesselAvailability1);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)) //
				.forVesselEvent(event1, vev -> vev.setHeelAtStart(10000)) //
				.forVesselEvent(event2, vev -> vev.setHeelAtStart(20000)) //
				.forVesselEvent(event3, vev -> vev.setHeelAtStart(30000)) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.AUGUST, 30)) //
				.make() // Sequence
				.make(); // Schedule

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JUNE, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JUNE, 10);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JUNE, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JUNE, 30);

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());

		final List<CollectedAssignment> collectedAssignments = new LinkedList<>();
		collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(vesselAvailability1, event1, event2, event3));

		transformer.updateVesselAvailabilities(collectedAssignments, schedule, records, periodRecord, mapping);

		Assertions.assertEquals(Collections.singletonList(port1), vesselAvailability1.getEndAt());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0), vesselAvailability1.getEndBy());
		Assertions.assertEquals(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0), vesselAvailability1.getEndAfter());
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

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);
		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(copyCargo) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() // Sequence
				.make(); // Schedule

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToLockdown, records.get(copyCargo).status);
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

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(copyCargo) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() // Sequence
				.make(); // Schedule

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToLockdown, records.get(copyCargo).status);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.MAY, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JULY, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);
		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(copyCargo) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToKeep, records.get(copyCargo).status);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.OCTOBER, 1));
		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.NOVEMBER, 1));
		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(copyCargo) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToRemove, records.get(copyCargo).status);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");

		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.JANUARY, 1));

		final DischargeSlot copyDischargeSlot = PeriodTestUtils.createDischargeSlot(copyScenarioModel, "discharge");
		copyDischargeSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));

		final Cargo copyCargo = PeriodTestUtils.createCargo(copyScenarioModel, copyLoadSlot, copyDischargeSlot);
		copyCargo.setAllowRewiring(true);

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forCargo(copyCargo) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() // Sequence
				.make(); // Schedule

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());

		Assertions.assertEquals(Status.ToLockdown, records.get(copyCargo).status);
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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());

		Assertions.assertEquals(Status.ToRemove, records.get(copyLoadSlot).status);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final LoadSlot copyLoadSlot = PeriodTestUtils.createLoadSlot(copyScenarioModel, "load");
		copyLoadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.APRIL, 1));

		final Schedule schedule = new SimpleScheduleBuilder() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());

		Assertions.assertEquals(Status.ToRemove, records.get(copyLoadSlot).status);
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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToKeep, records.get(copyLoadSlot).status);
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

		final Set<Slot<?>> seenSlots = new HashSet<>();
		final Set<Slot<?>> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();
		final Map<Slot<?>, SlotAllocation> slotAllocationMap = new HashMap<>();

		final Schedule schedule = new SimpleScheduleBuilder() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToRemove, records.get(copyLoadSlot).status);
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

		final LoadSlot loadSlot = PeriodTestUtils.createLoadSlot(scenarioModel, "load");
		loadSlot.setWindowStart(PeriodTestUtils.createLocalDate(2014, Calendar.DECEMBER, 1));

		final Schedule schedule = new SimpleScheduleBuilder() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());

		Assertions.assertEquals(Status.ToRemove, records.get(loadSlot).status);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.JANUARY, 1, 0));
		copyEvent.setDurationInDays(10);

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);
		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forVesselEvent(copyEvent) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToRemove, records.get(copyEvent).status);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.MARCH, 1, 0));
		copyEvent.setDurationInDays(20);

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);
		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forVesselEvent(copyEvent) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToLockdown, records.get(copyEvent).status);
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

		// Create a sample scenario
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.SEPTEMBER, 1, 0));
		copyEvent.setDurationInDays(20);

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);
		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forVesselEvent(copyEvent) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToLockdown, records.get(copyEvent).status);
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
		final LNGScenarioModel copyScenarioModel = PeriodTestUtils.createBasicScenario();

		final VesselEvent copyEvent = PeriodTestUtils.createCharterOutEvent(copyScenarioModel, "event");
		copyEvent.setStartAfter(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		copyEvent.setStartBy(PeriodTestUtils.createLocalDateTime(2014, Calendar.OCTOBER, 1, 0));
		copyEvent.setDurationInDays(10);

		final Port port1 = PeriodTestUtils.createPort(copyScenarioModel, "Port");

		final Vessel vessel1 = PeriodTestUtils.createVessel(copyScenarioModel, "Vessel1");

		final VesselAvailability vesselAvailability1 = PeriodTestUtils.createVesselAvailability(copyScenarioModel, vessel1);
		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability1) //
				.withStartEvent(port1, PeriodTestUtils.createDate(2014, Calendar.JANUARY, 1)) //
				.forVesselEvent(copyEvent) //
				.withEndEvent(port1, PeriodTestUtils.createDate(2014, Calendar.DECEMBER, 1)) //
				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, copyScenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToRemove, records.get(copyEvent).status);

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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(copyVesselAvailability) //
				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new LinkedList<>();
		collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(copyVesselAvailability));

		transformer.updateVesselAvailabilities(collectedAssignments, schedule, records, periodRecord, mapping);
		transformer.removeVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assertions.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));

		Assertions.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().isEmpty());

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
		Assertions.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));
		final Schedule schedule = new SimpleScheduleBuilder() //

				.withSequence(copyVesselAvailability) //

				.make() //
				.make();

		final Map<EObject, InclusionRecord> records = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new LinkedList<>();
		collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(copyVesselAvailability));

		transformer.updateVesselAvailabilities(collectedAssignments, schedule, records, periodRecord, mapping);
		transformer.removeVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assertions.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assertions.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(copyVesselAvailability) //
				.make() // Sequence
				.make(); // Schedule

		final Map<EObject, InclusionRecord> records = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new LinkedList<>();
		collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(copyVesselAvailability));

		transformer.updateVesselAvailabilities(collectedAssignments, schedule, records, periodRecord, mapping);
		transformer.removeVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assertions.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assertions.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(copyVesselAvailability) //
				.make() // Sequence
				.make(); // Schedule

		final Map<EObject, InclusionRecord> records = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new LinkedList<>();
		collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(copyVesselAvailability));

		transformer.updateVesselAvailabilities(collectedAssignments, schedule, records, periodRecord, mapping);
		transformer.removeVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assertions.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));

		Assertions.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().isEmpty());

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

		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(copyVesselAvailability) //
				.make() // Sequence
				.make(); // Schedule

		final Map<EObject, InclusionRecord> records = new HashMap<>();
		final List<CollectedAssignment> collectedAssignments = new LinkedList<>();
		collectedAssignments.add(PeriodTestUtils.createCollectedAssignment(copyVesselAvailability));

		transformer.updateVesselAvailabilities(collectedAssignments, schedule, records, periodRecord, mapping);
		transformer.removeVesselAvailabilities(PeriodTestUtils.createEditingDomain(copyScenarioModel), periodRecord, copyScenarioModel.getCargoModel(), mapping);

		// No change to original
		Assertions.assertTrue(scenarioModel.getCargoModel().getVesselAvailabilities().contains(vesselAvailability));
		Assertions.assertTrue(copyScenarioModel.getCargoModel().getVesselAvailabilities().contains(copyVesselAvailability));

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

		// Second scenario should really be different.
		transformer.trimSpotMarketCurves(periodRecord, scenarioModel, null);

		for (final SpotMarketGroup group : new SpotMarketGroup[] { scenarioModel.getReferenceModel().getSpotMarketsModel().getDesPurchaseSpotMarket(),
				scenarioModel.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket(), scenarioModel.getReferenceModel().getSpotMarketsModel().getFobPurchasesSpotMarket(),
				scenarioModel.getReferenceModel().getSpotMarketsModel().getFobSalesSpotMarket(), }) {
			final EList<SpotMarket> markets = group.getMarkets();
			final SpotMarket market = markets.get(0);
			final SpotAvailability availability = market.getAvailability();
			Assertions.assertTrue(availability.isSetConstant() == false);
			Assertions.assertTrue(availability.getCurve().getPoints().size() > 0);
			for (final IndexPoint<Integer> point : availability.getCurve().getPoints()) {
				final YearMonth date = point.getDate();
				final ZonedDateTime dateAsDateTime = date.atDay(1).atStartOfDay(ZoneId.of("UTC"));
				Assertions.assertTrue(dateAsDateTime.isAfter(periodRecord.lowerCutoff));
				Assertions.assertTrue(dateAsDateTime.isBefore(periodRecord.upperBoundary));
			}
			Assertions.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 4)) == 5);
			Assertions.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 5)) == 5);
			Assertions.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 6)) == 2);
			Assertions.assertTrue(availability.getCurve().getValueForMonth(DateAndCurveHelper.createYearMonth(2015, 6)) == 2);
		}
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

		final Set<Slot<?>> removedSlots = new HashSet<>();
		final Set<Cargo> removedCargoes = new HashSet<>();

		removedSlots.add(copyLoadSlot2);
		removedSlots.add(copyDischargeSlot2);
		removedCargoes.add(copyCargo2);

		final Map<EObject, InclusionRecord> records = new HashMap<>();
		{
			final InclusionRecord r = new InclusionRecord();
			r.object = copyCargo2;
			r.status = Status.ToRemove;
			records.put(copyCargo2, r);
		}

		transformer.removeExcludedObjects(PeriodTestUtils.createEditingDomain(copyScenarioModel), mapping, records);

		// Removed from copy
		Assertions.assertTrue(copyScenarioModel.getCargoModel().getCargoes().contains(copyCargo1));
		Assertions.assertTrue(copyScenarioModel.getCargoModel().getLoadSlots().contains(copyLoadSlot1));
		Assertions.assertTrue(copyScenarioModel.getCargoModel().getDischargeSlots().contains(copyDischargeSlot1));

		Assertions.assertFalse(copyScenarioModel.getCargoModel().getCargoes().contains(copyCargo2));
		Assertions.assertFalse(copyScenarioModel.getCargoModel().getLoadSlots().contains(copyLoadSlot2));
		Assertions.assertFalse(copyScenarioModel.getCargoModel().getDischargeSlots().contains(copyDischargeSlot2));

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

		final LoadSlot loadSlot1 = PeriodTestUtils.createLoadSlot(scenarioModel, "load1");
		final DischargeSlot dischargeSlot1 = PeriodTestUtils.createDischargeSlot(scenarioModel, "discharge1");
		final Cargo cargo1 = PeriodTestUtils.createCargo(scenarioModel, loadSlot1, dischargeSlot1);

		final Port port = PeriodTestUtils.createPort(scenarioModel, "port");
		port.getLocation().setTimeZone("Etc/GMT-1");

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

		// set flags
		cargo1.setAllowRewiring(true);
		cargo1.setLocked(false);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.MARCH, 15);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2015, Calendar.APRIL, 15);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2015, Calendar.AUGUST, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2015, Calendar.SEPTEMBER, 15);

		final Vessel vessel = PeriodTestUtils.createVessel(scenarioModel, "Vessel");

		final VesselAvailability vesselAvailability = PeriodTestUtils.createVesselAvailability(scenarioModel, vessel);

		// Cargo between lower cutoff and lower boundary
		final Schedule schedule = new SimpleScheduleBuilder() //
				.withSequence(vesselAvailability) //
				.withStartEvent(port, PeriodTestUtils.createDate(2014, Calendar.APRIL, 1)) //
				.withCargoAllocation() //
				.forSlot(loadSlot1, PeriodTestUtils.createDate("UTC", 2014, 10, 5, 1)) //
				.forSlot(dischargeSlot1, PeriodTestUtils.createDate("UTC", 2014, 11, 10, 2)) //
				.make() // Cargo Allocation
				.withEndEvent(port, PeriodTestUtils.createDate(2014, Calendar.JUNE, 30)) //
				.make() // Sequence
				.make(); // Schedule

		final Map<EObject, InclusionRecord> records = transformer.generateInclusionRecords(schedule, scenarioModel.getCargoModel(), periodRecord, new LinkedHashMap<>(), new LinkedList<>());
		Assertions.assertEquals(Status.ToLockdown, records.get(cargo1).status);

		transformer.lockDownRecords(periodRecord, records);

		Assertions.assertFalse(cargo1.isAllowRewiring());
		Assertions.assertTrue(cargo1.isLocked());

		Assertions.assertEquals(PeriodTestUtils.createLocalDate(2014, 10, 5), loadSlot1.getWindowStart());
		Assertions.assertEquals(PeriodTestUtils.createLocalDate(2014, 11, 10), dischargeSlot1.getWindowStart());

		Assertions.assertEquals(0, loadSlot1.getWindowSize());
		Assertions.assertEquals(0, dischargeSlot1.getWindowSize());

		Assertions.assertEquals(1, loadSlot1.getWindowStartTime());
		Assertions.assertEquals(2, dischargeSlot1.getWindowStartTime());
	}

	private PeriodTransformer createPeriodTransformer(final InclusionChecker inclusionChecker) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(InclusionChecker.class).toInstance(inclusionChecker);
				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseHeelRetention))//
				.toInstance(Boolean.FALSE);
			}
		});
		return injector.getInstance(PeriodTransformer.class);
	}
}
