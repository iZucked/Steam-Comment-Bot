/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Class to create a scenario. Call methods to customise the scenario. When finished get the final scenario using {@link #buildScenario()}. <br>
 * The scenario can probably have more stuff added to it after getting it from {@link #buildScenario()}, but it could also break it, it's untested.
 * 
 * @author Adam Semenenko
 * 
 */
public class CustomScenarioCreator extends DefaultScenarioCreator {

	private static final Logger log = LoggerFactory.getLogger(CustomScenarioCreator.class);

	public CustomScenarioCreator(final float dischargePrice) {
		this(dischargePrice, "UTC");
	}

	public CustomScenarioCreator(final float dischargePrice, final String timeZone) {
		super(timeZone);

		scenarioModelBuilder.getCommercialModelBuilder().setTaxRates(shippingEntity, createLocalDate(2000, Calendar.JANUARY, 1), 0.0f);
		scenarioModelBuilder.getCommercialModelBuilder().setTaxRates(contractEntity, createLocalDate(2000, Calendar.JANUARY, 1), 0.0f);

		salesContract = addSalesContract("Sales Contract", dischargePrice);
		purchaseContract = addPurchaseContract("Purchase Contract", 0.0);

		scenarioModelBuilder.getPortModelBuilder().createRoute(RouteOption.DIRECT.getName(), RouteOption.DIRECT);
	}

	/**
	 * Add a simple vessel to the scenario.
	 * 
	 * @param vesselClassName
	 *            The name of the class of the vessel
	 * @param numOfVesselsToCreate
	 *            The number of identical vessels to create.
	 * @param baseFuelUnitPrice
	 *            The base fuel unit price.
	 * @param speed
	 *            The one speed the vessel can travel at.
	 * @param capacity
	 * @param consumption
	 *            The fuel consumption (constant for all legs/idles) per day
	 * @param NBORate
	 *            The NBO rate (constant for all legs/idles) per day
	 * @param pilotLightRate
	 *            THe pilot light rate
	 * @param minHeelVolume
	 *            The minimum heel volume
	 */
	public VesselAvailability[] addVesselSimple(final String vesselClassName, final int numOfVesselsToCreate, final float baseFuelUnitPrice, final int speed, final int capacity, final int consumption,
			final int NBORate, final int pilotLightRate, final int minHeelVolume, final boolean isTimeChartered) {

		final float equivalenceFactor = 1;
		final int spotCharterCount = 0;

		return addVessel(vesselClassName, numOfVesselsToCreate, spotCharterCount, baseFuelUnitPrice, equivalenceFactor, speed, speed, capacity, speed, consumption, speed, consumption, consumption,
				NBORate, NBORate, speed, consumption, speed, consumption, consumption, NBORate, NBORate, pilotLightRate, minHeelVolume, isTimeChartered);
	}

	/**
	 * Creates a vessel class and adds the specified number of vessels of the created class to the scenario. The attributes of the vessel class and vessel are set using the arguments.
	 */
	public VesselAvailability[] addVessel(final String vesselClassName, final int numOfVesselsToCreate, final int spotCharterCount, final float baseFuelUnitPrice, final float equivalenceFactor,
			final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed, final int ballastMaxConsumption,
			final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption, final int ladenMaxSpeed,
			final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final int pilotLightRate, final int minHeelVolume,
			final boolean isTimeChartered) {

		final SpotMarketsModelBuilder spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		// final int minHeelVolume = 0;

		final double fillCapacity = 1.0;

		BaseFuel baseFuel = scenarioModelBuilder.getFleetModelBuilder().createBaseFuel("BASE FUEL", equivalenceFactor);
		final BaseFuelCost bfc = scenarioModelBuilder.getCostModelBuilder().createOrUpdateBaseFuelCost(baseFuel, Float.toString(baseFuelUnitPrice));

		final Vessel vessel = scenarioModelBuilder.getFleetModelBuilder().createVessel(vesselClassName, capacity, fillCapacity, minSpeed, maxSpeed, baseFuel, pilotLightRate, minHeelVolume,
				cooldownVolume, warmupTime);

		final CharterInMarket charterInMarket = spotMarketsModelBuilder.createCharterInMarket("market-" + vessel.getName(), vessel, shippingEntity, "0", spotCharterCount);

		scenarioModelBuilder.getFleetModelBuilder().setVesselStateAttributes(vessel, true, ladenNBORate, ladenIdleNBORate, ladenIdleConsumptionRate, 0);
		scenarioModelBuilder.getFleetModelBuilder().setVesselStateAttributes(vessel, false, ballastNBORate, ballastIdleNBORate, ballastIdleConsumptionRate, 0);

		scenarioModelBuilder.getFleetModelBuilder().setVesselStateAttributesCurve(vessel, false, ballastMinSpeed, ballastMinConsumption);
		if (ballastMinSpeed != ballastMaxSpeed) {
			scenarioModelBuilder.getFleetModelBuilder().setVesselStateAttributesCurve(vessel, false, ballastMaxSpeed, ballastMaxConsumption);
		}
		scenarioModelBuilder.getFleetModelBuilder().setVesselStateAttributesCurve(vessel, true, ladenMinSpeed, ladenMinConsumption);
		if (ladenMinSpeed != ladenMaxSpeed) {
			scenarioModelBuilder.getFleetModelBuilder().setVesselStateAttributesCurve(vessel, true, ladenMaxSpeed, ladenMaxConsumption);
		}

		// return a list of all vessels created.
		final VesselAvailability created[] = new VesselAvailability[numOfVesselsToCreate];

		// now create vessels of this class
		for (int i = 0; i < numOfVesselsToCreate; i++) {
			final VesselAvailability availability = scenarioModelBuilder.getCargoModelBuilder().makeVesselAvailability(vessel, shippingEntity) //
					.build();
			if (isTimeChartered) {
				availability.setTimeCharterRate("10");
			}
			created[i] = availability;
		}

		return created;
	}

	/**
	 * Add two ports to the scenario with one or many distances that are symmetric.
	 * 
	 * @param portA
	 *            The port to add.
	 * @param portB
	 *            The second port to add.
	 * @param distancesSymmetric
	 *            A list of symmetrical distances between the two ports.
	 */
	public void addPorts(final Port portA, final Port portB, final int distancesSymmetric) {
		addPorts(portA, portB, distancesSymmetric, distancesSymmetric);
	}

	/**
	 * Add two ports to the scenario and specify the distances from and to each port. <br>
	 * The ports must be created from the method {@link CustomScenarioCreator#createPort(String)} to be correctly added to the scenario.
	 * 
	 * @param portA
	 *            The port to add.
	 * @param portB
	 *            The second port to add.
	 * @param AtoBDistances
	 *            A list of distances from port A to port B
	 * @param BtoADistances
	 *            A list of distances from port B to port A
	 */
	public void addPorts(final Port portA, final Port portB, final int AtoBDistances, final int BtoADistances) {
		PortModel portModel = scenarioModelBuilder.getPortModelBuilder().getPortModel();
		if (!portModel.getPorts().contains(portA)) {
			portModel.getPorts().add(portA);
			portA.getLocation().setTimeZone(timeZone);
		}
		if (!portModel.getPorts().contains(portB)) {
			portModel.getPorts().add(portB);
			portB.getLocation().setTimeZone(timeZone);
		}

		if (!portA.getCapabilities().contains(PortCapability.LOAD)) {
			portA.getCapabilities().add(PortCapability.LOAD);
		}
		if (!portA.getCapabilities().contains(PortCapability.DISCHARGE)) {
			portA.getCapabilities().add(PortCapability.DISCHARGE);
		}

		if (!portB.getCapabilities().contains(PortCapability.LOAD)) {
			portB.getCapabilities().add(PortCapability.LOAD);
		}
		if (!portB.getCapabilities().contains(PortCapability.DISCHARGE)) {
			portB.getCapabilities().add(PortCapability.DISCHARGE);
		}

		// Assuming we've created the default rout
		final Route r = portModel.getRoutes().get(0);

		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(portA, portB, r.getRouteOption(), AtoBDistances, false);
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(portB, portA, r.getRouteOption(), BtoADistances, false);
	}

	/**
	 * Add a cargo to the scenario. <br>
	 * Both the load and discharge ports must be added using {@link #addPorts(Port, Port, int[], int[])} to correctly set up distances.
	 */
	public Cargo addCargo(final String cargoID, final Port loadPort, final Port dischargePort, final String loadPrice, final String dischargePrice, final double cvValue,
			final LocalDateTime loadWindowStart, final int travelTime) {
		PortModel portModel = scenarioModelBuilder.getPortModelBuilder().getPortModel();

		if (!portModel.getPorts().contains(loadPort)) {
			log.warn("Scenario does not contain load port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(loadPort);
		}
		if (!portModel.getPorts().contains(dischargePort)) {
			log.warn("Scenario does not contain discharge port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(dischargePort);
		}

		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		final ZoneId dischargeZone = dischargePort.getZoneId();

		// Use load window for discharge as we will update it later once we have a fully built up load window start datetime
		final Cargo cargo = scenarioModelBuilder.getCargoModelBuilder().makeCargo() //
				.makeFOBPurchase("load" + cargoID, loadWindowStart.toLocalDate(), loadPort, purchaseContract, null, loadPrice, cvValue) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(loadWindowStart.getHour()) //
				.withVolumeLimits(0, loadMaxQuantity, VolumeUnits.M3) //
				.build()//
				//
				.makeDESSale("discharge" + cargoID, loadWindowStart.toLocalDate(), dischargePort, salesContract, null, dischargePrice) //
				.withVolumeLimits(0, dischargeMaxQuantity, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(loadWindowStart.getHour()) //
				.withPricingEvent(PricingEvent.START_DISCHARGE, null) //
				.build()//
				//
				.build();

		// Now calculate the correct discharge window start
		final Slot load = cargo.getSlots().get(0);
		final ZonedDateTime dischargeDate = load.getSchedulingTimeWindow().getStart().withZoneSameInstant(dischargeZone).plusHours(travelTime);

		final Slot discharge = cargo.getSlots().get(1);
		discharge.setWindowStart(dischargeDate.toLocalDate());
		discharge.setWindowStartTime(dischargeDate.getHour());

		return cargo;
	}

	public Cargo addCargo(final String cargoID, final Port loadPort, final Port dischargePort, final int loadPrice, final float dischargePrice, final float cvValue,
			final LocalDateTime loadWindowStart, final int travelTime) {
		return addCargo(cargoID, loadPort, dischargePort, Float.toString(loadPrice), Float.toString(dischargePrice), cvValue, loadWindowStart, travelTime);
	}

	public CharterOutEvent addCharterOut(final String id, final @NonNull Port startPort, final @Nullable Port endPort, final @NonNull LocalDateTime startCharterOut, final int heelLimit,
			final int charterOutDurationDays, final float cvValue, final float dischargePrice, final int dailyCharterOutPrice, final int repositioningFee) {

		return addCharterOut(id, startPort, endPort, startCharterOut, heelLimit, heelLimit, charterOutDurationDays, cvValue, dischargePrice, dailyCharterOutPrice, repositioningFee);
	}

	/**
	 * Creates a canal and costs. Although this only returns a VesselClassCost for the canal costs, the canal can be retrieved by using {@link VesselClassCost#getCanal()}.
	 * 
	 * @param canalOption
	 *            The name of the canal
	 * @param distanceAToB
	 *            Distance along the canal from port A to port B
	 * @param distanceBToA
	 *            Distance along the canal from port B to port A
	 * @param canalLadenCost
	 *            Cost in dollars for a laden vessel
	 * @param canalUnladenCost
	 *            Cost in dollars for a ballast vessel
	 * @param canalTransitFuelDays
	 *            MT of base fuel per day used when in transit
	 * @param canalTransitTime
	 *            Transit time in hours
	 * @return
	 */
	public static void createCanalAndCost(final IScenarioDataProvider scenarioDataProvider, final RouteOption canalOption, final Port A, final Port B, final int distanceAToB, final int distanceBToA,
			final int canalLadenCost, final int canalUnladenCost, final int canalTransitFuelDays, final int canalNBORateDays, final int canalTransitTime) {

		final ScenarioModelBuilder scenarioModelBuilder = new ScenarioModelBuilder(scenarioDataProvider);

		final @NonNull Route canal = scenarioModelBuilder.getPortModelBuilder().createRoute(canalOption.getName(), canalOption);
		scenarioModelBuilder.getDistanceModelBuilder().createDistanceMatrix(canalOption);

		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(A, B, canalOption, distanceAToB, false);
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(B, A, canalOption, distanceBToA, false);

		final FleetModel fleetModel = scenarioModelBuilder.getFleetModelBuilder().getFleetModel();
		for (final Vessel vc : fleetModel.getVessels()) {
			scenarioModelBuilder.getFleetModelBuilder().setRouteParameters(vc, canalOption, canalTransitFuelDays, canalTransitFuelDays, canalNBORateDays, canalNBORateDays, canalTransitTime);
			scenarioModelBuilder.getCostModelBuilder().createRouteCost(vc, canalOption, canalLadenCost, canalUnladenCost);
		}
	}

	/**
	 * A vessel class has a list of inaccessible ports. This method can add to that list if the given vessel class has already been added to the scenario. Note that that this method does not check to
	 * see if the ports are already in the scenario.
	 * 
	 * @param vc
	 *            The vessel class to add the constraint to.
	 * @param inaccessiblePorts
	 *            The ports to make inaccessible.
	 * @return If the ports are added successfully the method will return true. If the vessel class is not in the scenario it will return false.
	 */
	public boolean addInaccessiblePortsOnVessel(final Vessel vc, final Port[] inaccessiblePorts) {

		if (scenarioFleetModelContainsVessel(vc)) {
			FleetModel fleetModel = scenarioModelBuilder.getFleetModelBuilder().getFleetModel();
			for (final Vessel v : fleetModel.getVessels()) {
				if (v.equals(vc)) {
					v.getVesselOrDelegateInaccessiblePorts().addAll(Arrays.asList(inaccessiblePorts));
				}

				return true;
			}
		}
		return false;
	}

	/**
	 * Each cargo has a list of vessels that has all vessels that are allowed to carry the cargo. (If empty there are no restrictions.) This method can add to that list. Note that no checks are made
	 * to see if the given vessels are already in the scenario.
	 * 
	 * @param cargo
	 *            The cargo to add the constraint to.
	 * @param allowedVessels
	 *            The list of vessels to add.
	 */
	public void addAllowedVesselsOnCargo(final Cargo cargo, final List<Vessel> allowedVessels) {

		for (final Slot s : cargo.getSlots()) {
			s.getRestrictedVessels().addAll(allowedVessels);
			s.setRestrictedVesselsArePermissive(true);
		}
	}

	public CommodityCurve addCommodityIndex(final String name) {

		return scenarioModelBuilder.getPricingModelBuilder().makeCommodityDataCurve(name, "currencyUnit", "volumeUnit").build();
	}

	public void addDataToCommodity(final CommodityCurve curve, final YearMonth date, final double value) {
		final YearMonthPoint ip = PricingFactory.eINSTANCE.createYearMonthPoint();
		ip.setDate(date);
		ip.setValue(value);
		curve.getPoints().add(ip);
	}

	/**
	 * Sets up a cooldown pricing model including an index
	 */
	public void setupCooldown(final double value) {

		scenarioModelBuilder.getCostModelBuilder().createCooldownPrice(Double.toString(value), false, scenarioModelBuilder.getPortModelBuilder().getPortModel().getPorts());
	}

	public ScenarioModelBuilder getScenarioModelBuilder() {
		return scenarioModelBuilder;
	}
}
