/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.ScenarioPackage;
import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.contract.ContractFactory;
import scenario.contract.ContractPackage;
import scenario.contract.Entity;
import scenario.contract.GroupEntity;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.fleet.CharterOut;
import scenario.fleet.FleetFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;
import scenario.market.Index;
import scenario.market.MarketFactory;
import scenario.market.MarketPackage;
import scenario.market.StepwisePrice;
import scenario.market.StepwisePriceCurve;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.port.PortModel;
import scenario.port.PortPackage;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.common.csv.DistanceImporter;
import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * A class for constructing the EMF representations of random test scenarios
 * 
 * @author hinton
 * 
 */
public class RandomScenarioUtils {
	private final ScenarioFactory scenarioFactory = ScenarioPackage.eINSTANCE.getScenarioFactory();
	private final FleetFactory fleetFactory = FleetPackage.eINSTANCE.getFleetFactory();
	private final PortFactory portFactory = PortPackage.eINSTANCE.getPortFactory();
	private final CargoFactory cargoFactory = CargoPackage.eINSTANCE.getCargoFactory();
	private final MarketFactory marketFactory = MarketPackage.eINSTANCE.getMarketFactory();
	private final ScheduleFactory scheduleFactory = SchedulePackage.eINSTANCE.getScheduleFactory();

	private final Random random;

	public RandomScenarioUtils(final Random random) {
		this.random = random;
	}

	public RandomScenarioUtils() {
		this(new Random(1));
	}

	public Scenario createScenario() {
		final Scenario scenario = scenarioFactory.createScenario();

		scenario.createMissingModels();

		return scenario;
	}

	public Scenario addDistanceModel(final Scenario scenario, final String distanceFile) throws FileNotFoundException, IOException {
		final DistanceImporter di = new DistanceImporter(distanceFile);
		final PortModel portModel = scenario.getPortModel();

		for (final String s : di.getKeys()) {
			final Port port = portFactory.createPort();
			port.setName(s);
			portModel.getPorts().add(port);
		}

		final DistanceModel dm = scenario.getDistanceModel();

		for (final Port a : scenario.getPortModel().getPorts()) {
			for (final Port b : scenario.getPortModel().getPorts()) {
				final int distance = di.getDistance(a.getName(), b.getName());
				if (!(a.equals(b)) && (distance != Integer.MAX_VALUE)) {
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
		final VesselClass steam_138 = addVesselClass(scenario, "STEAM-138", 12, 19.5f, 138000);
		final VesselClass steam_145 = addVesselClass(scenario, "STEAM-145", 12, 19.5f, 145000);
		final VesselClass dfde_177 = addVesselClass(scenario, "DFDE-177", 12, 19.5f, 177000); // TODO different from testutils, because the
		// curve for DFDE doesn't contain a speed of 20.
		final VesselClass steam_126 = addVesselClass(scenario, "STEAM-126", 12, 19.5f, 138000); // TODO units in the model; should it be a
		// float?

		final float[][] steamLaden = new float[][] { { 12, 91 }, { 13, 100 }, { 14, 115 }, { 15, 125 }, { 16, 140 }, { 17, 150 }, { 18, 160 }, { 19, 176 }, { 19.5f, 190 } };
		final float[][] dfdeLaden = new float[][] { { 12, 50 }, { 13, 65 }, { 14, 74 }, { 15, 85 }, { 16, 100 }, { 17, 110 }, { 18, 120 }, { 19, 133 }, { 19.5f, 145 } };

		final float[][] steamBallast = new float[steamLaden.length][2];
		for (int i = 0; i < steamLaden.length; i++) {
			steamBallast[i][0] = steamLaden[i][0];
			steamBallast[i][1] = 0.94f * steamLaden[i][1];
		}
		final float[][] dfdeBallast = new float[dfdeLaden.length][2];
		for (int i = 0; i < dfdeBallast.length; i++) {
			dfdeBallast[i][0] = dfdeLaden[i][0];
			dfdeBallast[i][1] = 0.94f * dfdeLaden[i][1];
		}

		final VesselFuel costs400 = FleetFactory.eINSTANCE.createVesselFuel();
		costs400.setName("MDO");
		costs400.setEquivalenceFactor(0.5f);
		costs400.setUnitPrice(400);

		final VesselFuel costs600 = FleetFactory.eINSTANCE.createVesselFuel();
		costs400.setName("HFO");
		costs400.setEquivalenceFactor(0.5f);
		costs400.setUnitPrice(600);

		// create class parameters; currently model uses containment for curves,
		// so we need to do duplicates
		steam_138.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 200, 180, 50, steamLaden));
		steam_138.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 180, 100, 50, steamBallast));
		steam_138.setBaseFuel(costs400);

		steam_145.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 200, 180, 50, steamLaden));
		steam_145.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 180, 100, 50, steamBallast));
		steam_145.setBaseFuel(costs400);

		dfde_177.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 230, 210, 50, dfdeLaden));
		dfde_177.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 180, 100, 50, dfdeBallast));
		dfde_177.setBaseFuel(costs600);

		steam_126.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 200, 180, 50, steamLaden));
		steam_126.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 180, 100, 50, steamBallast));
		steam_126.setBaseFuel(costs400);

		steam_138.setSpotCharterCount(spotCount);
		steam_145.setSpotCharterCount(spotCount);
		dfde_177.setSpotCharterCount(spotCount);
		steam_126.setSpotCharterCount(spotCount);

		// create vessels in each class

		randomiseAvailability(scenario, addVessel(scenario, "Methane Kari Elin", steam_138));

		randomiseAvailability(scenario, addVessel(scenario, "Methane Rita Andrea", steam_145));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Jane Elizabeth", steam_145));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Lydon Volney", steam_145));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Shirley Elizabeth", steam_145));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Heather Sally", steam_145));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Alison Victoria", steam_145));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Nile Eagle", steam_145));

		randomiseAvailability(scenario, addVessel(scenario, "Methane Julia Louise", dfde_177));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Becki Anne", dfde_177));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Mickie Harper", dfde_177));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Patricia Camila", dfde_177));

		addVessel(scenario, "Hilli", steam_126);
		addVessel(scenario, "Gimi", steam_126);
		addVessel(scenario, "Khannur", steam_126);
		addVessel(scenario, "Golar Freeze", steam_126);
		addVessel(scenario, "Methane Princes", steam_138);
		return scenario;
	}

	public Scenario addRandomCargoes(final Scenario scenario, final String distanceMatrix, final String slotsFile, final int count, final int minWindow, final int maxWindow, final int minVisit,
			final int maxVisit, final int minSlack, final int maxSlack, final double locality, final int scenarioDuration) throws NumberFormatException, IOException {
		// set up markets
		final Index loadMarket = marketFactory.createIndex();
		final Index dischargeMarket = marketFactory.createIndex();

		final StepwisePriceCurve loadCurve = marketFactory.createStepwisePriceCurve();
		final StepwisePriceCurve dischargeCurve = marketFactory.createStepwisePriceCurve();

		loadMarket.setPriceCurve(loadCurve);
		dischargeMarket.setPriceCurve(dischargeCurve);

		loadMarket.setName("LNG Sales Index");
		dischargeMarket.setName("LNG Purchase Index");

		// random walk time
		// every 30d, price changes a bit. load price tracks discharge price
		// though.
		int dischargePrice = 5000;
		final long now = new Date().getTime();

		dischargeCurve.setDefaultValue(dischargePrice / (float) Calculator.ScaleFactor);
		loadCurve.setDefaultValue((dischargePrice - 200) / (float) Calculator.ScaleFactor);

		for (int i = 0; i < scenarioDuration; i += 30) {
			final Date forwardDate = createHourlyDate(now + (i * Timer.ONE_DAY));
			final StepwisePrice price = marketFactory.createStepwisePrice();

			dischargePrice += (random.nextInt(80) - 40);

			dischargePrice = Math.max(dischargePrice, 3000);// arbitrarly
															// prevent it
															// getting cheap.

			price.setDate(forwardDate);
			price.setPriceFromDate(dischargePrice / (float) Calculator.ScaleFactor);

			dischargeCurve.getPrices().add(price);

			final StepwisePrice loadPrice = marketFactory.createStepwisePrice();
			loadPrice.setDate(forwardDate);
			loadPrice.setPriceFromDate((dischargePrice - 200) / (float) Calculator.ScaleFactor);
			loadCurve.getPrices().add(loadPrice);
		}

		scenario.setMarketModel(marketFactory.createMarketModel());
		scenario.getMarketModel().getIndices().add(loadMarket);
		scenario.getMarketModel().getIndices().add(dischargeMarket);

		final ContractFactory contractFactory = ContractPackage.eINSTANCE.getContractFactory();

		final Entity loadEntity = contractFactory.createEntity();
		final GroupEntity shipEntity = contractFactory.createGroupEntity();
		final Entity dischargeEntity = contractFactory.createEntity();

		loadEntity.setName("load entity");
		dischargeEntity.setName("discharge entity");
		shipEntity.setName("shipping entity");

		scenario.setContractModel(contractFactory.createContractModel());
		scenario.getContractModel().getEntities().add(loadEntity);
		scenario.getContractModel().getEntities().add(shipEntity);
		scenario.getContractModel().getEntities().add(dischargeEntity);

		scenario.getContractModel().setShippingEntity(shipEntity);

		final SalesContract dischargeContract = contractFactory.createSalesContract();
		final IndexPricePurchaseContract loadContract = contractFactory.createIndexPricePurchaseContract();

		// dischargeContract.setRegasEfficiency(1);
		dischargeContract.setEntity(dischargeEntity);
		dischargeContract.setName("discharge contract");
		dischargeContract.setIndex(dischargeMarket);

		loadContract.setEntity(loadEntity);
		loadContract.setIndex(loadMarket);
		loadContract.setName("load contract");

		scenario.getContractModel().getSalesContracts().add(dischargeContract);
		scenario.getContractModel().getPurchaseContracts().add(loadContract);

		// dischargeSlot.setUnitPrice(3.70f + random.nextInt(10));
		// loadSlot.setUnitPrice(dischargeSlot.getUnitPrice() - 0.2f);

		// load slots file

		final BufferedReader br = new BufferedReader(new FileReader(new File(slotsFile)));

		final Map<String, Integer> loadSlots = new HashMap<String, Integer>();
		final Map<String, Integer> dischargeSlots = new HashMap<String, Integer>();

		String line;
		int totalLoads = 0;
		int totalDischarges = 0;
		while ((line = br.readLine()) != null) {
			final String[] parts = line.split(",");
			final String pn = parts[0].replace("\"", "");
			final int loadDischargeCount = Integer.parseInt(parts[2]);
			if (parts[1].contains("L")) {
				loadSlots.put(pn, loadDischargeCount);
				totalLoads += loadDischargeCount;
			} else {
				dischargeSlots.put(pn, loadDischargeCount);
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
		final List<Pair<Pair<String, String>, Double>> wheel = new ArrayList<Pair<Pair<String, String>, Double>>();

		final Map<String, Integer> minDistanceFrom = new HashMap<String, Integer>();
		final Map<String, Integer> maxDistanceFrom = new HashMap<String, Integer>();

		for (final String s : loadSlots.keySet()) {
			int maxDistance = 0;
			int minDistance = Integer.MAX_VALUE;
			for (final String s2 : dischargeSlots.keySet()) {
				final int distance = di.getDistance(s, s2);
				if (distance == Integer.MAX_VALUE) {
					continue;
				}

				if (distance < minDistance) {
					minDistance = distance;
				}
				if (distance > maxDistance) {
					maxDistance = distance;
				}
			}
			minDistanceFrom.put(s, minDistance);
			maxDistanceFrom.put(s, maxDistance);
		}

		double totalp = 0;

		for (final Map.Entry<String, Integer> load : loadSlots.entrySet()) {
			final int minDistance = minDistanceFrom.get(load.getKey());
			final int maxDistance = maxDistanceFrom.get(load.getKey());
			for (final Map.Entry<String, Integer> discharge : dischargeSlots.entrySet()) {
				final int distance = di.getDistance(load.getKey(), discharge.getKey());
				if (distance == Integer.MAX_VALUE) {
					continue;
				}
				// need to implement suitable bias-o-factor here
				final Pair<String, String> slot = new Pair<String, String>(load.getKey(), discharge.getKey());

				final double dprop = 1 - ((distance - minDistance) / (double) (maxDistance - minDistance));
				final double loadFactor = dprop * (load.getValue() * discharge.getValue());
				final double dFactor = Math.exp(locality * dprop);
				final double p = loadFactor * dFactor;
				// loadFactor ;+ dFactor
				;

				wheel.add(new Pair<Pair<String, String>, Double>(slot, p));

				totalp += p;
			}
		}

		Collections.shuffle(wheel, random);
		for (int i = 0; i < count; i++) {
			double pin = random.nextDouble() * totalp;
			for (final Pair<Pair<String, String>, Double> choice : wheel) {
				if ((pin - choice.getSecond()) <= 0) {
					// add choice as a cargo.
					addCargo(scenario, now, choice.getFirst().getFirst(), choice.getFirst().getSecond(), minWindow, maxWindow, minVisit, maxVisit, minSlack, maxSlack, scenarioDuration, loadContract,
							dischargeContract);
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
	 * @param scenario
	 *            the scenario to add to
	 * @param now
	 *            the current time as a long
	 * @param loadPortName
	 *            the name of the load port
	 * @param dischargePortName
	 *            the name of the discharge port
	 * @param minWindow
	 *            the shortest arrival time window length
	 * @param maxWindow
	 *            the longest arrival time window length
	 * @param minVisit
	 *            the shortest port visit duration
	 * @param maxVisit
	 *            the longest port visit duration
	 * @param minSlack
	 *            the minimum number of days of slack in the journey
	 * @param maxSlack
	 *            the maximum number of days of slack in the journey
	 * @param scenarioDuration
	 *            the number of days the scenario runs for.
	 * @param dischargeMarket
	 * @param loadMarket
	 */
	private void addCargo(final Scenario scenario, final long now, final String loadPortName, final String dischargePortName, final int minWindow, final int maxWindow, final int minVisit,
			final int maxVisit, final int minSlack, final int maxSlack, final int scenarioDuration, final PurchaseContract loadContract, final SalesContract dischargeContract) {

		final int index = scenario.getCargoModel().getCargoes().size();

		Port loadPort = null, dischargePort = null;
		for (final Port p : scenario.getPortModel().getPorts()) {
			if (p.getName().equals(loadPortName)) {
				loadPort = p;
			}
			if (p.getName().equals(dischargePortName)) {
				dischargePort = p;
			}
			if ((loadPort != null) && (dischargePort != null)) {
				break;
			}
		}

		DistanceLine line = null;
		for (final DistanceLine dl : scenario.getDistanceModel().getDistances()) {
			if (dl.getFromPort().equals(loadPort) && dl.getToPort().equals(dischargePort)) {
				line = dl;
			}
		}

		assert line != null;

		final LoadSlot loadSlot = cargoFactory.createLoadSlot();
		final Slot dischargeSlot = cargoFactory.createSlot();

		loadSlot.setId("load-" + index);
		dischargeSlot.setId("discharge-" + index);

		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);

		// How quickly can the fastest vessel arrive (days)
		final int minTravelTime = (int) Math.ceil(Calculator.getTimeFromSpeedDistance(Calculator.scale(20), line.getDistance()) / 24.0);
		// How slowly can the slowest vessel arrive (days)
		final int maxTravelTime = (int) Math.ceil(Calculator.getTimeFromSpeedDistance(Calculator.scale(12), line.getDistance()) / 24.0);

		// Pick load start time window day
		final int startDay = random.nextInt(scenarioDuration);

		final int loadVisitDuration = minVisit + random.nextInt((maxVisit - minVisit) + 1);
		final int dischargeVisitDuration = minVisit + random.nextInt((maxVisit - minVisit) + 1);

		final int endDay = startDay + minTravelTime + (int) Math.ceil(loadVisitDuration / 24.0) + minSlack + random.nextInt((((maxTravelTime - minTravelTime) + maxSlack) - minSlack) + 1);

		final int loadWindow = minWindow + random.nextInt((maxWindow - minWindow) + 1);
		final int dischargeWindow = minWindow + random.nextInt((maxWindow - minWindow) + 1);

		loadSlot.setSlotDuration(loadVisitDuration);
		dischargeSlot.setSlotDuration(dischargeVisitDuration);

		loadSlot.setWindowDuration(loadWindow);
		dischargeSlot.setWindowDuration(dischargeWindow);

		loadSlot.setWindowStart(new DateAndOptionalTime(createHourlyDate(now + (Timer.ONE_DAY * startDay)), false));
		dischargeSlot.setWindowStart(new DateAndOptionalTime(createHourlyDate(now + (Timer.ONE_DAY * endDay)), false));

		// dischargeSlot.setMarket(dischargeMarket);
		// loadSlot.setMarket(loadMarket);
		dischargeSlot.setContract(dischargeContract);
		loadSlot.setContract(loadContract);

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
	 * Add a number of random charter outs to the given scenario. They will be created uniformly at random, starting between day 0 and day {@code scenarioLength}, and having lengths uniformly chosen
	 * from {@code minCharterLength} to {@code maxCharterLength}.
	 * 
	 * @param scenario
	 * @param charterOuts
	 * @param minCharterLength
	 * @param maxCharterLength
	 * @param scenarioLength
	 * @return
	 */
	public Scenario addCharterOuts(final Scenario scenario, int charterOuts, final int minCharterLength, final int maxCharterLength, final int scenarioLength) {
		while (charterOuts-- > 0) {
			createRandomCharterout(scenario, minCharterLength, maxCharterLength, scenarioLength);
		}
		return scenario;
	}

	private Vessel addVessel(final Scenario scenario, final String string, final VesselClass class1) {
		final Vessel v = fleetFactory.createVessel();

		v.setName(string);
		v.setClass(class1);

		scenario.getFleetModel().getFleet().add(v);

		return v;
	}

	private VesselStateAttributes createVesselStateAttributes(final VesselState state, final float nbo, final float idlenbo, final float idleconsumption, final float[][] curve) {
		final VesselStateAttributes vsa = fleetFactory.createVesselStateAttributes();

		vsa.setVesselState(state);
		vsa.setIdleConsumptionRate(idleconsumption);
		vsa.setNboRate(nbo);
		vsa.setIdleNBORate(idlenbo);

		for (final float[] point : curve) {
			final FuelConsumptionLine line = fleetFactory.createFuelConsumptionLine();
			line.setSpeed(point[0]);
			line.setConsumption(point[1]);
			vsa.getFuelConsumptionCurve().add(line);
		}

		return vsa;
	}

	private VesselClass addVesselClass(final Scenario scenario, final String name, final int minSpeed, final float maxSpeed, final long capacity) {
		final VesselClass vc = fleetFactory.createVesselClass();

		vc.setName(name);
		vc.setMinSpeed(minSpeed);
		vc.setMaxSpeed(maxSpeed);
		vc.setCapacity(capacity);
		vc.setDailyCharterInPrice(40000);

		scenario.getFleetModel().getVesselClasses().add(vc);

		return vc;
	}

	/**
	 * Create a random charter out with a duration between minSize and maxSize, and add it to the given scenario. Chooses a load port by picking a random cargo from the scenario and using its load
	 * port.
	 * 
	 * @param scenario
	 * @param minSize
	 * @param maxSize
	 */
	private void createRandomCharterout(final Scenario scenario, final int minSize, final int maxSize, final int scenarioLength) {
		final CharterOut co = fleetFactory.createCharterOut();

		// narrow initial TW for now
		final long now = (new Date()).getTime();
		final int size = random.nextInt(maxSize - minSize) + minSize;
		final int startDay = random.nextInt(scenarioLength - size);
		co.setStartDate(createHourlyDate(now + (startDay * Timer.ONE_DAY)));
		co.setEndDate(createHourlyDate(now + (startDay * Timer.ONE_DAY) + (6 * Timer.ONE_HOUR)));
		co.setDuration(size);
		co.getVesselClasses().add(RandomHelper.chooseElementFrom(random, scenario.getFleetModel().getVesselClasses()));

		co.setStartPort(RandomHelper.chooseElementFrom(random, scenario.getCargoModel().getCargoes()).getLoadSlot().getPort());

		co.setEndPort(co.getStartPort());

		scenario.getFleetModel().getVesselEvents().add(co);
	}

	/**
	 * Set a random start constraint for the given vessel from the given scenario. Currently puts the vessel at a uniformly randomly chosen port, with no time constraint.
	 * 
	 * @param scenario
	 * @param addVessel
	 */
	private void randomiseAvailability(final Scenario scenario, final Vessel addVessel) {
		// Add a random start constraint.
		final PortAndTime start = fleetFactory.createPortAndTime();
		final EList<Port> ports = scenario.getPortModel().getPorts();
		start.setPort(ports.get(random.nextInt(ports.size())));
	}

	/**
	 * Creates a date object rounded down to the nearest hour.
	 * 
	 * @param time
	 * @return
	 */
	private Date createHourlyDate(final long time) {
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);

		return c.getTime();
	}
}
