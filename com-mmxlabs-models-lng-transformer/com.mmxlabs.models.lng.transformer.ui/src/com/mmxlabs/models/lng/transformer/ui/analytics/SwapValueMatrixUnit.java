/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.ChangeablePriceCalculator;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveGeneratorModule;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class SwapValueMatrixUnit {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final JobExecutorFactory executorService;

	@NonNull
	private final ISequences initialSequences;

	private IScenarioDataProvider sdp;

	public SwapValueMatrixUnit(@NonNull final IScenarioDataProvider sdp, @NonNull final LNGDataTransformer lngDataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings, @NonNull final JobExecutorFactory executorService, @NonNull final ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.sdp = sdp;
		this.dataTransformer = lngDataTransformer;
		this.initialSequences = initialSequences;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		final List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputState.getBestSolution().getFirst()));
		modules.add(new InitialPhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, constraintAndFitnessSettings), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.IGNORE_EXPOSURES)).toInstance(Boolean.TRUE);
				install(new MoveGeneratorModule());
			}

			@Provides
			private EvaluationHelper provideEvaluationHelper(final Injector injector) {
				final EvaluationHelper helper = new EvaluationHelper();
				injector.injectMembers(helper);

				helper.setFlexibleCapacityViolationCount(Integer.MAX_VALUE);
				return helper;
			}

			@Provides
			@ThreadLocalScope
			private SwapValueSandboxEvaluator providePerThreadOptimiser(@NonNull final Injector injector) {
				SwapValueSandboxEvaluator optimiser = new SwapValueSandboxEvaluator();
				injector.injectMembers(optimiser);
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.executorService = executorService.withDefaultBegin(() -> {
			final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
			s.enter();
			return s;
		});
	}

	public void run(@NonNull final SwapValueMatrixModel model, @NonNull final Pair<@NonNull LoadSlot, @NonNull DischargeSlot> swapCargo, @NonNull final IMapperClass mapper,
			@NonNull final IProgressMonitor monitor) {
		monitor.beginTask("Evaluate swaps", IProgressMonitor.UNKNOWN);
		try (JobExecutor jobExecutor = executorService.begin()) {

			@NonNull
			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			// Swap out price calculator on port slots
			final VesselCharter vesselCharter = model.getBaseVesselCharter().getVesselCharter();
			final IResource resource = SequenceHelper.getResource(dataTransformer, vesselCharter);

			final ISequenceElement baseLoadSequenceElement;
			{
				final IPortSlot loadOption = modelEntityMap.getOptimiserObjectNullChecked(swapCargo.getFirst(), IPortSlot.class);
				baseLoadSequenceElement = portSlotProvider.getElement(loadOption);
			}

			final ChangeablePriceCalculator baseDischargePriceCalculator;
			final ISequenceElement baseDischargeSequenceElement;
			{
				final IPortSlot dischargeOption = modelEntityMap.getOptimiserObjectNullChecked(swapCargo.getSecond(), IPortSlot.class);
				if (dischargeOption instanceof final com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot oBaseDischargeSlot) {
					baseDischargePriceCalculator = new ChangeablePriceCalculator();
					oBaseDischargeSlot.setDischargePriceCalculator(baseDischargePriceCalculator);
					baseDischargeSequenceElement = portSlotProvider.getElement(dischargeOption);
				} else {
					throw new IllegalStateException("Swap sandbox could not set discharge price");
				}
			}

			@NonNull
			final LoadSlot swapLoadSlot;
			final ChangeablePriceCalculator swapLoadPriceCalculator;
			final ISequenceElement swapLoadSequenceElement;
			{

				swapLoadSlot = mapper.getPurchaseMarketOriginal(model.getSwapLoadMarket().getMarket(), model.getSwapLoadMarket().getMonth());
				final IPortSlot loadPortSlot = modelEntityMap.getOptimiserObject(swapLoadSlot, IPortSlot.class);
				if (loadPortSlot instanceof com.mmxlabs.scheduler.optimiser.components.impl.LoadOption oSwapMarketLoadSlot) {
					final ILoadPriceCalculator loadPriceCalculator = oSwapMarketLoadSlot.getLoadPriceCalculator();
					if (loadPriceCalculator instanceof ChangeablePriceCalculator changeableLoadPriceCalculator) {
						if (model.getSwapFee() != 0.0) {
							final int internalSwapFee = OptimiserUnitConvertor.convertToInternalPrice(model.getSwapFee());
							final ChangeablePriceCalculator newLoadPriceCalculator = new ChangeablePriceCalculator(internalSwapFee);
							oSwapMarketLoadSlot.setLoadPriceCalculator(newLoadPriceCalculator);
							swapLoadPriceCalculator = newLoadPriceCalculator;
						} else {
							swapLoadPriceCalculator = changeableLoadPriceCalculator;
						}
						swapLoadSequenceElement = portSlotProvider.getElement(loadPortSlot);
					} else {
						throw new IllegalStateException("Swap sandbox could not set market load price");

					}
				} else {
					throw new IllegalStateException("Swap sandbox could not set market load price");
				}
			}

			final ChangeablePriceCalculator swapDischargePriceCalculator;
			final ISequenceElement swapDischargeSequenceElement;
			final DischargeSlot swapDischargeSlot;
			{
				swapDischargeSlot = mapper.getSalesMarketOriginal(model.getSwapDischargeMarket().getMarket(), model.getSwapDischargeMarket().getMonth());
				final IPortSlot dischargePortSlot = modelEntityMap.getOptimiserObject(swapDischargeSlot, IPortSlot.class);
				if (dischargePortSlot instanceof com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption oSwapMarketDischargeSlot) {
					final ISalesPriceCalculator salesPriceCalculator = oSwapMarketDischargeSlot.getDischargePriceCalculator();
					if (salesPriceCalculator instanceof ChangeablePriceCalculator changeableDischargePriceCalculator) {
						swapDischargePriceCalculator = changeableDischargePriceCalculator;
						swapDischargeSequenceElement = portSlotProvider.getElement(dischargePortSlot);
					} else {
						throw new IllegalStateException("Swap sandbox could not set market discharge price");
					}
				} else {
					throw new IllegalStateException("Swap sandbox could not set market discharge price");

				}
			}

			final ISequence baseSequence = initialSequences.getSequence(resource);
			// Check cargo is paired together
			{
				final Iterator<ISequenceElement> seqIter = baseSequence.iterator();
				boolean foundLoad = false;
				boolean foundDischarge = false;
				while (seqIter.hasNext()) {
					final ISequenceElement nextElem = seqIter.next();
					if (foundLoad) {
						if (nextElem == baseDischargeSequenceElement) {
							foundDischarge = true;
							break;
						} else {
							throw new IllegalStateException("Could not find scheduled cargo");
						}
					} else if (nextElem == baseLoadSequenceElement) {
						foundLoad = true;
					}
				}
				if (!foundLoad || !foundDischarge) {
					throw new IllegalStateException("Could not find scheduled cargo");
				}
			}
			final ISequence swapVesselCharterSequence;
			{
				final List<ISequenceElement> swapVesselCharterSequenceList = new ArrayList<>(baseSequence.size());
				final Iterator<ISequenceElement> seqIter = baseSequence.iterator();
				while (seqIter.hasNext()) {
					final ISequenceElement nextElem = seqIter.next();
					if (nextElem == baseLoadSequenceElement) {
						seqIter.next();
						break;
					}
					swapVesselCharterSequenceList.add(nextElem);
				}
				swapVesselCharterSequenceList.add(baseLoadSequenceElement);
				swapVesselCharterSequenceList.add(swapDischargeSequenceElement);
				seqIter.forEachRemaining(swapVesselCharterSequenceList::add);
				swapVesselCharterSequence = new ListSequence(swapVesselCharterSequenceList);
			}
			final Pair<IResource, IModifiableSequence> swapNonShippedResouceSequencePair = SequenceHelper.createFOBDESSequence(injector, swapLoadSlot, swapCargo.getSecond());

			final ISequences baseSequences;
			{
				final Map<IResource, ISequence> map = new HashMap<>();
				map.put(resource, baseSequence);
				final ISequencesAttributesProvider provider = initialSequences.getProviders();
				final List<ISequenceElement> unusedElements = new ArrayList<>(initialSequences.getUnusedElements());
				baseSequences = new Sequences(Collections.singletonList(resource), map, Collections.emptyList(), provider);
			}
			final ISequences swapSequences;
			{
				final Map<IResource, ISequence> map = new HashMap<>();
				map.put(resource, swapVesselCharterSequence);
				map.put(swapNonShippedResouceSequencePair.getFirst(), swapNonShippedResouceSequencePair.getSecond());
				final List<IResource> resources = new ArrayList<>();
				resources.add(resource);
				resources.add(swapNonShippedResouceSequencePair.getFirst());
				final List<ISequenceElement> unusedElements = new ArrayList<>(initialSequences.getUnusedElements());
				final Set<ISequenceElement> newUsedElements = new HashSet<>();
				newUsedElements.add(swapLoadSequenceElement);
				newUsedElements.add(swapDischargeSequenceElement);
				unusedElements.removeAll(newUsedElements);
				final ISequencesAttributesProvider provider = initialSequences.getProviders();
				swapSequences = new Sequences(resources, map, Collections.emptyList(), provider);
			}

			final Map<Pair<Integer, Integer>, Pair<Long, Long>> results = new HashMap<>();
			{
				final int baseSalesPriceMinInclusive = model.getBaseDischargeMinPrice();
				final int baseSalesPriceMaxInclusive = model.getBaseDischargeMaxPrice();
				assert baseSalesPriceMinInclusive <= baseSalesPriceMaxInclusive;
				final int baseSalesPriceStepSize = model.getBaseDischargeStepSize();
				assert baseSalesPriceStepSize > 0;
				final int marketPriceMinInclusive = model.getMarketMinPrice();
				final int marketPriceMaxInclusive = model.getMarketMaxPrice();
				assert marketPriceMinInclusive <= marketPriceMaxInclusive;
				final int marketPriceStepSize = model.getMarketStepSize();
				assert marketPriceStepSize > 0;

				final List<Future<Pair<Pair<Integer, Integer>, Pair<Schedule, Schedule>>>> futures = new LinkedList<>();
				for (int marketPrice = marketPriceMinInclusive; marketPrice <= marketPriceMaxInclusive; ++marketPrice) {
					for (int baseSalesPrice = baseSalesPriceMinInclusive; baseSalesPrice <= baseSalesPriceMaxInclusive; ++baseSalesPrice) {
						final int pBaseSalesPrice = baseSalesPrice;
						final int pMarketPrice = marketPrice;
						final Callable<Pair<Pair<Integer, Integer>, Pair<Schedule, Schedule>>> job = () -> {
							if (monitor.isCanceled()) {
								return null;
							}
							final int internalBaseSalesPrice = OptimiserUnitConvertor.convertToInternalPrice(pBaseSalesPrice);
							final int internalMarketPrice = OptimiserUnitConvertor.convertToInternalPrice(pMarketPrice);
							baseDischargePriceCalculator.setPrice(internalBaseSalesPrice);
							swapLoadPriceCalculator.setPrice(internalMarketPrice);
							// Should subtract swap fee here
							swapDischargePriceCalculator.setPrice(internalMarketPrice);

							final SwapValueSandboxEvaluator evaluator = injector.getInstance(SwapValueSandboxEvaluator.class);
							final Pair<Schedule, Schedule> baseSwapSchedulePair = evaluator.evaluate(baseSequences, swapSequences, modelEntityMap);
							final Pair<Integer, Integer> key = Pair.of(pBaseSalesPrice, pMarketPrice);
							return Pair.of(key, baseSwapSchedulePair);
						};
						futures.add(jobExecutor.submit(job));
					}
				}
				final CompoundCommand cmd = new CompoundCommand("Calculate value matrix");
				final SwapValueMatrixResultSet resultsSet = AnalyticsFactory.eINSTANCE.createSwapValueMatrixResultSet();
				resultsSet.setSwapFee(model.getSwapFee());
				if (swapLoadSlot instanceof SpotLoadSlot spotLoadSlot) {
					resultsSet.setGeneratedSpotLoadSlot(spotLoadSlot);
				}
				if (swapDischargeSlot instanceof SpotDischargeSlot spotDischargeSlot) {
					resultsSet.setGeneratedSpotDischargeSlot(spotDischargeSlot);
				}
				for (final Future<Pair<Pair<Integer, Integer>, Pair<Schedule, Schedule>>> f : futures) {
					if (monitor.isCanceled()) {
						return;
					}
					try {
						final Pair<Pair<Integer, Integer>, Pair<Schedule, Schedule>> result = f.get();
						final int baseDischargePrice = result.getFirst().getFirst();
						final int swapMarketPrice = result.getFirst().getSecond();
						final Schedule baseSchedule = result.getSecond().getFirst();
						final Schedule swapSchedule = result.getSecond().getSecond();

						final SlotAllocation baseFobLoadSA = baseSchedule.getSlotAllocations().stream() //
								.filter(sa -> sa.getSlot() == swapCargo.getFirst()) //
								.findFirst() //
								.get();
						final SlotAllocation baseDesSaleSA = baseSchedule.getSlotAllocations().stream() //
								.filter(sa -> sa.getSlot() == swapCargo.getSecond()) //
								.findFirst() //
								.get();
						final SlotAllocation swapFobLoadSA = swapSchedule.getSlotAllocations().stream() //
								.filter(sa -> sa.getSlot() == swapCargo.getFirst()) //
								.findFirst() //
								.get();
						final SlotAllocation swapDesSaleSA = swapSchedule.getSlotAllocations().stream() //
								.filter(sa -> sa.getSlot() == swapCargo.getSecond()) //
								.findFirst() //
								.get();
						final SlotAllocation swapDesMarketPurchaseSA = swapSchedule.getSlotAllocations().stream() //
								.filter(sa -> sa.getSlot() == swapLoadSlot) //
								.findFirst() //
								.get();
						final SlotAllocation swapDesMarketSaleSA = swapSchedule.getSlotAllocations().stream() //
								.filter(sa -> sa.getSlot() == swapDischargeSlot) //
								.findFirst() //
								.get();

						final SwapValueMatrixResult swapValueMatrixResult = AnalyticsFactory.eINSTANCE.createSwapValueMatrixResult();
						swapValueMatrixResult.setBaseDischargePrice(baseDischargePrice);
						swapValueMatrixResult.setSwapMarketPrice(swapMarketPrice);
						final long basePnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(baseSchedule);
						final long swapPnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(swapSchedule);
						swapValueMatrixResult.setSwapPnlMinusBasePnl(swapPnl - basePnl);
						swapValueMatrixResult.setBaseLoadPrice(baseFobLoadSA.getPrice());
						swapValueMatrixResult.setSwapFobLoadPrice(swapFobLoadSA.getPrice());
						swapValueMatrixResult.setBaseFobLoadVolume(baseFobLoadSA.getEnergyTransferred());
						swapValueMatrixResult.setSwapFobLoadVolume(swapFobLoadSA.getEnergyTransferred());
						swapValueMatrixResult.setMarketBuyVolume(swapDesMarketPurchaseSA.getEnergyTransferred());
						swapValueMatrixResult.setMarketSellVolume(swapDesMarketSaleSA.getEnergyTransferred());
						swapValueMatrixResult.setBaseDesSellVolume(baseDesSaleSA.getEnergyTransferred());

						// Base PnL
						final List<Sequence> baseEvaluatedSequences = baseSchedule.getSequences();
						assert baseEvaluatedSequences.size() == 1;
						final Sequence baseEvaluatedSequence = baseEvaluatedSequences.get(0);
						assert baseEvaluatedSequence.getVesselCharter() == vesselCharter;
						final List<ProfitAndLossContainer> basePrecedingPnlContainers = new ArrayList<>();

						final Iterator<Event> iterBaseVisits = baseEvaluatedSequence.getEvents().stream().filter(event -> event instanceof SlotVisit || event instanceof ProfitAndLossContainer)
								.iterator();
						while (iterBaseVisits.hasNext()) {
							final Event nextEvent = iterBaseVisits.next();
							if (nextEvent instanceof final SlotVisit sv && sv.getSlotAllocation().getSlot() instanceof LoadSlot ls) {
								if (ls == swapCargo.getFirst()) {
									break;
								}
								final CargoAllocation cargoAllocation = sv.getSlotAllocation().getCargoAllocation();
								basePrecedingPnlContainers.add(cargoAllocation);
							} else if (nextEvent instanceof final ProfitAndLossContainer pnlContainer) {
								basePrecedingPnlContainers.add(pnlContainer);
							}
						}
						final CargoAllocation baseCargoAllocation;
						{
							assert iterBaseVisits.hasNext();
							final Event dischargeSlotEvent = iterBaseVisits.next();
							assert dischargeSlotEvent instanceof SlotVisit dischargeSlotVisit && dischargeSlotVisit.getSlotAllocation().getSlot() == swapCargo.getSecond();
							final SlotVisit dischargeSlotVisit = (SlotVisit) dischargeSlotEvent;
							baseCargoAllocation = dischargeSlotVisit.getSlotAllocation().getCargoAllocation();
						}

						final List<ProfitAndLossContainer> baseSucceedingPnlContainers = new ArrayList<>();
						while (iterBaseVisits.hasNext()) {
							final Event nextEvent = iterBaseVisits.next();
							if (nextEvent instanceof final SlotVisit sv && sv.getSlotAllocation().getSlot() instanceof LoadSlot) {
								baseSucceedingPnlContainers.add(sv.getSlotAllocation().getCargoAllocation());
							} else if (nextEvent instanceof final ProfitAndLossContainer pnlContainer) {
								baseSucceedingPnlContainers.add(pnlContainer);
							}
						}

						// Swap PnL
						final List<Sequence> swapEvaluatedSequences = swapSchedule.getSequences();
						assert swapEvaluatedSequences.size() == 2;

						final Sequence swapBackfillSeq;
						final Sequence swapVesselCharterSeq;
						{
							final Sequence firstSequence = swapEvaluatedSequences.get(0);
							final Sequence secondSequence = swapEvaluatedSequences.get(1);
							if (firstSequence.getVesselCharter() == vesselCharter) {
								swapVesselCharterSeq = firstSequence;
								assert secondSequence.getVesselCharter() == null;
								swapBackfillSeq = secondSequence;
							} else {
								assert firstSequence.getVesselCharter() == null;
								assert secondSequence.getVesselCharter() == vesselCharter;
								swapBackfillSeq = firstSequence;
								swapVesselCharterSeq = secondSequence;
							}
						}

						final List<ProfitAndLossContainer> swapPrecedingPnlContainers = new ArrayList<>();

						final Iterator<Event> iterSwapVesselCharterVisits = swapVesselCharterSeq.getEvents().stream()
								.filter(event -> event instanceof SlotVisit || event instanceof ProfitAndLossContainer).iterator();
						while (iterSwapVesselCharterVisits.hasNext()) {
							final Event nextEvent = iterSwapVesselCharterVisits.next();
							if (nextEvent instanceof final SlotVisit sv && sv.getSlotAllocation().getSlot() instanceof LoadSlot ls) {
								if (ls == swapCargo.getFirst()) {
									break;
								}
								final CargoAllocation cargoAllocation = sv.getSlotAllocation().getCargoAllocation();
								swapPrecedingPnlContainers.add(cargoAllocation);
							} else if (nextEvent instanceof final ProfitAndLossContainer pnlContainer) {
								swapPrecedingPnlContainers.add(pnlContainer);
							}
						}
						final CargoAllocation swapShippedCargoAllocation;
						{
							assert iterSwapVesselCharterVisits.hasNext();
							final Event desMarketSlotEvent = iterSwapVesselCharterVisits.next();
							assert desMarketSlotEvent instanceof SlotVisit desMarketSlotVisit && desMarketSlotVisit.getSlotAllocation().getSlot() == swapDischargeSlot;
							final SlotVisit desMarketSlotVisit = (SlotVisit) desMarketSlotEvent;
							swapShippedCargoAllocation = desMarketSlotVisit.getSlotAllocation().getCargoAllocation();
						}

						final List<ProfitAndLossContainer> swapSucceedingPnlContainers = new ArrayList<>();
						while (iterSwapVesselCharterVisits.hasNext()) {
							final Event nextEvent = iterSwapVesselCharterVisits.next();
							if (nextEvent instanceof final SlotVisit sv && sv.getSlotAllocation().getSlot() instanceof LoadSlot) {
								swapSucceedingPnlContainers.add(sv.getSlotAllocation().getCargoAllocation());
							} else if (nextEvent instanceof final ProfitAndLossContainer pnlContainer) {
								swapSucceedingPnlContainers.add(pnlContainer);
							}
						}
						assert swapBackfillSeq.getEvents().size() == 2;
						final CargoAllocation swapBackfillCargoAllocation = ((SlotVisit) swapBackfillSeq.getEvents().get(0)).getSlotAllocation().getCargoAllocation();

						final long basePrecedingPnl = sumPnl(basePrecedingPnlContainers);
						final long baseCargoPnl = sumPnl(baseCargoAllocation);
						final long baseSucceedingPnl = sumPnl(baseSucceedingPnlContainers);

						final long swapPrecedingPnl = sumPnl(swapPrecedingPnlContainers);
						final long swapShippedCargoPnl = sumPnl(swapShippedCargoAllocation);
						final long swapBackfillCargoPnl = sumPnl(swapBackfillCargoAllocation);
						final long swapSucceedingPnl = sumPnl(swapSucceedingPnlContainers);

						swapValueMatrixResult.setBasePrecedingPnl(basePrecedingPnl);
						swapValueMatrixResult.setBaseCargoPnl(baseCargoPnl);
						swapValueMatrixResult.setBaseSucceedingPnl(baseSucceedingPnl);

						swapValueMatrixResult.setSwapPrecedingPnl(swapPrecedingPnl);
						swapValueMatrixResult.setSwapShippedCargoPnl(swapShippedCargoPnl);
						swapValueMatrixResult.setSwapBackfillCargoPnl(swapBackfillCargoPnl);
						swapValueMatrixResult.setSwapSucceedingPnl(swapSucceedingPnl);

						swapValueMatrixResult.setBasePurchaseCost(baseCargoAllocation.getSlotAllocations().get(0).getVolumeValue());
						swapValueMatrixResult.setBaseSaleRevenue(baseCargoAllocation.getSlotAllocations().get(1).getVolumeValue());
						swapValueMatrixResult.setBaseShippingCost(ScheduleModelKPIUtils.getTotalShippingCost(baseCargoAllocation) - calculateBog(baseCargoAllocation));
						swapValueMatrixResult.setBaseAdditionalPnl(ScheduleModelKPIUtils.getAdditionalProfitAndLoss(baseCargoAllocation));

						swapValueMatrixResult.setSwapCargoPurchaseCost(swapShippedCargoAllocation.getSlotAllocations().get(0).getVolumeValue());
						swapValueMatrixResult.setSwapCargoSaleRevenue(swapShippedCargoAllocation.getSlotAllocations().get(1).getVolumeValue());
						swapValueMatrixResult.setSwapCargoShippingCost(ScheduleModelKPIUtils.getTotalShippingCost(swapShippedCargoAllocation) - calculateBog(swapShippedCargoAllocation));
						swapValueMatrixResult.setSwapCargoAdditionalPnl(ScheduleModelKPIUtils.getAdditionalProfitAndLoss(swapShippedCargoAllocation));

						swapValueMatrixResult.setSwapBackfillPurchaseCost(swapBackfillCargoAllocation.getSlotAllocations().get(0).getVolumeValue());
						swapValueMatrixResult.setSwapBackfillSaleRevenue(swapBackfillCargoAllocation.getSlotAllocations().get(1).getVolumeValue());
						swapValueMatrixResult.setSwapBackfillAdditionalPnl(ScheduleModelKPIUtils.getAdditionalProfitAndLoss(swapBackfillCargoAllocation));

						resultsSet.getResults().add(swapValueMatrixResult);
					} catch (final InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					} catch (final ExecutionException e) {
						e.printStackTrace();
					}
				}
				cmd.append(SetCommand.create(sdp.getEditingDomain(), model, AnalyticsPackage.eINSTANCE.getSwapValueMatrixModel_SwapValueMatrixResult(), resultsSet));
				RunnerHelper.syncExecDisplayOptional(() -> {
					sdp.getEditingDomain().getCommandStack().execute(cmd);
				});
				if (monitor.isCanceled()) {
					return;
				}

			}
		}
	}

	private static long sumPnl(@NonNull final List<ProfitAndLossContainer> pnlContainers) {
		return pnlContainers.stream().mapToLong(ScheduleModelKPIUtils::getElementPNL).sum();
	}

	private static long sumPnl(@NonNull final ProfitAndLossContainer pnlContainer) {
		return ScheduleModelKPIUtils.getElementPNL(pnlContainer);
	}

	private int calculateBog(final EventGrouping eventGrouping) {
		return eventGrouping.getEvents().stream() //
				.filter(FuelUsage.class::isInstance) //
				.map(FuelUsage.class::cast) //
				.mapToInt(fu -> getFuelCost(fu, Fuel.NBO, Fuel.FBO)) //
				.sum();
	}

	private int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		return fuelUser == null //
				? 0 //
				: fuelUser.getFuels().stream() //
						.filter(fq -> fuelsOfInterest.contains(fq.getFuel())) //
						.mapToDouble(FuelQuantity::getCost) //
						.mapToInt(d -> (int) d) //
						.sum();

	}

}
