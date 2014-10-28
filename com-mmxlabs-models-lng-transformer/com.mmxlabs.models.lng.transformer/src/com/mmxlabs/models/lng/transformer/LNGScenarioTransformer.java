/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Named;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
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
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
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
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.TransformerHelper;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.ICancellationFeeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IHedgesProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;

/**
 * Wrapper for an EMF LNG Scheduling {@link MMXRootObject}, providing utility methods to convert it into an optimisation job. Typical usage is to construct an LNGScenarioTransformer with a given
 * Scenario, and then call the {@link createOptimisationData} method. It is only expected that an instance will be used once. I.e. a single call to {@link #createOptimisationData(ModelEntityMap)}
 * 
 * @author hinton, Simon Goodall
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

	@Inject
	private ISchedulerBuilder builder;

	@Inject
	private IShipToShipBindingProviderEditor shipToShipBindingProvider;

	@Inject
	private Injector injector;

	@Inject
	private TimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

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

	private final Map<VesselClass, List<IVesselAvailability>> spotVesselAvailabilitiesByClass = new HashMap<>();

	private final List<IVesselAvailability> allVesselAvailabilities = new ArrayList<IVesselAvailability>();

	private final Map<Vessel, IVessel> allVessels = new HashMap<>();
	private final Map<IVessel, Collection<IVesselAvailability>> vesselToAvailabilities = new HashMap<>();

	/**
	 * The {@link Set} of ID strings already used. The UI should restrict user entered data items from clashing, but generated ID's may well clash with user ones.
	 */
	private final Set<String> usedIDStrings = new HashSet<String>();

	/**
	 * A {@link Map} of existing spot market slots by ID. This map is used later when building the spot market options.
	 */
	private final Map<String, Slot> marketSlotsByID = new HashMap<String, Slot>();

	private final EnumMap<SpotType, TreeMap<String, Collection<Slot>>> existingSpotCount = new EnumMap<SpotType, TreeMap<String, Collection<Slot>>>(SpotType.class);

	private OptimiserSettings optimiserParameters;

	@Inject
	private IPortVisitDurationProviderEditor portVisitDurationProviderEditor;

	@Inject
	private IHedgesProviderEditor hedgesProviderEditor;

	@Inject
	private ICancellationFeeProviderEditor cancellationFeeProviderEditor;

	/**
	 * Create a transformer for the given scenario; the class holds a reference, so changes made to the scenario after construction will be reflected in calls to the various helper methods.
	 * 
	 * @param scenario
	 */
	@Inject
	public LNGScenarioTransformer(final LNGScenarioModel rootObject, final OptimiserSettings optimiserParameters) {

		init(rootObject, optimiserParameters);
	}

	/**
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
	 */
	public IOptimisationData createOptimisationData(final ModelEntityMap modelEntityMap) throws IncompleteScenarioException {
		/*
		 * Set reference for hour 0
		 */
		findEarliestAndLatestTimes();

		dateHelper.setEarliestTime(earliestTime);
		dateHelper.setEarliestTime(earliestTime);
		// set earliest and latest times into modelEntityMap
		modelEntityMap.setEarliestDate(earliestTime);
		modelEntityMap.setLatestDate(latestTime);

		timeZoneToUtcOffsetProvider.setTimeZeroInMillis(earliestTime.getTime());

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
						curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				modelEntityMap.addModelObject(index, curve);
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
						curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				modelEntityMap.addModelObject(index, curve);
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
					final long dailyCost = OptimiserUnitConvertor.convertToInternalDailyCost(parsed.evaluate(0).intValue());
					if (dailyCost != (int) dailyCost) {
						throw new IllegalStateException(String.format("Daily Cost of %d is too big.", OptimiserUnitConvertor.convertToExternalDailyCost(dailyCost)));
					}
					curve.setValueAfter(0, (int) dailyCost);
				} else {

					for (final int i : parsed.getChangePoints()) {
						final long dailyCost = OptimiserUnitConvertor.convertToInternalDailyCost(parsed.evaluate(i).intValue());
						if (dailyCost != (int) dailyCost) {
							throw new IllegalStateException(String.format("Daily Cost of %d is too big.", OptimiserUnitConvertor.convertToExternalDailyCost(dailyCost)));
						}
						curve.setValueAfter(i, (int) dailyCost);
					}
				}
				modelEntityMap.addModelObject(index, curve);
				charterIndexAssociation.add(index, curve);
			} catch (final Exception exception) {
				log.warn("Error evaluating series " + index.getName(), exception);
			}
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
			injector.injectMembers(cooldownCalculator);

			for (final Port port : SetUtils.getObjects(price.getPorts())) {
				cooldownCalculators.put(port, cooldownCalculator);
			}
		}

		for (final Port ePort : portModel.getPorts()) {
			final IPort port;
			if (ePort.getLocation() != null) {
				final Location loc = ePort.getLocation();
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort), (float) loc.getLat(), (float) loc.getLon(), ePort.getTimeZone());
			} else {
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort), ePort.getTimeZone());
			}

			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);
			modelEntityMap.addModelObject(ePort, port);

			builder.setPortCV(port, OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getCvValue()));

			// Set port default values
			portVisitDurationProviderEditor.setVisitDuration(port, PortType.Load, ePort.getLoadDuration());
			portVisitDurationProviderEditor.setVisitDuration(port, PortType.Discharge, ePort.getDischargeDuration());
		}

		// set up the contract transformers
		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.startTransforming(rootObject, modelEntityMap, builder);
		}

		final Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> vesselAssociations = buildFleet(builder, portAssociation, baseFuelIndexAssociation, charterIndexAssociation,
				modelEntityMap);

		final CommercialModel commercialModel = rootObject.getCommercialModel();

		// Any NPE's in the following code are likely due to missing associations between a IContractTransformer and the EMF AContract object. IContractTransformer instances are typically OSGi
		// services. Ensure their bundles have been started!
		for (final SalesContract c : commercialModel.getSalesContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.getPriceInfo().eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + c.getPriceInfo().eClass().getName());
			}
			final ISalesPriceCalculator calculator = transformer.transformSalesPriceParameters(c, c.getPriceInfo());
			if (calculator == null) {
				throw new IllegalStateException("Unable to transform contract");
			}
			modelEntityMap.addModelObject(c, calculator);
		}

		for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.getPriceInfo().eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + c.getPriceInfo().eClass().getName());
			}
			final ILoadPriceCalculator calculator = transformer.transformPurchasePriceParameters(c, c.getPriceInfo());
			modelEntityMap.addModelObject(c, calculator);
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
							final Set<IVessel> vessels = new HashSet<>();
							// Add all pyhsical vessels including reference vessels ...
							vessels.addAll(allVessels.values());
							// ... however this excludes the spot vessels, so add them from here
							for (final IVesselAvailability vesselAvailability : allVesselAvailabilities) {
								vessels.add(vesselAvailability.getVessel());
							}
							// Now create port costs for all the vessel instances.
							for (final IVessel v : vessels) {
								// TODO should the builder handle the application of costs to vessel classes?
								final VesselClass vesselClass = vesselAssociations.getFirst().reverseLookup(v.getVesselClass());
								final long activityCost = OptimiserUnitConvertor.convertToInternalFixedCost(cost.getPortCost(vesselClass, entry.getActivity()));
								builder.setPortCost(portAssociation.lookup((Port) port), v, type, activityCost);
							}

						}
					}
				}
			}
		}

		buildDistances(builder, portAssociation, allPorts, portIndices, vesselAssociations.getFirst(), modelEntityMap);

		buildCargoes(builder, portAssociation, vesselAssociations.getSecond(), contractTransformers, modelEntityMap, optimiserParameters.isShippingOnly());

		buildVesselEvents(builder, portAssociation, vesselAssociations.getFirst(), modelEntityMap);

		buildSpotCargoMarkets(builder, portAssociation, contractTransformers, modelEntityMap);

		buildMarkToMarkets(builder, portAssociation, contractTransformers, modelEntityMap);

		setNominatedVessels(builder, modelEntityMap);

		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.finishTransforming();
		}

		builder.setEarliestDate(earliestTime);

		final IOptimisationData optimisationData = builder.getOptimisationData();

		return optimisationData;
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

	private void setNominatedVessels(final ISchedulerBuilder builder, final ModelEntityMap modelEntityMap) {

		final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();
		if (cargoModel != null) {

			for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {

				final IPortSlot portSlot = modelEntityMap.getOptimiserObject(loadSlot, IPortSlot.class);
				final IVessel vessel = allVessels.get(loadSlot.getAssignment());
				if (vessel != null && portSlot != null) {
					builder.setNominatedVessel(portSlot, vessel);
				}
			}
			for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {

				final IPortSlot portSlot = modelEntityMap.getOptimiserObject(dischargeSlot, IPortSlot.class);
				final IVessel vessel = allVessels.get(dischargeSlot.getAssignment());
				if (vessel != null && portSlot != null) {
					builder.setNominatedVessel(portSlot, vessel);
				}
			}
		}
	}

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
		final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();

		final HashSet<Date> allDates = new HashSet<Date>();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {
			allDates.add(event.getStartBy());
			allDates.add(event.getStartAfter());
		}
		for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
			if (vesselAvailability.isSetStartBy())
				allDates.add(vesselAvailability.getStartBy());
			if (vesselAvailability.isSetStartAfter())
				allDates.add(vesselAvailability.getStartAfter());

			if (vesselAvailability.isSetEndBy())
				allDates.add(vesselAvailability.getEndBy());
			if (vesselAvailability.isSetEndAfter())
				allDates.add(vesselAvailability.getEndAfter());
		}
		for (final Slot s : cargoModel.getLoadSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}
		for (final Slot s : cargoModel.getDischargeSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}

		earliestTime = allDates.isEmpty() ? new Date(0) : Collections.min(allDates);
		latestTime = allDates.isEmpty() ? new Date(0) : Collections.max(allDates);
	}

	private void buildVesselEvents(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<VesselClass, IVesselClass> classes,
			final ModelEntityMap modelEntityMap) {

		final Date latestDate = optimiserParameters.getRange().isSetOptimiseBefore() ? optimiserParameters.getRange().getOptimiseBefore() : latestTime;

		final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {

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
				final long maxHeel = heelOptions.isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getVolumeAvailable()) : 0l;
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

			for (final Vessel eVessel : SetUtils.getObjects(event.getAllowedVessels())) {
				final IVessel vessel = allVessels.get(eVessel);
				if (vessel != null) {
					for (final IVesselAvailability vesselAvailability : vesselToAvailabilities.get(vessel)) {
						builder.addVesselEventVessel(builderSlot, vesselAvailability);
					}
				}
			}

			modelEntityMap.addModelObject(event, builderSlot);
		}
	}

	/**
	 * Extract the cargoes from the scenario and add them to the given builder.
	 * 
	 * @param builder
	 *            current builder. should already have ports/distances/vessels built
	 * @param indexAssociation
	 * @param contractTransformers
	 * @param modelEntityMap
	 * @param defaultRewiring
	 */
	private void buildCargoes(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<Vessel, IVessel> vesselAssociation,
			final Collection<IContractTransformer> contractTransformers, final ModelEntityMap modelEntityMap, final boolean shippingOnly) {

		// All Discharge ports - for use with
		final Set<IPort> allDischargePorts = new HashSet<IPort>();
		final Set<IPort> allLoadPorts = new HashSet<IPort>();
		{
			final Collection<Port> allModelObjects = modelEntityMap.getAllModelObjects(Port.class);
			for (final Port p : allModelObjects) {
				if (p.getCapabilities().contains(PortCapability.DISCHARGE)) {
					allDischargePorts.add(modelEntityMap.getOptimiserObject(p, IPort.class));
				}
				if (p.getCapabilities().contains(PortCapability.LOAD)) {
					allLoadPorts.add(modelEntityMap.getOptimiserObject(p, IPort.class));
				}
			}
		}

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
						final ILoadOption load = createLoadOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, loadSlot);
						usedLoadSlots.add(loadSlot);
						loadOptions.add(load);
						slotMap.put(loadSlot, load);
						slots.add(load);
					}

				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					{
						final IDischargeOption discharge = createDischargeOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, dischargeSlot);
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
					final ITimeWindow twForBinding = getTimeWindowForSlotBinding(loadSlot, load, portAssociation.lookup(loadSlot.getPort()));
					configureLoadSlotRestrictions(builder, portAssociation, allDischargePorts, loadSlot, load, twForBinding);
					isTransfer = (((LoadSlot) slot).getTransferFrom() != null);
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					final IDischargeOption discharge = (IDischargeOption) slotMap.get(dischargeSlot);
					final ITimeWindow twForBinding = getTimeWindowForSlotBinding(dischargeSlot, discharge, portAssociation.lookup(dischargeSlot.getPort()));
					configureDischargeSlotRestrictions(builder, allLoadPorts, dischargeSlot, discharge, twForBinding);
					isTransfer = (((DischargeSlot) slot).getTransferTo() != null);
				}

				// remember any slots which were part of a ship-to-ship transfer
				// but don't do anything with them yet, because we need to wait until all slots have been processed
				if (isTransfer) {
					transferSlotMap.put(slot, slotMap.get(slot));
				}
			}

			final ICargo cargo = builder.createCargo(slots, shippingOnly ? false : eCargo.isAllowRewiring());

			modelEntityMap.addModelObject(eCargo, cargo);
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
				load = createLoadOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, loadSlot);
				if (!loadSlot.isOptional()) {
					builder.setSoftRequired(load);
				}
			}

			configureLoadSlotRestrictions(builder, portAssociation, allDischargePorts, loadSlot, load, getTimeWindowForSlotBinding(loadSlot, load, portAssociation.lookup(loadSlot.getPort())));
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
				discharge = createDischargeOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, dischargeSlot);
				if (!dischargeSlot.isOptional()) {
					builder.setSoftRequired(discharge);
				}
			}

			configureDischargeSlotRestrictions(builder, allLoadPorts, dischargeSlot, discharge, getTimeWindowForSlotBinding(dischargeSlot, discharge, portAssociation.lookup(dischargeSlot.getPort())));
		}
	}

	private ITimeWindow getTimeWindowForSlotBinding(final Slot modelSlot, final IPortSlot optimiserSlot, final IPort port) {

		if (modelSlot instanceof SpotSlot) {

			final Date startTime = modelSlot.getWindowStartWithSlotOrPortTime();
			final Date endTime = modelSlot.getWindowEndWithSlotOrPortTime();
			// Convert port local external date/time into UTC based internal time units
			final int twStart = timeZoneToUtcOffsetProvider.UTC(convertTime(earliestTime, startTime), port);
			final int twEnd = timeZoneToUtcOffsetProvider.UTC(convertTime(earliestTime, endTime), port);
			// This should probably be fixed in ScheduleBuilder#matchingWindows and elsewhere if needed, but subtract one to avoid e.g. 1st Feb 00:00 being permitted in the Jan
			// month block
			final ITimeWindow twUTC = builder.createTimeWindow(twStart, Math.max(twStart, twEnd - 1));

			return twUTC;
		} else {
			return optimiserSlot.getTimeWindow();
		}

	}

	public void configureDischargeSlotRestrictions(final ISchedulerBuilder builder, final Set<IPort> allLoadPorts, final DischargeSlot dischargeSlot, final IDischargeOption discharge,
			final ITimeWindow twForSlotBinding) {
		if (dischargeSlot.isFOBSale()) {

			if (dischargeSlot instanceof SpotDischargeSlot) {
				final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
				for (final IPort port : allLoadPorts) {
					// Take the UTC based window and shift according to local port timezone
					final int twStart = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getStart(), port);
					final int twEnd = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getEnd(), port);
					marketPortsMap.put(port, new TimeWindow(twStart, twEnd));
				}

				builder.bindLoadSlotsToFOBSale(discharge, marketPortsMap);
			} else

			if (dischargeSlot.isDivertible()) {
				// Bind to all loads
				final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
				for (final IPort port : allLoadPorts) {
					marketPortsMap.put(port, twForSlotBinding);
				}

				builder.bindLoadSlotsToFOBSale(discharge, marketPortsMap);
			} else {
				// Bind to current port only
				final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
				marketPortsMap.put(discharge.getPort(), twForSlotBinding);
				builder.bindLoadSlotsToFOBSale(discharge, marketPortsMap);
			}
		}
	}

	public void configureLoadSlotRestrictions(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Set<IPort> allDischargePorts, final LoadSlot loadSlot,
			final ILoadOption load, final ITimeWindow twForSlotBinding) {

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

				final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
				for (final IPort port : marketPorts) {
					// Take the UTC based window and shift according to local port timezone
					final int twStart = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getStart(), port);
					final int twEnd = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getEnd(), port);
					marketPortsMap.put(port, new TimeWindow(twStart, twEnd));
				}

				// Bind FOB/DES slots to resource
				builder.bindDischargeSlotsToDESPurchase(load, marketPortsMap);
			} else {
				// Bind FOB/DES slots to resource
				if (loadSlot.isDivertible()) {

					// Bind to all discharges
					// Note: DES Diversion already take into account shipping days restriction
					final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
					for (final IPort port : allDischargePorts) {
						marketPortsMap.put(port, twForSlotBinding);
					}
					builder.bindDischargeSlotsToDESPurchase(load, marketPortsMap);
				} else {
					// Bind to current port only
					final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
					marketPortsMap.put(load.getPort(), twForSlotBinding);
					builder.bindDischargeSlotsToDESPurchase(load, marketPortsMap);
				}
			}
		}
	}

	private IDischargeOption createDischargeOption(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<Vessel, IVessel> vesselAssociation,
			final Collection<IContractTransformer> contractTransformers, final ModelEntityMap modelEntityMap, final DischargeSlot dischargeSlot) {
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
						curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				dischargePriceCalculator = new PriceExpressionContract(curve);
				injector.injectMembers(dischargePriceCalculator);
			}
		} else if (dischargeSlot instanceof SpotSlot) {
			final SpotSlot spotSlot = (SpotSlot) dischargeSlot;
			final SpotMarket market = spotSlot.getMarket();

			final IContractTransformer transformer = contractTransformersByEClass.get(market.getPriceInfo().eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + market.getPriceInfo().eClass().getName());
			}
			final ISalesPriceCalculator calculator = transformer.transformSalesPriceParameters(null, market.getPriceInfo());
			if (calculator == null) {
				throw new IllegalStateException("Unable to transform contract");
			}
			// TODO?
			// modelEntityMap.addModelObject(c, calculator);

			dischargePriceCalculator = calculator;
		} else if (dischargeSlot.isSetContract()) {
			dischargePriceCalculator = modelEntityMap.getOptimiserObject(dischargeSlot.getContract(), ISalesPriceCalculator.class);
		} else {
			dischargePriceCalculator = null;
		}

		if (dischargePriceCalculator == null) {
			throw new IllegalStateException("Discharge Slot has no contract or other pricing data");
		}

		// local scope for slot creation convenience variables
		{
			// convenience variables
			final String name = dischargeSlot.getName();
			final long minVolume;
			final long maxVolume;
			if (dischargeSlot instanceof SpotSlot) {
				final SpotSlot spotSlot = (SpotSlot) dischargeSlot;
				final SpotMarket market = spotSlot.getMarket();
				minVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity());
				maxVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity());
			} else {
				minVolume = OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMinQuantity());
				maxVolume = OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMaxQuantity());
			}

			final long minCv;
			long maxCv;

			final int pricingDate = getSlotPricingDate(dischargeSlot);

			minCv = OptimiserUnitConvertor.convertToInternalConversionFactor(dischargeSlot.getSlotOrContractMinCv());
			maxCv = OptimiserUnitConvertor.convertToInternalConversionFactor(dischargeSlot.getSlotOrContractMaxCv());
			if (maxCv == 0) {
				maxCv = Long.MAX_VALUE;
			}

			/*
			 * if (dischargeSlot.isSetContract()) { final SalesContract salesContract = (SalesContract) dischargeSlot.getContract();
			 * 
			 * if (salesContract.isSetMinCvValue()) { minCv = OptimiserUnitConvertor.convertToInternalConversionFactor(salesContract.getMinCvValue()); } else { minCv = 0; }
			 * 
			 * if (salesContract.isSetMaxCvValue()) { maxCv = OptimiserUnitConvertor.convertToInternalConversionFactor(salesContract.getMaxCvValue()); } else { maxCv = Long.MAX_VALUE; } } else { minCv
			 * = 0; maxCv = Long.MAX_VALUE; }
			 */

			if (dischargeSlot.isFOBSale()) {
				final ITimeWindow localTimeWindow;
				// if (dischargeSlot.isDivertable()) {
				// // Extend window out to cover whole shipping days restriction
				// localTimeWindow = builder.createTimeWindow(dischargeWindow.getStart() - dischargeSlot.getShippingDaysRestriction() * 24, dischargeWindow.getEnd());
				// } else

				if (dischargeSlot instanceof SpotDischargeSlot) {
					// Convert back into a UTC based date and add in TZ flex
					final int utcStart = timeZoneToUtcOffsetProvider.UTC(dischargeWindow.getStart(), portAssociation.lookup(dischargeSlot.getPort()));
					final int utcEnd = timeZoneToUtcOffsetProvider.UTC(dischargeWindow.getEnd(), portAssociation.lookup(dischargeSlot.getPort()));
					localTimeWindow = createUTCPlusTimeWindow(utcStart, utcEnd);
				} else {
					localTimeWindow = dischargeWindow;
				}
				final IPort port;
				if (dischargeSlot instanceof SpotSlot) {
					port = null;
				} else {
					port = portAssociation.lookup(dischargeSlot.getPort());
				}

				discharge = builder.createFOBSaleDischargeSlot(name, port, localTimeWindow, minVolume, maxVolume, minCv, maxCv, dischargePriceCalculator, dischargeSlot.getSlotOrPortDuration(),
						pricingDate, transformPricingEvent(dischargeSlot.getSlotOrDelegatedPricingEvent()), dischargeSlot.isOptional());

				if (dischargeSlot.isDivertible()) {
					// builder.setShippingHoursRestriction(discharge, dischargeWindow, dischargeSlot.getShippingDaysRestriction() * 24);
				}
			} else {
				discharge = builder.createDischargeSlot(name, portAssociation.lookup(dischargeSlot.getPort()), dischargeWindow, minVolume, maxVolume, minCv, maxCv, dischargePriceCalculator,
						dischargeSlot.getSlotOrPortDuration(), pricingDate, transformPricingEvent(dischargeSlot.getSlotOrDelegatedPricingEvent()), dischargeSlot.isOptional());
			}
		}

		if (dischargeSlot instanceof SpotSlot) {
			marketSlotsByID.put(dischargeSlot.getName(), dischargeSlot);
			addSpotSlotToCount((SpotSlot) dischargeSlot);
		}

		modelEntityMap.addModelObject(dischargeSlot, discharge);
		for (final IContractTransformer contractTransformer : contractTransformers) {
			contractTransformer.slotTransformed(dischargeSlot, discharge);
		}

		final long hedgeValue = OptimiserUnitConvertor.convertToInternalFixedCost(dischargeSlot.getHedges());
		if (hedgeValue != 0) {
			hedgesProviderEditor.setHedgeValue(discharge, hedgeValue);
		}

		final long cancellationFee = OptimiserUnitConvertor.convertToInternalFixedCost(dischargeSlot.getSlotOrContractCancellationFee());
		cancellationFeeProviderEditor.setCancellationFee(discharge, cancellationFee);

		final Set<Vessel> eAllowedVessels = SetUtils.getObjects(dischargeSlot.getAllowedVessels());
		if (!eAllowedVessels.isEmpty()) {
			final Set<IVesselAvailability> vesselsForSlot = new HashSet<>();
			for (final Vessel eVessel : eAllowedVessels) {
				final IVessel vessel = vesselAssociation.lookup(eVessel);
				if (vessel != null) {
					vesselsForSlot.addAll(vesselToAvailabilities.get(vessel));
				}
			}
			builder.setSlotVesselAvailabilityRestriction(discharge, vesselsForSlot);
		}
		return discharge;
	}

	private ILoadOption createLoadOption(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<Vessel, IVessel> vesselAssociation,
			final Collection<IContractTransformer> contractTransformers, final ModelEntityMap modelEntityMap, final LoadSlot loadSlot) {
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
						curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
					}
				}
				loadPriceCalculator = new PriceExpressionContract(curve);
				injector.injectMembers(loadPriceCalculator);

			}

		} else if (loadSlot instanceof SpotSlot) {
			final SpotSlot spotSlot = (SpotSlot) loadSlot;
			final SpotMarket market = spotSlot.getMarket();

			final IContractTransformer transformer = contractTransformersByEClass.get(market.getPriceInfo().eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + market.getPriceInfo().eClass().getName());
			}
			final ILoadPriceCalculator calculator = transformer.transformPurchasePriceParameters(null, market.getPriceInfo());
			if (calculator == null) {
				throw new IllegalStateException("Unable to transform contract");
			}
			// TODO?
			// modelEntityMap.addModelObject(c, calculator);

			loadPriceCalculator = calculator;

		} else if (loadSlot.isSetContract()) {
			final PurchaseContract purchaseContract = (PurchaseContract) (loadSlot.getContract());
			loadPriceCalculator = modelEntityMap.getOptimiserObject(purchaseContract, ILoadPriceCalculator.class);
		} else {
			loadPriceCalculator = null;
		}

		if (loadPriceCalculator == null) {
			throw new IllegalStateException("Load Slot has no contract or other pricing data");
		}

		final int slotPricingDate = getSlotPricingDate(loadSlot);

		final long minVolume;
		final long maxVolume;
		if (loadSlot instanceof SpotSlot) {
			final SpotSlot spotSlot = (SpotSlot) loadSlot;
			final SpotMarket market = spotSlot.getMarket();
			minVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity());
			maxVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity());
		} else {
			minVolume = OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMinQuantity());
			maxVolume = OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMaxQuantity());
		}

		if (loadSlot.isDESPurchase()) {
			final ITimeWindow localTimeWindow;
			if (loadSlot.isDivertible()) {
				// Extend window out to cover whole shipping days restriction
				localTimeWindow = builder.createTimeWindow(loadWindow.getStart(), loadWindow.getEnd() + loadSlot.getShippingDaysRestriction() * 24);
			} else if (loadSlot instanceof SpotLoadSlot) {
				// Convert back into a UTC based date and add in TZ flex
				final int utcStart = timeZoneToUtcOffsetProvider.UTC(loadWindow.getStart(), portAssociation.lookup(loadSlot.getPort()));
				final int utcEnd = timeZoneToUtcOffsetProvider.UTC(loadWindow.getEnd(), portAssociation.lookup(loadSlot.getPort()));
				localTimeWindow = createUTCPlusTimeWindow(utcStart, utcEnd);
			} else {
				localTimeWindow = loadWindow;
			}
			final IPort port;
			if (loadSlot instanceof SpotSlot) {
				port = null;
			} else {
				port = portAssociation.lookup(loadSlot.getPort());
			}
			load = builder.createDESPurchaseLoadSlot(loadSlot.getName(), port, localTimeWindow, minVolume, maxVolume, loadPriceCalculator,
					OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrDelegatedCV()), loadSlot.getSlotOrPortDuration(), slotPricingDate,
					transformPricingEvent(loadSlot.getSlotOrDelegatedPricingEvent()), loadSlot.isOptional());

			if (loadSlot.isDivertible()) {
				builder.setShippingHoursRestriction(load, loadWindow, loadSlot.getShippingDaysRestriction() * 24);
			}
		} else {
			load = builder.createLoadSlot(loadSlot.getName(), portAssociation.lookup(loadSlot.getPort()), loadWindow, minVolume, maxVolume, loadPriceCalculator,
					OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrDelegatedCV()), loadSlot.getSlotOrPortDuration(), loadSlot.isSetArriveCold(), loadSlot.isArriveCold(),
					slotPricingDate, transformPricingEvent(loadSlot.getSlotOrDelegatedPricingEvent()), loadSlot.isOptional());
		}
		// Store market slots for lookup when building spot markets.
		modelEntityMap.addModelObject(loadSlot, load);

		for (final IContractTransformer contractTransformer : contractTransformers) {
			contractTransformer.slotTransformed(loadSlot, load);
		}
		if (loadSlot instanceof SpotSlot) {
			marketSlotsByID.put(loadSlot.getName(), loadSlot);
			addSpotSlotToCount((SpotSlot) loadSlot);
		}

		final long hedgeCost = OptimiserUnitConvertor.convertToInternalFixedCost(loadSlot.getHedges());
		if (hedgeCost != 0) {
			hedgesProviderEditor.setHedgeValue(load, hedgeCost);
		}

		final long cancellationFee = OptimiserUnitConvertor.convertToInternalFixedCost(loadSlot.getSlotOrContractCancellationFee());
		cancellationFeeProviderEditor.setCancellationFee(load, cancellationFee);

		final Set<Vessel> eAllowedVessels = SetUtils.getObjects(loadSlot.getAllowedVessels());
		if (!eAllowedVessels.isEmpty()) {
			final Set<IVesselAvailability> vesselsForSlot = new HashSet<>();
			for (final Vessel eVessel : eAllowedVessels) {
				final IVessel vessel = vesselAssociation.lookup(eVessel);
				assert vessel != null;
				if (vessel != null) {
					vesselsForSlot.addAll(vesselToAvailabilities.get(vessel));
				}
			}
			builder.setSlotVesselAvailabilityRestriction(load, vesselsForSlot);
		}
		return load;
	}

	private void buildSpotCargoMarkets(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap) {

		final SpotMarketsModel spotMarketsModel = rootObject.getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}
		final Date earliestDate = optimiserParameters.getRange().isSetOptimiseAfter() ? optimiserParameters.getRange().getOptimiseAfter() : earliestTime;
		final Date latestDate = optimiserParameters.getRange().isSetOptimiseBefore() ? optimiserParameters.getRange().getOptimiseBefore() : latestTime;

		buildDESPurchaseSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getDesPurchaseSpotMarket());
		buildDESSalesSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getDesSalesSpotMarket());
		buildFOBPurchaseSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getFobPurchasesSpotMarket());
		buildFOBSalesSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getFobSalesSpotMarket());

	}

	private void buildDESPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final Date earliestDate, final Date latestDate, final SpotMarketGroup desPurchaseSpotMarket) {
		if (desPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = desPurchaseSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			cal.setTime(earliestDate);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTimeZone(), cal.getTime());

				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : desPurchaseSpotMarket.getMarkets()) {
					assert market instanceof DESPurchaseMarket;
					if (market instanceof DESPurchaseMarket && market.isEnabled() == true) {
						final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
						final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
						final Set<IPort> marketPorts = new HashSet<IPort>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup((Port) ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot> existing = getSpotSlots(SpotType.DES_PURCHASE, getKeyForDate(cal.getTimeZone(), startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(desPurchaseMarket.getPriceInfo().eClass());
						final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, desPurchaseMarket.getPriceInfo());

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								// As we have no port we create two timewindows. One is pure UTC which we base the EMF Slot date on and shift for the slot binding. The second is UTC with a +/- 12 flex
								// for timezones passed into the optimiser slot. This combination allows the slot to be matched against any slot in the same month in any timezone, but be restricted to
								// match the month boundary in that timezone.

								// This should probably be fixed in ScheduleBuilder#matchingWindows and elsewhere if needed, but subtract one to avoid e.g. 1st Feb 00:00 being permitted in the Jan
								// month block
								final ITimeWindow twUTC = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime) - 1);
								final ITimeWindow twUTCPlus = createUTCPlusTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(desPurchaseMarket.getCv());

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								// Avoid ID clash
								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final ILoadOption desPurchaseSlot = builder.createDESPurchaseLoadSlot(id, null, twUTCPlus, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, 0, IPortSlot.NO_PRICING_DATE,
										transformPricingEvent(market.getPricingEvent()), true);

								// Create a fake model object to add in here;
								final SpotLoadSlot desSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								desSlot.setDESPurchase(true);
								desSlot.setName(id);
								desSlot.setArriveCold(false);
								desSlot.setWindowStart(new Date(startTime.getTime()));
								desSlot.setWindowStartTime(0);
								// desSlot.setContract(desPurchaseMarket.getContract());
								desSlot.setOptional(true);
								final long duration = Math.max(0, (endTime.getTime() - startTime.getTime()) / 1000 / 60 / 60);
								desSlot.setWindowSize((int) duration);
								// Key piece of information
								desSlot.setMarket(desPurchaseMarket);
								modelEntityMap.addModelObject(desSlot, desPurchaseSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(desSlot, desPurchaseSlot);
								}

								final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
								for (final IPort port : marketPorts) {
									// Use the UTC based time window
									final int twStart = timeZoneToUtcOffsetProvider.localTime(twUTC.getStart(), port);
									final int twEnd = timeZoneToUtcOffsetProvider.localTime(twUTC.getEnd(), port);
									marketPortsMap.put(port, new TimeWindow(twStart, twEnd));
								}
								builder.bindDischargeSlotsToDESPurchase(desPurchaseSlot, marketPortsMap);

								marketSlots.add(desPurchaseSlot);
								marketGroupSlots.add(desPurchaseSlot);
							}
						}
						builder.createSlotGroupCount(marketSlots, count);
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						// Disabled until UI available
						// builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private void buildFOBSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final Date earliestDate, final Date latestDate, final SpotMarketGroup fobSalesSpotMarket) {
		if (fobSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = fobSalesSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			cal.setTime(earliestDate);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTimeZone(), cal.getTime());
				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : fobSalesSpotMarket.getMarkets()) {
					assert market instanceof FOBSalesMarket;
					if (market instanceof FOBPurchasesMarket && market.isEnabled() == true) {
						final FOBSalesMarket fobSaleMarket = (FOBSalesMarket) market;
						final Set<Port> portSet = SetUtils.getObjects(fobSaleMarket.getOriginPorts());
						final Set<IPort> marketPorts = new HashSet<IPort>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup((Port) ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot> existing = getSpotSlots(SpotType.FOB_SALE, getKeyForDate(cal.getTimeZone(), startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(fobSaleMarket.getPriceInfo().eClass());
						final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(null, fobSaleMarket.getPriceInfo());

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {
								// As we have no port we create two timewindows. One is pure UTC which we base the EMF Slot date on and shift for the slot binding. The second is UTC with a +/- 12 flex
								// for timezones passed into the optimiser slot. This combination allows the slot to be matched against any slot in the same month in any timezone, but be restricted to
								// match the month boundary in that timezone.

								// This should probably be fixed in ScheduleBuilder#matchingWindows and elsewhere if needed, but subtract one to avoid e.g. 1st Feb 00:00 being permitted in the Jan
								// month block
								final ITimeWindow twUTC = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime) - 1);
								final ITimeWindow twUTCPlus = createUTCPlusTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final long minCv = 0;
								final long maxCv = Long.MAX_VALUE;

								final IDischargeOption fobSaleSlot = builder.createFOBSaleDischargeSlot(id, null, twUTCPlus, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), minCv, maxCv, priceCalculator, 0, IPortSlot.NO_PRICING_DATE,
										transformPricingEvent(market.getPricingEvent()), true);

								// Create a fake model object to add in here;
								final SpotDischargeSlot fobSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								fobSlot.setFOBSale(true);
								fobSlot.setName(id);
								fobSlot.setWindowStart(new Date(startTime.getTime()));
								fobSlot.setWindowStartTime(0);
								// fobSlot.setContract(fobSaleMarket.getContract());
								fobSlot.setOptional(true);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000 / 60 / 60;
								fobSlot.setWindowSize((int) duration);
								// Key piece of information
								fobSlot.setMarket(fobSaleMarket);
								modelEntityMap.addModelObject(fobSlot, fobSaleSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(fobSlot, fobSaleSlot);
								}

								final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
								for (final IPort port : marketPorts) {
									// Use the UTC based time window
									final int twStart = timeZoneToUtcOffsetProvider.localTime(twUTC.getStart(), port);
									final int twEnd = timeZoneToUtcOffsetProvider.localTime(twUTC.getEnd(), port);
									marketPortsMap.put(port, new TimeWindow(twStart, twEnd));
								}

								builder.bindLoadSlotsToFOBSale(fobSaleSlot, marketPortsMap);

								marketSlots.add(fobSaleSlot);
								marketGroupSlots.add(fobSaleSlot);
							}
						}

						builder.createSlotGroupCount(marketSlots, count);
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						// builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	/**
	 * Given a UTC based time window, extend it's range to cover the whole range of possible UTC offsets from UTC-12 to UTC+14
	 * 
	 * @param builder
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private ITimeWindow createUTCPlusTimeWindow(final int startTime, final int endTime) {
		return builder.createTimeWindow(startTime - 12, endTime + 14);
	}

	private void buildDESSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final Date earliestDate, final Date latestDate, final SpotMarketGroup desSalesSpotMarket) {
		if (desSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = desSalesSpotMarket.getAvailability();

			final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

			for (final SpotMarket market : desSalesSpotMarket.getMarkets()) {
				assert market instanceof DESSalesMarket;
				if (market instanceof DESSalesMarket && market.isEnabled() == true) {
					final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
					final Port notionalAPort = desSalesMarket.getNotionalPort();
					final IPort notionalIPort = portAssociation.lookup((Port) notionalAPort);

					// Loop over the date range in the optimisation generating market slots
					final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(notionalAPort.getTimeZone()));
					cal.setTime(earliestDate);

					// Set back to start of month
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);

					Date startTime = cal.getTime();
					while (startTime.before(latestDate)) {

						final String yearMonthString = getKeyForDate(cal.getTimeZone(), cal.getTime());
						// Roll forward
						cal.add(Calendar.MONTH, 1);
						final Date endTime = cal.getTime();

						final Collection<Slot> existing = getSpotSlots(SpotType.DES_SALE, getKeyForDate(cal.getTimeZone(), startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(desSalesMarket.getPriceInfo().eClass());
						final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(null, desSalesMarket.getPriceInfo());

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
								desSlot.setWindowStartTime(0);
								// desSlot.setContract(desSalesMarket.getContract());
								desSlot.setOptional(true);
								desSlot.setPort((Port) notionalAPort);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000l / 60l / 60l;
								desSlot.setWindowSize((int) duration);

								final int pricingDate = getSlotPricingDate(desSlot);

								final long minVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity());
								final long maxVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity());

								final IDischargeOption desSalesSlot = builder.createDischargeSlot(id, notionalIPort, tw, minVolume, maxVolume, 0, Long.MAX_VALUE, priceCalculator,
										desSlot.getSlotOrPortDuration(), pricingDate, transformPricingEvent(market.getPricingEvent()), true);

								// Key piece of information
								desSlot.setMarket(desSalesMarket);
								modelEntityMap.addModelObject(desSlot, desSalesSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(desSlot, desSalesSlot);
								}

								marketSlots.add(desSalesSlot);
								marketGroupSlots.add(desSalesSlot);
							}
						}
						builder.createSlotGroupCount(marketSlots, count);

						// Take group availability curve and add into a constraint.
						if (groupAvailability != null) {
							final int groupCount = getAvailabilityForDate(groupAvailability, startTime);
							if (marketGroupSlots.size() > groupCount) {
								// builder.createSlotGroupCount(marketGroupSlots, count);
							}
						}

						startTime = cal.getTime();
					}
				}
			}
		}
	}

	private void buildFOBPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final Date earliestDate, final Date latestDate, final SpotMarketGroup fobPurchaseSpotMarket) {
		if (fobPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = fobPurchaseSpotMarket.getAvailability();

			final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

			for (final SpotMarket market : fobPurchaseSpotMarket.getMarkets()) {
				assert market instanceof FOBPurchasesMarket;
				if (market instanceof FOBPurchasesMarket && market.isEnabled() == true) {
					final FOBPurchasesMarket fobPurchaseMarket = (FOBPurchasesMarket) market;
					final Port notionalAPort = fobPurchaseMarket.getNotionalPort();
					final IPort notionalIPort = portAssociation.lookup((Port) notionalAPort);
					// Loop over the date range in the optimisation generating market slots
					final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(notionalAPort.getTimeZone()));
					cal.setTime(earliestDate);

					// Set back to start of month
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);

					Date startTime = cal.getTime();
					while (startTime.before(latestDate)) {

						final String yearMonthString = getKeyForDate(cal.getTimeZone(), cal.getTime());

						// Roll forward
						cal.add(Calendar.MONTH, 1);
						final Date endTime = cal.getTime();
						final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(fobPurchaseMarket.getCv());

						final Collection<Slot> existing = getSpotSlots(SpotType.FOB_PURCHASE, getKeyForDate(cal.getTimeZone(), startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final IContractTransformer transformer = contractTransformersByEClass.get(fobPurchaseMarket.getPriceInfo().eClass());
						final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, fobPurchaseMarket.getPriceInfo());

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
								fobSlot.setWindowStartTime(0);
								// fobSlot.setContract(fobPurchaseMarket.getContract());
								fobSlot.setOptional(true);
								fobSlot.setArriveCold(true);
								// fobSlot.setCargoCV(fobPurchaseMarket.getCv());
								fobSlot.setPort((Port) notionalAPort);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000l / 60l / 60l;
								fobSlot.setWindowSize((int) duration);

								final ILoadOption fobPurchaseSlot = builder.createLoadSlot(id, notionalIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, fobSlot.getSlotOrPortDuration(), true, true,
										IPortSlot.NO_PRICING_DATE, transformPricingEvent(market.getPricingEvent()), true);

								// Key piece of information
								fobSlot.setMarket(fobPurchaseMarket);
								modelEntityMap.addModelObject(fobSlot, fobPurchaseSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(fobSlot, fobPurchaseSlot);
								}

								marketSlots.add(fobPurchaseSlot);
								marketGroupSlots.add(fobPurchaseSlot);
							}
						}
						builder.createSlotGroupCount(marketSlots, count);

						// Take group availability curve and add into a constraint.
						if (groupAvailability != null) {
							final int groupCount = getAvailabilityForDate(groupAvailability, startTime);
							if (marketGroupSlots.size() > groupCount) {
								// builder.createSlotGroupCount(marketGroupSlots, count);
							}
						}

						startTime = cal.getTime();
					}
				}
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
			final ModelEntityMap modelEntityMap) {

		final SpotMarketsModel spotMarketsModel = rootObject.getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}

		buildDESPurchaseMarkToMarket(builder, portAssociation, contractTransformers, modelEntityMap, spotMarketsModel.getDesPurchaseSpotMarket());
		buildDESSalesMarkToMarket(builder, portAssociation, contractTransformers, modelEntityMap, spotMarketsModel.getDesSalesSpotMarket());
		buildFOBSalesMarkToMarket(builder, portAssociation, contractTransformers, modelEntityMap, spotMarketsModel.getFobSalesSpotMarket());
		buildFOBPurchasesMarkToMarket(builder, portAssociation, contractTransformers, modelEntityMap, spotMarketsModel.getFobPurchasesSpotMarket());
	}

	private void buildDESPurchaseMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final SpotMarketGroup marketGroup) {
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
					final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, desPurchaseMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}

					final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(desPurchaseMarket.getCv());

					final IMarkToMarket optMarket = builder.createDESPurchaseMTM(marketPorts, cargoCVValue, priceCalculator, modelEntityMap.getOptimiserObject(market.getEntity(), IEntity.class));
					modelEntityMap.addModelObject(market, optMarket);

				}
			}
		}
	}

	private void buildDESSalesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final SpotMarketGroup marketGroup) {
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
					final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(null, desSalesMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}

					final IMarkToMarket optMarket = builder.createDESSalesMTM(marketPorts, priceCalculator, modelEntityMap.getOptimiserObject(market.getEntity(), IEntity.class));
					modelEntityMap.addModelObject(market, optMarket);

				}
			}
		}
	}

	private void buildFOBSalesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final SpotMarketGroup marketGroup) {
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
					final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(null, fobSalesMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}
					final IMarkToMarket optMarket = builder.createFOBSaleMTM(marketPorts, priceCalculator, modelEntityMap.getOptimiserObject(market.getEntity(), IEntity.class));
					modelEntityMap.addModelObject(market, optMarket);
				}
			}
		}
	}

	private void buildFOBPurchasesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final SpotMarketGroup marketGroup) {
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
					final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, fobPurchaseMarket.getPriceInfo());
					if (priceCalculator == null) {
						throw new IllegalStateException("No valid price calculator found");
					}
					final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(fobPurchaseMarket.getNotionalPort().getCvValue());

					final IMarkToMarket optMarket = builder.createFOBPurchaseMTM(marketPorts, cargoCVValue, priceCalculator, modelEntityMap.getOptimiserObject(market.getEntity(), IEntity.class));
					modelEntityMap.addModelObject(market, optMarket);
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
			final Association<VesselClass, IVesselClass> vesselAssociation, final ModelEntityMap modelEntityMap) throws IncompleteScenarioException {

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create the default distance matrix.
		 */
		final PortModel portModel = rootObject.getPortModel();
		for (final Route r : portModel.getRoutes()) {
			// Store Route under it's name
			modelEntityMap.addModelObject(r, r.getName());
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
							OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getLadenConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getLadenNBORate()));

					builder.setVesselClassRouteFuel(routeParameters.getRoute().getName(), vesselAssociation.lookup(evc), VesselState.Ballast,
							OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getBallastConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getBallastNBORate()));

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
	 * @param modelEntityMap
	 * @return
	 */
	private Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> buildFleet(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation,
			final Association<BaseFuelIndex, ICurve> baseFuelIndexAssociation, final Association<CharterIndex, ICurve> charterIndexAssociation, final ModelEntityMap modelEntityMap) {

		/*
		 * Build the fleet model - first we must create the vessel classes from the model
		 */
		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<VesselClass, IVesselClass>();
		final Association<Vessel, IVessel> vesselAssociation = new Association<Vessel, IVessel>();
		final Association<VesselAvailability, IVesselAvailability> vesselAvailabilityAssociation = new Association<VesselAvailability, IVesselAvailability>();
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

			modelEntityMap.addModelObject(eVc, vc);
		}

		// Sorted by fleet model vessel order
		final List<VesselAvailability> sortedAvailabilities = new ArrayList<>();
		{
			final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();
			for (final Vessel vessel : fleetModel.getVessels()) {

				for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
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

		for (final Vessel eVessel : fleetModel.getVessels()) {

			final IVessel vessel = builder.createVessel(eVessel.getName(), vesselClassAssociation.lookup(eVessel.getVesselClass()),
					OptimiserUnitConvertor.convertToInternalVolume((int) (eVessel.getVesselOrVesselClassCapacity() * eVessel.getVesselOrVesselClassFillCapacity())));
			vesselAssociation.add(eVessel, vessel);

			modelEntityMap.addModelObject(eVessel, vessel);
			allVessels.put(eVessel, vessel);

			/*
			 * set up inaccessible ports by applying resource allocation constraints
			 */
			final Set<IPort> inaccessiblePorts = new HashSet<IPort>();
			for (final Port ePort : SetUtils.getObjects(eVessel.getInaccessiblePorts())) {
				inaccessiblePorts.add(portAssociation.lookup((Port) ePort));
			}

			if (inaccessiblePorts.isEmpty() == false) {
				builder.setVesselInaccessiblePorts(vessel, inaccessiblePorts);
			}

			vesselToAvailabilities.put(vessel, new LinkedList<IVesselAvailability>());
		}

		// Now register the availabilities.
		for (final VesselAvailability eVesselAvailability : sortedAvailabilities) {
			final Vessel eVessel = eVesselAvailability.getVessel();

			Set<Port> portSet = SetUtils.getObjects(eVesselAvailability.getStartAt());
			Port startingPort = portSet.isEmpty() ? null : portSet.iterator().next();
			final IStartRequirement startRequirement = createStartRequirement(builder, portAssociation, eVesselAvailability.isSetStartAfter() ? eVesselAvailability.getStartAfter() : null,
					eVesselAvailability.isSetStartBy() ? eVesselAvailability.getStartBy() : null, startingPort, eVesselAvailability.getStartHeel());

			boolean endCold = false;
			long targetEndHeelInM3 = 0;
			EndHeelOptions endHeel = eVesselAvailability.getEndHeel();
			if (endHeel != null) {
				endCold = endHeel.isEndCold();
				targetEndHeelInM3 = endCold ? OptimiserUnitConvertor.convertToInternalVolume(endHeel.getTargetEndHeel()) : 0;
			}

			final IEndRequirement endRequirement = createEndRequirement(builder, portAssociation, eVesselAvailability.isSetEndAfter() ? eVesselAvailability.getEndAfter() : null,
					eVesselAvailability.isSetEndBy() ? eVesselAvailability.getEndBy() : null, SetUtils.getObjects(eVesselAvailability.getEndAt()), endCold, targetEndHeelInM3);

			final ICurve dailyCharterInCurve;
			if (eVesselAvailability.isSetTimeCharterRate()) {
				dailyCharterInCurve = dateHelper.generateCharterExpressionCurve(eVesselAvailability.getTimeCharterRate(), charterIndices);
			} else {
				dailyCharterInCurve = new ConstantValueCurve(0);
			}

			final IVessel vessel = vesselAssociation.lookup(eVessel);
			assert vessel != null;

			final IVesselAvailability vesselAvailability = builder.createVesselAvailability(vessel, dailyCharterInCurve, eVesselAvailability.isSetTimeCharterRate() ? VesselInstanceType.TIME_CHARTER
					: VesselInstanceType.FLEET, startRequirement, endRequirement);
			vesselAvailabilityAssociation.add(eVesselAvailability, vesselAvailability);

			modelEntityMap.addModelObject(eVesselAvailability, vesselAvailability);

			allVesselAvailabilities.add(vesselAvailability);

			vesselToAvailabilities.get(vessel).add(vesselAvailability);
		}

		// Spot market generation
		{
			final SpotMarketsModel spotMarketsModel = rootObject.getSpotMarketsModel();
			int charterCount = 0;

			final CharterOutStartDate charterOutStartDate = spotMarketsModel.getCharterOutStartDate();
			if (charterOutStartDate != null && charterOutStartDate.getCharterOutStartDate() != null) {
				builder.setGeneratedCharterOutStartTime(dateHelper.convertTime(charterOutStartDate.getCharterOutStartDate()));
			} else {
				builder.setGeneratedCharterOutStartTime(0);
			}

			for (final CharterCostModel charterCost : spotMarketsModel.getCharteringSpotMarkets()) {

				if (!charterCost.isEnabled()) {
					continue;
				}

				for (final VesselClass eVesselClass : charterCost.getVesselClasses()) {
					final ICurve charterInCurve;
					if (charterCost.getCharterInPrice() == null) {
						charterInCurve = new ConstantValueCurve(0);
					} else {
						charterInCurve = charterIndexAssociation.lookup(charterCost.getCharterInPrice());
					}

					charterCount = charterCost.getSpotCharterCount();
					if (charterCount > 0) {
						final List<IVesselAvailability> spots = builder.createSpotVessels("SPOT-" + eVesselClass.getName(), vesselClassAssociation.lookup(eVesselClass), charterCount, charterInCurve);
						spotVesselAvailabilitiesByClass.put(eVesselClass, spots);
						allVesselAvailabilities.addAll(spots);
					}

					if (charterCost.getCharterOutPrice() != null) {
						final ICurve charterOutCurve = charterIndexAssociation.lookup(charterCost.getCharterOutPrice());
						final int minDuration = 24 * charterCost.getMinCharterOutDuration();
						builder.createCharterOutCurve(vesselClassAssociation.lookup(eVesselClass), charterOutCurve, minDuration);
					}
				}
			}
		}

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
	private IStartRequirement createStartRequirement(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Date from, final Date to, final Port port,
			HeelOptions eHeelOptions) {
		ITimeWindow window = null;

		if (from == null && to != null) {
			window = builder.createTimeWindow(convertTime(earliestTime), convertTime(to));
		} else if (from != null && to == null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(latestTime));
		} else if (from != null && to != null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(to));
		}

		IHeelOptions heelOptions;
		if (eHeelOptions != null) {
			final long heelLimitInM3 = eHeelOptions.isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(eHeelOptions.getVolumeAvailable()) : 0;

			int cvValue = OptimiserUnitConvertor.convertToInternalConversionFactor(eHeelOptions.getCvValue());
			int pricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(eHeelOptions.getPricePerMMBTU());

			heelOptions = builder.createHeelOptions(heelLimitInM3, cvValue, pricePerMMBTu);
		} else {
			heelOptions = null;
		}
		return builder.createStartRequirement(portAssociation.lookup(port), window, heelOptions);
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param pat
	 * @return
	 */
	private IEndRequirement createEndRequirement(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Date from, final Date to, final Set<Port> ports,
			boolean endCold, long targetHeelInM3) {
		ITimeWindow window = null;

		if (from == null && to != null) {
			window = builder.createTimeWindow(convertTime(earliestTime), convertTime(to));
		} else if (from != null && to == null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(latestTime));
		} else if (from != null && to != null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(to));
		}

		final Set<IPort> portSet = new HashSet<IPort>();
		for (final Port p : ports) {
			portSet.add(portAssociation.lookup(p));
		}
		if (ports.isEmpty()) {
			return builder.createEndRequirement(null, window, endCold, targetHeelInM3);
		} else {
			return builder.createEndRequirement(portSet, window, endCold, targetHeelInM3);
		}

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
			final String key = getKeyForDate(TimeZone.getTimeZone(slot.getPort().getTimeZone()), slot.getWindowStart());
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

	private String getKeyForDate(final TimeZone zone, final Date date) {
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		df.setTimeZone(zone);
		final String key = df.format(date);
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

	private PricingEventType transformPricingEvent(final PricingEvent event) {
		switch (event) {
		case END_DISCHARGE:
			return PricingEventType.END_OF_DISCHARGE;
		case END_LOAD:
			return PricingEventType.END_OF_LOAD;
		case START_DISCHARGE:
			return PricingEventType.START_OF_DISCHARGE;
		case START_LOAD:
			return PricingEventType.START_OF_LOAD;

		}
		throw new IllegalArgumentException("Unsupported pricing event");
	}
	
	private int getSlotPricingDate(Slot slot) {
		int pricingDate;
		if (slot.isSetPricingDate()) {
			// convert pricing date to local time (as it currently gets converted to UTC in PricingEventHelper)
			Date pricingDateInLocalTime = DateAndCurveHelper.createSameDateAndTimeDifferentTimeZone(slot.getPricingDate(), "UTC", slot.getPort().getTimeZone());
			pricingDate = convertTime(earliestTime, pricingDateInLocalTime);
		} else {
			pricingDate = IPortSlot.NO_PRICING_DATE;
		}
		return pricingDate;
	}
}
