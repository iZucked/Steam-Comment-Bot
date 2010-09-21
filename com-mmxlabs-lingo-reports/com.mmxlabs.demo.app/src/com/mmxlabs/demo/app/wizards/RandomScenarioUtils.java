package com.mmxlabs.demo.app.wizards;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
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
import scenario.optimiser.lso.RandomMoveGeneratorSettings;
import scenario.optimiser.lso.ThresholderSettings;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.port.PortModel;
import scenario.port.PortPackage;

import com.mmxlabs.common.csv.DistanceImporter;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
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
	FleetFactory ff = FleetPackage.eINSTANCE.getFleetFactory();
	private Random random;

	public RandomScenarioUtils(Random random) {
		this.random = random;
	}

	public RandomScenarioUtils() {
		this(new Random(1));
	}

	/**
	 * Create a random EMF scenario object.
	 * 
	 * @param distanceMatrixFilePath
	 *            a distance matrix file's path
	 * @param addStandardFleet
	 *            whether to add the standard fleet
	 * @param randomCargoCount
	 *            how many random cargoes to generate (only works if
	 *            addStandardFleet is true)
	 * @param addStandardOptimiserSettings
	 *            whether to add a default set of optimiser parameters
	 * 
	 * @return a new scenario
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Scenario createRandomScenario(String distanceMatrixFilePath,
			boolean addStandardFleet, int randomCargoCount,
			boolean addStandardOptimiserSettings) throws FileNotFoundException,
			IOException {
		// create scenario
		Scenario scenario = ScenarioPackage.eINSTANCE.getScenarioFactory()
				.createScenario();
		// create standard elements of scenario
		addDistanceMatrixFromDistanceImporter(scenario, new DistanceImporter(
				distanceMatrixFilePath));

		return finishCreatingRandomScenario(scenario, addStandardFleet,
				randomCargoCount, addStandardOptimiserSettings);
	}

	public Scenario createRandomScenario(InputStream stream,
			boolean addStandardFleet, int randomCargoCount,
			boolean addStandardOptimiserSettings) throws IOException {
		Scenario scenario = ScenarioPackage.eINSTANCE.getScenarioFactory()
				.createScenario();
		addDistanceMatrixFromDistanceImporter(scenario, new DistanceImporter(
				stream));

		return finishCreatingRandomScenario(scenario, addStandardFleet,
				randomCargoCount, addStandardOptimiserSettings);
	}

	protected Scenario finishCreatingRandomScenario(Scenario scenario,
			boolean addStandardFleet, int randomCargoCount,
			boolean addStandardOptimiserSettings) {
		scenario.setFleetModel(FleetPackage.eINSTANCE.getFleetFactory()
				.createFleetModel());
		scenario.setCargoModel(CargoPackage.eINSTANCE.getCargoFactory()
				.createCargoModel());

		// Import distance matrix
		if (addStandardFleet) {
			addStandardFleet(scenario);
			if (randomCargoCount > 0) {
				createRandomCargoes(scenario, randomCargoCount);
			}
		}

		if (addStandardOptimiserSettings) {
			addStandardSettings(scenario);
		}

		return scenario;
	}

	/**
	 * Replace the distances and ports in the given scenario with ones from the
	 * given distance importer
	 * 
	 * @param scenario
	 * @param distanceMatrixFilePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void addDistanceMatrixFromDistanceImporter(Scenario scenario,
			DistanceImporter di) {
		// DistanceImporter di = new DistanceImporter(distanceMatrixFilePath);
		final PortFactory pf = PortPackage.eINSTANCE.getPortFactory();
		final PortModel portModel = pf.createPortModel();
		scenario.setPortModel(portModel);

		for (String s : di.getKeys()) {
			Port port = pf.createPort();
			port.setName(s);
			portModel.getPorts().add(port);
		}

		final DistanceModel dm = pf.createDistanceModel();
		scenario.setDistanceModel(dm);

		for (Port a : scenario.getPortModel().getPorts()) {
			for (Port b : scenario.getPortModel().getPorts()) {
				final int distance = di.getDistance(a.getName(), b.getName());
				if (!(a.equals(b)) && distance != Integer.MAX_VALUE) {
					final DistanceLine dl = pf.createDistanceLine();
					dl.setFromPort(a);
					dl.setToPort(b);
					dl.setDistance(distance);
					dm.getDistances().add(dl);
				}
			}
		}
	}

	/**
	 * Add some stanard optimiser settings to the given scenario
	 * 
	 * @param scenario
	 */
	void addStandardSettings(Scenario scenario) {
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

		settings.setNumberOfSteps(750000);
		ThresholderSettings thresholderSettings = lsof
				.createThresholderSettings();
		thresholderSettings.setAlpha(0.95);
		thresholderSettings.setEpochLength(5000);
		thresholderSettings.setInitialAcceptanceRate(0.25);
		settings.setThresholderSettings(thresholderSettings);

		RandomMoveGeneratorSettings mgs = lsof.createRandomMoveGeneratorSettings();
		mgs.setUsing2over2(true);
		mgs.setUsing3over2(true);
		mgs.setUsing4over1(true);
		mgs.setUsing4over2(true);
		
		settings.setMoveGeneratorSettings(mgs);
		
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

	void addStandardFleet(Scenario scenario) {
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
				VesselState.LADEN, 1.38f / 24.0f, 1.18f / 24.0f, 1 / 24.0f, steam));
		class1.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.38f / 24.0f, 1.18f / 24.0f, 1 / 24.0f, steam));
		class1.setBaseFuelEquivalenceFactor(0.5);

		class2.setLadenAttributes(createVesselStateAttributes(
				VesselState.LADEN, 1.45f / 24.0f, 1.25f / 24.0f, 1 / 24.0f, steam));
		class2.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.45f / 24.0f, 1.25f / 24.0f, 1 / 24.0f, steam));
		class2.setBaseFuelEquivalenceFactor(0.5);

		class3.setLadenAttributes(createVesselStateAttributes(
				VesselState.LADEN, 1.77f / 24.0f, 1.57f / 24.0f, 1 / 24.0f, dfde));
		class3.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.77f / 24.0f, 1.57f / 24.0f, 1 / 24.0f, dfde));
		class3.setBaseFuelEquivalenceFactor(0.5);

		class4.setLadenAttributes(createVesselStateAttributes(
				VesselState.LADEN, 1.26f / 24.0f, 1.06f / 24.0f, 1 / 24.0f, steam));
		class4.setBallastAttributes(createVesselStateAttributes(
				VesselState.BALLAST, 1.25f / 24.0f, 1.06f / 24.0f, 1 / 24.0f, steam));
		class4.setBaseFuelEquivalenceFactor(0.5);

		class1.setSpotCharterCount(3);
		class2.setSpotCharterCount(3);
		class3.setSpotCharterCount(3);
		class4.setSpotCharterCount(3);

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

		// addVessel(scenario, "Extra-Charter-1",
		// class1).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-2",
		// class2).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-3",
		// class3).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-4",
		// class4).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-5",
		// class1).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-6",
		// class2).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-7",
		// class3).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-8",
		// class4).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-9",
		// class1).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-10",
		// class2).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-11",
		// class3).setInstanceType(VesselInstanceType.SPOT_CHARTER);
		// addVessel(scenario, "Extra-Charter-12",
		// class4).setInstanceType(VesselInstanceType.SPOT_CHARTER);
	}

	private void randomiseAvailability(Scenario scenario, Vessel addVessel) {
		// Add a random start constraint.
		PortAndTime start = ff.createPortAndTime();
		EList<Port> ports = scenario.getPortModel().getPorts();
		start.setPort(ports.get(random.nextInt(ports.size())));
	}

	private Vessel addVessel(Scenario scenario, String string,
			VesselClass class1) {
		Vessel v = ff.createVessel();

		v.setName(string);
		v.setClass(class1);

		scenario.getFleetModel().getFleet().add(v);

		return v;
	}

	private VesselStateAttributes createVesselStateAttributes(
			VesselState state, float nbo, float idlenbo, float idleconsumption,
			float[][] curve) {
		VesselStateAttributes vsa = ff.createVesselStateAttributes();

		vsa.setVesselState(state);
		vsa.setIdleConsumptionRate(idleconsumption);
		vsa.setNboRate(nbo);
		vsa.setIdleNBORate(idlenbo);

		for (float[] point : curve) {
			FuelConsumptionLine line = ff.createFuelConsumptionLine();
			line.setConsumption(point[0]);
			line.setSpeed(point[1]);
			vsa.getFuelConsumptionCurve().add(line);
		}

		return vsa;
	}

	VesselClass addVesselClass(Scenario scenario, String name, int minSpeed,
			float f, long capacity, int baseFuelUnitPrice) {
		VesselClass vc = ff.createVesselClass();

		vc.setName(name);
		vc.setMinSpeed(minSpeed);
		vc.setMaxSpeed(f);
		vc.setCapacity(capacity);
		vc.setBaseFuelUnitPrice(baseFuelUnitPrice);
		vc.setDailyCharterPrice(40000);

		scenario.getFleetModel().getVesselClasses().add(vc);

		return vc;
	}

	void createRandomCargoes(Scenario scenario, int cargoCount) {
		// TODO this is not how it was before; load and discharge ports are more
		// random; there will be less contention.
		CargoFactory cf = CargoPackage.eINSTANCE.getCargoFactory();
		EList<DistanceLine> lines = scenario.getDistanceModel().getDistances();

		long now = (new Date()).getTime();

		final Set<Port> loadPorts = new HashSet<Port>();
		final Set<Port> dischargePorts = new HashSet<Port>();
		
		for (int i = 0; i < cargoCount; i++) {
			// create a random cargo
			Cargo c = cf.createCargo();
			LoadSlot load = cf.createLoadSlot();
			Slot discharge = cf.createSlot();
			
			DistanceLine dl = null;
			// invalid distance lines shouldn't be in the model
			while (true) {
				dl = lines.get(random.nextInt(lines.size()));
				final Port p1 = dl.getFromPort();
				final Port p2 = dl.getToPort();
				
				final boolean p1l = loadPorts.contains(p1);
				final boolean p2l = loadPorts.contains(p2);
				
				final boolean p1d = dischargePorts.contains(p1);
				final boolean p2d = dischargePorts.contains(p2);
				
				if (p1l && !p2l) {
					load.setPort(p1);
					discharge.setPort(p2);
					loadPorts.add(p1);
					dischargePorts.add(p2);
					break;
				} else if (p2l && !p1l) {
					load.setPort(p2);
					discharge.setPort(p1);
					loadPorts.add(p2);
					dischargePorts.add(p1);
					break;
				} else if (!p1l && !p2l && !p1d && !p2d) {
					load.setPort(p1);
					discharge.setPort(p2);
					loadPorts.add(p1);
					dischargePorts.add(p2);
					break;
				}
			}	
			
			load.setId("load-" + i);
			discharge.setId("discharge-" + i);

			final int minTime = (int) Math.ceil(dl.getDistance() / 20 / 24);
			final int maxTime = (int) Math.ceil(dl.getDistance() / 12 / 24);

			final int start = random.nextInt(365);
			final int end = 1 + start + minTime
					+ random.nextInt(maxTime - minTime + 15);

			Date startDate = new Date(now + start * Timer.ONE_DAY);
			Date endDate = new Date(now + end * Timer.ONE_DAY);

			load.setWindowStart(startDate);
			discharge.setWindowStart(endDate);

			load.setWindowDuration(6);
			discharge.setWindowDuration(6);

			discharge.setUnitPrice(3.70f + random.nextInt(10));
			load.setUnitPrice(discharge.getUnitPrice() - 0.2f);

			load.setMinQuantity(0);
			load.setMaxQuantity(200000);
			load.setCargoCVvalue(22.8f);

			discharge.setMinQuantity(0);
			discharge.setMaxQuantity(200000);

			c.setId("cargo-" + i);
			c.setLoadSlot(load);
			c.setDischargeSlot(discharge);
			scenario.getCargoModel().getCargoes().add(c);
		}
	}

}
