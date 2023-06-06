/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.inject.Named;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.common.curves.PreGeneratedLongCurve;
import com.mmxlabs.common.curves.WrappedParameterisedCurve;
import com.mmxlabs.common.parser.series.EmptySeries;
import com.mmxlabs.common.parser.series.ILazyExpressionContainer;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesType;
import com.mmxlabs.common.parser.series.ThreadLocalLazyExpressionContainer;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.IShippingDaysRestrictionSpeedProvider;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.cargo.util.SpotSlotUtils;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselRouteParameters;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PanamaTariffV2;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.YearMonthPointContainer;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.contracts.ICharterContractTransformer;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.models.lng.transformer.contracts.IVesselCharterTransformer;
import com.mmxlabs.models.lng.transformer.contracts.IVesselEventTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.TransformerHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.TimeWindowMaker;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultSpotCharterOutMarket;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultSpotMarket;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.ExpressionHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.ChangeablePriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.CooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PortfolioBreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PortfolioBreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelCurveProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICancellationFeeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyWindowProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerEditor;
import com.mmxlabs.scheduler.optimiser.providers.ILoadPriceCalculatorProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ILockedCargoProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IMiscCostsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPriceExpressionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IThirdPartyCargoProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.PriceCurveKey;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

/**
 * Wrapper for an EMF LNG Scheduling {@link MMXRootObject}, providing utility
 * methods to convert it into an optimisation job. Typical usage is to construct
 * an LNGScenarioTransformer with a given Scenario, and then call the
 * {@link createOptimisationData} method. It is only expected that an instance
 * will be used once. I.e. a single call to
 * {@link #createOptimisationData(ModelEntityMap)}
 * 
 * @author hinton, Simon Goodall
 * 
 */
public class LNGScenarioTransformer {

	/**
	 * Constant used to inject a limit for spot slot creation. Negative numbers do
	 * not apply a cap.
	 */
	public static final String LIMIT_SPOT_SLOT_CREATION = "limit-spot-slot-creation";

	private static final int NOMINAL_CARGO_INDEX = -1;

	private static final Logger LOG = LoggerFactory.getLogger(LNGScenarioTransformer.class);

	private final @NonNull LNGScenarioModel rootObject;

	@Inject
	@Named(ExtraDataProvider.EXTRA_VESSEL_CHARTERS)
	private List<VesselCharter> extraVesselCharters;

	@Inject
	@Named(ExtraDataProvider.EXTRA_VESSEL_EVENTS)
	private List<VesselEvent> extraVesselEvents;

	@Inject
	@Named(ExtraDataProvider.EXTRA_CHARTER_IN_MARKETS)
	private List<CharterInMarket> extraCharterInMarkets;

	@Inject
	@Named(ExtraDataProvider.EXTRA_SPOT_CARGO_MARKETS)
	private List<SpotMarket> extraSpotMarkets;

	@Inject
	@Named(ExtraDataProvider.EXTRA_CHARTER_IN_MARKET_OVERRIDES)
	private List<CharterInMarketOverride> extraCharterInMarketOverrides;

	@Inject
	@Named(ExtraDataProvider.EXTRA_LOAD_SLOTS)
	private List<LoadSlot> extraLoadSlots;

	@Inject
	@Named(ExtraDataProvider.EXTRA_DISCHARGE_SLOTS)
	private List<DischargeSlot> extraDischargeSlots;

	@Inject(optional = true)
	@Named(ExtraDataProvider.EXTRA_PRICE_CURVES)
	private List<CommodityCurveOverlay> extraPriceCurves;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	@Named(SchedulerConstants.Parser_BaseFuel)
	private SeriesParser baseFuelIndices;

	@Inject
	@Named(SchedulerConstants.Parser_Charter)
	private SeriesParser charterIndices;

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject
	@Named(SchedulerConstants.Parser_Currency)
	private SeriesParser currencyIndices;

	@Inject(optional = true)
	@Nullable
	private List<ITransformerExtension> transformerExtensions;

	@Inject
	private ISchedulerBuilder builder;

	@Inject
	private ILoadPriceCalculatorProviderEditor loadPriceCalculatorProvider;

	@Inject
	private IShipToShipBindingProviderEditor shipToShipBindingProvider;

	@Inject
	private Injector injector;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IDistanceProviderEditor distanceProviderEditor;

	@Inject
	private TimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject(optional = true)
	@Nullable
	private IShippingDaysRestrictionSpeedProvider shippingDaysRestrictionSpeedProvider;

	@Inject
	private IBaseFuelCurveProviderEditor baseFuelCurveProvider;

	@Inject
	private IPromptPeriodProviderEditor promptPeriodProviderEditor;

	@Inject
	private ILockedCargoProviderEditor lockedCargoProviderEditor;

	@Inject
	private IPriceExpressionProviderEditor priceExpressionProviderEditor;

	@Inject
	private ILazyExpressionManagerEditor lazyExpressionManagerEditor;

	/**
	 * Contains the contract transformers for each known contract type, by the
	 * EClass of the contract they transform.
	 */
	@NonNull
	private final Map<EClass, IContractTransformer> contractTransformersByEClass = new LinkedHashMap<>();

	/**
	 * A set of all contract transformers being used; these should be mapped to in
	 * {@link #contractTransformersByEClass}
	 */
	@NonNull
	private final Set<IContractTransformer> contractTransformers = new LinkedHashSet<>();

	/**
	 * A set of all vessel event transformers being used;
	 */
	@NonNull
	private final Set<IVesselEventTransformer> vesselEventTransformers = new LinkedHashSet<>();

	/**
	 * A set of all vessel availability transformers being used;
	 */
	@NonNull
	private final Set<IVesselCharterTransformer> vesselCharterTransformers = new LinkedHashSet<>();

	/**
	 * A set of all transformer extensions being used (should contain
	 * {@link #contractTransformers})
	 */
	@NonNull
	private final Set<ITransformerExtension> allTransformerExtensions = new LinkedHashSet<>();

	@NonNull
	private final Map<NonNullPair<CharterInMarket, Integer>, IVesselCharter> spotCharterInToAvailability = new HashMap<>();
	private final Map<CharterInMarket, IVesselCharter> spotCharterInToShortCargoAvailability = new HashMap<>();

	@NonNull
	private final List<IVesselCharter> allVesselAvailabilities = new ArrayList<>();

	@NonNull
	private final Map<Vessel, IVessel> allVessels = new HashMap<>();

	/**
	 * The {@link Set} of ID strings already used. The UI should restrict user
	 * entered data items from clashing, but generated ID's may well clash with user
	 * ones.
	 */
	@NonNull
	private final Set<String> usedIDStrings = new HashSet<>();

	/**
	 * A {@link Map} of existing spot market slots by ID. This map is used later
	 * when building the spot market options.
	 */
	@NonNull
	private final Map<String, @NonNull Slot<?>> marketSlotsByID = new HashMap<>();

	@NonNull
	private final Map<SpotMarket, TreeMap<String, Collection<Slot<?>>>> existingSpotCount = new HashMap<>();

	@Inject
	private ISpotMarketSlotsProviderEditor spotMarketSlotsProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	@Named(LNGTransformerHelper.HINT_SHIPPING_ONLY)
	private boolean shippingOnly;

	@Inject
	@Named(LNGTransformerHelper.HINT_SPOT_CARGO_MARKETS)
	private boolean withSpotCargoMarkets;

	@Inject
	@Named(LNGTransformerHelper.HINT_PORTFOLIO_BREAKEVEN)
	private boolean portfolioBreakevenFlag;

	@Inject
	private IPortVisitDurationProviderEditor portVisitDurationProviderEditor;

	@Inject
	private IMiscCostsProviderEditor miscCostsProviderEditor;

	@Inject
	private ICancellationFeeProviderEditor cancellationFeeProviderEditor;

	@Inject
	private ICounterPartyWindowProviderEditor counterPartyWindowProviderEditor;

	@Inject
	private IThirdPartyCargoProviderEditor thirdPartyCargoProviderEditor;

	@Inject
	@Named(LIMIT_SPOT_SLOT_CREATION)
	private int spotSlotCreationCap;

	private final UserSettings userSettings;

	/**
	 * A set of all ballast bonus contract transformers being used;
	 */
	@NonNull
	private final Set<ICharterContractTransformer> ballastBonusContractTransformers = new LinkedHashSet<>();

	private final Set<ISlotTransformer> slotTransformers = new LinkedHashSet<>();

	/**
	 * Create a transformer for the given scenario; the class holds a reference, so
	 * changes made to the scenario after construction will be reflected in calls to
	 * the various helper methods.
	 * 
	 * @param scenario
	 */
	@Inject
	public LNGScenarioTransformer(@NonNull final LNGScenarioModel rootObject, final UserSettings userSettings) {

		this.rootObject = rootObject;
		this.userSettings = userSettings;
	}

	/**
	 * Get any {@link ITransformerExtension} and {@link IContractTransformer}s from
	 * the platform's registry.
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

			if (transformer instanceof final IContractTransformer contractTransformer) {
				addContractTransformer(contractTransformer);
			}
			if (transformer instanceof final ISlotTransformer slotTransformer) {
				addSlotTransformer(slotTransformer);
			}
			if (transformer instanceof final IVesselCharterTransformer vesselCharterTransformer) {
				addVesselCharterTransformer(vesselCharterTransformer);
			}
			if (transformer instanceof final IVesselEventTransformer vesselEventTransformer) {
				addVesselEventTransformer(vesselEventTransformer);
			}
			if (transformer instanceof final ICharterContractTransformer ballastBonusContractTransformer) {
				addBallastBonusContractTransformer(ballastBonusContractTransformer);
			}
		}

		return true;
	}

	public void addTransformerExtension(@NonNull final ITransformerExtension extension) {
		allTransformerExtensions.add(extension);
	}

	private void addSlotTransformer(final ISlotTransformer slotTransformer) {
		slotTransformers.add(slotTransformer);
	}

	/**
	 */
	public void addContractTransformer(@NonNull final IContractTransformer transformer) {
		contractTransformers.add(transformer);
		for (final EClass ec : transformer.getContractEClasses()) {
			contractTransformersByEClass.put(ec, transformer);
		}
	}

	public void addVesselCharterTransformer(@NonNull final IVesselCharterTransformer transformer) {
		vesselCharterTransformers.add(transformer);
	}

	public void addBallastBonusContractTransformer(@NonNull final ICharterContractTransformer transformer) {
		ballastBonusContractTransformers.add(transformer);
	}

	public void addVesselEventTransformer(@NonNull final IVesselEventTransformer transformer) {
		vesselEventTransformers.add(transformer);
	}

	/**
	 * Instantiates and returns an {@link IOptimisationData} isomorphic to the
	 * contained scenario.
	 * 
	 * @return
	 * @throws IncompleteScenarioException
	 */
	@NonNull
	public IOptimisationData createOptimisationData(@NonNull final ModelEntityMap modelEntityMap) {

		timeZoneToUtcOffsetProvider.setTimeZeroInMillis(Instant.from(dateHelper.getEarliestTime()).toEpochMilli());

		// Use Math.max(0, arg) to avoid negative time values for prompt/horizon dates
		// which are earlier than time zero. E.g. the scheduling horizon feeds into time
		// windows

		if (rootObject.getPromptPeriodStart() != null) {
			promptPeriodProviderEditor.setStartOfPromptPeriod(Math.max(0, dateHelper.convertTime(rootObject.getPromptPeriodStart())));
		}
		if (rootObject.getPromptPeriodEnd() != null) {
			promptPeriodProviderEditor.setEndOfPromptPeriod(Math.max(0, dateHelper.convertTime(rootObject.getPromptPeriodEnd())));
		}

		if (rootObject.isSetSchedulingEndDate()) {
			promptPeriodProviderEditor.setEndOfSchedulingPeriod(Math.max(0, dateHelper.convertTime(rootObject.getSchedulingEndDate())));
		}

		if (userSettings.isSetPeriodStartDate() && userSettings.getPeriodStartDate() != null) {
			promptPeriodProviderEditor.setPeriodOptimisation(true);
			promptPeriodProviderEditor.setStartOfOptimisationPeriod(dateHelper.convertTime(userSettings.getPeriodStartDate()));
		}
		if (userSettings.isSetPeriodEnd() && userSettings.getPeriodEnd() != null) {
			promptPeriodProviderEditor.setPeriodOptimisation(true);
			promptPeriodProviderEditor.setEndOfOptimisationPeriod(dateHelper.convertTime(userSettings.getPeriodEnd()));
		}

		/**
		 * First, create all the market curves (should these come through the builder?)
		 */

		final Association<CommodityCurve, IParameterisedCurve> commodityIndexAssociation = new Association<>();
		final Association<BunkerFuelCurve, ICurve> baseFuelIndexAssociation = new Association<>();
		final Association<CharterCurve, ILongCurve> charterIndexAssociation = new Association<>();

		final PricingModel pricingModel = rootObject.getReferenceModel().getPricingModel();
		// For each curve, register with the SeriesParser object
		for (final CommodityCurve commodityIndex : pricingModel.getCommodityCurves()) {
			final String name = commodityIndex.getName();
			assert name != null;
			registerCommodityIndex(name, commodityIndex, commodityIndices);
		}
		for (final CommodityCurve commodityIndex : pricingModel.getFormulaeCurves()) {
			final String name = commodityIndex.getName();
			assert name != null;
			registerCommodityIndex(name, commodityIndex, commodityIndices);
		}
		for (final BunkerFuelCurve baseFuelIndex : pricingModel.getBunkerFuelCurves()) {
			registerIndex(baseFuelIndex.getName(), SeriesType.BUNKERS, baseFuelIndex, baseFuelIndices);
		}
		for (final CharterCurve charterIndex : pricingModel.getCharterCurves()) {
			registerIndex(charterIndex.getName(), SeriesType.CHARTER, charterIndex, charterIndices);
		}

		// Currency is added to all the options
		for (final CurrencyCurve currencyIndex : pricingModel.getCurrencyCurves()) {
			final String name = currencyIndex.getName();
			if (name != null) {
				registerIndex(name, SeriesType.CURRENCY, currencyIndex, commodityIndices);
				registerIndex(name, SeriesType.CURRENCY, currencyIndex, baseFuelIndices);
				registerIndex(name, SeriesType.CURRENCY, currencyIndex, charterIndices);
				registerIndex(name, SeriesType.CURRENCY, currencyIndex, currencyIndices);
			}
		}

		for (final UnitConversion factor : pricingModel.getConversionFactors()) {
			registerConversionFactor(factor, commodityIndices, baseFuelIndices, charterIndices, currencyIndices);
		}

		// Now pre-compute our various curve data objects...
		for (final CommodityCurve index : pricingModel.getCommodityCurves()) {
			try {
				IParameterisedCurve curve;
				if (index.isSetExpression()) {
					final ISeries parsed = commodityIndices.getSeries(index.getName()).get();
					curve = dateHelper.generateParameterisedExpressionCurve(parsed);
				} else {
					curve = new WrappedParameterisedCurve(dateHelper.constructConcreteCurve(index));
				}

				modelEntityMap.addModelObject(index, curve);
				commodityIndexAssociation.add(index, curve);
				if (commodityIndices.getSeries(index.getName()) instanceof ILazyExpressionContainer) {
					// Only the lazy curves need to be added - the series parser should already have
					// initialised on lazy curves
					final PriceCurveKey key = new PriceCurveKey(index.getName().toLowerCase(), null, SeriesType.COMMODITY);
					priceExpressionProviderEditor.setPriceCurve(key, EmptySeries.INSTANCE);
				}
			} catch (final Exception exception) {
				LOG.warn("Error evaluating series " + index.getName(), exception);
			}
		}

		for (final CommodityCurveOverlay overlay : extraPriceCurves) {
			final String indexName = overlay.getReferenceCurve().getName().toLowerCase();
			for (final YearMonthPointContainer ymPointContainer : overlay.getAlternativeCurves()) {
				if (ymPointContainer.getName() == null) {
					throw new IllegalStateException("Commodity curve option name must be provided");
				}
				final String curveName = ymPointContainer.getName().toLowerCase();
				final ISeries concreteSeries = dateHelper.constructConcreteSeries(ymPointContainer);
				final PriceCurveKey key = new PriceCurveKey(indexName, curveName, SeriesType.COMMODITY);
				priceExpressionProviderEditor.setPriceCurve(key, EmptySeries.INSTANCE);
			}
		}

		for (final BunkerFuelCurve index : pricingModel.getBunkerFuelCurves()) {
			try {

				ICurve curve;
				if (index.isSetExpression()) {
					final ISeries parsed = baseFuelIndices.getSeries(index.getName()).get();
					curve = dateHelper.generateExpressionCurve(parsed);
				} else {
					curve = dateHelper.constructConcreteCurve(index);
				}

				modelEntityMap.addModelObject(index, curve);
				baseFuelIndexAssociation.add(index, curve);
			} catch (final Exception exception) {
				LOG.warn("Error evaluating series " + index.getName(), exception);
			}
		}

		for (final CharterCurve index : pricingModel.getCharterCurves()) {
			try {
				ILongCurve curve;
				if (index.isSetExpression()) {
					final ISeries parsed = charterIndices.asSeries(index.getName());
					curve = dateHelper.generateLongExpressionCurve(parsed);
				} else {
					curve = dateHelper.constructConcreteLongCurve(index);
				}

				modelEntityMap.addModelObject(index, curve);
				charterIndexAssociation.add(index, curve);
			} catch (final Exception exception) {
				LOG.warn("Error evaluating series " + index.getName(), exception);
			}
		}

		/**
		 * Bidirectionally maps EMF {@link Port} Models to {@link IPort}s in the
		 * builder.
		 */
		final Association<Port, IPort> portAssociation = new Association<>();
		/**
		 * Lists all the {@link IPort}s created for this scenario
		 */
		final List<IPort> allPorts = new ArrayList<>();
		/**
		 * A reverse-lookup for the {@link allPorts} array.
		 */
		final Map<IPort, Integer> portIndices = new HashMap<>();

		/*
		 * Construct ports for each port in the scenario port model, and keep them in a
		 * two-way lookup table (the two-way lookup is needed to do things like setting
		 * distances later).
		 */
		final PortModel portModel = rootObject.getReferenceModel().getPortModel();

		final CostModel costModel = rootObject.getReferenceModel().getCostModel();

		final Map<CooldownPrice, ICooldownCalculator> cooldownCalculators = new HashMap<>();
		final Map<Port, CooldownPrice> portToCooldownMap = new HashMap<>();
		// Build calculators and explicit port mapping
		for (final CooldownPrice price : costModel.getCooldownCosts()) {
			ILongCurve lumpsumCurve = null;
			ICurve volumeCurve = null;

			if (price.getLumpsumExpression() != null && !price.getLumpsumExpression().isBlank()) {
				lumpsumCurve = dateHelper.generateLongExpressionCurve(price.getLumpsumExpression(), commodityIndices);
			}
			if (price.getVolumeExpression() != null && !price.getVolumeExpression().isBlank()) {
				volumeCurve = dateHelper.generateExpressionCurve(price.getVolumeExpression(), commodityIndices);
			}

			final ICooldownCalculator cooldownCalculator = new CooldownCalculator(lumpsumCurve, volumeCurve);
			injector.injectMembers(cooldownCalculator);

			cooldownCalculators.put(price, cooldownCalculator);

			for (final APortSet<Port> portSet : price.getPorts()) {
				if (portSet instanceof final Port port) {
					portToCooldownMap.put(port, price);
				}
			}
		}
		// Now do the group based mapping
		for (final CooldownPrice price : costModel.getCooldownCosts()) {
			for (final Port port : SetUtils.getObjects(price.getPorts())) {
				if (!portToCooldownMap.containsKey(port)) {
					portToCooldownMap.put(port, price);
				}
			}
		}

		for (final Port ePort : portModel.getPorts()) {
			final IPort port = portProvider.getPortForMMXID(ePort.mmxID());
			assert port != null;

			final CooldownPrice eCooldownPrice = portToCooldownMap.get(ePort);
			builder.setPortCooldownData(port, !ePort.isAllowCooldown(), cooldownCalculators.get(eCooldownPrice));

			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);
			modelEntityMap.addModelObject(ePort, port);

			builder.setPortCV(port, OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getCvValue()));

			final int minCv = ePort.isSetMinCvValue() ? OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getMinCvValue()) : 0;
			final int maxCv = ePort.isSetMaxCvValue() ? OptimiserUnitConvertor.convertToInternalConversionFactor(ePort.getMaxCvValue()) : Integer.MAX_VALUE;
			builder.setPortMinCV(port, minCv);
			builder.setPortMaxCV(port, maxCv);

			// Set port default values
			portVisitDurationProviderEditor.setVisitDuration(port, PortType.Load, ePort.getLoadDuration());
			portVisitDurationProviderEditor.setVisitDuration(port, PortType.Discharge, ePort.getDischargeDuration());
		}

		// Generate base fuels
		{
			for (final BaseFuelCost baseFuelCost : costModel.getBaseFuelCosts()) {
				final BaseFuel eBF = baseFuelCost.getFuel();
				assert eBF != null;

				final IBaseFuel bf = TransformerHelper.buildBaseFuel(builder, eBF);
				modelEntityMap.addModelObject(eBF, bf);

				final ICurve curve = dateHelper.generateExpressionCurve(baseFuelCost.getExpression(), baseFuelIndices);
				assert curve != null;
				baseFuelCurveProvider.setBaseFuelCurve(bf, curve);
			}
		}
		// set up the contract transformers
		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.startTransforming(rootObject, modelEntityMap, builder);
		}

		final Association<Vessel, IVessel> vesselAssociation = buildFleet(builder, portAssociation, modelEntityMap);

		for (final IVesselCharter oVesselCharter : allVesselAvailabilities) {
			assert oVesselCharter != null;
			final VesselAssignmentType eVesselCharter = modelEntityMap.getModelObject(oVesselCharter, VesselAssignmentType.class);

			for (final IVesselCharterTransformer vesselCharterTransformer : vesselCharterTransformers) {
				assert vesselCharterTransformer != null;

				// This can be null if the availability is generated from a Spot option
				if (eVesselCharter instanceof final VesselCharter vesselCharter) {
					vesselCharterTransformer.vesselCharterTransformed(vesselCharter, oVesselCharter);
				}
			}
		}

		final CommercialModel commercialModel = rootObject.getReferenceModel().getCommercialModel();

		// Any NPE's in the following code are likely due to missing associations
		// between a IContractTransformer and the EMF AContract object.
		// IContractTransformer instances are typically OSGi
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

		final Map<Pair<Port, PortCapability>, PortCostEntry> portToPortCostMap = new HashMap<>();
		// Register group based ports first
		for (final PortCost portCost : costModel.getPortCosts()) {
			for (final Port port : SetUtils.getObjects(portCost.getPorts())) {
				for (final PortCostEntry pce : portCost.getEntries()) {
					if (pce.getCost() != 0) {
						portToPortCostMap.put(new Pair<>(port, pce.getActivity()), pce);
					}
				}
			}
		}
		// Override with specific costs
		for (final PortCost portCost : costModel.getPortCosts()) {
			for (final APortSet<Port> portSet : portCost.getPorts()) {
				if (portSet instanceof final Port port) {

					for (final PortCostEntry pce : portCost.getEntries()) {
						if (pce.getCost() != 0) {
							portToPortCostMap.put(new Pair<>(port, pce.getActivity()), pce);
						}
					}
				}
			}
		}

		// process port costs
		for (final Map.Entry<Pair<Port, PortCapability>, PortCostEntry> e : portToPortCostMap.entrySet()) {
			final PortCostEntry entry = e.getValue();
			final PortCost cost = (PortCost) entry.eContainer();
			final Pair<Port, PortCapability> key = e.getKey();
			PortType type = null;
			switch (key.getSecond()) {
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
			case TRANSFER:
			default:
				break;
			}

			if (type != null) {
				// Now create port costs for all the vessel instances.
				for (final IVessel oVessel : allVessels.values()) {
					// TODO should the builder handle the application of costs to vessels?
					final Vessel eVessel = vesselAssociation.reverseLookup(oVessel);
					final long activityCost = OptimiserUnitConvertor.convertToInternalFixedCost(cost.getPortCost(eVessel, entry.getActivity()));
					builder.setPortCost(portAssociation.lookupNullChecked(key.getFirst()), oVessel, type, activityCost);
				}
			}
		}

		buildDistances(builder, vesselAssociation, portAssociation, modelEntityMap);

		registerSpotCargoMarkets(modelEntityMap);

		buildCargoes(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap);

		buildVesselEvents(builder, portAssociation, vesselAssociation, modelEntityMap);

		buildSpotCargoMarkets(builder, portAssociation, modelEntityMap, vesselAssociation);

		// Disable this completely as MTM mapping clashes with spot market mapping in
		// modelEntityMap
		// buildMarkToMarkets(builder, portAssociation, contractTransformers,
		// modelEntityMap);

		setNominatedVessels(builder, modelEntityMap);

		buildRouteEntryPoints(portModel, portAssociation);

		buildCanalDistances(portModel);

		// freeze any frozen assignments
		freezeAssignmentModel(builder, modelEntityMap);

		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.finishTransforming();
		}

		builder.setEarliestDate(dateHelper.getEarliestTime());

		return builder.getOptimisationData();
	}

	private @Nullable ICharterContract createAndGetCharterContract(final GenericCharterContract eBallastBonusContract) {
		if (eBallastBonusContract != null) {
			for (final ICharterContractTransformer ballastBonusContractTransformer : ballastBonusContractTransformers) {
				// This can be null if the availability is generated from a Spot option
				@Nullable
				final ICharterContract ballastBonusContract = ballastBonusContractTransformer.createCharterContract(eBallastBonusContract);
				if (ballastBonusContract != null) {
					return ballastBonusContract;
				}
			}
		}
		return null;
	}

	private void registerIndex(final String name, SeriesType seriesType, @NonNull final AbstractYearMonthCurve curve, @NonNull final SeriesParser indices) {
		assert name != null;
		if (curve.isSetExpression()) {
			indices.addSeriesExpression(name, seriesType, curve.getExpression());

		} else {
			final SortedSet<Pair<YearMonth, Number>> vals = new TreeSet<>((o1, o2) -> o1.getFirst().compareTo(o2.getFirst()));
			for (final YearMonthPoint pt : curve.getPoints()) {
				vals.add(new Pair<>(pt.getDate(), pt.getValue()));
			}
			final int[] times = new int[vals.size()];
			final Number[] nums = new Number[vals.size()];
			int k = 0;
			for (final Pair<YearMonth, Number> e : vals) {
				times[k] = dateHelper.convertTime(e.getFirst());
				nums[k++] = e.getSecond();
			}
			indices.addSeriesData(name, seriesType, times, nums);
		}
	}

	private void registerCommodityIndex(final String name, @NonNull final AbstractYearMonthCurve curve, @NonNull final SeriesParser indices) {
		assert name != null;
		final boolean hasLazyEntry = extraPriceCurves.stream() //
				.map(CommodityCurveOverlay::getReferenceCurve) //
				.map(CommodityCurve::getName) //
				.anyMatch(name::equalsIgnoreCase);
		if (hasLazyEntry) {
			final ILazyExpressionContainer lazyContainer = new ThreadLocalLazyExpressionContainer(name, SeriesType.COMMODITY);
			indices.addSeriesData(name, lazyContainer);
			lazyExpressionManagerEditor.addPriceCurve(name, lazyContainer);
		} else {
			registerIndex(name, SeriesType.COMMODITY, curve, indices);
		}
	}

	private void registerConversionFactor(@NonNull final UnitConversion factor, @NonNull final SeriesParser... parsers) {
		final String name = PriceIndexUtils.createConversionFactorName(factor);
		final String reverseName = PriceIndexUtils.createReverseConversionFactorName(factor);
		if (name != null && reverseName != null) {
			for (final SeriesParser parser : parsers) {
				parser.addSeriesExpression(name, SeriesType.CONVERSION, Double.toString(factor.getFactor()));
				parser.addSeriesExpression(reverseName, SeriesType.CONVERSION, Double.toString(1.0 / factor.getFactor()));
			}
		}
	}

	private void setNominatedVessels(@NonNull final ISchedulerBuilder builder, @NonNull final ModelEntityMap modelEntityMap) {

		final CargoModel cargoModel = rootObject.getCargoModel();
		if (cargoModel != null) {
			for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {
				assert loadSlot != null;
				if (loadSlot.isDESPurchase()) {
					final IPortSlot portSlot = modelEntityMap.getOptimiserObject(loadSlot, IPortSlot.class);
					final IVessel vessel = allVessels.get(loadSlot.getNominatedVessel());
					if (vessel != null && portSlot != null) {
						builder.setNominatedVessel(portSlot, vessel);
					}
				}
			}

			for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {
				assert dischargeSlot != null;
				if (dischargeSlot.isFOBSale()) {
					final IPortSlot portSlot = modelEntityMap.getOptimiserObject(dischargeSlot, IPortSlot.class);
					final IVessel vessel = allVessels.get(dischargeSlot.getNominatedVessel());
					if (vessel != null && portSlot != null) {
						builder.setNominatedVessel(portSlot, vessel);
					}
				}
			}
		}
	}

	private void freezeAssignmentModel(@NonNull final ISchedulerBuilder builder, @NonNull final ModelEntityMap modelEntityMap) {

		// TODO: Freeze as part of the buildXXX methods when object is created rather
		// than here.

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
			IVesselCharter vesselCharter = null;
			if (vesselAssignmentType instanceof final VesselCharter va) {
				vesselCharter = modelEntityMap.getOptimiserObject(va, IVesselCharter.class);
			} else if (vesselAssignmentType instanceof final CharterInMarketOverride charterInMarketOverride) {
				vesselCharter = modelEntityMap.getOptimiserObject(charterInMarketOverride, IVesselCharter.class);
			} else if (vesselAssignmentType instanceof final CharterInMarket charterInMarket) {
				final int spotIndex = assignableElement.getSpotIndex();
				final NonNullPair<CharterInMarket, Integer> key = new NonNullPair<>(charterInMarket, spotIndex);
				vesselCharter = spotCharterInToAvailability.get(key);
				if (vesselCharter == null) {
					vesselCharter = spotCharterInToShortCargoAvailability.get(charterInMarket);
				}
				isNominalVessel = spotIndex == NOMINAL_CARGO_INDEX;
			}

			if (vesselCharter == null) {
				if (assignableElement instanceof Cargo) {
					// Keep going to constrain slot pairing if needed.
				} else {
					continue;
				}
			}

			final boolean freeze = assignableElement.isLocked();
			final Set<Slot<?>> lockedSlots = checkAndCollectLockedSlots(assignableElement);
			final boolean freezeElements = (!(!freeze && lockedSlots.isEmpty()));

			if (assignableElement instanceof final Cargo cargo) {
				final List<IPortSlot> allOptimiserSlots = new LinkedList<>();
				Slot<?> prevSlot = null;
				IPortSlot prevPortSlot = null;
				for (final Slot<?> slot : cargo.getSortedSlots()) {

					final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
					allOptimiserSlots.add(portSlot);

					if (vesselCharter != null && (freeze || lockedSlots.contains(slot))) {
						// bind slots to vessel
						builder.freezeSlotToVesselCharter(portSlot, vesselCharter);
					}

					if ((prevSlot != null) && (freeze || (lockedSlots.contains(slot) && lockedSlots.contains(prevSlot)))) {
						// bind sequencing as well - this forces
						// previousSlot to come before currentSlot.
						builder.constrainSlotAdjacency(prevPortSlot, portSlot);
					}

					prevSlot = slot;
					prevPortSlot = portSlot;
				}

				if (isNominalVessel && vesselCharter != null) {
					builder.bindSlotsToRoundTripVessel(vesselCharter, allOptimiserSlots.toArray(new IPortSlot[allOptimiserSlots.size()]));
				}
				if (cargo.isLocked()) {
					lockedCargoProviderEditor.addLockedCargo(allOptimiserSlots);
				}
			} else if (assignableElement instanceof VesselEvent) {
				if (freezeElements) {
					final IVesselEventPortSlot slot = modelEntityMap.getOptimiserObject(assignableElement, IVesselEventPortSlot.class);
					if (slot != null && vesselCharter != null) {
						builder.freezeSlotToVesselCharter(slot, vesselCharter);
					}
				}
			}
		}
	}

	@NonNull
	private Set<@NonNull Slot<?>> checkAndCollectLockedSlots(@NonNull final AssignableElement assignableElement) {
		final Set<@NonNull Slot<?>> lockedSlots = new HashSet<>();

		if (assignableElement instanceof final Cargo cargo) {
			for (final Slot<?> slot : cargo.getSortedSlots()) {
				if (slot.isLocked()) {
					lockedSlots.add(slot);
				}
			}
		}
		return lockedSlots;
	}

	private void buildVesselEvents(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final Association<Vessel, IVessel> vesselAssociation,
			@NonNull final ModelEntityMap modelEntityMap) {

		final ZonedDateTime latestDate = dateHelper.getLatestTime();

		final CargoModel cargoModel = rootObject.getCargoModel();

		final List<VesselEvent> vesselEvents = new LinkedList<>();
		vesselEvents.addAll(cargoModel.getVesselEvents());
		if (extraVesselEvents != null) {
			vesselEvents.addAll(extraVesselEvents);
		}

		for (final VesselEvent event : vesselEvents) {

			if (event.getStartAfterAsDateTime().isAfter(latestDate)) {
				// continue;
			}

			final ITimeWindow window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(event.getStartAfterAsDateTime()), dateHelper.convertTime(event.getStartByAsDateTime()), false);
			final IPort port = portAssociation.lookupNullChecked(event.getPort());
			final int durationHours = event.getDurationInDays() * 24;
			final IVesselEventPortSlot builderSlot;
			if (event instanceof final CharterOutEvent charterOut) {
				final IPort endPort = portAssociation.lookupNullChecked(charterOut.isSetRelocateTo() ? charterOut.getRelocateTo() : charterOut.getPort());

				final long totalHireRevenue = OptimiserUnitConvertor.convertToInternalDailyCost(charterOut.getHireRate()) * (long) charterOut.getDurationInDays();
				final long repositioning = OptimiserUnitConvertor.convertToInternalFixedCost(charterOut.getRepositioningFee());
				final long ballastBonus = OptimiserUnitConvertor.convertToInternalFixedCost(charterOut.getBallastBonus());

				final IHeelOptionConsumer heelConsumer = createHeelConsumer(charterOut.getRequiredHeel());
				// IHeelOptionConsumer heelConsumer = new HeelOptionConsumer(0L, Long.MAX_VALUE,
				// VesselTankState.EITHER, new ConstantHeelPriceCalculator(0));
				final IHeelOptionSupplier heelSupplier = createHeelSupplier(charterOut.getAvailableHeel(), null);

				builderSlot = builder.createCharterOutEvent(event.getName(), window, port, endPort, durationHours, heelConsumer, heelSupplier, totalHireRevenue, repositioning, ballastBonus,
						charterOut.isOptional());
			} else if (event instanceof DryDockEvent) {
				builderSlot = builder.createDrydockEvent(event.getName(), window, port, durationHours);
			} else if (event instanceof MaintenanceEvent) {
				builderSlot = builder.createMaintenanceEvent(event.getName(), window, port, durationHours);
			} else {
				throw new RuntimeException("Unknown event type: " + event.getClass().getName());
			}

			applyEventVesselRestrictions(event.getAllowedVessels(), builderSlot, vesselAssociation);
			// Apply restrictions to related slots
			for (final IPortSlot e : builderSlot.getEventPortSlots()) {
				if (e != builderSlot) {
					applyEventVesselRestrictions(event.getAllowedVessels(), e, vesselAssociation);
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
	 * @param builder              current builder. should already have
	 *                             ports/distances/vessels built
	 * @param indexAssociation
	 * @param contractTransformers
	 * @param modelEntityMap
	 * @param defaultRewiring
	 */
	private void buildCargoes(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final Association<Vessel, IVessel> vesselAssociation,
			@NonNull final Collection<IContractTransformer> contractTransformers, @NonNull final ModelEntityMap modelEntityMap) {

		// All Discharge ports - for use with
		final Set<IPort> allDischargePorts = new HashSet<>();
		final Set<IPort> allLoadPorts = new HashSet<>();
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

		final Set<LoadSlot> usedLoadSlots = new HashSet<>();
		final Set<DischargeSlot> usedDischargeSlots = new HashSet<>();

		final CargoModel cargoModel = rootObject.getCargoModel();
		final Map<Slot<?>, IPortSlot> transferSlotMap = new HashMap<>();

		final List<Predicate<Cargo>> cargoFilterFunctions = new ArrayList<>();
		final List<Consumer<List<@NonNull IPortSlot>>> slotConsumers = new ArrayList<>();

		// Is this check needed now? What was it supposed to do?
		// latest time should be derived from this data anyway...
		cargoFilterFunctions.add(c -> !c.getSortedSlots().get(0).getSchedulingTimeWindow().getStart().isAfter(latestDate));

		// ADP optimisations only keep third-party cargoes
		final ADPModel adpModel = rootObject.getAdpModel();
		if (adpModel != null && userSettings.getMode() == OptimisationMode.ADP) {
			cargoFilterFunctions.add(c -> c.getSlots().size() == 2);

			final Predicate<Cargo> thirdPartyCheck = cargo -> {
				final List<Slot<?>> slots = cargo.getSortedSlots();
				final Slot<?> loadSlot = slots.get(0);
				final Slot<?> dischargeSlot = slots.get(1);
				final BaseLegalEntity loadSlotEntity = loadSlot.getSlotOrDelegateEntity();
				if (loadSlotEntity != null && loadSlotEntity.isThirdParty()) {
					if (dischargeSlot.getSlotOrDelegateEntity() != loadSlotEntity) {
						throw new IllegalStateException("Third-party cargoes must use the same entity");
					}
					if (cargo.isAllowRewiring()) {
						throw new IllegalStateException("Third-party cargoes must not allow rewiring");
					}
					return true;
				}
				final BaseLegalEntity dischargeSlotEntity = dischargeSlot.getSlotOrDelegateEntity();
				if (dischargeSlotEntity != null && dischargeSlotEntity.isThirdParty()) {
					throw new IllegalStateException("Third-party cargoes must use the same entity");
				}
				return false;
			};

			cargoFilterFunctions.add(thirdPartyCheck);
			slotConsumers.add(slots -> {
				assert slots.size() == 2;
				final ILoadOption load = (ILoadOption) slots.get(0);
				final IDischargeOption discharge = (IDischargeOption) slots.get(1);
				thirdPartyCargoProviderEditor.addThirdPartyCargo(load, discharge);
			});
		}

		cargoModel.getCargoes().stream() //
				.filter(c -> cargoFilterFunctions.stream().allMatch(fun -> fun.test(c))) //
				.forEach(eCargo -> {
					final List<@NonNull ILoadOption> loadOptions = new LinkedList<>();
					final List<@NonNull IDischargeOption> dischargeOptions = new LinkedList<>();
					final List<@NonNull IPortSlot> slots = new ArrayList<>(eCargo.getSortedSlots().size());
					final Map<Slot<?>, IPortSlot> slotMap = new HashMap<>();
					for (final Slot<?> slot : eCargo.getSortedSlots()) {
						if (slot instanceof final LoadSlot loadSlot) {
							final ILoadOption load = createLoadOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, loadSlot);
							usedLoadSlots.add(loadSlot);
							loadOptions.add(load);
							slotMap.put(loadSlot, load);
							slots.add(load);
						} else if (slot instanceof final DischargeSlot dischargeSlot) {
							final IDischargeOption discharge = createDischargeOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, dischargeSlot);
							usedDischargeSlots.add(dischargeSlot);
							dischargeOptions.add(discharge);
							slotMap.put(dischargeSlot, discharge);
							slots.add(discharge);
						} else {
							throw new IllegalArgumentException("Unexpected Slot type");
						}
					}

					final boolean isSoftRequired = false;
					for (final Slot<?> slot : eCargo.getSortedSlots()) {
						boolean isTransfer = false;

						if (slot instanceof final LoadSlot loadSlot) {
							// Bind FOB/DES slots to resource
							final ILoadOption load = (ILoadOption) slotMap.get(loadSlot);
							assert loadSlot != null;
							configureLoadSlotRestrictions(builder, portAssociation, allDischargePorts, loadSlot, load);
							isTransfer = (((LoadSlot) slot).getTransferFrom() != null);
							if (isSoftRequired) {
								setSlotAsSoftRequired(builder, slot, load);
							}
						} else if (slot instanceof final DischargeSlot dischargeSlot) {
							final IDischargeOption discharge = (IDischargeOption) slotMap.get(dischargeSlot);
							assert discharge != null;
							configureDischargeSlotRestrictions(builder, portAssociation, allLoadPorts, dischargeSlot, discharge);
							isTransfer = (((DischargeSlot) slot).getTransferTo() != null);
							if (isSoftRequired) {
								setSlotAsSoftRequired(builder, slot, discharge);
							}
						}

						// remember any slots which were part of a ship-to-ship transfer
						// but don't do anything with them yet, because we need to wait until all slots
						// have been processed
						if (isTransfer) {
							transferSlotMap.put(slot, slotMap.get(slot));
						}
					}

					final ICargo cargo = builder.createCargo(slots, shippingOnly ? false : eCargo.isAllowRewiring());
					modelEntityMap.addModelObject(eCargo, cargo);

					slotConsumers.forEach(consumer -> consumer.accept(slots));
				});

		// register ship-to-ship transfers with the relevant data component provider
		for (final Entry<Slot<?>, IPortSlot> entry : transferSlotMap.entrySet()) {
			final Slot<?> slot = entry.getKey();
			final IPortSlot portSlot = entry.getValue();
			Slot<?> converse = null;
			if (slot instanceof final DischargeSlot dischargeSlot) {
				converse = dischargeSlot.getTransferTo();
			} else if (slot instanceof final LoadSlot loadSlot) {
				converse = loadSlot.getTransferFrom();
			}

			shipToShipBindingProvider.setConverseTransferElement(portSlot, transferSlotMap.get(converse));
		}

		Stream.concat(cargoModel.getLoadSlots().stream(), extraLoadSlots.stream()).forEach(loadSlot -> {
			assert loadSlot != null;
			if (usedLoadSlots.contains(loadSlot)) {
				return;
			}

			final ILoadOption load;
			{
				// Make optional
				load = createLoadOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, loadSlot);
				if (loadSlot.getCargo() == null) {
					setSlotAsSoftRequired(builder, loadSlot, load);
				}
			}

			configureLoadSlotRestrictions(builder, portAssociation, allDischargePorts, loadSlot, load);
			usedLoadSlots.add(loadSlot);
		});

		Stream.concat(cargoModel.getDischargeSlots().stream(), extraDischargeSlots.stream()).forEach(dischargeSlot -> {
			assert dischargeSlot != null;
			if (usedDischargeSlots.contains(dischargeSlot)) {
				return;
			}

			final IDischargeOption discharge;
			{
				discharge = createDischargeOption(builder, portAssociation, vesselAssociation, contractTransformers, modelEntityMap, dischargeSlot);
				if (dischargeSlot.getCargo() == null) {
					setSlotAsSoftRequired(builder, dischargeSlot, discharge);
				}
			}

			configureDischargeSlotRestrictions(builder, portAssociation, allLoadPorts, dischargeSlot, discharge);
			usedDischargeSlots.add(dischargeSlot);

		});
	}

	private void setSlotAsSoftRequired(final ISchedulerBuilder builder, final Slot<?> eSlot, final IPortSlot slot) {
		if (!eSlot.isOptional()) {
			builder.setSoftRequired(slot);
		}
	}

	@NonNull
	private ITimeWindow getTimeWindowForSlotBinding(final Slot<?> modelSlot, final IPortSlot optimiserSlot, final IPort port) {

		boolean extendWindows = false;

		if (modelSlot instanceof SpotSlot) {
			// This should only be called from the non-shipped code path.
			// Should assume to be the same as DES.DEST_WITH_SOURCE or FOB.SOURCE_WITH_DEST
			extendWindows = true;
		} else if (modelSlot instanceof final LoadSlot slot) {
			if (slot.isDESPurchase()) {
				if (slot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
					// return getTimewindowAsUTCWithFlex(slot);
				} else if (slot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DEST_WITH_SOURCE || slot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERTIBLE) {
					extendWindows = true;
				}
			}
		} else if (modelSlot instanceof final DischargeSlot slot) {
			if (slot.isFOBSale()) {
				if (slot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
					// return getTimewindowAsUTCWithFlex(slot);
				} else if (slot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.SOURCE_WITH_DEST) {
					extendWindows = true;
				}
			}
		}

		if (extendWindows) {
			// TODO: IS this with flex or not??
			final ZonedDateTime startTime = modelSlot.getSchedulingTimeWindow().getStart();
			final ZonedDateTime endTime = modelSlot.getSchedulingTimeWindow().getEnd();
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
			@NonNull final DischargeSlot dischargeSlot, @NonNull final IDischargeOption discharge) {
		if (dischargeSlot.isFOBSale()) {

			if (dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.SOURCE_WITH_DEST || dischargeSlot instanceof SpotDischargeSlot) {
				final Set<IPort> marketPorts = new HashSet<>();
				if (dischargeSlot instanceof final SpotDischargeSlot spotSlot) {
					final FOBSalesMarket fobSaleMarket = (FOBSalesMarket) spotSlot.getMarket();
					final Set<Port> portSet = SetUtils.getObjects(fobSaleMarket.getOriginPorts());
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup(ap);
						if (ip != null) {
							marketPorts.add(ip);
						}
					}
				} else {
					marketPorts.addAll(allLoadPorts);
				}
				final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
				for (final IPort port : marketPorts) {

					// Re-use the real date objects to map back to integer timezones to avoid
					// mismatching windows caused by half hour timezone shifts
					final ZonedDateTime portWindowStart = dischargeSlot.getSchedulingTimeWindow().getStart().withZoneSameLocal(ZoneId.of(port.getTimeZoneId()));
					final ZonedDateTime portWindowEnd = dischargeSlot.getSchedulingTimeWindow().getEndWithFlex().withZoneSameLocal(ZoneId.of(port.getTimeZoneId()));

					final ITimeWindow tw = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(portWindowStart), dateHelper.convertTime(portWindowEnd), 0, false);

					marketPortsMap.put(port, tw);
					((DischargeOption) discharge).addLocalisedTimeWindow(port, tw);
				}

				builder.bindLoadSlotsToFOBSale(discharge, marketPortsMap);
			} else {
				final ITimeWindow twForSlotBinding;
				if (dischargeSlot.getPort() == null) {
					twForSlotBinding = getTimeWindowForSlotBinding(dischargeSlot, discharge, portProvider.getAnywherePort());
				} else {
					twForSlotBinding = getTimeWindowForSlotBinding(dischargeSlot, discharge, portAssociation.lookupNullChecked(dischargeSlot.getPort()));
				}

				if (dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {

					// Bind to all loads
					final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
					for (final IPort port : allLoadPorts) {
						marketPortsMap.put(port, twForSlotBinding);
					}

					builder.bindLoadSlotsToFOBSale(discharge, marketPortsMap);

					final List<ERouteOption> allowedRoutes = new LinkedList<>();
					if (shippingDaysRestrictionSpeedProvider != null) {
						for (final Route route : shippingDaysRestrictionSpeedProvider.getValidRoutes(ScenarioModelUtil.getPortModel(rootObject), dischargeSlot)) {
							allowedRoutes.add(mapRouteOption(route));
						}
					}
					builder.setDivertibleFOBAllowedRoute(discharge, allowedRoutes);
				} else {
					// Bind to current port only
					final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
					marketPortsMap.put(discharge.getPort(), twForSlotBinding);
					builder.bindLoadSlotsToFOBSale(discharge, marketPortsMap);
				}
			}
		}

	}

	public void configureLoadSlotRestrictions(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final Set<IPort> allDischargePorts,
			@NonNull final LoadSlot loadSlot, @NonNull final ILoadOption load) {

		if (loadSlot.isDESPurchase()) {
			if (loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DEST_WITH_SOURCE//
					|| loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERTIBLE //
					|| loadSlot instanceof SpotLoadSlot) {
				final Set<IPort> marketPorts = new HashSet<>();
				if (loadSlot instanceof final SpotLoadSlot spotLoadSlot) {
					final SpotMarket market = spotLoadSlot.getMarket();
					if (market instanceof final DESPurchaseMarket desPurchaseMarket) {
						final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup(ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}
					}

				} else {
					marketPorts.addAll(allDischargePorts);
				}

				final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
				for (final IPort port : marketPorts) {
					// Re-use the real date objects to map back to integer timezones to avoid
					// mismatching windows caused by half hour timezone shifts
					final ZonedDateTime portWindowStart = loadSlot.getSchedulingTimeWindow().getStart().withZoneSameLocal(ZoneId.of(port.getTimeZoneId()));// .atTime,
					final ZonedDateTime portWindowEnd = loadSlot.getSchedulingTimeWindow().getEndWithFlex().withZoneSameLocal(ZoneId.of(port.getTimeZoneId()));// .atTime,

					final ITimeWindow tw = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(portWindowStart), dateHelper.convertTime(portWindowEnd), 0, false);

					marketPortsMap.put(port, tw);
					((LoadOption) load).addLocalisedTimeWindow(port, tw);
				}

				// Bind FOB/DES slots to resource
				builder.bindDischargeSlotsToDESPurchase(load, marketPortsMap);
			} else {

				final ITimeWindow twForSlotBinding;
				if (loadSlot.getPort() == null) {
					twForSlotBinding = getTimeWindowForSlotBinding(loadSlot, load, portProvider.getAnywherePort());
				} else {
					twForSlotBinding = getTimeWindowForSlotBinding(loadSlot, load, portAssociation.lookupNullChecked(loadSlot.getPort()));
				}

				// Bind FOB/DES slots to resource
				if (loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {

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
			builder.setDivertibleDESAllowedRoute(load, allowedRoutes);

		}
	}

	@NonNull
	private IDischargeOption createDischargeOption(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final Association<Vessel, IVessel> vesselAssociation, @NonNull final Collection<IContractTransformer> contractTransformers, @NonNull final ModelEntityMap modelEntityMap,
			@NonNull final DischargeSlot dischargeSlot) {
		final IDischargeOption discharge;
		final String elementName = String.format("%s-%s", dischargeSlot.isFOBSale() ? "FS" : "DS", dischargeSlot.getName());

		usedIDStrings.add(elementName);

		final ITimeWindow dischargeWindow = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(dischargeSlot.getSchedulingTimeWindow().getStart()),
				dateHelper.convertTime(dischargeSlot.getSchedulingTimeWindow().getEndWithFlex()), dischargeSlot.getWindowFlex(), false);

		final ISalesPriceCalculator dischargePriceCalculator;

		final boolean isSpot = (dischargeSlot instanceof SpotSlot);
		if (dischargeSlot.isSetPriceExpression() && SlotContractParamsHelper.isSlotExpressionUsed(dischargeSlot)) {

			if (dischargeSlot.isSetPriceExpression()) {
				final String priceExpression = dischargeSlot.getPriceExpression();

				if ("??".equals(priceExpression)) {
					dischargePriceCalculator = new ChangeablePriceCalculator();
				} else if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
					if (portfolioBreakevenFlag) {
						dischargePriceCalculator = new PortfolioBreakEvenSalesPriceCalculator();
					} else {
						dischargePriceCalculator = new BreakEvenSalesPriceCalculator();
					}
				} else {
					dischargePriceCalculator = getSalesPriceCalculatorForExpression(priceExpression);
				}
			} else {
				dischargePriceCalculator = null;
			}
		} else if (dischargeSlot instanceof final SpotSlot spotSlot) {
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

			dischargePriceCalculator = calculator;
		} else if (dischargeSlot.isSetContract()) {
			dischargePriceCalculator = modelEntityMap.getOptimiserObject(dischargeSlot.getContract(), ISalesPriceCalculator.class);
		} else {
			dischargePriceCalculator = null;
		}

		if (dischargePriceCalculator == null) {
			throw new IllegalStateException(String.format("Discharge Slot [%s] has no contract or other pricing data", dischargeSlot.getName()));
		}

		final boolean isVolumeLimitInM3 = dischargeSlot.getSlotOrDelegateVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3 ? true : false;

		// local scope for slot creation convenience variables
		{
			// convenience variables
			final String name = elementName;
			final long minVolume;
			final long maxVolume;
			if (dischargeSlot instanceof final SpotSlot spotSlot) {
				final SpotMarket market = spotSlot.getMarket();
				minVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity());
				maxVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity());
			} else {
				minVolume = OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrDelegateMinQuantity());
				if (dischargeSlot.getSlotOrDelegateMaxQuantity() == Integer.MAX_VALUE) {
					maxVolume = Long.MAX_VALUE;
				} else {
					maxVolume = OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrDelegateMaxQuantity());
				}
			}

			final long minCv;
			long maxCv;

			final int pricingDate = getSlotPricingDate(dischargeSlot);

			minCv = OptimiserUnitConvertor.convertToInternalConversionFactor(dischargeSlot.getSlotOrDelegateMinCv());
			maxCv = OptimiserUnitConvertor.convertToInternalConversionFactor(dischargeSlot.getSlotOrDelegateMaxCv());
			if (maxCv == 0) {
				maxCv = Long.MAX_VALUE;
			}

			/*
			 * if (dischargeSlot.isSetContract()) { final SalesContract salesContract =
			 * (SalesContract) dischargeSlot.getContract();
			 * 
			 * if (salesContract.isSetMinCvValue()) { minCv =
			 * OptimiserUnitConvertor.convertToInternalConversionFactor(salesContract.
			 * getMinCvValue()); } else { minCv = 0; }
			 * 
			 * if (salesContract.isSetMaxCvValue()) { maxCv =
			 * OptimiserUnitConvertor.convertToInternalConversionFactor(salesContract.
			 * getMaxCvValue()); } else { maxCv = Long.MAX_VALUE; } } else { minCv = 0;
			 * maxCv = Long.MAX_VALUE; }
			 */

			final boolean slotLocked = dischargeSlot.isLocked() || shippingOnly && dischargeSlot.getCargo() == null;
			final boolean slotCancelled = dischargeSlot.isCancelled();
			if (dischargeSlot.isFOBSale()) {
				final ITimeWindow localTimeWindow;

				if (dischargeSlot instanceof SpotDischargeSlot || dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.SOURCE_WITH_DEST) {
					// Convert back into a UTC based date and add in TZ flex
					final int utcStart = timeZoneToUtcOffsetProvider.UTC(dischargeWindow.getInclusiveStart(), portAssociation.lookup(dischargeSlot.getPort()));
					final int utcEnd = timeZoneToUtcOffsetProvider.UTC(dischargeWindow.getExclusiveEnd(), portAssociation.lookup(dischargeSlot.getPort()));
					localTimeWindow = createUTCPlusTimeWindow(utcStart, utcEnd);
				} else {

					if (dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
						localTimeWindow = TimeWindowMaker.createInclusiveExclusive(dischargeWindow.getInclusiveStart() - dischargeSlot.getSlotOrDelegateShippingDaysRestriction() * 24,
								dischargeWindow.getExclusiveEnd(), 0, false);

					} else {
						localTimeWindow = dischargeWindow;
					}
				}
				final IPort port;
				if (dischargeSlot instanceof SpotSlot) {
					port = null;
				} else {
					port = portAssociation.lookup(dischargeSlot.getPort());
				}

				discharge = builder.createFOBSaleDischargeSlot(name, port, localTimeWindow, minVolume, maxVolume, minCv, maxCv, dischargePriceCalculator,
						dischargeSlot.getSchedulingTimeWindow().getDuration(), pricingDate, transformPricingEvent(dischargeSlot.getSlotOrDelegatePricingEvent()), dischargeSlot.isOptional(),
						slotLocked, isSpot, isVolumeLimitInM3, slotCancelled);

				if (dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
					builder.setShippingHoursRestriction(discharge, dischargeWindow, dischargeSlot.getSlotOrDelegateShippingDaysRestriction() * 24);
				}
			} else {
				discharge = builder.createDischargeSlot(name, portAssociation.lookupNullChecked(dischargeSlot.getPort()), dischargeWindow, minVolume, maxVolume, minCv, maxCv, dischargePriceCalculator,
						dischargeSlot.getSchedulingTimeWindow().getDuration(), pricingDate, transformPricingEvent(dischargeSlot.getSlotOrDelegatePricingEvent()), dischargeSlot.isOptional(),
						slotLocked, isSpot, isVolumeLimitInM3, slotCancelled);
				if (dischargeSlot.isWindowCounterParty()) {
					counterPartyWindowProviderEditor.setCounterPartyWindow(discharge);
				}
			}
		}

		// Register as spot market slot
		if (dischargeSlot instanceof final SpotSlot spotSlot) {
			registerSpotMarketSlot(modelEntityMap, dischargeSlot, discharge);
			marketSlotsByID.put(elementName, dischargeSlot);
			addSpotSlotToCount(spotSlot);
		}

		modelEntityMap.addModelObject(dischargeSlot, discharge);
		for (final ISlotTransformer slotTransformer : slotTransformers) {
			slotTransformer.slotTransformed(dischargeSlot, discharge);
		}

		// set additional misc costs in provider
		setMiscCosts(dischargeSlot, discharge);

		final ILongCurve cancellationCurve = dateHelper.generateLongExpressionCurve(dischargeSlot.getSlotOrDelegateCancellationExpression(), commodityIndices);
		if (cancellationCurve != null) {
			cancellationFeeProviderEditor.setCancellationExpression(discharge, cancellationCurve);
		}
		if (!dischargeSlot.isFOBSale()) {
			applySlotVesselRestrictions(dischargeSlot.getSlotOrDelegateVesselRestrictions(), dischargeSlot.getSlotOrDelegateVesselRestrictionsArePermissive(), discharge, vesselAssociation);
		} else {
			final Iterator<Pair<Vessel, IVessel>> vesselAssociationIterator = vesselAssociation.iterator();
			while (vesselAssociationIterator.hasNext()) {
				final Pair<Vessel, IVessel> currentAssociation = vesselAssociationIterator.next();
				final Vessel eVessel = currentAssociation.getFirst();
				final IVessel oVessel = currentAssociation.getSecond();
				final int ladenReferenceSpeed;
				final int ballastReferenceSpeed;
				if (shippingDaysRestrictionSpeedProvider == null) {
					ladenReferenceSpeed = ballastReferenceSpeed = oVessel.getMaxSpeed();
				} else {
					ladenReferenceSpeed = OptimiserUnitConvertor.convertToInternalSpeed(shippingDaysRestrictionSpeedProvider.getSpeed(dischargeSlot, eVessel, true));
					ballastReferenceSpeed = OptimiserUnitConvertor.convertToInternalSpeed(shippingDaysRestrictionSpeedProvider.getSpeed(dischargeSlot, eVessel, false));
				}
				builder.setShippingDaysRestrictionReferenceSpeed(discharge, oVessel, VesselState.Laden, ladenReferenceSpeed);
				builder.setShippingDaysRestrictionReferenceSpeed(discharge, oVessel, VesselState.Ballast, ballastReferenceSpeed);
			}
		}
		return discharge;
	}

	private ISalesPriceCalculator getSalesPriceCalculatorForExpression(final String priceExpression) {
		final ExpressionPriceParameters dynamicContract = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		dynamicContract.setPriceExpression(priceExpression);

		final IContractTransformer transformer = contractTransformersByEClass.get(dynamicContract.eClass());
		if (transformer == null) {
			throw new IllegalStateException("No Price Parameters transformer registered for  " + dynamicContract.eClass().getName());
		}
		final ISalesPriceCalculator calculator = transformer.transformSalesPriceParameters(null, dynamicContract);
		if (calculator == null) {
			throw new IllegalStateException("Unable to transform contract");
		}
		return calculator;
	}

	@NonNull
	private ILoadOption createLoadOption(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final Association<Vessel, IVessel> vesselAssociation, @NonNull final Collection<IContractTransformer> contractTransformers, @NonNull final ModelEntityMap modelEntityMap,
			@NonNull final LoadSlot loadSlot) {
		final ILoadOption load;
		final String elementName = String.format("%s-%s", loadSlot.isDESPurchase() ? "DP" : "FP", loadSlot.getName());
		usedIDStrings.add(elementName);

		final ITimeWindow loadWindow = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(loadSlot.getSchedulingTimeWindow().getStart()),
				dateHelper.convertTime(loadSlot.getSchedulingTimeWindow().getEndWithFlex()), loadSlot.getWindowFlex(), false);

		final ILoadPriceCalculator loadPriceCalculator;
		final boolean isSpot = (loadSlot instanceof SpotSlot);
		if (loadSlot.isSetPriceExpression() && SlotContractParamsHelper.isSlotExpressionUsed(loadSlot)) {

			if (loadSlot.isSetPriceExpression()) {
				final String priceExpression = loadSlot.getPriceExpression();
				if ("??".equals(priceExpression)) {
					loadPriceCalculator = new ChangeablePriceCalculator();
				} else if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
					if (portfolioBreakevenFlag) {
						loadPriceCalculator = new PortfolioBreakEvenLoadPriceCalculator();
					} else {
						loadPriceCalculator = new BreakEvenLoadPriceCalculator();
					}
				} else {
					loadPriceCalculator = getPurchasePriceCalculatorForExpression(priceExpression);
				}
			} else {
				loadPriceCalculator = null;
			}
//				final ICurve curve;
//				IIntegerIntervalCurve priceIntervals;
//				final IExpression<ISeries> expression = commodityIndices.parse(priceExpression);
//				if (expression.canEvaluate()) {
//					final ISeries parsed = expression.evaluate();
//					if (parsed.getChangePoints().length == 0) {
//						priceIntervals = monthIntervalsInHoursCurve;
//					} else {
//						priceIntervals = integerIntervalCurveHelper.getSplitMonthDatesForChangePoint(parsed.getChangePoints());
//					}
//					curve = buildStepwiseIntegerCurve(parsed);
//				} else {
//					final LazyIntegerIntervalCurve lazyIntervalCurve = new LazyIntegerIntervalCurve(priceIntervals,
//							parsed -> parsed.getChangePoints() == 0 ? monthIntervalsInHoursCurve : integerIntervalCurveHelper.getSplitMonthDatesForChangePoint(parsed.getChangePoints()));
//					priceIntervals = lazyIntervalCurve;
//					final Consumer<ISeries> parsedSeriesConsumer = lazyIntervalCurve::initialise;
//					lazyExpressionManagerEditor.addLazyIntervalCurve(lazyIntervalCurve);
//					final ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, LNGScenarioTransformer::buildStepwiseIntegerCurve, parsedSeriesConsumer);
//					curve = lazyCurve;
//					lazyExpressionManagerEditor.addLazyCurve(lazyCurve);
//				}

//				loadPriceCalculator = new PriceExpressionContract(curve, priceIntervals);
//				injector.injectMembers(loadPriceCalculator);

			// loadPriceCalculator = new PriceExpressionContract(curve, priceIntervals);
			// injector.injectMembers(loadPriceCalculator);

		} else if (loadSlot instanceof final SpotSlot spotSlot) {
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

			loadPriceCalculator = calculator;

		} else if (loadSlot.isSetContract()) {
			final PurchaseContract purchaseContract = loadSlot.getContract();
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
		if (loadSlot instanceof final SpotSlot spotSlot) {
			final SpotMarket market = spotSlot.getMarket();
			minVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity());
			maxVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity());
		} else {
			minVolume = OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrDelegateMinQuantity());
			if (loadSlot.getSlotOrDelegateMaxQuantity() == Integer.MAX_VALUE) {
				maxVolume = Long.MAX_VALUE;
			} else {
				maxVolume = OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrDelegateMaxQuantity());
			}
		}

		final boolean isVolumeLimitInM3 = loadSlot.getSlotOrDelegateVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3;

		final boolean slotLocked = loadSlot.isLocked() || shippingOnly && loadSlot.getCargo() == null;
		final boolean slotCancelled = loadSlot.isCancelled();
		if (loadSlot.isDESPurchase()) {
			final ITimeWindow localTimeWindow;
			if (loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
				// Extend window out to cover whole shipping days restriction
				localTimeWindow = TimeWindowMaker.createInclusiveExclusive(loadWindow.getInclusiveStart(), loadWindow.getExclusiveEnd() + loadSlot.getSlotOrDelegateShippingDaysRestriction() * 24, 0,
						false);
			} else if (loadSlot instanceof SpotLoadSlot || loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DEST_WITH_SOURCE//
					|| loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERTIBLE) {
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
					OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrDelegateCV()), loadSlot.getSchedulingTimeWindow().getDuration(), slotPricingDate,
					transformPricingEvent(loadSlot.getSlotOrDelegatePricingEvent()), loadSlot.isOptional(), slotLocked, isSpot, isVolumeLimitInM3, slotCancelled);

			if (loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
				builder.setShippingHoursRestriction(load, loadWindow, loadSlot.getSlotOrDelegateShippingDaysRestriction() * 24);
			}
		} else {
			load = builder.createLoadSlot(elementName, portAssociation.lookupNullChecked(loadSlot.getPort()), loadWindow, minVolume, maxVolume, loadPriceCalculator,
					OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrDelegateCV()), loadSlot.getSchedulingTimeWindow().getDuration(), loadSlot.isSetArriveCold(),
					loadSlot.isArriveCold(), loadSlot.isSchedulePurge(), slotPricingDate, transformPricingEvent(loadSlot.getSlotOrDelegatePricingEvent()), loadSlot.isOptional(), slotLocked, isSpot,
					isVolumeLimitInM3, slotCancelled);
			if (loadSlot.isWindowCounterParty()) {
				counterPartyWindowProviderEditor.setCounterPartyWindow(load);
			}
		}
		// Store market slots for lookup when building spot markets.
		modelEntityMap.addModelObject(loadSlot, load);

		// Register as spot market slot
		if (loadSlot instanceof final SpotSlot spotSlot) {
			registerSpotMarketSlot(modelEntityMap, loadSlot, load);
			marketSlotsByID.put(elementName, loadSlot);
			addSpotSlotToCount(spotSlot);
		}

		for (final ISlotTransformer slotTransformer : slotTransformers) {
			slotTransformer.slotTransformed(loadSlot, load);
		}

		// set additional misc costs in provider
		setMiscCosts(loadSlot, load);

		final ILongCurve cancellationCurve = dateHelper.generateLongExpressionCurve(loadSlot.getSlotOrDelegateCancellationExpression(), commodityIndices);
		if (cancellationCurve != null) {
			cancellationFeeProviderEditor.setCancellationExpression(load, cancellationCurve);
		}

		if (!loadSlot.isDESPurchase()) {
			applySlotVesselRestrictions(loadSlot.getSlotOrDelegateVesselRestrictions(), loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive(), load, vesselAssociation);
		} else {
			final Iterator<Pair<Vessel, IVessel>> vesselAssociationIterator = vesselAssociation.iterator();
			while (vesselAssociationIterator.hasNext()) {
				final Pair<Vessel, IVessel> currentAssociation = vesselAssociationIterator.next();
				final Vessel eVessel = currentAssociation.getFirst();
				final IVessel oVessel = currentAssociation.getSecond();
				final int ladenReferenceSpeed;
				final int ballastReferenceSpeed;
				if (shippingDaysRestrictionSpeedProvider == null) {
					ladenReferenceSpeed = ballastReferenceSpeed = oVessel.getMaxSpeed();
				} else {
					ladenReferenceSpeed = OptimiserUnitConvertor.convertToInternalSpeed(shippingDaysRestrictionSpeedProvider.getSpeed(loadSlot, eVessel, true));
					ballastReferenceSpeed = OptimiserUnitConvertor.convertToInternalSpeed(shippingDaysRestrictionSpeedProvider.getSpeed(loadSlot, eVessel, false));
				}
				builder.setShippingDaysRestrictionReferenceSpeed(load, oVessel, VesselState.Laden, ladenReferenceSpeed);
				builder.setShippingDaysRestrictionReferenceSpeed(load, oVessel, VesselState.Ballast, ballastReferenceSpeed);
			}
		}
		return load;
	}

	private ILoadPriceCalculator getPurchasePriceCalculatorForExpression(final String priceExpression) {
		final ExpressionPriceParameters dynamicContract = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		dynamicContract.setPriceExpression(priceExpression);

		final IContractTransformer transformer = contractTransformersByEClass.get(dynamicContract.eClass());
		if (transformer == null) {
			throw new IllegalStateException("No Price Parameters transformer registered for  " + dynamicContract.eClass().getName());
		}
		final ILoadPriceCalculator calculator = transformer.transformPurchasePriceParameters(null, dynamicContract);
		if (calculator == null) {
			throw new IllegalStateException("Unable to transform contract");
		}
		return calculator;
	}

	private void registerSpotMarketSlot(final ModelEntityMap modelEntityMap, final Slot<?> modelSlot, final IPortSlot portSlot) {
		final SpotSlot spotSlot = (SpotSlot) modelSlot;
		final SpotMarket market = spotSlot.getMarket();
		final ISequenceElement element = portSlotProvider.getElement(portSlot);
		final String marketDateKey = String.format("%04d-%02d", modelSlot.getWindowStart().getYear(), modelSlot.getWindowStart().getMonthValue());
		final ISpotMarket o_spotMarket = modelEntityMap.getOptimiserObjectNullChecked(market, ISpotMarket.class);
		spotMarketSlotsProviderEditor.setSpotMarketSlot(element, portSlot, o_spotMarket, marketDateKey);
	}

	private void applyEventVesselRestrictions(final @Nullable List<AVesselSet<Vessel>> allowedVessels, final @NonNull IPortSlot optimiserSlot, final Association<Vessel, IVessel> vesselAssociation) {

		if (allowedVessels == null) {
			return;
		}

		if (!allowedVessels.isEmpty()) {

			List<@NonNull IVessel> permittedVessels = null;

			for (final AVesselSet<Vessel> vesselSet : allowedVessels) {

				for (final Vessel e_vessel : SetUtils.getObjects(vesselSet)) {
					final IVessel o_vessel = vesselAssociation.lookupNullChecked(e_vessel);
					if (permittedVessels == null) {
						permittedVessels = new LinkedList<>();
					}
					permittedVessels.add(o_vessel);
				}
				builder.setVesselPermissions(optimiserSlot, permittedVessels, !allowedVessels.isEmpty());
			}
		}
	}

	private void applySlotVesselRestrictions(final @Nullable List<AVesselSet<Vessel>> restrictedVessels, final boolean isPermissive, final @NonNull IPortSlot optimiserSlot,
			final Association<Vessel, IVessel> vesselAssociation) {

		if (restrictedVessels == null) {
			return;
		}

		if (isPermissive || !restrictedVessels.isEmpty()) {
			final List<@NonNull IVessel> permittedVessels = new LinkedList<>();

			for (final Vessel e_vessel : SetUtils.getObjects(restrictedVessels)) {
				final IVessel o_vessel = vesselAssociation.lookupNullChecked(e_vessel);
				permittedVessels.add(o_vessel);
			}
			builder.setVesselPermissions(optimiserSlot, permittedVessels, isPermissive);
		}
	}

	private void registerSpotCargoMarkets(@NonNull final ModelEntityMap modelEntityMap) {

		final SpotMarketsModel spotMarketsModel = rootObject.getReferenceModel().getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}

		registerSpotMarket(modelEntityMap, spotMarketsModel.getDesPurchaseSpotMarket());
		registerSpotMarket(modelEntityMap, spotMarketsModel.getDesSalesSpotMarket());
		registerSpotMarket(modelEntityMap, spotMarketsModel.getFobPurchasesSpotMarket());
		registerSpotMarket(modelEntityMap, spotMarketsModel.getFobSalesSpotMarket());

	}

	private void buildSpotCargoMarkets(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final ModelEntityMap modelEntityMap,
			final @NonNull Association<Vessel, IVessel> vesselAssociation) {

		// Not needed for a shipping only optimisation
		if (shippingOnly) {
			return;
		}

		if (!withSpotCargoMarkets) {
			return;
		}

		final SpotMarketsModel spotMarketsModel = rootObject.getReferenceModel().getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}
		final ZonedDateTime earliestDate = dateHelper.getEarliestTime();
		final ZonedDateTime latestDate = dateHelper.getLatestTime();

		buildDESPurchaseSpotMarket(builder, portAssociation, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getDesPurchaseSpotMarket());
		buildDESSalesSpotMarket(builder, portAssociation, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getDesSalesSpotMarket(), vesselAssociation);
		buildFOBPurchaseSpotMarket(builder, portAssociation, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getFobPurchasesSpotMarket(), vesselAssociation);
		buildFOBSalesSpotMarket(builder, portAssociation, modelEntityMap, earliestDate, latestDate, spotMarketsModel.getFobSalesSpotMarket());
	}

	private void buildDESPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap, final ZonedDateTime earliestDate,
			final ZonedDateTime latestDate, final SpotMarketGroup desPurchaseSpotMarket) {
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
				// Get the year/month key
				final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

				// Calculate timezone end date for time window
				final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

				// Should this market month be included?
				if (dateHelper.convertTime(tzEndTime) < promptPeriodProviderEditor.getStartOfPromptPeriod()) {
					tzStartTime = tzStartTime.plusMonths(1);
					continue;
				}
				if (dateHelper.convertTime(tzEndTime) < promptPeriodProviderEditor.getStartOfOptimisationPeriod()) {
					tzStartTime = tzStartTime.plusMonths(1);
					continue;
				}

				final List<IPortSlot> marketGroupSlots = new ArrayList<>();

				LOOP_MARKET: for (final SpotMarket market : desPurchaseSpotMarket.getMarkets()) {
					assert market instanceof DESPurchaseMarket;
					if (market instanceof final DESPurchaseMarket desPurchaseMarket && desPurchaseMarket.isEnabled()) {
						final LNGPriceCalculatorParameters priceInfo = desPurchaseMarket.getPriceInfo();
						assert priceInfo != null;
						final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
						final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, priceInfo);

						final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());
						final Set<IPort> marketPorts = new HashSet<>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup(ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot<?>> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<>(count);
						for (final Slot<?> slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = getCappedRemainingSpotOptions(count - existing.size());
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								// As we have no port we create two timewindows. One is pure UTC which we base
								// the EMF Slot date on and shift for the slot binding. The second is UTC with a
								// -12/+14
								// flex for timezones passed into the optimiser slot. This combination allows
								// the slot to be matched against any slot in the same month in any timezone,
								// but be
								// restricted to match the month boundary in that timezone.
								final int trimmedStart = Math.max(promptPeriodProviderEditor.getStartOfPromptPeriod(),
										Math.max(promptPeriodProviderEditor.getStartOfOptimisationPeriod(), dateHelper.convertTime(tzStartTime)));

								final int end = dateHelper.convertTime(tzEndTime);
								assert end > trimmedStart;

								// Create a fake model object to add in here
								final SpotLoadSlot desSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								desSlot.setDESPurchase(true);

								desSlot.setArriveCold(false);
								desSlot.setWindowStart(LocalDate.of(startTime.getYear(), startTime.getMonthValue(), startTime.getDayOfMonth()));
								desSlot.setWindowStartTime(0);
								desSlot.setOptional(true);
								desSlot.setWindowSize(1);
								desSlot.setWindowSizeUnits(TimePeriod.MONTHS);
								// Key piece of information
								desSlot.setMarket(desPurchaseMarket);

								final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
								for (final IPort port : marketPorts) {
									// Re-use the real date objects to map back to integer timezones to avoid
									// mismatching windows caused by half hour timezone shifts
									final ZonedDateTime portWindowStart = desSlot.getWindowStart().atStartOfDay(ZoneId.of(port.getTimeZoneId()));
									final ZonedDateTime portWindowEnd = portWindowStart.plusHours(desSlot.getSchedulingTimeWindow().getSizeInHours());
									// Re-check against opt start date.
									final int trimmedPortWindowStart = Math.max(promptPeriodProviderEditor.getStartOfPromptPeriod(),
											Math.max(promptPeriodProviderEditor.getStartOfOptimisationPeriod(), dateHelper.convertTime(portWindowStart)));
									final int trimmedPortWindowEnd = dateHelper.convertTime(portWindowEnd);

									if (trimmedPortWindowStart <= trimmedPortWindowEnd) {
										final ITimeWindow tw = TimeWindowMaker.createInclusiveInclusive(trimmedPortWindowStart, trimmedPortWindowEnd, 0, false);
										marketPortsMap.put(port, tw);
									}

								}
								if (marketPortsMap.isEmpty()) {
									continue LOOP_MARKET;
								}

								// This should probably be fixed in ScheduleBuilder#matchingWindows and
								// elsewhere if needed, but subtract one to avoid e.g. 1st Feb 00:00 being
								// permitted in the Jan
								// month block
								final ITimeWindow twUTCPlus = createUTCPlusTimeWindow(trimmedStart, end);

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

								final boolean isVolumeLimitInM3 = desPurchaseMarket.getVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3;

								final ILoadOption desPurchaseSlot = builder.createDESPurchaseLoadSlot(internalID, null, twUTCPlus,
										OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()), OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()),
										priceCalculator, cargoCVValue, 0, IPortSlot.NO_PRICING_DATE, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3, false);

								;
								desSlot.setName(externalID);
								modelEntityMap.addModelObject(desSlot, desPurchaseSlot);

								desPurchaseSlot.setKey(String.format("DP-%s-%s", market.getName(), yearMonthString));

								for (final ISlotTransformer slotTransformer : slotTransformers) {
									slotTransformer.slotTransformed(desSlot, desPurchaseSlot);
								}

								builder.bindDischargeSlotsToDESPurchase(desPurchaseSlot, marketPortsMap);

								marketSlots.add(desPurchaseSlot);
								marketGroupSlots.add(desPurchaseSlot);

								registerSpotMarketSlot(modelEntityMap, desSlot, desPurchaseSlot);

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

	private void buildFOBSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap, final ZonedDateTime earliestDate,
			final ZonedDateTime latestDate, final SpotMarketGroup fobSalesSpotMarket) {
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
				// Get the year/month key
				final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

				// Calculate timezone end date for time window
				final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

				// Should this market month be included?
				if (dateHelper.convertTime(tzEndTime) < promptPeriodProviderEditor.getStartOfPromptPeriod()) {
					tzStartTime = tzStartTime.plusMonths(1);
					continue;
				}
				if (dateHelper.convertTime(tzEndTime) < promptPeriodProviderEditor.getStartOfOptimisationPeriod()) {
					tzStartTime = tzStartTime.plusMonths(1);
					continue;
				}

				final List<IPortSlot> marketGroupSlots = new ArrayList<>();

				LOOP_MARKET: for (final SpotMarket market : fobSalesSpotMarket.getMarkets()) {
					assert market instanceof FOBSalesMarket;
					if (market instanceof final FOBSalesMarket fobSaleMarket && fobSaleMarket.isEnabled()) {

						final LNGPriceCalculatorParameters priceInfo = fobSaleMarket.getPriceInfo();
						assert priceInfo != null;
						final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
						final ISalesPriceCalculator priceCalculator = transformer.transformSalesPriceParameters(null, priceInfo);

						final Set<Port> portSet = SetUtils.getObjects(fobSaleMarket.getOriginPorts());
						final Set<IPort> marketPorts = new HashSet<>();
						for (final Port ap : portSet) {
							final IPort ip = portAssociation.lookup(ap);
							if (ip != null) {
								marketPorts.add(ip);
							}
						}

						final Collection<Slot<?>> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<>(count);
						for (final Slot<?> slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = getCappedRemainingSpotOptions(count - existing.size());
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {
								// As we have no port we create two timewindows. One is pure UTC which we base
								// the EMF Slot date on and shift for the slot binding. The second is UTC with a
								// +/- 12 flex
								// for timezones passed into the optimiser slot. This combination allows the
								// slot to be matched against any slot in the same month in any timezone, but be
								// restricted to
								// match the month boundary in that timezone.

								final int trimmedStart = Math.max(promptPeriodProviderEditor.getStartOfPromptPeriod(),
										Math.max(promptPeriodProviderEditor.getStartOfOptimisationPeriod(), dateHelper.convertTime(tzStartTime)));
								final int end = dateHelper.convertTime(tzEndTime);
								assert end > trimmedStart;

								final SpotDischargeSlot fobSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								fobSlot.setFOBSale(true);
								fobSlot.setWindowStart(startTime);
								fobSlot.setWindowStartTime(0);
								fobSlot.setOptional(true);
								fobSlot.setWindowSize(1);
								fobSlot.setWindowSizeUnits(TimePeriod.MONTHS);
								// Key piece of information
								fobSlot.setMarket(fobSaleMarket);

								final Map<IPort, ITimeWindow> marketPortsMap = new HashMap<>();
								for (final IPort port : marketPorts) {

									// Re-use the real date objects to map back to integer timezones to avoid
									// mismatching windows caused by half hour timezone shifts
									final ZonedDateTime portWindowStart = fobSlot.getWindowStart().atStartOfDay(ZoneId.of(port.getTimeZoneId()));
									final ZonedDateTime portWindowEnd = portWindowStart.plusHours(fobSlot.getSchedulingTimeWindow().getSizeInHours());
									// Re-check against opt start date.
									final int trimmedPortWindowStart = Math.max(promptPeriodProviderEditor.getStartOfPromptPeriod(),
											Math.max(promptPeriodProviderEditor.getStartOfOptimisationPeriod(), dateHelper.convertTime(portWindowStart)));
									final int trimmedPortWindowEnd = dateHelper.convertTime(portWindowEnd);
									if (trimmedPortWindowStart <= trimmedPortWindowEnd) {
										final ITimeWindow tw = TimeWindowMaker.createInclusiveInclusive(trimmedPortWindowStart, trimmedPortWindowEnd, 0, false);
										marketPortsMap.put(port, tw);
									}
								}
								if (marketPortsMap.isEmpty()) {
									continue LOOP_MARKET;
								}
								// This should probably be fixed in ScheduleBuilder#matchingWindows and
								// elsewhere if needed, but subtract one to avoid e.g. 1st Feb 00:00 being
								// permitted in the Jan
								// month block
								final ITimeWindow twUTCPlus = createUTCPlusTimeWindow(trimmedStart, end);

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
										priceCalculator, 0, IPortSlot.NO_PRICING_DATE, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3, false);

								// Create a fake model object to add in here
								fobSlot.setName(externalID);
								modelEntityMap.addModelObject(fobSlot, fobSaleSlot);

								fobSaleSlot.setKey(String.format("FS-%s-%s", market.getName(), yearMonthString));

								for (final ISlotTransformer slotTransformer : slotTransformers) {
									slotTransformer.slotTransformed(fobSlot, fobSaleSlot);
								}

								builder.bindLoadSlotsToFOBSale(fobSaleSlot, marketPortsMap);

								marketSlots.add(fobSaleSlot);
								marketGroupSlots.add(fobSaleSlot);

								registerSpotMarketSlot(modelEntityMap, fobSlot, fobSaleSlot);

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
			}
		}
	}

	/**
	 * Given a UTC based time window, extend it's range to cover the whole range of
	 * possible UTC offsets from UTC-12 to UTC+14
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

	private void buildDESSalesSpotMarket(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @NonNull final ModelEntityMap modelEntityMap,
			@NonNull final ZonedDateTime earliestDate, @NonNull final ZonedDateTime latestDate, @Nullable final SpotMarketGroup desSalesSpotMarket,
			final @NonNull Association<Vessel, IVessel> vesselAssociation) {
		if (desSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = desSalesSpotMarket.getAvailability();

			final List<IPortSlot> marketGroupSlots = new ArrayList<>();

			for (final SpotMarket market : desSalesSpotMarket.getMarkets()) {
				assert market instanceof DESSalesMarket;
				if (market instanceof final DESSalesMarket desSalesMarket && desSalesMarket.isEnabled()) {

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
					ZonedDateTime tzStartTime = initialYearMonth.atDay(1).atStartOfDay(notionalAPort.getZoneId());
					// Loop!
					while (!tzStartTime.isAfter(latestDate)) {

						// Convert into timezoneless date objects (for EMF slot object)
						final LocalDate startTime = tzStartTime.toLocalDate();
						// Get the year/month key
						final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

						// Calculate timezone end date for time window
						final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

						// Should this market month be included?
						if (dateHelper.convertTime(tzEndTime) <= promptPeriodProviderEditor.getStartOfPromptPeriod()) {
							tzStartTime = tzStartTime.plusMonths(1);
							continue;
						}
						if (dateHelper.convertTime(tzEndTime) <= promptPeriodProviderEditor.getStartOfOptimisationPeriod()) {
							tzStartTime = tzStartTime.plusMonths(1);
							continue;
						}

						final Collection<Slot<?>> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<>(count);
						for (final Slot<?> slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}
						final int trimmedStart = Math.max(promptPeriodProviderEditor.getStartOfPromptPeriod(),
								Math.max(promptPeriodProviderEditor.getStartOfOptimisationPeriod(), dateHelper.convertTime(tzStartTime)));
						final int end = dateHelper.convertTime(tzEndTime);
						assert end > trimmedStart;

						final int remaining = getCappedRemainingSpotOptions(count - existing.size());
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = TimeWindowMaker.createInclusiveExclusive(trimmedStart, end, 0, false);

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
								desSlot.setPort(notionalAPort);
								desSlot.setWindowSize(1);
								desSlot.setWindowSizeUnits(TimePeriod.MONTHS);

								final int pricingDate = getSlotPricingDate(desSlot);

								final long minVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity());
								final long maxVolume = OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity());

								final boolean isVolumeLimitInM3 = desSalesMarket.getVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3;

								final IDischargeOption desSalesSlot = builder.createDischargeSlot(internalID, notionalIPort, tw, minVolume, maxVolume, 0, Long.MAX_VALUE, priceCalculator,
										desSlot.getSchedulingTimeWindow().getDuration(), pricingDate, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3, false);

								// Key piece of information
								desSlot.setMarket(desSalesMarket);
								modelEntityMap.addModelObject(desSlot, desSalesSlot);

								desSalesSlot.setKey(String.format("DS-%s-%s", market.getName(), yearMonthString));

								for (final ISlotTransformer slotTransformer : slotTransformers) {
									slotTransformer.slotTransformed(desSlot, desSalesSlot);
								}

								marketSlots.add(desSalesSlot);
								marketGroupSlots.add(desSalesSlot);

								registerSpotMarketSlot(modelEntityMap, desSlot, desSalesSlot);

								applySlotVesselRestrictions(desSlot.getSlotOrDelegateVesselRestrictions(), desSlot.getSlotOrDelegateVesselRestrictionsArePermissive(), desSalesSlot, vesselAssociation);
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

	private void buildFOBPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap, final ZonedDateTime earliestDate,
			final ZonedDateTime latestDate, final SpotMarketGroup fobPurchaseSpotMarket, final @NonNull Association<Vessel, IVessel> vesselAssociation) {
		if (fobPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = fobPurchaseSpotMarket.getAvailability();

			final List<IPortSlot> marketGroupSlots = new ArrayList<>();

			for (final SpotMarket market : fobPurchaseSpotMarket.getMarkets()) {
				assert market instanceof FOBPurchasesMarket;
				if (market instanceof final FOBPurchasesMarket fobPurchaseMarket && fobPurchaseMarket.isEnabled()) {
					final LNGPriceCalculatorParameters priceInfo = fobPurchaseMarket.getPriceInfo();
					assert priceInfo != null;

					final IContractTransformer transformer = contractTransformersByEClass.get(priceInfo.eClass());
					assert transformer != null;

					final ILoadPriceCalculator priceCalculator = transformer.transformPurchasePriceParameters(null, priceInfo);
					assert priceCalculator != null;

					final Port notionalAPort = fobPurchaseMarket.getNotionalPort();
					assert notionalAPort != null;
					final IPort notionalIPort = portAssociation.lookupNullChecked(notionalAPort);

					/** Loop over the date range in the optimisation generating market slots */
					// Get the YearMonth of the earliest date in the scenario.
					final YearMonth initialYearMonth = YearMonth.from(earliestDate.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate());

					// Convert this to the 1st of the month in the notional port timezone.
					ZonedDateTime tzStartTime = initialYearMonth.atDay(1).atStartOfDay(notionalAPort.getZoneId());

					while (!tzStartTime.isAfter(latestDate)) {

						// Convert into timezoneless date objects (for EMF slot object)
						final LocalDate startTime = tzStartTime.toLocalDate();
						// Get the year/month key
						final String yearMonthString = SpotSlotUtils.getKeyForDate(startTime);

						// Calculate timezone end date for time window
						final ZonedDateTime tzEndTime = tzStartTime.plusMonths(1);

						// Should this market month be included?
						if (dateHelper.convertTime(tzEndTime) <= promptPeriodProviderEditor.getStartOfPromptPeriod()) {
							tzStartTime = tzStartTime.plusMonths(1);
							continue;
						}
						if (dateHelper.convertTime(tzEndTime) <= promptPeriodProviderEditor.getStartOfOptimisationPeriod()) {
							tzStartTime = tzStartTime.plusMonths(1);
							continue;
						}

						final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(fobPurchaseMarket.getCv());

						final Collection<Slot<?>> existing = getSpotSlots(market, SpotSlotUtils.getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<>(count);
						for (final Slot<?> slot : existing) {
							final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}
						final int trimmedStart = Math.max(promptPeriodProviderEditor.getStartOfPromptPeriod(),
								Math.max(promptPeriodProviderEditor.getStartOfOptimisationPeriod(), dateHelper.convertTime(tzStartTime)));
						final int end = dateHelper.convertTime(tzEndTime);
						assert end > trimmedStart;

						final int remaining = getCappedRemainingSpotOptions(count - existing.size());
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = TimeWindowMaker.createInclusiveExclusive(trimmedStart, end, 0, false);
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

								final boolean isVolumeLimitInM3 = fobPurchaseMarket.getVolumeLimitsUnit() == com.mmxlabs.models.lng.types.VolumeUnits.M3;

								// Create a fake model object to add in here;
								final SpotLoadSlot fobSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								fobSlot.setName(externalID);
								fobSlot.setWindowStart(startTime);
								fobSlot.setWindowStartTime(0);
								fobSlot.setOptional(true);
								fobSlot.setPort(notionalAPort);
								fobSlot.setArriveCold(fobSlot.getPort() == null ? true : !fobSlot.getPort().isAllowCooldown());
								fobSlot.setWindowSize(1);
								fobSlot.setWindowSizeUnits(TimePeriod.MONTHS);

								final ILoadOption fobPurchaseSlot = builder.createLoadSlot(internalID, notionalIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, fobSlot.getSchedulingTimeWindow().getDuration(),
										fobSlot.isArriveCold(), true, false, IPortSlot.NO_PRICING_DATE, transformPricingEvent(market.getPricingEvent()), true, false, true, isVolumeLimitInM3, false);

								// Key piece of information
								fobSlot.setMarket(fobPurchaseMarket);
								modelEntityMap.addModelObject(fobSlot, fobPurchaseSlot);

								fobPurchaseSlot.setKey(String.format("FP-%s-%s", market.getName(), yearMonthString));

								for (final ISlotTransformer slotTransformer : slotTransformers) {
									slotTransformer.slotTransformed(fobSlot, fobPurchaseSlot);
								}

								marketSlots.add(fobPurchaseSlot);
								marketGroupSlots.add(fobPurchaseSlot);

								registerSpotMarketSlot(modelEntityMap, fobSlot, fobPurchaseSlot);

								applySlotVesselRestrictions(fobSlot.getSlotOrDelegateVesselRestrictions(), fobSlot.getSlotOrDelegateVesselRestrictionsArePermissive(), fobPurchaseSlot,
										vesselAssociation);
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

	private void registerSpotMarket(final ModelEntityMap modelEntityMap, final SpotMarketGroup desPurchaseSpotMarket) {
		if (desPurchaseSpotMarket != null) {
			for (final SpotMarket market : desPurchaseSpotMarket.getMarkets()) {
				final ISpotMarket spotMarket = new DefaultSpotMarket(market.getName(), modelEntityMap.getOptimiserObjectNullChecked(market.getEntity(), IEntity.class));
				modelEntityMap.addModelObject(market, spotMarket);
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
			}
		}
		return count;
	}

	private void buildMarkToMarkets(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap) {

		final SpotMarketsModel spotMarketsModel = rootObject.getReferenceModel().getSpotMarketsModel();
		if (spotMarketsModel == null) {
			return;
		}

		buildDESPurchaseMarkToMarket(builder, portAssociation, modelEntityMap, spotMarketsModel.getDesPurchaseSpotMarket());
		buildDESSalesMarkToMarket(builder, portAssociation, modelEntityMap, spotMarketsModel.getDesSalesSpotMarket());
		buildFOBSalesMarkToMarket(builder, portAssociation, modelEntityMap, spotMarketsModel.getFobSalesSpotMarket());
		buildFOBPurchasesMarkToMarket(builder, portAssociation, modelEntityMap, spotMarketsModel.getFobPurchasesSpotMarket());
	}

	private void buildDESPurchaseMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap, final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof DESPurchaseMarket;
				if (market instanceof final DESPurchaseMarket desPurchaseMarket) {
					final Set<Port> portSet = SetUtils.getObjects(desPurchaseMarket.getDestinationPorts());

					final Set<IPort> marketPorts = new HashSet<>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup(ap);
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

	private void buildDESSalesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap, final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof DESSalesMarket;
				if (market instanceof final DESSalesMarket desSalesMarket) {
					final Set<Port> portSet = Collections.singleton(desSalesMarket.getNotionalPort());

					final Set<IPort> marketPorts = new HashSet<>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup(ap);
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

	private void buildFOBSalesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap, final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof FOBSalesMarket;
				if (market instanceof final FOBSalesMarket fobSalesMarket) {
					final Set<Port> portSet = SetUtils.getObjects(fobSalesMarket.getOriginPorts());

					final Set<IPort> marketPorts = new HashSet<>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup(ap);
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

	private void buildFOBPurchasesMarkToMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final ModelEntityMap modelEntityMap,
			final SpotMarketGroup marketGroup) {
		if (marketGroup != null) {

			for (final SpotMarket market : marketGroup.getMarkets()) {
				assert market instanceof FOBPurchasesMarket;
				if (market instanceof final FOBPurchasesMarket fobPurchaseMarket) {
					final Set<Port> portSet = SetUtils.getObjects(fobPurchaseMarket.getMarketPorts());

					final Set<IPort> marketPorts = new HashSet<>();
					for (final Port ap : portSet) {
						final IPort ip = portAssociation.lookup(ap);
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
	 * @param builder           the builder we are working with
	 * @param portAssociation   an association between ports in the EMF model and
	 *                          IPorts in the builder
	 * @param allPorts          the list of all IPorts constructed so far
	 * @param portIndices       a reverse-lookup for the ports in allPorts
	 * @param vesselAssociation
	 * @throws IncompleteScenarioException
	 */
	private void buildDistances(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Vessel, IVessel> vesselAssociation, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final ModelEntityMap modelEntityMap) {

		// set canal route consumptions and toll info
		final PortModel portModel = ScenarioModelUtil.getPortModel(rootObject);
		final CostModel costModel = ScenarioModelUtil.getCostModel(rootObject);

		final Set<IVessel> optimiserVessels = new HashSet<>();
		optimiserVessels.addAll(allVessels.values());
		for (final IVesselCharter vesselCharter : allVesselAvailabilities) {
			final IVessel vessel = vesselCharter.getVessel();
			if (vessel != null) {
				optimiserVessels.add(vessel);
			}

		}
		// Register group based costs first
		{
			for (final RouteCost routeCost : costModel.getRouteCosts()) {
				if (routeCost.getRouteOption() == RouteOption.DIRECT) {
					continue;
				}
				for (final AVesselSet<Vessel> vesselSet : routeCost.getVessels()) {
					if (vesselSet instanceof Vessel) {
						// Skip for now
					} else {
						final ERouteOption mappedRouteOption = mapRouteOption(routeCost.getRouteOption());
						for (final Vessel eVessel : SetUtils.getObjects(vesselSet)) {

							final IVessel oVessel = vesselAssociation.lookup(eVessel);
							assert oVessel != null;
							builder.setVesselRouteCost(mappedRouteOption, oVessel, CostType.Laden,
									new ConstantValueLongCurve(OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getLadenCost())));

							builder.setVesselRouteCost(mappedRouteOption, oVessel, CostType.Ballast,
									new ConstantValueLongCurve(OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost())));

							builder.setVesselRouteCost(mappedRouteOption, oVessel, CostType.RoundTripBallast,
									new ConstantValueLongCurve(OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost())));
						}
					}
				}
			}

		}

		// Next apply formula
		final PanamaCanalTariff panamaCanalTariff = costModel.getPanamaCanalTariff();
		if (panamaCanalTariff != null) {
			buildPanamaCosts(builder, vesselAssociation, optimiserVessels, panamaCanalTariff, dateHelper);
		}

		final SuezCanalTariff suezCanalTariff = costModel.getSuezCanalTariff();
		if (suezCanalTariff != null) {
			buildSuezCosts(builder, vesselAssociation, portAssociation, optimiserVessels, suezCanalTariff, currencyIndices, dateHelper);
		}

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create
		 * the default distance matrix.
		 */
		final Set<RouteOption> seenRoutes = new HashSet<>();
		for (final Route r : portModel.getRoutes()) {
			seenRoutes.add(r.getRouteOption());
			// Store Route under it's name
			modelEntityMap.addModelObject(r, mapRouteOption(r).name());
		}

		// Finally register specific costs to override anything else
		{
			for (final RouteCost routeCost : costModel.getRouteCosts()) {
				if (routeCost.getRouteOption() == RouteOption.DIRECT) {
					continue;
				}
				for (final AVesselSet<Vessel> vesselSet : routeCost.getVessels()) {
					if (vesselSet instanceof final Vessel eVessel) {
						final ERouteOption mappedRouteOption = mapRouteOption(routeCost.getRouteOption());
						final IVessel oVessel = vesselAssociation.lookup(eVessel);
						assert oVessel != null;
						builder.setVesselRouteCost(mappedRouteOption, oVessel, CostType.Laden, new ConstantValueLongCurve(OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getLadenCost())));

						builder.setVesselRouteCost(mappedRouteOption, oVessel, CostType.Ballast,
								new ConstantValueLongCurve(OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost())));

						builder.setVesselRouteCost(mappedRouteOption, oVessel, CostType.RoundTripBallast,
								new ConstantValueLongCurve(OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost())));
					}
				}
			}
		}

		// Register route parameters
		for (final IVessel oVessel : optimiserVessels) {
			assert oVessel != null;
			final Vessel eVessel = vesselAssociation.reverseLookupNullChecked(oVessel);
			for (final VesselRouteParameters routeParameters : eVessel.getVesselOrDelegateRouteParameters()) {
				final ERouteOption mappedRouteOption = mapRouteOption(routeParameters.getRouteOption());
				builder.setVesselRouteTransitTime(mappedRouteOption, oVessel, routeParameters.getExtraTransitTime());

				builder.setVesselRouteFuel(mappedRouteOption, oVessel, VesselState.Laden, OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getLadenConsumptionRate()),
						OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getLadenNBORate()));

				builder.setVesselRouteFuel(mappedRouteOption, oVessel, VesselState.Ballast, OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getBallastConsumptionRate()),
						OptimiserUnitConvertor.convertToInternalDailyRate(routeParameters.getBallastNBORate()));
			}
		}
	}

	public static void buildPanamaCosts(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Vessel, IVessel> vesselAssociation, final Collection<IVessel> vessels,
			@NonNull final PanamaCanalTariff panamaCanalTariff, final @NonNull DateAndCurveHelper dateHelper) {

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

		for (final IVessel vessel : vessels) {
			assert vessel != null;
			final Vessel eVessel = vesselAssociation.reverseLookupNullChecked(vessel);
			final int capacityInM3 = eVessel.getVesselOrDelegateCapacity();

			final PreGeneratedLongCurve ladenCurve = new PreGeneratedLongCurve();
			final PreGeneratedLongCurve ballastCurve = new PreGeneratedLongCurve();
			final PreGeneratedLongCurve ballastRoundtripCurve = new PreGeneratedLongCurve();

			// Legacy pricing model
			{

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
				ladenCurve.setDefaultValue(OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalLadenCost)));
				ballastCurve.setDefaultValue(OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalBallastCost)));
				ballastRoundtripCurve.setDefaultValue(OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalBallastRoundTripCost)));
			}

			// Pricing model from 1st Jan 2023
			for (final PanamaTariffV2 tariff : panamaCanalTariff.getAnnualTariffs()) {
				final int time = dateHelper.convertTime(tariff.getEffectiveFrom().atStartOfDay(ZoneId.of("Etc/UTC")));

				double totalLadenCost = tariff.getFixedFee() + tariff.getCapacityTariff() * capacityInM3;
				// Ballast is 85% of laden cost
				double totalBallastCost = 0.85 * totalLadenCost;
				// No more round trip discount
				double totalBallastRoundTripCost = totalBallastCost;

				// If there is a markup %, apply it
				if (panamaCanalTariff.getMarkupRate() != 0.0) {
					final double multiplier = 1.0 + panamaCanalTariff.getMarkupRate();
					totalLadenCost *= multiplier;
					totalBallastCost *= multiplier;
					totalBallastRoundTripCost *= multiplier;
				}

				ladenCurve.setValueAfter(time, OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalLadenCost)));
				ballastCurve.setValueAfter(time, OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalBallastCost)));
				ballastRoundtripCurve.setValueAfter(time, OptimiserUnitConvertor.convertToInternalFixedCost((int) Math.round(totalBallastRoundTripCost)));
			}

			builder.setVesselRouteCost(ERouteOption.PANAMA, vessel, CostType.Laden, ladenCurve);
			builder.setVesselRouteCost(ERouteOption.PANAMA, vessel, CostType.Ballast, ballastCurve);
			builder.setVesselRouteCost(ERouteOption.PANAMA, vessel, CostType.RoundTripBallast, ballastRoundtripCurve);
		}
	}

	public static void buildSuezCosts(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Vessel, IVessel> vesselAssociation, @NonNull final Association<Port, IPort> portAssociation,
			final Collection<IVessel> vesselAvailabilities, @NonNull final SuezCanalTariff suezCanalTariff, final @NonNull SeriesParser currencyIndices, final @NonNull DateAndCurveHelper dateHelper) {

		// Extract band information into a sorted list
		final List<Pair<Integer, SuezCanalTariffBand>> bands = new LinkedList<>();
		for (final SuezCanalTariffBand band : suezCanalTariff.getBands()) {
			int upperBound = Integer.MAX_VALUE;
			if (band.isSetBandEnd()) {
				upperBound = band.getBandEnd();
			}
			bands.add(new Pair<>(upperBound, band));
		}
		// Sort the bands smallest to largest
		Collections.sort(bands, (b1, b2) -> b1.getFirst().compareTo(b2.getFirst()));

		for (final IVessel oVessel : vesselAvailabilities) {
			assert oVessel != null;
			final Vessel eVessel = vesselAssociation.reverseLookup(oVessel);
			final int scnt = eVessel.getVesselOrDelegateSCNT();

			if (scnt == 0) {
				continue;
			}

			double ladenCostInSDR = 0.0;
			double ballastCostInSDR = 0.0;
			for (final Pair<Integer, SuezCanalTariffBand> p : bands) {
				final SuezCanalTariffBand band = p.getSecond();
				//// How much vessel capacity is used for this band calculation?
				// First, find the largest value valid in this band
				int contributingLevel = Math.min(scnt, p.getFirst());

				// Next, subtract band lower bound to find the capacity contribution
				contributingLevel = Math.max(0, contributingLevel - (band.isSetBandStart() ? band.getBandStart() : 0));

				if (contributingLevel > 0) {
					ladenCostInSDR += contributingLevel * band.getLadenTariff();
					ballastCostInSDR += contributingLevel * band.getBallastTariff();
				}
			}

			// If there is a markup %, apply it
			if (suezCanalTariff.getDiscountFactor() != 0.0) {
				final double multiplier = 1.0 - suezCanalTariff.getDiscountFactor();
				ladenCostInSDR *= multiplier;
				ballastCostInSDR *= multiplier;
			}

			int tugCount = 0;
			for (final SuezCanalTugBand band : suezCanalTariff.getTugBands()) {
				if (!band.isSetBandEnd() && scnt >= band.getBandStart()) {
					tugCount = band.getTugs();
					break;
				} else if (!band.isSetBandStart() && scnt < band.getBandEnd()) {
					tugCount = band.getTugs();
					break;
				} else if (scnt >= band.getBandStart() && scnt < band.getBandEnd()) {
					tugCount = band.getTugs();
					break;
				}
			}

			final double extraCosts = ((double) tugCount * suezCanalTariff.getTugCost()) + suezCanalTariff.getFixedCosts();

			final String ladenExpression = String.format("(%.3f*%s)+%.3f", ladenCostInSDR, suezCanalTariff.getSdrToUSD(), extraCosts);
			final String ballastExpression = String.format("(%.3f*%s)+%.3f", ballastCostInSDR, suezCanalTariff.getSdrToUSD(), extraCosts);

			@Nullable
			final ILongCurve ladenCostCurve = dateHelper.generateLongExpressionCurve(ladenExpression, currencyIndices);
			assert ladenCostCurve != null;
			builder.setVesselRouteCost(ERouteOption.SUEZ, oVessel, CostType.Laden, ladenCostCurve);

			final ILongCurve ballastCostCurve = dateHelper.generateLongExpressionCurve(ballastExpression, currencyIndices);
			assert ballastCostCurve != null;
			builder.setVesselRouteCost(ERouteOption.SUEZ, oVessel, CostType.Ballast, ballastCostCurve);
			builder.setVesselRouteCost(ERouteOption.SUEZ, oVessel, CostType.RoundTripBallast, ballastCostCurve);

			for (final SuezCanalRouteRebate rebate : suezCanalTariff.getRouteRebates()) {
				final Set<Port> fromPorts = SetUtils.getObjects(rebate.getFrom());
				if (fromPorts.isEmpty()) {
					continue;
				}

				final Set<Port> toPorts = SetUtils.getObjects(rebate.getTo());
				if (toPorts.isEmpty()) {
					continue;
				}

				final long oFactor = OptimiserUnitConvertor.convertToInternalPercentage(rebate.getRebate());

				for (final Port from : fromPorts) {
					final IPort oFrom = portAssociation.lookupNullChecked(from);
					for (final Port to : toPorts) {
						final IPort oTo = portAssociation.lookupNullChecked(to);
						builder.setSuezRouteRebate(oFrom, oTo, oFactor);
						builder.setSuezRouteRebate(oTo, oFrom, oFactor);
					}
				}

			}
		}
	}

	/**
	 * Construct the fleet model for the scenario
	 * 
	 * @param builder         a builder which has had its ports and distances
	 *                        instantiated
	 * @param portAssociation the Port <-> IPort association to connect EMF Ports
	 *                        with builder IPorts
	 * @param modelEntityMap
	 * @return
	 */
	private @NonNull Association<Vessel, IVessel> buildFleet(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation,
			@NonNull final ModelEntityMap modelEntityMap) {

		/*
		 * Build the fleet model - first we must create the vessels from the model
		 */
		final Association<Vessel, IVessel> vesselAssociation = new Association<>();
		final Association<VesselCharter, IVesselCharter> vesselCharterAssociation = new Association<>();

		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(rootObject);

		// look up prices

		for (final Vessel eVessel : fleetModel.getVessels()) {
			assert eVessel != null;
			// TODO: TEMPORARY FIX: Populate fleet data with default values for additional
			// fuel types.
			final IVessel oVessel;

			if (eVessel.isMarker() && (builder instanceof final SchedulerBuilder sBuilder)) {
				final long capacity = OptimiserUnitConvertor.convertToInternalVolume((int) (eVessel.getVesselOrDelegateCapacity() * eVessel.getVesselOrDelegateFillCapacity()));

				oVessel = sBuilder.createVirtualMarkerVessel(eVessel.getName(), capacity);
			} else {
				final IBaseFuel oTravelBaseFuel = modelEntityMap.getOptimiserObjectNullChecked(eVessel.getVesselOrDelegateBaseFuel(), IBaseFuel.class);
				IBaseFuel oIdleBaseFuel = null;
				final BaseFuel eIdleBaseFuel = eVessel.getVesselOrDelegateIdleBaseFuel();
				if (eIdleBaseFuel != null) {
					oIdleBaseFuel = modelEntityMap.getOptimiserObjectNullChecked(eIdleBaseFuel, IBaseFuel.class);
				} else {
					oIdleBaseFuel = oTravelBaseFuel;
				}
				IBaseFuel oInPortBaseFuel = null;
				final BaseFuel eInPortBaseFuel = eVessel.getVesselOrDelegateInPortBaseFuel();
				if (eInPortBaseFuel != null) {
					oInPortBaseFuel = modelEntityMap.getOptimiserObjectNullChecked(eInPortBaseFuel, IBaseFuel.class);
				} else {
					oInPortBaseFuel = oTravelBaseFuel;
				}
				IBaseFuel oPilotLightBaseFuel = null;
				final BaseFuel ePilotLightBaseFuel = eVessel.getVesselOrDelegatePilotLightBaseFuel();
				if (ePilotLightBaseFuel != null) {
					oPilotLightBaseFuel = modelEntityMap.getOptimiserObjectNullChecked(ePilotLightBaseFuel, IBaseFuel.class);
				} else {
					oPilotLightBaseFuel = oTravelBaseFuel;
				}

				oVessel = TransformerHelper.buildIVessel(builder, eVessel, oTravelBaseFuel, oIdleBaseFuel, oInPortBaseFuel, oPilotLightBaseFuel);

				/*
				 * set up inaccessible ports by applying resource allocation constraints
				 */
				final Set<IPort> oInaccessiblePorts = new HashSet<>();
				for (final Port ePort : SetUtils.getObjects(eVessel.getVesselOrDelegateInaccessiblePorts())) {
					oInaccessiblePorts.add(portAssociation.lookup(ePort));
				}

				if (!oInaccessiblePorts.isEmpty()) {
					builder.setVesselInaccessiblePorts(oVessel, oInaccessiblePorts);
				}
			}

			/*
			 * set up inaccessible routes for vessel
			 */
			getAndSetInaccessibleRoutesForVessel(builder, eVessel, oVessel);

			vesselAssociation.add(eVessel, oVessel);
			modelEntityMap.addModelObject(eVessel, oVessel);
			allVessels.put(eVessel, oVessel);
		}

		final CargoModel cargoModel = rootObject.getCargoModel();
		// Sorted by fleet model vessel order
		final List<VesselCharter> sortedAvailabilities = new ArrayList<>();
		{
			for (final Vessel vessel : fleetModel.getVessels()) {

				for (final VesselCharter vesselCharter : cargoModel.getVesselCharters()) {
					if (vesselCharter.getVessel() == vessel) {
						sortedAvailabilities.add(vesselCharter);
					}
				}
			}

		}

		if (extraVesselCharters != null) {
			sortedAvailabilities.addAll(extraVesselCharters);
		}

		// Now register the availabilities.
		for (final VesselCharter eVesselCharter : sortedAvailabilities) {
			final Vessel eVessel = eVesselCharter.getVessel();
			assert eVessel != null;

			final Port startingPort = eVesselCharter.getStartAt();

			final StartHeelOptions startHeel = eVesselCharter.getStartHeel();
			assert startHeel != null;
			final IHeelOptionSupplier heelSupplier = createHeelSupplier(startHeel, null);
			final IStartRequirement startRequirement = createStartRequirement(builder, portAssociation, eVesselCharter.isSetStartAfter() ? eVesselCharter.getStartAfterAsDateTime() : null,
					eVesselCharter.isSetStartBy() ? eVesselCharter.getStartByAsDateTime() : null, startingPort, heelSupplier);

			final ZonedDateTime endBy = eVesselCharter.isSetEndBy() ? eVesselCharter.getEndByAsDateTime() : null;
			ZonedDateTime endAfter = eVesselCharter.isSetEndAfter() ? eVesselCharter.getEndAfterAsDateTime() : null;

			if (rootObject.isSetSchedulingEndDate()) {
				final ZonedDateTime schedulingEndDate = rootObject.getSchedulingEndDate().atStartOfDay(ZoneId.of("Etc/UTC"));
				if (endAfter != null && endAfter.isAfter(schedulingEndDate)) {
					endAfter = schedulingEndDate;
				}
			}

			final EndHeelOptions endHeel = eVesselCharter.getEndHeel();
			assert endHeel != null;
			final IHeelOptionConsumer heelConsumer = createHeelConsumer(endHeel);
			final Set<Port> endPorts = SetUtils.getObjects(eVesselCharter.getEndAt());
			// Assume validation ensures at least one valid port will remain if initial set
			// has ports present.
			endPorts.removeAll(SetUtils.getObjects(eVessel.getVesselOrDelegateInaccessiblePorts()));
			final IEndRequirement endRequirement = createEndRequirement(builder, portAssociation, endAfter, endBy, endPorts, heelConsumer);

			final int minDuration = eVesselCharter.getCharterOrDelegateMinDuration();
			if (minDuration != 0) {
				endRequirement.setMinDurationInHours(minDuration * 24);
			}

			final int maxDuration = eVesselCharter.getCharterOrDelegateMaxDuration();
			if (maxDuration != 0) {
				endRequirement.setMaxDurationInHours(maxDuration * 24);
			}

			final ILongCurve dailyCharterInCurve;
			if (eVesselCharter.isSetTimeCharterRate()) {
				dailyCharterInCurve = dateHelper.generateLongExpressionCurve(eVesselCharter.getTimeCharterRate(), charterIndices);
			} else {
				dailyCharterInCurve = new ConstantValueLongCurve(0);
			}
			assert dailyCharterInCurve != null;

			final IVessel vessel = vesselAssociation.lookupNullChecked(eVessel);

			final GenericCharterContract eBallastBonusContract = eVesselCharter.getCharterOrDelegateCharterContract();
			final ICharterContract ballastBonusContract = createAndGetCharterContract(eBallastBonusContract);

			final IVesselCharter vesselCharter = builder.createVesselCharter(vessel, dailyCharterInCurve,
					eVesselCharter.isSetTimeCharterRate() ? VesselInstanceType.TIME_CHARTER : VesselInstanceType.FLEET, startRequirement, endRequirement, ballastBonusContract,
					new ConstantValueLongCurve(0), eVesselCharter.isOptional());
			vesselCharterAssociation.add(eVesselCharter, vesselCharter);

			modelEntityMap.addModelObject(eVesselCharter, vesselCharter);

			allVesselAvailabilities.add(vesselCharter);

			for (final IVesselCharterTransformer vesselCharterTransformer : vesselCharterTransformers) {
				vesselCharterTransformer.vesselCharterTransformed(eVesselCharter, vesselCharter);
			}
		}

		// Spot charter market generation
		{
			final SpotMarketsModel spotMarketsModel = rootObject.getReferenceModel().getSpotMarketsModel();

			final CharterOutMarketParameters charterOutMarketParameters = spotMarketsModel.getCharterOutMarketParameters();
			if (charterOutMarketParameters != null) {
				if (charterOutMarketParameters.getCharterOutStartDate() != null) {
					builder.setGeneratedCharterOutStartTime(dateHelper.convertTime(charterOutMarketParameters.getCharterOutStartDate().atStartOfDay(ZoneId.of("UTC"))));
				} else {
					builder.setGeneratedCharterOutStartTime(0);
				}
				if (charterOutMarketParameters.getCharterOutEndDate() != null) {
					builder.setGeneratedCharterOutEndTime(dateHelper.convertTime(charterOutMarketParameters.getCharterOutEndDate().atStartOfDay(ZoneId.of("UTC"))));
				} else {
					builder.setGeneratedCharterOutEndTime(Integer.MAX_VALUE);
				}
			} else {
				builder.setGeneratedCharterOutStartTime(0);
				builder.setGeneratedCharterOutEndTime(Integer.MAX_VALUE);
			}

			final List<CharterInMarket> charterInMarkets = new LinkedList<>();
			charterInMarkets.addAll(spotMarketsModel.getCharterInMarkets());
			if (extraCharterInMarkets != null) {
				charterInMarkets.addAll(extraCharterInMarkets);
			}
			Pair<CharterInMarket, CharterInMarket> adpOriginalToClone = null;
			final ADPModel adpModel = rootObject.getAdpModel();
			if (adpModel != null) {
				final CharterInMarket defaultNominalMarket = adpModel.getFleetProfile().getDefaultNominalMarket();
				final CharterInMarket simpleMarket = EcoreUtil.copy(defaultNominalMarket);

				// Reset various fields
				simpleMarket.unsetStartAt();
				simpleMarket.unsetEndAt();
				simpleMarket.unsetGenericCharterContract();
				simpleMarket.unsetMaxDuration();
				simpleMarket.unsetMinDuration();
				simpleMarket.unsetStartHeelCV();

				simpleMarket.setSpotCharterCount(0);

				adpOriginalToClone = Pair.of(defaultNominalMarket, simpleMarket);

				charterInMarkets.add(simpleMarket);
			}

			for (final CharterInMarket charterInMarket : charterInMarkets) {
				final Vessel eVessel = charterInMarket.getVessel();
				assert eVessel != null;

				final IVessel oVessel = vesselAssociation.lookupNullChecked(eVessel);

				final ILongCurve charterInCurve;
				if (charterInMarket.getCharterInRate() != null) {
					charterInCurve = dateHelper.generateLongExpressionCurve(charterInMarket.getCharterInRate(), charterIndices);
				} else {
					charterInCurve = new ConstantValueLongCurve(0);
				}
				assert charterInCurve != null;

				final int charterCount = charterInMarket.getSpotCharterCount();
				final GenericCharterContract eCharterContract = charterInMarket.getGenericCharterContract();
				final @Nullable ICharterContract oCharterContract = createAndGetCharterContract(eCharterContract);

				StartHeelOptions startHeel = null;
				IHeelOptionSupplier heelSupplier = null;
				EndHeelOptions endHeel = null;
				IHeelOptionConsumer heelConsumer = null;
				if (eCharterContract != null) {
					startHeel = eCharterContract.getStartHeel();
					endHeel = eCharterContract.getEndHeel();
					if (startHeel != null) {
						// Check override
						final Integer startCV = charterInMarket.isSetStartHeelCV() ? OptimiserUnitConvertor.convertToInternalConversionFactor(charterInMarket.getStartHeelCV()) : null;
						heelSupplier = createHeelSupplier(startHeel, startCV);
					}
					if (endHeel != null) {
						heelConsumer = createHeelConsumer(endHeel);
					}
				}
				if (heelSupplier == null) {
					final int startCV = charterInMarket.isSetStartHeelCV() ? OptimiserUnitConvertor.convertToInternalConversionFactor(charterInMarket.getStartHeelCV()) : 0;
					heelSupplier = new HeelOptionSupplier(oVessel.getSafetyHeel(), oVessel.getSafetyHeel(), startCV, new ConstantHeelPriceCalculator(0));
				}
				if (heelConsumer == null) {
					heelConsumer = new HeelOptionConsumer(oVessel.getSafetyHeel(), oVessel.getSafetyHeel(), VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(0), false);
				}
				final Port startingPort = charterInMarket.getStartAt();
				final IStartRequirement charterInStartRule = builder.createStartRequirement(portAssociation.lookup(startingPort), false, null, heelSupplier);

				final IEndRequirement charterInEndRule;
				{

					final Set<Port> endPorts = SetUtils.getObjects(charterInMarket.getEndAt());
					if (endPorts.isEmpty()) {
						charterInEndRule = builder.createEndRequirement(null, false, new TimeWindow(0, Integer.MAX_VALUE), heelConsumer);
					} else {
						// user defined set of ports
						endPorts.removeAll(SetUtils.getObjects(eVessel.getVesselOrDelegateInaccessiblePorts()));
						final Set<IPort> portSet = new LinkedHashSet<>();
						for (final Port p : endPorts) {
							portSet.add(portAssociation.lookup(p));
						}
						charterInEndRule = builder.createEndRequirement(portSet, false, new TimeWindow(0, Integer.MAX_VALUE), heelConsumer);
					}
				}

				final int minDurationInDays = charterInMarket.getMarketOrContractMinDuration();
				final int maxDurationInDays = charterInMarket.getMarketOrContractMaxDuration();
				if (maxDurationInDays != 0 || minDurationInDays != 0) {
					if (maxDurationInDays != 0) {
						charterInEndRule.setMaxDurationInHours(maxDurationInDays * 24);
					}
					if (minDurationInDays != 0) {
						charterInEndRule.setMinDurationInHours(minDurationInDays * 24);
					}
				}

				final ISpotCharterInMarket spotCharterInMarket = builder.createSpotCharterInMarket(charterInMarket.getName(), oVessel, charterInCurve, charterInMarket.isNominal(), charterCount,
						charterInStartRule, charterInEndRule, oCharterContract, new ConstantValueLongCurve(0));

				modelEntityMap.addModelObject(charterInMarket, spotCharterInMarket);

				// spot charter in market nominal
				// Only create a nominal vessel if enabled
				if (charterInMarket.isNominal()) {
					final IVesselCharter roundTripOption = builder.createRoundTripCargoVessel("RoundTrip-" + charterInMarket.getName(), spotCharterInMarket);
					final NonNullPair<CharterInMarket, Integer> key = new NonNullPair<>(charterInMarket, NOMINAL_CARGO_INDEX);
					spotCharterInToAvailability.put(key, roundTripOption);
					allVesselAvailabilities.add(roundTripOption);

					if (adpOriginalToClone != null && charterInMarket == adpOriginalToClone.getSecond()) {
						// During export, look up the original market, not the temporary clone.
						modelEntityMap.addOptimiserToModelOnlyMapping(spotCharterInMarket, adpOriginalToClone.getFirst());

						// Bind this market to the ADP model. Really we could do with some "tags" for
						// special named items.
						modelEntityMap.addNamedOptimiserObject(OptimiserConstants.DEFAULT_INTERNAL_VESSEL, roundTripOption);
					}
					if (adpOriginalToClone != null && charterInMarket == adpOriginalToClone.getFirst()) {
						modelEntityMap.addNamedOptimiserObject(OptimiserConstants.DEFAULT_EXTERNAL_VESSEL, roundTripOption);
					}
				}

				// spot charter in market vessel availability
				if (charterCount > 0 && charterInMarket.isEnabled()) {

					final List<IVesselCharter> spots = builder.createSpotVessels("SPOT-" + charterInMarket.getName(), spotCharterInMarket);
					for (int i = 0; i < spots.size(); ++i) {
						final NonNullPair<CharterInMarket, Integer> key = new NonNullPair<>(charterInMarket, i);
						final IVesselCharter spotAvailability = spots.get(i);
						spotCharterInToAvailability.put(key, spotAvailability);

						for (final IVesselCharterTransformer vesselCharterTransformer : vesselCharterTransformers) {
							vesselCharterTransformer.charterInVesselCharterTransformed(charterInMarket, spotAvailability);
						}

						allVesselAvailabilities.add(spotAvailability);
					}
				}
			}

			final List<CharterInMarketOverride> overrides = new LinkedList<>();
			overrides.addAll(cargoModel.getCharterInMarketOverrides());
			overrides.addAll(extraCharterInMarketOverrides);
			for (final CharterInMarketOverride charterInMarketOverride : overrides) {
				final CharterInMarket charterInMarket = charterInMarketOverride.getCharterInMarket();
				final ISpotCharterInMarket spotCharterInMarket = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

				final Vessel eVessel = charterInMarket.getVessel();
				assert eVessel != null;
				final IVessel oVessel = vesselAssociation.lookupNullChecked(eVessel);

				@Nullable
				ICharterContract charterContract = null;

				if (charterInMarketOverride.isIncludeBallastBonus()) {
					charterContract = spotCharterInMarket.getCharterContract();
				}

				{
					final IStartRequirement start;
					if (charterInMarketOverride.isSetStartDate() || charterInMarketOverride.getStartHeel() != null) {
						ITimeWindow tw;
						if (charterInMarketOverride.getStartDate() != null) {
							final int time = dateHelper.convertTime(charterInMarketOverride.getStartDateAsDateTime());
							tw = new TimeWindow(time, time + 1);
						} else {
							tw = null;
						}
						IHeelOptionSupplier heelOptions;
						if (charterInMarketOverride.getStartHeel() != null) {
							heelOptions = createHeelSupplier(charterInMarketOverride.getStartHeel(), null);
						} else {
							heelOptions = builder.createHeelSupplier(oVessel.getSafetyHeel(), oVessel.getSafetyHeel(), 0, new ConstantHeelPriceCalculator(0));
						}
						start = builder.createStartRequirement(null, tw != null, tw, heelOptions);
					} else if (spotCharterInMarket.getStartRequirement() != null) {
						start = spotCharterInMarket.getStartRequirement();
					} else {
						final IHeelOptionSupplier heelOptions = builder.createHeelSupplier(oVessel.getSafetyHeel(), oVessel.getSafetyHeel(), 0, new ConstantHeelPriceCalculator(0));
						start = builder.createStartRequirement(null, false, null, heelOptions);
					}

					final IEndRequirement end;
					if (charterInMarketOverride.getEndPort() != null || charterInMarketOverride.getEndDate() != null || charterInMarketOverride.getEndHeel() != null) {
						ITimeWindow tw;
						if (charterInMarketOverride.getEndDate() != null) {
							final int time = dateHelper.convertTime(charterInMarketOverride.getEndDateAsDateTime());
							tw = new TimeWindow(time, time + 1);
						} else {
							tw = new TimeWindow(0, Integer.MAX_VALUE);
						}
						IHeelOptionConsumer heelOptions;
						if (charterInMarketOverride.getEndHeel() != null) {
							heelOptions = createHeelConsumer(charterInMarketOverride.getEndHeel());
						} else {
							heelOptions = spotCharterInMarket.getEndRequirement().getHeelOptions();
						}
						Collection<IPort> portSet;
						if (charterInMarketOverride.getEndPort() != null) {
							portSet = Collections.singleton(portAssociation.lookupNullChecked(charterInMarketOverride.getEndPort()));
						} else {
							portSet = spotCharterInMarket.getEndRequirement().getLocations();
						}
						end = builder.createEndRequirement(portSet, tw.getExclusiveEnd() != Integer.MAX_VALUE, tw, heelOptions);
					} else if (spotCharterInMarket.getEndRequirement() != null) {
						end = spotCharterInMarket.getEndRequirement();
					} else {
						final IHeelOptionConsumer heelConsumer = builder.createHeelConsumer(oVessel.getSafetyHeel(), oVessel.getSafetyHeel(), VesselTankState.MUST_BE_COLD,
								new ConstantHeelPriceCalculator(0), false);
						end = createSpotEndRequirement(builder, portAssociation, Collections.emptySet(), heelConsumer);
					}

					final int maxDurationInDays = charterInMarketOverride.getLocalOrDelegateMaxDuration();
					if (maxDurationInDays != 0) {
						end.setMaxDurationInHours(maxDurationInDays * 24);
					}
					final int minDurationInDays = charterInMarketOverride.getLocalOrDelegateMinDuration();
					if (minDurationInDays != 0) {
						end.setMinDurationInHours(minDurationInDays * 24);
					}

					final ILongCurve dailyCharterInPrice = spotCharterInMarket.getDailyCharterInRateCurve();
					final IVessel spotVessel = spotCharterInMarket.getVessel();

					// End cold already enforced in VoyagePlanner#getVoyageOptionsAndSetVpoChoices
					final IVesselCharter spotAvailability = builder.createVesselCharter(spotVessel, dailyCharterInPrice, VesselInstanceType.SPOT_CHARTER, start, end, charterContract,
							new ConstantValueLongCurve(0L), true);

					// FIX API!
					if (spotAvailability instanceof final DefaultVesselCharter defaultVesselCharter) {
						if (spotCharterInMarket != null) {
							defaultVesselCharter.setSpotCharterInMarket(spotCharterInMarket);
							defaultVesselCharter.setSpotIndex(charterInMarketOverride.getSpotIndex());
						}
					}

					modelEntityMap.addModelObject(charterInMarketOverride, spotAvailability);

					for (final IVesselCharterTransformer vesselCharterTransformer : vesselCharterTransformers) {
						vesselCharterTransformer.charterInVesselCharterTransformed(charterInMarket, spotAvailability);
					}
					allVesselAvailabilities.add(spotAvailability);
				}
			}

			for (final CharterOutMarket eCharterOutMarket : spotMarketsModel.getCharterOutMarkets()) {
				assert eCharterOutMarket != null;

				if (!eCharterOutMarket.isEnabled()) {
					continue;
				}

				if (eCharterOutMarket.getCharterOutRate() != null) {

					final DefaultSpotCharterOutMarket iCharterOutMarket = new DefaultSpotCharterOutMarket(eCharterOutMarket.getName());

					modelEntityMap.addModelObject(eCharterOutMarket, iCharterOutMarket);
					final ILongCurve charterOutCurve = dateHelper.generateLongExpressionCurve(eCharterOutMarket.getCharterOutRate(), charterIndices);
					assert charterOutCurve != null;

					final int minDuration = 24 * eCharterOutMarket.getMinCharterOutDuration();

					final int maxDuration = eCharterOutMarket.isSetMaxCharterOutDuration() ? 24 * eCharterOutMarket.getMaxCharterOutDuration() : Integer.MAX_VALUE;

					final Set<Port> portSet = SetUtils.getObjects(eCharterOutMarket.getAvailablePorts());

					final List<Port> sortedPortSet = new ArrayList<>(portSet);
					Collections.sort(sortedPortSet, (p1, p2) -> p1.getName().compareTo(p2.getName()));

					final Set<IPort> marketPorts = new LinkedHashSet<>();
					for (final Port ap : sortedPortSet) {
						final IPort ip = portAssociation.lookup(ap);
						if (ip != null) {
							marketPorts.add(ip);
						}
					}
					for (final Vessel eVessel : SetUtils.getObjects(eCharterOutMarket.getVessels())) {
						builder.createCharterOutCurve(vesselAssociation.lookupNullChecked(eVessel), charterOutCurve, minDuration, maxDuration, marketPorts, iCharterOutMarket);
					}
				}
			}
		}

		return vesselAssociation;
	}

	private void getAndSetInaccessibleRoutesForVessel(final ISchedulerBuilder builder, final Vessel eVessel, final IVessel vessel) {
		final Set<ERouteOption> inaccessibleERoutesForVessel = createRouteOptionSet(eVessel.getVesselOrDelegateInaccessibleRoutes());
		setInaccessibleRoutesForVessel(builder, vessel, inaccessibleERoutesForVessel);
	}

	private void setInaccessibleRoutesForVessel(final ISchedulerBuilder builder, final IVessel vessel, final Set<ERouteOption> inaccessibleERoutesForVessel) {
		builder.setVesselInaccessibleRoutes(vessel, inaccessibleERoutesForVessel);
	}

	public Set<ERouteOption> createRouteOptionSet(final Collection<RouteOption> routeOptions) {
		final Set<ERouteOption> inaccessibleERoutes = new HashSet<>();
		for (final RouteOption routeOption : routeOptions) {
			inaccessibleERoutes.add(mapRouteOption(routeOption));
		}
		return inaccessibleERoutes;
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal
	 * use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param pat
	 * @return
	 */
	@NonNull
	private IStartRequirement createStartRequirement(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @Nullable final ZonedDateTime from,
			@Nullable final ZonedDateTime to, @Nullable final Port port, @Nullable final IHeelOptionSupplier heelSupplier) {
		final ITimeWindow window;
		boolean hasTimeRequirement = true;
		if (from == null && to != null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(dateHelper.getEarliestTime()), dateHelper.convertTime(to), 0, false);
		} else if (from != null && to == null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(from), dateHelper.convertTime(dateHelper.getLatestTime()), 0, false);
		} else if (from != null && to != null) {
			window = TimeWindowMaker.createInclusiveInclusive(dateHelper.convertTime(from), dateHelper.convertTime(to), 0, false);
		} else {
			window = TimeWindowMaker.createInclusiveInclusive(0, 0, 0, false);
			hasTimeRequirement = false;
		}

		return builder.createStartRequirement(portAssociation.lookup(port), hasTimeRequirement, window, heelSupplier);
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal
	 * use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param force
	 * @param pat
	 * @return
	 */
	@NonNull
	private IEndRequirement createSpotEndRequirement(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @Nullable final Set<Port> ports,
			final IHeelOptionConsumer heelConsumer) {

		final Set<IPort> portSet = new LinkedHashSet<>();
		if (ports != null) {
			for (final Port p : ports) {
				portSet.add(portAssociation.lookup(p));
			}
		}
		// Is the availability open ended or do we force the end rule?
		if (ports == null || ports.isEmpty()) {
			return builder.createEndRequirement(null, false, new TimeWindow(0, Integer.MAX_VALUE), heelConsumer);
		} else {
			return builder.createEndRequirement(portSet, false, new TimeWindow(0, Integer.MAX_VALUE), heelConsumer);
		}
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal
	 * use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param force
	 * @param pat
	 * @return
	 */
	@NonNull
	private IEndRequirement createEndRequirement(@NonNull final ISchedulerBuilder builder, @NonNull final Association<Port, IPort> portAssociation, @Nullable final ZonedDateTime from,
			@Nullable final ZonedDateTime to, @Nullable final Set<Port> ports, final IHeelOptionConsumer heelConsumer) {
		final ITimeWindow window;

		boolean isOpenEnded = false;
		if (from == null && to != null) {
			// Would expect this to be zero!
			final int inclusiveStart = Math.max(0, dateHelper.convertTime(dateHelper.getEarliestTime()));
			final int inclusiveEnd = Math.max(inclusiveStart, dateHelper.convertTime(to));

			window = TimeWindowMaker.createInclusiveInclusive(inclusiveStart, inclusiveEnd, 0, false);
		} else if (from != null && to == null) {
			// Set a default window end date which is valid change later
			final int inclusiveStart = Math.max(0, dateHelper.convertTime(from));

			window = TimeWindowMaker.createInclusiveExclusive(inclusiveStart, Integer.MAX_VALUE, 0, true);
			builder.addPartiallyOpenEndWindow((MutableTimeWindow) window);
		} else if (from != null && to != null) {
			final int inclusiveStart = Math.max(0, dateHelper.convertTime(from));
			final int inclusiveEnd = Math.max(inclusiveStart, dateHelper.convertTime(to));

			window = TimeWindowMaker.createInclusiveInclusive(inclusiveStart, inclusiveEnd, 0, false);
		} else {
			// No window
			isOpenEnded = true;
			window = new MutableTimeWindow(0, Integer.MAX_VALUE);
		}

		final Set<IPort> portSet = new LinkedHashSet<>();
		for (final Port p : ports) {
			portSet.add(portAssociation.lookup(p));
		}

		if (ports.isEmpty()) {
			return builder.createEndRequirement(null, !isOpenEnded, window, heelConsumer);
		} else {
			return builder.createEndRequirement(portSet, !isOpenEnded, window, heelConsumer);
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
			LOG.warn("Spot slot with an invalid market found");
			return;
		}

		final TreeMap<String, Collection<Slot<?>>> curve;
		if (existingSpotCount.containsKey(market)) {
			curve = existingSpotCount.get(market);
		} else {
			curve = new TreeMap<>();
			existingSpotCount.put(market, curve);
		}
		if (spotSlot instanceof Slot) {
			final Slot<?> slot = (Slot<?>) spotSlot;
			final String key = SpotSlotUtils.getKeyForDate(slot.getWindowStart());
			final Collection<Slot<?>> slots;
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
	private Collection<@NonNull Slot<?>> getSpotSlots(@NonNull final SpotMarket spotMarket, @NonNull final String key) {

		if (existingSpotCount.containsKey(spotMarket)) {
			final TreeMap<String, Collection<@NonNull Slot<?>>> curve = existingSpotCount.get(spotMarket);
			if (curve.containsKey(key)) {
				final Collection<Slot<?>> slots = curve.get(key);
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

	private void setMiscCosts(final Slot slot, final IPortSlot portSlot) {
		final long miscCosts = OptimiserUnitConvertor.convertToInternalFixedCost(slot.getMiscCosts()) * -1L; // make negative as cost
		if (miscCosts != 0) {
			miscCostsProviderEditor.setCostsValue(portSlot, miscCosts);
		}
	}

	/**
	 * Given the number spot slots available to create, optionally limit this
	 * number.
	 * 
	 * @param count
	 * @return
	 */
	private int getCappedRemainingSpotOptions(final int count) {
		if (spotSlotCreationCap < 0) {
			return count;
		}
		return Math.min(count, spotSlotCreationCap);
	}

	private @NonNull IHeelOptionConsumer createHeelConsumer(@NonNull final EndHeelOptions heelOptions) {
		final long minimumEndHeelInM3 = OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getMinimumEndHeel());
		// Zero can mean unbounded if we do not need to arrive warm.
		final long maximumEndHeelInM3 = (heelOptions.getTankState() != EVesselTankState.MUST_BE_WARM && heelOptions.getMaximumEndHeel() == 0) ? Long.MAX_VALUE
				: OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getMaximumEndHeel());
		final IHeelPriceCalculator heelPriceCalculator;
		final boolean useLastPrice;
		if (heelOptions.isUseLastHeelPrice()) {
			useLastPrice = true;
			heelPriceCalculator = new IHeelPriceCalculator() {

				@Override
				public int getHeelPrice(final long heelVolume, final int localTime, @NonNull final IPort port) {
					// Should be using last heel price, not computing a new one
					throw new IllegalStateException();
				}

				@Override
				public int getHeelPrice(final long heelVolumeInM3, final int utcTime) {
					// Should be using last heel price, not computing a new one
					throw new IllegalStateException();
				}
			};
		} else {
			useLastPrice = false;

			final String expression = heelOptions.getPriceExpression();
			if (expression == null || expression.isEmpty()) {
				heelPriceCalculator = ConstantHeelPriceCalculator.ZERO;
			} else {
				final ICurve expressionCurve = dateHelper.generateExpressionCurve(commodityIndices.asSeries(expression));
				heelPriceCalculator = new ExpressionHeelPriceCalculator(expression, expressionCurve);
				injector.injectMembers(heelPriceCalculator);
			}
		}
		final VesselTankState vesselTankState;
		switch (heelOptions.getTankState()) {
		case EITHER:
			vesselTankState = VesselTankState.EITHER;
			break;
		case MUST_BE_COLD:
			vesselTankState = VesselTankState.MUST_BE_COLD;
			break;
		case MUST_BE_WARM:
			vesselTankState = VesselTankState.MUST_BE_WARM;
			break;
		default:
			throw new IllegalArgumentException();
		}
		return builder.createHeelConsumer(minimumEndHeelInM3, maximumEndHeelInM3, vesselTankState, heelPriceCalculator, useLastPrice);
	}

	private @NonNull IHeelOptionSupplier createHeelSupplier(@NonNull final StartHeelOptions heelOptions, final Integer startCV) {
		final long minimumHeelInM3 = OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getMinVolumeAvailable());
		final long maximumHeelInM3 = OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getMaxVolumeAvailable());
		final int cargoCV = startCV != null ? startCV : OptimiserUnitConvertor.convertToInternalConversionFactor(heelOptions.getCvValue());
		final IHeelPriceCalculator heelPriceCalculator;
		final String expression = heelOptions.getPriceExpression();
		if (expression == null || expression.isEmpty()) {
			heelPriceCalculator = ConstantHeelPriceCalculator.ZERO;
		} else {
			final ICurve expressionCurve = dateHelper.generateExpressionCurve(commodityIndices.asSeries(expression));
			heelPriceCalculator = new ExpressionHeelPriceCalculator(expression, expressionCurve);
			injector.injectMembers(heelPriceCalculator);
		}

		return builder.createHeelSupplier(minimumHeelInM3, maximumHeelInM3, cargoCV, heelPriceCalculator);
	}

	private void buildRouteEntryPoints(final PortModel portModel, final Association<Port, IPort> portAssociation) {
		portModel.getRoutes().forEach(r -> {
			if (r.getNorthEntrance() != null && r.getSouthEntrance() != null) {
				final Port northPort = r.getNorthEntrance().getPort();
				final Port southPort = r.getSouthEntrance().getPort();
				if (northPort != null && southPort != null) {
					distanceProviderEditor.setEntryPointsForRouteOption(mapRouteOption(r), portAssociation.lookupNullChecked(northPort), portAssociation.lookupNullChecked(southPort));
					distanceProviderEditor.setCanalDistance(mapRouteOption(r), (int) r.getDistance());
				}
			}
		});
	}

	private void buildCanalDistances(final PortModel portModel) {
		portModel.getRoutes().forEach(r -> {
			distanceProviderEditor.setCanalDistance(mapRouteOption(r), (int) r.getDistance());
		});
	}

}
