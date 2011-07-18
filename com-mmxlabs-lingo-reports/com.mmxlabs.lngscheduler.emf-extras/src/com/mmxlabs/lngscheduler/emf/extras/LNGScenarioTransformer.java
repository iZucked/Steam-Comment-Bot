/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import scenario.contract.IndexPricePurchaseContract;
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
import scenario.fleet.VesselEvent;
import scenario.fleet.VesselStateAttributes;
import scenario.market.Index;
import scenario.market.StepwisePrice;
import scenario.market.StepwisePriceCurve;
import scenario.optimiser.Discount;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.Objective;
import scenario.optimiser.OptimisationSettings;
import scenario.port.Canal;
import scenario.port.DistanceLine;
import scenario.port.Port;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.SpotVessel;

import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.InterpolatingDiscountCurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

/**
 * Wrapper for an EMF LNG Scheduling {@link scenario.Scenario}, providing
 * utility methods to coSnvert it into an optimization job. Typical usage is to
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

		final Association<Index, ICurve> indexAssociation = new Association<Index, ICurve>();

		for (final Index index : scenario.getMarketModel().getIndices()) {
			final StepwiseIntegerCurve curve = createCurveForIndex(index, 1.0f);

			indexAssociation.add(index, curve);
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
			} else if (c instanceof IndexPricePurchaseContract) {
				calculator = builder.createMarketPriceContract(indexAssociation
						.lookup(((IndexPricePurchaseContract) c).getIndex()));
			} else if (c instanceof ProfitSharingPurchaseContract) {
				final ProfitSharingPurchaseContract p = (ProfitSharingPurchaseContract) c;
				calculator = builder.createProfitSharingContract(
						indexAssociation.lookup(p.getIndex()),
						indexAssociation.lookup(p.getReferenceIndex()),
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

		buildCargoes(builder, portAssociation, indexAssociation,
				vesselAssociations.getSecond(), entities,
				purchaseContractAssociation);

		buildVesselEvents(builder, portAssociation,
				vesselAssociations.getFirst(), vesselAssociations.getSecond(),
				entities);

		buildTotalVolumeLimits(builder, portAssociation);

		buildDiscountCurves(builder);

		freezeStartSequences(builder, entities);

		return builder.getOptimisationData();
	}

	/**
	 * Create and associate discount curves
	 * 
	 * This ought to be in {@link OptimisationTransformer} but that doesn't have
	 * access to set up DCPs.
	 * 
	 * @param builder
	 */
	private void buildDiscountCurves(final SchedulerBuilder builder) {
		// set up DCP

		final DiscountCurve defaultCurve = scenario.getOptimisation()
				.getCurrentSettings().isSetDefaultDiscountCurve() ? scenario
				.getOptimisation().getCurrentSettings()
				.getDefaultDiscountCurve() : null;
			
		final ICurve realDefaultCurve;
		if (defaultCurve == null) {
			realDefaultCurve = new ConstantValueCurve(1);
		} else {
			realDefaultCurve = buildDiscountCurve(defaultCurve);
		}

		for (final Objective objective : scenario.getOptimisation()
				.getCurrentSettings().getObjectives()) {
			if (objective.isSetDiscountCurve()) {
				builder.setFitnessComponentDiscountCurve(objective.getName(),
						buildDiscountCurve(objective.getDiscountCurve()));
			} else {
				builder.setFitnessComponentDiscountCurve(objective.getName(),
						realDefaultCurve);
			}
		}
	}

	/**
	 * @param defaultCurve
	 * @return
	 */
	private ICurve buildDiscountCurve(final DiscountCurve curve) {
		final InterpolatingDiscountCurve realCurve = new InterpolatingDiscountCurve();

		final int baseTime = curve.isSetStartDate() ? convertTime(curve
				.getStartDate()) : 0;
		if (baseTime > 0) {
			realCurve.setValueAtPoint(0, 1);
			realCurve.setValueAtPoint(baseTime - 1, 1);
		}

		for (final Discount d : curve.getDiscounts()) {
			realCurve.setValueAtPoint(baseTime + d.getTime(),
					d.getDiscountFactor());
		}
		return realCurve;
	}

	private StepwiseIntegerCurve createCurveForIndex(final Index index,
			float scale) {
		final StepwisePriceCurve curveModel = index.getPriceCurve();
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(Calculator.scaleToInt(scale
				* curveModel.getDefaultValue()));
		boolean gotOneEarlyDate = false;
		for (final StepwisePrice price : curveModel.getPrices()) {
			final int hours = convertTime(price.getDate());
			if (hours < 0) {
				if (gotOneEarlyDate)
					continue;
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
		Pair<Date, Date> a = EMFUtils.findEarliestAndLatestEvents(scenario);
		earliestTime = a.getFirst();
		latestTime = a.getSecond();
	}

	private void buildVesselEvents(SchedulerBuilder builder,
			Association<Port, IPort> portAssociation,
			Association<VesselClass, IVesselClass> classes,
			Association<Vessel, IVessel> vessels, ModelEntityMap entities) {
		
		final Date latestDate = scenario.getOptimisation().getCurrentSettings().isSetIgnoreElementsAfter() ?
				scenario.getOptimisation().getCurrentSettings().getIgnoreElementsAfter() : latestTime;
		
		for (final VesselEvent event : scenario.getFleetModel()
				.getVesselEvents()) {
			
			if (event.getStartDate().after(latestDate))
				continue;
			
			final ITimeWindow window = builder.createTimeWindow(
					convertTime(event.getStartDate()),
					convertTime(event.getEndDate()));
			final IPort port = portAssociation.lookup(event.getStartPort());
			final int durationHours = event.getDuration() * 24;
			final IVesselEventPortSlot builderSlot;
			if (event instanceof CharterOut) {
				final CharterOut charterOut = (CharterOut) event;
				final IPort endPort = portAssociation.lookup(charterOut
						.getEndPort());
				builderSlot = builder.createCharterOutEvent(event.getId(),
						window, port, endPort, durationHours,
						charterOut.getMaxHeelOut()
								* (long) Calculator.ScaleFactor,
						Calculator.scaleToInt(charterOut.getHeelCVValue()));
			} else {
				builderSlot = builder.createDrydockEvent(event.getId(), window,
						port, durationHours);
			}

			for (final Vessel v : event.getVessels()) {
				builder.addVesselEventVessel(builderSlot, vessels.lookup(v));
			}

			for (final VesselClass vc : event.getVesselClasses()) {
				builder.addVesselEventVesselClass(builderSlot,
						classes.lookup(vc));
			}

			entities.addModelObject(event, builderSlot);
		}
	}

	/**
	 * Extract the cargoes from the scenario and add them to the given builder.
	 * 
	 * @param builder
	 *            current builder. should already have ports/distances/vessels
	 *            built
	 * @param indexAssociation
	 * @param entities
	 * @param purchaseContractAssociation
	 */
	private void buildCargoes(
			final SchedulerBuilder builder,
			final Association<Port, IPort> ports,
			final Association<Index, ICurve> indexAssociation,
			final Association<Vessel, IVessel> vesselAssociation,
			final ModelEntityMap entities,
			final Association<PurchaseContract, ILoadPriceCalculator> purchaseContractAssociation) {
		
		final Date latestDate = scenario.getOptimisation().getCurrentSettings().isSetIgnoreElementsAfter() ?
				scenario.getOptimisation().getCurrentSettings().getIgnoreElementsAfter() : latestTime;
		for (final Cargo eCargo : scenario.getCargoModel().getCargoes()) {
			// ignore all non-fleet cargoes, as far as optimisation goes.
			if (eCargo.getCargoType().equals(CargoType.FLEET) == false)
				continue;
			
			if (eCargo.getLoadSlot().getWindowStart().after(latestDate))
				continue;
			
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

			final ILoadPriceCalculator loadPriceCalculator;
			if (loadSlot.isSetFixedPrice()) {
				loadPriceCalculator = new ILoadPriceCalculator() {
					@Override
					public int calculateLoadUnitPrice(int loadTime,
							long loadVolume, int dischargeTime,
							int actualSalesPrice, int cvValue,
							VoyageDetails ladenLeg, VoyageDetails ballastLeg,
							IVesselClass vesselClass) {
						return Calculator.scaleToInt(loadSlot.getFixedPrice());
					}
				};
			} else {
				final PurchaseContract purchaseContract = (PurchaseContract) (loadSlot
						.getSlotOrPortContract(scenario));
				loadPriceCalculator = purchaseContractAssociation
						.lookup(purchaseContract);
			}

			final ILoadSlot load = builder.createLoadSlot(loadSlot.getId(),
					ports.lookup(loadSlot.getPort()), loadWindow,
					Calculator.scale(loadSlot.getMinQuantity()),
					Calculator.scale(loadSlot.getMaxQuantity()),
					loadPriceCalculator,
					(int) Calculator.scale(loadSlot.getCargoOrPortCVValue()),
					dischargeSlot.getSlotDuration());

			final ICurve dischargeCurve;

			if (dischargeSlot.isSetFixedPrice()) {
				dischargeCurve = new ConstantValueCurve(
						Calculator.scaleToInt(dischargeSlot.getFixedPrice()));
			} else {
				final Index dischargeIndex = ((SalesContract) dischargeSlot
						.getSlotOrPortContract(scenario)).getIndex();

				final float regasEfficiency = (dischargeSlot.getPort())
						.getRegasEfficiency();
				if (regasEfficiency != 1.0f) {
					dischargeCurve = createCurveForIndex(dischargeIndex,
							regasEfficiency);
				} else {
					dischargeCurve = indexAssociation.lookup(dischargeIndex);
				}
			}

			final IDischargeSlot discharge = builder.createDischargeSlot(
					dischargeSlot.getId(),
					ports.lookup(dischargeSlot.getPort()), dischargeWindow,
					Calculator.scale(dischargeSlot.getMinQuantity()),
					Calculator.scale(dischargeSlot.getMaxQuantity()),
					dischargeCurve, dischargeSlot.getSlotDuration());

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
				for (final VesselClass evc : scenario.getFleetModel().getVesselClasses()) {
					for (final scenario.fleet.VesselClassCost classCost : evc.getCanalCosts()) {
						if (classCost.getCanal() != canal) continue;
						final IVesselClass vc = vesselAssociation.lookup(evc);
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
							Calculator.scaleToInt(eVc.getBaseFuel()
									.getUnitPrice()),
							Calculator.scaleToInt(eVc.getBaseFuel()
									.getEquivalenceFactor()),
							// This should be divide?
							Calculator.scaleToInt(eVc.getDailyCharterInPrice() / 24.0));
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

			final int dailyCharterPrice = eV.isSetDailyCharterOutPrice() ? eV
					.getDailyCharterOutPrice() : (eV.getClass_()
					.isSetDailyCharterOutPrice() ? eV.getClass_()
					.getDailyCharterOutPrice() : eV.getClass_()
					.getDailyCharterInPrice());

			final IVessel vessel = builder.createVessel(eV.getName(),
					vesselClassAssociation.lookup(eV.getClass_()), Calculator
							.scale(dailyCharterPrice) / 24,
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
			if (eVc.getSpotCharterCount() > 0) {
				final List<IVessel> spots = builder.createSpotVessels("SPOT-"
						+ eVc.getName(), vesselClassAssociation.lookup(eVc),
						eVc.getSpotCharterCount());
				// TODO this is not necessarily ideal; if there is an initial
				// solution set we associate all the spot vessels with ones in
				// that solution.
				int vesselIndex = 0;
				if (scenario.getOptimisation() != null
						&& scenario.getOptimisation().getCurrentSettings() != null) {
					final Schedule initialSchedule = scenario.getOptimisation()
							.getCurrentSettings().getInitialSchedule();
					if (initialSchedule != null) {
						for (final AllocatedVessel allocatedVessel : initialSchedule
								.getFleet()) {
							if (allocatedVessel instanceof SpotVessel
									&& ((SpotVessel) allocatedVessel)
											.getVesselClass() == eVc) {
								// map it to one of the ones we have just made
								assert vesselIndex < spots.size() : "Initial schedule should not have more spot vessels than fleet suggests";
								entities.addModelObject(allocatedVessel,
										spots.get(vesselIndex));
								vesselIndex++;
							}
						}
					}
				}
			}
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

		LookupTableConsumptionRateCalculator cc = new LookupTableConsumptionRateCalculator(
				vc.getMinSpeed(), vc.getMaxSpeed(), consumptionCalculator);

		builder.setVesselClassStateParamaters(vc, state,
				Calculator.scaleToInt(attrs.getNboRate()) / 24,
				Calculator.scaleToInt(attrs.getIdleNBORate()) / 24,
				Calculator.scaleToInt(attrs.getIdleConsumptionRate()) / 24, cc);
	}

	public void freezeStartSequences(final ISchedulerBuilder builder,
			final ModelEntityMap entities) {
		if (scenario.getOptimisation() != null
				&& scenario.getOptimisation().getCurrentSettings() != null
				&& scenario.getOptimisation().getCurrentSettings()
						.getInitialSchedule() != null) {
			final int freezeHours = 24 * scenario.getOptimisation()
					.getCurrentSettings().getFreezeDaysFromStart();
			if (freezeHours <= 0)
				return;
			final Schedule initialSchedule = scenario.getOptimisation()
					.getCurrentSettings().getInitialSchedule();
			// set up constraints on elements of initial schedule

			// TODO at the moment OptimisationTransformer also processes the
			// initial schedule, to create advice for the initial sequence
			// builder. This isn't ideal, but if we want the ISchedulerBuilder
			// to be the only way to get an IOptimisationData, and to prevent
			// post-hoc changes to same it has to be this way.

			final Date freezeDate = entities.getDateFromHours(freezeHours);
			System.err.println("Freezing sequencing decisions before " + freezeDate);
			for (final Sequence sequence : initialSchedule.getSequences()) {
				final AllocatedVessel av = sequence.getVessel();
				final IVessel builderVessel;
				if (av instanceof SpotVessel) {
					// reverse lookup hack - see above where I associated spot
					// vessels with
					// IVessels in buildFleet(.)
					builderVessel = entities.getOptimiserObject(av,
							IVessel.class);
				} else if (av instanceof FleetVessel) {
					builderVessel = entities.getOptimiserObject(
							((FleetVessel) av).getVessel(), IVessel.class);
				} else {
					throw new RuntimeException(
							av.getClass().getSimpleName()
									+ " is a kind of AllocatedVessel this code cannot translate");
				}

				IPortSlot previousSlot = null;
				/**
				 * Used to ensure that if we freeze a load we also freeze the next discharge.
				 */
				boolean waitingForDischargeSlot = false;
				for (final ScheduledEvent event : sequence.getEvents()) {
					if (event.getStartTime().before(freezeDate) || waitingForDischargeSlot) {
						final IPortSlot currentSlot;
						if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							// lock sequence and vessel
							currentSlot = entities.getOptimiserObject(
									slotVisit.getSlot(), IPortSlot.class);
							if (slotVisit.getSlot() instanceof LoadSlot) {
								waitingForDischargeSlot = true;
							} else {
								waitingForDischargeSlot = false;
							}
						} else if (event instanceof VesselEventVisit) {
							final VesselEventVisit vev = (VesselEventVisit) event;
							currentSlot = entities.getOptimiserObject(
									vev.getVesselEvent(), IPortSlot.class);
						} else {
							currentSlot = null;
						}

						if (currentSlot != null) {
							// bind slot to vessel - this overrides any previous
							// binding
							builder.constrainSlotToVessels(currentSlot,
									Collections.singleton(builderVessel));

							// this removes any binding to vessel class, because
							// the allowed vessels are the union of the above
							// and the classes.
							builder.constrainSlotToVesselClasses(currentSlot,
									null);
							if (previousSlot != null) {
								// bind sequencing as well - this forces
								// previousSlot to come before currentSlot.
								builder.constrainSlotAdjacency(previousSlot,
										currentSlot);
							}

							previousSlot = currentSlot;
						}
					} else {
						break;
					}
				}
			}
		}
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
