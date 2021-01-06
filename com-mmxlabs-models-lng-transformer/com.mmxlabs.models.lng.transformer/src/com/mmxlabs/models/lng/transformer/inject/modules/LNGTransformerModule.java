/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.time.ZoneId;
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
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.util.DefaultExposuresCustomiser;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
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
import com.mmxlabs.scheduler.optimiser.cache.NullCacheKeyDependencyLinker;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleFOBShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.impl.DefaultDivertibleDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.impl.DefaultDivertibleFOBShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.CharterRateToCharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VoyagePlanStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.curves.CachingPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.IBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.InPortBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.fitness.impl.DefaultEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.providers.IExternalDateProvider;
import com.mmxlabs.scheduler.optimiser.providers.IInternalDateProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.CachingTimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.ITimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.FeasibleTimeWindowTrimmer;
import com.mmxlabs.scheduler.optimiser.scheduling.PortTimesRecordMaker;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.util.SchedulerCalculationUtils;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses
 * injection to populate the data structures.
 * 
 */
public class LNGTransformerModule extends AbstractModule {

	public static final String EARLIEST_AND_LATEST_TIMES = "earliest-and-latest-times";

	private final @NonNull LNGScenarioModel scenario;

	public static final String MONTH_ALIGNED_INTEGER_INTERVAL_CURVE = "MonthAlignedIntegerCurve";

	private final boolean shippingOnly;

	private final boolean withSpotCargoMarkets;

	private final boolean hintEnableCache;
	private final boolean portfolioBreakEven;

	private final boolean withNoNominalsInPrompt;
	private final boolean withCharterLength;

	// auto-hedging mode. If true - use flat curve
	private final boolean withFlatCurve;

	private final @NonNull UserSettings userSettings;

	private final @NonNull IScenarioDataProvider scenarioDataProvider;

	private final int concurrencyLevel;

	@NonNull
	private final Collection<String> hints;

	/**
	 * @param concurrencyLevel
	 */
	public LNGTransformerModule(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final UserSettings userSettings, final int concurrencyLevel,
			@NonNull final Collection<@NonNull String> hints) {
		this.scenarioDataProvider = scenarioDataProvider;
		this.concurrencyLevel = concurrencyLevel;
		this.hints = hints;
		this.scenario = (LNGScenarioModel) scenarioDataProvider.getScenario();
		this.userSettings = userSettings;
		this.shippingOnly = hints.contains(LNGTransformerHelper.HINT_SHIPPING_ONLY);
		this.hintEnableCache = !hints.contains(LNGTransformerHelper.HINT_DISABLE_CACHES);
		this.portfolioBreakEven = hints.contains(LNGTransformerHelper.HINT_PORTFOLIO_BREAKEVEN);
		this.withSpotCargoMarkets = hints.contains(LNGTransformerHelper.HINT_SPOT_CARGO_MARKETS);
		this.withNoNominalsInPrompt = hints.contains(LNGTransformerHelper.HINT_NO_NOMINALS_IN_PROMPT);
		this.withCharterLength = hints.contains(LNGTransformerHelper.HINT_CHARTER_LENGTH);
		this.withFlatCurve = hints.contains(LNGTransformerHelper.HINT_GENERATED_PAPERS_PNL);
		assert scenario != null;
		// Temporary check to make sure the UI is correctly configured.
		assert scenario.isLongTerm() == (userSettings.getMode() == OptimisationMode.LONG_TERM);
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());

		bind(UserSettings.class).toInstance(userSettings);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.SCENARIO_TYPE_ADP)).toInstance(userSettings.getMode() == OptimisationMode.ADP);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.SCENARIO_TYPE_LONG_TERM)).toInstance(userSettings.getMode() == OptimisationMode.LONG_TERM);

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
		bind(IInternalDateProvider.class).to(DateAndCurveHelper.class);

		bind(DefaultModelEntityMap.class).in(Singleton.class);
		bind(ModelEntityMap.class).to(DefaultModelEntityMap.class);
		bind(IExternalDateProvider.class).to(DefaultModelEntityMap.class);

		bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
		bind(LNGVoyageCalculator.class).in(Singleton.class);

		bind(VoyagePlanStartDateCharterRateCalculator.class).in(Singleton.class);
		bind(ICharterRateCalculator.class).to(VoyagePlanStartDateCharterRateCalculator.class);

		bind(ICharterCostCalculator.class).to(CharterRateToCharterCostCalculator.class);

		// <--- Time windows

		bind(SchedulerCalculationUtils.class);

		bind(FeasibleTimeWindowTrimmer.class);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)).toInstance(Boolean.TRUE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePNLBasedWindowTrimming)).toInstance(Boolean.TRUE);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_SchedulePurges)).toInstance(Boolean.FALSE);

		bind(PortTimesRecordMaker.class);
		bind(PriceIntervalProviderHelper.class);
		bind(PriceIntervalProducer.class);
		// --->
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);
		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);

		bind(DefaultDivertibleDESShippingTimesCalculator.class).in(Singleton.class);
		bind(IDivertibleDESShippingTimesCalculator.class).to(DefaultDivertibleDESShippingTimesCalculator.class);

		bind(DefaultDivertibleFOBShippingTimesCalculator.class).in(Singleton.class);
		bind(IDivertibleFOBShippingTimesCalculator.class).to(DefaultDivertibleFOBShippingTimesCalculator.class);

		// <------ Exposures and papers

		bind(IExposuresCustomiser.class).to(DefaultExposuresCustomiser.class);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.OPTIMISE_PAPER_PNL)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.COMPUTE_EXPOSURES)).toInstance(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES));
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.COMPUTE_PAPER_PNL)).toInstance(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS));
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.RE_HEDGE_WITH_PAPERS)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)).toInstance(withFlatCurve);

		// ----->

		// Register default implementations
		bind(IProfitAndLossCacheKeyDependencyLinker.class).to(NullCacheKeyDependencyLinker.class);

		bind(boolean.class).annotatedWith(Names.named(IEndEventScheduler.ENABLE_HIRE_COST_ONLY_END_RULE)).toInstance(Boolean.TRUE);
		bind(IEndEventScheduler.class).to(DefaultEndEventScheduler.class);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.COMMERCIAL_VOLUME_OVERCAPACITY)).toInstance(Boolean.FALSE);
		bind(IVolumeAllocator.class).to(MinMaxUnconstrainedVolumeAllocator.class);

		bind(IEntityValueCalculator.class).to(DefaultEntityValueCalculator.class);

		// Default bindings for caches
		final CacheMode mode = hints.contains(LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS) ? CacheMode.Off : CacheMode.On;

		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_ArrivalTimeCache)).toInstance(mode);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_PNLTrimmerCache)).toInstance(mode);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VoyagePlanEvaluatorCache)).toInstance(mode);

		// Default two weeks idle time.
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CHARTER_LENGTH) && withCharterLength) {
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)).toInstance(14 * 24);
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.COOLDOWN_MIN_IDLE_HOURS)).toInstance(14 * 24);
		} else {
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);
			bind(int.class).annotatedWith(Names.named(SchedulerConstants.COOLDOWN_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);
		}
	}

	@Provides
	private IBoilOffHelper provideInPortBoilOffHelper(@NonNull final Injector injector, @Named(SchedulerConstants.COMMERCIAL_VOLUME_OVERCAPACITY) final boolean toggle) {
		return new InPortBoilOffHelper(toggle);
	}

	@Provides
	private IVoyagePlanOptimiser provideVoyagePlanOptimiser(final @NonNull VoyagePlanOptimiser delegate) {
		return delegate;
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
		return new CachingTimeWindowSchedulingCanalDistanceProvider(delegate);
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
			public int mapMonthToChangePoint(final int months) {

				// Convert internal time units back into UTC date/time
				@NonNull
				final ZonedDateTime dateTimeZero = map.getDateFromHours(0, "Etc/UTC");

				final ZonedDateTime startOfYear = dateTimeZero.withDayOfYear(1).withHour(0);
				@NonNull
				final ZonedDateTime plusMonths = startOfYear.plusMonths(months);

				return Hours.between(dateTimeZero, plusMonths);
			}

			@Override
			public int mapChangePointToMonth(final int hoursFromEarliestDateTime) {
				// Convert internal time units back into UTC date/time
				@NonNull
				final ZonedDateTime dateTimeZero = map.getDateFromHours(0, "Etc/UTC");

				final ZonedDateTime startOfYear = dateTimeZero.withDayOfYear(1).withHour(0);
				@NonNull
				final ZonedDateTime plusMonths = dateTimeZero.plusHours(hoursFromEarliestDateTime).withDayOfMonth(1);

				final int m = Months.between(startOfYear, plusMonths);

				return m;
			}
		};
	}

	@Provides
	@Singleton
	private SeriesParserData provideSeriesParserData(final ShiftFunctionMapper shiftMapper, final CalendarMonthMapper monthMapper,
			@Named(EARLIEST_AND_LATEST_TIMES) final Pair<ZonedDateTime, ZonedDateTime> dates) {
		final SeriesParserData data = new SeriesParserData();
		data.setShiftMapper(shiftMapper);
		data.setCalendarMonthMapper(monthMapper);
		data.earliestAndLatestTime = dates;
		return data;
	}

	@Provides
	@Named(SchedulerConstants.Parser_Commodity)
	@Singleton
	private SeriesParser provideCommodityParser(final SeriesParserData seriesParserData) {
		return new SeriesParser(seriesParserData);
	}

	@Provides
	@Named(SchedulerConstants.Parser_Charter)
	@Singleton
	private SeriesParser provideCharterParser(final SeriesParserData seriesParserData) {
		return new SeriesParser(seriesParserData);
	}

	@Provides
	@Named(SchedulerConstants.Parser_BaseFuel)
	@Singleton
	private SeriesParser provideBaseFuelParser(final SeriesParserData seriesParserData) {
		return new SeriesParser(seriesParserData);
	}

	@Provides
	@Named(SchedulerConstants.Parser_Currency)
	@Singleton
	private SeriesParser provideCurrencyParser(final SeriesParserData seriesParserData) {
		return new SeriesParser(seriesParserData);
	}

	@Provides
	@Singleton
	@Named(SchedulerConstants.MIDNIGHT_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve provideDailyMidnightIIntegerIntervalCurve(@NonNull final DateAndCurveHelper dateAndCurveHelper) {
		final IntegerIntervalCurve midnightsInUTC = new IntegerIntervalCurve();

		final ZonedDateTime zdt = dateAndCurveHelper.getEarliestTime();
		// Make sure we get midnight
		final int hourAtZDT = dateAndCurveHelper.convertTime(zdt.withZoneSameInstant(ZoneId.of("UTC")).withHour(0));
		final int latestTime = dateAndCurveHelper.convertTime(dateAndCurveHelper.getLatestTime());

		// May be less than zero if time zero is later than midnight
		assert (hourAtZDT <= 0);
		// Add 23 hours to ensure the end time is still covered
		final int upperBound = latestTime + 23;

		// As this is UTC, it is safe to always add 24hrs
		for (int i = hourAtZDT; i <= upperBound; i += 24) {
			midnightsInUTC.add(i);
		}

		return midnightsInUTC;
	}
}
