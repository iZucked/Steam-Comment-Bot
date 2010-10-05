package com.mmxlabs.demo.app.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.ScenarioPackage;
import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.CargoModel;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.fleet.CharterOut;
import scenario.fleet.FleetFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;
import scenario.optimiser.Constraint;
import scenario.optimiser.Objective;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimiserFactory;
import scenario.optimiser.OptimiserPackage;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoFactory;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.ThresholderSettings;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.port.PortModel;
import scenario.port.PortPackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.common.csv.DistanceImporter;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

/**
 * A class for constructing the EMF representations of random test scenarios
 * 
 * @author hinton
 * 
 */
public class RandomScenarioUtils {
	private final ScenarioFactory scenarioFactory = ScenarioPackage.eINSTANCE
				.getScenarioFactory();
	private final FleetFactory fleetFactory = FleetPackage.eINSTANCE.getFleetFactory();
	private final PortFactory portFactory = PortPackage.eINSTANCE.getPortFactory();
	private final CargoFactory cargoFactory = CargoPackage.eINSTANCE.getCargoFactory();

	private final Random random;

	public RandomScenarioUtils(Random random) {
		this.random = random;
	}

	public RandomScenarioUtils() {
		this(new Random(1));
	}

	public Scenario createScenario() {
		final Scenario scenario = scenarioFactory.createScenario();
		
		scenario.setCanalModel(portFactory.createCanalModel());
		scenario.setCargoModel(cargoFactory.createCargoModel());
		scenario.setDistanceModel(portFactory.createDistanceModel());
		scenario.setFleetModel(fleetFactory.createFleetModel());
		scenario.setPortModel(portFactory.createPortModel());
		
		return scenario;
	}

	public Scenario addDistanceModel(final Scenario scenario,
			final String distanceFile) throws FileNotFoundException,
			IOException {
		final DistanceImporter di = new DistanceImporter(distanceFile);
		final PortModel portModel = scenario.getPortModel();

		for (String s : di.getKeys()) {
			Port port = portFactory.createPort();
			port.setName(s);
			portModel.getPorts().add(port);
		}

		final DistanceModel dm = scenario.getDistanceModel();

		for (Port a : scenario.getPortModel().getPorts()) {
			for (Port b : scenario.getPortModel().getPorts()) {
				final int distance = di.getDistance(a.getName(), b.getName());
				if (!(a.equals(b)) && distance != Integer.MAX_VALUE) {
					final DistanceLine dl = portFactory.createDistanceLine();
					dl.setFromPort(a);
					dl.setToPort(b);
					dl.setDistance(distance);
					dm.getDistances().add(dl);
				}
			}
		}

		return scenario;
	}

	public Scenario addDefaultFleet(final Scenario scenario, final int spotCount) {
		// generate the standard fleet
		VesselClass class1 = addVesselClass(scenario, "STEAM-138", 12, 20,
				138000, 200);
		VesselClass class2 = addVesselClass(scenario, "STEAM-145", 12, 20,
				145000, 200);
		VesselClass class3 = addVesselClass(scenario, "DFDE-177", 12, 16,
				177000, 200); // TODO different from testutils, because the
								// curve for DFDE doesn't contain a speed of 20.
		VesselClass class4 = addVesselClass(scenario, "STEAM-126", 12, 19.5f,
				138000, 200); // TODO units in the model; should it be a float?

		// TODO 0,0 points are fake, to make boiloff numbers fit in the curve.
		float[][] steam = new float[][] { { 0, 0 }, { 12, 12 }, { 20, 20 } };
		float[][] dfde = new float[][] { { 0, 0 }, { 12, 8 }, { 20, 16 } };

		// create class parameters; currently model uses containment for curves,
		// so we need to do duplicates
		class1.setLadenAttributes(createVesselStateAttributes(
				VesselState.LADEN, 1.38f / 24.0f, 1.18f / 24.0f, 1 / 24.0f,
				steam));
		class1.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.38f / 24.0f, 1.18f / 24.0f, 1 / 24.0f,
				steam));
		class1.setBaseFuelEquivalenceFactor(0.5);

		class2.setLadenAttributes(createVesselStateAttributes(
				VesselState.LADEN, 1.45f / 24.0f, 1.25f / 24.0f, 1 / 24.0f,
				steam));
		class2.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.45f / 24.0f, 1.25f / 24.0f, 1 / 24.0f,
				steam));
		class2.setBaseFuelEquivalenceFactor(0.5);

		class3.setLadenAttributes(createVesselStateAttributes(
				VesselState.LADEN, 1.77f / 24.0f, 1.57f / 24.0f, 1 / 24.0f,
				dfde));
		class3.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.77f / 24.0f, 1.57f / 24.0f, 1 / 24.0f,
				dfde));
		class3.setBaseFuelEquivalenceFactor(0.5);

		class4.setLadenAttributes(createVesselStateAttributes(
				VesselState.LADEN, 1.26f / 24.0f, 1.06f / 24.0f, 1 / 24.0f,
				steam));
		class4.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.25f / 24.0f, 1.06f / 24.0f, 1 / 24.0f,
				steam));
		class4.setBaseFuelEquivalenceFactor(0.5);

		class1.setSpotCharterCount(spotCount);
		class2.setSpotCharterCount(spotCount);
		class3.setSpotCharterCount(spotCount);
		class4.setSpotCharterCount(spotCount);

		// create vessels in each class

		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Rita Andrea", class1));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Jane Elizabeth", class2));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Lydon Volney", class2));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Shirley Elizabeth", class2));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Heather Sally", class2));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Alison Victoria", class2));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Nile Eagle", class2));

		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Julia Louise", class3));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Becki Anne", class3));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Mickie Harper", class3));
		randomiseAvailability(scenario,
				addVessel(scenario, "Methane Unknown", class3));

		addVessel(scenario, "Hilli", class4);
		addVessel(scenario, "Gimi", class4);
		addVessel(scenario, "Khannur", class4);
		addVessel(scenario, "Golar Freeze", class4);
		addVessel(scenario, "Methane Princes", class1);
		return scenario;
	}

	public Scenario addRandomCargoes(final Scenario scenario,
			final String distanceMatrix, final String slotsFile,
			final int count, int minWindow, int maxWindow, int minVisit,
			int maxVisit, int minSlack, int maxSlack, double locality, int scenarioDuration)
			throws NumberFormatException, IOException {
		// load slots file
		
		BufferedReader br = new BufferedReader(new FileReader(new File(
				slotsFile)));

		Map<String, Integer> loadSlots = new HashMap<String, Integer>();
		Map<String, Integer> dischargeSlots = new HashMap<String, Integer>();

		String line;
		int totalLoads = 0;
		int totalDischarges = 0;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split(",");
			String pn = parts[0];
			int loadDischargeCount = Integer.parseInt(parts[2]);
			if (parts[1].equals("L")) {
				loadSlots.put(parts[0], loadDischargeCount);
				totalLoads += loadDischargeCount;
			} else {
				dischargeSlots.put(parts[0], loadDischargeCount);
				totalDischarges += loadDischargeCount;
			}
		}

		// now generate load/discharge pairs randomly, taking account of
		// locality. hum.
		// create a wheel-of-fortune where the total area dedicated to each load
		// / discharge
		// is in accordance with the proportions above, but also where the
		// locality
		// factor places emphasis on shorter edges somehow.
		// not sure exactly how... I guess we go
		// for load/discharge pair a, b:
		// pr[a,b] = pr[load=a] * pr[discharge=b] * something

		final DistanceImporter di = new DistanceImporter(distanceMatrix);
		final List<Pair<Pair<String, String>, Double>> wheel =
			new ArrayList<Pair<Pair<String, String>, Double>>();

		double totalp = 0;
		
		for (final Map.Entry<String, Integer> load : loadSlots.entrySet()) {
			for (final Map.Entry<String, Integer> discharge : dischargeSlots
					.entrySet()) {
				final int distance = di.getDistance(load.getKey(), discharge.getKey());
				if (distance == Integer.MAX_VALUE) continue;
				//need to implement suitable bias-o-factor here
				final Pair<String, String> slot = new Pair<String, String>(
						load.getKey(), discharge.getKey());
				
				final double p = (load.getValue() / (double) totalLoads) * 
				(discharge.getValue() / (double) totalDischarges);
				wheel.add(new Pair<Pair<String, String>, Double>(
						slot, p));
				
				totalp += p;
			}
		}
		final long now = new Date().getTime();
		Collections.shuffle(wheel, random);
		for (int i = 0; i<count; i++) {
			double pin = random.nextDouble() * totalp;
			for (final Pair<Pair<String, String>, Double> choice : wheel) {
				if (pin - choice.getSecond() <= 0) {
					//add choice as a cargo.
					addCargo(scenario, now, choice.getFirst().getFirst(), choice.getFirst().getSecond(),
							minWindow, maxWindow, minVisit, maxVisit, minSlack, maxSlack, scenarioDuration);
					break;
				}
				pin -= choice.getSecond();
			}
		}

		return scenario;
	}

	/**
	 * Add a random cargo to the scenario;
	 * 
	 * @param scenario the scenario to add to
	 * @param now the current time as a long
	 * @param loadPortName the name of the load port
	 * @param dischargePortName the name of the discharge port
	 * @param minWindow the shortest arrival time window length
	 * @param maxWindow the longest arrival time window length
	 * @param minVisit the shortest port visit duration
	 * @param maxVisit the longest port visit duration
	 * @param minSlack the minimum number of days of slack in the journey
	 * @param maxSlack the maximum number of days of slack in the journey
	 * @param scenarioDuration the number of days the scenario runs for.
	 */
	private void addCargo(final Scenario scenario, final long now, final String loadPortName, String dischargePortName,
			int minWindow, int maxWindow, int minVisit, int maxVisit,
			int minSlack, int maxSlack, int scenarioDuration) {
		
		final int index = scenario.getCargoModel().getCargoes().size();
		
		Port loadPort = null, dischargePort = null;
		for (final Port p : scenario.getPortModel().getPorts()) {
			if (p.getName().equals(loadPortName))
				loadPort = p;
			if (p.getName().equals(dischargePortName))
				dischargePort = p;
			if (loadPort != null && dischargePort != null)
				break;
		}
		
		DistanceLine line = null;
		for (final DistanceLine dl : scenario.getDistanceModel().getDistances()) {
			if (dl.getFromPort().equals(loadPort) && dl.getToPort().equals(dischargePort))
				line = dl;
		}
		
		assert line!=null;
		
		final LoadSlot loadSlot = cargoFactory.createLoadSlot();
		final Slot dischargeSlot = cargoFactory.createSlot();
		
		loadSlot.setId("load-" + index);
		dischargeSlot.setId("discharge-" + index);
		
		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);
		
		final int minTravelTime = Calculator.getTimeFromSpeedDistance(
				Calculator.scale(20), line.getDistance()) / 24;
		final int maxTravelTime = Calculator.getTimeFromSpeedDistance(
				Calculator.scale(12), line.getDistance()) / 24;
		
		// Pick load start time window day
		final int startDay = random.nextInt(scenarioDuration);
		
		final int loadVisitDuration = minVisit + random.nextInt(maxVisit - minVisit+1);
		final int dischargeVisitDuration = minVisit + random.nextInt(maxVisit - minVisit+1);
		
		final int endDay = startDay + minTravelTime + loadVisitDuration + 
			minSlack + random.nextInt(maxTravelTime - minTravelTime + maxSlack-minSlack+1);
		
		final int loadWindow = minWindow + random.nextInt(maxWindow - minWindow+1);
		final int dischargeWindow = minWindow + random.nextInt(maxWindow - minWindow+1);
		
		loadSlot.setSlotDuration(loadVisitDuration);
		dischargeSlot.setSlotDuration(dischargeVisitDuration);
		
		loadSlot.setWindowDuration(loadWindow);
		dischargeSlot.setWindowDuration(dischargeWindow);
		
		loadSlot.setWindowStart(new Date(now + Timer.ONE_DAY * startDay));
		dischargeSlot.setWindowStart(new Date(now + Timer.ONE_DAY * endDay));
		
		dischargeSlot.setUnitPrice(3.70f + random.nextInt(10));
		loadSlot.setUnitPrice(dischargeSlot.getUnitPrice() - 0.2f);

		loadSlot.setMinQuantity(0);
		loadSlot.setMaxQuantity(200000);
		loadSlot.setCargoCVvalue(22.8f);
		
		dischargeSlot.setMinQuantity(0);
		dischargeSlot.setMaxQuantity(200000);
		
		final Cargo cargo = cargoFactory.createCargo();
		
		cargo.setId("cargo-" + index);
		cargo.setLoadSlot(loadSlot);
		cargo.setDischargeSlot(dischargeSlot);
		
		scenario.getCargoModel().getCargoes().add(cargo);
	}

	/**
	 * Add a number of random charter outs to the given scenario. They will be created uniformly at random,
	 * starting between day 0 and day {@code scenarioLength}, and having lengths uniformly chosen from
	 * {@code minCharterLength} to {@code maxCharterLength}.
	 * 
	 * @param scenario
	 * @param charterOuts
	 * @param minCharterLength
	 * @param maxCharterLength
	 * @param scenarioLength
	 * @return
	 */
	public Scenario addCharterOuts(final Scenario scenario, int charterOuts,
			final int minCharterLength, final int maxCharterLength, final int scenarioLength) {
		while (charterOuts-- > 0)
			createRandomCharterout(scenario, minCharterLength, maxCharterLength, scenarioLength);
		return scenario;
	}

	/**
	 * Add some stanard optimiser settings to the given scenario
	 * 
	 * @param scenario
	 */
	public void addStandardSettings(Scenario scenario) {
		final OptimiserFactory of = OptimiserPackage.eINSTANCE
				.getOptimiserFactory();
		final LsoFactory lsof = LsoPackage.eINSTANCE.getLsoFactory();

		Optimisation optimisation = of.createOptimisation();

		LSOSettings settings = lsof.createLSOSettings();

		settings.setName("Default LSO Settings");

		// create constraints
		{
			final EList<Constraint> constraints = settings.getConstraints();
			constraints.add(createConstraint(of,
					ResourceAllocationConstraintCheckerFactory.NAME, true));
			constraints
					.add(createConstraint(
							of,
							OrderedSequenceElementsConstraintCheckerFactory.NAME,
							true));
			constraints.add(createConstraint(of,
					PortTypeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of,
					TravelTimeConstraintCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME,
							1));
			objectives.add(createObjective(of,
					CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME,
					1));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME,
							1));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME,
							1));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.DISTANCE_COMPONENT_NAME,
							0));
		}

		settings.setNumberOfSteps(20000);
		ThresholderSettings thresholderSettings = lsof
				.createThresholderSettings();
		thresholderSettings.setAlpha(0.9);
		thresholderSettings.setEpochLength(1000);
		thresholderSettings.setInitialAcceptanceRate(0.2);
		settings.setThresholderSettings(thresholderSettings);
		//
		// RandomMoveGeneratorSettings mgs =
		// lsof.createRandomMoveGeneratorSettings();
		// mgs.setUsing2over2(true);
		// mgs.setUsing3over2(true);
		// mgs.setUsing4over1(true);
		// mgs.setUsing4over2(true);
		//
		// settings.setMoveGeneratorSettings(mgs);

		optimisation.getAllSettings().add(settings);
		optimisation.setCurrentSettings(settings);
		scenario.setOptimisation(optimisation);
	}

	Constraint createConstraint(OptimiserFactory of, String name,
			boolean enabled) {
		Constraint c = of.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

	Objective createObjective(OptimiserFactory of, String name, double weight) {
		Objective o = of.createObjective();
		o.setName(name);
		o.setWeight(weight);
		return o;
	}

	private Vessel addVessel(Scenario scenario, String string,
			VesselClass class1) {
		Vessel v = fleetFactory.createVessel();

		v.setName(string);
		v.setClass(class1);

		scenario.getFleetModel().getFleet().add(v);

		return v;
	}

	private VesselStateAttributes createVesselStateAttributes(
			VesselState state, float nbo, float idlenbo, float idleconsumption,
			float[][] curve) {
		VesselStateAttributes vsa = fleetFactory.createVesselStateAttributes();

		vsa.setVesselState(state);
		vsa.setIdleConsumptionRate(idleconsumption);
		vsa.setNboRate(nbo);
		vsa.setIdleNBORate(idlenbo);

		for (float[] point : curve) {
			FuelConsumptionLine line = fleetFactory.createFuelConsumptionLine();
			line.setConsumption(point[0]);
			line.setSpeed(point[1]);
			vsa.getFuelConsumptionCurve().add(line);
		}

		return vsa;
	}

	private VesselClass addVesselClass(Scenario scenario, String name, int minSpeed,
			float f, long capacity, int baseFuelUnitPrice) {
		VesselClass vc = fleetFactory.createVesselClass();

		vc.setName(name);
		vc.setMinSpeed(minSpeed);
		vc.setMaxSpeed(f);
		vc.setCapacity(capacity);
		vc.setBaseFuelUnitPrice(baseFuelUnitPrice);
		vc.setDailyCharterPrice(40000);

		scenario.getFleetModel().getVesselClasses().add(vc);

		return vc;
	}

	/**
	 * Create a random charter out with a duration between minSize and maxSize, and add it to the given
	 * scenario. Chooses a load port by picking a random cargo from the scenario and using its load port.
	 * @param scenario
	 * @param minSize
	 * @param maxSize
	 */
	private void createRandomCharterout(Scenario scenario, int minSize, int maxSize, final int scenarioLength) {
		CharterOut co = fleetFactory.createCharterOut();

		// narrow initial TW for now
		final long now = (new Date()).getTime();
		final int size = random.nextInt(maxSize - minSize) + minSize;
		final int startDay = random.nextInt(scenarioLength - size);
		co.setStartDate(new Date(now + startDay * Timer.ONE_DAY));
		co.setEndDate(new Date(now + startDay * Timer.ONE_DAY + 6
				* Timer.ONE_HOUR));
		co.setDuration(size);
		co.getVesselClasses().add(
				RandomHelper.chooseElementFrom(random, scenario.getFleetModel()
						.getVesselClasses()));

		co.setPort(RandomHelper
				.chooseElementFrom(random,
						scenario.getCargoModel().getCargoes()).getLoadSlot()
				.getPort());

		scenario.getFleetModel().getCharterOuts().add(co);
	}
	
	/**
	 * Set a random start constraint for the given vessel from the given scenario.
	 * Currently puts the vessel at a uniformly randomly chosen port, with no time constraint.
	 * @param scenario
	 * @param addVessel
	 */
	private void randomiseAvailability(Scenario scenario, Vessel addVessel) {
		// Add a random start constraint.
		PortAndTime start = fleetFactory.createPortAndTime();
		EList<Port> ports = scenario.getPortModel().getPorts();
		start.setPort(ports.get(random.nextInt(ports.size())));
	}
}
