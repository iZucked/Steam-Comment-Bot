/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.mtm;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.MTMResult;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
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
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.InsertCargoSequencesGenerator;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LazyFollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;

public class MTMSanboxUnit {

	static class SingleResult {
		int arrivalTime = Integer.MAX_VALUE;
		int price;
		long volumeInMMBTU;
		long shippingCost;
	}

	static class InternalResult {
		int arrivalTime = Integer.MAX_VALUE;
		int netbackPrice;
		long volumeInMMBTU;
		long shippingCost;

		public void merge(@Nullable final InternalResult other) {
			if (other != null) {
				if (other.arrivalTime < this.arrivalTime) {
					this.arrivalTime = other.arrivalTime;
					this.netbackPrice = other.netbackPrice;
					this.volumeInMMBTU = other.volumeInMMBTU;
					this.shippingCost = other.shippingCost;
				}
			}
		}

		public void merge(@Nullable final SingleResult other) {
			if (other != null) {
				if (other.arrivalTime < this.arrivalTime) {
					this.arrivalTime = other.arrivalTime;
					this.netbackPrice = other.price;
					this.volumeInMMBTU = other.volumeInMMBTU;
					this.shippingCost = other.shippingCost;
				}
			}
		}
	}

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@NonNull
	private final String phase;

	private @NonNull JobExecutorFactory jobExecutorFactory;

	private @NonNull LNGScenarioModel scenarioModel;

	@SuppressWarnings("null")
	public MTMSanboxUnit(@NonNull final LNGScenarioModel scenarioModel, @NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final JobExecutorFactory jobExecutorFactory, @NonNull final ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.scenarioModel = scenarioModel;
		this.dataTransformer = dataTransformer;
		this.phase = phase;
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
				bind(InsertCargoSequencesGenerator.class);

				bind(LazyFollowersAndPrecedersProviderImpl.class).in(Singleton.class);
				bind(IFollowersAndPreceders.class).to(LazyFollowersAndPrecedersProviderImpl.class);

				bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
			}

			private final Map<Thread, EvaluationHelper> threadCache_EvaluationHelper = new ConcurrentHashMap<>(100);

			@Provides
			private EvaluationHelper provideEvaluationHelper(final Injector injector, @Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE) final boolean isReevaluating,
					@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences) {

				EvaluationHelper helper = threadCache_EvaluationHelper.get(Thread.currentThread());
				if (helper == null) {
					helper = new EvaluationHelper(isReevaluating);
					injector.injectMembers(helper);

					final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
					helper.acceptSequences(initialRawSequences, manipulator.createManipulatedSequences(initialRawSequences));

					helper.setFlexibleCapacityViolationCount(Integer.MAX_VALUE);

					threadCache_EvaluationHelper.put(Thread.currentThread(), helper);
				}
				return helper;
			}

			@Provides
			@ThreadLocalScope
			private MTMSandboxEvaluator providePerThreadOptimiser(@NonNull final Injector injector) {

				MTMSandboxEvaluator optimiser = new MTMSandboxEvaluator();
				injector.injectMembers(optimiser);
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public void run(final MTMModel model, final IMapperClass mapper, @NonNull final IProgressMonitor monitor) {
		monitor.beginTask("Generate solutions", IProgressMonitor.UNKNOWN);
		final JobExecutorFactory subExecutorFactory = jobExecutorFactory.withDefaultBegin(() -> {
			final ThreadLocalScopeImpl s = injector.getInstance(ThreadLocalScopeImpl.class);
			s.enter();
			return s;
		});
		try (JobExecutor jobExecutor = subExecutorFactory.begin()) {
			try {

				@NonNull
				final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

				// Phase 1 : generate basic point-to-point break evens
				{
					final List<Future<Runnable>> futures = new LinkedList<>();

					for (final MTMRow row : model.getRows()) {
						row.getRhsResults().clear();
						row.getLhsResults().clear();

						createOpportunity(model, mapper, monitor, modelEntityMap, futures, row, mapper.getOriginal(row.getSellOption()), mapper.getOriginal(row.getBuyOption()), jobExecutor);

					}

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
						} catch (final InterruptedException e) {
							e.printStackTrace();
						} catch (final ExecutionException e) {
							e.printStackTrace();
						}
					}
					if (monitor.isCanceled()) {
						return;
					}
				}
			} finally {
				monitor.done();
			}

		} finally {

			// Only keeping the best result
			for (final MTMRow row : model.getRows()) {

				Map<SpotMarket, List<MTMResult>> grouped = row.getRhsResults().stream().collect(Collectors.groupingBy(MTMResult::getTarget));

				for (final Map.Entry<SpotMarket, List<MTMResult>> entry : grouped.entrySet()) {
					double price = -Double.MAX_VALUE;
					final List<MTMResult> lmtm = entry.getValue();
					MTMResult best = null;
					for (final MTMResult result : lmtm) {
						if (price < result.getEarliestPrice()) {
							price = result.getEarliestPrice();
							best = result;
						}
					}
					lmtm.remove(best);
					row.getRhsResults().removeAll(lmtm);
				}
			}
		}
	}

	private void createOpportunity(final MTMModel model, final IMapperClass mapper, final IProgressMonitor monitor, final ModelEntityMap modelEntityMap, final List<Future<Runnable>> futures,
			final MTMRow row, final DischargeSlot discharge, final LoadSlot load, JobExecutor jobExecutor) {

		if (discharge == null && load == null) {
			return;
		}

		for (final CharterInMarket shipping : model.getNominalMarkets()) {

			if (!shipping.isEnabled()) {
				continue;
			}

			for (final SpotMarket market : model.getMarkets()) {
				final Callable<Runnable> job = () -> {
					if (monitor.isCanceled()) {
						return null;
					}

					final MTMResult mtmResult = AnalyticsFactory.eINSTANCE.createMTMResult();
					final ExistingCharterMarketOption ecmo = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
					ecmo.setCharterInMarket(shipping);
					ecmo.setSpotIndex(-1);
					mtmResult.setShipping(ecmo);
					mtmResult.setTarget(market);

					final InternalResult ret = new InternalResult();
					LoadSlot ls = load;
					DischargeSlot ds = discharge;

					Slot<?> beTarget = null;

					try {

						String timeZone = "UTC";
						for (int i = 0; i < 2; ++i) {

							boolean shipped = true;

							if (ls != null) {
								if (market instanceof FOBSalesMarket) {
									shipped = false;
									ds = mapper.getSalesMarketOriginal(market, YearMonth.from(ls.getWindowStart()));
								} else if (market instanceof DESSalesMarket) {
									if (ls.isDESPurchase()) {
										shipped = false;
										ds = mapper.getSalesMarketOriginal(market, YearMonth.from(ls.getWindowStart()));
									} else {
										shipped = true;
										ds = mapper.getSalesMarketOriginal(market, YearMonth.from(ls.getWindowStart().plusMonths(i)));
									}
								}
								if (ds != null) {
									beTarget = ds;
								}
							} else if (ds != null) {
								if (market instanceof FOBPurchasesMarket) {
									if (ds.isFOBSale()) {
										if (((FOBPurchasesMarket) market).getNotionalPort() == ds.getPort()) {
											shipped = false;
											ls = mapper.getPurchaseMarketOriginal(market, YearMonth.from(ds.getWindowStart()));
										}
									} else {
										shipped = true;
										ls = mapper.getPurchaseMarketOriginal(market, YearMonth.from(ds.getWindowStart().minusMonths(i)));
									}
								} else if (market instanceof DESPurchaseMarket) {
									if (!ds.isFOBSale() && ((DESPurchaseMarket) market).getDestinationPorts().contains(ds.getPort())) {
										shipped = false;
										ls = mapper.getPurchaseMarketOriginal(market, YearMonth.from(ds.getWindowStart()));
									}
								}
								if (ls != null) {
									beTarget = ls;
								}
							}

							if (ls != null && ds != null) {
								if (shipped) {
									timeZone = ls.getPort().getLocation().getTimeZone();
								} else {
									if (ls.getPort() != null) {
										timeZone = ls.getPort().getLocation().getTimeZone();
									} else if (ds.getPort() != null) {
										timeZone = ds.getPort().getLocation().getTimeZone();
									}
								}

								final SingleResult result = doIt(shipped, shipping, ls, ds, beTarget);
								ret.merge(result);
								if (!shipped) {
									// only one iteration.
									break;
								}
							}

						}

						if (ret != null && ret.arrivalTime != Integer.MAX_VALUE) {
							mtmResult.setEarliestETA(modelEntityMap.getDateFromHours(ret.arrivalTime, timeZone).toLocalDate());
							mtmResult.setEarliestVolume(OptimiserUnitConvertor.convertToExternalVolume(ret.volumeInMMBTU));
							mtmResult.setEarliestPrice(OptimiserUnitConvertor.convertToExternalPrice(ret.netbackPrice));
							mtmResult.setShippingCost(OptimiserUnitConvertor.convertToExternalPrice(ret.volumeInMMBTU == 0 ? 0 : //
							Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(ret.shippingCost, ret.volumeInMMBTU)));
						}

						return new Runnable() {
							@Override
							public void run() {
								if (mtmResult.getEarliestETA() != null) {
									if (row.getBuyOption() != null) {
										row.getRhsResults().add(mtmResult);
									} else if (row.getSellOption() != null) {
										row.getLhsResults().add(mtmResult);
									}
								}
							}
						};
					} finally {
						monitor.worked(1);
					}
				};

				futures.add(jobExecutor.submit(job));
			}
		}
	}

	private @Nullable SingleResult doIt(final boolean shipped, final CharterInMarket charterInMarket, final LoadSlot load, final DischargeSlot discharge, final Slot target) {
		final ModifiableSequences solution;
		final IResource resource;
		if (!shipped) {
			resource = SequenceHelper.getResource(dataTransformer, load.isDESPurchase() ? load : discharge);
			solution = new ModifiableSequences(CollectionsUtil.makeArrayList(resource));
			SequenceHelper.addFOBDESSequence(solution, dataTransformer, load, discharge);
		} else {
			resource = SequenceHelper.getResource(dataTransformer, charterInMarket, -1);
			solution = new ModifiableSequences(CollectionsUtil.makeArrayList(resource));
			SequenceHelper.addSequence(solution, injector, charterInMarket, -1, load, discharge);
		}
		final MTMSandboxEvaluator evaluator = injector.getInstance(MTMSandboxEvaluator.class);
		final IPortSlot portSlot = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(target, IPortSlot.class);

		return evaluator.evaluate(resource, solution, portSlot);
	}
}
