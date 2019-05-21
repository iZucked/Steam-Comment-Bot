/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.provider.CargoItemProviderAdapterFactory;
import com.mmxlabs.models.lng.fleet.provider.FleetItemProviderAdapterFactory;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.scenario.actions.impl.FixSlotWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FixVesselEventWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeCargoChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeSlotChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeVesselEventChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveCargoChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveSlotChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveVesselEventChange;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class RollForwardEngineTest {

	/**
	 * Helper class, used to encapsulate a test data model and the expected results. 
	 * 
	 * @author simonmcgregor
	 *
	 */
	private static class SlotCargoModelTestData 
	{
		final public Set<Slot<?>> slotsExpectedToBeRemoved = new HashSet<>();
		final public Set<Slot<?>> slotsExpectedToBeFrozen = new HashSet<>();
		final public Set<Cargo> cargoesExpectedToBeRemoved = new HashSet<>();
		final public Set<Cargo> cargoesExpectedToBeFrozen = new HashSet<>();
		
		final public LocalDate freezeCalendar;
		final public LocalDate removeCalendar;
		
		final public CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
			
		
		public SlotCargoModelTestData(LocalDate freezeDate, LocalDate removeDate) {
			freezeCalendar = freezeDate;
			removeCalendar = removeDate;
		}
				
	}
	
	/**
	 * Creates a load or discharge slot relative to a particular date. 
	 * 
	 * @param isLoadSlot Whether to create a load slot (a discharge slot is created otherwise)
	 * @param cargoModel The data model to create the slot in
	 * @param name The name to give the slot
	 * @param date The date that the slot is created relative to
	 * @param daysEarlierForStart The number of days the slot start time occurs earlier than the specified date (can be negative)
	 * @param durationInHours The duration of the slot
	 * @return The slot created.
	 */
	protected Slot<?> createRelativeSlot(boolean isLoadSlot, CargoModel cargoModel, String name, LocalDate date, int daysEarlierForStart, int durationInHours) {
		Slot<?> result = isLoadSlot ? CargoFactory.eINSTANCE.createLoadSlot() : CargoFactory.eINSTANCE.createDischargeSlot();
		
		result.setName(name);
		result.setWindowStart(date.minusDays(daysEarlierForStart));
		result.setWindowSize(durationInHours);
		
		if (isLoadSlot) {
			cargoModel.getLoadSlots().add((LoadSlot) result);
		}
		else {
			cargoModel.getDischargeSlots().add((DischargeSlot) result);
		}				
		
		return result;
	}
		
	/**
	 * Helper data class for createRelativeSlot method
	 * 
	 * @author simonmcgregor
	 *
	 */
	private static class NamedDate {
		public LocalDate date;
		public String name;
		
		public NamedDate(String name, LocalDate date) {
			this.name = name;
			this.date = date;
		}
	}
	
	/**
	 * Helper enumeration for createRelativeSlot method. Each time relation has a name, a relative number of days earlier, and a duration.
	 * @author simonmcgregor
	 *
	 */
	private enum TimeRelation {
		WELL_BEFORE, BEFORE, OVERLAPPING, AFTER, WELL_AFTER;
		
		public String getName() {
			switch (this) {
			case WELL_BEFORE: return "WellBefore";
			case BEFORE: return "Before";
			case OVERLAPPING: return "Overlapping";
			case AFTER: return "After";
			case WELL_AFTER: return "WellAfter";
			default: return null;
			}
		}
		
		public int getDaysBefore() {
			switch (this) {
			case WELL_BEFORE: return 4;
			case BEFORE: return 2;
			case OVERLAPPING: return 1;
			case AFTER: return -1;
			case WELL_AFTER: return -4;
			default: return 0;
			}			
		}

		public int getDurationInHours() {
			switch (this) {
			case WELL_BEFORE: 
			case AFTER: 
			case WELL_AFTER: 
			case BEFORE: return 24;
			case OVERLAPPING: return 48;
			default: return 0;
			}			
		}
	};

	/**
	 * Helper class to provide unique names by appending consecutive numbers to successive usages of the same name.
	 * Will not work properly if names are used that already have a numeric suffix.
	 * 
	 * @author simonmcgregor
	 *
	 */
	private static class NameScheme {
		private Map<String, Integer> nameVersions = new HashMap<>();
		
		public String getUniqueName(String name) {
			int version = 1;
			
			if (nameVersions.containsKey(name)) {
				version = nameVersions.get(name) + 1;
			}
			
			nameVersions.put(name, version);
			
			return String.format("%s%d", name, version);			
		}
	}
	
	/**
	 * Creates a new slot in the specified time relation to the particular date. Uses the slot type, the time relation, and the name of the date to 
	 * generate a name for the slot.
	 * 
	 * @param isLoadSlot
	 * @param cargoModel
	 * @param timeRelation
	 * @param namedDate
	 * @return
	 */
	protected Slot<?> createRelativeSlot(boolean isLoadSlot, CargoModel cargoModel, TimeRelation timeRelation, NamedDate namedDate, NameScheme nameScheme) {
		String loadDischarge = isLoadSlot ? "Load" : "Discharge";
		String timeRelationString = timeRelation.getName();
		int daysBefore = timeRelation.getDaysBefore();
		int durationInHours = timeRelation.getDurationInHours();
		
		String name = String.format("%sSlot%s%s", loadDischarge, timeRelationString, namedDate.name);
		if (nameScheme != null) {
			name = nameScheme.getUniqueName(name);
		}
		return createRelativeSlot(isLoadSlot, cargoModel, name, namedDate.date, daysBefore, durationInHours);
	}
	
	/**
	 * Convenience method to create a LoadSlot relative to a particular date
	 * @param cargoModel
	 * @param timeRelation
	 * @param namedDate
	 * @param nameScheme
	 * @return
	 */
	protected LoadSlot createRelativeLoadSlot(CargoModel cargoModel, TimeRelation timeRelation, NamedDate namedDate, NameScheme nameScheme) {
		return (LoadSlot) createRelativeSlot(true, cargoModel, timeRelation, namedDate, nameScheme);
	}
	
	/**
	 * Convenience method to create a LoadSlot relative to a particular date
	 * @param cargoModel
	 * @param timeRelation
	 * @param namedDate
	 * @param nameScheme
	 * @return
	 */
	protected DischargeSlot createRelativeDischargeSlot(CargoModel cargoModel, TimeRelation timeRelation, NamedDate namedDate, NameScheme nameScheme) {
		return (DischargeSlot) createRelativeSlot(false, cargoModel, timeRelation, namedDate, nameScheme);
	}

	/**
	 * Creates a cargo with load and discharge slots as specified relative to the specified date.
	 * @param cargoModel
	 * @param loadTime
	 * @param dischargeTime
	 * @param namedDate
	 * @param nameScheme
	 * @return
	 */
	protected Cargo createRelativeCargo(CargoModel cargoModel, TimeRelation loadTime, TimeRelation dischargeTime, NamedDate namedDate, NameScheme nameScheme) {
		final LoadSlot loadSlot = createRelativeLoadSlot(cargoModel, loadTime, namedDate, nameScheme);
		final DischargeSlot dischargeSlot = createRelativeDischargeSlot(cargoModel, dischargeTime, namedDate, nameScheme);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		cargoModel.getCargoes().add(cargo);
		
		return cargo;		
	}
	
	/**
	 * The following method is obsolete, since we no longer remove data when we roll forward, but should be retained in case we 
	 * want to reinstate the removal logic.
	 * 
	 * @return
	 */
	protected SlotCargoModelTestData generateSlotCargoDataWithRemovals() 
	{	
		final SlotCargoModelTestData result = new SlotCargoModelTestData( LocalDate.of(2013, 9, 1), LocalDate.of(2013, 8, 1) );
		
		final NamedDate freezeDate = new NamedDate("Freeze", result.freezeCalendar);
		final NamedDate removeDate = new NamedDate("Remove", result.removeCalendar);

		final LocalDate freezeCalendar = result.freezeCalendar;
		final LocalDate removeCalendar = result.removeCalendar;
		
		final CargoModel cargoModel = result.cargoModel;
		
		Set<Slot<?>> slotsExpectedToBeRemoved = result.slotsExpectedToBeRemoved;
		Set<Slot<?>> slotsExpectedToBeFrozen = result.slotsExpectedToBeFrozen;
		
		Set<Cargo> cargoesExpectedToBeRemoved = result.cargoesExpectedToBeRemoved; 
		Set<Cargo> cargoesExpectedToBeFrozen = result.cargoesExpectedToBeFrozen; 
				
		
		NameScheme nameScheme = new NameScheme(); // used to give the slots unique names
		
		// Generate the Data model
		{
			// Load Slot before remove date
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.BEFORE, removeDate, nameScheme);
				slotsExpectedToBeRemoved.add(slot);
			}

			// Load Slot partially over remove date
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.OVERLAPPING, removeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);
			}

			// Load Slot before freeze date
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.BEFORE, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);
			}

			// Load Slot partially over freeze date
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);
			}

			// Load Slot clear of both dates
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.AFTER, freezeDate, nameScheme);
			}
			
			// Discharge Slot before remove date
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.BEFORE, removeDate, nameScheme);
				slotsExpectedToBeRemoved.add(slot);
			}

			// Discharge Slot partially over remove date
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.OVERLAPPING, removeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);

			}

			// Discharge Slot before freeze date
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.BEFORE, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);

			}

			// Discharge Slot partially over freeze date
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);
			}

			// Discharge Slot clear of both dates
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.AFTER, freezeDate, nameScheme);
			}

			// Cargo before remove date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.WELL_BEFORE, TimeRelation.BEFORE, removeDate, nameScheme);
				cargoesExpectedToBeRemoved.add(cargo);
			}

			// Cargo one slot before, one slot after remove date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.BEFORE, TimeRelation.AFTER, removeDate, nameScheme);
				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo one slot before remove date, one slot partially over remove date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.BEFORE, TimeRelation.OVERLAPPING, removeDate, nameScheme);
				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo before freeze date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.WELL_BEFORE, TimeRelation.BEFORE, freezeDate, nameScheme);
				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo one slot before, one slot after freeze date
			{

				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.BEFORE, TimeRelation.AFTER, freezeDate, nameScheme);
				// As of May 2019, cargoes "on the water" are *not* frozen. This is a change from previous logic
			}

			// Cargo one slot before freeze date, one slot partially over freeze date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.BEFORE, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				// As of May 2019, cargoes "on the water" are *not* frozen. This is a change from previous logic
			}

			// Cargo clear of both dates
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.AFTER, TimeRelation.WELL_AFTER, freezeDate, nameScheme);
				// don't freeze or remove this cargo
			}
		}
		
		return result;
	}

	/**
	 * Returns slot cargo data and expected test results.
	 * 
	 * @return
	 */
	protected SlotCargoModelTestData generateSlotCargoData() 
	{	
		final SlotCargoModelTestData result = new SlotCargoModelTestData( LocalDate.of(2013, 9, 1), LocalDate.of(2013, 8, 1) );
		
		final NamedDate freezeDate = new NamedDate("Freeze", result.freezeCalendar);

		final CargoModel cargoModel = result.cargoModel;
		
		Set<Slot<?>> slotsExpectedToBeFrozen = result.slotsExpectedToBeFrozen;
		
		Set<Cargo> cargoesExpectedToBeFrozen = result.cargoesExpectedToBeFrozen; 
				
		
		NameScheme nameScheme = new NameScheme(); // used to give the slots unique names
		
		// Generate the Data model
		{

			// Load Slot before freeze date
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.BEFORE, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);
			}

			// Load Slot partially over freeze date
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);
			}

			// Load Slot clear of freeze date
			{
				LoadSlot slot = createRelativeLoadSlot(cargoModel, TimeRelation.AFTER, freezeDate, nameScheme);
			}
			

			// Discharge Slot before freeze date
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.BEFORE, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);

			}

			// Discharge Slot partially over freeze date
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				slotsExpectedToBeFrozen.add(slot);
			}

			// Discharge Slot clear of freeze date
			{
				DischargeSlot slot = createRelativeDischargeSlot(cargoModel, TimeRelation.AFTER, freezeDate, nameScheme);
			}


			// Cargo before freeze date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.WELL_BEFORE, TimeRelation.BEFORE, freezeDate, nameScheme);
				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo one slot before, one slot after freeze date
			{

				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.BEFORE, TimeRelation.AFTER, freezeDate, nameScheme);
				// As of May 2019, cargoes "on the water" are *not* frozen. This is a change from previous logic
			}

			// Cargo one slot before freeze date, one slot partially over freeze date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.BEFORE, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				// As of May 2019, cargoes "on the water" are *not* frozen. This is a change from previous logic
			}

			// Cargo clear of freeze date
			{
				Cargo cargo = createRelativeCargo(cargoModel, TimeRelation.AFTER, TimeRelation.WELL_AFTER, freezeDate, nameScheme);
				// don't freeze or remove this cargo
			}
		}
		
		return result;
	}

	
	/**
	 * This test is disabled as it refers to obsolete code, which employs removal logic as well as freezing logic.
	 */
	@Test
	@Disabled
	public void examineSlotsAndCargoesTest() {		
		SlotCargoModelTestData testData = generateSlotCargoDataWithRemovals();

		final RollForwardEngine engine = new RollForwardEngine();

		final List<Slot<?>> slotsToRemove = new LinkedList<>();
		final List<Slot<?>> slotsToFreeze = new LinkedList<>();
		final List<Cargo> cargoesToRemove = new LinkedList<>();
		final List<Cargo> cargoesToFreeze = new LinkedList<>();

		engine.examineSlotsAndCargoesObsolete(testData.freezeCalendar, testData.removeCalendar, testData.cargoModel, slotsToRemove, slotsToFreeze, cargoesToFreeze, cargoesToRemove);

		// Check lists contain expected items
		Assert.assertEquals(testData.slotsExpectedToBeRemoved, new HashSet<>(slotsToRemove));
		Assert.assertEquals(testData.slotsExpectedToBeFrozen, new HashSet<>(slotsToFreeze));
		Assert.assertEquals(testData.cargoesExpectedToBeRemoved, new HashSet<>(cargoesToRemove));
		Assert.assertEquals(testData.cargoesExpectedToBeFrozen, new HashSet<>(cargoesToFreeze));
	}
	
	@Test
	/**
	 * Test for getSlotsToFreeze() and getCargoesToFreeze()
	 */
	public void slotsAndCargoesToFreezeTest( ) {
		final SlotCargoModelTestData slotCargoTestData = generateSlotCargoData();

		final RollForwardEngine engine = new RollForwardEngine();

		final List<Slot<?>> slotsToFreeze = engine.getSlotsToFreeze(slotCargoTestData.freezeCalendar, slotCargoTestData.cargoModel);
		final List<Cargo> cargoesToFreeze = engine.getCargoesToFreeze(slotCargoTestData.freezeCalendar, slotCargoTestData.cargoModel);
		

		// Check lists contain expected items
		Assert.assertEquals("Expected slots are not frozen.", slotCargoTestData.slotsExpectedToBeFrozen, new HashSet<>(slotsToFreeze));
		Assert.assertEquals("Expected cargoes are not frozen.", slotCargoTestData.cargoesExpectedToBeFrozen, new HashSet<>(cargoesToFreeze));		
	}
	
	@Test
	/**
	 * Test for getEventsToFreeze()
	 */
	public void eventsToFreezeTest() {
		final RollForwardEngine engine = new RollForwardEngine();

		final VesselEventTestData vesselEventTestData = generateVesselEventTestData();
		
		List<VesselEvent> eventsToFreeze = engine.getEventsToFreeze(vesselEventTestData.freezeCalendar, vesselEventTestData.cargoModel);
		// Check lists contain expected items
		Assert.assertEquals("Expected events are not frozen.", vesselEventTestData.eventsExpectedToBeFrozen, new HashSet<>(eventsToFreeze));
	}
	
	
	/**
	 * Helper class, used to encapsulate a test data model and the expected results. 
	 * 
	 * @author simonmcgregor
	 *
	 */
	private static class VesselEventTestData
	{
		public final LocalDate freezeCalendar;
		public final LocalDate removeCalendar;
		private final CargoModel cargoModel;

		public final Set<VesselEvent> eventsExpectedToBeRemoved = new HashSet<>();
		public final Set<VesselEvent> eventsExpectedToBeFrozen = new HashSet<>();

		public VesselEventTestData(LocalDate freezeDate, LocalDate removeDate) {
			freezeCalendar = freezeDate;
			removeCalendar = removeDate;
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();
			
		}
		
	}
	
	protected VesselEvent createRelativeVesselEvent(CargoModel cargoModel, TimeRelation timeRelation, NamedDate namedDate, NameScheme nameScheme) {
		final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
		
		String name = String.format("Event%s%s", timeRelation, namedDate.name);
		if (nameScheme != null) {
			name = nameScheme.getUniqueName(name);
		}
		event.setName(name);

		final LocalDateTime dt = namedDate.date.atStartOfDay();
		int daysBefore = timeRelation.getDaysBefore();
		event.setStartAfter(dt.minusDays( daysBefore ));
		event.setStartBy(dt.minusDays( daysBefore - 1 ));
		
		event.setDurationInDays(timeRelation.getDurationInHours() / 24);

		cargoModel.getVesselEvents().add(event);
		
		return event;		
	}
	
	
	
	
	/**
	 * Obsolete method testing removal logic. Retained in case we decide to reinstate the removal logic.
	 * @return
	 */
	public VesselEventTestData generateVesselEventTestDataWithRemoval() {
		LocalDate freezeCalendar = LocalDate.of(2013, 9, 1);
		LocalDate removeCalendar = LocalDate.of(2013, 8, 1);

		VesselEventTestData result = new VesselEventTestData(freezeCalendar, removeCalendar);
		final Set<VesselEvent> eventsExpectedToBeRemoved = result.eventsExpectedToBeRemoved;
		final Set<VesselEvent> eventsExpectedToBeFrozen = result.eventsExpectedToBeFrozen;
		final CargoModel cargoModel = result.cargoModel;
		
		NameScheme nameScheme = new NameScheme(); // keep names unique
		NamedDate freezeDate = new NamedDate("Freeze", freezeCalendar);
		NamedDate removeDate = new NamedDate("Remove", removeCalendar);

		// Generate the Data model
		{
			// Event fully before remove and freeze date
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.BEFORE, removeDate, nameScheme);
				eventsExpectedToBeRemoved.add(event);

			}

			// Event fully before freeze, but not remove date
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.BEFORE, freezeDate, nameScheme);
				eventsExpectedToBeFrozen.add(event);
			}

			// Event partially over remove date
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.OVERLAPPING, removeDate, nameScheme);
				eventsExpectedToBeFrozen.add(event);
			}

			// Event partially over freeze date
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				eventsExpectedToBeFrozen.add(event);

			}

			// Event clear of both dates
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.AFTER, freezeDate, nameScheme);				
			}

		}
		
		
		return result;
	}
	
	/**
	 * Returns test data (including expected results) for vessel event freezing.
	 * @return
	 */
	public  VesselEventTestData generateVesselEventTestData() {
		LocalDate freezeCalendar = LocalDate.of(2013, 9, 1);
		LocalDate removeCalendar = LocalDate.of(2013, 8, 1);

		VesselEventTestData result = new VesselEventTestData(freezeCalendar, removeCalendar);
		final Set<VesselEvent> eventsExpectedToBeFrozen = result.eventsExpectedToBeFrozen;
		final CargoModel cargoModel = result.cargoModel;
		
		NameScheme nameScheme = new NameScheme(); // keep names unique
		NamedDate freezeDate = new NamedDate("Freeze", freezeCalendar);

		// Generate the Data model
		{
			// Event fully before freeze, but not remove date
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.BEFORE, freezeDate, nameScheme);
				eventsExpectedToBeFrozen.add(event);
			}

			// Event partially over freeze date
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.OVERLAPPING, freezeDate, nameScheme);
				eventsExpectedToBeFrozen.add(event);

			}

			// Event clear of both dates
			{
				VesselEvent event = createRelativeVesselEvent(cargoModel, TimeRelation.AFTER, freezeDate, nameScheme);				
			}

		}
		
		
		return result;
	}

	/**
	 * This test is disabled as it refers to obsolete code, which employs removal logic as well as freezing logic.
	 */
	@Test
	@Disabled
	public void examineVesselEventsTest() {
		
		VesselEventTestData testData = generateVesselEventTestDataWithRemoval();

		final RollForwardEngine engine = new RollForwardEngine();

		final List<VesselEvent> eventsToRemove = new LinkedList<>();
		final List<VesselEvent> eventsToFreeze = new LinkedList<>();

		engine.examineVesselEventsObsolete(testData.freezeCalendar, testData.removeCalendar, testData.cargoModel, eventsToRemove, eventsToFreeze);

		// Check lists contain expected items
		Assert.assertEquals(testData.eventsExpectedToBeRemoved, new HashSet<>(eventsToRemove));
		Assert.assertEquals(testData.eventsExpectedToBeFrozen, new HashSet<>(eventsToFreeze));
	}

	private enum CargoAndEventChangesType {
		EVENT_TO_REMOVE, EVENT_TO_FREEZE, SLOT_TO_REMOVE, SLOT_TO_FREEZE, CARGO_TO_REMOVE, CARGO_TO_FREEZE

	}

	@Test
	@Disabled // removal logic is obsolete
	public void generateCargoAndEventChangesTest_SlotToRemove() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTestWithRemovals(CargoAndEventChangesType.SLOT_TO_REMOVE, slot, null);

		Assert.assertEquals(1, changes.size());
		final IRollForwardChange change = changes.get(0);

		Assert.assertTrue(change instanceof RemoveSlotChange);

		final RemoveSlotChange removeSlotChange = (RemoveSlotChange) change;
		Assert.assertSame(slot, removeSlotChange.getChangedObject());
	}

	@Test
	@Disabled // uses obsolete removals logic
	public void generateCargoAndEventChangesTest_LoadSlotToFreeze_Old() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTestWithRemovals(CargoAndEventChangesType.SLOT_TO_FREEZE, loadSlot, null);

		// We do not freeze slots yet
		Assert.assertEquals(0, changes.size());
	}

	@Test
	public void generateCargoAndEventChangesTest_LoadSlotToFreeze() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.SLOT_TO_FREEZE, loadSlot, null);

		Assert.assertEquals(1, changes.size());
		final IRollForwardChange change = changes.get(0);

		Assert.assertTrue(change instanceof FreezeSlotChange);

		final FreezeSlotChange removeSlotChange = (FreezeSlotChange) change;
		Assert.assertSame(loadSlot, removeSlotChange.getChangedObject());	
	}

	@Test
	@Disabled // uses obsolete removals code
	public void generateCargoAndEventChangesTest_EventToRemove() {

		final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTestWithRemovals(CargoAndEventChangesType.EVENT_TO_REMOVE, event, null);

		Assert.assertEquals(1, changes.size());
		final IRollForwardChange change = changes.get(0);

		Assert.assertTrue(change instanceof RemoveVesselEventChange);

		final RemoveVesselEventChange removeSlotChange = (RemoveVesselEventChange) change;
		Assert.assertSame(event, removeSlotChange.getChangedObject());
	}

	@Test
	@Disabled // uses obsolete code with removals logic
	public void generateCargoAndEventChangesTest_EventToFreeze_Old() {

		final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();

		event.setVesselAssignmentType(vessel);

		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("UTC");
		p.setLocation(l);
		event.setPort(p);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		final VesselEventVisit vesselEventVisit = ScheduleFactory.eINSTANCE.createVesselEventVisit();
		vesselEventVisit.setVesselEvent(event);

		sequence.getEvents().add(vesselEventVisit);

		LocalDate dt = LocalDate.of(2014, 9, 4);

		// Create 10 day window, with schedule start halfway
		event.setStartAfter(dt.atTime(0, 0));
		dt = dt.plusDays(5);
		vesselEventVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
		dt = dt.plusDays(1);
		vesselEventVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		dt = dt.plusDays(4);
		event.setStartAfter(dt.atTime(0, 0));

		event.setDurationInDays(1);

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTestWithRemovals(CargoAndEventChangesType.EVENT_TO_FREEZE, event, schedule);

		Assert.assertEquals(2, changes.size());

		FreezeVesselEventChange freezeChange = null;
		FixVesselEventWindowChange windowChange = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof FreezeVesselEventChange) {
				Assert.assertNull("Only one freeze change expected", freezeChange);
				freezeChange = (FreezeVesselEventChange) change;
			} else if (change instanceof FixVesselEventWindowChange) {
				Assert.assertNull("Only one window change expected", windowChange);
				windowChange = (FixVesselEventWindowChange) change;
			} else {
				Assert.fail("Unexpected change");
			}
		}
	}

	@Test
	public void generateCargoAndEventChangesTest_EventToFreeze() {

		final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();

		event.setVesselAssignmentType(vessel);

		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("UTC");
		p.setLocation(l);
		event.setPort(p);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		final VesselEventVisit vesselEventVisit = ScheduleFactory.eINSTANCE.createVesselEventVisit();
		vesselEventVisit.setVesselEvent(event);

		sequence.getEvents().add(vesselEventVisit);

		LocalDate dt = LocalDate.of(2014, 9, 4);

		// Create 10 day window, with schedule start halfway
		event.setStartAfter(dt.atTime(0, 0));
		dt = dt.plusDays(5);
		vesselEventVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
		dt = dt.plusDays(1);
		vesselEventVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		dt = dt.plusDays(4);
		event.setStartAfter(dt.atTime(0, 0));

		event.setDurationInDays(1);

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.EVENT_TO_FREEZE, event, schedule);

		Assert.assertEquals(2, changes.size());

		FreezeVesselEventChange freezeChange = null;
		FixVesselEventWindowChange windowChange = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof FreezeVesselEventChange) {
				Assert.assertNull("Only one freeze change expected", freezeChange);
				freezeChange = (FreezeVesselEventChange) change;
			} else if (change instanceof FixVesselEventWindowChange) {
				Assert.assertNull("Only one window change expected", windowChange);
				windowChange = (FixVesselEventWindowChange) change;
			} else {
				Assert.fail("Unexpected change");
			}
		}
	}

	@Test
	@Disabled // uses obsolete code with removals logic
	public void generateCargoAndEventChangesTest_CargoToFreeze_Old() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("UTC");
		p.setLocation(l);
		loadSlot.setPort(p);
		dischargeSlot.setPort(p);

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();
		cargo.setVesselAssignmentType(vessel);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		LocalDate dt = LocalDate.of(2013, 9, 4);

		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
		{
			schedule.getCargoAllocations().add(cargoAllocation);
		}
		{

			final Slot slot = loadSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}
		dt = dt.plusDays(10);
		{

			final Slot slot = dischargeSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTestWithRemovals(CargoAndEventChangesType.CARGO_TO_FREEZE, cargo, schedule);

		Assert.assertEquals(3, changes.size());

		FreezeCargoChange freezeChange = null;
		FixSlotWindowChange windowChange1 = null;
		FixSlotWindowChange windowChange2 = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof FreezeCargoChange) {
				Assert.assertNull("Only one freeze change expected", freezeChange);
				freezeChange = (FreezeCargoChange) change;
			} else if (change instanceof FixSlotWindowChange) {
				if (windowChange1 == null) {
					windowChange1 = (FixSlotWindowChange) change;
				} else if (windowChange2 == null) {
					windowChange2 = (FixSlotWindowChange) change;
				} else {
					Assert.fail("Only two window changes expected");
				}
			} else {
				Assert.fail("Unexpected change");
			}
		}
	}

	@Test
	public void generateCargoAndEventChangesTest_CargoToFreeze() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("UTC");
		p.setLocation(l);
		loadSlot.setPort(p);
		dischargeSlot.setPort(p);

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();
		cargo.setVesselAssignmentType(vessel);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		LocalDate dt = LocalDate.of(2013, 9, 4);

		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
		{
			schedule.getCargoAllocations().add(cargoAllocation);
		}
		{

			final Slot slot = loadSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}
		dt = dt.plusDays(10);
		{

			final Slot slot = dischargeSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.CARGO_TO_FREEZE, cargo, schedule);

		Assert.assertEquals(3, changes.size());

		FreezeCargoChange freezeChange = null;
		FixSlotWindowChange windowChange1 = null;
		FixSlotWindowChange windowChange2 = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof FreezeCargoChange) {
				Assert.assertNull("Only one freeze change expected", freezeChange);
				freezeChange = (FreezeCargoChange) change;
			} else if (change instanceof FixSlotWindowChange) {
				if (windowChange1 == null) {
					windowChange1 = (FixSlotWindowChange) change;
				} else if (windowChange2 == null) {
					windowChange2 = (FixSlotWindowChange) change;
				} else {
					Assert.fail("Only two window changes expected");
				}
			} else {
				Assert.fail("Unexpected change");
			}
		}
	}

	@Test
	@Disabled // uses obsolete code with removals logic
	public void generateCargoAndEventChangesTest_CargoToRemove() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();

		cargo.setVesselAssignmentType(vessel);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		LocalDate dt = LocalDate.of(2014, 9, 4);

		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
		{
			schedule.getCargoAllocations().add(cargoAllocation);
		}
		{

			final Slot slot = loadSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}
		dt = dt.plusDays(10);
		{

			final Slot slot = dischargeSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTestWithRemovals(CargoAndEventChangesType.CARGO_TO_REMOVE, cargo, schedule);

		Assert.assertEquals(3, changes.size());

		RemoveSlotChange removeChange1 = null;
		RemoveSlotChange removeChange2 = null;
		RemoveCargoChange removeCargo = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof RemoveCargoChange) {
				Assert.assertNull("Only one remove cargo change expected", removeCargo);
				removeCargo = (RemoveCargoChange) change;
			} else if (change instanceof RemoveSlotChange) {
				if (removeChange1 == null) {
					removeChange1 = (RemoveSlotChange) change;
				} else if (removeChange2 == null) {
					removeChange2 = (RemoveSlotChange) change;
				} else {
					Assert.fail("Only two remove slot changes expected");
				}
			} else {
				Assert.fail("Unexpected change");
			}
		}
		Assert.assertNotNull(removeCargo);
		Assert.assertNotNull(removeChange1);
		Assert.assertNotNull(removeChange2);
	}

	/**
	 * Obsolete method retained in case we want removals logic reinstated in the roll forward code.
	 * @param type
	 * @param obj
	 * @param schedule
	 * @return
	 */
	private @NonNull List<IRollForwardChange> generateCargoAndEventChangesTestWithRemovals(@NonNull final CargoAndEventChangesType type, @NonNull final Object obj, @Nullable final Schedule schedule) {

		final List<VesselEvent> eventsToRemove = new LinkedList<>();
		final List<VesselEvent> eventsToFreeze = new LinkedList<>();
		final List<Slot<?>> slotsToRemove = new LinkedList<>();
		final List<Slot<?>> slotsToFreeze = new LinkedList<>();
		final List<Cargo> cargoesToRemove = new LinkedList<>();
		final List<Cargo> cargoesToFreeze = new LinkedList<>();

		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();

		scenarioModel.setCargoModel(cargoModel);
		scenarioModel.setScheduleModel(scheduleModel);

		if (schedule != null) {
			scheduleModel.setSchedule(schedule);
		}
		switch (type) {
		case CARGO_TO_FREEZE:
			cargoesToFreeze.add((Cargo) obj);
			cargoModel.getCargoes().add((Cargo) obj);
			for (final Slot slot : ((Cargo) obj).getSlots()) {
				addSlotToModel(cargoModel, slot);
			}
			break;
		case CARGO_TO_REMOVE:
			cargoesToRemove.add((Cargo) obj);
			cargoModel.getCargoes().add((Cargo) obj);
			for (final Slot<?> slot : ((Cargo) obj).getSlots()) {
				addSlotToModel(cargoModel, slot);
			}
			break;
		case EVENT_TO_FREEZE:
			eventsToFreeze.add((VesselEvent) obj);
			cargoModel.getVesselEvents().add((VesselEvent) obj);
			break;
		case EVENT_TO_REMOVE:
			eventsToRemove.add((VesselEvent) obj);
			cargoModel.getVesselEvents().add((VesselEvent) obj);
			break;
		case SLOT_TO_FREEZE:
			slotsToFreeze.add((Slot) obj);
			addSlotToModel(cargoModel, (Slot) obj);
			break;
		case SLOT_TO_REMOVE:
			slotsToRemove.add((Slot) obj);
			addSlotToModel(cargoModel, (Slot) obj);
			break;
		}

		final List<IRollForwardChange> changes = new LinkedList<>();
		final RollForwardEngine engine = new RollForwardEngine();

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new CargoItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FleetItemProviderAdapterFactory());

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		engine.generateCargoAndEventChangesObsolete(domain, scenarioModel, changes, cargoesToRemove, cargoesToFreeze, eventsToRemove, eventsToFreeze, slotsToRemove, slotsToFreeze);

		return changes;
	}

	/**
	 * Modifies the schedule data, uses the roll forward engine to generate a list of {@link IRollForwardChange} objects, and returns the list, according to the specified event change type. 
	 * @param type
	 * @param obj
	 * @param schedule
	 * @return
	 */
	private @NonNull List<IRollForwardChange> generateCargoAndEventChangesTest(@NonNull final CargoAndEventChangesType type, @NonNull final Object obj, @Nullable final Schedule schedule) {

		final List<VesselEvent> eventsToRemove = new LinkedList<>();
		final List<VesselEvent> eventsToFreeze = new LinkedList<>();
		final List<Slot<?>> slotsToRemove = new LinkedList<>();
		final List<Slot<?>> slotsToFreeze = new LinkedList<>();
		final List<Cargo> cargoesToRemove = new LinkedList<>();
		final List<Cargo> cargoesToFreeze = new LinkedList<>();

		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();

		scenarioModel.setCargoModel(cargoModel);
		scenarioModel.setScheduleModel(scheduleModel);

		if (schedule != null) {
			scheduleModel.setSchedule(schedule);
		}
		switch (type) {
		case CARGO_TO_FREEZE:
			cargoesToFreeze.add((Cargo) obj);
			cargoModel.getCargoes().add((Cargo) obj);
			for (final Slot slot : ((Cargo) obj).getSlots()) {
				addSlotToModel(cargoModel, slot);
			}
			break;
		case CARGO_TO_REMOVE:
			cargoesToRemove.add((Cargo) obj);
			cargoModel.getCargoes().add((Cargo) obj);
			for (final Slot<?> slot : ((Cargo) obj).getSlots()) {
				addSlotToModel(cargoModel, slot);
			}
			break;
		case EVENT_TO_FREEZE:
			eventsToFreeze.add((VesselEvent) obj);
			cargoModel.getVesselEvents().add((VesselEvent) obj);
			break;
		case EVENT_TO_REMOVE:
			eventsToRemove.add((VesselEvent) obj);
			cargoModel.getVesselEvents().add((VesselEvent) obj);
			break;
		case SLOT_TO_FREEZE:
			slotsToFreeze.add((Slot) obj);
			addSlotToModel(cargoModel, (Slot) obj);
			break;
		case SLOT_TO_REMOVE:
			slotsToRemove.add((Slot) obj);
			addSlotToModel(cargoModel, (Slot) obj);
			break;
		}

		final List<IRollForwardChange> changes = new LinkedList<>();
		final RollForwardEngine engine = new RollForwardEngine();

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new CargoItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FleetItemProviderAdapterFactory());

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		engine.generateCargoAndEventChanges(domain, scenarioModel, cargoesToFreeze, eventsToFreeze, slotsToFreeze, changes);

		return changes;
	}

	private void addSlotToModel(final CargoModel cargoModel, final Slot<?> slot) {

		if (slot instanceof LoadSlot) {
			cargoModel.getLoadSlots().add((LoadSlot) slot);
		} else {
			cargoModel.getDischargeSlots().add((DischargeSlot) slot);
		}
	}
}
