/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.lngscheduler.emf.extras;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.Cargo;
import scenario.cargo.CargoType;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.MarketPricePurchaseContract;
import scenario.contract.NetbackPurchaseContract;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.contract.TotalVolumeLimit;
import scenario.fleet.CharterOut;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;
import scenario.market.Market;
import scenario.market.StepwisePrice;
import scenario.market.StepwisePriceCurve;
import scenario.optimiser.OptimisationSettings;
import scenario.port.Canal;
import scenario.port.DistanceLine;
import scenario.port.Port;
import scenario.port.VesselClassCost;

import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.ICharterOut;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

/**
 * Wrapper for an EMF LNG Scheduling {@link scenario.Scenario}, providing
 * utility methods to convert it into an optimization job. Typical usage is to
 * construct an LNGScenarioTransformer with a given Scenario, and then call the
 * {@link createOptimisationData} method
 * 
 * @author hinton
 * 
 */
public class LNGScenarioTransformer {
	private Scenario scenario;
	private TimeZone timezone;
	private Date earliestTime;
	private Date latestTime;

	/**
	 * Create a transformer for the given scenario; the class holds a reference,
	 * so changes made to the scenario after construction will be reflected in
	 * calls to the various helper methods.
	 * 
	 * @param scenario
	 */
	public LNGScenarioTransformer(Scenario scenario) {
		init(scenario);
	}

	/*
	 * Create a transformer by loading a scenario from a URI
	 */
	public LNGScenarioTransformer(URI uri) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI,
				ScenarioPackage.eINSTANCE);

		Resource resource = resourceSet.getResource(uri, true);
		for (EObject e : resource.getContents()) {
			if (e instanceof Scenario) {
				init((Scenario) e);
			}
		}
	}

	protected void init(Scenario scenario) {
		this.scenario = scenario;
		this.timezone = TimeZone.getTimeZone("UTC");
	}

	/**
	 * Instantiates and returns an {@link IOptimisationData} isomorphic to the
	 * contained scenario.
	 * 
	 * @return
	 * @throws IncompleteScenarioException
	 */
	public IOptimisationData<ISequenceElement> createOptimisationData(
			final ModelEntityMap entities) throws IncompleteScenarioException {
		/*
		 * Set reference for hour 0
		 */
		findEarliestAndLatestTimes();

		/**
		 * First, create all the market curves (should these come through the
		 * builder?)
		 */

		final Association<Market, ICurve> marketAssociation = new Association<Market, ICurve>();

		for (final Market market : scenario.getMarketModel().getMarkets()) {
			final StepwiseIntegerCurve curve = createCurveForMarket(market, 1.0f);

			marketAssociation.add(market, curve);
		}

		SchedulerBuilder builder = new SchedulerBuilder();
		/**
		 * Bidirectionally maps EMF {@link Port} Models to {@link IPort}s in the
		 * builder.
		 */
		Association<Port, IPort> portAssociation = new Association<Port, IPort>();
		/**
		 * Lists all the {@link IPort}s created for this scenario
		 */
		List<IPort> allPorts = new ArrayList<IPort>();
		/**
		 * A reverse-lookup for the {@link allPorts} array.
		 */
		Map<IPort, Integer> portIndices = new HashMap<IPort, Integer>();
		/*
		 * Construct ports for each port in the scenario port model, and keep
		 * them in a two-way lookup table (the two-way lookup is needed to do
		 * things like setting distances later).
		 */
		for (Port ePort : scenario.getPortModel().getPorts()) {
			IPort port = builder.createPort(ePort.getName());
			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);

			entities.addModelObject(ePort, port);
		}

		Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> vesselAssociations = buildFleet(
				builder, portAssociation, entities);

		buildDistances(builder, portAssociation, allPorts, portIndices,
				vesselAssociations.getFirst());

		/**
		 * Next create the contract logic models
		 */

		final Association<PurchaseContract, ILoadPriceCalculator> purchaseContractAssociation = new Association<PurchaseContract, ILoadPriceCalculator>();

		for (final PurchaseContract c : scenario.getContractModel()
				.getPurchaseContracts()) {
			final ILoadPriceCalculator calculator;
			if (c instanceof FixedPricePurchaseContract) {
				calculator = builder.createFixedPriceContract(Calculator
						.scaleToInt(((FixedPricePurchaseContract) c)
								.getUnitPrice()));
			} else if (c instanceof MarketPricePurchaseContract) {
				calculator = builder
						.createMarketPriceContract(marketAssociation
								.lookup(((MarketPricePurchaseContract) c)
										.getMarket()));
			} else if (c instanceof ProfitSharingPurchaseContract) {
				final ProfitSharingPurchaseContract p = (ProfitSharingPurchaseContract) c;
				calculator = builder.createProfitSharingContract(
						marketAssociation.lookup(p.getMarket()),
						marketAssociation.lookup(p.getReferenceMarket()),
						Calculator.scaleToInt(p.getAlpha()),
						Calculator.scaleToInt(p.getBeta()),
						Calculator.scaleToInt(p.getGamma()));
			} else if (c instanceof NetbackPurchaseContract) {
				calculator = builder.createNetbackContract(Calculator
						.scaleToInt(((NetbackPurchaseContract) c)
								.getBuyersMargin()));
			} else {
				throw new RuntimeException("Unknown class of contract : "
						+ c.eClass().getName());
			}

			purchaseContractAssociation.add(c, calculator);
		}

		buildCargoes(builder, portAssociation, marketAssociation,
				vesselAssociations.getSecond(), entities,
				purchaseContractAssociation);

		buildCharterOuts(builder, portAssociation,
				vesselAssociations.getFirst(), vesselAssociations.getSecond(),
				entities);

		buildTotalVolumeLimits(builder, portAssociation);

		return builder.getOptimisationData();
	}

	private StepwiseIntegerCurve createCurveForMarket(final Market market, float scale) {
		final StepwisePriceCurve curveModel = market.getPriceCurve();
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(Calculator.scaleToInt(scale * curveModel
				.getDefaultValue()));
		boolean gotOneEarlyDate = false;
		for (final StepwisePrice price : curveModel.getPrices()) {
			final int hours = convertTime(price.getDate());
			if (hours < 0) {
				if (gotOneEarlyDate) continue;
				gotOneEarlyDate = true;
			}
			curve.setValueAfter(hours,
					Calculator.scaleToInt(scale * price.getPriceFromDate()));
		}
		return curve;
	}

	/**
	 * Set up the total volume limits, if there are any
	 * 
	 * @param builder
	 * @param portAssociation
	 */
	private void buildTotalVolumeLimits(final SchedulerBuilder builder,
			final Association<Port, IPort> portAssociation) {

		final int latestTimeAsInt = convertTime(latestTime);

		if (scenario.getContractModel() != null) {
			for (final TotalVolumeLimit tvl : scenario.getContractModel()
					.getVolumeConstraints()) {
				final Set<IPort> ports = new HashSet<IPort>();
				for (final Port ePort : tvl.getPorts()) {
					ports.add(portAssociation.lookup(ePort));
				}

				int startTime = convertTime(tvl.getStartDate());
				final int duration = tvl.getDuration();
				while (true) {
					final ITimeWindow window = builder.createTimeWindow(
							startTime, startTime + duration);
					builder.addTotalVolumeConstraint(ports, true, true,
							tvl.getMaximumVolume(), window);

					startTime += (duration + 1);
					if (startTime > latestTimeAsInt
							|| tvl.isRepeating() == false)
						break;
				}
			}
		}
	}

	/**
	 * Find the earliest date entry in the model, for relative hours
	 * calculations. Uses a slightly naff bit of reflection to find all getters
	 * which return a date.
	 */
	private void findEarliestAndLatestTimes() {
		// search only within the cargo model, because we only care about the cargoes.
		final Pair<Date, Date> mm = EMFUtils.findMinMaxDateAttributes(scenario.getCargoModel());

		earliestTime = mm.getFirst();
		latestTime = mm.getSecond();
	}

	private void buildCharterOuts(SchedulerBuilder builder,
			Association<Port, IPort> portAssociation,
			Association<VesselClass, IVesselClass> classes,
			Association<Vessel, IVessel> vessels, ModelEntityMap entities) {
		for (CharterOut charterOut : scenario.getFleetModel().getCharterOuts()) {
			final ITimeWindow window = builder.createTimeWindow(
					convertTime(charterOut.getStartDate()),
					convertTime(charterOut.getEndDate()));
			final IPort port = portAssociation.lookup(charterOut.getPort());

			final ICharterOut builderCharterOut = builder.createCharterOut(
					window, port, charterOut.getDuration() * 24); // EMF
																	// measures
																	// in days
																	// here.

			entities.addModelObject(charterOut, builderCharterOut);

			for (final Vessel v : charterOut.getVessels()) {
				builder.addCharterOutVessel(builderCharterOut,
						vessels.lookup(v));
			}

			for (final VesselClass vc : charterOut.getVesselClasses()) {
				builder.addCharterOutVesselClass(builderCharterOut,
						classes.lookup(vc));
			}
		}
	}

	/**
	 * Extract the cargoes from the scenario and add them to the given builder.
	 * 
	 * @param builder
	 *            current builder. should already have ports/distances/vessels
	 *            built
	 * @param marketAssociation
	 * @param entities
	 * @param purchaseContractAssociation
	 */
	private void buildCargoes(
			final SchedulerBuilder builder,
			final Association<Port, IPort> ports,
			final Association<Market, ICurve> marketAssociation,
			final Association<Vessel, IVessel> vesselAssociation,
			final ModelEntityMap entities,
			final Association<PurchaseContract, ILoadPriceCalculator> purchaseContractAssociation) {
		for (final Cargo eCargo : scenario.getCargoModel().getCargoes()) {
			// ignore all non-fleet cargoes, as far as optimisation goes.

			if (eCargo.getCargoType().equals(CargoType.FLEET) == false) continue;
			// not escargot.
			final LoadSlot loadSlot = eCargo.getLoadSlot();
			final Slot dischargeSlot = eCargo.getDischargeSlot();
			final int loadStart = convertTime(earliestTime,
					loadSlot.getWindowStart());
			final int dischargeStart = convertTime(earliestTime,
					dischargeSlot.getWindowStart());

			// TODO check units again
			final ITimeWindow loadWindow = builder.createTimeWindow(loadStart,
					loadStart + loadSlot.getWindowDuration());
			final ITimeWindow dischargeWindow = builder.createTimeWindow(
					dischargeStart,
					dischargeStart + dischargeSlot.getWindowDuration());

			final Market dischargeMarket = ((SalesContract) dischargeSlot
					.getSlotOrPortContract()).getMarket();

			final PurchaseContract purchaseContract = (PurchaseContract) (loadSlot
					.getSlotOrPortContract());

			final ILoadSlot load = builder.createLoadSlot(loadSlot.getId(),
					ports.lookup(loadSlot.getPort()), loadWindow,
					Calculator.scale(loadSlot.getMinQuantity()),
					Calculator.scale(loadSlot.getMaxQuantity()),
					purchaseContractAssociation.lookup(purchaseContract),
					(int) Calculator.scale(loadSlot.getCargoCVvalue()),
					dischargeSlot.getSlotDuration());

			final ICurve dischargeCurve;
			// create scaled discharge market, incorporating regas losses
			{
				final float regasEfficiency = ((SalesContract) dischargeSlot
						.getSlotOrPortContract()).getRegasEfficiency();
				if (regasEfficiency != 1.0f) {
					dischargeCurve = createCurveForMarket(dischargeMarket, regasEfficiency);
				} else {
					dischargeCurve = marketAssociation.lookup(dischargeMarket);
				}
			}

			final IDischargeSlot discharge = builder.createDischargeSlot(
					dischargeSlot.getId(),
					ports.lookup(dischargeSlot.getPort()), dischargeWindow,
					Calculator.scale(dischargeSlot.getMinQuantity()),
					Calculator.scale(dischargeSlot.getMaxQuantity()),
					dischargeCurve,
					dischargeSlot.getSlotDuration());

			entities.addModelObject(loadSlot, load);
			entities.addModelObject(dischargeSlot, discharge);

			final ICargo cargo = builder.createCargo(eCargo.getId(), load,
					discharge);

			if (!eCargo.getAllowedVessels().isEmpty()) {
				HashSet<IVessel> vesselsForCargo = new HashSet<IVessel>();
				for (final Vessel v : eCargo.getAllowedVessels()) {
					vesselsForCargo.add(vesselAssociation.lookup(v));
				}
				builder.setCargoVesselRestriction(cargo, vesselsForCargo);
			}
		}
	}

	/**
	 * Convert a date into relative hours; returns the number of hours between
	 * windowStart and earliest.
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	private int convertTime(Date earliest, Date windowStart) {
		// I am using two calendars, because the java date objects are all
		// deprecated; however, timezones should not be a problem because
		// every Date in the EMF representation is in UTC.
		Calendar a = Calendar.getInstance(timezone);
		a.setTime(earliest);
		Calendar b = Calendar.getInstance(timezone);
		b.setTime(windowStart);
		long difference = b.getTimeInMillis() - a.getTimeInMillis();
		return (int) (difference / Timer.ONE_HOUR);
	}

	/**
	 * Create the distance matrix for the given builder.
	 * 
	 * @param builder
	 *            the builder we are working with
	 * @param portAssociation
	 *            an association between ports in the EMF model and IPorts in
	 *            the builder
	 * @param allPorts
	 *            the list of all IPorts constructed so far
	 * @param portIndices
	 *            a reverse-lookup for the ports in allPorts
	 * @param vesselAssociation
	 * @throws IncompleteScenarioException
	 */
	private void buildDistances(SchedulerBuilder builder,
			Association<Port, IPort> portAssociation, List<IPort> allPorts,
			Map<IPort, Integer> portIndices,
			Association<VesselClass, IVesselClass> vesselAssociation)
			throws IncompleteScenarioException {

		/*
		 * Now fill out the distances from the distance model. Firstly we need
		 * to create the default distance matrix.
		 */
		for (DistanceLine dl : scenario.getDistanceModel().getDistances()) {
			IPort from, to;
			from = portAssociation.lookup(dl.getFromPort());
			to = portAssociation.lookup(dl.getToPort());

			final int distance = dl.getDistance();

			builder.setPortToPortDistance(from, to,
					IMultiMatrixProvider.Default_Key, distance);

		}

		/*
		 * Next we need to handle the secondary distance matrices for each
		 * canal.
		 */
		if (scenario.getCanalModel() != null) {
			for (Canal canal : scenario.getCanalModel().getCanals()) {
				final String name = canal.getName();
				builder.setDefaultRouteCost(name,
						Calculator.scale(canal.getDefaultCost()));

				for (final DistanceLine dl : canal.getDistanceModel()
						.getDistances()) {
					builder.setPortToPortDistance(
							portAssociation.lookup(dl.getFromPort()),
							portAssociation.lookup(dl.getToPort()), name,
							dl.getDistance());
				}

				// set vessel class costs
				for (final VesselClassCost classCost : canal.getClassCosts()) {
					final IVesselClass vc = vesselAssociation.lookup(classCost
							.getVesselClass());
					builder.setVesselClassRouteCost(name, vc,
							VesselState.Laden,
							Calculator.scale(classCost.getLadenCost()));
					builder.setVesselClassRouteCost(name, vc,
							VesselState.Ballast,
							Calculator.scale(classCost.getUnladenCost()));

					builder.setVesselClassRouteTimeAndFuel(name, vc,
							classCost.getTransitTime(),
							Calculator.scale(classCost.getTransitFuel() / 24));
				}
			}
		}
	}

	/**
	 * Construct the fleet model for the scenario
	 * 
	 * @param builder
	 *            a builder which has had its ports and distances instantiated
	 * @param portAssociation
	 *            the Port <-> IPort association to connect EMF Ports with
	 *            builder IPorts
	 * @param entities
	 * @return
	 */
	private Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> buildFleet(
			SchedulerBuilder builder, Association<Port, IPort> portAssociation,
			ModelEntityMap entities) {

		/*
		 * Build the fleet model - first we must create the vessel classes from
		 * the model
		 */
		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<VesselClass, IVesselClass>();
		final Association<Vessel, IVessel> vesselAssociation = new Association<Vessel, IVessel>();
		// TODO: Check that we are mutliplying/dividing correctly to maintain
		// precision

		for (VesselClass eVc : scenario.getFleetModel().getVesselClasses()) {
			IVesselClass vc = builder
					.createVesselClass(
							eVc.getName(),
							Calculator.scaleToInt(eVc.getMinSpeed()),
							Calculator.scaleToInt(eVc.getMaxSpeed()),
							Calculator.scale(eVc.getCapacity()
									* eVc.getFillCapacity()), // TODO is
																// capacity mean
																// to be scaled?
							Calculator.scaleToInt(eVc.getMinHeelVolume()),
							Calculator.scaleToInt(eVc.getBaseFuelUnitPrice()),
							Calculator.scaleToInt(eVc
									.getBaseFuelEquivalenceFactor()),
							// This should be divide?
							Calculator.scaleToInt(eVc.getDailyCharterPrice() / 24.0));
			vesselClassAssociation.add(eVc, vc);

			/*
			 * Set state-specific attributes
			 */
			buildVesselStateAttributes(
					builder,
					vc,
					com.mmxlabs.scheduler.optimiser.components.VesselState.Laden,
					eVc.getLadenAttributes());
			buildVesselStateAttributes(
					builder,
					vc,
					com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast,
					eVc.getBallastAttributes());

			/*
			 * set up inaccessible ports by applying resource allocation
			 * constraints
			 */
			Set<IPort> inaccessiblePorts = new HashSet<IPort>();
			for (Port ePort : eVc.getInaccessiblePorts()) {
				final IPort port = portAssociation.lookup(ePort);
				inaccessiblePorts.add(port);
			}

			if (inaccessiblePorts.isEmpty() == false) {
				builder.setVesselClassInaccessiblePorts(vc, inaccessiblePorts);
			}

			entities.addModelObject(eVc, vc);
		}

		/*
		 * Now create each vessel
		 */
		for (Vessel eV : scenario.getFleetModel().getFleet()) {
			IStartEndRequirement startRequirement = createRequirement(builder,
					portAssociation, eV.getStartRequirement());
			IStartEndRequirement endRequirement = createRequirement(builder,
					portAssociation, eV.getEndRequirement());
			final IVessel vessel = builder.createVessel(eV.getName(),
					vesselClassAssociation.lookup(eV.getClass_()),
					eV.isTimeChartered() ? VesselInstanceType.TIME_CHARTER
							: VesselInstanceType.FLEET, startRequirement,
					endRequirement);
			vesselAssociation.add(eV, vessel);

			entities.addModelObject(eV, vessel);
		}

		/*
		 * Create spot charter vessels with no start/end requirements
		 */
		for (VesselClass eVc : scenario.getFleetModel().getVesselClasses()) {
			if (eVc.getSpotCharterCount() > 0)
				builder.createSpotVessels("SPOT-" + eVc.getName(),
						vesselClassAssociation.lookup(eVc),
						eVc.getSpotCharterCount());
		}

		return new Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>>(
				vesselClassAssociation, vesselAssociation);
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for
	 * internal use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param pat
	 * @return
	 */
	private IStartEndRequirement createRequirement(SchedulerBuilder builder,
			Association<Port, IPort> portAssociation, PortAndTime pat) {
		if (pat == null) {
			return builder.createStartEndRequirement();
		}

		if (pat.isSetPort() && pat.isSetStartTime()) {
			final int startHours = convertTime(pat.getStartTime());
			final int endHours = pat.isSetEndTime() ? convertTime(pat
					.getEndTime()) : startHours + 1;
			final ITimeWindow window = builder.createTimeWindow(startHours,
					endHours);
			return builder.createStartEndRequirement(
					portAssociation.lookup(pat.getPort()), window);
		} else if (pat.isSetPort()) {
			return builder.createStartEndRequirement(portAssociation.lookup(pat
					.getPort()));
		} else if (pat.isSetStartTime()) {
			final int startHours = convertTime(pat.getStartTime());
			final int endHours = pat.isSetEndTime() ? convertTime(pat
					.getEndTime()) : startHours + 1;
			final ITimeWindow window = builder.createTimeWindow(startHours,
					endHours);
			return builder.createStartEndRequirement(window);
		} else {
			return builder.createStartEndRequirement();
		}
	}

	private int convertTime(Date startTime) {
		return convertTime(earliestTime, startTime);
	}

	/**
	 * Tell the builder to set up the given vessel state from the EMF fleet
	 * model
	 * 
	 * @param builder
	 *            the builder which is currently in use
	 * @param vc
	 *            the {@link IVesselClass} which the builder has constructed
	 *            whose attributes we are setting
	 * @param laden
	 *            the
	 *            {@link com.mmxlabs.scheduler.optimiser.components.VesselState}
	 *            we are setting attributes for
	 * @param ladenAttributes
	 *            the {@link VesselStateAttributes} from the EMF model
	 */
	private void buildVesselStateAttributes(SchedulerBuilder builder,
			IVesselClass vc,
			com.mmxlabs.scheduler.optimiser.components.VesselState state,
			VesselStateAttributes attrs) {

		// create consumption rate calculator for the curve
		TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();

		for (FuelConsumptionLine line : attrs.getFuelConsumptionCurve()) {
			keypoints.put(Calculator.scaleToInt(line.getSpeed()),
					Calculator.scale(line.getConsumption()) / 24);
		}

		InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(
				keypoints);

		LookupTableConsumptionRateCalculator cc = new LookupTableConsumptionRateCalculator(vc.getMinSpeed(), vc.getMaxSpeed(), consumptionCalculator);
		
		builder.setVesselClassStateParamaters(vc, state,
				Calculator.scaleToInt(attrs.getNboRate()) / 24,
				Calculator.scaleToInt(attrs.getIdleNBORate()) / 24,
				Calculator.scaleToInt(attrs.getIdleConsumptionRate()) / 24,
				cc);
	}

	/**
	 * Utility method for getting the current optimisation settings from this
	 * scenario. TODO maybe put this in another file/model somewhere else.
	 * 
	 * @return
	 */
	public OptimisationSettings getOptimisationSettings() {
		if (scenario.getOptimisation() != null) {
			return scenario.getOptimisation().getCurrentSettings();
		} else {
			return null;
		}
	}
}
