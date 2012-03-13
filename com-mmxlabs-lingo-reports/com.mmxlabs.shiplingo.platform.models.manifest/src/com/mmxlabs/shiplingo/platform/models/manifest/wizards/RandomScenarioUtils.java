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

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.common.csv.DistanceImporter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;

/**
 * A class for constructing the EMF representations of random test scenarios
 * 
 * @author hinton
 * 
 */
public class RandomScenarioUtils {
	private final MMXCoreFactory mmxCoreFactory = MMXCorePackage.eINSTANCE.getMMXCoreFactory();
	private final FleetFactory fleetFactory = FleetPackage.eINSTANCE.getFleetFactory();
	private final PortFactory portFactory = PortPackage.eINSTANCE.getPortFactory();
	private final CargoFactory cargoFactory = CargoPackage.eINSTANCE.getCargoFactory();
	private final PricingFactory pricingFactory = PricingPackage.eINSTANCE.getPricingFactory();
	private final CommercialFactory commercialFactory = CommercialPackage.eINSTANCE.getCommercialFactory();
	private final ScheduleFactory scheduleFactory = SchedulePackage.eINSTANCE.getScheduleFactory();

	private final Random random;

	public RandomScenarioUtils(final Random random) {
		this.random = random;
	}

	public RandomScenarioUtils() {
		this(new Random(1));
	}

	public MMXRootObject createScenario() {
		final MMXRootObject rootObject = ManifestJointModel.createEmptyInstance();

		return rootObject;
	}

	public MMXRootObject addDistanceModel(final MMXRootObject scenario, final String distanceFile) throws FileNotFoundException, IOException {
		final DistanceImporter di = new DistanceImporter(distanceFile);
		final PortModel portModel = scenario.getSubModel(PortModel.class);

		for (final String s : di.getKeys()) {
			final Port port = portFactory.createPort();
			port.setName(s);
			portModel.getPorts().add(port);
		}

		final Route route = portFactory.createRoute();
		route.setName("default");
		portModel.getRoutes().add(route);
		for (final Port a : portModel.getPorts()) {
			for (final Port b : portModel.getPorts()) {
				final int distance = di.getDistance(a.getName(), b.getName());
				if (!(a.equals(b)) && (distance != Integer.MAX_VALUE)) {
					final RouteLine dl = portFactory.createRouteLine();
					dl.setFrom(a);
					dl.setTo(b);
					dl.setDistance(distance);
					route.getLines().add(dl);
				}
			}
		}

		return scenario;
	}

	public MMXRootObject addDefaultFleet(final MMXRootObject scenario, final int spotCount) {
		// generate the standard fleet
		final VesselClass steam_138 = addVesselClass(scenario, "STEAM-138", 12, 19.5f, 138000);
		final VesselClass steam_145 = addVesselClass(scenario, "STEAM-145", 12, 19.5f, 145000);
		final VesselClass dfde_177 = addVesselClass(scenario, "DFDE-177", 12, 19.5f, 177000); // TODO different from testutils, because the
		// curve for DFDE doesn't contain a speed of 20.
		final VesselClass steam_126 = addVesselClass(scenario, "STEAM-126", 12, 19.5f, 138000); // TODO units in the model; should it be a
		// float?

		final double[][] steamLaden = new double[][] { { 12, 91 }, { 13, 100 }, { 14, 115 }, { 15, 125 }, { 16, 140 }, { 17, 150 }, { 18, 160 }, { 19, 176 }, { 19.5f, 190 } };
		final double[][] dfdeLaden = new double[][] { { 12, 50 }, { 13, 65 }, { 14, 74 }, { 15, 85 }, { 16, 100 }, { 17, 110 }, { 18, 120 }, { 19, 133 }, { 19.5f, 145 } };

		final double[][] steamBallast = new double[steamLaden.length][2];
		for (int i = 0; i < steamLaden.length; i++) {
			steamBallast[i][0] = steamLaden[i][0];
			steamBallast[i][1] = (int) (0.94f * steamLaden[i][1]);
		}
		final double[][] dfdeBallast = new double[dfdeLaden.length][2];
		for (int i = 0; i < dfdeBallast.length; i++) {
			dfdeBallast[i][0] = dfdeLaden[i][0];
			dfdeBallast[i][1] = (int) (0.94f * dfdeLaden[i][1]);
		}

		final BaseFuel costs400 = FleetFactory.eINSTANCE.createBaseFuel();
		costs400.setName("MDO");
		costs400.setEquivalenceFactor(0.5f);

		// PPricing

		PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		FleetCostModel fleetCostModel = pricingFactory.createFleetCostModel();
		pricingModel.setFleetCost(fleetCostModel);

		{
			DataIndex<Double> idx = pricingFactory.createDataIndex();
			idx.setName("MDO");

			IndexPoint<Double> p = pricingFactory.createIndexPoint();
			p.setValue(400.0);
			idx.getPoints().add(p);

			BaseFuelCost bfp400 = pricingFactory.createBaseFuelCost();
			bfp400.setFuel(costs400);
			bfp400.setPrice(400);
			fleetCostModel.getBaseFuelPrices().add(bfp400);
			pricingModel.getCommodityIndices().add(idx);

		}

		final BaseFuel costs600 = FleetFactory.eINSTANCE.createBaseFuel();
		costs600.setName("HFO");
		costs600.setEquivalenceFactor(0.5f);
		{
			DataIndex<Double> idx = pricingFactory.createDataIndex();
			idx.setName("HFO");

			IndexPoint<Double> p = pricingFactory.createIndexPoint();
			p.setValue(400.0);
			idx.getPoints().add(p);

			BaseFuelCost bfp = pricingFactory.createBaseFuelCost();
			bfp.setFuel(costs600);
			bfp.setPrice(600);
			fleetCostModel.getBaseFuelPrices().add(bfp);
			pricingModel.getCommodityIndices().add(idx);

		}
		FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		fleetModel.getBaseFuels().add(costs400);
		fleetModel.getBaseFuels().add(costs600);
		// create class parameters; currently model uses containment for curves,
		// so we need to do duplicates
		steam_138.setLadenAttributes(createVesselStateAttributes(200, 180, 50, steamLaden));
		steam_138.setBallastAttributes(createVesselStateAttributes(180, 100, 50, steamBallast));
		steam_138.setBaseFuel(costs400);

		steam_145.setLadenAttributes(createVesselStateAttributes(200, 180, 50, steamLaden));
		steam_145.setBallastAttributes(createVesselStateAttributes(180, 100, 50, steamBallast));
		steam_145.setBaseFuel(costs400);

		dfde_177.setLadenAttributes(createVesselStateAttributes(230, 210, 50, dfdeLaden));
		dfde_177.setBallastAttributes(createVesselStateAttributes(180, 100, 50, dfdeBallast));
		dfde_177.setBaseFuel(costs600);

		steam_126.setLadenAttributes(createVesselStateAttributes(200, 180, 50, steamLaden));
		steam_126.setBallastAttributes(createVesselStateAttributes(180, 100, 50, steamBallast));
		steam_126.setBaseFuel(costs400);

		CharterCostModel charterCostModel = pricingFactory.createCharterCostModel();
		{
			DataIndex<Integer> idx = pricingFactory.createDataIndex();
			IndexPoint<Integer> p = pricingFactory.createIndexPoint();
			p.setValue(100000);
			idx.getPoints().add(p);
			charterCostModel.setCharterInPrice(idx);
			pricingModel.getCharterIndices().add(idx);
		}
		{
			DataIndex<Integer> idx = pricingFactory.createDataIndex();
			IndexPoint<Integer> p = pricingFactory.createIndexPoint();
			p.setValue(80000);
			idx.getPoints().add(p);
			charterCostModel.setCharterOutPrice(idx);
			pricingModel.getCharterIndices().add(idx);
		}
		{
			DataIndex<Integer> idx = pricingFactory.createDataIndex();
			IndexPoint<Integer> p = pricingFactory.createIndexPoint();
			p.setValue(4);
			idx.getPoints().add(p);
			charterCostModel.setSpotCharterCount(4);
			pricingModel.getCharterIndices().add(idx);
			
		}

		fleetCostModel.getCharterCosts().add(charterCostModel);

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

	public MMXRootObject addRandomCargoes(final MMXRootObject scenario, final String distanceMatrix, final String slotsFile, final int count, final int minWindow, final int maxWindow,
			final int minVisit, final int maxVisit, final int minSlack, final int maxSlack, final double locality, final int scenarioDuration) throws NumberFormatException, IOException {

		PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		CommercialModel commercialModel = scenario.getSubModel(CommercialModel.class);

		// set up markets
		final DataIndex<Double> loadCurve = pricingFactory.createDataIndex();
		final DataIndex<Double> dischargeCurve = pricingFactory.createDataIndex();
		pricingModel.getCommodityIndices().add(loadCurve);
		pricingModel.getCommodityIndices().add(dischargeCurve);

		loadCurve.setName("LNG Sales Index");
		dischargeCurve.setName("LNG Purchase Index");

		// random walk time
		// every 30d, price changes a bit. load price tracks discharge price
		// though.
		int dischargePrice = 5000;
		final long now = new Date().getTime();

		for (int i = 0; i < scenarioDuration; i += 30) {
			final Date forwardDate = createHourlyDate(now + (i * Timer.ONE_DAY));
			final IndexPoint<Double> price = pricingFactory.createIndexPoint();

			dischargePrice += (random.nextInt(80) - 40);

			dischargePrice = Math.max(dischargePrice, 3000);// arbitrarly
															// prevent it
															// getting cheap.

			price.setDate(forwardDate);
			price.setValue(dischargePrice / (double) Calculator.ScaleFactor);

			dischargeCurve.getPoints().add(price);

			final IndexPoint<Double> loadPrice = pricingFactory.createIndexPoint();
			loadPrice.setDate(forwardDate);
			loadPrice.setValue((dischargePrice - 200) / (double) Calculator.ScaleFactor);
			loadCurve.getPoints().add(loadPrice);
		}

		final LegalEntity loadEntity = commercialFactory.createLegalEntity();
		final LegalEntity shipEntity = commercialFactory.createLegalEntity();
		final LegalEntity dischargeEntity = commercialFactory.createLegalEntity();

		loadEntity.setName("load entity");
		dischargeEntity.setName("discharge entity");
		shipEntity.setName("shipping entity");

		commercialModel.getEntities().add(loadEntity);
		commercialModel.getEntities().add(dischargeEntity);
		// Contained here
		commercialModel.getEntities().add(shipEntity);
		// But referenced here
		commercialModel.setShippingEntity(shipEntity);

		final IndexPriceContract dischargeContract = commercialFactory.createIndexPriceContract();
		final IndexPriceContract loadContract = commercialFactory.createIndexPriceContract();

		// dischargeContract.setRegasEfficiency(1);
		dischargeContract.setEntity(dischargeEntity);
		dischargeContract.setName("discharge contract");
		dischargeContract.setIndex(dischargeCurve);

		loadContract.setEntity(loadEntity);
		loadContract.setIndex(loadCurve);
		loadContract.setName("load contract");

		commercialModel.getSalesContracts().add(dischargeContract);
		commercialModel.getPurchaseContracts().add(loadContract);

		// dischargeSlot.setUnitPrice(3.70f + random.nextInt(10));
		// loadSlot.setUnitPrice(dischargeSlot.getUnitPrice() - 0.2f);

		// load slots file

		BufferedReader br = null;

		final Map<String, Integer> loadSlots = new HashMap<String, Integer>();
		final Map<String, Integer> dischargeSlots = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(new FileReader(new File(slotsFile)));

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
		} finally {
			if (br != null) {
				br.close();
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
	private void addCargo(final MMXRootObject scenario, final long now, final String loadPortName, final String dischargePortName, final int minWindow, final int maxWindow, final int minVisit,
			final int maxVisit, final int minSlack, final int maxSlack, final int scenarioDuration, final PurchaseContract loadContract, final SalesContract dischargeContract) {

		CargoModel cargoModel = scenario.getSubModel(CargoModel.class);
		PortModel portModel = scenario.getSubModel(PortModel.class);
		final int index = cargoModel.getCargos().size();

		Port loadPort = null, dischargePort = null;
		for (final Port p : portModel.getPorts()) {
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

		RouteLine line = null;
		for (final RouteLine dl : portModel.getRoutes().get(0).getLines()) {
			if (dl.getFrom().equals(loadPort) && dl.getTo().equals(dischargePort)) {
				line = dl;
			}
		}

		assert line != null;

		final LoadSlot loadSlot = cargoFactory.createLoadSlot();
		final DischargeSlot dischargeSlot = cargoFactory.createDischargeSlot();

		loadSlot.setName("load-" + index);
		dischargeSlot.setName("discharge-" + index);

		loadSlot.setPort(loadPort);
		dischargeSlot.setPort(dischargePort);

		// How quickly can the fastest vessel arrive (days)
		final int minTravelTime = (int) Math.ceil(Calculator.getTimeFromSpeedDistance((int) Calculator.scale(20), line.getDistance()) / 24.0);
		// How slowly can the slowest vessel arrive (days)
		final int maxTravelTime = (int) Math.ceil(Calculator.getTimeFromSpeedDistance((int) Calculator.scale(12), line.getDistance()) / 24.0);

		// Pick load start time window day
		final int startDay = random.nextInt(scenarioDuration);

		final int loadVisitDuration = minVisit + random.nextInt((maxVisit - minVisit) + 1);
		final int dischargeVisitDuration = minVisit + random.nextInt((maxVisit - minVisit) + 1);

		final int endDay = startDay + minTravelTime + (int) Math.ceil(loadVisitDuration / 24.0) + minSlack + random.nextInt((((maxTravelTime - minTravelTime) + maxSlack) - minSlack) + 1);

		final int loadWindow = minWindow + random.nextInt((maxWindow - minWindow) + 1);
		final int dischargeWindow = minWindow + random.nextInt((maxWindow - minWindow) + 1);

		loadSlot.setDuration(loadVisitDuration);
		dischargeSlot.setDuration(dischargeVisitDuration);

		loadSlot.setWindowSize(loadWindow);
		dischargeSlot.setWindowSize(dischargeWindow);

		loadSlot.setWindowStart(createHourlyDate(now + (Timer.ONE_DAY * startDay)));
		dischargeSlot.setWindowStart(createHourlyDate(now + (Timer.ONE_DAY * endDay)));

		// dischargeSlot.setMarket(dischargeMarket);
		// loadSlot.setMarket(loadMarket);
		dischargeSlot.setContract(dischargeContract);
		loadSlot.setContract(loadContract);

		loadSlot.setMinQuantity(0);
		loadSlot.setMaxQuantity(200000);
		loadSlot.setCargoCV(22.8f);

		dischargeSlot.setMinQuantity(0);
		dischargeSlot.setMaxQuantity(200000);

		final Cargo cargo = cargoFactory.createCargo();

		cargo.setName("cargo-" + index);
		cargo.setLoadSlot(loadSlot);
		cargo.setDischargeSlot(dischargeSlot);

		cargoModel.getCargos().add(cargo);
		cargoModel.getLoadSlots().add(loadSlot);
		cargoModel.getDischargeSlots().add(dischargeSlot);
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
	public MMXRootObject addCharterOuts(final MMXRootObject scenario, int charterOuts, final int minCharterLength, final int maxCharterLength, final int scenarioLength) {
		while (charterOuts-- > 0) {
			createRandomCharterout(scenario, minCharterLength, maxCharterLength, scenarioLength);
		}
		return scenario;
	}

	private Vessel addVessel(final MMXRootObject scenario, final String string, final VesselClass class1) {
		final Vessel v = fleetFactory.createVessel();

		v.setName(string);
		v.setVesselClass(class1);

		scenario.getSubModel(FleetModel.class).getVessels().add(v);

		return v;
	}

	private VesselStateAttributes createVesselStateAttributes(final int nbo, final int idlenbo, final int idleconsumption, final double[][] curve) {
		final VesselStateAttributes vsa = fleetFactory.createVesselStateAttributes();

		vsa.setIdleBaseRate(idleconsumption);
		vsa.setNboRate(nbo);
		vsa.setIdleNBORate(idlenbo);

		for (final double[] point : curve) {
			final FuelConsumption line = fleetFactory.createFuelConsumption();
			line.setSpeed(point[0]);
			line.setConsumption((int) point[1]);
			vsa.getFuelConsumption().add(line);
		}

		return vsa;
	}

	private VesselClass addVesselClass(final MMXRootObject scenario, final String name, final double minSpeed, final double maxSpeed, final int capacity) {
		final VesselClass vc = fleetFactory.createVesselClass();

		vc.setName(name);
		vc.setMinSpeed(minSpeed);
		vc.setMaxSpeed(maxSpeed);
		vc.setCapacity(capacity);

		scenario.getSubModel(FleetModel.class).getVesselClasses().add(vc);

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
	private void createRandomCharterout(final MMXRootObject scenario, final int minSize, final int maxSize, final int scenarioLength) {
		final CharterOutEvent co = fleetFactory.createCharterOutEvent();

		FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		CargoModel cargoModel = scenario.getSubModel(CargoModel.class);

		// narrow initial TW for now
		final long now = (new Date()).getTime();
		final int size = random.nextInt(maxSize - minSize) + minSize;
		final int startDay = random.nextInt(scenarioLength - size);
		co.setStartAfter(createHourlyDate(now + (startDay * Timer.ONE_DAY)));
		co.setStartBy(createHourlyDate(now + (startDay * Timer.ONE_DAY) + (6 * Timer.ONE_HOUR)));
		co.setDurationInDays(size);
		co.getAllowedVessels().add(RandomHelper.chooseElementFrom(random, fleetModel.getVesselClasses()));

		co.setPort(RandomHelper.chooseElementFrom(random, cargoModel.getCargos()).getLoadSlot().getPort());

		co.setRelocateTo(co.getPort());

		fleetModel.getVesselEvents().add(co);
	}

	/**
	 * Set a random start constraint for the given vessel from the given scenario. Currently puts the vessel at a uniformly randomly chosen port, with no time constraint.
	 * 
	 * @param scenario
	 * @param addVessel
	 */
	private void randomiseAvailability(final MMXRootObject scenario, final Vessel addVessel) {
		// TODO: This method did not do a lot...
		// // Add a random start constraint.
		// final PortAndTime start = fleetFactory.createPortAndTime();
		// final EList<Port> ports = scenario.getPortModel().getPorts();
		// start.setPort(ports.get(random.nextInt(ports.size())));
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
