/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.viability;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ViabilityResult;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
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
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LazyFollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;

public class ViabilitySanboxUnit {

	static class SingleResult {
		int arrivalTime = Integer.MAX_VALUE;
		int price;
		long volume;
	}

	static class InternalResult {
		int earliestTime = Integer.MAX_VALUE;
		int latestTime = Integer.MIN_VALUE;
		int earliestPrice;
		int latestPrice;
		long earliestVolume;
		long latestVolume;

		public void merge(@Nullable final InternalResult other) {
			if (other != null) {
				if (other.earliestTime < this.earliestTime) {
					this.earliestTime = other.earliestTime;
					this.earliestPrice = other.earliestPrice;
					this.earliestVolume = other.earliestVolume;
				}
				if (other.latestTime > this.latestTime) {
					this.latestTime = other.latestTime;
					this.latestPrice = other.latestPrice;
					this.latestVolume = other.latestVolume;
				}
			}
		}

		public void merge(@Nullable final SingleResult other) {
			if (other != null) {
				if (other.arrivalTime < this.earliestTime) {
					this.earliestTime = other.arrivalTime;
					this.earliestPrice = other.price;
					this.earliestVolume = other.volume;
				}
				if (other.arrivalTime > this.latestTime) {
					this.latestTime = other.arrivalTime;
					this.latestPrice = other.price;
					this.latestVolume = other.volume;
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

	private final Map<Thread, ViabilitySandboxEvaluator> threadCache = new ConcurrentHashMap<>(100);

	private @NonNull ExecutorService executorService;

	private @NonNull LNGScenarioModel scenarioModel;

	@SuppressWarnings("null")
	public ViabilitySanboxUnit(@NonNull final LNGScenarioModel scenarioModel, @NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final ExecutorService executorService, @NonNull final ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.scenarioModel = scenarioModel;
		this.dataTransformer = dataTransformer;
		this.phase = phase;
		this.executorService = executorService;

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
			private ViabilitySandboxEvaluator providePerThreadOptimiser(@NonNull final Injector injector) {

				ViabilitySandboxEvaluator optimiser = threadCache.get(Thread.currentThread());
				if (optimiser == null) {
					final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);
					scope.enter();
					optimiser = new ViabilitySandboxEvaluator();
					injector.injectMembers(optimiser);
					threadCache.put(Thread.currentThread(), optimiser);
				}
				return optimiser;
			}
		});

		injector = dataTransformer.getInjector().createChildInjector(modules);

		this.inputState = inputState;
	}

	public void run(final ViabilityModel model, final IMapperClass mapper, final Map<ShippingOption, VesselAssignmentType> shippingMap, @NonNull final IProgressMonitor monitor) {
		monitor.beginTask("Generate solutions", IProgressMonitor.UNKNOWN);

		try {
			try {

				@NonNull
				final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

				// Phase 1 : generate basic point-to-point break evens
				{
					final List<Future<Runnable>> futures = new LinkedList<>();

					for (final ViabilityRow row : model.getRows()) {
						row.getRhsResults().clear();
						row.getLhsResults().clear();

						ShippingOption shipping = row.getShipping();

						final VesselAssignmentType vesselAssignment = shippingMap.get(shipping);
						int vsi = -2;

						if (shipping instanceof ExistingCharterMarketOption) {
							final ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) shipping;
							vsi = ecmo.getSpotIndex();
						}

						final int vesselSpotIndex = vsi;

						final LoadSlot load;

						if (row.getBuyOption() != null) {
							load = mapper.getOriginal(row.getBuyOption());
							// } else if (row.getSellOption() != null) {
							// discharge = mapper.getOriginal(row.getSellOption());
						} else {
							continue;
						}

						for (final SpotMarket market : model.getMarkets()) {
							if (row.getBuyOption() != null) {

							}
							// if (row.getSellOption() != null) {
							// if (market instanceof DESPurchaseMarket) {
							// shipped = false;
							// load = mapper.getPurchaseMarketBreakEven(market,
							// YearMonth.from(discharge.getWindowStart()));
							// reference_slot = mapper.getPurchaseMarketOriginal(market,
							// YearMonth.from(discharge.getWindowStart()));
							// } else if (market instanceof FOBPurchasesMarket) {
							// if (discharge.isFOBSale()) {
							// shipped = false;
							// load = mapper.getPurchaseMarketBreakEven(market,
							// YearMonth.from(discharge.getWindowStart()));
							// reference_slot = mapper.getPurchaseMarketOriginal(market,
							// YearMonth.from(discharge.getWindowStart()));
							// } else {
							// shipped = true;
							// load = mapper.getPurchaseMarketBreakEven(market,
							// YearMonth.from(discharge.getWindowStart().minusMonths(1)));
							// reference_slot = mapper.getPurchaseMarketOriginal(market,
							// YearMonth.from(discharge.getWindowStart().minusMonths(1)));
							// }
							// } else {
							// continue;
							// }
							// be_target = load;
							// if (load.getPort() == null) {
							// timeZone = discharge.getPort().getLocation().getTimeZone();
							// } else {
							// timeZone = load.getPort().getLocation().getTimeZone();
							// }
							// }

							if (load == null) {
								continue;
							}

							// final boolean pShipped = shipped;
							// final LoadSlot pLoad = load;
							// final DischargeSlot pDischarge = discharge;
							// final Slot<?> pBE_Target = be_target;
							// final Slot<?> pReference_slot = reference_slot;
							// final String pTimeZone = timeZone;
							final Callable<Runnable> job = () -> {
								if (monitor.isCanceled()) {
									return null;
								}
								final ViabilityResult viabilityResult = AnalyticsFactory.eINSTANCE.createViabilityResult();
								viabilityResult.setTarget(market);
								final InternalResult ret = new InternalResult();
								DischargeSlot discharge = null;
								Slot be_target = null;
								Slot reference_slot = null;

								try {

									String timeZone = "UTC";
									for (int i = 0; i < 4; ++i) {

										boolean shipped = true;

										if (market instanceof FOBSalesMarket) {
											shipped = false;
											discharge = mapper.getSalesMarketBreakEven(market, YearMonth.from(load.getWindowStart()));
											reference_slot = mapper.getSalesMarketOriginal(market, YearMonth.from(load.getWindowStart()));
										} else if (market instanceof DESSalesMarket) {
											if (load.isDESPurchase()) {
												shipped = false;
												discharge = mapper.getSalesMarketBreakEven(market, YearMonth.from(load.getWindowStart()));
												reference_slot = mapper.getSalesMarketOriginal(market, YearMonth.from(load.getWindowStart()));
											} else {
												shipped = true;
												discharge = mapper.getSalesMarketBreakEven(market, YearMonth.from(load.getWindowStart().plusMonths(i)));
												reference_slot = mapper.getSalesMarketOriginal(market, YearMonth.from(load.getWindowStart().plusMonths(i)));
											}
										} else {
											continue;
										}
										if (discharge == null) {
											continue;
										}
										be_target = discharge;
										if (discharge.getPort() == null) {
											timeZone = load.getPort().getLocation().getTimeZone();
										} else {
											timeZone = discharge.getPort().getLocation().getTimeZone();
										}

										final InternalResult result = doIt(shipped, vesselAssignment, vesselSpotIndex, load, discharge, be_target, dataTransformer.getInitialSequences());
										ret.merge(result);
										if (!shipped) {
											// only one iteration.
											break;
										}
									}
									if (ret != null && ret.earliestTime != Integer.MAX_VALUE) {
										// result.setPrice(OptimiserUnitConvertor.convertToExternalPrice(price.getFirst()));
										viabilityResult.setEarliestETA(modelEntityMap.getDateFromHours(ret.earliestTime, timeZone).toLocalDate());
										viabilityResult.setLatestETA(modelEntityMap.getDateFromHours(ret.latestTime, timeZone).toLocalDate());
										viabilityResult.setEarliestVolume(OptimiserUnitConvertor.convertToExternalVolume(ret.earliestVolume));
										viabilityResult.setLatestVolume(OptimiserUnitConvertor.convertToExternalVolume(ret.latestVolume));
										viabilityResult.setEarliestPrice(OptimiserUnitConvertor.convertToExternalPrice(ret.earliestPrice));
										viabilityResult.setLatestPrice(OptimiserUnitConvertor.convertToExternalPrice(ret.latestPrice));
										// final Pair<Integer, Integer> referencePrice;
										// if (pReference_slot instanceof DischargeSlot) {
										// referencePrice = doIt(pShipped, vesselAssignment, pLoad, (DischargeSlot)
										// pReference_slot, pReference_slot, dataTransformer.getInitialSequences());
										// } else {
										// referencePrice = doIt(pShipped, vesselAssignment, (LoadSlot) pReference_slot,
										// pDischarge, pReference_slot, dataTransformer.getInitialSequences());
										// }
										// if (referencePrice != null) {
										// result.setReferencePrice(OptimiserUnitConvertor.convertToExternalPrice(referencePrice.getFirst()));
										// }

									}
									synchronized (row) {
										if (row.getBuyOption() != null) {
											row.getRhsResults().add(viabilityResult);
										} else {
											row.getLhsResults().add(viabilityResult);
										}
									}
									return null;
								} finally {
									monitor.worked(1);
								}
							};
							futures.add(executorService.submit(job));
						}
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

			final PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class);

			// Clean up thread-locals created in the scope object
			for (final Thread thread : threadCache.keySet()) {
				scope.exit(thread);
				threadCache.clear();
			}
		}
	}

	private InternalResult doIt(final boolean shipped, final VesselAssignmentType vesselAssignment, final int vesselSpotIndex, final LoadSlot load, final DischargeSlot discharge, final Slot target,
			final ISequences initialSequences) {
		final IResource resource;

		final IPortSlot a = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(load, IPortSlot.class);
		final IPortSlot b = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(discharge, IPortSlot.class);

		if (!shipped) {
			resource = SequenceHelper.getResource(dataTransformer, load.isDESPurchase() ? load : discharge);
			final IModifiableSequences solution = new ModifiableSequences(CollectionsUtil.makeArrayList(resource));
			SequenceHelper.addFOBDESSequence(solution, dataTransformer, load, discharge);
			assert solution != null;
			final ViabilitySandboxEvaluator evaluator = injector.getInstance(ViabilitySandboxEvaluator.class);
			final IPortSlot portSlot = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(target, IPortSlot.class);
			final InternalResult res = new InternalResult();
			res.merge(evaluator.evaluate(resource, solution, portSlot));
			return res;
		} else {
			if (vesselAssignment instanceof VesselAvailability) {
				resource = SequenceHelper.getResource(dataTransformer, (VesselAvailability) vesselAssignment);
			} else if (vesselAssignment instanceof CharterInMarket) {
				resource = SequenceHelper.getResource(dataTransformer, (CharterInMarket) vesselAssignment, vesselSpotIndex);
			} else {
				return null;
			}
			final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
			final List<ISequenceElement> cargoSegment = Lists.newArrayList(portSlotProvider.getElement(a), portSlotProvider.getElement(b));

			final InternalResult ret = new InternalResult();

			final IPortSlot portSlot = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(target, IPortSlot.class);

			final ViabilitySandboxEvaluator evaluator = injector.getInstance(ViabilitySandboxEvaluator.class);
			final ViabilityWindowTrimmer trimmer = injector.getInstance(ViabilityWindowTrimmer.class);
			final InsertCargoSequencesGenerator generator = injector.getInstance(InsertCargoSequencesGenerator.class);

			generator.generateOptions(initialSequences, cargoSegment, resource, trimmer, portSlot, (solution) -> {
				final SingleResult result = evaluator.evaluate(resource, solution, portSlot);
				ret.merge(result);
				return result != null;
			});
			return ret;
		}
	}
}
