/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Named;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.IDESPurchaseSlotBindingsGenerator;
import com.mmxlabs.models.lng.transformer.inject.ISlotTimeWindowGenerator;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;

/**
 * Wrapper for an EMF LNG Scheduling {@link MMXRootObject}, providing utility methods to convert it into an optimisation job. Typical usage is to construct an LNGScenarioTransformer with a given
 * Scenario, and then call the {@link createOptimisationData} method. It is only expected that an instance will be used once. I.e. a single call to {@link #createOptimisationData(ModelEntityMap)}
 * 
 * @author hinton
 * 
 */
public class LNGScenarioTransformer {
	private static final Logger log = LoggerFactory.getLogger(LNGScenarioTransformer.class);

	private LNGScenarioModel rootObject;

	private Date earliestTime;
	private Date latestTime;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	@Named(LNGTransformerModule.Parser_BaseFuel)
	private SeriesParser baseFuelIndices;

	@Inject
	@Named(LNGTransformerModule.Parser_Charter)
	private SeriesParser charterIndices;

	@Inject
	@Named(LNGTransformerModule.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject(optional = true)
	private List<ITransformerExtension> transformerExtensions;

	@Inject(optional = true)
	private ISlotTimeWindowGenerator slotTimeWindowGenerator;

	@Inject(optional = true)
	private IDESPurchaseSlotBindingsGenerator desPurchaseSlotBindingsGenerator;

	@Inject
	private ISchedulerBuilder builder;

	@Inject
	private IShipToShipBindingProviderEditor shipToShipBindingProvider;

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

	private final Map<VesselClass, List<IVessel>> spotVesselsByClass = new HashMap<VesselClass, List<IVessel>>();

	private final ArrayList<IVessel> allVessels = new ArrayList<IVessel>();

	// private OptimiserSettings defaultSettings = null;

	/**
	 * The {@link Set} of ID strings already used. The UI should restrict user entered data items from clashing, but generated ID's may well clash with user ones.
	 */
	private final Set<String> usedIDStrings = new HashSet<String>();

	/**
	 * A {@link Map} of existing spot market slots by ID. This map is used later when building the spot market options.
	 */
	private final Map<String, Slot> marketSlotsByID = new HashMap<String, Slot>();

	private final EnumMap<SpotType, TreeMap<String, Collection<Slot>>> existingSpotCount = new EnumMap<SpotType, TreeMap<String, Collection<Slot>>>(SpotType.class);

	private final Map<Vessel, VesselAvailability> vesselAvailabiltyMap = new HashMap<Vessel, VesselAvailability>();

	private OptimiserSettings optimiserParameters;

	/**
	 * Create a transformer for the given scenario; the class holds a reference, so changes made to the scenario after construction will be reflected in calls to the various helper methods.
	 * 
	 * @param scenario
	 * @since 5.0
	 */
	@Inject
	public LNGScenarioTransformer(final LNGScenarioModel rootObject, final OptimiserSettings optimiserParameters) {

		init(rootObject, optimiserParameters);
	}

	/**
	 * @since 5.0
	 */
	protected void init(final LNGScenarioModel rootObject, final OptimiserSettings optimiserParameters) {

		this.rootObject = rootObject;
		this.optimiserParameters = optimiserParameters;
	}

	/**
	 * Get any {@link ITransformerExtension} and {@link IContractTransformer}s from the platform's registry.
	 */
	@Inject
	public boolean addPlatformTransformerExtensions() {

		if (transformerExtensions == null) {
			return false;
		}

		for (final ITransformerExtension transformer : transformerExtensions) {

			addTransformerExtension(transformer);

			if (transformer instanceof IContractTransformer) {
				final IContractTransformer contractTransformer = (IContractTransformer) transformer;
				addContractTransformer(contractTransformer);
			}
		}

		return true;
	}

	public void addTransformerExtension(final ITransformerExtension extension) {
		log.debug(extension.getClass().getCanonicalName() + " added to transformer extensions");
		allTransformerExtensions.add(extension);
	}

	/**
	 * @since 2.0
	 */
	public void addContractTransformer(final IContractTransformer transformer) {
		contractTransformers.add(transformer);
		for (final EClass ec : transformer.getContractEClasses()) {
			log.debug(transformer.getClass().getCanonicalName() + " handling contracts with eClass " + ec.getName());
			contractTransformersByEClass.put(ec, transformer);
		}
	}

	/**
	 * Instantiates and returns an {@link IOptimisationData} isomorphic to the contained scenario.
	 * 
	 * @return
	 * @throws IncompleteScenarioException
	 * @since 3.0
	 */
	public IOptimisationData createOptimisationData(final ModelEntityMap entities) throws IncompleteScenarioException {
		/*
		 * Set reference for hour 0
		 */
		findEarliestAndLatestTimes();

		dateHelper.setEarliestTime(earliestTime);
		// set earliest and latest times into entities
		entities.setEarliestDate(earliestTime);

		/**
		 * First, create all the market curves (should these come through the builder?)
		 */

		final Association<CommodityIndex, ICurve> commodityIndexAssociation = new Association<CommodityIndex, ICurve>();
		final Association<BaseFuelIndex, ICurve> baseFuelIndexAssociation = new Association<BaseFuelIndex, ICurve>();
		final Association<CharterIndex, ICurve> charterIndexAssociation = new Association<CharterIndex, ICurve>();

		final PricingModel pricingModel = rootObject.getPricingModel();
		// For each curve, register with the SeriesParser object
		for (final CommodityIndex commodityIndex : pricingModel.getCommodityIndices()) {
			final Index<Double> index = commodityIndex.getData();
			registerIndex(commodityIndex.getName(), index, commodityIndices);
		}
		for (final BaseFuelIndex baseFuelIndex : pricingModel.getBaseFuelPrices()) {
			final Index<Double> index = baseFuelIndex.getData();
			registerIndex(baseFuelIndex.getName(), index, baseFuelIndices);
		}
		for (final CharterIndex charterIndex : pricingModel.getCharterIndices()) {
			final Index<Integer> index = charterIndex.getData();
			registerIndex(charterIndex.getName(), index, charterIndices);
		}

		// Now pre-compute our various curve data objects...
		for (final CommodityIndex index : pricingModel.getCommodityIndices()) {
			try {
				final ISeries parsed = commodityIndices.getSeries(index.getName());
				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				curve.setDefaultValue(0);
				final int[] changePoints = parsed.getChangePoints();
				if (changePoints.length == 0) {
					if (index instanceof DerivedIndex<?>) {
						curve.setValueAfter(0, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
					}
				} else {
					for (final int i : changePoints) {
						curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				entities.addModelObject(index, curve);
				commodityIndexAssociation.add(index, curve);
			} catch (final Exception exception) {
				log.warn("Error evaluating series " + index.getName(), exception);
			}
		}

		for (final BaseFuelIndex index : pricingModel.getBaseFuelPrices()) {
			try {
				final ISeries parsed = baseFuelIndices.getSeries(index.getName());
				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				curve.setDefaultValue(0);

				final int[] changePoints = parsed.getChangePoints();
				if (changePoints.length == 0) {
					curve.setValueAfter(0, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
				} else {

					for (final int i : parsed.getChangePoints()) {
						curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				entities.addModelObject(index, curve);
				baseFuelIndexAssociation.add(index, curve);
			} catch (final Exception exception) {
				log.warn("Error evaluating series " + index.getName(), exception);
			}
		}

		for (final CharterIndex index : pricingModel.getCharterIndices()) {
			try {
				final ISeries parsed = charterIndices.getSeries(index.getName());
				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				curve.setDefaultValue(0);

				final int[] changePoints = parsed.getChangePoints();
				if (changePoints.length == 0) {
					final long dailyCost = OptimiserUnitConvertor.convertToInternalHourlyCost(parsed.evaluate(0).intValue());
					if (dailyCost != (int) dailyCost) {
						throw new IllegalStateException(String.format("Daily Cost of %d is too big.", OptimiserUnitConvertor.convertToExternalDailyCost(dailyCost)));
					}
					curve.setValueAfter(0, (int) dailyCost);
				} else {

					for (final int i : parsed.getChangePoints()) {
						final long dailyCost = OptimiserUnitConvertor.convertToInternalHourlyCost(parsed.evaluate(i).intValue());
						if (dailyCost != (int) dailyCost) {
							throw new IllegalStateException(String.format("Daily Cost of %d is too big.", OptimiserUnitConvertor.convertToExternalDailyCost(dailyCost)));
						}
						curve.setValueAfter(i - 1, (int) dailyCost);
					}
				}
				entities.addModelObject(index, curve);
				charterIndexAssociation.add(index, curve);
			} catch (final Exception exception) {
				log.warn("Error evaluating series " + index.getName(), exception);
			}
		}

		// set up the contract transformers
		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.startTransforming(rootObject, entities, builder);
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
		final PortModel portModel = rootObject.getPortModel();

		final Map<Port, ICooldownPriceCalculator> cooldownCalculators = new HashMap<Port, ICooldownPriceCalculator>();
		for (final CooldownPrice price : pricingModel.getCooldownPrices()) {
			final ICooldownPriceCalculator cooldownCalculator = new PriceExpressionContract(commodityIndexAssociation.lookup(price.getIndex()));

			for (final Port port : SetUtils.getObjects(price.getPorts())) {
				cooldownCalculators.put(port, cooldownCalculator);
			}
		}

		for (final Port ePort : portModel.getPorts()) {
			final IPort port;
			if (ePort.getLocation() != null) {
				final Location loc = ePort.getLocation();
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort), (float) loc.getLat(), (float) loc.getLon());
			} else {
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort));
			}
			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);
			entities.addModelObject(ePort, port);

			builder.setPortCV(port, OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getCvValue()));
		}

		final Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> vesselAssociations = buildFleet(builder, portAssociation, baseFuelIndexAssociation, charterIndexAssociation,
				entities);

		final CommercialModel commercialModel = rootObject.getCommercialModel();

		// Any NPE's in the following code are likely due to missing associations between a IContractTransformer and the EMF AContract object. IContractTransformer instances are typically OSGi
		// services. Ensure their bundles have been started!
		for (final SalesContract c : commercialModel.getSalesContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.getPriceInfo().eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + c.getPriceInfo().eClass().getName());
			}
			final ISalesPriceCalculator calculator = transformer.transformSalesPriceParameters(c.getPriceInfo());
			if (calculator == null) {
				throw new IllegalStateException("Unable to transform contract");
			}
			entities.addModelObject(c, calculator);
		}

		for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.getPriceInfo().eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + c.getPriceInfo().eClass().getName());
			}
			final ILoadPriceCalculator calculator = transformer.transformPurchasePriceParameters(c.getPriceInfo());
			entities.addModelObject(c, calculator);
		}

		// process port costs
		final PricingModel pricing = rootObject.getPricingModel();
		if (pricing != null) {
			for (final PortCost cost : pricing.getPortCosts()) {
				for (final Port port : SetUtils.getObjects(cost.getPorts())) {
					for (final PortCostEntry entry : cost.getEntries()) {
						PortType type = null;
						switch (entry.getActivity()) {
						case LOAD:
							type = PortType.Load;
							break;
						case DISCHARGE:
							type = PortType.Discharge;
							break;
						case DRYDOCK:
							type = PortType.DryDock;
							break;
						case MAINTENANCE:
							type = PortType.Maintenance;
							break;
						}

						if (type != null) {
							for (final IVessel v : allVessels) {
								// TODO should the builder handle the application of costs to vessel classes?
								final long activityCost = OptimiserUnitConvertor.convertToInternalFixedCost(cost.getPortCost(vesselAssociations.getFirst().reverseLookup(v.getVesselClass()),
										entry.getActivity()));
								builder.setPortCost(portAssociation.lookup((Port) port), v, type, activityCost);
							}
						}
					}
				}
			}
		}

		buildDistances(builder, portAssociation, allPorts, portIndices, vesselAssociations.getFirst(), entities);

		buildCargoes(builder, portAssociation, vesselAssociations.getSecond(), contractTransformers, entities, optimiserParameters.isShippingOnly());

		buildVesselEvents(builder, portAssociation, vesselAssociations.getFirst(), vesselAssociations.getSecond(), entities);

		buildSpotCargoMarkets(builder, portAssociation, contractTransformers, entities);

		buildMarkToMarkets(builder, portAssociation, contractTransformers, entities);

		// buildDiscountCurves(builder);

		// freezeStartSequences(builder, entities);

		// freeze any frozen assignments
		freezeAssignmentModel(builder, entities);

		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.finishTransforming();
		}

		builder.setEarliestDate(earliestTime);

		return builder.getOptimisationData();
	}

	@SuppressWarnings("rawtypes")
	private void registerIndex(final String name, final Index<? extends Number> index, final SeriesParser indices) {
		if (index instanceof DataIndex) {
			final DataIndex<? extends Number> di = (DataIndex<? extends Number>) index;
			final SortedSet<Pair<Date, Number>> vals = new TreeSet<Pair<Date, Number>>(new Comparator<Pair<Date, ?>>() {
				@Override
				public int compare(final Pair<Date, ?> o1, final Pair<Date, ?> o2) {
					return o1.getFirst().compareTo(o2.getFirst());
				}
			});
			for (final IndexPoint<? extends Number> pt : di.getPoints()) {
				vals.add(new Pair<Date, Number>(pt.getDate(), pt.getValue()));
			}
			final int[] times = new int[vals.size()];
			final Number[] nums = new Number[vals.size()];
			int k = 0;
			for (final Pair<Date, Number> e : vals) {
				times[k] = convertTime(e.getFirst());
				nums[k++] = e.getSecond();
			}
			indices.addSeriesData(name, times, nums);
		} else if (index instanceof DerivedIndex) {
			indices.addSeriesExpression(name, ((DerivedIndex) index).getExpression());
		}
	}

	private void freezeAssignmentModel(final ISchedulerBuilder builder, final ModelEntityMap entities) {

		Date freezeDate = null;
		final OptimisationRange range = optimiserParameters.getRange();
		if (range != null) {
			freezeDate = range.getOptimiseAfter();
		}

		final AssignmentModel assignmentModel = rootObject.getPortfolioModel().getAssignmentModel();
		if (assignmentModel != null) {

			for (final ElementAssignment assignment : assignmentModel.getElementAssignments()) {
				final UUIDObject o = assignment.getAssignedObject();

				boolean freeze = assignment.isLocked();

				if (!freeze && freezeDate != null) {
					if (o instanceof Cargo) {
						final Cargo cargo = (Cargo) o;
						if (!cargo.getSortedSlots().isEmpty()) {
							if (cargo.getSortedSlots().get(0).getWindowStart().before(freezeDate)) {
								freeze = true;
							}
						}
					} else if (o instanceof VesselEvent) {
						final VesselEvent vesselEvent = (VesselEvent) o;
						if (vesselEvent.getStartBy().before(freezeDate)) {
							freeze = true;
						}
					}
				}

				if (!freeze) {
					continue;
				}

				final AVesselSet<Vessel> vesselSet = assignment.getAssignment();
				final IVessel vessel;
				if (vesselSet instanceof VesselClass) {
					final List<IVessel> spots = spotVesselsByClass.get(vesselSet);
					if (spots != null && spots.isEmpty() == false) {
						vessel = spots.get(0);
						spots.remove(0);
					} else {
						vessel = null;
					}

				} else if (vesselSet instanceof Vessel) {
					final VesselAvailability vesselAvailability = vesselAvailabiltyMap.get(vesselSet);
					vessel = entities.getOptimiserObject(vesselAvailability, IVessel.class);
				} else {
					vessel = null;
				}

				if (vessel == null) {
					continue;
				}

				if (o instanceof Cargo) {
					final Cargo cargo = (Cargo) o;
					IPortSlot prevSlot = null;
					for (final Slot slot : cargo.getSortedSlots()) {
						final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
						if (cargo != null) {
							// bind slots to vessel
							builder.constrainSlotToVessels(portSlot, Collections.singleton(vessel));
							// bind sequencing as well - this forces
							// previousSlot to come before currentSlot.
							if (prevSlot != null) {
								builder.constrainSlotAdjacency(prevSlot, portSlot);

							}
							prevSlot = portSlot;
						}
					}
				} else if (o instanceof VesselEvent) {
					final IVesselEventPortSlot slot = entities.getOptimiserObject(o, IVesselEventPortSlot.class);
					if (slot != null) {
						builder.constrainSlotToVessels(slot, Collections.singleton(vessel));
					}
				}
			}
		}
	}

	// /**
	// * Create and associate discount curves
	// *
	// * This ought to be in {@link OptimisationTransformer} but that doesn't have access to set up DCPs.
	// *
	// * @param builder
	// */
	// private void buildDiscountCurves(final ISchedulerBuilder builder) {
	// // set up DCP
	//
	// final DiscountCurve defaultCurve = scenario.getOptimisation().getCurrentSettings().isSetDefaultDiscountCurve() ? scenario.getOptimisation().getCurrentSettings().getDefaultDiscountCurve()
	// : null;
	//
	// final ICurve realDefaultCurve;
	// if (defaultCurve == null) {
	// realDefaultCurve = new ConstantValueCurve(1);
	// } else {
	// realDefaultCurve = buildDiscountCurve(defaultCurve);
	// }
	//
	// for (final Objective objective : scenario.getOptimisation().getCurrentSettings().getObjectives()) {
	// if (objective.isSetDiscountCurve()) {
	// builder.setFitnessComponentDiscountCurve(objective.getName(), buildDiscountCurve(objective.getDiscountCurve()));
	// } else {
	// builder.setFitnessComponentDiscountCurve(objective.getName(), realDefaultCurve);
	// }
	// }
	// }

	// /**
	// * @param defaultCurve
	// * @return
	// */
	// private ICurve buildDiscountCurve(final DiscountCurve curve) {
	// final InterpolatingDiscountCurve realCurve = new InterpolatingDiscountCurve();
	//
	// final int baseTime = curve.isSetStartDate() ? convertTime(curve.getStartDate()) : 0;
	// if (baseTime > 0) {
	// realCurve.setValueAtPoint(0, 1);
	// realCurve.setValueAtPoint(baseTime - 1, 1);
	// }
	//
	// for (final Discount d : curve.getDiscounts()) {
	// realCurve.setValueAtPoint(baseTime + d.getTime(), d.getDiscountFactor());
	// }
	// return realCurve;
	// }

	/**
	 * Find the earliest and latest times set by events in the model. This takes into account:
	 * 
	 * <ul>
	 * <li>Slot window dates</li>
	 * <li>Vessel event dates</li>
	 * <li>Vessel availability dates</li>
	 * </ul>
	 */
	private void findEarliestAndLatestTimes() {
		earliestTime = null;
		latestTime = null;
		final ScenarioFleetModel scenarioFleetModel = rootObject.getPortfolioModel().getScenarioFleetModel();
		final CargoModel cargo = rootObject.getPortfolioModel().getCargoModel();

		final HashSet<Date> allDates = new HashSet<Date>();

		for (final VesselEvent event : scenarioFleetModel.getVesselEvents()) {
			allDates.add(event.getStartBy());
			allDates.add(event.getStartAfter());
		}
		for (final VesselAvailability vesselAvailability : scenarioFleetModel.getVesselAvailabilities()) {
			if (vesselAvailability.isSetStartBy())
				allDates.add(vesselAvailability.getStartBy());
			if (vesselAvailability.isSetStartAfter())
				allDates.add(vesselAvailability.getStartAfter());

			if (vesselAvailability.isSetEndBy())
				allDates.add(vesselAvailability.getEndBy());
			if (vesselAvailability.isSetEndAfter())
				allDates.add(vesselAvailability.getEndAfter());
		}
		for (final Slot s : cargo.getLoadSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}
		for (final Slot s : cargo.getDischargeSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}

		earliestTime = allDates.isEmpty() ? new Date(0) : Collections.min(allDates);
		latestTime = allDates.isEmpty() ? new Date(0) : Collections.max(allDates);
	}

	private void buildVesselEvents(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<VesselClass, IVesselClass> classes,
			final Association<Vessel, IVessel> vessels, final ModelEntityMap entities) {

		final Date latestDate = optimiserParameters.getRange().isSetOptimiseBefore() ? optimiserParameters.getRange().getOptimiseBefore() : latestTime;

		final ScenarioFleetModel scenarioFleetModel = rootObject.getPortfolioModel().getScenarioFleetModel();

		for (final VesselEvent event : scenarioFleetModel.getVesselEvents()) {

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
				final HeelOptions heelOptions = charterOut.getHeelOptions();
				final long maxHeel = heelOptions.isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getVolumeAvailable()) : Long.MAX_VALUE;
				final long totalHireCost = OptimiserUnitConvertor.convertToInternalDailyCost(charterOut.getHireRate()) * (long) charterOut.getDurationInDays();
				final long repositioning = OptimiserUnitConvertor.convertToInternalFixedCost(charterOut.getRepositioningFee());
				builderSlot = builder.createCharterOutEvent(event.getName(), window, port, endPort, durationHours, maxHeel,
						OptimiserUnitConvertor.convertToInternalConversionFactor(heelOptions.getCvValue()), OptimiserUnitConvertor.convertToInternalPrice(heelOptions.getPricePerMMBTU()),
						totalHireCost, repositioning);
			} else if (event instanceof DryDockEvent) {
				builderSlot = builder.createDrydockEvent(event.getName(), window, port, durationHours);
			} else if (event instanceof MaintenanceEvent) {
				builderSlot = builder.createMaintenanceEvent(event.getName(), window, port, durationHours);
			} else {
				throw new RuntimeException("Unknown event type: " + event.getClass().getName());
			}

			for (final Vessel v : SetUtils.getObjects(event.getAllowedVessels())) {
				final IVessel optimiserVessel = vessels.lookup((Vessel) v);
				if (optimiserVessel != null) {
					builder.addVesselEventVessel(builderSlot, optimiserVessel);
				}
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
	private void buildCargoes(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<Vessel, IVessel> vesselAssociation,
			final Collection<IContractTransformer> contractTransformers, final ModelEntityMap entities, final boolean shippingOnly) {

		final Date latestDate = optimiserParameters.getRange().isSetOptimiseBefore() ? optimiserParameters.getRange().getOptimiseBefore() : latestTime;

		final Set<LoadSlot> usedLoadSlots = new HashSet<LoadSlot>();
		final Set<DischargeSlot> usedDischargeSlots = new HashSet<DischargeSlot>();

		final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();
		final Map<Slot, IPortSlot> transferSlotMap = new HashMap<Slot, IPortSlot>();

		for (final Cargo eCargo : cargoModel.getCargoes()) {

			if (eCargo.getSortedSlots().get(0).getWindowStartWithSlotOrPortTime().after(latestDate)) {
				continue;
			}

			final List<ILoadOption> loadOptions = new LinkedList<ILoadOption>();
			final List<IDischargeOption> dischargeOptions = new LinkedList<IDischargeOption>();
			final List<IPortSlot> slots = new ArrayList<IPortSlot>(eCargo.getSortedSlots().size());
			final Map<Slot, IPortSlot> slotMap = new HashMap<Slot, IPortSlot>();
			for (final Slot slot : eCargo.getSortedSlots()) {
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					{
						final ILoadOption load = createLoadOption(builder, portAssociation, contractTransformers, entities, loadSlot);
						usedLoadSlots.add(loadSlot);
						loadOptions.add(load);
						slotMap.put(loadSlot, load);
						slots.add(load);
					}

				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					{
						final IDischargeOption discharge = createDischargeOption(builder, portAssociation, contractTransformers, entities, dischargeSlot);
						usedDischargeSlots.add(dischargeSlot);
						dischargeOptions.add(discharge);
						slotMap.put(dischargeSlot, discharge);
						slots.add(discharge);
					}
				} else {
					throw new IllegalArgumentException("Unexpected Slot type");
				}
			}

			for (final Slot slot : eCargo.getSortedSlots()) {
				boolean isTransfer = false;

				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					// Bind FOB/DES slots to resource
					final ILoadOption load = (ILoadOption) slotMap.get(loadSlot);
					if (loadSlot.isDESPurchase()) {
						if (loadSlot instanceof SpotLoadSlot) {
							final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) loadSlot;
							final Set<IPort> marketPorts = new HashSet<IPort>();
							{
								final SpotMarket market = spotLoadSlot.getMarket();
								if (market instanceof DESPurchaseMarket) {
									final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
									final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
									for (final Port ap : portSet) {
										final IPort ip = portAssociation.lookup(ap);
										if (ip != null) {
											marketPorts.add(ip);
										}
									}
								}

							}
							builder.bindDischargeSlotsToDESPurchase(load, marketPorts);
						} else {
							if (desPurchaseSlotBindingsGenerator == null) {
								final Set<IPort> ports = Collections.singleton(load.getPort());
								builder.bindDischargeSlotsToDESPurchase(load, ports);
							} else {
								desPurchaseSlotBindingsGenerator.bindDischargeSlotsToDESPurchase(builder, loadSlot, load);
							}
						}
					}
					isTransfer = (((LoadSlot) slot).getTransferFrom() != null);
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.isFOBSale()) {
						for (final IPortSlot load : loadOptions) {
							builder.bindLoadSlotsToFOBSale((IDischargeOption) slotMap.get(dischargeSlot), Collections.singleton(load.getPort()));
						}
					}
					isTransfer = (((DischargeSlot) slot).getTransferTo() != null);
				}

				// remember any slots which were part of a ship-to-ship transfer
				// but don't do anything with them yet, because we need to wait until all slots have been processed
				if (isTransfer) {
					transferSlotMap.put(slot, slotMap.get(slot));
				}
			}

			final ICargo cargo = builder.createCargo(slots, shippingOnly ? false : eCargo.isAllowRewiring());

			entities.addModelObject(eCargo, cargo);
			if (eCargo.getCargoType() == CargoType.FLEET) {

				final Set<Vessel> allowedVessels = SetUtils.getObjects(eCargo.getAllowedVessels());
				if (!allowedVessels.isEmpty()) {
					final Set<IVessel> vesselsForCargo = new HashSet<IVessel>();
					for (final Vessel v : allowedVessels) {
						vesselsForCargo.add(vesselAssociation.lookup((Vessel) v));
					}
					builder.setCargoVesselRestriction(slots, vesselsForCargo);
				}
			}
		}

		// register ship-to-ship transfers with the relevant data component provider
		for (final Entry<Slot, IPortSlot> entry : transferSlotMap.entrySet()) {
			final Slot slot = entry.getKey();
			final IPortSlot portSlot = entry.getValue();
			Slot converse = null;
			if (slot instanceof DischargeSlot) {
				converse = ((DischargeSlot) slot).getTransferTo();
			} else if (slot instanceof LoadSlot) {
				converse = ((LoadSlot) slot).getTransferFrom();
			}

			shipToShipBindingProvider.setConverseTransferElement(portSlot, transferSlotMap.get(converse));
		}

		for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {

			// TODO: Filter on date

			// if (eCargo.getLoadSlot().getWindowStartWithSlotOrPortTime().after(latestDate)) {
			// continue;
			// }
			if (usedLoadSlots.contains(loadSlot)) {
				continue;
			}

			final ILoadOption load;
			{

				// Make optional
				load = createLoadOption(builder, portAssociation, contractTransformers, entities, loadSlot);
				if (!loadSlot.isOptional()) {
					builder.setSoftRequired(load);
				}
			}
			// Bind FOB/DES slots to resource
			if (loadSlot.isDESPurchase()) {
				if (loadSlot instanceof SpotLoadSlot) {
					final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) loadSlot;
					final Set<IPort> marketPorts = new HashSet<IPort>();
					{
						final SpotMarket market = spotLoadSlot.getMarket();
						if (market instanceof DESPurchaseMarket) {
							final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
							final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
							for (final Port ap : portSet) {
								final IPort ip = portAssociation.lookup((Port) ap);
								if (ip != null) {
									marketPorts.add(ip);
								}
							}
						}

					}
					builder.bindDischargeSlotsToDESPurchase(load, marketPorts);
				} else {
					if (desPurchaseSlotBindingsGenerator == null) {
						final Set<IPort> ports = Collections.singleton(load.getPort());
						builder.bindDischargeSlotsToDESPurchase(load, ports);
					} else {
						desPurchaseSlotBindingsGenerator.bindDischargeSlotsToDESPurchase(builder, loadSlot, load);
					}
				}
			}
		}

		for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {

			// TODO: Filter on date
			// if (eCargo.getLoadSlot().getWindowStartWithSlotOrPortTime().after(latestDate)) {
			// continue;
			// }
			if (usedDischargeSlots.contains(dischargeSlot)) {
				continue;
			}

			final IDischargeOption discharge;
			{
				discharge = createDischargeOption(builder, portAssociation, contractTransformers, entities, dischargeSlot);
				if (!dischargeSlot.isOptional()) {
					builder.setSoftRequired(discharge);
				}
			}
			// Bind FOB/DES slots to resource
			if (dischargeSlot.isFOBSale()) {
				builder.bindLoadSlotsToFOBSale(discharge, Collections.singleton(discharge.getPort()));
			}

		}
	}

	private IDischargeOption createDischargeOption(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final DischargeSlot dischargeSlot) {
		final IDischargeOption discharge;
		usedIDStrings.add(dischargeSlot.getName());

		final ITimeWindow dischargeWindow = builder.createTimeWindow(convertTime(earliestTime, dischargeSlot.getWindowStartWithSlotOrPortTime()),
				convertTime(earliestTime, dischargeSlot.getWindowEndWithSlotOrPortTime()));

		final ISalesPriceCalculator dischargePriceCalculator;

		if (dischargeSlot.isSetPriceExpression()) {

			final String priceExpression = dischargeSlot.getPriceExpression();
			if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
				dischargePriceCalculator = new BreakEvenSalesPriceCalculator();
			} else {
				final IExpression<ISeries> expression = commodityIndices.parse(priceExpression);
				final ISeries parsed = expression.evaluate();

				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				if (parsed.getChangePoints().length == 0) {
					curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
				} else {

					curve.setDefaultValue(0);
					for (final int i : parsed.getChangePoints()) {
						curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				dischargePriceCalculator = new PriceExpressionContract(curve);
			}

		} else if (dischargeSlot.isSetContract()) {
			dischargePriceCalculator = entities.getOptimiserObject(dischargeSlot.getContract(), ISalesPriceCalculator.class);
		} else {
			throw new IllegalStateException("Discharge Slot has no contract or other pricing data");
		}

		// local scope for slot creation convenience variables
		{
			// convenience variables
			final String name = dischargeSlot.getName();
			final IPort port = portAssociation.lookup(dischargeSlot.getPort());
			final long minVolume = OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMinQuantity());
			final long maxVolume = OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMaxQuantity());

			final long minCv;
			final long maxCv;

			if (dischargeSlot.isSetPricingDate()) {
				dischargeSlot.getPricingDate();
			}
			final int pricingDate = dischargeSlot.isSetPricingDate() ? convertTime(earliestTime, dischargeSlot.getPricingDate()) : IPortSlot.NO_PRICING_DATE;

			if (dischargeSlot.isSetContract()) {
				final SalesContract salesContract = (SalesContract) dischargeSlot.getContract();

				if (salesContract.isSetMinCvValue()) {
					minCv = OptimiserUnitConvertor.convertToInternalConversionFactor(salesContract.getMinCvValue());
				} else {
					minCv = 0;
				}

				if (salesContract.isSetMaxCvValue()) {
					maxCv = OptimiserUnitConvertor.convertToInternalConversionFactor(salesContract.getMaxCvValue());
				} else {
					maxCv = Long.MAX_VALUE;
				}
			} else {
				minCv = 0;
				maxCv = Long.MAX_VALUE;
			}

			if (dischargeSlot.isFOBSale()) {
				discharge = builder.createFOBSaleDischargeSlot(name, port, dischargeWindow, minVolume, maxVolume, minCv, maxCv, dischargePriceCalculator, pricingDate, dischargeSlot.isOptional());
			} else {
				discharge = builder.createDischargeSlot(name, port, dischargeWindow, minVolume, maxVolume, minCv, maxCv, dischargePriceCalculator, dischargeSlot.getSlotOrPortDuration(), pricingDate,
						dischargeSlot.isOptional());
			}
		}

		if (dischargeSlot instanceof SpotSlot) {
			marketSlotsByID.put(dischargeSlot.getName(), dischargeSlot);
			addSpotSlotToCount((SpotSlot) dischargeSlot);
		}

		entities.addModelObject(dischargeSlot, discharge);
		for (final IContractTransformer contractTransformer : contractTransformers) {
			contractTransformer.slotTransformed(dischargeSlot, discharge);
		}
		return discharge;
	}

	private ILoadOption createLoadOption(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final LoadSlot loadSlot) {
		final ILoadOption load;
		usedIDStrings.add(loadSlot.getName());

		final ITimeWindow loadWindow = builder.createTimeWindow(convertTime(earliestTime, loadSlot.getWindowStartWithSlotOrPortTime()),
				convertTime(earliestTime, loadSlot.getWindowEndWithSlotOrPortTime()));

		final ILoadPriceCalculator loadPriceCalculator;

		if (loadSlot.isSetPriceExpression()) {

			final String priceExpression = loadSlot.getPriceExpression();
			if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
				loadPriceCalculator = new BreakEvenLoadPriceCalculator();
			} else {
				final IExpression<ISeries> expression = commodityIndices.parse(priceExpression);
				final ISeries parsed = expression.evaluate();

				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				if (parsed.getChangePoints().length == 0) {
					curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
				} else {

					curve.setDefaultValue(0);
					for (final int i : parsed.getChangePoints()) {
						curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				loadPriceCalculator = new PriceExpressionContract(curve);
			}
		} else if (loadSlot.isSetContract()) {
			final PurchaseContract purchaseContract = (PurchaseContract) (loadSlot.getContract());
			loadPriceCalculator = entities.getOptimiserObject(purchaseContract, ILoadPriceCalculator.class);
		} else {
			throw new IllegalStateException("Load Slot has no contract or other pricing data");
		}

		final int slotPricingDate = loadSlot.isSetPricingDate() ? convertTime(earliestTime, loadSlot.getPricingDate()) : IPortSlot.NO_PRICING_DATE;

		if (loadSlot.isDESPurchase()) {
			final ITimeWindow localTimeWindow;
			if (slotTimeWindowGenerator == null) {
				localTimeWindow = loadWindow;
			} else {
				localTimeWindow = slotTimeWindowGenerator.generateTimeWindow(builder, loadSlot, earliestTime, loadWindow);
			}
			load = builder.createDESPurchaseLoadSlot(loadSlot.getName(), portAssociation.lookup(loadSlot.getPort()), localTimeWindow,
					OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMinQuantity()), OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMaxQuantity()),
					loadPriceCalculator, OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrPortCV()), slotPricingDate, loadSlot.isOptional());
		} else {
			load = builder.createLoadSlot(loadSlot.getName(), portAssociation.lookup(loadSlot.getPort()), loadWindow,
					OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMinQuantity()), OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMaxQuantity()),
					loadPriceCalculator, OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrPortCV()), loadSlot.getSlotOrPortDuration(), loadSlot.isSetArriveCold(),
					loadSlot.isArriveCold(), slotPricingDate, loadSlot.isOptional());
		}
		// Store market slots for lookup when building spot markets.
		entities.addModelObject(loadSlot, load);

		for (final IContractTransformer contractTransformer : contractTransformers) {
			contractTransformer.slotTransformed(loadSlot, load);
		}
		if (loadSlot instanceof SpotSlot) {
			marketSlotsByID.put(loadSlot.getName(), loadSlot);
			addSpotSlotToCount((SpotSlot) loadSlot);
		}
		return load;
	}

	private void buildSpotCargoMarkets(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities) {

		final SpotMarketsModel spotMarketsModel = rootObject.getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}
		final Date earliestDate = optimiserParameters.getRange().isSetOptimiseAfter() ? optimiserParameters.getRange().getOptimiseAfter() : earliestTime;
		final Date latestDate = optimiserParameters.getRange().isSetOptimiseBefore() ? optimiserParameters.getRange().getOptimiseBefore() : latestTime;

		buildDESPurchaseSpotMarket(builder, portAssociation, contractTransformers, entities, earliestDate, latestDate, spotMarketsModel.getDesPurchaseSpotMarket());
		buildDESSalesSpotMarket(builder, portAssociation, contractTransformers, entities, earliestDate, latestDate, spotMarketsModel.getDesSalesSpotMarket());
		buildFOBPurchaseSpotMarket(builder, portAssociation, contractTransformers, entities, earliestDate, latestDate, spotMarketsModel.getFobPurchasesSpotMarket());
		buildFOBSalesSpotMarket(builder, portAssociation, contractTransformers, entities, earliestDate, latestDate, spotMarketsModel.getFobSalesSpotMarket());

	}

	private void buildDESPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date earliestDate, final Date latestDate, final SpotMarketGroup desPurchaseSpotMarket) {
		if (desPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = desPurchaseSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestDate);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());

				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : desPurchaseSpotMarket.getMarkets()) {
					assert market instanceof DESPurchaseMarket;
					if (market instanceof DESPurchaseMarket) {
						final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
						final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
						final Set<IPort> marketPorts = new HashSet<IPort>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup((Port) ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot> existing = getSpotSlots(SpotType.DES_PURCHASE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(desPurchaseMarket.getPriceInfo().eClass());
						final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(desPurchaseMarket.getPriceInfo());

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(desPurchaseMarket.getCv());

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								// Avoid ID clash
								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final ILoadOption desPurchaseSlot = builder.createDESPurchaseLoadSlot(id, null, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, IPortSlot.NO_PRICING_DATE, true);

								// Create a fake model object to add in here;
								final SpotLoadSlot desSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								desSlot.setDESPurchase(true);
								desSlot.setName(id);
								desSlot.setArriveCold(false);
								desSlot.setCargoCV(desPurchaseMarket.getCv());
								desSlot.setWindowStart(new Date(startTime.getTime()));
								// desSlot.setContract(desPurchaseMarket.getContract());
								desSlot.setOptional(true);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000 / 60 / 60;
								desSlot.setWindowSize((int) duration);
								// Key piece of information
								desSlot.setMarket(desPurchaseMarket);
								entities.addModelObject(desSlot, desPurchaseSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(desSlot, desPurchaseSlot);
								}

								builder.bindDischargeSlotsToDESPurchase(desPurchaseSlot, marketPorts);

								marketSlots.add(desPurchaseSlot);
								marketGroupSlots.add(desPurchaseSlot);
							}
						}
						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private void buildFOBSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date earliestDate, final Date latestDate, final SpotMarketGroup fobSalesSpotMarket) {
		if (fobSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = fobSalesSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestDate);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());
				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : fobSalesSpotMarket.getMarkets()) {
					assert market instanceof FOBSalesMarket;
					if (market instanceof FOBPurchasesMarket) {
						final FOBSalesMarket fobSaleMarket = (FOBSalesMarket) market;
						final Set<Port> portSet = SetUtils.getObjects(fobSaleMarket.getOriginPorts());
						final Set<IPort> marketPorts = new HashSet<IPort>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup((Port) ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot> existing = getSpotSlots(SpotType.FOB_SALE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(fobSaleMarket.getPriceInfo().eClass());
						final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(fobSaleMarket.getPriceInfo());

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final long minCv = 0;
								final long maxCv = Long.MAX_VALUE;

								final IDischargeOption fobSaleSlot = builder.createFOBSaleDischargeSlot(id, null, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), minCv, maxCv, priceCalculator, IPortSlot.NO_PRICING_DATE, true);

								// Create a fake model object to add in here;
								final SpotDischargeSlot fobSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								fobSlot.setFOBSale(true);
								fobSlot.setName(id);
								fobSlot.setWindowStart(new Date(startTime.getTime()));
								// fobSlot.setContract(fobSaleMarket.getContract());
								fobSlot.setOptional(true);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000 / 60 / 60;
								fobSlot.setWindowSize((int) duration);
								// Key piece of information
								fobSlot.setMarket(fobSaleMarket);
								entities.addModelObject(fobSlot, fobSaleSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(fobSlot, fobSaleSlot);
								}

								builder.bindLoadSlotsToFOBSale(fobSaleSlot, marketPorts);

								marketSlots.add(fobSaleSlot);
								marketGroupSlots.add(fobSaleSlot);
							}
						}

						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private void buildDESSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date earliestDate, final Date latestDate, final SpotMarketGroup desSalesSpotMarket) {
		if (desSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = desSalesSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestDate);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());
				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();
				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : desSalesSpotMarket.getMarkets()) {
					assert market instanceof DESSalesMarket;
					if (market instanceof DESSalesMarket) {
						final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
						final Port notionalAPort = desSalesMarket.getNotionalPort();
						final IPort notionalIPort = portAssociation.lookup((Port) notionalAPort);

						final Collection<Slot> existing = getSpotSlots(SpotType.DES_SALE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(desSalesMarket.getPriceInfo().eClass());
						final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(desSalesMarket.getPriceInfo());

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								// Create a fake model object to add in here;
								final SpotDischargeSlot desSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								desSlot.setName(id);
								desSlot.setWindowStart(new Date(startTime.getTime()));
								// desSlot.setContract(desSalesMarket.getContract());
								desSlot.setOptional(true);
								desSlot.setPort((Port) notionalAPort);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000l / 60l / 60l;
								desSlot.setWindowSize((int) duration);

								if (desSlot.isSetPricingDate()) {
									desSlot.getPricingDate();
								}
								final int pricingDate = desSlot.isSetPricingDate() ? convertTime(earliestTime, desSlot.getPricingDate()) : IPortSlot.NO_PRICING_DATE;

								final IDischargeOption desSalesSlot = builder
										.createDischargeSlot(id, notionalIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
												OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), 0, Long.MAX_VALUE, priceCalculator, desSlot.getSlotOrPortDuration(),
												pricingDate, true);

								// Key piece of information
								desSlot.setMarket(desSalesMarket);
								entities.addModelObject(desSlot, desSalesSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(desSlot, desSalesSlot);
								}

								marketSlots.add(desSalesSlot);
								marketGroupSlots.add(desSalesSlot);
							}
						}
						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private void buildFOBPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date earliestDate, final Date latestDate, final SpotMarketGroup fobPurchaseSpotMarket) {
		if (fobPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = fobPurchaseSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestDate);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());

				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : fobPurchaseSpotMarket.getMarkets()) {
					assert market instanceof FOBPurchasesMarket;
					if (market instanceof FOBPurchasesMarket) {
						final FOBPurchasesMarket fobPurchaseMarket = (FOBPurchasesMarket) market;
						final Port notionalAPort = fobPurchaseMarket.getNotionalPort();
						final IPort notionalIPort = portAssociation.lookup((Port) notionalAPort);

						final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(fobPurchaseMarket.getCv());

						final Collection<Slot> existing = getSpotSlots(SpotType.FOB_PURCHASE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(fobPurchaseMarket.getPriceInfo().eClass());
						final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(fobPurchaseMarket.getPriceInfo());

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {

									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								// Create a fake model object to add in here;
								final SpotLoadSlot fobSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								fobSlot.setName(id);
								fobSlot.setWindowStart(new Date(startTime.getTime()));
								// fobSlot.setContract(fobPurchaseMarket.getContract());
								fobSlot.setOptional(true);
								fobSlot.setArriveCold(true);
								fobSlot.setCargoCV(fobPurchaseMarket.getCv());
								fobSlot.setPort((Port) notionalAPort);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000l / 60l / 60l;
								fobSlot.setWindowSize((int) duration);

								final ILoadOption fobPurchaseSlot = builder.createLoadSlot(id, notionalIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, fobSlot.getSlotOrPortDuration(), true, true,
										IPortSlot.NO_PRICING_DATE, true);

								// Key piece of information
								fobSlot.setMarket(fobPurchaseMarket);
								entities.addModelObject(fobSlot, fobPurchaseSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(fobSlot, fobPurchaseSlot);
								}

								marketSlots.add(fobPurchaseSlot);
								marketGroupSlots.add(fobPurchaseSlot);
							}
						}
						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private int getAvailabilityForDate(final SpotAvailability availability, final Date startTime) {
		boolean valueSet = false;
		int count = 0;
		if (availability != null) {
			if (availability.isSetCurve()) {
				final Index<Integer> curve = availability.getCurve();

				final Integer value = curve.getValueForMonth(startTime);
				if (value != null) {
					count = value;
					valueSet = true;
				}

			}
			if (!valueSet && availability.isSetConstant()) {
				count = availability.getConstant();
				valueSet = true;

			}
		}
		return count;
	}

	private void buildMarkToMarkets(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities) {

		final SpotMarketsModel spotMarketsModel = rootObject.getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}

		buildDESPurchaseMarkToMarket(builder, portAssociation, contractTransformers, entities, spotMarketsModel.getDesPurchaseSpotMarket());
		buildDESSalesMarkToMarket(builder, portAssociation, contractTransformers, entities, spotMarketsModel.getDesSalesSpotMarket());
		buildFOBSalesMarkToMarket(builder, portAssociation, contractTransformers, entities, spotMarketsModel.getFobSalesSpotMarket());
		buildFOBPurchasesMarkToMarket(builder, portAssociation, contractTransformers, entities, spotMarketsModel.getFobPurchasesSpotMarket());
	}

	private void buildDESPurchaseMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof DESPurchaseMarket;
				if (market instanceof DESPurchaseMarket) {
					final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
					final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());

					final Set<IPort> marketPorts = new HashSet<IPort>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup((Port) ap);
						if (ip != null) {
							marketPorts.add(ip);
						}
					}

					final IContractTransformer transformer = contractTransformersByEClass.get(desPurchaseMarket.getPriceInfo().eClass());
					final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(desPurchaseMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}

					final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(desPurchaseMarket.getCv());

					final IMarkToMarket optMarket = builder.createDESPurchaseMTM(marketPorts, cargoCVValue, priceCalculator);
					entities.addModelObject(market, optMarket);

				}
			}
		}
	}

	private void buildDESSalesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof DESSalesMarket;
				if (market instanceof DESSalesMarket) {
					final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
					final Set<Port> portSet = Collections.singleton(desSalesMarket.getNotionalPort());

					final Set<IPort> marketPorts = new HashSet<IPort>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup((Port) ap);
						if (ip != null) {
							marketPorts.add(ip);
						}
					}

					final IContractTransformer transformer = contractTransformersByEClass.get(desSalesMarket.getPriceInfo().eClass());
					final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(desSalesMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}

					final IMarkToMarket optMarket = builder.createDESSalesMTM(marketPorts, priceCalculator);
					entities.addModelObject(market, optMarket);

				}
			}
		}
	}

	private void buildFOBSalesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof FOBSalesMarket;
				if (market instanceof FOBSalesMarket) {
					final FOBSalesMarket fobSalesMarket = (FOBSalesMarket) market;
					final Set<Port> portSet = SetUtils.getObjects(fobSalesMarket.getOriginPorts());

					final Set<IPort> marketPorts = new HashSet<IPort>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup((Port) ap);
						if (ip != null) {
							marketPorts.add(ip);
						}
					}

					final IContractTransformer transformer = contractTransformersByEClass.get(fobSalesMarket.getPriceInfo().eClass());
					final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(fobSalesMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}
					final IMarkToMarket optMarket = builder.createFOBSaleMTM(marketPorts, priceCalculator);
					entities.addModelObject(market, optMarket);
				}
			}
		}
	}

	private void buildFOBPurchasesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof FOBPurchasesMarket;
				if (market instanceof FOBPurchasesMarket) {
					final FOBPurchasesMarket fobPurchaseMarket = (FOBPurchasesMarket) market;
					final Set<Port> portSet = SetUtils.getObjects(fobPurchaseMarket.getMarketPorts());

					final Set<IPort> marketPorts = new HashSet<IPort>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup((Port) ap);
						if (ip != null) {
							marketPorts.add(ip);
						}
					}

					final IContractTransformer transformer = contractTransformersByEClass.get(fobPurchaseMarket.getPriceInfo().eClass());
					final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(fobPurchaseMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}
					final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(fobPurchaseMarket.getNotionalPort().getCvValue());

					final IMarkToMarket optMarket = builder.createFOBPurchaseMTM(marketPorts, cargoCVValue, priceCalculator);
					entities.addModelObject(market, optMarket);
				}
			}
		}
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
			final Association<VesselClass, IVesselClass> vesselAssociation, final ModelEntityMap entities) throws IncompleteScenarioException {

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create the default distance matrix.
		 */
		final PortModel portModel = rootObject.getPortModel();
		for (final Route r : portModel.getRoutes()) {
			// Store Route under it's name
			entities.addModelObject(r, r.getName());
			for (final RouteLine dl : r.getLines()) {
				IPort from, to;
				from = portAssociation.lookup(dl.getFrom());
				to = portAssociation.lookup(dl.getTo());

				final int distance = dl.getFullDistance();

				builder.setPortToPortDistance(from, to, r.getName(), distance);
			}

			// Set extra time and fuel consumption
			final FleetModel fleetModel = rootObject.getFleetModel();
			for (final VesselClass evc : fleetModel.getVesselClasses()) {
				for (final VesselClassRouteParameters routeParameters : evc.getRouteParameters()) {
					builder.setVesselClassRouteTransitTime(routeParameters.getRoute().getName(), vesselAssociation.lookup(evc), routeParameters.getExtraTransitTime());

					builder.setVesselClassRouteFuel(routeParameters.getRoute().getName(), vesselAssociation.lookup(evc), VesselState.Laden,
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getLadenConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getLadenNBORate()));

					builder.setVesselClassRouteFuel(routeParameters.getRoute().getName(), vesselAssociation.lookup(evc), VesselState.Ballast,
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getBallastConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getBallastNBORate()));

				}
			}

			// set tolls
			final PricingModel pm = rootObject.getPricingModel();
			for (final RouteCost routeCost : pm.getRouteCosts()) {
				final IVesselClass vesselClass = vesselAssociation.lookup(routeCost.getVesselClass());

				builder.setVesselClassRouteCost(routeCost.getRoute().getName(), vesselClass, VesselState.Laden, OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getLadenCost()));
				builder.setVesselClassRouteCost(routeCost.getRoute().getName(), vesselClass, VesselState.Ballast, OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost()));
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
			final Association<BaseFuelIndex, ICurve> baseFuelIndexAssociation, final Association<CharterIndex, ICurve> charterIndexAssociation, final ModelEntityMap entities) {

		/*
		 * Build the fleet model - first we must create the vessel classes from the model
		 */
		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<VesselClass, IVesselClass>();
		final Association<Vessel, IVessel> vesselAssociation = new Association<Vessel, IVessel>();
		// TODO: Check that we are mutliplying/dividing correctly to maintain
		// precision

		final FleetModel fleetModel = rootObject.getFleetModel();
		final PricingModel pricingModel = rootObject.getPricingModel();

		// look up prices

		for (final VesselClass eVc : fleetModel.getVesselClasses()) {
			int baseFuelPriceInInternalUnits = 0;
			for (final BaseFuelCost baseFuelCost : pricingModel.getFleetCost().getBaseFuelPrices()) {
				if (baseFuelCost.getFuel() == eVc.getBaseFuel()) {
					final BaseFuelIndex index = baseFuelCost.getIndex();
					final ICurve curve = baseFuelIndexAssociation.lookup(index);
					if (curve != null) {
						final EList<Date> dates = index.getData().getDates();
						final int point = dateHelper.convertTime(earliestTime, dates.get(0));
						baseFuelPriceInInternalUnits = curve.getValueAtPoint(point);
					}
					break;
				}
			}

			final IVesselClass vc = TransformerHelper.buildIVesselClass(builder, eVc, baseFuelPriceInInternalUnits);

			vesselClassAssociation.add(eVc, vc);

			/*
			 * set up inaccessible ports by applying resource allocation constraints
			 */
			final Set<IPort> inaccessiblePorts = new HashSet<IPort>();
			for (final Port ePort : SetUtils.getObjects(eVc.getInaccessiblePorts())) {
				inaccessiblePorts.add(portAssociation.lookup((Port) ePort));
			}

			if (inaccessiblePorts.isEmpty() == false) {
				builder.setVesselClassInaccessiblePorts(vc, inaccessiblePorts);
			}

			entities.addModelObject(eVc, vc);
		}

		final List<VesselAvailability> sortedAvailabilities = new ArrayList<VesselAvailability>();
		{
			final ScenarioFleetModel scenarioFleetModel = rootObject.getPortfolioModel().getScenarioFleetModel();
			for (final Vessel vessel : fleetModel.getVessels()) {

				for (final VesselAvailability vesselAvailability : scenarioFleetModel.getVesselAvailabilities()) {
					if (vesselAvailability.getVessel() == vessel) {
						sortedAvailabilities.add(vesselAvailability);
						break;
					}
				}
			}

		}
		/*
		 * Now create each vessel
		 */
		// for (final VesselAvailability vesselAvailability : fleetModel.getScenarioFleetModel().getVesselAvailabilities()) {
		for (final VesselAvailability vesselAvailability : sortedAvailabilities) {
			final Vessel eV = vesselAvailability.getVessel();

			vesselAvailabiltyMap.put(eV, vesselAvailability);

			final IStartEndRequirement startRequirement = createRequirement(builder, portAssociation, vesselAvailability.isSetStartAfter() ? vesselAvailability.getStartAfter() : null,
					vesselAvailability.isSetStartBy() ? vesselAvailability.getStartBy() : null, SetUtils.getObjects(vesselAvailability.getStartAt()));

			final IStartEndRequirement endRequirement = createRequirement(builder, portAssociation, vesselAvailability.isSetEndAfter() ? vesselAvailability.getEndAfter() : null,
					vesselAvailability.isSetEndBy() ? vesselAvailability.getEndBy() : null, SetUtils.getObjects(vesselAvailability.getEndAt()));

			// TODO: Hook up once charter out opt implemented
			final int dailyCharterInPrice = vesselAvailability.isSetTimeCharterRate() ? vesselAvailability.getTimeCharterRate() : 0;// vesselAssociation.lookup(eV).getHourlyCharterInPrice() * 24;

			final long heelLimit = vesselAvailability.getStartHeel().isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(vesselAvailability.getStartHeel().getVolumeAvailable())
					: 0;

			final int hourlyCharterInRate = (int) OptimiserUnitConvertor.convertToInternalHourlyCost(dailyCharterInPrice);
			final ICurve hourlyCharterInCurve = new ConstantValueCurve(hourlyCharterInRate);

			final IVessel vessel = builder.createVessel(eV.getName(), vesselClassAssociation.lookup(eV.getVesselClass()), hourlyCharterInCurve,
					vesselAvailability.isSetTimeCharterRate() ? VesselInstanceType.TIME_CHARTER : VesselInstanceType.FLEET, startRequirement, endRequirement, heelLimit,
					OptimiserUnitConvertor.convertToInternalConversionFactor(vesselAvailability.getStartHeel().getCvValue()),
					OptimiserUnitConvertor.convertToInternalPrice(vesselAvailability.getStartHeel().getPricePerMMBTU()),
					OptimiserUnitConvertor.convertToInternalVolume((int) (eV.getVesselOrVesselClassCapacity() * eV.getVesselOrVesselClassFillCapacity())));
			vesselAssociation.add(eV, vessel);

			entities.addModelObject(vesselAvailability, vessel);
			allVessels.add(vessel);

			/*
			 * set up inaccessible ports by applying resource allocation constraints
			 */
			final Set<IPort> inaccessiblePorts = new HashSet<IPort>();
			for (final Port ePort : SetUtils.getObjects(eV.getInaccessiblePorts())) {
				inaccessiblePorts.add(portAssociation.lookup((Port) ePort));
			}

			if (inaccessiblePorts.isEmpty() == false) {
				builder.setVesselInaccessiblePorts(vessel, inaccessiblePorts);
			}
		}

		{
			final SpotMarketsModel spotMarketsModel = rootObject.getSpotMarketsModel();
			int charterCount = 0;
			for (final CharterCostModel charterCost : spotMarketsModel.getCharteringSpotMarkets()) {

				for (final VesselClass eVc : charterCost.getVesselClasses()) {
					final ICurve charterInCurve;
					if (charterCost.getCharterInPrice() == null) {
						charterInCurve = new ConstantValueCurve(0);
					} else {
						charterInCurve = charterIndexAssociation.lookup(charterCost.getCharterInPrice());
						// charterInCurve = dateHelper.createCurveForIntegerIndex(charterCost.getCharterInPrice().getData(), 1.0f / 24.0f, false);
					}

					charterCount = charterCost.getSpotCharterCount();
					if (charterCount > 0) {
						final List<IVessel> spots = builder.createSpotVessels("SPOT-" + eVc.getName(), vesselClassAssociation.lookup(eVc), charterCount, charterInCurve);
						spotVesselsByClass.put(eVc, spots);
						allVessels.addAll(spots);
					}

					if (charterCost.getCharterOutPrice() != null) {
						// final ICurve charterOutCurve = dateHelper.createCurveForIntegerIndex(charterCost.getCharterOutPrice().getData(), 1.0f / 24.0f, false);
						final ICurve charterOutCurve = charterIndexAssociation.lookup(charterCost.getCharterOutPrice());
						final int minDuration = 24 * charterCost.getMinCharterOutDuration();
						builder.createCharterOutCurve(vesselClassAssociation.lookup(eVc), charterOutCurve, minDuration);
					}
				}
			}

		}
		//
		// /*
		// * Create spot charter vessels with no start/end requirements
		// */
		// for (final VesselClass eVc : fleetModel.getVesselClasses()) {
		// if (eVc.getSpotCharterCount() > 0) {
		// final List<IVessel> spots = builder.createSpotVessels("SPOT-" + eVc.getName(), vesselClassAssociation.lookup(eVc), eVc.getSpotCharterCount());
		// // TODO this is not necessarily ideal; if there is an initial
		// // solution set we associate all the spot vessels with ones in
		// // that solution.
		// int vesselIndex = 0;
		// if ((scenario.getOptimisation() != null) && (scenario.getOptimisation().getCurrentSettings() != null)) {
		// final Schedule initialSchedule = scenario.getOptimisation().getCurrentSettings().getInitialSchedule();
		// if (initialSchedule != null) {
		// for (final AllocatedVessel allocatedVessel : initialSchedule.getFleet()) {
		// if ((allocatedVessel instanceof SpotVessel) && (((SpotVessel) allocatedVessel).getVesselClass() == eVc)) {
		// // map it to one of the ones we have just made
		// assert vesselIndex < spots.size() : "Initial schedule should not have more spot vessels than fleet suggests";
		// entities.addModelObject(allocatedVessel, spots.get(vesselIndex));
		// vesselIndex++;
		// }
		// }
		// }
		// }
		// }
		// }

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
	private IStartEndRequirement createRequirement(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Date from, final Date to, final Set<Port> ports) {
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
			if (ports.size() > 1) {
				final Set<IPort> portSet = new HashSet<IPort>();
				for (final Port p : ports) {
					portSet.add(portAssociation.lookup(p));
				}
				if (window == null) {
					return builder.createStartEndRequirement(portSet);
				} else {
					return builder.createStartEndRequirement(portSet, window);
				}
			} else {
				final IPort port = portAssociation.lookup((Port) ports.iterator().next());
				if (window == null) {
					return builder.createStartEndRequirement(port);
				} else {
					return builder.createStartEndRequirement(port, window);
				}
			}
		}

		return builder.createStartEndRequirement();
	}

	/**
	 * Add the spot slot to the existing market spot slot counter.
	 * 
	 * @param spotSlot
	 */
	private void addSpotSlotToCount(final SpotSlot spotSlot) {
		final SpotMarket market = spotSlot.getMarket();
		final SpotType spotType;
		if (market instanceof DESPurchaseMarket) {
			spotType = SpotType.DES_PURCHASE;
		} else if (market instanceof DESSalesMarket) {
			spotType = SpotType.DES_SALE;
		} else if (market instanceof FOBPurchasesMarket) {
			spotType = SpotType.FOB_PURCHASE;
		} else if (market instanceof FOBSalesMarket) {
			spotType = SpotType.FOB_SALE;
		} else {
			log.warn("Spot slot with an invalid market found");
			return;
		}

		final TreeMap<String, Collection<Slot>> curve;
		if (existingSpotCount.containsKey(spotType)) {
			curve = existingSpotCount.get(spotType);
		} else {
			curve = new TreeMap<String, Collection<Slot>>();
			existingSpotCount.put(spotType, curve);
		}
		if (spotSlot instanceof Slot) {
			final Slot slot = (Slot) spotSlot;
			final String key = getKeyForDate(slot.getWindowStart());
			final Collection<Slot> slots;
			if (curve.containsKey(key)) {
				slots = curve.get(key);
			} else {
				slots = new LinkedList<Slot>();
				curve.put(key, slots);
			}
			slots.add(slot);
		}
	}

	private Collection<Slot> getSpotSlots(final SpotType spotType, final String key) {
		if (existingSpotCount.containsKey(spotType)) {
			final TreeMap<String, Collection<Slot>> curve = existingSpotCount.get(spotType);
			if (curve.containsKey(key)) {
				final Collection<Slot> slots = curve.get(key);
				if (slots != null) {
					return slots;
				}
			}
		}
		return Collections.emptyList();
	}

	private String getKeyForDate(final Date date) {
		final String key = new SimpleDateFormat("yyyy-MM").format(date);
		return key;
	}

	/**
	 * Convert a date into relative hours; returns the number of hours between windowStart and earliest.
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	private int convertTime(final Date earliest, final Date windowStart) {
		return dateHelper.convertTime(earliest, windowStart);
	}

	private int convertTime(final Date startTime) {
		return dateHelper.convertTime(earliestTime, startTime);
	}
}
