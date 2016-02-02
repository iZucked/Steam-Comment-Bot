/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.time.ZonedDateTime;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.DefaultModelEntityMap;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.IntegerIntervalCurveHelper;
import com.mmxlabs.models.lng.transformer.util.LNGScenarioUtils;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.impl.DefaultDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.GeneratedVesselEventFactory;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VoyagePlanStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.curves.CachingPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.CachingTimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.ITimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;


/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 */
public class LNGTransformerModule extends AbstractModule {

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

	private final LNGScenarioModel scenario;

	private final OptimiserSettings optimiserSettings;

	public final static String MONTH_ALIGNED_INTEGER_INTERVAL_CURVE = "MonthAlignedIntegerCurve";
	
	/**
	 */
	public LNGTransformerModule(@NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings) {
		this.scenario = scenario;
		this.optimiserSettings = optimiserSettings;
		assert scenario != null;
		assert optimiserSettings != null;
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());

		bind(LNGScenarioModel.class).toInstance(scenario);
		// bind(OptimiserSettings.class).toInstance(optimiserSettings);

		// Parser for each section
		final SeriesParser commodityParser = new SeriesParser();
		bind(SeriesParser.class).annotatedWith(Names.named(Parser_Commodity)).toInstance(commodityParser);
		final SeriesParser charterParser = new SeriesParser();
		bind(SeriesParser.class).annotatedWith(Names.named(Parser_Charter)).toInstance(charterParser);
		final SeriesParser baseFuelParser = new SeriesParser();
		bind(SeriesParser.class).annotatedWith(Names.named(Parser_BaseFuel)).toInstance(baseFuelParser);

		bind(LNGScenarioTransformer.class).in(Singleton.class);

		bind(DateAndCurveHelper.class).in(Singleton.class);

		bind(ModelEntityMap.class).to(DefaultModelEntityMap.class).in(Singleton.class);

		bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
		bind(LNGVoyageCalculator.class).in(Singleton.class);

		bind(VoyagePlanStartDateCharterRateCalculator.class).in(Singleton.class);
		bind(ICharterRateCalculator.class).to(VoyagePlanStartDateCharterRateCalculator.class);

		bind(PriceIntervalProducer.class);

		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);

		bind(IDivertableDESShippingTimesCalculator.class).to(DefaultDivertableDESShippingTimesCalculator.class);
		bind(DefaultDivertableDESShippingTimesCalculator.class).in(Singleton.class);

		// Register default implementations
		bind(IVolumeAllocator.class).to(UnconstrainedVolumeAllocator.class).in(Singleton.class);
		bind(IEntityValueCalculator.class).to(DefaultEntityValueCalculator.class);

		bind(GeneratedVesselEventFactory.class).in(PerChainUnitScope.class);
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
	@Singleton
	IPriceIntervalProducer providePriceIntervalProducer(final PriceIntervalProducer delegate, @NonNull Injector injector) {
		final CachingPriceIntervalProducer cachingPriceIntervalProducer = new CachingPriceIntervalProducer(delegate);
		injector.injectMembers(cachingPriceIntervalProducer);
		return cachingPriceIntervalProducer;
	}
	
	@Provides
	@Singleton
	ITimeWindowSchedulingCanalDistanceProvider provideTimeWindowSchedulingCanalDistanceProvider(@NonNull Injector injector) {
		TimeWindowSchedulingCanalDistanceProvider delegate = new TimeWindowSchedulingCanalDistanceProvider();
		injector.injectMembers(delegate);
		final CachingTimeWindowSchedulingCanalDistanceProvider cachingTimeWindowSchedulingCanalDistanceProvider = new CachingTimeWindowSchedulingCanalDistanceProvider(delegate);
		return cachingTimeWindowSchedulingCanalDistanceProvider;
	}

	@Provides
	@Singleton
	@Named(MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve provideMonthAlignedIIntegerIntervalCurve(@NonNull DateAndCurveHelper dateAndCurveHelper, @NonNull IntegerIntervalCurveHelper integerIntervalCurveHelper) {

		IIntegerIntervalCurve months = integerIntervalCurveHelper.getMonthAlignedIntegerIntervalCurve(
				integerIntervalCurveHelper.getPreviousMonth(dateAndCurveHelper.convertTime(dateAndCurveHelper.getEarliestTime())),
				integerIntervalCurveHelper.getNextMonth(dateAndCurveHelper.convertTime(dateAndCurveHelper.getLatestTime())),
				0);

		return months;
	}


}
