/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesParserData;
import com.mmxlabs.common.parser.series.ShiftFunctionMapper;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.DefaultModelEntityMap;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.IntegerIntervalCurveHelper;
import com.mmxlabs.models.lng.transformer.util.LNGScenarioUtils;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.cache.IProfitAndLossCacheKeyDependencyLinker;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.cache.NullCacheKeyDependencyLinker;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleFOBShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.impl.DefaultDivertibleDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.impl.DefaultDivertibleFOBShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VoyagePlanStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.curves.CachingPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.CachingEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.CheckingEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.CachingVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.CheckingVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.IBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.InPortBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CachingVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CheckingVPO;
import com.mmxlabs.scheduler.optimiser.fitness.impl.DefaultEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.CachingTimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.ITimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.ArrivalTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.EarliestSlotTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.FeasibleTimeWindowTrimmer;
import com.mmxlabs.scheduler.optimiser.scheduling.IArrivalTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.ISlotTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.PortTimesRecordMaker;
import com.mmxlabs.scheduler.optimiser.scheduling.PriceBasedWindowTrimmer;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 */
public class LNGTransformerModule extends AbstractModule {

	public static final String COMMERCIAL_VOLUME_OVERCAPACITY = "COMMERCIAL_VOLUME_OVERCAPACITY";

	private static final int DEFAULT_VPO_CACHE_SIZE = 20_000;

	public static final String EARLIEST_AND_LATEST_TIMES = "earliest-and-latest-times";

	/**
	 */
	public static final String Parser_Commodity = "Commodity";
	/**
	 */
	public static final String Parser_BaseFuel = "BaseFuel";
	/**
	 */
	public static final String Parser_Charter = "Charter";
	/**
	 */
	public static final String Parser_Currency = "Currency";

	private final @NonNull LNGScenarioModel scenario;

	public static final String MONTH_ALIGNED_INTEGER_INTERVAL_CURVE = "MonthAlignedIntegerCurve";

	private final boolean shippingOnly;

	private final boolean withSpotCargoMarkets;

	private final boolean hintEnableCache;
	private final boolean portfolioBreakEven;

	private final boolean withNoNominalsInPrompt;
	private final boolean withCharterLength;

	private @NonNull UserSettings userSettings;

	private @NonNull IScenarioDataProvider scenarioDataProvider;

	private int concurrencyLevel;

	/**
	 * @param concurrencyLevel
	 */
	public LNGTransformerModule(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull UserSettings userSettings, int concurrencyLevel, @NonNull final Collection<@NonNull String> hints) {
		this.scenarioDataProvider = scenarioDataProvider;
		this.concurrencyLevel = concurrencyLevel;
		this.scenario = (LNGScenarioModel) scenarioDataProvider.getScenario();
		this.userSettings = userSettings;
		this.shippingOnly = hints.contains(LNGTransformerHelper.HINT_SHIPPING_ONLY);
		this.hintEnableCache = !hints.contains(LNGTransformerHelper.HINT_DISABLE_CACHES);
		this.portfolioBreakEven = hints.contains(LNGTransformerHelper.HINT_PORTFOLIO_BREAKEVEN);
		this.withSpotCargoMarkets = hints.contains(LNGTransformerHelper.HINT_SPOT_CARGO_MARKETS);
		this.withNoNominalsInPrompt = hints.contains(LNGTransformerHelper.HINT_NO_NOMINALS_IN_PROMPT);
		this.withCharterLength = hints.contains(LNGTransformerHelper.HINT_CHARTER_LENGTH);
		assert scenario != null;
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());

		bind(UserSettings.class).toInstance(userSettings);

		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_SHIPPING_ONLY)).toInstance(shippingOnly);
		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_DISABLE_CACHES)).toInstance(hintEnableCache);
		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_PORTFOLIO_BREAKEVEN)).toInstance(portfolioBreakEven);
		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_SPOT_CARGO_MARKETS)).toInstance(withSpotCargoMarkets);
		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_NO_NOMINALS_IN_PROMPT)).toInstance(withNoNominalsInPrompt);
		bind(int.class).annotatedWith(Names.named(SchedulerConstants.CONCURRENCY_LEVEL)).toInstance(concurrencyLevel);

		// Default binding - no limit
		bind(int.class).annotatedWith(Names.named(LNGScenarioTransformer.LIMIT_SPOT_SLOT_CREATION)).toInstance(-1);

		bind(long.class).annotatedWith(Names.named(SchedulerConstants.KEY_DEFAULT_MAX_VOLUME_IN_M3)).toInstance(OptimiserUnitConvertor.convertToInternalVolume(140_000));

		bind(LNGScenarioModel.class).toInstance(scenario);
		bind(IScenarioDataProvider.class).toInstance(scenarioDataProvider);

		bind(LNGScenarioTransformer.class).in(Singleton.class);

		bind(DateAndCurveHelper.class).in(Singleton.class);

		bind(ModelEntityMap.class).to(DefaultModelEntityMap.class).in(Singleton.class);

		bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
		bind(LNGVoyageCalculator.class).in(Singleton.class);

		bind(VoyagePlanStartDateCharterRateCalculator.class).in(Singleton.class);
		bind(ICharterRateCalculator.class).to(VoyagePlanStartDateCharterRateCalculator.class);

		// <--- Time windows

		bind(SchedulerCalculationUtils.class);

		bind(FeasibleTimeWindowTrimmer.class);
		bind(PriceBasedWindowTrimmer.class);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)).toInstance(Boolean.TRUE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)).toInstance(Boolean.FALSE);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_SchedulePurges)).toInstance(Boolean.FALSE);

		bind(PriceIntervalProviderHelper.class);
		bind(PriceIntervalProducer.class);

		bind(IArrivalTimeScheduler.class).to(ArrivalTimeScheduler.class);
		bind(TimeWindowScheduler.class);
		bind(ISlotTimeScheduler.class).to(EarliestSlotTimeScheduler.class);

		bind(PortTimesRecordMaker.class);

		// --->

		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);

		bind(IDivertibleDESShippingTimesCalculator.class).to(DefaultDivertibleDESShippingTimesCalculator.class);
		bind(DefaultDivertibleDESShippingTimesCalculator.class).in(Singleton.class);

		bind(IDivertibleFOBShippingTimesCalculator.class).to(DefaultDivertibleFOBShippingTimesCalculator.class);
		bind(DefaultDivertibleFOBShippingTimesCalculator.class).in(Singleton.class);

		// Register default implementations
		bind(IProfitAndLossCacheKeyDependencyLinker.class).to(NullCacheKeyDependencyLinker.class);

		bind(boolean.class).annotatedWith(Names.named(IEndEventScheduler.ENABLE_HIRE_COST_ONLY_END_RULE)).toInstance(Boolean.TRUE);
		bind(IEndEventScheduler.class).to(DefaultEndEventScheduler.class);

		bind(IVolumeAllocator.class).annotatedWith(NotCaching.class).to(UnconstrainedVolumeAllocator.class);
		bind(IEntityValueCalculator.class).annotatedWith(NotCaching.class).to(DefaultEntityValueCalculator.class);

		// Default bindings for caches
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_ArrivalTimeCache)).toInstance(CacheMode.On);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VoyagePlanOptimiserCache)).toInstance(CacheMode.On);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocationCache)).toInstance(CacheMode.Off);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocatedSequenceCache)).toInstance(CacheMode.Off);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_ProfitandLossCache)).toInstance(CacheMode.Off);

		// Default two weeks idle time.
		if (LicenseFeatures.isPermitted("features:charter-length") && withCharterLength) {
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)).toInstance(14 * 24);
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.COOLDOWN_MIN_IDLE_HOURS)).toInstance(14 * 24);
		} else {
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.COOLDOWN_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);
		}
	}

	@Provides
	@Named(COMMERCIAL_VOLUME_OVERCAPACITY)
	private boolean commercialVolumeOverCapacity() {
		return false;
	}

	@Provides
	private IBoilOffHelper provideInPortBoilOffHelper(@NonNull final Injector injector, @Named(COMMERCIAL_VOLUME_OVERCAPACITY) final boolean toggle) {
		return new InPortBoilOffHelper(toggle);
	}

	@Provides
	@PerChainUnitScope
	private IVoyagePlanOptimiser provideVoyagePlanOptimiser(Injector injector, final @NonNull VoyagePlanOptimiser delegate,
			@Named(SchedulerConstants.Key_VoyagePlanOptimiserCache) CacheMode cacheMode) {

		if (cacheMode == CacheMode.Off || !hintEnableCache) {
			return delegate;
		} else {
			final CachingVoyagePlanOptimiser cachingVoyagePlanOptimiser = new CachingVoyagePlanOptimiser(delegate, DEFAULT_VPO_CACHE_SIZE);
			injector.injectMembers(cachingVoyagePlanOptimiser);
			if (cacheMode == CacheMode.On) {
				return cachingVoyagePlanOptimiser;
			} else {
				assert cacheMode == CacheMode.Verify;
				return new CheckingVPO(delegate, cachingVoyagePlanOptimiser);
			}
		}
	}

	@Provides
	@PerChainUnitScope
	private IVolumeAllocator provideVolumeAllocator(@NonNull final Injector injector, final @NonNull @NotCaching IVolumeAllocator reference,
			@Named(SchedulerConstants.Key_VolumeAllocationCache) final CacheMode cacheMode) {
		if (cacheMode != CacheMode.Off && hintEnableCache) {
			final CachingVolumeAllocator cacher = new CachingVolumeAllocator(reference);
			injector.injectMembers(cacher);
			if (cacheMode == CacheMode.Verify) {
				return new CheckingVolumeAllocator(reference, cacher);
			}
			return cacher;
		} else {
			return reference;
		}

	}

	@Provides
	@PerChainUnitScope
	private IEntityValueCalculator provideEntityValueCalculator(final @NonNull Injector injector, final @NonNull @NotCaching IEntityValueCalculator reference,
			@Named(SchedulerConstants.Key_ProfitandLossCache) final CacheMode cacheMode) {

		if (cacheMode != CacheMode.Off && hintEnableCache) {
			final CachingEntityValueCalculator cacher = new CachingEntityValueCalculator(reference);
			injector.injectMembers(cacher);
			if (cacheMode == CacheMode.Verify) {
				return new CheckingEntityValueCalculator(reference, cacher);
			} else {
				return cacher;
			}
		} else {
			return reference;
		}

	}

	@Provides
	@Singleton
	private IOptimisationData provideOptimisationData(@NonNull final LNGScenarioTransformer lngScenarioTransformer, @NonNull final ModelEntityMap modelEntityMap) throws IncompleteScenarioException {
		return lngScenarioTransformer.createOptimisationData(modelEntityMap);
	}

	@Singleton
	@Provides
	@Named(EARLIEST_AND_LATEST_TIMES)
	private Pair<ZonedDateTime, ZonedDateTime> provideEarliestAndLatestTime(@NonNull final LNGScenarioModel scenario) {
		return LNGScenarioUtils.findEarliestAndLatestTimes(scenario);
	}

	@Provides
	@PerChainUnitScope
	private IPriceIntervalProducer providePriceIntervalProducer(final PriceIntervalProducer delegate, @NonNull final Injector injector) {
		final CachingPriceIntervalProducer cachingPriceIntervalProducer = new CachingPriceIntervalProducer(delegate);
		injector.injectMembers(cachingPriceIntervalProducer);
		return cachingPriceIntervalProducer;
	}

	@Provides
	@PerChainUnitScope
	private ITimeWindowSchedulingCanalDistanceProvider provideTimeWindowSchedulingCanalDistanceProvider(@NonNull final Injector injector) {
		final TimeWindowSchedulingCanalDistanceProvider delegate = new TimeWindowSchedulingCanalDistanceProvider();
		injector.injectMembers(delegate);
		final CachingTimeWindowSchedulingCanalDistanceProvider cachingTimeWindowSchedulingCanalDistanceProvider = new CachingTimeWindowSchedulingCanalDistanceProvider(delegate);
		return cachingTimeWindowSchedulingCanalDistanceProvider;
	}

	@Provides
	@Singleton
	@Named(MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve provideMonthAlignedIIntegerIntervalCurve(@NonNull final DateAndCurveHelper dateAndCurveHelper, @NonNull final IntegerIntervalCurveHelper integerIntervalCurveHelper) {

		return integerIntervalCurveHelper.getMonthAlignedIntegerIntervalCurve(//
				integerIntervalCurveHelper.getPreviousMonth(dateAndCurveHelper.convertTime(dateAndCurveHelper.getEarliestTime())), //
				integerIntervalCurveHelper.getNextMonth(dateAndCurveHelper.convertTime(dateAndCurveHelper.getLatestTime())), 0);
	}

	@Provides
	private ShiftFunctionMapper getMapperFunction(final ModelEntityMap map, final DateAndCurveHelper helper) {
		return new ShiftFunctionMapper() {

			@Override
			public int mapChangePoint(final int currentChangePoint, final int shiftAmount) {
				// Convert internal time units back into UTC date/time
				@NonNull
				final ZonedDateTime dateFromHours = map.getDateFromHours(currentChangePoint, "Etc/UTC");
				// Shift the time by number of months
				@NonNull
				final ZonedDateTime shiftedTime = dateFromHours.minusMonths(shiftAmount);
				// Convert back to internal time units.
				final int result = helper.convertTime(shiftedTime);
				return result;
			}
		};
	}

	@Provides
	private CalendarMonthMapper getMonthMapperFunction(final ModelEntityMap map, final DateAndCurveHelper helper) {
		return new CalendarMonthMapper() {

			@Override
			public int mapMonthToChangePoint(int months) {

				// Convert internal time units back into UTC date/time
				@NonNull
				final ZonedDateTime dateTimeZero = map.getDateFromHours(0, "Etc/UTC");

				final ZonedDateTime startOfYear = dateTimeZero.withDayOfYear(1).withHour(0);
				@NonNull
				final ZonedDateTime plusMonths = startOfYear.plusMonths(months);

				return Hours.between(dateTimeZero, plusMonths);
			}

			@Override
			public int mapChangePointToMonth(int date) {
				// Convert internal time units back into UTC date/time
				@NonNull
				final ZonedDateTime dateTimeZero = map.getDateFromHours(0, "Etc/UTC");

				final ZonedDateTime startOfYear = dateTimeZero.withDayOfYear(1).withHour(0);
				@NonNull
				final ZonedDateTime plusMonths = dateTimeZero.plusHours(date).withDayOfMonth(1);

				int m = Months.between(startOfYear, plusMonths);

				return m;
			}
		};
	}

	@Provides
	@Singleton
	private SeriesParserData provideSeriesParserData(final ShiftFunctionMapper shiftMapper, CalendarMonthMapper monthMapper,
			@Named(EARLIEST_AND_LATEST_TIMES) Pair<ZonedDateTime, ZonedDateTime> dates) {
		final SeriesParserData data = new SeriesParserData();
		data.setShiftMapper(shiftMapper);
		data.setCalendarMonthMapper(monthMapper);
		data.earliestAndLatestTime = dates;
		return data;
	}

	@Provides
	@Named(Parser_Commodity)
	@Singleton
	private SeriesParser provideCommodityParser(final SeriesParserData seriesParserData) {
		final SeriesParser parser = new SeriesParser(seriesParserData);
		return parser;
	}

	@Provides
	@Named(Parser_Charter)
	@Singleton
	private SeriesParser provideCharterParser(final SeriesParserData seriesParserData) {
		final SeriesParser parser = new SeriesParser(seriesParserData);
		return parser;
	}

	@Provides
	@Named(Parser_BaseFuel)
	@Singleton
	private SeriesParser provideBaseFuelParser(final SeriesParserData seriesParserData) {
		final SeriesParser parser = new SeriesParser(seriesParserData);
		return parser;
	}

	@Provides
	@Named(Parser_Currency)
	@Singleton
	private SeriesParser provideCurrencyParser(final SeriesParserData seriesParserData) {
		final SeriesParser parser = new SeriesParser(seriesParserData);
		return parser;
	}
}
