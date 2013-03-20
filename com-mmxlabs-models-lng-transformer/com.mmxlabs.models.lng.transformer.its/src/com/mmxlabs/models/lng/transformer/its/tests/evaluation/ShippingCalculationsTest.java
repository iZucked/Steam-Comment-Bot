package com.mmxlabs.models.lng.transformer.its.tests.evaluation;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator.MinimalScenarioSetup;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.mmxcore.MMXRootObject;


public class ShippingCalculationsTest {
	
	public enum Expectations {
		DURATIONS, FUEL_COSTS, HIRE_COSTS, NBO_USAGE, BF_USAGE, FBO_USAGE, PILOT_USAGE
	}


	public void checkClasses(EList<? extends Object> objects, Class<?> [] classes) {
		Assert.assertEquals("Unexpected number of events in sequence", classes.length, objects.size());
		for (int i = 0; i < classes.length; i++) {
			if (i >= objects.size()) {
				return;
			}
			Object object = objects.get(i);
			Class<?> clazz = classes[i];
			String format = "Event #%d of schedule should be instance of %s not %s.";
			Assert.assertTrue(String.format(format, i, clazz.toString(), object.getClass().toString()), clazz.isInstance(object));
		}		
	}
	
	public void checkDurations(List<? extends Event> events, int [] durations) {
		Assert.assertEquals(durations.length, events.size());
		for (int i = 0; i < durations.length; i++) {
			Event event = events.get(i);
			Assert.assertEquals("Event duration for " + event, durations[i], event.getDuration());
		}
	}
	
	public void checkFuelCosts(List<? extends FuelUsage> events, int [] costs) {
		Assert.assertEquals(costs.length, events.size());
		for (int i = 0; i < costs.length; i++) {
			FuelUsage event = events.get(i);
			Assert.assertEquals("Event cost for " + event, costs[i], event.getFuelCost());
		}
	}	
	
	public int getFuelConsumption(FuelUsage event, Fuel fuel) {
		int result = 0;
		for (FuelQuantity quantity: event.getFuels()) {
			if (quantity.getFuel() == fuel) {
				for (FuelAmount a: quantity.getAmounts()) {
					result += a.getQuantity();
				}
			}
		}
		return result;
		
	}
	
	public void checkFuelConsumptions(List<? extends FuelUsage> events, Fuel fuel, int[] consumptions) {
		for (int i = 0; i < consumptions.length; i++) {
			FuelUsage event = events.get(i);
			Assert.assertEquals(fuel.getName() + " consumption for " + event, consumptions[i], getFuelConsumption(event, fuel));
		}
		
	}
	
	public void checkHireCosts(List<? extends Event> events, int[] costs) {
		for (int i = 0; i < costs.length; i++) {
			Event event = events.get(i);
			Assert.assertEquals("Hire cost for " + event, costs[i], event.getHireCost());
		}
		
	}
	
	public void checkLoadDischargeVolumes(List<? extends SlotVisit> events, int[] volumes) {
		for (int i = 0; i < volumes.length; i++) {
			SlotVisit event = events.get(i);
			CargoAllocation ca = event.getSlotAllocation().getCargoAllocation();
			Slot slot = event.getSlotAllocation().getSlot();
			
			int volume = 0;
			int expectedVolume = volumes[i];
			String description = null;
			
			if (slot instanceof LoadSlot) {
				volume = ca.getLoadVolume();
				description = "Load";
			}
			else if (slot instanceof DischargeSlot) {
				volume = ca.getDischargeVolume();
				expectedVolume = -expectedVolume;
				description = "Discharge";
			}
						
			Assert.assertEquals(description + " volume for " + event, expectedVolume, volume);				
		}
		
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> extractObjectsOfClass(EList<? extends Object> objects, Class<T> clazz) {
		LinkedList<T> result = new LinkedList<T>();
		for (Object object: objects) {
			if (clazz.isInstance(object)) {
				result.add((T) object);
			}
		}
		return result;
	}
	
	/*
	 * We need to create a barebones scenario with a single vessel schedule.
	 * Then the scenario needs to be evaluated to test correct calculation of:
	 *  - Fuel costs
	 *  - Port costs
	 *  - Route costs
	 *  - NBO rates
	 */
	
	public SequenceTester getDefaultTester() {
		// expected classes of the sequence elements
		Class<?> [] expectedClasses = { 
				StartEvent.class, 
				Journey.class, Idle.class, SlotVisit.class, 
				Journey.class, Idle.class, SlotVisit.class, 
				Journey.class, Idle.class, 
				EndEvent.class 
		};
	
		final SequenceTester checker = new SequenceTester(expectedClasses);
	
		// expected durations of journeys
		final int [] expectedJourneyDurations = { 1, 2, 1 };
		checker.setExpectedDurations(Journey.class, expectedJourneyDurations);
	
		// expected FBO consumptions of journeys
		// none (not economical in default)
		final int [] expectedFboJourneyConsumptions = { 0, 0, 0 };
		checker.setExpectedFboConsumptions(Journey.class, expectedFboJourneyConsumptions);
	
		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 
		final int [] expectedNboJourneyConsumptions = { 0, 20, 0 };
		checker.setExpectedNboConsumptions(Journey.class, expectedNboJourneyConsumptions);
	
		final int [] expectedBaseFuelJourneyConsumptions = { 15, 10, 15 };
		checker.setExpectedBaseFuelConsumptions(Journey.class, expectedBaseFuelJourneyConsumptions);
	
		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }  
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }   
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }  
		final int [] expectedJourneyCosts = { 150, 520, 150 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);
	
		// expected durations of idles
		final int [] expectedIdleDurations = { 0, 2, 0 };
		checker.setExpectedDurations(Idle.class, expectedIdleDurations);
		
		final int [] expectedIdleCosts = { 0, 210, 0 }; 
		checker.setExpectedFuelCosts(Idle.class, expectedIdleCosts);

		final int [] expectedBfIdleConsumptions = { 0, 0, 0 };
		checker.setExpectedBaseFuelConsumptions(Idle.class, expectedBfIdleConsumptions);
		final int [] expectedNboIdleConsumptions = { 0, 10, 0 };
		checker.setExpectedNboConsumptions(Idle.class, expectedNboIdleConsumptions);
		
		final int [] expectedLoadDischargeVolumes = { 10000, -9970 };
		checker.setExpectedLoadDischargeVolumes(expectedLoadDischargeVolumes);
		
		return checker;
	}

	public class SequenceTester {
		Map<Class<? extends Event>, int []> expectedDurations = new HashMap<Class<? extends Event>, int []>();
		Map<Class<? extends FuelUsage>, int []> expectedFuelCosts = new HashMap<Class<? extends FuelUsage>, int []>();
		//Map<Class<? extends FuelUsage>, Map<Fuel, int []>> expectedFuelConsumptions = new HashMap<Class<? extends FuelUsage>, Map<Fuel, int []>>();
		Map<Fuel, Map<Class<? extends FuelUsage>, int []>> expectedFuelConsumptions = new HashMap<Fuel, Map<Class<? extends FuelUsage>, int []>>();
		Map<Class<? extends Event>, int []> expectedHireCosts = new HashMap<Class<? extends Event>, int []>();
		int [] expectedLoadDischargeVolumes = null;
		
		Class<?> [] classes;
		
		public SequenceTester(Class<?> [] classes) {
			setClasses(classes);
		}
		
		public void setClasses(Class<?> [] classes) {
			this.classes = classes;			
		}
		
		@SuppressWarnings("unchecked")
		public <T> Map<Class<?>, int []> getLookup(Expectations field) {
			Map<T, int []> lookup = null;
			Fuel component = null;
			
			switch (field) {
			case DURATIONS:
				lookup = (Map<T, int[]>) expectedDurations;
				break;
			case FUEL_COSTS:
				lookup = (Map<T, int[]>) expectedFuelCosts;
				break;
			case HIRE_COSTS:
				lookup = (Map<T, int[]>) expectedHireCosts;
				break;
			case FBO_USAGE:
				component = Fuel.FBO;
				break;
			case NBO_USAGE:
				component = Fuel.NBO;
				break;
			case BF_USAGE:
				component = Fuel.BASE_FUEL;
				break;
			case PILOT_USAGE:
				component = Fuel.PILOT_LIGHT;
				break;				
			}
			
			if (component != null) {
				lookup = (Map<T, int[]>) expectedFuelConsumptions.get(component);
				if (lookup == null) {
					lookup = (Map<T, int[]>) new HashMap<Class<? extends FuelUsage>, int[]>();
					expectedFuelConsumptions.put(component, (Map<Class<? extends FuelUsage>, int[]>) lookup);
				}
			}
			
			return (Map<Class<?>, int[]>) lookup;
		}
		
		public void setExpectedValues(Expectations field, Class<?> clazz, int [] values) {
			getLookup(field).put(clazz, values);
		}
		
		public int [] getExpectedValues(Expectations field, Class<?> clazz) {
			return getLookup(field).get(clazz);
		}
		
		public void setExpectedValue(int value, Expectations field, Class<?> clazz, int index) {
			getExpectedValues(field, clazz)[index] = value;
		}
		
		public void setExpectedDurations(Class<? extends Event> clazz, int [] durations) {
			expectedDurations.put(clazz, durations);
		}
		
		public void setExpectedDuration(Class<? extends Event> clazz, int index, int duration) {
			expectedDurations.get(clazz)[index] = duration;
		}
		
		public void setExpectedHireCosts(Class<? extends Event> clazz, int [] costs) {
			expectedHireCosts.put(clazz, costs);
		}
		
		public void setExpectedFuelCosts(Class<? extends FuelUsage> clazz, int [] costs) {
			expectedFuelCosts.put(clazz, costs);
		}
		
		public void setExpectedFuelConsumptions(Class<? extends FuelUsage> clazz, Fuel component, int [] consumptions) {
			Map<Class<? extends FuelUsage>, int[]> lookup = expectedFuelConsumptions.get(component);
			if (lookup == null) {
				lookup = new HashMap<Class<? extends FuelUsage>, int []>();
				expectedFuelConsumptions.put(component, lookup);
			}
			
			lookup.put(clazz, consumptions);
		}
		
		public void setExpectedBaseFuelConsumptions(Class<? extends FuelUsage> clazz, int [] consumptions) {
			setExpectedFuelConsumptions(clazz, Fuel.BASE_FUEL, consumptions);			
		}
		
		public void setExpectedFboConsumptions(Class<? extends FuelUsage> clazz, int [] consumptions) {
			setExpectedFuelConsumptions(clazz, Fuel.FBO, consumptions);			
		}
		
		public void setExpectedNboConsumptions(Class<? extends FuelUsage> clazz, int [] consumptions) {
			setExpectedFuelConsumptions(clazz, Fuel.NBO, consumptions);			
		}
		
		public void setExpectedLoadDischargeVolumes(int [] volumes) {
			expectedLoadDischargeVolumes = volumes;
		}
		
		public void additionalChecks() {
		}
		
		@SuppressWarnings("unchecked")
		public void check(Sequence sequence) {
			EList<Event> events = sequence.getEvents();
			checkClasses(events, classes);
			
			for (Class<?> clazz: new HashSet<Class<?>>(Arrays.asList(classes))) {
				List<?> objects = extractObjectsOfClass(events, clazz);
				
				int [] expectedClassDurations = expectedDurations.get(clazz);
				if (expectedClassDurations != null) {
					checkDurations((List<? extends Event>) objects, expectedClassDurations);
				}
				
				int [] expectedClassHireCosts = expectedHireCosts.get(clazz);
				if (expectedClassHireCosts != null) {
					checkHireCosts((List<? extends Event>) objects, expectedClassHireCosts);
				}
				
				for (Entry<Fuel, Map<Class<? extends FuelUsage>, int[]>> entry: expectedFuelConsumptions.entrySet()) {
					int [] expectedFuelUsages = entry.getValue().get(clazz);
					if (expectedFuelUsages != null) {
						checkFuelConsumptions((List<? extends FuelUsage>) objects, entry.getKey(), expectedFuelUsages);
					}
				}
				
				int [] expectedClassCosts = expectedFuelCosts.get(clazz);
				if (expectedClassCosts != null) {
					checkFuelCosts((List<? extends FuelUsage>) objects, expectedClassCosts);
				}
				
				if (clazz.equals(SlotVisit.class) && expectedLoadDischargeVolumes != null) {
					checkLoadDischargeVolumes((List<? extends SlotVisit>) objects, expectedLoadDischargeVolumes);
				}

			}
		}
		
		public void setupExpectedHireCosts(int hireRatePerHour) {
			// change from default scenario: all events should have time charter costs
			for (Class<?> clazz: classes) {
				int [] classDurations = expectedDurations.get(clazz);
				if (classDurations != null) {
					int n = classDurations.length;
					int [] classHireCosts = new int [n];
					for (int i = 0; i < n; i++) {
						classHireCosts[i] = classDurations[i] * hireRatePerHour;
					}
					setExpectedHireCosts((Class<? extends Event>) clazz, classHireCosts);
				}
			}
			
		}
		
	}
	
	
	/*
	 * We need to create a barebones scenario with a single vessel schedule.
	 * Then the scenario needs to be evaluated to test correct calculation of:
	 *  - Fuel costs
	 *  - Port costs
	 *  - Route costs
	 *  - NBO rates
	 */

	@Test
	public void testCanalRouteShorter() {
		System.err.println("\n\nUse canal which is cheaper than default route");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: add a canal
		
		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 10, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);
		
		SequenceTester checker = getDefaultTester();		
		
		// change from default scenario
		// second journey is now half as long due to canal usage
		// so fuel usage is halved 
		
		// use half as much fuel on second journey (as default)
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 1);
	
		// use half as much fuel on second journey (as default)
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 1);
	
		// second journey costs half as much (as default)
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 1);
		
		// second journey takes half as long (as default) 
		checker.setExpectedValue(1, Expectations.DURATIONS, Journey.class, 1);
		
		// so second idle is 1 longer
		checker.setExpectedValue(3, Expectations.DURATIONS, Idle.class, 1);
				
		// and correspondingly costs more
		checker.setExpectedValue(315, Expectations.FUEL_COSTS, Idle.class, 1);
		
		// and requires more fuel
		checker.setExpectedValue(15, Expectations.NBO_USAGE, Idle.class, 1);
	
		// idle uses 15 NBO, journey uses 10
		int [] expectedloadDischargeVolumes = { 10000, -9975 };
		checker.setExpectedLoadDischargeVolumes(expectedloadDischargeVolumes);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);		
		
		Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 1, ladenJourney.getToll());
		
	}

	@Test
	public void testCanalRouteLonger() {
		System.err.println("\n\nDon't use canal which is longer than default route");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: add a canal, but it is longer than the default route
		
		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 30, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);
		
		SequenceTester checker = getDefaultTester();		
		
		// no change from default scenario: canal route should be ignored
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
		
		Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 0, ladenJourney.getToll());
	}

	@Test
	public void testCanalRouteTooExpensive() {
		System.err.println("\n\nDon't use canal which is has a high cost associated with it");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: add a canal, 
		// which is shorter than the default route
		// but has a high usage cost
		
		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 10, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);
		RouteCost cost = dsc.getRouteCost(mss.vc, canal);
		cost.setLadenCost(500);
		
		SequenceTester checker = getDefaultTester();		
		
		// no change from default scenario: canal route should be ignored
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
		
		Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 0, ladenJourney.getToll());
	}

	@Test
	public void testCanalRouteShorterWithDelay() {
		System.err.println("\n\nUse canal which is cheaper than default route but has a delay");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: add a canal 
		
		final Route canal = dsc.portCreator.addCanal("canal");
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 10, canal);
		dsc.fleetCreator.assignDefaultCanalData(mss.vc, canal);
		VesselClassRouteParameters routeParameters = dsc.getRouteParameters(mss.vc, canal);
		
		routeParameters.setExtraTransitTime(2);
		routeParameters.setLadenNBORate(TimeUnitConvert.convertPerHourToPerDay(1));
		routeParameters.setLadenConsumptionRate(TimeUnitConvert.convertPerHourToPerDay(2));
		
		SequenceTester checker = getDefaultTester();		
		
		// change from default scenario
		// second journey is now 1 hr longer due to canal usage (but 10 units shorter distance)
		// so fuel usage is halved, plus extra from canal 
		
		// use half as much fuel on second journey (as default) plus 2 for the canal 
		checker.setExpectedValue(12, Expectations.NBO_USAGE, Journey.class, 1);
	
		// use half as much fuel on second journey (as default) plus 2 for the canal
		checker.setExpectedValue(7, Expectations.BF_USAGE, Journey.class, 1);
	
		// second journey cost is different 
		checker.setExpectedValue(322, Expectations.FUEL_COSTS, Journey.class, 1);
		
		// second journey takes 3hrs (instead of 2)
		checker.setExpectedValue(3, Expectations.DURATIONS, Journey.class, 1);
		
		// so second idle is 1hr less
		checker.setExpectedValue(1, Expectations.DURATIONS, Idle.class, 1);
		
		// and correspondingly costs less
		checker.setExpectedValue(105, Expectations.FUEL_COSTS, Idle.class, 1);
		
		// and requires less fuel
		checker.setExpectedValue(5, Expectations.NBO_USAGE, Idle.class, 1);
		
		
		// idle NBO consumption is 5, plus 12 for journey 
		int [] expectedloadDischargeVolumes = { 10000, -9983 };
		checker.setExpectedLoadDischargeVolumes(expectedloadDischargeVolumes);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
		
		Journey ladenJourney = extractObjectsOfClass(sequence.getEvents(), Journey.class).get(1);
		Assert.assertEquals("Toll for laden leg via canal", 1, ladenJourney.getToll());
	}

	@Test
	public void testPlentyStartHeel() {
		System.err.println("\n\nGenerous Start Heel Means NBO on First Voyage");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		
		// change from default scenario
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		final Vessel vessel = mss.vessel;
		vessel.getStartHeel().setVolumeAvailable(1000);
		vessel.getStartHeel().setPricePerMMBTU(1);
				
		final SequenceTester checker = getDefaultTester();
	
		// change from default scenario
		// first journey should use NBO and base fuel (not just base fuel)
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);
	
		// cost of first journey should be changed accordingly
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
	}

	/*
	 * Note: NBO from partial start heel is ignored by the voyage calculator
	 * because the calculator is based on fuel choices, and partial NBO is
	 * not a fuel choice.
	 * This unit test has been disabled, although the calculation it tracks 
	 * is not handled correctly by the current voyage calculator. In future 
	 * it may need to be re-enabled. 
	 */
	@Ignore("Disabled since partial NBO is not supported.")
	@Test
	public void testLimitedStartHeel() {
		System.err.println("\n\nLimited Start Heel");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		
		// change from default scenario
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		final Vessel vessel = mss.vessel;
		vessel.getStartHeel().setVolumeAvailable(5);
		vessel.getStartHeel().setPricePerMMBTU(1);
				
		final SequenceTester checker = getDefaultTester();
		
		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);
	
		// cost of first journey should be changed accordingly
		checker.setExpectedValue(205, Expectations.FUEL_COSTS, Journey.class, 0);
			
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}

	@Test
	public void testFBOLimitedByMinHeel() {
		System.err.println("\n\nUse FBO for one trip after loading");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		final MMXRootObject scenario = dsc.buildScenario();
		
		// change from default scenario
		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
		
		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		fuelPrice.setPrice(100);
		// but the vessel's capacity is only 50m3 greater than its minimum heel
		// and the journeys (after loading) use a total of 40m3 NBO
		// so only 10m3 is available for FBO, which is not enough for both journeys
		mss.vc.setCapacity(60);
		mss.vc.setMinHeel(10);
						
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);						
		
		final Sequence sequence = schedule.getSequences().get(0);
	
		final List<Journey> journeys = extractObjectsOfClass(sequence.getEvents(), Journey.class);
		
		// in this scenario, there should be FBO used on one or other of the journey legs after loading
		// but not both
		
		int fboUsages = 0;
		for (int i = 1; i < 3; i++) {
			if (getFuelConsumption(journeys.get(i), Fuel.FBO) > 0) {
				fboUsages += 1;
			}
		}
		Assert.assertEquals("Exactly one leg uses FBO", 1, fboUsages);		
	}

	@Test
	public void testMinHeelForcesBfJourney() {
		System.err.println("\n\nMinimum Heel Forces BF only journey");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: make the minimum heel unattainable
		mss.vc.setMinHeel(10000);
	
		final SequenceTester checker = getDefaultTester();
				
		// change from default: no NBO consumption (min heel forces BF travel)
		final int [] expectedNboJourneyConsumptions = { 0, 0, 0 };
		checker.setExpectedNboConsumptions(Journey.class, expectedNboJourneyConsumptions);
	
		// change from default: all BF consumption (min heel forces BF travel)
		final int [] expectedBaseFuelJourneyConsumptions = { 15, 30, 15 };
		checker.setExpectedBaseFuelConsumptions(Journey.class, expectedBaseFuelJourneyConsumptions);
	
		// expected costs of journeys
		final int [] expectedJourneyCosts = { 150, 300, 150 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}

	@Test
	public void testHeelMeansNoCooldownRequired() {
		System.err.println("\n\nStart heel is sufficient to avoid cooldown at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
	
		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(0);
		mss.vc.setCoolingTime(1);
		mss.vc.setCoolingVolume(100);
		
		mss.setupCooldown(1.0);		
		mss.loadPort.setAllowCooldown(true);
		
		// but a big start heel should mean no cooldown is required
		mss.vessel.getStartHeel().setVolumeAvailable(1000);
		mss.vessel.getStartHeel().setPricePerMMBTU(1);
				
		final SequenceTester checker = getDefaultTester();
	
		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);
	
		// cost of first journey should be changed accordingly
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
	}

	/*
	 * We need to create a barebones scenario with a single vessel schedule.
	 * Then the scenario needs to be evaluated to test correct calculation of:
	 *  - Fuel costs
	 *  - Port costs
	 *  - Route costs
	 *  - NBO rates
	 */
	
	@Test
	public void testBasicScenario() {
		System.err.println("\n\nBasic Scenario");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
	
		final SequenceTester checker = getDefaultTester();
		
		/*
		 * evaluate and get a schedule
		 * note: this involves 
		 * - initialising a transformer using a TransformerExtensionTest module
		 * - transforming the scenario and running an optimiser on the transformed data
		 * - using additional indirection to inject members into an optimiser exporter
		 */
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
		/*
		// extract the three journeys
		List<Journey> journeys = extractObjectsOfClass(events, Journey.class);
		Journey originToLoad = journeys.get(0);
		Journey loadToDischarge = journeys.get(1);
		Journey dischargeToOrigin = journeys.get(2);
	
		// check they go between the right places and have the right distances
		dsc.checkJourneyGeography(originToLoad, mss.originPort, mss.loadPort);
		dsc.checkJourneyGeography(loadToDischarge, mss.loadPort, mss.dischargePort);
		dsc.checkJourneyGeography(dischargeToOrigin, mss.dischargePort, mss.originPort);
		*/		
	
	}

	@Test
	public void testFBODesirable() {
		System.err.println("\n\nUse FBO for both trips after loading");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		
		// change from default scenario
		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
		
		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		fuelPrice.setPrice(100);
				
		final SequenceTester checker = getDefaultTester();
		
		// change from default scenario
		// second and third journeys now use LNG only
		final int [] expectedFboJourneyConsumptions = { 0, 10, 5 };
		checker.setExpectedFboConsumptions(Journey.class, expectedFboJourneyConsumptions);
	
		final int [] expectedNboJourneyConsumptions = { 0, 20, 10 };
		checker.setExpectedNboConsumptions(Journey.class, expectedNboJourneyConsumptions);
	
		final int [] expectedBaseFuelJourneyConsumptions = { 15, 0, 0 };
		checker.setExpectedBaseFuelConsumptions(Journey.class, expectedBaseFuelJourneyConsumptions);
	
		// first journey costs 10x as much, other journeys change costing too
		final int [] expectedJourneyCosts = { 1500, 630, 315 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);
		
		// idle LNG consumption is 10, plus 30 + 15 for journeys and 500 min heel
		int [] expectedloadDischargeVolumes = { 10000, -9445 };
		checker.setExpectedLoadDischargeVolumes(expectedloadDischargeVolumes);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
	}

	@Test
	public void testMaxLoadVolume() {
		System.err.println("\n\nMaximum Load Volume Limits Load & Discharge");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: add a maximum load volume
		mss.cargo.getLoadSlot().setMaxQuantity(500);
	
		final SequenceTester checker = getDefaultTester();
		
		int [] expectedloadDischargeVolumes = { 500, -470 };
		checker.setExpectedLoadDischargeVolumes(expectedloadDischargeVolumes);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
		
		
	}

	@Test
	public void testMaxDischargeVolume() {
		System.err.println("\n\nMaximum Discharge Volume Limits Load & Discharge");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: add a maximum load volume
		mss.cargo.getDischargeSlot().setMaxQuantity(500);
	
		final SequenceTester checker = getDefaultTester();
		
		int [] expectedloadDischargeVolumes = { 530, -500 };
		checker.setExpectedLoadDischargeVolumes(expectedloadDischargeVolumes);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
		
		
	}

	@Test
	public void testMinDischargeVolume() {
		System.err.println("\n\nMinimum Discharge Volume Prevents FBO");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: base fuel price more expensive, so FBO is economical
		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
		final BaseFuelCost fuelPrice = fleetCostModel.getBaseFuelPrices().get(0);
		// base fuel is now 10x more expensive, so FBO is economical
		fuelPrice.setPrice(100);
		
		// but minimum discharge volume means that it causes a capacity violation
		mss.cargo.getDischargeSlot().setMinQuantity(9965);
		
		// for the moment, set min heel to zero since it causes problems in the volume calculations
		mss.vc.setMinHeel(0);
	
		final SequenceTester checker = getDefaultTester();
		
		int [] expectedloadDischargeVolumes = { 10000, -9970 };
		checker.setExpectedLoadDischargeVolumes(expectedloadDischargeVolumes);
		
		// first & last journeys cost 10x as much
		final int [] expectedJourneyCosts = { 1500, 1420, 1500 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);
	
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);						
	}

	@Test
	public void testMaxLoadVolumeForcesBfIdle() {
		System.err.println("\n\nMaximum Load Volume Forces BF Idle");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: add a maximum load volume
		mss.cargo.getLoadSlot().setMaxQuantity(20);
	
		final SequenceTester checker = getDefaultTester();
		
		int [] expectedloadDischargeVolumes = { 20, 0 };
		checker.setExpectedLoadDischargeVolumes(expectedloadDischargeVolumes);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}

	@Test
	public void testIdleAfterVesselReturn() {
		System.err.println("\n\nSpecified date for vessel return causes idling.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: set a "return after" date 
		// somewhat later than the end of the discharge window 
		VesselAvailability av = mss.vessel.getAvailability();
		Date endDischarge = mss.cargo.getDischargeSlot().getWindowEndWithSlotOrPortTime();
		
		// return 3 hrs after discharge window ends
		Date returnDate = new Date(endDischarge.getTime() + 3 * 3600 * 1000);
		av.setEndAfter(returnDate);
		System.err.println("Vessel to return after: " + returnDate);
	
		final SequenceTester checker = getDefaultTester();
				
		// change from default: BF idle consumption at base port after return
		// (idle at discharge port is NBO)
		checker.setExpectedValue(10, Expectations.BF_USAGE, Idle.class, 2);
		// change from default: idle at base port after return
		checker.setExpectedValue(2, Expectations.DURATIONS, Idle.class, 2);
		// change from default: idle cost at base port
		checker.setExpectedValue(100, Expectations.FUEL_COSTS, Idle.class, 2);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}

	@Test
	public void testIdleAfterVesselStart() {
		System.err.println("\n\nSpecified date for vessel start causes idling.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: set a "return after" date 
		// somewhat later than the end of the discharge window 
		VesselAvailability av = mss.vessel.getAvailability();
		Date startLoad = mss.cargo.getLoadSlot().getWindowStartWithSlotOrPortTime();
		
		// start 3 hrs before load window begins
		Date startDate = new Date(startLoad.getTime() - 3 * 3600 * 1000);
		av.setStartBy(startDate);
		System.err.println("Vessel to start before: " + startDate);
	
		final SequenceTester checker = getDefaultTester();				
	
		// change from default: BF idle consumption at load port after arrival
		// (idle at discharge port is NBO)
		checker.setExpectedValue(10, Expectations.BF_USAGE, Idle.class, 0);
		// change from default: idle at base port after return
		checker.setExpectedValue(2, Expectations.DURATIONS, Idle.class, 0);
		// change from default: idle cost at base port
		checker.setExpectedValue(100, Expectations.FUEL_COSTS, Idle.class, 0);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}

	@Test
	public void testIgnoreStartAfterAndEndBy() {
		System.err.println("\n\nNo effects of in-bounds values for vessel start-after and end-by");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		// change from default scenario: set a "return after" date 
		// somewhat later than the end of the discharge window 
		VesselAvailability av = mss.vessel.getAvailability();
		Date startLoad = mss.cargo.getLoadSlot().getWindowStartWithSlotOrPortTime();
		Date endDischarge = mss.cargo.getDischargeSlot().getWindowEndWithSlotOrPortTime();
		
		// start within 5 hrs before load window starts
		Date startDate = new Date(startLoad.getTime() - 5 * 3600 * 1000);
		av.setStartAfter(startDate);
		System.err.println("Vessel to start after: " + startDate);
	
		// return within 5 hrs after discharge window ends
		Date returnDate = new Date(endDischarge.getTime() + 5 * 3600 * 1000);
		av.setEndBy(returnDate);
		System.err.println("Vessel to return by: " + returnDate);
			
		// should have no effect on the generated schedule
		final SequenceTester checker = getDefaultTester();
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}

	@Test
	public void testExtraTimeScheduledForCooldown() {
		System.err.println("\n\nExtra time should be scheduled after leaving start port for cooldown at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
	
		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(0);
		mss.vc.setCoolingTime(1);
		mss.vc.setCoolingVolume(100);
		
		mss.setupCooldown(1.0);		
		mss.loadPort.setAllowCooldown(true);
					
		final SequenceTester checker = getDefaultTester();
	
		// change from default scenario: should insert a cooldown event
		Class<?> [] expectedClasses = { 
				StartEvent.class, 
				Journey.class, Idle.class, Cooldown.class, SlotVisit.class, 
				Journey.class, Idle.class, SlotVisit.class, 
				Journey.class, Idle.class, 
				EndEvent.class 
		};
	
		checker.setClasses(expectedClasses);
		
		// change from default: cooldown time
		final int [] expectedCooldownTimes = { 1 };
		checker.setExpectedDurations(Cooldown.class, expectedCooldownTimes);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);			
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}

	@Test
	public void testLongWarmupMeansNoCooldownRequired() {
		System.err.println("\n\nStart heel is sufficient to avoid cooldown at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
	
		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(3);
		mss.vc.setCoolingTime(1);
		mss.vc.setCoolingVolume(100);
		
		mss.setupCooldown(1.0);		
		mss.loadPort.setAllowCooldown(true);
		
		final SequenceTester checker = getDefaultTester();
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);		
	}

	@Test
	public void testCooldownAdded() {
		System.err.println("\n\nCooldown event should be scheduled at load port.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
	
		// change from default scenario: cooldown times and volumes specified
		mss.vc.setWarmingTime(0);
		mss.vc.setCoolingTime(0);
		mss.vc.setCoolingVolume(100);
		
		mss.setupCooldown(1.0);		
		mss.loadPort.setAllowCooldown(true);
					
		final SequenceTester checker = getDefaultTester();
	
		// change from default scenario: should insert a cooldown event
		Class<?> [] expectedClasses = { 
				StartEvent.class, 
				Journey.class, Idle.class, Cooldown.class, SlotVisit.class, 
				Journey.class, Idle.class, SlotVisit.class, 
				Journey.class, Idle.class, 
				EndEvent.class 
		};
	
		checker.setClasses(expectedClasses);
		
		// change from default: cooldown time
		final int [] expectedCooldownTimes = { 0 };
		checker.setExpectedDurations(Cooldown.class, expectedCooldownTimes);
		
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);			
				
		final Sequence sequence = schedule.getSequences().get(0);
		Cooldown cooldown = extractObjectsOfClass(sequence.getEvents(), Cooldown.class).get(0);
		Assert.assertEquals("Cooldown cost", 2100, cooldown.getCost()); 
	
		checker.check(sequence);				
	}

	@Test
	public void testCharterCostSet() {
		System.err.println("\n\nNon-zero vessel charter cost added correctly.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
	
		final int charterRatePerDay = 240;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		mss.vessel.setTimeCharterRate(charterRatePerDay);
					
		final SequenceTester checker = getDefaultTester();		
	
		int hireCostPerHour = charterRatePerDay / 24;
		checker.setupExpectedHireCosts(hireCostPerHour);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);			
				
		final Sequence sequence = schedule.getSequences().get(0);
		
		checker.check(sequence);				
	
		// change from default scenario: sequence daily hire rate should be set
		Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}

	@Test
	public void testCharterCostUnset() {
		System.err.println("\n\nZero vessel charter cost added correctly.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
	
		final int charterRatePerDay = 0;
		// change from default scenario: vessel has time charter rate 240 per day (10 per hour)
		mss.vessel.setTimeCharterRate(charterRatePerDay);
					
		final SequenceTester checker = getDefaultTester();		
	
		int hireCostPerHour = charterRatePerDay / 24;
		checker.setupExpectedHireCosts(hireCostPerHour);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);			
				
		final Sequence sequence = schedule.getSequences().get(0);
		
		checker.check(sequence);				
	
		// change from default scenario: sequence daily hire rate should be set
		Assert.assertEquals("Daily cost for vessel hire", charterRatePerDay, sequence.getDailyHireRate());
	}
	
	
	@Test
	public void testVesselStartsAnywhere() {
		System.err.println("\n\nVessel starts anywhere.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		mss.vessel.getAvailability().getStartAt().clear();
	
		final SequenceTester checker = getDefaultTester();		

		// change from default scenario: vessel makes only two journeys
		Class<?> [] expectedClasses = { 
				StartEvent.class, Idle.class, SlotVisit.class, 
				Journey.class, Idle.class, SlotVisit.class, 
				Journey.class, Idle.class, 
				EndEvent.class 
		};

		checker.setClasses(expectedClasses);
		
		// expected durations of journeys
		final int [] expectedJourneyDurations = { 2, 1 };
		checker.setExpectedDurations(Journey.class, expectedJourneyDurations);
	
		// expected FBO consumptions of journeys
		// none (not economical in default)
		final int [] expectedFboJourneyConsumptions = { 0, 0 };
		checker.setExpectedFboConsumptions(Journey.class, expectedFboJourneyConsumptions);
	
		// expected NBO consumptions of journeys
		// 0 (no start heel)
		// 20 = 
		final int [] expectedNboJourneyConsumptions = { 20, 0 };
		checker.setExpectedNboConsumptions(Journey.class, expectedNboJourneyConsumptions);
	
		final int [] expectedBaseFuelJourneyConsumptions = { 10, 15 };
		checker.setExpectedBaseFuelConsumptions(Journey.class, expectedBaseFuelJourneyConsumptions);
	
		// expected costs of journeys
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }  
		// 520 = 10 { base fuel unit cost } * 10 { base fuel consumption } + 21 { LNG CV } * 1 { LNG cost per MMBTU } * 20 { LNG consumption }   
		// 150 = 10 { base fuel unit cost } * 15 { base fuel consumption }  
		final int [] expectedJourneyCosts = { 520, 150 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);			
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);			
				
		final Sequence sequence = schedule.getSequences().get(0);
		
		checker.check(sequence);				
	
	}

	@Test
	public void testVesselEndsAnywhere() {
		System.err.println("\n\nVessel ends anywhere - travels back to load port for end.");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		mss.vessel.getAvailability().getEndAt().clear();
	
		final SequenceTester checker = getDefaultTester();		
	
		// change from default: final journey is 2hrs
		checker.setExpectedValue(2, Expectations.DURATIONS, Journey.class, 2);	
		// change from default: final journey consumes double the base fuel
		checker.setExpectedValue(30, Expectations.BF_USAGE, Journey.class, 2);
		// change from default: final journey costs double 
		checker.setExpectedValue(300, Expectations.FUEL_COSTS, Journey.class, 2);
	
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);			
				
		final Sequence sequence = schedule.getSequences().get(0);
		
		checker.check(sequence);				
	
	}

	@Test
	public void testLimitedStartHeelForcesBfIdle() {
		System.err.println("\n\nLimited Start Heel, should NBO travel and BF on idle");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		
		// change from default scenario
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		final Vessel vessel = mss.vessel;
		vessel.getStartHeel().setVolumeAvailable(10);
		vessel.getStartHeel().setPricePerMMBTU(1);
		
		// change from default scenario: set a "return after" date 
		// somewhat later than the end of the discharge window 
		VesselAvailability av = mss.vessel.getAvailability();
		Date startLoad = mss.cargo.getLoadSlot().getWindowStartWithSlotOrPortTime();
		
		// start 3 hrs before load window begins
		Date startDate = new Date(startLoad.getTime() - 3 * 3600 * 1000);
		av.setStartBy(startDate);
		System.err.println("Vessel to start before: " + startDate);
				
		final SequenceTester checker = getDefaultTester();
				
		// change from default scenario
		// first journey should use NBO and base fuel
		checker.setExpectedValue(10, Expectations.NBO_USAGE, Journey.class, 0);
		checker.setExpectedValue(5, Expectations.BF_USAGE, Journey.class, 0);
		checker.setExpectedValue(260, Expectations.FUEL_COSTS, Journey.class, 0);		
		
		// change from default: BF idle consumption at load port after arrival
		// (idle at discharge port is NBO)
		checker.setExpectedValue(10, Expectations.BF_USAGE, Idle.class, 0);
		checker.setExpectedValue(2, Expectations.DURATIONS, Idle.class, 0);
		checker.setExpectedValue(100, Expectations.FUEL_COSTS, Idle.class, 0);

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);
	
		checker.check(sequence);				
	}
	
}
