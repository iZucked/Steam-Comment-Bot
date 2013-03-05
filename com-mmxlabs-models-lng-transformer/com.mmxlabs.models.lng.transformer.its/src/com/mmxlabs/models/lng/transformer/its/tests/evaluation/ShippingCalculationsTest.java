package com.mmxlabs.models.lng.transformer.its.tests.evaluation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
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
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;


public class ShippingCalculationsTest {
	
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
	
	public class SequenceTester {
		Map<Class<? extends Event>, int []> expectedDurations = new HashMap<Class<? extends Event>, int []>();
		Map<Class<? extends FuelUsage>, int []> expectedFuelCosts = new HashMap<Class<? extends FuelUsage>, int []>();
		Map<Class<? extends FuelUsage>, Map<Fuel, int []>> expectedFuelConsumptions = new HashMap<Class<? extends FuelUsage>, Map<Fuel, int []>>();
		
		
		//Map<Pair<Class<? extends FuelUsage>, FuelComponent>, int []> fuelUsages = new HashMap<Pair<Class<? extends FuelUsage>, FuelComponent>, int []>();
		Class<?> [] classes;
		
		public SequenceTester(Class<?> [] classes) {
			this.classes = classes;
		}
		
		public void setExpectedDurations(Class<? extends Event> clazz, int [] durations) {
			this.expectedDurations.put(clazz, durations);
		}
		
		public void setExpectedFuelCosts(Class<? extends FuelUsage> clazz, int [] costs) {
			this.expectedFuelCosts.put(clazz, costs);
		}
		
		public void setExpectedFuelConsumptions(Class<? extends FuelUsage> clazz, Fuel component, int [] consumptions) {
			Map<Fuel, int[]> lookup = expectedFuelConsumptions.get(clazz);
			if (lookup == null) {
				lookup = new HashMap<Fuel, int []>();
				expectedFuelConsumptions.put(clazz, lookup);
			}
			
			lookup.put(component, consumptions);
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
				
				Map<Fuel, int[]> lookup = expectedFuelConsumptions.get(clazz);
				if (lookup != null) {
					for (Entry<Fuel, int[]> entry: lookup.entrySet()) {
						checkFuelConsumptions((List<? extends FuelUsage>) objects, entry.getKey(), entry.getValue());
					}					
				}
				
				int [] expectedClassCosts = expectedFuelCosts.get(clazz);
				if (expectedClassCosts != null) {
					checkFuelCosts((List<? extends FuelUsage>) objects, expectedClassCosts);
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

		final int [] expectedFboJourneyConsumptions = { 0, 0, 0 };
		checker.setExpectedFboConsumptions(Journey.class, expectedFboJourneyConsumptions);

		final int [] expectedNboJourneyConsumptions = { 0, 20, 0 };
		checker.setExpectedNboConsumptions(Journey.class, expectedNboJourneyConsumptions);

		final int [] expectedBaseFuelJourneyConsumptions = { 15, 10, 15 };
		checker.setExpectedBaseFuelConsumptions(Journey.class, expectedBaseFuelJourneyConsumptions);

		// expected costs of journeys
		// 150 = 10 [ base fuel unit cost ] *  
		final int [] expectedJourneyCosts = { 150, 520, 150 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);

		// expected durations of idles
		final int [] expectedIdleDurations = { 0, 2, 0 };
		checker.setExpectedDurations(Idle.class, expectedIdleDurations);
		
		final int [] expectedIdleCosts = { 0, 210, 0 }; 
		checker.setExpectedFuelCosts(Idle.class, expectedIdleCosts);

		
		return checker;
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
		System.err.println("Basic Scenario");
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
	public void testPlentyStartHeel() {
		System.err.println("Generous Start Heel");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		
		// change from default scenario
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		final Vessel vessel = mss.vessel;
		vessel.getStartHeel().setVolumeAvailable(600);
		vessel.getStartHeel().setPricePerMMBTU(1);
				
		final SequenceTester checker = getDefaultTester();

		// change from default scenario
		// first journey should use NBO and base fuel
		final int [] expectedNboJourneyConsumptions = { 10, 20, 0 };
		checker.setExpectedNboConsumptions(Journey.class, expectedNboJourneyConsumptions);

		final int [] expectedBaseFuelJourneyConsumptions = { 5, 10, 10 };
		checker.setExpectedBaseFuelConsumptions(Journey.class, expectedBaseFuelJourneyConsumptions);
		
		// change from default scenario
		final int [] expectedJourneyCosts = { 260, 520, 100 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);		
	}

	@Test
	public void testLimitedStartHeel() {
		System.err.println("Limited Start Heel");
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
		final int [] expectedNboJourneyConsumptions = { 5, 20, 0 };
		checker.setExpectedNboConsumptions(Journey.class, expectedNboJourneyConsumptions);

		final int [] expectedBaseFuelJourneyConsumptions = { 10, 10, 10 };
		checker.setExpectedBaseFuelConsumptions(Journey.class, expectedBaseFuelJourneyConsumptions);

		// change from default scenario
		final int [] expectedJourneyCosts = { 205, 520, 100 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);				
	}

	@Test
	public void testFBODesirable() {
		System.err.println("Use FBO for both trips after loading");
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

		// first journey costs 10x as much
		final int [] expectedJourneyCosts = { 1500, 630, 315 };
		checker.setExpectedFuelCosts(Journey.class, expectedJourneyCosts);
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);		
	}

	@Test
	public void testFBOLimitedByMinHeel() {
		System.err.println("Use FBO for one trip after loading");
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
	
	
	public void testUseCanalRoute() {
		// TODO: finish this test 
		System.err.println("Use canal which is cheaper than default route");
		final DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		final MMXRootObject scenario = dsc.buildScenario();
		final MinimalScenarioSetup mss = dsc.minimalScenarioSetup;
		
		final Route canal = dsc.addRoute("canal");
		
		canal.setCanal(true);
		
		// shorter via canal (default is 20)
		dsc.portCreator.setDistance(mss.loadPort, mss.dischargePort, 10, canal);
		
		final RouteCost canalCost = PricingFactory.eINSTANCE.createRouteCost();
		canalCost.setRoute(canal);
		canalCost.setLadenCost(1); // cost in dollars for a laden vessel
		canalCost.setBallastCost(1); // cost in dollars for a ballast vessel

		VesselClassRouteParameters params = FleetFactory.eINSTANCE.createVesselClassRouteParameters();

		params.setRoute(canal);
		params.setLadenConsumptionRate(15);
		params.setBallastConsumptionRate(15);
		params.setLadenNBORate(10);
		params.setBallastNBORate(10);
		params.setExtraTransitTime(0);
						
		SequenceTester checker = getDefaultTester();		
		
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);				
				
		Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);		
		
	}
	

}
