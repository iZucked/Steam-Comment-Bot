/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.marketability;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityResult;
import com.mmxlabs.models.lng.analytics.MarketabilityResultContainer;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.marketability.MarketabilityUtils;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.MarketabilityCargoSequenceGenerator;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHelper;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class MarketabilitySandboxUnit {

	static class SingleResult {
		int arrivalTime = Integer.MAX_VALUE;
	}

	static class InternalResult {
		int earliestTime = Integer.MAX_VALUE;
		int latestTime = Integer.MIN_VALUE;

		public void merge(@Nullable final InternalResult other) {
			if (other != null) {
				if (other.earliestTime < this.earliestTime) {
					this.earliestTime = other.earliestTime;
				}
				if (other.latestTime > this.latestTime) {
					this.latestTime = other.latestTime;
				}
			}
		}

		public void merge(@Nullable final SingleResult other) {
			if (other != null) {
				if (other.arrivalTime < this.earliestTime) {
					this.earliestTime = other.arrivalTime;
				}
				if (other.arrivalTime > this.latestTime) {
					this.latestTime = other.arrivalTime;
				}
			}
		}
	}

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	private @NonNull JobExecutorFactory jobExecutorFactory;

	@SuppressWarnings("null")
	public MarketabilitySandboxUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final UserSettings userSettings, @NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings,
			@NonNull final JobExecutorFactory jobExecutorFactory, @NonNull final ISequences initialSequences, @NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;
		this.jobExecutorFactory = jobExecutorFactory;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.add(new InitialPhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constainAndFitnessSettings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(boolean.class).annotatedWith(Names.named(MoveHelper.LEGACY_CHECK_RESOURCE)).toInstance(Boolean.FALSE);
				bind(MoveHelper.class).in(Singleton.class);
				bind(IMoveHelper.class).to(MoveHelper.class);
				bind(MarketabilityCargoSequenceGenerator.class);
			}

			@Provides
			@ThreadLocalScope
			private EvaluationHelper provideEvaluationHelper(final Injector injector, @Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences) {
				EvaluationHelper helper = new EvaluationHelper();
				injector.injectMembers(helper);

				final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
				helper.acceptSequences(initialRawSequences, manipulator.createManipulatedSequences(initialRawSequences));

				helper.setFlexibleCapacityViolationCount(Integer.MAX_VALUE);

				return helper;
			}

			@Provides
			@ThreadLocalScope
			private MarketabilitySandboxEvaluator providePerThreadOptimiser(@NonNull final Injector injector) {

				MarketabilitySandboxEvaluator optimiser = new MarketabilitySandboxEvaluator();
				injector.injectMembers(optimiser);
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);
	}

	public synchronized void run(final @NonNull MarketabilityModel model, final @NonNull IMapperClass mapper, final @NonNull Map<ShippingOption, VesselAssignmentType> shippingMap,
			@NonNull final IProgressMonitor monitor, final @NonNull LNGScenarioToOptimiserBridge bridge) {
		monitor.beginTask("Generate solutions", IProgressMonitor.UNKNOWN);

		List<String> hints = new LinkedList<>(dataTransformer.getHints());
		hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
		dataTransformer.getLifecyleManager().startPhase("export", hints);

		final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
			final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
			s.enter();
			return s;
		});
		try (JobExecutor jobExecutor = subExecutorFactory.begin()) {

			final @NonNull ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final List<Future<Runnable>> futures = new LinkedList<>();
			Schedule schedule = createSchedule(model, bridge, mapper);
			createFutureJobs(schedule, model, mapper, shippingMap, monitor, modelEntityMap, futures, jobExecutor);
			// Block until all futures completed
			for (final Future<Runnable> f : futures) {
				if (monitor.isCanceled()) {
					return;
				}
				try {
					final Runnable runnable = f.get();
					if (runnable != null) {
						runnable.run();
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			monitor.done();
		}
	}

	@NonNullByDefault
	private Schedule createSchedule(final MarketabilityModel model, final LNGScenarioToOptimiserBridge bridge, final IMapperClass mapper) {
		final Schedule schedule = bridge.createOptimiserInitialSchedule();
		assert schedule != null;
		for (final MarketabilityRow row : model.getRows()) {
			LoadSlot loadSlot = mapper.getOriginal(row.getBuyOption());
			DischargeSlot dischargeSlot = mapper.getOriginal(row.getSellOption());
			MarketabilityResultContainer container = row.getResult();
			SlotVisit loadSlotVisit = schedule.getSlotAllocations().stream().filter(x -> x.getSlot() == loadSlot).map(SlotAllocation::getSlotVisit).findAny().orElseThrow();
			SlotVisit dischargeSlotVisit = schedule.getSlotAllocations().stream().filter(x -> x.getSlot() == dischargeSlot).map(SlotAllocation::getSlotVisit).findAny().orElseThrow();
			assert loadSlotVisit != null;
			assert dischargeSlotVisit != null;
			CanalJourneyEvent ladenPanama = MarketabilityUtils.findNextPanama(loadSlotVisit);
			CanalJourneyEvent ballastPanama = MarketabilityUtils.findNextPanama(dischargeSlotVisit);
			container.setBuyDate(loadSlotVisit.getStart().toLocalDateTime());
			container.setSellDate(dischargeSlotVisit.getStart().toLocalDateTime());
			if (ladenPanama != null) {
				container.setLadenPanama(ladenPanama.getStart().toLocalDateTime());
			}
			if (ballastPanama != null) {
				container.setBallastPanama(ballastPanama.getStart().toLocalDateTime());
			}
			MarketabilityUtils.addNextEventToRow(container, dischargeSlotVisit);
		}
		return schedule;
	}

	private void createFutureJobs(final Schedule schedule, final MarketabilityModel model, final IMapperClass mapper, final Map<ShippingOption, VesselAssignmentType> shippingMap,
			final IProgressMonitor monitor, final ModelEntityMap modelEntityMap, final List<Future<Runnable>> futures, JobExecutor jobExecutor) {
		for (final MarketabilityRow row : model.getRows()) {
			row.getResult().getRhsResults().clear();

			ShippingOption shipping = row.getShipping();

			final VesselAssignmentType vesselAssignment = shippingMap.get(shipping);
			final int vesselSpotIndex = getSpotIndex(shipping);
			final Double vesselSpeed;
			if (model.isSetVesselSpeed()) {
				vesselSpeed = model.getVesselSpeed();
			} else {
				vesselSpeed = null;
			}
			final LoadSlot load;

			if (row.getBuyOption() != null) {
				load = mapper.getOriginal(row.getBuyOption());
				if (load == null) {
					continue;
				}

				for (final SpotMarket market : model.getMarkets()) {
					final Callable<Runnable> job = () -> {
						if (monitor.isCanceled()) {
							return null;
						}
						try {
							final MarketabilityResult result;
							if (model.getShippingTemplates().contains(shipping)) {
								result = generateMarketabilityResult(schedule, mapper, modelEntityMap, vesselAssignment, vesselSpotIndex, load, market, vesselSpeed);
							} else {
								result = null;
							}
							return () -> {
								row.getResult().getRhsResults().add(result);
							};
						} finally {
							monitor.worked(1);
						}
					};
					futures.add(jobExecutor.submit(job));
				}
			}
		}
	}

	private int getSpotIndex(ShippingOption shipping) {
		int vsi = -2;

		if (shipping instanceof ExistingCharterMarketOption ecmo) {
			vsi = ecmo.getSpotIndex();
		}
		return vsi;
	}

	private MarketabilityResult generateMarketabilityResult(final Schedule schedule, final IMapperClass mapper, final ModelEntityMap modelEntityMap, final VesselAssignmentType vesselAssignment,
			final int vesselSpotIndex, final LoadSlot load, final SpotMarket market, final Double vesselSpeed) {

		final MarketabilityResult marketabilityResult = AnalyticsFactory.eINSTANCE.createMarketabilityResult();
		marketabilityResult.setTarget(market);

		final InternalResult ret = new InternalResult();
		String timeZone = null;
		// Selects sales markets in the next 4 months from the load slot
		for (int i = 0; i < 4; ++i) {
			final DischargeSlot discharge = getDischarge(mapper, load, market, i);
			if (discharge == null) {
				continue;
			}
			Slot<?> marketabilityTarget = discharge;
			timeZone = discharge.getPort().getLocation().getTimeZone();
			final InternalResult result = doShipped(schedule, vesselAssignment, vesselSpotIndex, load, discharge, marketabilityTarget, dataTransformer.getInitialSequences(), vesselSpeed);
			ret.merge(result);
		}
		assert timeZone != null;
		if (ret.earliestTime != Integer.MAX_VALUE) {
			marketabilityResult.setEarliestETA(modelEntityMap.getDateFromHours(ret.earliestTime, timeZone));
			marketabilityResult.setLatestETA(modelEntityMap.getDateFromHours(ret.latestTime, timeZone));
		}
		return marketabilityResult;
	}

	private InternalResult doShipped(final Schedule schedule, final VesselAssignmentType vesselAssignment, final int vesselSpotIndex, final LoadSlot load, final DischargeSlot discharge,
			final Slot<?> target, final ISequences initialSequences, final Double vesselSpeed) {
		final IResource resource;

		final IPortSlot a = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(load, IPortSlot.class);
		final IPortSlot b = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(discharge, IPortSlot.class);

		if (vesselAssignment instanceof VesselCharter vesselCharter) {
			resource = SequenceHelper.getResource(dataTransformer, vesselCharter);
		} else if (vesselAssignment instanceof CharterInMarket charterInMarket) {
			resource = SequenceHelper.getResource(dataTransformer, charterInMarket, vesselSpotIndex);
		} else {
			return null;
		}
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final List<@NonNull ISequenceElement> cargoSegment = Lists.newArrayList(portSlotProvider.getElement(a), portSlotProvider.getElement(b));
		final InternalResult ret = new InternalResult();

		final IPortSlot portSlot = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(target, IPortSlot.class);

		final MarketabilitySandboxEvaluator evaluator = injector.getInstance(MarketabilitySandboxEvaluator.class);
		final MarketabilityCargoSequenceGenerator generator = injector.getInstance(MarketabilityCargoSequenceGenerator.class);
		generator.generateOptions(schedule, initialSequences, cargoSegment, resource, portSlot, vesselSpeed, (solution) -> {
			final SingleResult result = evaluator.evaluate(resource, solution, portSlot);
			ret.merge(result);
			return result != null;
		});
		return ret;
	}

	private DischargeSlot getDischarge(final IMapperClass mapper, final LoadSlot load, final SpotMarket market, final int month) {
		if (market instanceof FOBSalesMarket || load.isDESPurchase()) {
			throw new IllegalArgumentException("Marketablility cargoes must be shipped");
		}
		return mapper.getSalesMarketOriginal(market, YearMonth.from(load.getWindowStart().plusMonths(month)));
	}
}
