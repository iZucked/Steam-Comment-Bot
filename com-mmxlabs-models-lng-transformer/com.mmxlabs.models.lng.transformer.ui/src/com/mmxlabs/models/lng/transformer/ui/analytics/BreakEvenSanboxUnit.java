/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.JobExecutor;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
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
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.impl.ChangeablePriceCalculator;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveGeneratorModule;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class BreakEvenSanboxUnit {

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	private @NonNull JobExecutorFactory executorService;

	private IScenarioDataProvider sdp;

	@SuppressWarnings("null")
	public BreakEvenSanboxUnit(@NonNull final IScenarioDataProvider sdp, @NonNull final LNGDataTransformer dataTransformer, @NonNull final String phase, @NonNull final UserSettings userSettings,
			@NonNull final ConstraintAndFitnessSettings constainAndFitnessSettings, @NonNull final JobExecutorFactory executorService, @NonNull final ISequences initialSequences,
			@NonNull final IMultiStateResult inputState, @NonNull final Collection<String> hints) {
		this.sdp = sdp;
		this.dataTransformer = dataTransformer;

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
				install(new MoveGeneratorModule(true));
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
			private BreakEvenSandboxEvaluator providePerThreadOptimiser(@NonNull final Injector injector) {

				BreakEvenSandboxEvaluator optimiser = new BreakEvenSandboxEvaluator();
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

	public void run(final BreakEvenAnalysisModel model, final IMapperClass mapper, final Map<ShippingOption, VesselAssignmentType> shippingMap, @NonNull final IProgressMonitor monitor) {
		monitor.beginTask("Generate solutions", IProgressMonitor.UNKNOWN);

		try (JobExecutor jobExecutor = executorService.begin()) {

			EditingDomain ed = sdp.getEditingDomain();

			final @NonNull ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			CompoundCommand cmd = new CompoundCommand("Run break-evens");

			Map<BreakEvenAnalysisRow, BreakEvenAnalysisResultSet> rowToResultSetMap = new HashMap<>();
			// Phase 1 : generate basic point-to-point break evens
			{
				final List<Future<Runnable>> futures = new LinkedList<>();

				for (final BreakEvenAnalysisRow row : model.getRows()) {
					final BreakEvenAnalysisResultSet resultSet = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResultSet();

					rowToResultSetMap.put(row, resultSet);

					cmd.append(SetCommand.create(ed, row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON, SetCommand.UNSET_VALUE));
					cmd.append(SetCommand.create(ed, row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON, SetCommand.UNSET_VALUE));
					cmd.append(SetCommand.create(ed, row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS, SetCommand.UNSET_VALUE));
					cmd.append(SetCommand.create(ed, row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS, SetCommand.UNSET_VALUE));

					if (row.getBuyOption() != null) {
						cmd.append(AddCommand.create(ed, row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS, resultSet));
					} else if (row.getSellOption() != null) {
						cmd.append(AddCommand.create(ed, row, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS, resultSet));
					}

					final VesselAssignmentType vesselAssignment = shippingMap.get(row.getShipping());

					LoadSlot load = null;
					DischargeSlot discharge = null;
					Slot<?> be_target = null;
					Slot<?> reference_slot = null;

					if (row.getBuyOption() != null) {
						load = mapper.getOriginal(row.getBuyOption());
					} else if (row.getSellOption() != null) {
						discharge = mapper.getOriginal(row.getSellOption());
					} else {
						continue;
					}

					String timeZone = "UTC";
					boolean shipped = true;
					for (final SpotMarket market : model.getMarkets()) {
						if (row.getBuyOption() != null) {
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
									discharge = mapper.getSalesMarketBreakEven(market, YearMonth.from(load.getWindowStart().plusMonths(1)));
									reference_slot = mapper.getSalesMarketOriginal(market, YearMonth.from(load.getWindowStart().plusMonths(1)));
								}
							} else {
								continue;
							}
							be_target = discharge;
							if (discharge.getPort() == null) {
								timeZone = load.getPort().getLocation().getTimeZone();
							} else {
								timeZone = discharge.getPort().getLocation().getTimeZone();
							}
						}
						if (row.getSellOption() != null) {
							if (market instanceof DESPurchaseMarket) {
								shipped = false;
								load = mapper.getPurchaseMarketBreakEven(market, YearMonth.from(discharge.getWindowStart()));
								reference_slot = mapper.getPurchaseMarketOriginal(market, YearMonth.from(discharge.getWindowStart()));
							} else if (market instanceof FOBPurchasesMarket) {
								if (discharge.isFOBSale()) {
									shipped = false;
									load = mapper.getPurchaseMarketBreakEven(market, YearMonth.from(discharge.getWindowStart()));
									reference_slot = mapper.getPurchaseMarketOriginal(market, YearMonth.from(discharge.getWindowStart()));
								} else {
									shipped = true;
									load = mapper.getPurchaseMarketBreakEven(market, YearMonth.from(discharge.getWindowStart().minusMonths(1)));
									reference_slot = mapper.getPurchaseMarketOriginal(market, YearMonth.from(discharge.getWindowStart().minusMonths(1)));
								}
							} else {
								continue;
							}
							be_target = load;
							if (load.getPort() == null) {
								timeZone = discharge.getPort().getLocation().getTimeZone();
							} else {
								timeZone = load.getPort().getLocation().getTimeZone();
							}
						}

						if (load == null || discharge == null) {
							continue;
						}

						final boolean pShipped = shipped;
						final LoadSlot pLoad = load;
						final DischargeSlot pDischarge = discharge;
						final Slot<?> pBE_Target = be_target;
						final Slot<?> pReference_slot = reference_slot;
						final String pTimeZone = timeZone;
						final Callable<Runnable> job = () -> {
							if (monitor.isCanceled()) {
								return null;
							}
							try {
								final Pair<Integer, Integer> price = doIt(pShipped, vesselAssignment, pLoad, pDischarge, pBE_Target, null);
								if (price != null) {
									final BreakEvenAnalysisResult result = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResult();
									result.setTarget(market);
									result.setPrice(OptimiserUnitConvertor.convertToExternalPrice(price.getFirst()));

									result.setEta(modelEntityMap.getDateFromHours(price.getSecond(), pTimeZone).toLocalDate());
									final Pair<Integer, Integer> referencePrice;
									if (pReference_slot instanceof DischargeSlot) {
										referencePrice = doIt(pShipped, vesselAssignment, pLoad, (DischargeSlot) pReference_slot, pReference_slot, null);
									} else {
										referencePrice = doIt(pShipped, vesselAssignment, (LoadSlot) pReference_slot, pDischarge, pReference_slot, null);
									}
									if (referencePrice != null) {
										result.setReferencePrice(OptimiserUnitConvertor.convertToExternalPrice(referencePrice.getFirst()));
									}
									return () -> resultSet.getResults().add(result);
								} else {
									return null;
								}
							} finally {
								monitor.worked(1);
							}
						};
						futures.add(jobExecutor.submit(job));
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
						Thread.currentThread().interrupt();
					} catch (final ExecutionException e) {
						e.printStackTrace();
					}
				}
				if (monitor.isCanceled()) {
					return;
				}
			}

			// Phase 2 relative BE
			{
				final List<Future<Runnable>> futures = new LinkedList<>();

				for (final BreakEvenAnalysisRow targetRow : model.getRows()) {
					if (targetRow.getBuyOption() == null && targetRow.getSellOption() == null) {
						continue;
					}

					for (final BreakEvenAnalysisRow rowToBaseOn : model.getRows()) {
						if (targetRow == rowToBaseOn) {
							// Skip identity
							continue;
						}

						if (rowToBaseOn.getBuyOption() == null && rowToBaseOn.getSellOption() == null) {
							continue;
						}

						if ((targetRow.getBuyOption() == null) != (rowToBaseOn.getBuyOption() == null)) {
							continue;
						}
						if ((targetRow.getSellOption() == null) != (rowToBaseOn.getSellOption() == null)) {
							continue;
						}

						final BreakEvenAnalysisResultSet resultSetToBaseOn = rowToResultSetMap.get(rowToBaseOn);
						assert resultSetToBaseOn != null;
						assert resultSetToBaseOn.getBasedOn() == null;

						for (final BreakEvenAnalysisResult resultBasedOn : resultSetToBaseOn.getResults()) {
							final BreakEvenAnalysisResultSet resultSetB = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResultSet();
							resultSetB.setBasedOn(resultBasedOn);

							if (targetRow.getBuyOption() != null) {
								cmd.append(AddCommand.create(ed, targetRow, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS, resultSetB));
							} else {
								cmd.append(AddCommand.create(ed, targetRow, AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS, resultSetB));
							}

							final Callable<Runnable> job = () -> {
								if (monitor.isCanceled()) {
									return null;
								}
								try {

									final List<Runnable> multiRunnable = new LinkedList<>();

									// Part 1 Calculate b/e for source element
									final VesselAssignmentType vesselAssignment = shippingMap.get(targetRow.getShipping());

									Pair<Integer, Integer> source_be_price;
									{

										LoadSlot load = null;
										DischargeSlot discharge = null;
										Slot<?> be_target = null;
										final SpotMarket market = (SpotMarket) resultBasedOn.getTarget();
										boolean shipped = true;

										if (targetRow.getBuyOption() != null) {
											load = mapper.getBreakEven(targetRow.getBuyOption());

											if (market instanceof FOBSalesMarket) {
												shipped = false;
												discharge = mapper.getSalesMarketChangable(market, YearMonth.from(load.getWindowStart()));
											} else if (market instanceof DESSalesMarket) {
												if (load.isDESPurchase()) {
													shipped = false;
													discharge = mapper.getSalesMarketChangable(market, YearMonth.from(load.getWindowStart()));
												} else {
													shipped = true;
													discharge = mapper.getSalesMarketChangable(market, YearMonth.from(load.getWindowStart().plusMonths(1)));
												}
											} else {
												assert false;
											}
											be_target = load;

										} else if (targetRow.getSellOption() != null) {
											discharge = mapper.getBreakEven(targetRow.getSellOption());

											if (market instanceof DESPurchaseMarket) {
												shipped = false;
												load = mapper.getPurchaseMarketChangable(market, YearMonth.from(discharge.getWindowStart()));
											} else if (market instanceof FOBPurchasesMarket) {
												if (discharge.isFOBSale()) {
													shipped = false;
													load = mapper.getPurchaseMarketChangable(market, YearMonth.from(discharge.getWindowStart()));
												} else {
													shipped = true;
													load = mapper.getPurchaseMarketChangable(market, YearMonth.from(discharge.getWindowStart().minusMonths(1)));
												}
											} else {
												assert false;
											}
											be_target = discharge;
										} else {
											assert false;
										}
										if (load == null || discharge == null) {
											throw new IllegalStateException();
										}
										final Slot<?> pbe_target = be_target;
										source_be_price = doIt(shipped, vesselAssignment, load, discharge, be_target, (o_load, o_discharge) -> {
											if (pbe_target instanceof LoadSlot) {
												((ChangeablePriceCalculator) o_discharge.getDischargePriceCalculator())
														.setPrice(OptimiserUnitConvertor.convertToInternalPrice(resultBasedOn.getPrice()));
											} else {
												((ChangeablePriceCalculator) o_load.getLoadPriceCalculator()).setPrice(OptimiserUnitConvertor.convertToInternalPrice(resultBasedOn.getPrice()));
											}

										});
									}

									if (source_be_price == null) {
										return null;
									}
									multiRunnable.add(() -> resultSetB.setPrice(OptimiserUnitConvertor.convertToExternalPrice(source_be_price.getFirst())));

									// Part 2 - loop over all markets and generate new b/e
									{
										LoadSlot load = null;
										DischargeSlot discharge = null;
										Slot<?> be_target = null;
										Slot<?> reference_slot = null;

										if (targetRow.getBuyOption() != null) {
											load = mapper.getChangable(targetRow.getBuyOption());
										} else if (targetRow.getSellOption() != null) {
											discharge = mapper.getChangable(targetRow.getSellOption());
										} else {
											assert false;
										}

										String timeZone;
										boolean shipped;
										for (final SpotMarket market : model.getMarkets()) {
											if (targetRow.getBuyOption() != null) {
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
														discharge = mapper.getSalesMarketBreakEven(market, YearMonth.from(load.getWindowStart().plusMonths(1)));
														reference_slot = mapper.getSalesMarketOriginal(market, YearMonth.from(load.getWindowStart().plusMonths(1)));
													}
												} else {
													continue;
												}
												be_target = discharge;
												if (discharge.getPort() == null) {
													timeZone = load.getPort().getLocation().getTimeZone();
												} else {
													timeZone = discharge.getPort().getLocation().getTimeZone();
												}
											} else if (targetRow.getSellOption() != null) {
												if (market instanceof DESPurchaseMarket) {
													shipped = false;
													load = mapper.getPurchaseMarketBreakEven(market, YearMonth.from(discharge.getWindowStart()));
													reference_slot = mapper.getPurchaseMarketOriginal(market, YearMonth.from(discharge.getWindowStart()));
												} else if (market instanceof FOBPurchasesMarket) {
													if (discharge.isFOBSale()) {
														shipped = false;
														load = mapper.getPurchaseMarketBreakEven(market, YearMonth.from(discharge.getWindowStart()));
														reference_slot = mapper.getPurchaseMarketOriginal(market, YearMonth.from(discharge.getWindowStart()));
													} else {
														shipped = true;
														load = mapper.getPurchaseMarketBreakEven(market, YearMonth.from(discharge.getWindowStart().minusMonths(1)));
														reference_slot = mapper.getPurchaseMarketOriginal(market, YearMonth.from(discharge.getWindowStart().minusMonths(1)));
													}
												} else {
													continue;
												}
												be_target = load;
												if (load.getPort() == null) {
													timeZone = discharge.getPort().getLocation().getTimeZone();
												} else {
													timeZone = load.getPort().getLocation().getTimeZone();
												}
											} else {
												continue;
											}

											if (load == null || discharge == null) {
												continue;
											}

											final Slot<?> pbe_target = be_target;
											int internal_be_price = source_be_price.getFirst();
											final Pair<Integer, Integer> price = doIt(shipped, vesselAssignment, load, discharge, be_target, (o_load, o_discharge) -> {
												if (pbe_target instanceof LoadSlot) {
													((ChangeablePriceCalculator) o_discharge.getDischargePriceCalculator()).setPrice(internal_be_price);
												} else {
													((ChangeablePriceCalculator) o_load.getLoadPriceCalculator()).setPrice(internal_be_price);
												}

											});
											if (price != null) {
												final BreakEvenAnalysisResult result = AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisResult();
												result.setTarget(market);
												result.setPrice(OptimiserUnitConvertor.convertToExternalPrice(price.getFirst()));

												result.setEta(modelEntityMap.getDateFromHours(price.getSecond(), timeZone).toLocalDate());
												final Pair<Integer, Integer> referencePrice;
												if (reference_slot instanceof DischargeSlot) {
													referencePrice = doIt(shipped, vesselAssignment, load, (DischargeSlot) reference_slot, reference_slot, null);
												} else {
													referencePrice = doIt(shipped, vesselAssignment, (LoadSlot) reference_slot, discharge, reference_slot, null);
												}
												if (referencePrice != null) {
													result.setReferencePrice(OptimiserUnitConvertor.convertToExternalPrice(referencePrice.getFirst()));
												}
												multiRunnable.add(() -> resultSetB.getResults().add(result));
											}
										}
									}
									return () -> multiRunnable.forEach(Runnable::run);
								} finally {
									monitor.worked(1);
								}
							};
							futures.add(jobExecutor.submit(job));
						}
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
						Thread.currentThread().interrupt();
					} catch (final ExecutionException e) {
						e.printStackTrace();
					}
				}
				if (monitor.isCanceled()) {
					return;
				}
			}

			ed.getCommandStack().execute(cmd);

		} finally {
			monitor.done();
		}
	}

	private Pair<Integer, Integer> doIt(final boolean shipped, final VesselAssignmentType vesselAssignment, final LoadSlot load, final DischargeSlot discharge, final Slot<?> target,
			final BiConsumer<LoadOption, DischargeOption> action) {
		final IResource resource;

		if (action != null) {
			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final IPortSlot a = modelEntityMap.getOptimiserObjectNullChecked(load, IPortSlot.class);
			final IPortSlot b = modelEntityMap.getOptimiserObjectNullChecked(discharge, IPortSlot.class);

			action.accept((LoadOption) a, (DischargeOption) b);
		}
		IModifiableSequences solution = null;
		if (!shipped) {
			resource = SequenceHelper.getResource(dataTransformer, load.isDESPurchase() ? load : discharge);
			solution = new ModifiableSequences(CollectionsUtil.makeArrayList(resource));
			SequenceHelper.addFOBDESSequence(solution, dataTransformer, load, discharge);
		} else if (vesselAssignment instanceof VesselCharter) {
			resource = SequenceHelper.getResource(dataTransformer, (VesselCharter) vesselAssignment);
			solution = new ModifiableSequences(CollectionsUtil.makeArrayList(resource));
			SequenceHelper.addSequence(solution, dataTransformer.getInjector(), (VesselCharter) vesselAssignment, load, discharge);
		} else if (vesselAssignment instanceof CharterInMarket) {
			resource = SequenceHelper.getResource(dataTransformer, (CharterInMarket) vesselAssignment, -1);
			solution = new ModifiableSequences(CollectionsUtil.makeArrayList(resource));
			SequenceHelper.addSequence(solution, dataTransformer.getInjector(), (CharterInMarket) vesselAssignment, -1, load, discharge);
		} else {
			return null;
		}
		assert solution != null;
		final BreakEvenSandboxEvaluator evaluator = injector.getInstance(BreakEvenSandboxEvaluator.class);
		final IPortSlot portSlot = dataTransformer.getModelEntityMap().getOptimiserObjectNullChecked(target, IPortSlot.class);
		return evaluator.evaluate(resource, solution, portSlot, null, 0);
	}
}
