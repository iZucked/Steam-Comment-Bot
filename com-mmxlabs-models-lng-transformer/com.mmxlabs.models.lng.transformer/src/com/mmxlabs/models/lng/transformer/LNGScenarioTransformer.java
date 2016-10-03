/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.curves.StepwiseLongCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.AssignableElement;
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
import com.mmxlabs.models.lng.cargo.util.IShippingDaysRestrictionSpeedProvider;
import com.mmxlabs.models.lng.cargo.util.SpotSlotUtils;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.contracts.IVesselAvailabilityTransformer;
import com.mmxlabs.models.lng.transformer.contracts.IVesselEventTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.TransformerHelper;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.TimeWindowMaker;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.CooldownLumpSumCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.CooldownPriceIndexedCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICancellationFeeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IHedgesProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ILoadPriceCalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;

/**
 * Wrapper for an EMF LNG Scheduling {@link MMXRootObject}, providing utility methods to convert it into an optimisation job. Typical usage is to construct an LNGScenarioTransformer with a given
 * Scenario, and then call the {@link createOptimisationData} method. It is only expected that an instance will be used once. I.e. a single call to {@link #createOptimisationData(ModelEntityMap)}
 * 
 * @author hinton, Simon Goodall
 * 
 */
public class LNGScenarioTransformer {

	private static final Logger log = LoggerFactory.getLogger(LNGScenarioTransformer.class);

	private final @NonNull LNGScenarioModel rootObject;

	@Inject
	@NonNull
	private DateAndCurveHelper dateHelper;

	@Inject
	@Named(LNGTransformerModule.Parser_BaseFuel)
	@NonNull
	private SeriesParser baseFuelIndices;

	@Inject
	@Named(LNGTransformerModule.Parser_Charter)
	@NonNull
	private SeriesParser charterIndices;

	@Inject
	@Named(LNGTransformerModule.Parser_Commodity)
	@NonNull
	private SeriesParser commodityIndices;

	@Inject(optional = true)
	@Nullable
	private List<ITransformerExtension> transformerExtensions;

	@Inject
	@NonNull
	private ISchedulerBuilder builder;

	@Inject
	@NonNull
	private ILoadPriceCalculatorProviderEditor loadPriceCalculatorProvider;

	@Inject
	@NonNull
	private IShipToShipBindingProviderEditor shipToShipBindingProvider;

	@Inject
	@NonNull
	private Injector injector;

	@Inject
	@NonNull
	private IndexedMultiMatrixProvider<IPort, Integer> portDistanceProvider;

	@Inject
	@NonNull
	private IDistanceProviderEditor distanceProviderEditor;

	@Inject
	@NonNull
	private TimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject(optional = true)
	@Nullable
	private IShippingDaysRestrictionSpeedProvider shippingDaysRestrictionSpeedProvider;

	@Inject
	@NonNull
	private IBaseFuelCurveProviderEditor baseFuelCurveProvider;

	@Inject
	@NonNull
	private IPromptPeriodProviderEditor promptPeriodProviderEditor;

	/**
	 * Contains the contract transformers for each known contract type, by the EClass of the contract they transform.
	 */
	@NonNull
	private final Map<EClass, IContractTransformer> contractTransformersByEClass = new LinkedHashMap<EClass, IContractTransformer>();

	/**
	 * A set of all contract transformers being used; these should be mapped to in {@link #contractTransformersByEClass}
	 */
	@NonNull
	private final Set<IContractTransformer> contractTransformers = new LinkedHashSet<IContractTransformer>();

	/**
	 * A set of all vessel event transformers being used;
	 */
	@NonNull
	private final Set<IVesselEventTransformer> vesselEventTransformers = new LinkedHashSet<IVesselEventTransformer>();

	/**
	 * A set of all vessel availability transformers being used;
	 */
	@NonNull
	private final Set<IVesselAvailabilityTransformer> vesselAvailabilityTransformers = new LinkedHashSet<IVesselAvailabilityTransformer>();

	/**
	 * A set of all transformer extensions being used (should contain {@link #contractTransformers})
	 */
	@NonNull
	private final Set<ITransformerExtension> allTransformerExtensions = new LinkedHashSet<ITransformerExtension>();

	@NonNull
	private final Map<NonNullPair<CharterInMarket, Integer>, IVesselAvailability> spotCharterInToAvailability = new HashMap<>();
	private final Map<CharterInMarket, IVesselAvailability> spotCharterInToShortCargoAvailability = new HashMap<>();

	@NonNull
	private final List<IVesselAvailability> allVesselAvailabilities = new ArrayList<IVesselAvailability>();

	@NonNull
	private final Map<Vessel, IVessel> allVessels = new HashMap<>();

	@NonNull
	private final Map<IVessel, Collection<IVesselAvailability>> vesselToAvailabilities = new HashMap<>();

	/**
	 * The {@link Set} of ID strings already used. The UI should restrict user entered data items from clashing, but generated ID's may well clash with user ones.
	 */
	@NonNull
	private final Set<String> usedIDStrings = new HashSet<String>();

	/**
	 * A {@link Map} of existing spot market slots by ID. This map is used later when building the spot market options.
	 */
	@NonNull
	private final Map<String, Slot> marketSlotsByID = new HashMap<String, Slot>();

	@NonNull
	private final Map<SpotMarket, TreeMap<String, Collection<Slot>>> existingSpotCount = new HashMap<>();

	// @NonNull
	// private final OptimiserSettings optimiserParameters;

	@Inject
	@Named(LNGTransformerHelper.HINT_SHIPPING_ONLY)
	private boolean shippingOnly;

	@Inject
	@NonNull
	private IPortVisitDurationProviderEditor portVisitDurationProviderEditor;

	@Inject
	@NonNull
	private IHedgesProviderEditor hedgesProviderEditor;

	@Inject
	@NonNull
	private ICancellationFeeProviderEditor cancellationFeeProviderEditor;

	@Inject
	@Named(LNGTransformerModule.MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve monthIntervalsInHoursCurve;

	/**
	 * Create a transformer for the given scenario; the class holds a reference, so changes made to the scenario after construction will be reflected in calls to the various helper methods.
	 * 
	 * @param scenario
	 */
	@Inject
	public LNGScenarioTransformer(@NonNull final LNGScenarioModel rootObject) {

		this.rootObject = rootObject;
		// this.optimiserParameters = optimiserParameters;
	}

	/**
	 * Get any {@link ITransformerExtension} and {@link IContractTransformer}s from the platform's registry.
	 */
	@Inject
	public boolean addPlatformTransformerExtensions() {

		// Pin for null analysis
		final List<ITransformerExtension> pTransformerExtensions = transformerExtensions;
		if (pTransformerExtensions == null) {
			return false;
		}

		for (final ITransformerExtension transformer : pTransformerExtensions) {
			assert transformer != null;
			addTransformerExtension(transformer);

			if (transformer instanceof IContractTransformer) {
				final IContractTransformer contractTransformer = (IContractTransformer) transformer;
				addContractTransformer(contractTransformer);
			}
			if (transformer instanceof IVesselAvailabilityTransformer) {
				final IVesselAvailabilityTransformer vesselAvailabilityTransformer = (IVesselAvailabilityTransformer) transformer;
				addVesselAvailabilityTransformer(vesselAvailabilityTransformer);
			}
			if (transformer instanceof IVesselEventTransformer) {
				final IVesselEventTransformer vesselEventTransformer = (IVesselEventTransformer) transformer;
				addVesselEventTransformer(vesselEventTransformer);
			}
		}

		return true;
	}

	public void addTransformerExtension(@NonNull final ITransformerExtension extension) {
		log.debug(extension.getClass().getCanonicalName() + " added to transformer extensions");
		allTransformerExtensions.add(extension);
	}

	/**
	 */
	public void addContractTransformer(@NonNull final IContractTransformer transformer) {
		contractTransformers.add(transformer);
		for (final EClass ec : transformer.getContractEClasses()) {
			log.debug(transformer.getClass().getCanonicalName() + " handling contracts with eClass " + ec.getName());
			contractTransformersByEClass.put(ec, transformer);
		}
	}

	public void addVesselAvailabilityTransformer(@NonNull final IVesselAvailabilityTransformer transformer) {
		vesselAvailabilityTransformers.add(transformer);
	}

	public void addVesselEventTransformer(@NonNull final IVesselEventTransformer transformer) {
		vesselEventTransformers.add(transformer);
	}

	/**
	 * Instantiates and returns an {@link IOptimisationData} isomorphic to the contained scenario.
	 * 
	 * @return
	 * @throws IncompleteScenarioException
	 */
	@NonNull
	public IOptimisationData createOptimisationData(@NonNull final ModelEntityMap modelEntityMap) throws IncompleteScenarioException {

		// set earliest and latest times into modelEntityMap
		// modelEntityMap.setEarliestDate(dateHelper.getEarliestTime());
		// modelEntityMap.setLatestDate(dateHelper.getLatestTime());

		timeZoneToUtcOffsetProvider.setTimeZeroInMillis(Instant.from(dateHelper.getEarliestTime()).toEpochMilli());

		if (rootObject.isSetPromptPeriodStart()) {
			promptPeriodProviderEditor.setStartOfPromptPeriod(dateHelper.convertTime(rootObject.getPromptPeriodStart()));
		}
		if (rootObject.isSetPromptPeriodEnd()) {
			promptPeriodProviderEditor.setEndOfPromptPeriod(dateHelper.convertTime(rootObject.getPromptPeriodEnd()));
		}

		/**
		 * First, create all the market curves (should these come through the builder?)
		 */

		final Association<CommodityIndex, ICurve> commodityIndexAssociation = new Association<>();
		final Association<BaseFuelIndex, ICurve> baseFuelIndexAssociation = new Association<>();
		final Association<CharterIndex, ILongCurve> charterIndexAssociation = new Association<>();

		final PricingModel pricingModel = rootObject.getReferenceModel().getPricingModel();
		// For each curve, register with the SeriesParser object
		for (final CommodityIndex commodityIndex : pricingModel.getCommodityIndices()) {
			final Index<Double> index = commodityIndex.getData();
			assert index != null;
			final String name = commodityIndex.getName();
			assert name != null;
			registerIndex(name, index, commodityIndices);
		}
		for (final BaseFuelIndex baseFuelIndex : pricingModel.getBaseFuelPrices()) {
			final Index<Double> index = baseFuelIndex.getData();
			registerIndex(baseFuelIndex.getName(), index, baseFuelIndices);
		}
		for (final CharterIndex charterIndex : pricingModel.getCharterIndices()) {
			final Index<Integer> index = charterIndex.getData();
			registerIndex(charterIndex.getName(), index, charterIndices);
		}

		// Currency is added to all the options
		for (final CurrencyIndex currencyIndex : pricingModel.getCurrencyIndices()) {
			final Index<Double> index = currencyIndex.getData();
			assert index != null;
			final String name = currencyIndex.getName();
			assert name != null;
			registerIndex(name, index, commodityIndices);
			registerIndex(name, index, baseFuelIndices);
			registerIndex(name, index, charterIndices);
		}

		for (UnitConversion factor : pricingModel.getConversionFactors()) {
			registerConversionFactor(factor, commodityIndices, baseFuelIndices, charterIndices);
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
				final StepwiseLongCurve curve = new StepwiseLongCurve();
				curve.setDefaultValue(0L);

				final int[] changePoints = parsed.getChangePoints();
				if (changePoints.length == 0) {
					final long dailyCost = OptimiserUnitConvertor.convertToInternalDailyCost(parsed.evaluate(0).intValue());
					curve.setValueAfter(0, dailyCost);
				} else {

					for (final int i : parsed.getChangePoints()) {
						final long dailyCost = OptimiserUnitConvertor.convertToInternalDailyCost(parsed.evaluate(i).intValue());
						curve.setValueAfter(i, dailyCost);
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
		final PortModel portModel = rootObject.getReferenceModel().getPortModel();

		final CostModel costModel = rootObject.getReferenceModel().getCostModel();

		final Map<Port, ICooldownCalculator> cooldownCalculators = new HashMap<Port, ICooldownCalculator>();
		for (final CooldownPrice price : costModel.getCooldownCosts()) {
			final ICooldownCalculator cooldownCalculator;
			// Check here if price is indexed or expression
			if (price.isLumpsum()) {
				@Nullable
				final ILongCurve cooldownCurve = dateHelper.generateLongExpressionCurve(price.getExpression(), commodityIndices);
				if (cooldownCurve == null) {
					throw new IllegalStateException("Unable to parse cooldown curve");
				}
				cooldownCalculator = new CooldownLumpSumCalculator(cooldownCurve);
			} else {
				cooldownCalculator = new CooldownPriceIndexedCalculator(dateHelper.generateExpressionCurve(price.getExpression(), commodityIndices));
			}
			injector.injectMembers(cooldownCalculator);

			for (final Port port : SetUtils.getObjects(price.getPorts())) {
				cooldownCalculators.put(port, cooldownCalculator);
			}
		}

		for (final Port ePort : portModel.getPorts()) {
			final IPort port;
			final int minCv = ePort.isSetMinCvValue() ? OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getMinCvValue()) : 0;
			final int maxCv = ePort.isSetMaxCvValue() ? OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getMaxCvValue()) : Integer.MAX_VALUE;
			if (ePort.getLocation() != null) {
				final Location loc = ePort.getLocation();
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort), (float) loc.getLat(), (float) loc.getLon(), ePort.getTimeZone(), minCv, maxCv);
			} else {
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort), ePort.getTimeZone(), minCv, maxCv);
			}

			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);
			modelEntityMap.addModelObject(ePort, port);

			builder.setPortCV(port, OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getCvValue()));
			builder.setPortMinCV(port, minCv);
			builder.setPortMaxCV(port, maxCv);

			// Set port default values
			portVisitDurationProviderEditor.setVisitDuration(port, PortType.Load, ePort.getLoadDuration());
			portVisitDurationProviderEditor.setVisitDuration(port, PortType.Discharge, ePort.getDischargeDuration());
		}

		// Generate base fuels
		{
			for (final BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
				final BaseFuelIndex index = baseFuelCost.getIndex();
				assert index != null;

				final BaseFuel eBF = baseFuelCost.getFuel();
				assert eBF != null;

				final IBaseFuel bf = TransformerHelper.buildBaseFuel(builder, eBF);
				modelEntityMap.addModelObject(eBF, bf);

				final ICurve curve = baseFuelIndexAssociation.lookupNullChecked(index);
				baseFuelCurveProvider.setBaseFuelCurve(bf, curve);
			}
		}
		// set up the contract transformers
		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.startTransforming(rootObject, modelEntityMap, builder);
		}

		final NonNullPair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> vesselAssociations = buildFleet(builder, portAssociation, baseFuelIndexAssociation,
				charterIndexAssociation, modelEntityMap);
		for (final IVesselAvailability vesselAvailability : allVesselAvailabilities) {
			assert vesselAvailability != null;

			for (final IVesselAvailabilityTransformer vesselAvailabilityTransformer : vesselAvailabilityTransformers) {
				assert vesselAvailabilityTransformer != null;

				final VesselAvailability eVesselAvailability = modelEntityMap.getModelObject(vesselAvailability, VesselAvailability.class);
				// This can be null if the availability is generated from a Spot option
				if (eVesselAvailability != null) {

					vesselAvailabilityTransformer.vesselAvailabilityTransformed(eVesselAvailability, vesselAvailability);
				}
			}
		}

		final CommercialModel commercialModel = rootObject.getReferenceModel().getCommercialModel();

		// Any NPE's in the following code are likely due to missing associations between a IContractTransformer and the EMF AContract object. IContractTransformer instances are typically OSGi
		// services. Ensure their bundles have been started!
		for (final SalesContract c : commercialModel.getSalesContracts()) {
			final LNGPriceCalculatorParameters priceInfo = c.getPriceInfo();
			assert priceInfo != null;

			final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + priceInfo.eClass().getName());
			}
			final ISalesPriceCalculator calculator = transformer.transformSalesPriceParameters(c, priceInfo);
			if (calculator == null) {
				throw new IllegalStateException("Unable to transform contract");
			}
			modelEntityMap.addModelObject(c, calculator);
		}

		for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
			final LNGPriceCalculatorParameters priceInfo = c.getPriceInfo();
			assert priceInfo != null;
			final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + priceInfo.eClass().getName());
			}
			final ILoadPriceCalculator calculator = transformer.transformPurchasePriceParameters(c, priceInfo);
			assert calculator != null;

			modelEntityMap.addModelObject(c, calculator);
			loadPriceCalculatorProvider.setPortfolioCalculator(calculator);
		}

		// process port costs
		for (final PortCost cost : costModel.getPortCosts()) {
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
							builder.setPortCost(portAssociation.lookupNullChecked((Port) port), v, type, activityCost);
						}

					}
				}
			}
		}

		buildDistances(builder, portAssociation, allPorts, portIndices, vesselAssociations.getSecond(), vesselAssociations.getFirst(), modelEntityMap);

		buildCargoes(builder, portAssociation, vesselAssociations.getSecond(), vesselAssociations.getFirst(), contractTransformers, modelEntityMap);

		buildVesselEvents(builder, portAssociation, vesselAssociations.getSecond(), vesselAssociations.getFirst(), modelEntityMap);

		buildSpotCargoMarkets(builder, portAssociation, contractTransformers, modelEntityMap);

		buildMarkToMarkets(builder, portAssociation, contractTransformers, modelEntityMap);

		setNominatedVessels(builder, modelEntityMap);

		// freeze any frozen assignments
		freezeAssignmentModel(builder, modelEntityMap);

		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.finishTransforming();
		}

		builder.setEarliestDate(dateHelper.getEarliestTime());

		final IOptimisationData optimisationData = builder.getOptimisationData();

		return optimisationData;
	}

	@SuppressWarnings("rawtypes")
	private void registerIndex(@NonNull final String name, @NonNull final Index<? extends Number> index, @NonNull final SeriesParser indices) {
		if (index instanceof DataIndex) {
			final DataIndex<? extends Number> di = (DataIndex<? extends Number>) index;
			final SortedSet<Pair<YearMonth, Number>> vals = new TreeSet<Pair<YearMonth, Number>>(new Comparator<Pair<YearMonth, ?>>() {
				@Override
				public int compare(final Pair<YearMonth, ?> o1, final Pair<YearMonth, ?> o2) {
					return o1.getFirst().compareTo(o2.getFirst());
				}
			});
			for (final IndexPoint<? extends Number> pt : di.getPoints()) {
				vals.add(new Pair<YearMonth, Number>(pt.getDate(), pt.getValue()));
			}
			final int[] times = new int[vals.size()];
			final Number[] nums = new Number[vals.size()];
			int k = 0;
			for (final Pair<YearMonth, Number> e : vals) {
				times[k] = dateHelper.convertTime(e.getFirst());
				nums[k++] = e.getSecond();
			}
			indices.addSeriesData(name, times, nums);
		} else if (index instanceof DerivedIndex) {
			indices.addSeriesExpression(name, ((DerivedIndex) index).getExpression());
		}
	}

	private void registerConversionFactor(@NonNull final UnitConversion factor, @NonNull final SeriesParser... parsers) {
		String name = PriceIndexUtils.createConversionFactorName(factor);
		if (name != null) {
			for (SeriesParser parser : parsers) {
				parser.addSeriesExpression(name, Double.toString(factor.getFactor()));
			}
		}
	}

	private void setNominatedVessels(@NonNull final ISchedulerBuilder builder, @NonNull final ModelEntityMap modelEntityMap) {

		final CargoModel cargoModel = rootObject.getCargoModel();
		if (cargoModel != null) {
			for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {
				assert loadSlot != null;
				final IPortSlot portSlot = modelEntityMap.getOptimiserObject(loadSlot, IPortSlot.class);
				final IVessel vessel = allVessels.get(loadSlot.getNominatedVessel());
				if (vessel != null && portSlot != null) {
					builder.setNominatedVessel(portSlot, vessel);
				}
			}

			for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {
				assert dischargeSlot != null;
				final IPortSlot portSlot = modelEntityMap.getOptimiserObject(dischargeSlot, IPortSlot.class);
				final IVessel vessel = allVessels.get(dischargeSlot.getNominatedVessel());
				if (vessel != null && portSlot != null) {
					builder.setNominatedVessel(portSlot, vessel);
				}
			}
		}
	}

	private void freezeAssignmentModel(@NonNull final ISchedulerBuilder builder, @NonNull final ModelEntityMap modelEntityMap) {

		// TODO: Freeze as part of the buildXXX methods when object is created rather than here.

		final Set<AssignableElement> assignableElements = new LinkedHashSet<>();
		assignableElements.addAll(rootObject.getCargoModel().getCargoes());
		assignableElements.addAll(rootObject.getCargoModel().getVesselEvents());

		for (final AssignableElement assignableElement : assignableElements) {
			// Open positions handled by the LockedUnusedElementsConstraintChecker.
			// TODO: What about FOB/DES cargoes?
			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
			if (vesselAssignmentType == null) {
				// continue;
			}

			boolean isNominalVessel = false;
			IVesselAvailability vesselAvailability = null;
			if (vesselAssignmentType instanceof VesselAvailability) {
				final VesselAvailability va = (VesselAvailability) vesselAssignmentType;
				vesselAvailability = modelEntityMap.getOptimiserObject(va, IVesselAvailability.class);
			}
			if (vesselAssignmentType instanceof CharterInMarket) {
				final int spotIndex = assignableElement.getSpotIndex();
				final NonNullPair<CharterInMarket, Integer> key = new NonNullPair<>((CharterInMarket) vesselAssignmentType, spotIndex);
				vesselAvailability = spotCharterInToAvailability.get(key);
				if (vesselAvailability == null) {
					vesselAvailability = spotCharterInToShortCargoAvailability.get(vesselAssignmentType);
				}
				isNominalVessel = spotIndex == -1;
			}

			if (vesselAvailability == null) {
				if (assignableElement instanceof Cargo) {
					// Keep going to constrain slot pairing if needed.
				} else {
					continue;
				}
			}

			final boolean freeze = assignableElement.isLocked();
			final Set<Slot> lockedSlots = checkAndCollectLockedSlots(assignableElement);
			final boolean freezeElements = (!(!freeze && lockedSlots.isEmpty()));

			if (assignableElement instanceof Cargo) {
				final Cargo cargo = (Cargo) assignableElement;
				IPortSlot prevSlot = null;
				for (final Slot slot : cargo.getSortedSlots()) {
					final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);

					if (isNominalVessel && vesselAvailability != null) {
						builder.bindSlotsToRoundTripVessel(vesselAvailability, portSlot);
					}

					if (vesselAvailability != null && (freeze || lockedSlots.contains(slot))) {
						// bind slots to vessel
						builder.freezeSlotToVesselAvailability(portSlot, vesselAvailability);
					}

					if ((prevSlot != null) & (freeze || (lockedSlots.contains(slot) && lockedSlots.contains(prevSlot)))) {
						// bind sequencing as well - this forces
						// previousSlot to come before currentSlot.
						builder.constrainSlotAdjacency(prevSlot, portSlot);
					}

					prevSlot = portSlot;
				}
			} else if (assignableElement instanceof VesselEvent) {
				if (freezeElements) {
					final IVesselEventPortSlot slot = modelEntityMap.getOptimiserObject(assignableElement, IVesselEventPortSlot.class);
					if (slot != null && vesselAvailability != null) {
						builder.freezeSlotToVesselAvailability(slot, vesselAvailability);
					}
				}
			}
		}
	}

	@NonNull
	private Set<@NonNull Slot> checkAndCollectLockedSlots(@NonNull final AssignableElement assignableElement) {
		final Set<@NonNull Slot> lockedSlots = new HashSet<>();

		if (assignableElement instanceof Cargo) {
			final Cargo cargo = (Cargo) assignableElement;
			for (final Slot slot : cargo.getSortedSlots()) {
				if (slot.isLocked()) {
					lockedSlots.add(slot);
				}
			}
		}
		return lockedSlots;
	}

	private void buildVesselEvents(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final Association<Vessel, IVessel> vesselAssociation,
			@NonNull final Association<VesselClass, IVesselClass> vesselClassAssociation, @NonNull final ModelEntityMap modelEntityMap) {

		final ZonedDateTime latestDate = dateHelper.getLatestTime();

		final CargoModel cargoModel = rootObject.getCargoModel();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {

			if (event.getStartAfterAsDateTime().isAfter(latestDate)) {
				continue;
			}

			final ITimeWindow window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(event.getStartAfterAsDateTime()), dateHelper.convertTime(event.getStartByAsDateTime()), false);
			final IPort port = portAssociation.lookupNullChecked(event.getPort());
			final int durationHours = event.getDurationInDays() * 24;
			final IVesselEventPortSlot builderSlot;
			if (event instanceof CharterOutEvent) {
				final CharterOutEvent charterOut = (CharterOutEvent) event;
				final IPort endPort = portAssociation.lookupNullChecked(charterOut.isSetRelocateTo() ? charterOut.getRelocateTo() : charterOut.getPort());
				final HeelOptions heelOptions = charterOut.getHeelOptions();
				final long maxHeel = heelOptions.isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getVolumeAvailable()) : 0l;
				final long totalHireRevenue = OptimiserUnitConvertor.convertToInternalDailyCost(charterOut.getHireRate()) * (long) charterOut.getDurationInDays();
				final long repositioning = OptimiserUnitConvertor.convertToInternalFixedCost(charterOut.getRepositioningFee());
				builderSlot = builder.createCharterOutEvent(event.getName(), window, port, endPort, durationHours, maxHeel,
						OptimiserUnitConvertor.convertToInternalConversionFactor(heelOptions.getCvValue()), OptimiserUnitConvertor.convertToInternalPrice(heelOptions.getPricePerMMBTU()),
						totalHireRevenue, repositioning);
			} else if (event instanceof DryDockEvent) {
				builderSlot = builder.createDrydockEvent(event.getName(), window, port, durationHours);
			} else if (event instanceof MaintenanceEvent) {
				builderSlot = builder.createMaintenanceEvent(event.getName(), window, port, durationHours);
			} else {
				throw new RuntimeException("Unknown event type: " + event.getClass().getName());
			}

			applySlotVesselRestrictions(event.getAllowedVessels(), builderSlot, vesselAssociation, vesselClassAssociation);
			// Apply restrictions to related slots
			for (final IPortSlot e : builderSlot.getEventPortSlots()) {
				if (e != builderSlot) {
					applySlotVesselRestrictions(event.getAllowedVessels(), e, vesselAssociation, vesselClassAssociation);
				}
			}

			modelEntityMap.addModelObject(event, builderSlot);
			for (final IVesselEventTransformer vesselEventTransformer : vesselEventTransformers) {
				vesselEventTransformer.vesselEventTransformed(event, builderSlot);
			}
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
	private void buildCargoes(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final Association<Vessel, IVessel> vesselAssociation,
			@NonNull final Association<VesselClass, IVesselClass> vesselClassAssociation, @NonNull final Collection<IContractTransformer> contractTransformers,
			@NonNull final ModelEntityMap modelEntityMap) {

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

		final ZonedDateTime latestDate = dateHelper.getLatestTime();

		final Set<LoadSlot> usedLoadSlots = new HashSet<LoadSlot>();
		final Set<DischargeSlot> usedDischargeSlots = new HashSet<DischargeSlot>();

		final CargoModel cargoModel = rootObject.getCargoModel();
		final Map<Slot, IPortSlot> transferSlotMap = new HashMap<Slot, IPortSlot>();

		for (final Cargo eCargo : cargoModel.getCargoes()) {

			if (eCargo.getSortedSlots().get(0).getWindowStartWithSlotOrPortTime().isAfter(latestDate)) {
				continue;
			}

			final List<@NonNull ILoadOption> loadOptions = new LinkedList<>();
			final List<@NonNull IDischargeOption> dischargeOptions = new LinkedList<>();
			final List<@NonNull IPortSlot> slots = new ArrayList<>(eCargo.getSortedSlots().size());
			final Map<Slot, IPortSlot> slotMap = new HashMap<>();
			for (final Slot slot : eCargo.getSortedSlots()) {
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					{
						final ILoadOption load = createLoadOption(builder, portAssociation, vesselAssociation, vesselClassAssociation, contractTransformers, modelEntityMap, loadSlot);
						usedLoadSlots.add(loadSlot);
						loadOptions.add(load);
						slotMap.put(loadSlot, load);
						slots.add(load);
					}

				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					{
						final IDischargeOption discharge = createDischargeOption(builder, portAssociation, vesselAssociation, vesselClassAssociation, contractTransformers, modelEntityMap,
								dischargeSlot);
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
					assert loadSlot != null;
					final ITimeWindow twForBinding = getTimeWindowForSlotBinding(loadSlot, load, portAssociation.lookupNullChecked(loadSlot.getPort()));
					configureLoadSlotRestrictions(builder, portAssociation, allDischargePorts, loadSlot, load, twForBinding);
					isTransfer = (((LoadSlot) slot).getTransferFrom() != null);
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					final IDischargeOption discharge = (IDischargeOption) slotMap.get(dischargeSlot);
					assert discharge != null;
					final ITimeWindow twForBinding = getTimeWindowForSlotBinding(dischargeSlot, discharge, portAssociation.lookupNullChecked(dischargeSlot.getPort()));
					configureDischargeSlotRestrictions(builder, portAssociation, allLoadPorts, dischargeSlot, discharge, twForBinding);
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
			assert loadSlot != null;
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
				load = createLoadOption(builder, portAssociation, vesselAssociation, vesselClassAssociation, contractTransformers, modelEntityMap, loadSlot);
				if (!loadSlot.isOptional()) {
					builder.setSoftRequired(load);
				}
			}

			configureLoadSlotRestrictions(builder, portAssociation, allDischargePorts, loadSlot, load,
					getTimeWindowForSlotBinding(loadSlot, load, portAssociation.lookupNullChecked(loadSlot.getPort())));
		}

		for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {
			assert dischargeSlot != null;
			// TODO: Filter on date
			// if (eCargo.getLoadSlot().getWindowStartWithSlotOrPortTime().after(latestDate)) {
			// continue;
			// }
			if (usedDischargeSlots.contains(dischargeSlot)) {
				continue;
			}

			final IDischargeOption discharge;
			{
				discharge = createDischargeOption(builder, portAssociation, vesselAssociation, vesselClassAssociation, contractTransformers, modelEntityMap, dischargeSlot);
				if (!dischargeSlot.isOptional()) {
					builder.setSoftRequired(discharge);
				}
			}

			configureDischargeSlotRestrictions(builder, portAssociation, allLoadPorts, dischargeSlot, discharge,
					getTimeWindowForSlotBinding(dischargeSlot, discharge, portAssociation.lookup(dischargeSlot.getPort())));
		}
	}

	@NonNull
	private ITimeWindow getTimeWindowForSlotBinding(final Slot modelSlot, final IPortSlot optimiserSlot, final IPort port) {

		if (modelSlot instanceof SpotSlot) {
			// TODO: IS this with flex or not??
			final ZonedDateTime startTime = modelSlot.getWindowStartWithSlotOrPortTime();
			final ZonedDateTime endTime = modelSlot.getWindowEndWithSlotOrPortTime();
			// Convert port local external date/time into UTC based internal time units
			final int twStart = timeZoneToUtcOffsetProvider.UTC(dateHelper.convertTime(startTime), port);
			final int twEnd = timeZoneToUtcOffsetProvider.UTC(dateHelper.convertTime(endTime), port);
			final ITimeWindow twUTC = TimeWindowMaker.createInclusiveInclusive(twStart, Math.max(twStart, twEnd), 0, false);

			return twUTC;
		} else {
			@Nullable
			final ITimeWindow timeWindow = optimiserSlot.getTimeWindow();
			assert timeWindow != null;
			return timeWindow;
		}

	}

	public void configureDischargeSlotRestrictions(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final Set<IPort> allLoadPorts,
			@NonNull final DischargeSlot dischargeSlot, @NonNull final IDischargeOption discharge, @NonNull final ITimeWindow twForSlotBinding) {
		if (dischargeSlot.isFOBSale()) {

			if (dischargeSlot instanceof SpotDischargeSlot) {
				final SpotDischargeSlot spotSlot = (SpotDischargeSlot) dischargeSlot;
				final FOBSalesMarket fobSaleMarket = (FOBSalesMarket) spotSlot.getMarket();
				final Set<Port> portSet = SetUtils.getObjects(fobSaleMarket.getOriginPorts());
				final Set<IPort> marketPorts = new HashSet<IPort>();
				for (final Port ap : portSet) {
					final IPort ip = portAssociation.lookup((Port) ap);
					if (ip != null) {
						marketPorts.add(ip);
					}
				}

				final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
				for (final IPort port : marketPorts) {
					// Take the UTC based window and shift according to local port timezone
					final int twStart = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getInclusiveStart(), port);
					final int twEnd = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getExclusiveEnd(), port);
					marketPortsMap.put(port, new TimeWindow(twStart, twEnd));
				}

				builder.bindLoadSlotsToFOBSale(discharge, marketPortsMap);
			} else if (dischargeSlot.isDivertible()) {
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

	public void configureLoadSlotRestrictions(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final Set<IPort> allDischargePorts,
			@NonNull final LoadSlot loadSlot, @NonNull final ILoadOption load, @NonNull final ITimeWindow twForSlotBinding) {

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
					final int twStart = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getInclusiveStart(), port);
					final int twEnd = timeZoneToUtcOffsetProvider.localTime(twForSlotBinding.getExclusiveEnd(), port);
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

			final List<ERouteOption> allowedRoutes = new LinkedList<>();
			if (shippingDaysRestrictionSpeedProvider != null) {
				for (final Route route : shippingDaysRestrictionSpeedProvider.getValidRoutes(ScenarioModelUtil.getPortModel(rootObject), loadSlot)) {
					allowedRoutes.add(mapRouteOption(route));
				}
			}
			builder.setDivertableDESAllowedRoute(load, allowedRoutes);

		}
	}

	@NonNull
	private IDischargeOption createDischargeOption(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final Association<Vessel, IVessel> vesselAssociation, @NonNull final Association<VesselClass, IVesselClass> vesselClassAssociation,
			@NonNull final Collection<IContractTransformer> contractTransformers, @NonNull final ModelEntityMap modelEntityMap, @NonNull final DischargeSlot dischargeSlot) {
		final IDischargeOption discharge;
		final String elementName = String.format("%s-%s", dischargeSlot.isFOBSale() ? "FS" : "DS", dischargeSlot.getName());

		usedIDStrings.add(elementName);

		// final ITimeWindow dischargeWindow = builder.createTimeWindow(dateHelper.convertTime(dischargeSlot.getWindowStartWithSlotOrPortTimeWithFlex()),
		// dateHelper.convertTime(dischargeSlot.getWindowEndWithSlotOrPortTimeWithFlex()), dischargeSlot.getWindowFlex());

		final ITimeWindow dischargeWindow = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(dischargeSlot.getWindowStartWithSlotOrPortTimeWithFlex()),
				dateHelper.convertTime(dischargeSlot.getWindowEndWithSlotOrPortTimeWithFlex()), dischargeSlot.getWindowFlex(), false);

		final ISalesPriceCalculator dischargePriceCalculator;

		final boolean isSpot = (dischargeSlot instanceof SpotSlot);
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
				dischargePriceCalculator = new PriceExpressionContract(curve, monthIntervalsInHoursCurve);
				injector.injectMembers(dischargePriceCalculator);
			}
		} else if (dischargeSlot instanceof SpotSlot) {
			final SpotSlot spotSlot = (SpotSlot) dischargeSlot;
			final SpotMarket market = spotSlot.getMarket();

			final LNGPriceCalculatorParameters priceInfo = market.getPriceInfo();
			assert priceInfo != null;
			final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + priceInfo.eClass().getName());
			}
			final ISalesPriceCalculator calculator = transformer.transformSalesPriceParameters(null, priceInfo);
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
			throw new IllegalStateException(String.format("Discharge Slot [%s] has no contract or other pricing data", dischargeSlot.getName()));
		}

		final boolean isVolumeLimitInM3 = dischargeSlot.getSlotOrContractVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3 ? true : false;

		// local scope for slot creation convenience variables
		{
			// convenience variables
			final String name = elementName;
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

			final boolean slotLocked = dischargeSlot.isLocked() || shippingOnly && dischargeSlot.getCargo() == null;
			if (dischargeSlot.isFOBSale()) {
				final ITimeWindow localTimeWindow;
				// if (dischargeSlot.isDivertable()) {
				// // Extend window out to cover whole shipping days restriction
				// localTimeWindow = builder.createTimeWindow(dischargeWindow.getStart() - dischargeSlot.getShippingDaysRestriction() * 24, dischargeWindow.getEnd());
				// } else

				if (dischargeSlot instanceof SpotDischargeSlot) {
					// Convert back into a UTC based date and add in TZ flex
					final int utcStart = timeZoneToUtcOffsetProvider.UTC(dischargeWindow.getInclusiveStart(), portAssociation.lookup(dischargeSlot.getPort()));
					final int utcEnd = timeZoneToUtcOffsetProvider.UTC(dischargeWindow.getExclusiveEnd(), portAssociation.lookup(dischargeSlot.getPort()));
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
						pricingDate, transformPricingEvent(dischargeSlot.getSlotOrDelegatedPricingEvent()), dischargeSlot.isOptional(), slotLocked, isSpot, isVolumeLimitInM3);

				if (dischargeSlot.isDivertible()) {
					// builder.setShippingHoursRestriction(discharge, dischargeWindow, dischargeSlot.getShippingDaysRestriction() * 24);
				}
			} else {
				discharge = builder.createDischargeSlot(name, portAssociation.lookupNullChecked(dischargeSlot.getPort()), dischargeWindow, minVolume, maxVolume, minCv, maxCv, dischargePriceCalculator,
						dischargeSlot.getSlotOrPortDuration(), pricingDate, transformPricingEvent(dischargeSlot.getSlotOrDelegatedPricingEvent()), dischargeSlot.isOptional(), slotLocked, isSpot,
						isVolumeLimitInM3);
			}
		}

		if (dischargeSlot instanceof SpotSlot) {
			marketSlotsByID.put(elementName, dischargeSlot);
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

		final ILongCurve cancellationCurve = dateHelper.generateLongExpressionCurve(dischargeSlot.getCancellationExpression(), commodityIndices);
		if (cancellationCurve != null) {
			cancellationFeeProviderEditor.setCancellationExpression(discharge, cancellationCurve);
		}
		applySlotVesselRestrictions(dischargeSlot.getAllowedVessels(), discharge, vesselAssociation, vesselClassAssociation);

		return discharge;
	}

	@NonNull
	private ILoadOption createLoadOption(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final Association<Vessel, IVessel> vesselAssociation, @NonNull final Association<VesselClass, IVesselClass> vesselClassAssociation,
			@NonNull final Collection<IContractTransformer> contractTransformers, @NonNull final ModelEntityMap modelEntityMap, @NonNull final LoadSlot loadSlot) {
		final ILoadOption load;
		final String elementName = String.format("%s-%s", loadSlot.isDESPurchase() ? "DP" : "FP", loadSlot.getName());
		usedIDStrings.add(elementName);

		final ITimeWindow loadWindow = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(loadSlot.getWindowStartWithSlotOrPortTimeWithFlex()),
				dateHelper.convertTime(loadSlot.getWindowEndWithSlotOrPortTimeWithFlex()), loadSlot.getWindowFlex(), false);

		final ILoadPriceCalculator loadPriceCalculator;
		final boolean isSpot = (loadSlot instanceof SpotSlot);

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
				loadPriceCalculator = new PriceExpressionContract(curve, monthIntervalsInHoursCurve);
				injector.injectMembers(loadPriceCalculator);

			}

		} else if (loadSlot instanceof SpotSlot) {
			final SpotSlot spotSlot = (SpotSlot) loadSlot;
			final SpotMarket market = spotSlot.getMarket();

			final LNGPriceCalculatorParameters priceInfo = market.getPriceInfo();
			assert priceInfo != null;
			final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
			if (transformer == null) {
				throw new IllegalStateException("No Price Parameters transformer registered for  " + priceInfo.eClass().getName());
			}
			final ILoadPriceCalculator calculator = transformer.transformPurchasePriceParameters(null, priceInfo);
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
			throw new IllegalStateException(String.format("Load Slot [%s] has no contract or other pricing data", loadSlot.getName()));
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

		final boolean isVolumeLimitInM3 = loadSlot.getSlotOrContractVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3 ? true : false;

		final boolean slotLocked = loadSlot.isLocked() || shippingOnly && loadSlot.getCargo() == null;
		if (loadSlot.isDESPurchase()) {
			final ITimeWindow localTimeWindow;
			if (loadSlot.isDivertible()) {
				// Extend window out to cover whole shipping days restriction
				localTimeWindow = TimeWindowMaker.createInclusiveExclusive(loadWindow.getInclusiveStart(), loadWindow.getExclusiveEnd() + loadSlot.getShippingDaysRestriction() * 24, 0, false);
			} else if (loadSlot instanceof SpotLoadSlot) {
				// Convert back into a UTC based date and add in TZ flex
				final int utcStart = timeZoneToUtcOffsetProvider.UTC(loadWindow.getInclusiveStart(), portAssociation.lookup(loadSlot.getPort()));
				final int utcEnd = timeZoneToUtcOffsetProvider.UTC(loadWindow.getExclusiveEnd(), portAssociation.lookup(loadSlot.getPort()));
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
			load = builder.createDESPurchaseLoadSlot(elementName, port, localTimeWindow, minVolume, maxVolume, loadPriceCalculator,
					OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrDelegatedCV()), loadSlot.getSlotOrPortDuration(), slotPricingDate,
					transformPricingEvent(loadSlot.getSlotOrDelegatedPricingEvent()), loadSlot.isOptional(), slotLocked, isSpot, isVolumeLimitInM3);

			if (loadSlot.isDivertible()) {
				builder.setShippingHoursRestriction(load, loadWindow, loadSlot.getShippingDaysRestriction() * 24);
			}
		} else {
			load = builder.createLoadSlot(elementName, portAssociation.lookupNullChecked(loadSlot.getPort()), loadWindow, minVolume, maxVolume, loadPriceCalculator,
					OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrDelegatedCV()), loadSlot.getSlotOrPortDuration(), loadSlot.isSetArriveCold(), loadSlot.isArriveCold(),
					slotPricingDate, transformPricingEvent(loadSlot.getSlotOrDelegatedPricingEvent()), loadSlot.isOptional(), slotLocked, isSpot, isVolumeLimitInM3);
		}
		// Store market slots for lookup when building spot markets.
		modelEntityMap.addModelObject(loadSlot, load);

		for (final IContractTransformer contractTransformer : contractTransformers) {
			contractTransformer.slotTransformed(loadSlot, load);
		}
		if (loadSlot instanceof SpotSlot) {
			marketSlotsByID.put(elementName, loadSlot);
			addSpotSlotToCount((SpotSlot) loadSlot);
		}

		final long hedgeCost = OptimiserUnitConvertor.convertToInternalFixedCost(loadSlot.getHedges());
		if (hedgeCost != 0) {
			hedgesProviderEditor.setHedgeValue(load, hedgeCost);
		}

		final ILongCurve cancellationCurve = dateHelper.generateLongExpressionCurve(loadSlot.getCancellationExpression(), commodityIndices);
		if (cancellationCurve != null) {
			cancellationFeeProviderEditor.setCancellationExpression(load, cancellationCurve);
		}

		applySlotVesselRestrictions(loadSlot.getAllowedVessels(), load, vesselAssociation, vesselClassAssociation);

		return load;
	}

	private void applySlotVesselRestrictions(final @Nullable List<AVesselSet<Vessel>> allowedVessels, final @NonNull IPortSlot optimiserSlot, final Association<Vessel, IVessel> vesselAssociation,
			final Association<VesselClass, IVesselClass> vesselClassAssociation) {

		if (allowedVessels == null) {
			return;
		}

		if (!allowedVessels.isEmpty()) {

			List<@NonNull IVessel> permittedVessels = null;
			List<@NonNull IVesselClass> permittedVesselClasses = null;

			for (final AVesselSet<Vessel> vesselSet : allowedVessels) {
				if (vesselSet instanceof Vessel) {
					final Vessel e_vessel = (Vessel) vesselSet;
					final IVessel o_vessel = vesselAssociation.lookupNullChecked(e_vessel);
					if (permittedVessels == null) {
						permittedVessels = new LinkedList<>();
					}
					permittedVessels.add(o_vessel);
				} else if (vesselSet instanceof VesselClass) {
					final VesselClass e_vesselClass = (VesselClass) vesselSet;
					final IVesselClass o_vesselClass = vesselClassAssociation.lookupNullChecked(e_vesselClass);
					if (permittedVesselClasses == null) {
						permittedVesselClasses = new LinkedList<>();
					}
					permittedVesselClasses.add(o_vesselClass);
				} else if (vesselSet instanceof VesselGroup) {
					final VesselGroup vesselGroup = (VesselGroup) vesselSet;
					for (final Vessel e_vessel : vesselGroup.getVessels()) {
						final IVessel o_vessel = vesselAssociation.lookupNullChecked(e_vessel);
						if (permittedVessels == null) {
							permittedVessels = new LinkedList<>();
						}
						permittedVessels.add(o_vessel);
					}
				} else {
					assert false;
				}
				builder.setVesselAndClassPermissions(optimiserSlot, permittedVessels, permittedVesselClasses);
			}
		}
	}

	private void buildSpotCargoMarkets(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final Collection<IContractTransformer> contractTransformers, @NonNull final ModelEntityMap modelEntityMap) {

		// Not needed for a shipping only optimisation
		if (shippingOnly) {
			return;
		}

		final SpotMarketsModel spotMarketsModel = rootObject.getReferenceModel().getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}
		final ZonedDateTime earliestDate = dateHelper.getEarliestTime();
		final ZonedDateTime latestDate = dateHelper.getLatestTime();

		buildDESPurchaseSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getDesPurchaseSpotMarket());
		buildDESSalesSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getDesSalesSpotMarket());
		buildFOBPurchaseSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getFobPurchasesSpotMarket());
		buildFOBSalesSpotMarket(builder, portAssociation, contractTransformers, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getFobSalesSpotMarket());

	}

	private void buildDESPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final ZonedDateTime earliestDate, final ZonedDateTime latestDate, final SpotMarketGroup desPurchaseSpotMarket) {
		if (desPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = desPurchaseSpotMarket.getAvailability();

			/** Loop over the date range in the optimisation generating market slots */
			// Get the YearMonth of the earliest date in the scenario.
			final YearMonth initialYearMonth = YearMonth.from(earliestDate.withZoneSameLocal(ZoneId.of("UTC")).toLocalDate());

			// Convert this to the 1st of the month in the notional port timezone.
			ZonedDateTime tzStartTime = initialYearMonth.atDay(1).atStartOfDay(ZoneId.of("UTC"));
			while (!tzStartTime.isAfter(latestDate)) {

				// Convert into timezoneless date objects (for EMF slot object)
				final LocalDate startTime = tzStartTime.toLocalDate();
				// Calculate the upper bound.
				final LocalDate endTime = startTime.plusMonths(1);
				// Get the year/month key
				final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

				// Calculate timezone end date for time window
				final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : desPurchaseSpotMarket.getMarkets()) {
					assert market instanceof DESPurchaseMarket;
					if (market instanceof DESPurchaseMarket && market.isEnabled() == true) {
						final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;

						final LNGPriceCalculatorParameters priceInfo = desPurchaseMarket.getPriceInfo();
						assert priceInfo != null;
						final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
						final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, priceInfo);

						final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
						final Set<IPort> marketPorts = new HashSet<IPort>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup((Port) ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								// As we have no port we create two timewindows. One is pure UTC which we base the EMF Slot date on and shift for the slot binding. The second is UTC with a -12/+14
								// flex for timezones passed into the optimiser slot. This combination allows the slot to be matched against any slot in the same month in any timezone, but be
								// restricted to match the month boundary in that timezone.

								// This should probably be fixed in ScheduleBuilder#matchingWindows and elsewhere if needed, but subtract one to avoid e.g. 1st Feb 00:00 being permitted in the Jan
								// month block
								final ITimeWindow twUTC = TimeWindowMaker.createInclusiveExclusive(dateHelper.convertTime(tzStartTime), dateHelper.convertTime(tzEndTime), 0, false);
								final ITimeWindow twUTCPlus = createUTCPlusTimeWindow(dateHelper.convertTime(tzStartTime), dateHelper.convertTime(tzEndTime));

								final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(desPurchaseMarket.getCv());

								final String typePrefix = "DP-";
								final String externalIDPrefix = market.getName() + "-" + yearMonthString + "-";

								// Avoid ID clash
								String externalID = externalIDPrefix + (i + offset);
								String internalID = typePrefix + externalID;
								while (usedIDStrings.contains(internalID)) {
									externalID = externalIDPrefix + (i + ++offset);
									internalID = typePrefix + externalID;
								}
								usedIDStrings.add(internalID);

								final boolean isVolumeLimitInM3 = desPurchaseMarket.getVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3 ? true : false;

								final ILoadOption desPurchaseSlot = builder.createDESPurchaseLoadSlot(internalID, null, twUTCPlus,
										OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()), OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()),
										priceCalculator, cargoCVValue, 0, IPortSlot.NO_PRICING_DATE, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3);

								// Create a fake model object to add in here;
								final SpotLoadSlot desSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								desSlot.setDESPurchase(true);
								desSlot.setName(externalID);
								desSlot.setArriveCold(false);
								desSlot.setWindowStart(LocalDate.of(startTime.getYear(), startTime.getMonthValue(), startTime.getDayOfMonth()));
								desSlot.setWindowStartTime(0);
								// desSlot.setContract(desPurchaseMarket.getContract());
								desSlot.setOptional(true);
								desSlot.setWindowSize(1);
								desSlot.setWindowSizeUnits(TimePeriod.MONTHS);
								// Key piece of information
								desSlot.setMarket(desPurchaseMarket);
								modelEntityMap.addModelObject(desSlot, desPurchaseSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(desSlot, desPurchaseSlot);
								}

								final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
								for (final IPort port : marketPorts) {
									// Use the UTC based time window
									final int twStart = timeZoneToUtcOffsetProvider.localTime(twUTC.getInclusiveStart(), port);
									final int twEnd = timeZoneToUtcOffsetProvider.localTime(twUTC.getExclusiveEnd(), port);
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

				tzStartTime = tzStartTime.plusMonths(1);
			}
		}
	}

	private void buildFOBSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final ZonedDateTime earliestDate, final ZonedDateTime latestDate, final SpotMarketGroup fobSalesSpotMarket) {
		if (fobSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = fobSalesSpotMarket.getAvailability();

			/** Loop over the date range in the optimisation generating market slots */
			// Get the YearMonth of the earliest date in the scenario.
			final YearMonth initialYearMonth = YearMonth.from(earliestDate.withZoneSameLocal(ZoneId.of("UTC")).toLocalDate());

			// Convert this to the 1st of the month in the notional port timezone.
			ZonedDateTime tzStartTime = initialYearMonth.atDay(1).atStartOfDay(ZoneId.of("UTC"));
			while (!tzStartTime.isAfter(latestDate)) {

				// Convert into timezoneless date objects (for EMF slot object)
				final LocalDate startTime = tzStartTime.toLocalDate();
				// Calculate the upper bound.
				final LocalDate endTime = startTime.plusMonths(1);
				// Get the year/month key
				final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

				// Calculate timezone end date for time window
				final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : fobSalesSpotMarket.getMarkets()) {
					assert market instanceof FOBSalesMarket;
					if (market instanceof FOBSalesMarket && market.isEnabled() == true) {
						final FOBSalesMarket fobSaleMarket = (FOBSalesMarket) market;

						final LNGPriceCalculatorParameters priceInfo = fobSaleMarket.getPriceInfo();
						assert priceInfo != null;
						final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
						final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(null, priceInfo);

						final Set<Port> portSet = SetUtils.getObjects(fobSaleMarket.getOriginPorts());
						final Set<IPort> marketPorts = new HashSet<IPort>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup((Port) ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {
								// As we have no port we create two timewindows. One is pure UTC which we base the EMF Slot date on and shift for the slot binding. The second is UTC with a +/- 12 flex
								// for timezones passed into the optimiser slot. This combination allows the slot to be matched against any slot in the same month in any timezone, but be restricted to
								// match the month boundary in that timezone.

								// This should probably be fixed in ScheduleBuilder#matchingWindows and elsewhere if needed, but subtract one to avoid e.g. 1st Feb 00:00 being permitted in the Jan
								// month block
								final ITimeWindow twUTC = TimeWindowMaker.createInclusiveExclusive(dateHelper.convertTime(tzStartTime), dateHelper.convertTime(tzEndTime), 0, false);
								final ITimeWindow twUTCPlus = createUTCPlusTimeWindow(dateHelper.convertTime(tzStartTime), dateHelper.convertTime(tzEndTime));

								final String typePrefix = "FS-";
								final String externalIDPrefix = market.getName() + "-" + yearMonthString + "-";

								// Avoid ID clash
								String externalID = externalIDPrefix + (i + offset);
								String internalID = typePrefix + externalID;
								while (usedIDStrings.contains(internalID)) {
									externalID = externalIDPrefix + (i + ++offset);
									internalID = typePrefix + externalID;
								}
								usedIDStrings.add(internalID);

								final long minCv = 0;
								final long maxCv = Long.MAX_VALUE;

								final boolean isVolumeLimitInM3 = fobSaleMarket.getVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3 ? true : false;

								final IDischargeOption fobSaleSlot = builder.createFOBSaleDischargeSlot(internalID, null, twUTCPlus,
										OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()), OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), minCv, maxCv,
										priceCalculator, 0, IPortSlot.NO_PRICING_DATE, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3);

								// Create a fake model object to add in here;
								final SpotDischargeSlot fobSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								fobSlot.setFOBSale(true);
								fobSlot.setName(externalID);
								fobSlot.setWindowStart(startTime);
								fobSlot.setWindowStartTime(0);
								// fobSlot.setContract(fobSaleMarket.getContract());
								fobSlot.setOptional(true);
								fobSlot.setWindowSize(1);
								fobSlot.setWindowSizeUnits(TimePeriod.MONTHS);
								// Key piece of information
								fobSlot.setMarket(fobSaleMarket);
								modelEntityMap.addModelObject(fobSlot, fobSaleSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(fobSlot, fobSaleSlot);
								}

								final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
								for (final IPort port : marketPorts) {
									// Use the UTC based time window
									final int twStart = timeZoneToUtcOffsetProvider.localTime(twUTC.getInclusiveStart(), port);
									final int twEnd = timeZoneToUtcOffsetProvider.localTime(twUTC.getExclusiveEnd(), port);
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

				tzStartTime = tzStartTime.plusMonths(1);
				;
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
	@NonNull
	private ITimeWindow createUTCPlusTimeWindow(final int inclusiveStartTime, final int exclusiveEndTime) {
		return TimeWindowMaker.createInclusiveExclusive(inclusiveStartTime - 12, exclusiveEndTime + 14, 0, false);
	}

	private void buildDESSalesSpotMarket(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final Collection<IContractTransformer> contractTransformers, @NonNull final ModelEntityMap modelEntityMap, @NonNull final ZonedDateTime earliestDate,
			@NonNull final ZonedDateTime latestDate, @Nullable final SpotMarketGroup desSalesSpotMarket) {
		if (desSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = desSalesSpotMarket.getAvailability();

			final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

			for (final SpotMarket market : desSalesSpotMarket.getMarkets()) {
				assert market instanceof DESSalesMarket;
				if (market instanceof DESSalesMarket && market.isEnabled() == true) {
					final DESSalesMarket desSalesMarket = (DESSalesMarket) market;

					final LNGPriceCalculatorParameters priceInfo = desSalesMarket.getPriceInfo();
					assert priceInfo != null;
					final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
					final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(null, priceInfo);

					final Port notionalAPort = desSalesMarket.getNotionalPort();
					assert notionalAPort != null;
					final IPort notionalIPort = portAssociation.lookupNullChecked(notionalAPort);

					/** Loop over the date range in the optimisation generating market slots */
					// Get the YearMonth of the earliest date in the scenario.
					final YearMonth initialYearMonth = YearMonth.from(earliestDate.withZoneSameLocal(ZoneId.of("UTC")).toLocalDate());
					// Convert this to the 1st of the month in the notional port timezone.
					ZonedDateTime tzStartTime = initialYearMonth.atDay(1).atStartOfDay(ZoneId.of(notionalAPort.getTimeZone()));
					// Loop!
					while (!tzStartTime.isAfter(latestDate)) {

						// Convert into timezoneless date objects (for EMF slot object)
						final LocalDate startTime = tzStartTime.toLocalDate();
						// Calculate the upper bound.
						final LocalDate endTime = startTime.plusMonths(1);
						// Get the year/month key
						final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

						// Calculate timezone end date for time window
						final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

						final Collection<Slot> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = TimeWindowMaker.createInclusiveExclusive(dateHelper.convertTime(tzStartTime), dateHelper.convertTime(tzEndTime), 0, false);

								final String typePrefix = "DS-";
								final String externalIDPrefix = market.getName() + "-" + yearMonthString + "-";

								// Avoid ID clash
								String externalID = externalIDPrefix + (i + offset);
								String internalID = typePrefix + externalID;
								while (usedIDStrings.contains(internalID)) {
									externalID = externalIDPrefix + (i + ++offset);
									internalID = typePrefix + externalID;
								}
								usedIDStrings.add(internalID);

								// Create a fake model object to add in here;
								final SpotDischargeSlot desSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								desSlot.setName(externalID);
								desSlot.setWindowStart(startTime);
								desSlot.setWindowStartTime(0);
								// desSlot.setContract(desSalesMarket.getContract());
								desSlot.setOptional(true);
								desSlot.setPort((Port) notionalAPort);
								desSlot.setWindowSize(1);
								desSlot.setWindowSizeUnits(TimePeriod.MONTHS);

								final int pricingDate = getSlotPricingDate(desSlot);

								final long minVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity());
								final long maxVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity());

								final boolean isVolumeLimitInM3 = desSalesMarket.getVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3 ? true : false;

								final IDischargeOption desSalesSlot = builder.createDischargeSlot(internalID, notionalIPort, tw, minVolume, maxVolume, 0, Long.MAX_VALUE, priceCalculator,
										desSlot.getSlotOrPortDuration(), pricingDate, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3);

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

						// Increment for loop
						tzStartTime = tzStartTime.plusMonths(1);
					}
				}
			}
		}
	}

	private void buildFOBPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap modelEntityMap, final ZonedDateTime earliestDate, final ZonedDateTime latestDate, final SpotMarketGroup fobPurchaseSpotMarket) {
		if (fobPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = fobPurchaseSpotMarket.getAvailability();

			final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

			for (final SpotMarket market : fobPurchaseSpotMarket.getMarkets()) {
				assert market instanceof FOBPurchasesMarket;
				if (market instanceof FOBPurchasesMarket && market.isEnabled() == true) {
					final FOBPurchasesMarket fobPurchaseMarket = (FOBPurchasesMarket) market;

					final LNGPriceCalculatorParameters priceInfo = fobPurchaseMarket.getPriceInfo();
					assert priceInfo != null;

					final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
					assert transformer != null;

					final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, priceInfo);
					assert priceCalculator != null;

					final Port notionalAPort = fobPurchaseMarket.getNotionalPort();
					assert notionalAPort != null;
					final IPort notionalIPort = portAssociation.lookupNullChecked((Port) notionalAPort);

					/** Loop over the date range in the optimisation generating market slots */
					// Get the YearMonth of the earliest date in the scenario.
					final YearMonth initialYearMonth = YearMonth.from(earliestDate.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

					// Convert this to the 1st of the month in the notional port timezone.
					ZonedDateTime tzStartTime = initialYearMonth.atDay(1).atStartOfDay(ZoneId.of(notionalAPort.getTimeZone()));

					while (!tzStartTime.isAfter(latestDate)) {

						// Convert into timezoneless date objects (for EMF slot object)
						final LocalDate startTime = tzStartTime.toLocalDate();
						// Calculate the upper bound.
						final LocalDate endTime = startTime.plusMonths(1);
						// Get the year/month key
						final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

						// Calculate timezone end date for time window
						final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

						final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(fobPurchaseMarket.getCv());

						final Collection<Slot> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = TimeWindowMaker.createInclusiveExclusive(dateHelper.convertTime(tzStartTime), dateHelper.convertTime(tzEndTime), 0, false);
								final String typePrefix = "FP-";
								final String externalIDPrefix = market.getName() + "-" + yearMonthString + "-";

								// Avoid ID clash
								String externalID = externalIDPrefix + (i + offset);
								String internalID = typePrefix + externalID;
								while (usedIDStrings.contains(internalID)) {
									externalID = externalIDPrefix + (i + ++offset);
									internalID = typePrefix + externalID;
								}
								usedIDStrings.add(internalID);

								final boolean isVolumeLimitInM3 = fobPurchaseMarket.getVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3 ? true : false;

								// Create a fake model object to add in here;
								final SpotLoadSlot fobSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								fobSlot.setName(externalID);
								fobSlot.setWindowStart(startTime);
								fobSlot.setWindowStartTime(0);
								// fobSlot.setContract(fobPurchaseMarket.getContract());
								fobSlot.setOptional(true);
								fobSlot.setArriveCold(true);
								// fobSlot.setCargoCV(fobPurchaseMarket.getCv());
								fobSlot.setPort((Port) notionalAPort);
								fobSlot.setWindowSize(1);
								fobSlot.setWindowSizeUnits(TimePeriod.MONTHS);

								final ILoadOption fobPurchaseSlot = builder.createLoadSlot(internalID, notionalIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, fobSlot.getSlotOrPortDuration(), true, true,
										IPortSlot.NO_PRICING_DATE, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3);

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

						tzStartTime = tzStartTime.plusMonths(1);
					}
				}
			}

		}
	}

	private int getAvailabilityForDate(final SpotAvailability availability, final LocalDate startTime) {
		boolean valueSet = false;
		int count = 0;
		if (availability != null) {
			if (availability.isSetCurve()) {
				final Index<Integer> curve = availability.getCurve();

				final Integer value = curve.getValueForMonth(YearMonth.of(startTime.getYear(), startTime.getMonthValue()));
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

		final SpotMarketsModel spotMarketsModel = rootObject.getReferenceModel().getSpotMarketsModel();
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

					final IMarkToMarket optMarket = builder.createDESPurchaseMTM(marketPorts, cargoCVValue, priceCalculator,
							modelEntityMap.getOptimiserObjectNullChecked(market.getEntity(), IEntity.class));
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

					final BaseLegalEntity eEntity = market.getEntity();
					assert eEntity != null;
					final IMarkToMarket optMarket = builder.createDESSalesMTM(marketPorts, priceCalculator, modelEntityMap.getOptimiserObjectNullChecked(eEntity, IEntity.class));
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
					final IMarkToMarket optMarket = builder.createFOBSaleMTM(marketPorts, priceCalculator, modelEntityMap.getOptimiserObjectNullChecked(market.getEntity(), IEntity.class));
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

					final IMarkToMarket optMarket = builder.createFOBPurchaseMTM(marketPorts, cargoCVValue, priceCalculator,
							modelEntityMap.getOptimiserObjectNullChecked(market.getEntity(), IEntity.class));
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
	private void buildDistances(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final List<IPort> allPorts,
			@NonNull final Map<IPort, Integer> portIndices, @NonNull final Association<Vessel, IVessel> vesselAssociation, @NonNull final Association<VesselClass, IVesselClass> vesselClassAssociation,
			@NonNull final ModelEntityMap modelEntityMap) throws IncompleteScenarioException {

		final LinkedHashSet<RouteOption> orderedKeys = Sets.newLinkedHashSet();

		orderedKeys.add(RouteOption.DIRECT);
		orderedKeys.add(RouteOption.SUEZ);
		if (LicenseFeatures.isPermitted("features:panama-canal")) {
			orderedKeys.add(RouteOption.PANAMA);
		}

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create the default distance matrix.
		 */
		final Set<RouteOption> seenRoutes = new HashSet<>();
		final PortModel portModel = rootObject.getReferenceModel().getPortModel();
		for (final Route r : portModel.getRoutes()) {
			seenRoutes.add(r.getRouteOption());
			// Store Route under it's name
			modelEntityMap.addModelObject(r, mapRouteOption(r).name());
			for (final RouteLine dl : r.getLines()) {
				IPort from, to;
				from = portAssociation.lookupNullChecked(dl.getFrom());
				to = portAssociation.lookupNullChecked(dl.getTo());

				final int distance = dl.getFullDistance();

				builder.setPortToPortDistance(from, to, mapRouteOption(r), distance);
			}

			final Set<IVessel> optimiserVessels = new HashSet<>();
			optimiserVessels.addAll(allVessels.values());
			for (final IVesselAvailability vesselAvailability : allVesselAvailabilities) {
				final IVessel vessel = vesselAvailability.getVessel();
				if (vessel != null) {
					optimiserVessels.add(vessel);
				}
			}

			// set canal route consumptions and toll info
			final CostModel costModel = rootObject.getReferenceModel().getCostModel();

			final PanamaCanalTariff panamaCanalTariff = costModel.getPanamaCanalTariff();
			if (panamaCanalTariff != null) {
				buildPanamaCosts(builder, vesselAssociation, vesselClassAssociation, optimiserVessels, panamaCanalTariff);
				if (panamaCanalTariff.isSetAvailableFrom()) {
					final LocalDate availableFrom = panamaCanalTariff.getAvailableFrom();
					if (availableFrom != null) {
						final int time = dateHelper.convertTime(availableFrom);
						distanceProviderEditor.setRouteAvailableFrom(ERouteOption.PANAMA, time);
					}
				}
			}

			final Map<VesselClass, List<RouteCost>> vesselClassToRouteCostMap = costModel.getRouteCosts().stream() //
					.collect(Collectors.groupingBy(RouteCost::getVesselClass, Collectors.mapping(Function.identity(), Collectors.toList())));

			for (final IVessel vessel : optimiserVessels) {

				final IVesselClass vesselClass = vessel.getVesselClass();
				if (vesselClass != null) {
					final VesselClass eVesselClass = vesselClassAssociation.reverseLookup(vesselClass);
					for (final VesselClassRouteParameters routeParameters : eVesselClass.getRouteParameters()) {
						builder.setVesselRouteTransitTime(mapRouteOption(routeParameters.getRoute()), vessel, routeParameters.getExtraTransitTime());

						builder.setVesselRouteFuel(mapRouteOption(routeParameters.getRoute()), vessel, VesselState.Laden,
								OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getLadenConsumptionRate()),
								OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getLadenNBORate()));

						builder.setVesselRouteFuel(mapRouteOption(routeParameters.getRoute()), vessel, VesselState.Ballast,
								OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getBallastConsumptionRate()),
								OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getBallastNBORate()));
					}

					final List<RouteCost> routeCosts = vesselClassToRouteCostMap.get(eVesselClass);
					if (routeCosts != null) {
						// Some unit tests have null data state
						for (final RouteCost routeCost : routeCosts) {

							if (panamaCanalTariff != null && routeCost.getRoute().getRouteOption() == RouteOption.PANAMA) {
								// continue;
							} else {

								builder.setVesselRouteCost(mapRouteOption(routeCost.getRoute()), vessel, CostType.Laden, OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getLadenCost()));

								builder.setVesselRouteCost(mapRouteOption(routeCost.getRoute()), vessel, CostType.Ballast,
										OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost()));

								builder.setVesselRouteCost(mapRouteOption(routeCost.getRoute()), vessel, CostType.RoundTripBallast,
										OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost()));
							}
						}
					}
				}
			}
		}
		// Filter out unused routes
		orderedKeys.retainAll(seenRoutes);

		// Fix sort order for distance iteration
		final String[] preSortedKeys = orderedKeys.stream() //
				.map(RouteOption::getName)//
				.collect(Collectors.toList()) //
				.toArray(new String[orderedKeys.size()]);
		portDistanceProvider.setPreSortedKeys(preSortedKeys);

	}

	public static void buildPanamaCosts(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Vessel, IVessel> vesselAssociation,
			@NonNull final Association<VesselClass, IVesselClass> vesselClassAssociation, final Collection<IVessel> vesselAvailabilities, @NonNull final PanamaCanalTariff panamaCanalTariff) {

		// Extract band information into a sorted list
		final List<Pair<Integer, PanamaCanalTariffBand>> bands = new LinkedList<>();
		for (final PanamaCanalTariffBand band : panamaCanalTariff.getBands()) {
			int upperBound = Integer.MAX_VALUE;
			if (band.isSetBandEnd()) {
				upperBound = band.getBandEnd();
			}
			bands.add(new Pair<>(upperBound, band));
		}
		// Sort the bands smallest to largest
		Collections.sort(bands, (b1, b2) -> b1.getFirst().compareTo(b2.getFirst()));

		for (final IVessel vessel : vesselAvailabilities) {
			final int capacityInM3;
			assert vessel != null;
			final Vessel eVessel = vesselAssociation.reverseLookup(vessel);
			if (eVessel == null) {
				// spot charter
				final VesselClass eVesselClass = vesselClassAssociation.reverseLookupNullChecked(vessel.getVesselClass());
				capacityInM3 = eVesselClass.getCapacity();
			} else {
				capacityInM3 = eVessel.getVesselOrVesselClassCapacity();
			}
			double totalLadenCost = 0.0;
			double totalBallastCost = 0.0;
			double totalBallastRoundTripCost = 0.0;
			for (final Pair<Integer, PanamaCanalTariffBand> p : bands) {
				final PanamaCanalTariffBand band = p.getSecond();
				//// How much vessel capacity is used for this band calculation?
				// First, find the largest value valid in this band
				double contributingCapacity = Math.min(capacityInM3, p.getFirst());

				// Next, subtract band lower bound to find the capacity contribution
				contributingCapacity = Math.max(0, contributingCapacity - (band.isSetBandStart() ? band.getBandStart() : 0));

				if (contributingCapacity > 0) {
					totalLadenCost += contributingCapacity * band.getLadenTariff();
					totalBallastCost += contributingCapacity * band.getBallastTariff();
					totalBallastRoundTripCost += contributingCapacity * band.getBallastRoundtripTariff();
				}
			}

			// If there is a markup %, apply it
			if (panamaCanalTariff.getMarkupRate() != 0.0) {
				final double multiplier = 1.0 + panamaCanalTariff.getMarkupRate();
				totalLadenCost *= multiplier;
				totalBallastCost *= multiplier;
				totalBallastRoundTripCost *= multiplier;
			}

			builder.setVesselRouteCost(ERouteOption.PANAMA, vessel, CostType.Laden, OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalLadenCost)));
			builder.setVesselRouteCost(ERouteOption.PANAMA, vessel, CostType.Ballast, OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalBallastCost)));
			builder.setVesselRouteCost(ERouteOption.PANAMA, vessel, CostType.RoundTripBallast, OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalBallastRoundTripCost)));
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
	private NonNullPair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> buildFleet(@NonNull final ISchedulerBuilder builder,
			@NonNull final Association<Port, IPort> portAssociation, @NonNull final Association<BaseFuelIndex, ICurve> baseFuelIndexAssociation,
			@NonNull final Association<CharterIndex, ILongCurve> charterIndexAssociation, @NonNull final ModelEntityMap modelEntityMap) {

		/*
		 * Build the fleet model - first we must create the vessel classes from the model
		 */
		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<VesselClass, IVesselClass>();
		final Association<Vessel, IVessel> vesselAssociation = new Association<Vessel, IVessel>();
		final Association<VesselAvailability, IVesselAvailability> vesselAvailabilityAssociation = new Association<VesselAvailability, IVesselAvailability>();
		// TODO: Check that we are mutliplying/dividing correctly to maintain
		// precision

		final FleetModel fleetModel = rootObject.getReferenceModel().getFleetModel();
		final PortModel portModel = rootObject.getReferenceModel().getPortModel();

		// look up prices

		for (final VesselClass eVc : fleetModel.getVesselClasses()) {
			assert eVc != null;

			final IBaseFuel bf = modelEntityMap.getOptimiserObjectNullChecked(eVc.getBaseFuel(), IBaseFuel.class);
			final IVesselClass vc = TransformerHelper.buildIVesselClass(builder, eVc, bf);

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

			/*
			 * set up inaccessible routes for vessel class
			 */
			getAndSetInaccessibleRoutesForVesselClass(builder, eVc, vc);
			modelEntityMap.addModelObject(eVc, vc);
		}

		// Sorted by fleet model vessel order
		final List<VesselAvailability> sortedAvailabilities = new ArrayList<>();
		{
			final CargoModel cargoModel = rootObject.getCargoModel();
			for (final Vessel vessel : fleetModel.getVessels()) {

				for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
					if (vesselAvailability.getVessel() == vessel) {
						sortedAvailabilities.add(vesselAvailability);
					}
				}
			}

		}
		/*
		 * Now create each vessel
		 */

		for (final Vessel eVessel : fleetModel.getVessels()) {

			final IVessel vessel = builder.createVessel(eVessel.getName(), vesselClassAssociation.lookupNullChecked(eVessel.getVesselClass()),
					OptimiserUnitConvertor.convertToInternalVolume((int) (eVessel.getVesselOrVesselClassCapacity() * eVessel.getVesselOrVesselClassFillCapacity())));
			vesselAssociation.add(eVessel, vessel);

			final int ballastReferenceSpeed, ladenReferenceSpeed;
			if (shippingDaysRestrictionSpeedProvider == null) {
				ballastReferenceSpeed = ladenReferenceSpeed = vessel.getVesselClass().getMaxSpeed();
			} else {
				ballastReferenceSpeed = OptimiserUnitConvertor.convertToInternalSpeed(shippingDaysRestrictionSpeedProvider.getSpeed(eVessel.getVesselClass(), false /* ballast */));
				ladenReferenceSpeed = OptimiserUnitConvertor.convertToInternalSpeed(shippingDaysRestrictionSpeedProvider.getSpeed(eVessel.getVesselClass(), true /* laden */));
			}
			builder.setShippingDaysRestrictionReferenceSpeed(vessel, VesselState.Ballast, ballastReferenceSpeed);
			builder.setShippingDaysRestrictionReferenceSpeed(vessel, VesselState.Laden, ladenReferenceSpeed);
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

			/*
			 * set up inaccessible routes
			 */
			getAndSetInaccessibleRoutesForVessel(builder, eVessel, vessel);
			vesselToAvailabilities.put(vessel, new LinkedList<IVesselAvailability>());
		}

		// Now register the availabilities.
		for (final VesselAvailability eVesselAvailability : sortedAvailabilities) {
			final Vessel eVessel = eVesselAvailability.getVessel();

			final Set<Port> portSet = SetUtils.getObjects(eVesselAvailability.getStartAt());
			final Port startingPort = portSet.isEmpty() ? null : portSet.iterator().next();
			final IStartRequirement startRequirement = createStartRequirement(builder, portAssociation, eVesselAvailability.isSetStartAfter() ? eVesselAvailability.getStartAfterAsDateTime() : null,
					eVesselAvailability.isSetStartBy() ? eVesselAvailability.getStartByAsDateTime() : null, startingPort, eVesselAvailability.getStartHeel());

			boolean endCold = false;
			long targetEndHeelInM3 = 0;
			final EndHeelOptions endHeel = eVesselAvailability.getEndHeel();
			if (endHeel != null) {
				endCold = endHeel.isSetTargetEndHeel();
				targetEndHeelInM3 = endCold ? OptimiserUnitConvertor.convertToInternalVolume(endHeel.getTargetEndHeel()) : 0;
				if (targetEndHeelInM3 == 0) {
					endCold = false;
				}
			}

			final IEndRequirement endRequirement = createEndRequirement(builder, portAssociation, eVesselAvailability.isSetEndAfter() ? eVesselAvailability.getEndAfterAsDateTime() : null,
					eVesselAvailability.isSetEndBy() ? eVesselAvailability.getEndByAsDateTime() : null, SetUtils.getObjects(eVesselAvailability.getEndAt()), endCold, targetEndHeelInM3,
					eVesselAvailability.isForceHireCostOnlyEndRule());

			final ILongCurve dailyCharterInCurve;
			if (eVesselAvailability.isSetTimeCharterRate()) {
				dailyCharterInCurve = dateHelper.generateLongExpressionCurve(eVesselAvailability.getTimeCharterRate(), charterIndices);
			} else {
				dailyCharterInCurve = new ConstantValueLongCurve(0);
			}
			assert dailyCharterInCurve != null;

			final IVessel vessel = vesselAssociation.lookupNullChecked(eVessel);

			final IVesselAvailability vesselAvailability = builder.createVesselAvailability(vessel, dailyCharterInCurve,
					eVesselAvailability.isSetTimeCharterRate() ? VesselInstanceType.TIME_CHARTER : VesselInstanceType.FLEET, startRequirement, endRequirement);
			vesselAvailabilityAssociation.add(eVesselAvailability, vesselAvailability);

			modelEntityMap.addModelObject(eVesselAvailability, vesselAvailability);

			allVesselAvailabilities.add(vesselAvailability);

			vesselToAvailabilities.get(vessel).add(vesselAvailability);
		}

		// Spot market generation
		{
			final SpotMarketsModel spotMarketsModel = rootObject.getReferenceModel().getSpotMarketsModel();
			int charterCount = 0;

			final CharterOutStartDate charterOutStartDate = spotMarketsModel.getCharterOutStartDate();
			if (charterOutStartDate != null && charterOutStartDate.getCharterOutStartDate() != null) {
				builder.setGeneratedCharterOutStartTime(dateHelper.convertTime(charterOutStartDate.getCharterOutStartDate().atStartOfDay(ZoneId.of("UTC"))));
			} else {
				builder.setGeneratedCharterOutStartTime(0);
			}

			for (final CharterInMarket charterCost : spotMarketsModel.getCharterInMarkets()) {

				final VesselClass eVesselClass = charterCost.getVesselClass();

				final ILongCurve charterInCurve;
				if (charterCost.getCharterInRate() != null) {
					charterInCurve = dateHelper.generateLongExpressionCurve(charterCost.getCharterInRate(), charterIndices);
				} else {
					charterInCurve = new ConstantValueLongCurve(0);
				}

				assert charterInCurve != null;
				charterCount = charterCost.getSpotCharterCount();
				final IVesselClass oVesselClass = vesselClassAssociation.lookupNullChecked(eVesselClass);
				final ISpotCharterInMarket spotCharterInMarket = builder.createSpotCharterInMarket(charterCost.getName(), oVesselClass, charterInCurve, charterCount);
				modelEntityMap.addModelObject(charterCost, spotCharterInMarket);

				{
					final IVesselAvailability roundTripOption = builder.createRoundTripCargoVessel("RoundTrip-" + charterCost.getName(), spotCharterInMarket);
					final NonNullPair<CharterInMarket, Integer> key = new NonNullPair<>(charterCost, -1);
					spotCharterInToAvailability.put(key, roundTripOption);
					allVesselAvailabilities.add(roundTripOption);
					vesselToAvailabilities.put(roundTripOption.getVessel(), Collections.singleton(roundTripOption));

					/*
					 * set up inaccessible routes
					 */
					getAndSetInaccessibleRoutesForSpotCharterInVessel(builder, charterCost, roundTripOption.getVessel());
				}
				if (charterCount > 0 && charterCost.isEnabled()) {

					final List<IVesselAvailability> spots = builder.createSpotVessels("SPOT-" + charterCost.getName(), spotCharterInMarket);
					for (int i = 0; i < spots.size(); ++i) {
						final NonNullPair<CharterInMarket, Integer> key = new NonNullPair<>(charterCost, i);
						final IVesselAvailability spotAvailability = spots.get(i);
						spotCharterInToAvailability.put(key, spotAvailability);

						vesselToAvailabilities.put(spotAvailability.getVessel(), Collections.singleton(spotAvailability));

						/*
						 * set up inaccessible routes
						 */
						getAndSetInaccessibleRoutesForSpotCharterInVessel(builder, charterCost, spotAvailability.getVessel());

					}
					allVesselAvailabilities.addAll(spots);
				}
			}
			for (final CharterOutMarket charterCost : spotMarketsModel.getCharterOutMarkets()) {
				assert charterCost != null;

				if (!charterCost.isEnabled()) {
					continue;
				}

				final VesselClass eVesselClass = charterCost.getVesselClass();

				if (charterCost.getCharterOutRate() != null) {
					final ILongCurve charterOutCurve = dateHelper.generateLongExpressionCurve(charterCost.getCharterOutRate(), charterIndices);
					assert charterOutCurve != null;

					final int minDuration = 24 * charterCost.getMinCharterOutDuration();

					final Set<Port> portSet = SetUtils.getObjects(charterCost.getAvailablePorts());
					final ArrayList<Port> sortedPortSet = new ArrayList<Port>(portSet);
					Collections.sort(sortedPortSet, new Comparator<Port>() {
						@Override
						public int compare(final Port p1, final Port p2) {
							return p1.getName().compareTo(p2.getName());
						}

					});
					final Set<IPort> marketPorts = new LinkedHashSet<IPort>();
					for (final Port ap : sortedPortSet) {
						final IPort ip = portAssociation.lookup((Port) ap);
						if (ip != null) {
							marketPorts.add(ip);
						}
					}

					builder.createCharterOutCurve(vesselClassAssociation.lookupNullChecked(eVesselClass), charterOutCurve, minDuration, marketPorts);
				}
			}
		}

		return new NonNullPair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>>(vesselClassAssociation, vesselAssociation);

	}

	private void getAndSetInaccessibleRoutesForVessel(final ISchedulerBuilder builder, final Vessel eVessel, final IVessel vessel) {
		final Set<ERouteOption> inaccessibleERoutesForVessel = getInaccessibleERoutesForVessel(eVessel);
		setInaccessibleRoutesForVessel(builder, vessel, inaccessibleERoutesForVessel);
	}

	private void getAndSetInaccessibleRoutesForVesselClass(final ISchedulerBuilder builder, final VesselClass eVesselClass, final IVesselClass vesselClass) {
		final Set<ERouteOption> inaccessibleERoutesForVessel = createRouteOptionSet(true, eVesselClass.getInaccessibleRoutes());
		setInaccessibleRoutesForVesselClass(builder, vesselClass, inaccessibleERoutesForVessel);
	}

	private void getAndSetInaccessibleRoutesForSpotCharterInVessel(final ISchedulerBuilder builder, final CharterInMarket charterInMarket, final IVessel vessel) {
		final Set<ERouteOption> inaccessibleERoutesForVessel = createRouteOptionSet(charterInMarket.isOverrideInaccessibleRoutes(), charterInMarket.getInaccessibleRoutes());
		setInaccessibleRoutesForVessel(builder, vessel, inaccessibleERoutesForVessel);
	}

	private void setInaccessibleRoutesForVessel(final ISchedulerBuilder builder, final IVessel vessel, final Set<ERouteOption> inaccessibleERoutesForVessel) {
		builder.setVesselInaccessibleRoutes(vessel, inaccessibleERoutesForVessel);
	}

	private void setInaccessibleRoutesForVesselClass(final ISchedulerBuilder builder, final IVesselClass vesselClass, final Set<ERouteOption> inaccessibleERoutesForVessel) {
		if (!inaccessibleERoutesForVessel.isEmpty()) {
			builder.setVesselClassInaccessibleRoutes(vesselClass, inaccessibleERoutesForVessel);
		}
	}

	public Set<ERouteOption> getInaccessibleERoutesForVessel(final Vessel eVessel) {
		return createRouteOptionSet(eVessel.isOverrideInaccessibleRoutes(), eVessel.getInaccessibleRoutes());
	}

	public Set<ERouteOption> createRouteOptionSet(final boolean canOverride, final Collection<RouteOption> routeOptions) {
		final Set<ERouteOption> inaccessibleERoutes = new HashSet<>();
		if (canOverride) {
			for (final RouteOption routeOption : routeOptions) {
				inaccessibleERoutes.add(mapRouteOption(routeOption));
			}
		}
		return inaccessibleERoutes;
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param pat
	 * @return
	 */
	@NonNull
	private IStartRequirement createStartRequirement(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @Nullable final ZonedDateTime from,
			@Nullable final ZonedDateTime to, @Nullable final Port port, @Nullable final HeelOptions eHeelOptions) {
		ITimeWindow window = null;

		if (from == null && to != null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(dateHelper.getEarliestTime()), dateHelper.convertTime(to), 0, false);
		} else if (from != null && to == null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(from), dateHelper.convertTime(dateHelper.getLatestTime()), 0, false);
		} else if (from != null && to != null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(from), dateHelper.convertTime(to), 0, false);
		}

		IHeelOptions heelOptions;
		if (eHeelOptions != null) {
			final long heelLimitInM3 = eHeelOptions.isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(eHeelOptions.getVolumeAvailable()) : 0;

			final int cvValue = OptimiserUnitConvertor.convertToInternalConversionFactor(eHeelOptions.getCvValue());
			final int pricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(eHeelOptions.getPricePerMMBTU());

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
	 * @param force
	 * @param pat
	 * @return
	 */
	@NonNull
	private IEndRequirement createEndRequirement(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @Nullable final ZonedDateTime from,
			@Nullable final ZonedDateTime to, @Nullable final Set<Port> ports, final boolean endCold, final long targetHeelInM3, final boolean forceHireCostOnlyEndRule) {
		ITimeWindow window = null;

		boolean isOpenEnded = false;
		if (from == null && to != null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(dateHelper.getEarliestTime()), dateHelper.convertTime(to), 0, false);
		} else if (from != null && to == null) {
			// Set a default window end date which is valid change later
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(from), dateHelper.convertTime(from), 0, true);
			builder.addOpenEndWindow((MutableTimeWindow) window);
		} else if (from != null && to != null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(from), dateHelper.convertTime(to), 0, false);
		} else {
			// No window
			isOpenEnded = true;
		}

		final Set<IPort> portSet = new HashSet<IPort>();
		for (final Port p : ports) {
			portSet.add(portAssociation.lookup(p));
		}
		// Is the availability open ended or do we force the end rule?
		final boolean useHireCostOnlyEndRule = forceHireCostOnlyEndRule || isOpenEnded;
		if (ports.isEmpty()) {
			return builder.createEndRequirement(null, window, endCold, targetHeelInM3, useHireCostOnlyEndRule);
		} else {
			return builder.createEndRequirement(portSet, window, endCold, targetHeelInM3, useHireCostOnlyEndRule);
		}

	}

	/**
	 * Add the spot slot to the existing market spot slot counter.
	 * 
	 * @param spotSlot
	 */
	private void addSpotSlotToCount(@NonNull final SpotSlot spotSlot) {
		final SpotMarket market = spotSlot.getMarket();
		if (market == null) {
			log.warn("Spot slot with an invalid market found");
			return;
		}

		final TreeMap<String, Collection<Slot>> curve;
		if (existingSpotCount.containsKey(market)) {
			curve = existingSpotCount.get(market);
		} else {
			curve = new TreeMap<String, Collection<Slot>>();
			existingSpotCount.put(market, curve);
		}
		if (spotSlot instanceof Slot) {
			final Slot slot = (Slot) spotSlot;
			final String key = SpotSlotUtils.getKeyForDate(slot.getWindowStart());
			final Collection<Slot> slots;
			if (curve.containsKey(key)) {
				slots = curve.get(key);
			} else {
				slots = new LinkedList<>();
				curve.put(key, slots);
			}
			slots.add(slot);
		}
	}

	@NonNull
	private Collection<Slot> getSpotSlots(@NonNull final SpotMarket spotMarket, @NonNull final String key) {

		if (existingSpotCount.containsKey(spotMarket)) {
			final TreeMap<String, Collection<Slot>> curve = existingSpotCount.get(spotMarket);
			if (curve.containsKey(key)) {
				final Collection<Slot> slots = curve.get(key);
				if (slots != null) {
					return slots;
				}
			}
		}
		return Collections.emptyList();
	}

	@NonNull
	private PricingEventType transformPricingEvent(@NonNull final PricingEvent event) {
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

	private int getSlotPricingDate(@NonNull final Slot slot) {
		int pricingDate;
		if (slot.isSetPricingDate()) {
			final ZonedDateTime dateTime = slot.getPricingDateAsDateTime();
			assert dateTime != null;
			pricingDate = dateHelper.convertTime(dateTime);
		} else {
			pricingDate = IPortSlot.NO_PRICING_DATE;
		}
		return pricingDate;
	}

	@NonNull
	public static ERouteOption mapRouteOption(@NonNull final Route route) {
		final RouteOption routeOption = route.getRouteOption();
		switch (routeOption) {
		case DIRECT:
			return ERouteOption.DIRECT;
		case PANAMA:
			return ERouteOption.PANAMA;
		case SUEZ:
			return ERouteOption.SUEZ;
		}
		throw new IllegalStateException();
	}

	@NonNull
	public static ERouteOption mapRouteOption(final RouteOption routeOption) {
		switch (routeOption) {
		case DIRECT:
			return ERouteOption.DIRECT;
		case PANAMA:
			return ERouteOption.PANAMA;
		case SUEZ:
			return ERouteOption.SUEZ;
		}
		throw new IllegalStateException();
	}

}
