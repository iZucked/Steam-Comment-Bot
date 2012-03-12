/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.management.timer.Timer;

import org.eclipse.emf.ecore.EClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer.ModelClass;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;

/**
 * Wrapper for an EMF LNG Scheduling {@link MMXRootObject}, providing utility methods to convert it into an optimisation job. Typical usage is to construct an LNGScenarioTransformer with a given
 * Scenario, and then call the {@link createOptimisationData} method. It is only expected that an instance will be used once. I.e. a single call to {@link #createOptimisationData(ModelEntityMap)}
 * 
 * @author hinton
 * 
 */
public class LNGScenarioTransformer {
	private static final Logger log = LoggerFactory.getLogger(LNGScenarioTransformer.class);

	private MMXRootObject rootObject;
	private TimeZone timezone;
	private Date earliestTime;
	private Date latestTime;

	@Inject(optional = true)
	private Iterable<ContractTransformer> transformerExtensions;

	@Inject
	private ISchedulerBuilder builder;

	/**
	 * Contains the contract transformers for each known contract type, by the EClass of the contract they transform.
	 */
	final Map<EClass, IContractTransformer> contractTransformersByEClass = new LinkedHashMap<EClass, IContractTransformer>();

	/**
	 * A set of all contract transformers being used; these should be mapped to in {@link #contractTransformersByEClass}
	 */
	final Set<IContractTransformer> contractTransformers = new LinkedHashSet<IContractTransformer>();

	/**
	 * A set of all transformer extensions being used (should contain {@link #contractTransformers})
	 */
	final Set<ITransformerExtension> allTransformerExtensions = new LinkedHashSet<ITransformerExtension>();

	/**
	 * Create a transformer for the given scenario; the class holds a reference, so changes made to the scenario after construction will be reflected in calls to the various helper methods.
	 * 
	 * @param scenario
	 */
	public LNGScenarioTransformer(final MMXRootObject rootObject) {

		init(rootObject);
	}

	protected void init(final MMXRootObject rootObject) {

		this.rootObject = rootObject;
		this.timezone = TimeZone.getTimeZone("UTC");
	}

	/**
	 * Get any {@link ITransformerExtension} and {@link IContractTransformer}s from the platform's registry.
	 */
	public boolean addPlatformTransformerExtensions() {

		if (transformerExtensions == null) {
			return false;
		}

		final Map<String, EClass> contracts = new HashMap<String, EClass>();

		CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
		for (final Contract c : commercialModel.getSalesContracts()) {
			contracts.put(c.eClass().getInstanceClass().getCanonicalName(), c.eClass());
		}

		for (final Contract c : commercialModel.getPurchaseContracts()) {
			contracts.put(c.eClass().getInstanceClass().getCanonicalName(), c.eClass());
		}

		for (final ContractTransformer t : transformerExtensions) {

			final ModelClass[] modelClass = t.getModelClass();

			final HashSet<String> modelClasses = new HashSet<String>();
			for (final ModelClass mc : modelClass) {
				modelClasses.add(mc.getTransformer());
			}
			modelClasses.retainAll(contracts.keySet());

			final ITransformerExtension transformer = t.getTransformer();
			addTransformerExtension(transformer);

			if (transformer instanceof IContractTransformer) {
				final List<EClass> classes = new LinkedList<EClass>();
				for (final String mc : modelClasses) {
					classes.add(contracts.get(mc));
				}
				addContractTransformer((IContractTransformer) transformer, classes);
			}
		}

		return true;
	}

	public void addTransformerExtension(final ITransformerExtension extension) {
		log.debug(extension.getClass().getCanonicalName() + " added to transformer extensions");
		allTransformerExtensions.add(extension);
	}

	public void addContractTransformer(final IContractTransformer transformer, final Collection<EClass> forContracts) {
		contractTransformers.add(transformer);
		for (final EClass ec : forContracts) {
			log.debug(transformer.getClass().getCanonicalName() + " handling contracts with eClass " + ec.getName());
			contractTransformersByEClass.put(ec, transformer);
		}
	}

	/**
	 * Instantiates and returns an {@link IOptimisationData} isomorphic to the contained scenario.
	 * 
	 * @return
	 * @throws IncompleteScenarioException
	 */
	public IOptimisationData createOptimisationData(final ModelEntityMap entities) throws IncompleteScenarioException {
		/*
		 * Set reference for hour 0
		 */
		findEarliestAndLatestTimes();

		/**
		 * First, create all the market curves (should these come through the builder?)
		 */

		final Association<Index, ICurve> indexAssociation = new Association<Index, ICurve>();

		PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
		for (final Index index : pricingModel.getCommodityIndices()) {
			final StepwiseIntegerCurve curve = createCurveForIndex(index, 1.0f);

			entities.addModelObject(index, curve);

			indexAssociation.add(index, curve);
		}

		// set up the contract transformers
		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.startTransforming(rootObject, entities, builder);
		}

		CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);

		for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.eClass());
			final ILoadPriceCalculator2 calculator = transformer.transformPurchaseContract(c);
			entities.addModelObject(c, calculator);
		}

		for (final SalesContract c : commercialModel.getSalesContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.eClass());
			final IShippingPriceCalculator calculator = transformer.transformSalesContract(c);
			entities.addModelObject(c, calculator);
		}

		/**
		 * Bidirectionally maps EMF {@link Port} Models to {@link IPort}s in the builder.
		 */
		final Association<Port, IPort> portAssociation = new Association<Port, IPort>();
		/**
		 * Lists all the {@link IPort}s created for this scenario
		 */
		final List<IPort> allPorts = new ArrayList<IPort>();
		/**
		 * A reverse-lookup for the {@link allPorts} array.
		 */
		final Map<IPort, Integer> portIndices = new HashMap<IPort, Integer>();

		/*
		 * Construct ports for each port in the scenario port model, and keep them in a two-way lookup table (the two-way lookup is needed to do things like setting distances later).
		 */
		PortModel portModel = rootObject.getSubModel(PortModel.class);
		
		
		final Map<APort, IShippingPriceCalculator> cooldownCalculators = new HashMap<APort, IShippingPriceCalculator>();
		for (final CooldownPrice price : pricingModel.getCooldownPrices()) {
			final IShippingPriceCalculator cooldownCalculator = 
					new MarketPriceContract(indexAssociation.lookup(price.getIndex()));
			
			for (final APort port : SetUtils.getPorts(price.getPorts())) {
				cooldownCalculators.put(port, cooldownCalculator);
			}
		}
		
		for (final Port ePort : portModel.getPorts()) {
			final IPort port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort));
			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);
			entities.addModelObject(ePort, port);
		}

		final Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> vesselAssociations = buildFleet(builder, portAssociation, entities);

		buildDistances(builder, portAssociation, allPorts, portIndices, vesselAssociations.getFirst());

		buildCargoes(builder, portAssociation, indexAssociation, vesselAssociations.getSecond(), contractTransformers, entities, getOptimisationSettings().isRewire());

		buildVesselEvents(builder, portAssociation, vesselAssociations.getFirst(), vesselAssociations.getSecond(), entities);

//		buildDiscountCurves(builder);

//		freezeStartSequences(builder, entities);

		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.finishTransforming();
		}

		return builder.getOptimisationData();
	}

//	/**
//	 * Create and associate discount curves
//	 * 
//	 * This ought to be in {@link OptimisationTransformer} but that doesn't have access to set up DCPs.
//	 * 
//	 * @param builder
//	 */
//	private void buildDiscountCurves(final ISchedulerBuilder builder) {
//		// set up DCP
//
//		final DiscountCurve defaultCurve = scenario.getOptimisation().getCurrentSettings().isSetDefaultDiscountCurve() ? scenario.getOptimisation().getCurrentSettings().getDefaultDiscountCurve()
//				: null;
//
//		final ICurve realDefaultCurve;
//		if (defaultCurve == null) {
//			realDefaultCurve = new ConstantValueCurve(1);
//		} else {
//			realDefaultCurve = buildDiscountCurve(defaultCurve);
//		}
//
//		for (final Objective objective : scenario.getOptimisation().getCurrentSettings().getObjectives()) {
//			if (objective.isSetDiscountCurve()) {
//				builder.setFitnessComponentDiscountCurve(objective.getName(), buildDiscountCurve(objective.getDiscountCurve()));
//			} else {
//				builder.setFitnessComponentDiscountCurve(objective.getName(), realDefaultCurve);
//			}
//		}
//	}

//	/**
//	 * @param defaultCurve
//	 * @return
//	 */
//	private ICurve buildDiscountCurve(final DiscountCurve curve) {
//		final InterpolatingDiscountCurve realCurve = new InterpolatingDiscountCurve();
//
//		final int baseTime = curve.isSetStartDate() ? convertTime(curve.getStartDate()) : 0;
//		if (baseTime > 0) {
//			realCurve.setValueAtPoint(0, 1);
//			realCurve.setValueAtPoint(baseTime - 1, 1);
//		}
//
//		for (final Discount d : curve.getDiscounts()) {
//			realCurve.setValueAtPoint(baseTime + d.getTime(), d.getDiscountFactor());
//		}
//		return realCurve;
//	}

	private StepwiseIntegerCurve createCurveForIndex(final Index<Double> index, final float scale) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);
		
		boolean gotOneEarlyDate = false;
		for (final Date date : index.getDates()) {
			final double value = index.getValueAfter(date);
			final int hours = convertTime(date);
			if (hours < 0) {
				if (gotOneEarlyDate) continue;
				gotOneEarlyDate = true;
			}
			curve.setValueAfter(hours, Calculator.scaleToInt(scale * value));
		}
		
		return curve;
	}

	/**
	 * Find the earliest date entry in the model, for relative hours calculations. Uses a slightly naff bit of reflection to find all getters which return a date.
	 */
	private void findEarliestAndLatestTimes() {
		final Pair<Date, Date> a = EMFUtils.findMinMaxDateAttributes(rootObject);
		earliestTime = a.getFirst();
		latestTime = a.getSecond();
	}

	private void buildVesselEvents(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<VesselClass, IVesselClass> classes,
			final Association<Vessel, IVessel> vessels, final ModelEntityMap entities) {

		final Date latestDate = getOptimisationSettings().getRange().isSetOptimiseBefore() ? getOptimisationSettings().getRange().getOptimiseBefore() : latestTime;

		FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);

		for (final VesselEvent event : fleetModel.getVesselEvents()) {

			if (event.getStartAfter().after(latestDate)) {
				continue;
			}

			final ITimeWindow window = builder.createTimeWindow(convertTime(event.getStartAfter()), convertTime(event.getStartBy()));
			final IPort port = portAssociation.lookup(event.getPort());
			final int durationHours = event.getDurationInDays() * 24;
			final IVesselEventPortSlot builderSlot;
			if (event instanceof CharterOutEvent) {
				final CharterOutEvent charterOut = (CharterOutEvent) event;
				final IPort endPort = portAssociation.lookup(charterOut.isSetRelocateTo() ? charterOut.getRelocateTo() : charterOut.getPort());
				
				HeelOptions heelOptions = charterOut.getHeelOptions();
				final long maxHeel = heelOptions.isSetVolumeAvailable() ? (heelOptions.getVolumeAvailable() * Calculator.ScaleFactor) : Long.MAX_VALUE;
				builderSlot = builder.createCharterOutEvent(event.getName(), window, port, endPort, durationHours, maxHeel, Calculator.scaleToInt(heelOptions.getCvValue()),
						Calculator.scaleToInt(heelOptions.getPricePerMMBTU()));

			} else if (event instanceof DryDockEvent) {
				builderSlot = builder.createDrydockEvent(event.getName(), window, port, durationHours);
			} else if (event instanceof MaintenanceEvent) {
				builderSlot = builder.createMaintenanceEvent(event.getName(), window, port, durationHours);
			} else {
				throw new RuntimeException("Unknown event type: " + event.getClass().getName());
			}

			for (final AVessel v : SetUtils.getVessels(event.getAllowedVessels())) {
				builder.addVesselEventVessel(builderSlot, vessels.lookup((Vessel) v));
			}

			entities.addModelObject(event, builderSlot);
		}
	}

	/**
	 * Extract the cargoes from the scenario and add them to the given builder.
	 * 
	 * @param builder
	 *            current builder. should already have ports/distances/vessels built
	 * @param indexAssociation
	 * @param contractTransformers
	 * @param entities
	 * @param defaultRewiring
	 */
	private void buildCargoes(final ISchedulerBuilder builder, final Association<Port, IPort> ports, final Association<Index, ICurve> indexAssociation,
			final Association<Vessel, IVessel> vesselAssociation, final Collection<IContractTransformer> contractTransformers, final ModelEntityMap entities, final boolean defaultRewiring) {

		final Date latestDate = getOptimisationSettings().getRange().isSetOptimiseBefore() ? getOptimisationSettings().getRange().getOptimiseBefore() : latestTime;

		CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		for (final Cargo eCargo : cargoModel.getCargos()) {
			// ignore all non-fleet cargoes, as far as optimisation goes.
			if (eCargo.getCargoType().equals(CargoType.FLEET) == false) {
				continue;
			}

			if (eCargo.getLoadSlot().getWindowStart().after(latestDate)) {
				continue;
			}

			final LoadSlot loadSlot = eCargo.getLoadSlot();
			final int loadStart = convertTime(earliestTime, loadSlot.getWindowStart());//, loadSlot.getPort());

			final ITimeWindow loadWindow = builder.createTimeWindow(loadStart, loadStart + loadSlot.getSlotOrPortWindowSize());

			final ILoadPriceCalculator2 loadPriceCalculator;
			if (loadSlot.isSetFixedPrice()) {
				final int fixedPrice = Calculator.scaleToInt(loadSlot.getFixedPrice());

				loadPriceCalculator = new SimpleContract() {

					@Override
					public int calculateSimpleLoadUnitPrice(final int loadTime) {
						return fixedPrice;
					}
				};
			} else {
				final PurchaseContract purchaseContract = (PurchaseContract) (loadSlot.getContract());
				loadPriceCalculator = entities.getOptimiserObject(purchaseContract, ILoadPriceCalculator2.class);
			}

			final ILoadSlot load = builder.createLoadSlot(loadSlot.getName(), ports.lookup(loadSlot.getPort()), loadWindow, Calculator.scale(loadSlot.getSlotOrContractMinQuantity()),
					Calculator.scale(loadSlot.getSlotOrContractMaxQuantity()), loadPriceCalculator, Calculator.scaleToInt(loadSlot.getSlotOrPortCV()), loadSlot.getSlotOrPortDuration(),
					loadSlot.isSetArriveCold(), loadSlot.isArriveCold(), false);

			final Slot dischargeSlot = eCargo.getDischargeSlot();
			final int dischargeStart = convertTime(earliestTime, dischargeSlot.getWindowStart());//, dischargeSlot.getPort());
			final ITimeWindow dischargeWindow = builder.createTimeWindow(dischargeStart, dischargeStart + dischargeSlot.getWindowSize());
			final IShippingPriceCalculator dischargePriceCalculator;

			if (dischargeSlot.isSetFixedPrice()) {
				final int fixedPrice = Calculator.scaleToInt(dischargeSlot.getFixedPrice());
				dischargePriceCalculator = new IShippingPriceCalculator() {
					@Override
					public void prepareEvaluation(final ISequences sequences) {
					}

					@Override
					public int calculateUnitPrice(final IPortSlot slot, final int time) {
						return fixedPrice;
					}
				};
			} else {
				dischargePriceCalculator = entities.getOptimiserObject(dischargeSlot.getContract(), IShippingPriceCalculator.class);
			}

			final IDischargeSlot discharge = builder.createDischargeSlot(dischargeSlot.getName(), ports.lookup(dischargeSlot.getPort()), dischargeWindow,
					Calculator.scale(dischargeSlot.getSlotOrContractMinQuantity()), Calculator.scale(dischargeSlot.getSlotOrContractMaxQuantity()), dischargePriceCalculator,
					dischargeSlot.getSlotOrPortDuration(), false);

			entities.addModelObject(loadSlot, load);
			entities.addModelObject(dischargeSlot, discharge);

			for (final IContractTransformer contractTransformer : contractTransformers) {
				contractTransformer.slotTransformed(loadSlot, load);
				contractTransformer.slotTransformed(dischargeSlot, discharge);
			}

			final ICargo cargo = builder.createCargo(eCargo.getName(), load, discharge, eCargo.isSetAllowRewiring() ? eCargo.isAllowRewiring() : defaultRewiring);

			final Set<AVessel> allowedVessels = SetUtils.getVessels(eCargo.getAllowedVessels());
			if (allowedVessels.isEmpty()) {
				Set<IVessel> vesselsForCargo = new HashSet<IVessel>();
				for (final AVessel v : allowedVessels) {
					vesselsForCargo .add(vesselAssociation.lookup((Vessel) v));
				}
				builder.setCargoVesselRestriction(cargo, vesselsForCargo);
			}
		}
	}

	/**
	 * Convert a date into relative hours; returns the number of hours between windowStart and earliest.
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	private int convertTime(final Date earliest, final Date windowStart) {
		// I am using two calendars, because the java date objects are all
		// deprecated; however, timezones should not be a problem because
		// every Date in the EMF representation is in UTC.
		final Calendar a = Calendar.getInstance(timezone);
		a.setTime(earliest);
		final Calendar b = Calendar.getInstance(timezone);
		b.setTime(windowStart);
		final long difference = b.getTimeInMillis() - a.getTimeInMillis();
		return (int) (difference / Timer.ONE_HOUR);
	}

	/**
	 * Create the distance matrix for the given builder.
	 * 
	 * @param builder
	 *            the builder we are working with
	 * @param portAssociation
	 *            an association between ports in the EMF model and IPorts in the builder
	 * @param allPorts
	 *            the list of all IPorts constructed so far
	 * @param portIndices
	 *            a reverse-lookup for the ports in allPorts
	 * @param vesselAssociation
	 * @throws IncompleteScenarioException
	 */
	private void buildDistances(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final List<IPort> allPorts, final Map<IPort, Integer> portIndices,
			final Association<VesselClass, IVesselClass> vesselAssociation) throws IncompleteScenarioException {

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create the default distance matrix.
		 */
		PortModel portModel = rootObject.getSubModel(PortModel.class);
		for (final Route r : portModel.getRoutes()) {
			for (final RouteLine dl : r.getLines()) {
				IPort from, to;
				from = portAssociation.lookup(dl.getFrom());
				to = portAssociation.lookup(dl.getTo());

				final int distance = dl.getDistance();
				
				builder.setPortToPortDistance(from, to, r.getName(), distance);
			}
			
			// Set extra time and fuel consumption
			FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
			for (final VesselClass evc : fleetModel.getVesselClasses()) {
				for (final VesselClassRouteParameters routeParameters : evc.getRouteParameters()) {
					builder.setVesselClassRouteTimeAndFuel(routeParameters.getRoute().getName(), 
							vesselAssociation.lookup(evc),
							routeParameters.getExtraTransitTime(),
							Calculator.scale(routeParameters.getLadenConsumptionRate()));
					//TODO handle rates properly in optimiser
				}
			}
			
			// set tolls
			PricingModel pm = rootObject.getSubModel(PricingModel.class);
			for (final RouteCost routeCost : pm.getRouteCosts()) {
				final IVesselClass vesselClass = vesselAssociation.lookup(routeCost.getVesselClass());

				builder.setVesselClassRouteCost(routeCost.getRoute().getName(), vesselClass, VesselState.Laden, routeCost.getLadenCost());
				builder.setVesselClassRouteCost(routeCost.getRoute().getName(), vesselClass, VesselState.Ballast, routeCost.getBallastCost());
			}
		}

	}

	/**
	 * Construct the fleet model for the scenario
	 * 
	 * @param builder
	 *            a builder which has had its ports and distances instantiated
	 * @param portAssociation
	 *            the Port <-> IPort association to connect EMF Ports with builder IPorts
	 * @param entities
	 * @return
	 */
	private Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> buildFleet(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation,
			final ModelEntityMap entities) {

		/*
		 * Build the fleet model - first we must create the vessel classes from the model
		 */
		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<VesselClass, IVesselClass>();
		final Association<Vessel, IVessel> vesselAssociation = new Association<Vessel, IVessel>();
		// TODO: Check that we are mutliplying/dividing correctly to maintain
		// precision

		FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
		
		// look up prices
		
		for (final VesselClass eVc : fleetModel.getVesselClasses()) {
			int baseFuelPrice = 0;
			for (BaseFuelCost baseFuelCost : pricingModel.getFleetCost().getBaseFuelPrices()) {
				if (baseFuelCost.getFuel() == eVc.getBaseFuel()) {
					baseFuelPrice = Calculator.scaleToInt(
							baseFuelCost.getPrice().getValueAfter(latestTime));
					break;
				}
			}
			
			int charterInPrice = 0;
			
			int charterCount = 0;
			for (CharterCostModel charterCost : pricingModel.getFleetCost().getCharterCosts()) {
				if (charterCost.getVesselClasses().contains(eVc)) {
					if (charterCost.getCharterInPrice() != null) {
						charterInPrice = charterCost.getCharterInPrice().getValueAfter(latestTime);
					} else {
						charterInPrice = 0;
					}
//					charterOutPrice = charterCost.getCharterOutPrice().getValueAfter(latestTime);
					if (charterCost.getSpotCharterCount() != null) {
						charterCount = charterCost.getSpotCharterCount().getValueAfter(latestTime);
					} else {
						charterCount = 0;
					}
				}
			}
			
			final IVesselClass vc = builder.createVesselClass(
					eVc.getName(),
					Calculator.scaleToInt(eVc.getMinSpeed()),
					Calculator.scaleToInt(eVc.getMaxSpeed()),
					Calculator.scale((int) (eVc.getFillCapacity() * eVc.getCapacity())), 
					Calculator.scaleToInt(eVc.getMinHeel()), 
					baseFuelPrice,
					Calculator.scaleToInt(eVc.getBaseFuel().getEquivalenceFactor()),
					Calculator.scaleToInt(eVc.getPilotLightRate()) / 24,
					Calculator.scaleToInt(charterInPrice / 24.0), 
					eVc.getWarmingTime(), 
					eVc.getCoolingTime(), 
					Calculator.scale(eVc.getCoolingVolume())
					);
			
			vesselClassAssociation.add(eVc, vc);

			/*
			 * Set state-specific attributes
			 */
			buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Laden, eVc.getLadenAttributes());
			buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast, eVc.getBallastAttributes());

			/*
			 * set up inaccessible ports by applying resource allocation constraints
			 */
			final Set<IPort> inaccessiblePorts = new HashSet<IPort>();
			for (final APort ePort : SetUtils.getPorts(eVc.getInaccessiblePorts())) {
				inaccessiblePorts.add(portAssociation.lookup((Port) ePort));
			}
			
			if (inaccessiblePorts.isEmpty() == false) {
				builder.setVesselClassInaccessiblePorts(vc, inaccessiblePorts);
			}

			if (charterCount > 0) {
				for (int i = 0; i<charterCount; i++) {
					builder.createSpotVessels("SPOT-" + eVc.getName(), vesselClassAssociation.lookup(eVc), charterCount);
				}
			}
			
			entities.addModelObject(eVc, vc);
		}

		/*
		 * Now create each vessel
		 */
		for (final Vessel eV : fleetModel.getVessels()) {
			final IStartEndRequirement startRequirement = createRequirement(builder, portAssociation, 
					eV.getAvailability().getStartAfter(), eV.getAvailability().getStartBy(), 
					SetUtils.getPorts(eV.getAvailability().getStartAt()));
			
			final IStartEndRequirement endRequirement = createRequirement(builder, portAssociation, 
					eV.getAvailability().getEndAfter(), eV.getAvailability().getEndBy(), 
					SetUtils.getPorts(eV.getAvailability().getEndAt()));
			
			final int dailyCharterPrice = eV.isSetTimeCharterRate() ? eV.getTimeCharterRate(): 
				vesselClassAssociation.lookup(eV.getVesselClass()).getHourlyCharterInPrice() * 24;
			
			final long heelLimit = eV.getStartHeel().isSetVolumeAvailable()
					? Calculator.scale(eV.getStartHeel().getVolumeAvailable()) : 0;

			final IVessel vessel = builder.createVessel(eV.getName(), 
					vesselClassAssociation.lookup(eV.getVesselClass()), 
					(int) (Calculator.scale(dailyCharterPrice) / 24),
					eV.isSetTimeCharterRate() ? VesselInstanceType.TIME_CHARTER : VesselInstanceType.FLEET, 
							startRequirement, endRequirement, heelLimit,
					Calculator.scaleToInt(eV.getStartHeel().getCvValue()), Calculator.scaleToInt(eV.getStartHeel().getPricePerMMBTU()));
			vesselAssociation.add(eV, vessel);

			entities.addModelObject(eV, vessel);
		}
//
//		/*
//		 * Create spot charter vessels with no start/end requirements
//		 */
//		for (final VesselClass eVc : fleetModel.getVesselClasses()) {
//			if (eVc.getSpotCharterCount() > 0) {
//				final List<IVessel> spots = builder.createSpotVessels("SPOT-" + eVc.getName(), vesselClassAssociation.lookup(eVc), eVc.getSpotCharterCount());
//				// TODO this is not necessarily ideal; if there is an initial
//				// solution set we associate all the spot vessels with ones in
//				// that solution.
//				int vesselIndex = 0;
//				if ((scenario.getOptimisation() != null) && (scenario.getOptimisation().getCurrentSettings() != null)) {
//					final Schedule initialSchedule = scenario.getOptimisation().getCurrentSettings().getInitialSchedule();
//					if (initialSchedule != null) {
//						for (final AllocatedVessel allocatedVessel : initialSchedule.getFleet()) {
//							if ((allocatedVessel instanceof SpotVessel) && (((SpotVessel) allocatedVessel).getVesselClass() == eVc)) {
//								// map it to one of the ones we have just made
//								assert vesselIndex < spots.size() : "Initial schedule should not have more spot vessels than fleet suggests";
//								entities.addModelObject(allocatedVessel, spots.get(vesselIndex));
//								vesselIndex++;
//							}
//						}
//					}
//				}
//			}
//		}

		return new Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>>(vesselClassAssociation, vesselAssociation);
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param pat
	 * @return
	 */
	private IStartEndRequirement createRequirement(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Date from, final Date to, final Set<APort> ports) {
		ITimeWindow window = null;
		
		if (from == null && to != null) {
			window = builder.createTimeWindow(convertTime(earliestTime), convertTime(to));
		} else if (from != null && to == null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(latestTime));
		} else if (from != null && to != null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(to));
		}
		
		if (ports.isEmpty()) {
			if (window != null) {
				return builder.createStartEndRequirement(window);
			}
		} else {
			//TODO handle multiple start/end ports
			final IPort port = portAssociation.lookup((Port) ports.iterator().next());
			if (window == null) {
				return builder.createStartEndRequirement(port);
			} else {
				return builder.createStartEndRequirement(port, window);
			}
		}
		
		return builder.createStartEndRequirement();
	}

	private int convertTime(final Date startTime) {
		return convertTime(earliestTime, startTime);
	}

	/**
	 * Tell the builder to set up the given vessel state from the EMF fleet model
	 * 
	 * @param builder
	 *            the builder which is currently in use
	 * @param vc
	 *            the {@link IVesselClass} which the builder has constructed whose attributes we are setting
	 * @param laden
	 *            the {@link com.mmxlabs.scheduler.optimiser.components.VesselState} we are setting attributes for
	 * @param ladenAttributes
	 *            the {@link VesselStateAttributes} from the EMF model
	 */
	private void buildVesselStateAttributes(final ISchedulerBuilder builder, final IVesselClass vc, final com.mmxlabs.scheduler.optimiser.components.VesselState state,
			final VesselStateAttributes attrs) {

		// create consumption rate calculator for the curve
		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();

		for (final FuelConsumption line : attrs.getFuelConsumption()) {
			keypoints.put(Calculator.scaleToInt(line.getSpeed()), Calculator.scale(line.getConsumption()) / 24);
		}

		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		final LookupTableConsumptionRateCalculator cc = new LookupTableConsumptionRateCalculator(vc.getMinSpeed(), vc.getMaxSpeed(), consumptionCalculator);

		builder.setVesselClassStateParamaters(vc, state, Calculator.scaleToInt(attrs.getNboRate()) / 24, Calculator.scaleToInt(attrs.getIdleNBORate()) / 24,
				Calculator.scaleToInt(attrs.getIdleBaseRate()) / 24, cc);
	}

//	public void freezeStartSequences(final ISchedulerBuilder builder, final ModelEntityMap entities) {
//		if ((scenario.getOptimisation() != null) && (scenario.getOptimisation().getCurrentSettings() != null) && (scenario.getOptimisation().getCurrentSettings().getInitialSchedule() != null)) {
//			final int freezeHours = 24 * scenario.getOptimisation().getCurrentSettings().getFreezeDaysFromStart();
//			if (freezeHours <= 0) {
//				return;
//			}
//			final Schedule initialSchedule = scenario.getOptimisation().getCurrentSettings().getInitialSchedule();
//			// set up constraints on elements of initial schedule
//
//			// TODO at the moment OptimisationTransformer also processes the
//			// initial schedule, to create advice for the initial sequence
//			// builder. This isn't ideal, but if we want the ISchedulerBuilder
//			// to be the only way to get an IOptimisationData, and to prevent
//			// post-hoc changes to same it has to be this way.
//
//			final Date freezeDate = entities.getDateFromHours(freezeHours);
//			log.debug("Freezing sequencing decisions before " + freezeDate);
//			for (final Sequence sequence : initialSchedule.getSequences()) {
//				final AllocatedVessel av = sequence.getVessel();
//				final IVessel builderVessel;
//				if (av instanceof SpotVessel) {
//					// reverse lookup hack - see above where I associated spot
//					// vessels with
//					// IVessels in buildFleet(.)
//					builderVessel = entities.getOptimiserObject(av, IVessel.class);
//				} else if (av instanceof FleetVessel) {
//					builderVessel = entities.getOptimiserObject(((FleetVessel) av).getVessel(), IVessel.class);
//				} else {
//					throw new RuntimeException(av.getClass().getSimpleName() + " is a kind of AllocatedVessel this code cannot translate");
//				}
//
//				IPortSlot previousSlot = null;
//				/**
//				 * Used to ensure that if we freeze a load we also freeze the next discharge.
//				 */
//				boolean waitingForDischargeSlot = false;
//				for (final Event event : sequence.getEvents()) {
//					if (event.getStart().before(freezeDate) || waitingForDischargeSlot) {
//						final IPortSlot currentSlot;
//						if (event instanceof SlotVisit) {
//							final SlotVisit slotVisit = (SlotVisit) event;
//							// lock sequence and vessel
//							currentSlot = entities.getOptimiserObject(slotVisit.getSlot(), IPortSlot.class);
//							if (slotVisit.getSlot() instanceof LoadSlot) {
//								waitingForDischargeSlot = true;
//							} else {
//								waitingForDischargeSlot = false;
//							}
//						} else if (event instanceof VesselEventVisit) {
//							final VesselEventVisit vev = (VesselEventVisit) event;
//							currentSlot = entities.getOptimiserObject(vev.getVesselEvent(), IPortSlot.class);
//						} else {
//							currentSlot = null;
//						}
//
//						if (currentSlot != null) {
//							// bind slot to vessel - this overrides any previous
//							// binding
//							builder.constrainSlotToVessels(currentSlot, Collections.singleton(builderVessel));
//
//							// this removes any binding to vessel class, because
//							// the allowed vessels are the union of the above
//							// and the classes.
//							builder.constrainSlotToVesselClasses(currentSlot, null);
//							if (previousSlot != null) {
//								// bind sequencing as well - this forces
//								// previousSlot to come before currentSlot.
//								builder.constrainSlotAdjacency(previousSlot, currentSlot);
//							}
//
//							previousSlot = currentSlot;
//						}
//					} else {
//						break;
//					}
//				}
//			}
//		}
//	}

	OptimiserSettings defaultSettings = null;
	/**
	 * Utility method for getting the current optimisation settings from this scenario. TODO maybe put this in another file/model somewhere else.
	 * 
	 * @return
	 */
	public OptimiserSettings getOptimisationSettings() {
		final OptimiserModel om = rootObject.getSubModel(OptimiserModel.class);
		if (om != null) {
			// select settings
			OptimiserSettings x = om.getActiveSetting();
			if (x != null) return x;
		}
		if (defaultSettings == null) {
			defaultSettings = ScenarioUtils.createDefaultSettings();
		}
		return defaultSettings;
	}
}
