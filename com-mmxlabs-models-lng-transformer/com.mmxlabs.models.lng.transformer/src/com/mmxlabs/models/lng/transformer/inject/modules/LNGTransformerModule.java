/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.ShiftFunctionMapper;
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
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.IProfitAndLossCacheKeyDependencyLinker;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.cache.NullCacheKeyDependencyLinker;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.impl.DefaultDivertableDESShippingTimesCalculator;
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
import com.mmxlabs.scheduler.optimiser.fitness.impl.DefaultEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.CachingTimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.ITimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 */
public class LNGTransformerModule extends AbstractModule {
	
	public static final String COMMERCIAL_VOLUME_OVERCAPACITY = "COMMERCIAL_VOLUME_OVERCAPACITY";
	
	private final static int DEFAULT_VPO_CACHE_SIZE = 20_000;

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

	private final LNGScenarioModel scenario;

	public final static String MONTH_ALIGNED_INTEGER_INTERVAL_CURVE = "MonthAlignedIntegerCurve";

	private final boolean shippingOnly;

	private final boolean hintEnableCache;

	/**
	 */
	public LNGTransformerModule(@NonNull final LNGScenarioModel scenario, @NonNull final Collection<@NonNull String> hints) {
		this.scenario = scenario;
		this.shippingOnly = hints.contains(LNGTransformerHelper.HINT_SHIPPING_ONLY);
		this.hintEnableCache = !hints.contains(LNGTransformerHelper.HINT_DISABLE_CACHES);
		assert scenario != null;
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());

		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_SHIPPING_ONLY)).toInstance(shippingOnly);
		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_DISABLE_CACHES)).toInstance(hintEnableCache);
		bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_PORTFOLIO_BREAKEVEN)).toInstance(!hintEnableCache);

		bind(LNGScenarioModel.class).toInstance(scenario);
		// bind(OptimiserSettings.class).toInstance(optimiserSettings);

		bind(LNGScenarioTransformer.class).in(Singleton.class);

		bind(DateAndCurveHelper.class).in(Singleton.class);

		bind(ModelEntityMap.class).to(DefaultModelEntityMap.class).in(Singleton.class);

		bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
		bind(LNGVoyageCalculator.class).in(Singleton.class);

		bind(VoyagePlanStartDateCharterRateCalculator.class).in(Singleton.class);
		bind(ICharterRateCalculator.class).to(VoyagePlanStartDateCharterRateCalculator.class);

		bind(PriceIntervalProviderHelper.class);
		bind(PriceIntervalProducer.class);

		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);

		bind(IDivertableDESShippingTimesCalculator.class).to(DefaultDivertableDESShippingTimesCalculator.class);
		bind(DefaultDivertableDESShippingTimesCalculator.class).in(Singleton.class);

		// Register default implementations
		bind(IProfitAndLossCacheKeyDependencyLinker.class).to(NullCacheKeyDependencyLinker.class);

		bind(IEndEventScheduler.class).to(DefaultEndEventScheduler.class);

		bind(IVolumeAllocator.class).annotatedWith(NotCaching.class).to(UnconstrainedVolumeAllocator.class);
		bind(IEntityValueCalculator.class).annotatedWith(NotCaching.class).to(DefaultEntityValueCalculator.class);

		// Default bindings for caches
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocationCache)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocatedSequenceCache)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_ProfitandLossCache)).toInstance(Boolean.FALSE);

	}
	
	@Provides
	@Named(COMMERCIAL_VOLUME_OVERCAPACITY)
	private boolean commercialVolumeOverCapacity() {
		return false;
	}
	
	@Provides
	private IBoilOffHelper provideInPortBoilOffHelper(@NonNull final Injector injector, @Named(COMMERCIAL_VOLUME_OVERCAPACITY) final boolean toggle){
		final InPortBoilOffHelper helper = new InPortBoilOffHelper(toggle);
//		injector.injectMembers(helper);
		
		return helper;
	}

	@Provides
	@PerChainUnitScope
	private IVolumeAllocator provideVolumeAllocator(@NonNull final Injector injector, final @NotCaching IVolumeAllocator reference,
			@Named(SchedulerConstants.Key_VolumeAllocationCache) final boolean enableCache) {
		if (enableCache && hintEnableCache) {
			final CachingVolumeAllocator cacher = new CachingVolumeAllocator(reference);
			injector.injectMembers(cacher);
			if (false) {
				return new CheckingVolumeAllocator(reference, cacher);
			}
			return cacher;
		} else {
			return reference;
		}

	}

	@Provides
	@PerChainUnitScope
	private IEntityValueCalculator provideEntityValueCalculator(final @NonNull Injector injector, final @NotCaching IEntityValueCalculator reference,
			@Named(SchedulerConstants.Key_ProfitandLossCache) final boolean enableCache) {

		if (enableCache && hintEnableCache) {
			final CachingEntityValueCalculator cacher = new CachingEntityValueCalculator(reference);
			injector.injectMembers(cacher);
			if (false) {
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
		final IOptimisationData optimisationData = lngScenarioTransformer.createOptimisationData(modelEntityMap);

		return optimisationData;
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

		final IIntegerIntervalCurve months = integerIntervalCurveHelper.getMonthAlignedIntegerIntervalCurve(
				integerIntervalCurveHelper.getPreviousMonth(dateAndCurveHelper.convertTime(dateAndCurveHelper.getEarliestTime())),
				integerIntervalCurveHelper.getNextMonth(dateAndCurveHelper.convertTime(dateAndCurveHelper.getLatestTime())), 0);

		return months;
	}

	@Provides
	@PerChainUnitScope
	private IVoyagePlanOptimiser provideVoyagePlanOptimiser(final VoyagePlanOptimiser delegate) {
		final CachingVoyagePlanOptimiser cachingVoyagePlanOptimiser = new CachingVoyagePlanOptimiser(delegate, DEFAULT_VPO_CACHE_SIZE);
		return cachingVoyagePlanOptimiser;
	}

	@Provides
	@Named(VoyagePlanOptimiser.VPO_SPEED_STEPPING)
	private boolean isVPOSpeedStepping() {
		return true;
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
	@Named(Parser_Commodity)
	@Singleton
	private SeriesParser provideCommodityParser(final ShiftFunctionMapper shiftMapper) {
		final SeriesParser parser = new SeriesParser();
		parser.setShiftMapper(shiftMapper);
		return parser;
	}

	@Provides
	@Named(Parser_Charter)
	@Singleton
	private SeriesParser provideCharterParser(final ShiftFunctionMapper shiftMapper) {
		final SeriesParser parser = new SeriesParser();
		parser.setShiftMapper(shiftMapper);
		return parser;
	}

	@Provides
	@Named(Parser_BaseFuel)
	@Singleton
	private SeriesParser provideBaseFuelParser(final ShiftFunctionMapper shiftMapper) {
		final SeriesParser parser = new SeriesParser();
		parser.setShiftMapper(shiftMapper);
		return parser;
	}

	@Provides
	@Named(Parser_Currency)
	@Singleton
	private SeriesParser provideCurrencyParser(final ShiftFunctionMapper shiftMapper) {
		final SeriesParser parser = new SeriesParser();
		parser.setShiftMapper(shiftMapper);
		return parser;
	}
}
