/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator.BreakEvenMode;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OtherPNL;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.breakeven.BreakEvenTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.TraderBasedInsertionHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleModelToScheduleSpecification;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.exceptions.InfeasibleSolutionException;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scheduler.optimiser.insertion.SequencesUndoSpotHelper;

public class SolutionSetExporterUnit {

	private static final Logger LOG = LoggerFactory.getLogger(SolutionSetExporterUnit.class);

	public static IChainLink exportMultipleSolutions(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge,
			final Supplier<AbstractSolutionSet> solutionSetFactory, final boolean dualPNLMode, final OptionalLong portfolioBreakEvenTarget, boolean addToModel) {

		final IChainLink link = new IChainLink() {
			@Override
			public @NonNull IMultiStateResult run(final LNGDataTransformer dt, @NonNull final SequencesContainer initialSequences, @NonNull final IMultiStateResult inputState,
					@NonNull final IProgressMonitor monitor) {
				// Use the real initial sequences rather then the one from the last "reset"
				final SequencesContainer container = new SequencesContainer(dt.getInitialResult().getBestSolution());
				SolutionSetExporterUnit.export(scenarioToOptimiserBridge, solutionSetFactory, dualPNLMode, portfolioBreakEvenTarget, container, inputState, monitor, addToModel);
				return inputState;
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}
		};
		if (chainBuilder != null) {
			chainBuilder.addLink(link);
		}
		return link;
	}

	public static void export(

			@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Supplier<AbstractSolutionSet> solutionSetFactory, final boolean dualPNLMode,
			final OptionalLong portfolioBreakEvenTarget, @NonNull final SequencesContainer initialSequences, @NonNull final IMultiStateResult inputState, @NonNull final IProgressMonitor monitor, boolean addToModel) {
		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = inputState.getSolutions();
		final ScheduleModelToScheduleSpecification scheduleTransformer = new ScheduleModelToScheduleSpecification();
		SequencesToChangeDescriptionTransformer changeTransformer = null;
		// ChangeModelToScheduleSpecification specificationTransformer = null;
		// build this on the raw transformed seq
		if (dualPNLMode) {
			changeTransformer = new SequencesToChangeDescriptionTransformer(scenarioToOptimiserBridge.getFullDataTransformer());
			// specificationTransformer = new
			// ChangeModelToScheduleSpecification(scenarioToOptimiserBridge.getScenarioDataProvider(),
			// null);
			final ISequences s = scenarioToOptimiserBridge.getTransformedOriginalRawSequences(initialSequences.getSequencesPair().getFirst());
			changeTransformer.prepareFromBase(s);
		}
		solutions.add(0, initialSequences.getSequencesPair());
		monitor.beginTask("Export", solutions.size());

		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		final Injector injector = dataTransformer.getInjector().createChildInjector();
		final SequencesUndoSpotHelper spotUndoHelper = injector.getInstance(SequencesUndoSpotHelper.class);

		try {

			TraderBasedInsertionHelper helper = null;
			if (dualPNLMode) {
				helper = new TraderBasedInsertionHelper(scenarioToOptimiserBridge.getScenarioDataProvider(), scenarioToOptimiserBridge);
				helper.prepareFromBase(solutions.get(0).getFirst());
			}

			final AbstractSolutionSet plan = solutionSetFactory.get();
			plan.setHasDualModeSolutions(dualPNLMode);
			boolean firstSolution = true;
			for (final NonNullPair<ISequences, Map<String, Object>> changeSet : solutions) {
				if (changeSet.getFirst() == null) {
					continue;
				}
				try {
					final ISequences sequences = spotUndoHelper.undoSpotMarketSwaps(initialSequences.getSequences(), changeSet.getFirst());

					// Perform a portfolio break-even if requested.
					if (portfolioBreakEvenTarget.isPresent()) {
						plan.setPortfolioBreakEvenMode(true);

						@NonNull
						final Collection<@NonNull String> hints = new LinkedList<>(dataTransformer.getHints());

						hints.add(LNGTransformerHelper.HINT_DISABLE_CACHES);

						final BreakEvenOptimisationStage stageSettings = ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage();
						stageSettings.setName("portfolio-be");
						stageSettings.setTargetProfitAndLoss(portfolioBreakEvenTarget.getAsLong());

						final BreakEvenTransformerUnit t = new BreakEvenTransformerUnit(dataTransformer, dataTransformer.getUserSettings(), stageSettings, changeSet.getFirst(),
								new MultiStateResult(sequences, new HashMap<>()), hints);

						t.run(new NullProgressMonitor());
					}

					final Schedule childSchedule = scenarioToOptimiserBridge.createSchedule(sequences, changeSet.getSecond());

					final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
					scheduleModel.setSchedule(childSchedule);

					// New spot slots etc will need to be contained here.
					for (final SlotAllocation a : childSchedule.getSlotAllocations()) {
						final Slot<?> slot = a.getSlot();
						if (slot != null && slot.eContainer() == null) {
							plan.getExtraSlots().add(slot);
						}
					}
					for (final Sequence seq : childSchedule.getSequences()) {
						final CharterInMarket cm = seq.getCharterInMarket();
						if (cm != null && cm.eContainer() == null) {
							plan.getExtraCharterInMarkets().add(cm);
						}
						final VesselAvailability va = seq.getVesselAvailability();
						if (va != null && va.eContainer() == null) {
							plan.getExtraVesselAvailabilities().add(va);
						}
						for (final Event evt : seq.getEvents()) {
							if (evt instanceof final VesselEventVisit vesselEventVisit) {
								final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
								if (vesselEvent != null && vesselEvent.eContainer() == null) {
									plan.getExtraVesselEvents().add(vesselEvent);
								}
							}
						}
					}

					final SolutionOption option = helper == null ? AnalyticsFactory.eINSTANCE.createSolutionOption() : AnalyticsFactory.eINSTANCE.createDualModeSolutionOption();
					if (helper != null) {
						final ISequences s = scenarioToOptimiserBridge.getTransformedOriginalRawSequences(changeSet.getFirst());
						option.setChangeDescription(changeTransformer.generateChangeDescription(s));
						// try {
						// Pair<ScheduleSpecification, ExtraDataProvider> p =
						// specificationTransformer.buildScheduleSpecification(option.getChangeDescription());
						// option.setScheduleSpecification(p.getFirst());
						// plan.getExtraSlots().addAll(p.getSecond().extraLoads);
						// plan.getExtraSlots().addAll(p.getSecond().extraDischarges);
						// } catch (Throwable t) {
						// changeTransformer.generateChangeDescription(changeSet.getFirst());
						// }
					}
					option.setScheduleModel(scheduleModel);
					option.setScheduleSpecification(scheduleTransformer.generateScheduleSpecifications(scheduleModel));

					if (!firstSolution && helper != null) {
						helper.processSolution((DualModeSolutionOption) option, sequences, scheduleModel);
					}
					if (firstSolution) {
						firstSolution = false;
						plan.setBaseOption(option);
					} else {
						plan.getOptions().add(option);
					}
				} catch (final Exception e) {
					throw new RuntimeException("Unable to store scenario: " + e.getMessage(), e);
				}

				monitor.worked(1);
			}
			if (helper != null) {
				final UserSettings userSettings = EMFCopier.copy(plan.getUserSettings());
				final EditingDomain originalEditingDomain = scenarioToOptimiserBridge.getScenarioDataProvider().getEditingDomain();
				helper.generateResults(scenarioToOptimiserBridge.getScenarioInstance(), userSettings, originalEditingDomain, plan);
			}
			if (addToModel) {
				RunnerHelper.syncExecDisplayOptional(() -> {
					final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioToOptimiserBridge.getScenarioDataProvider());
					analyticsModel.getOptimisations().add(plan);
					final ScenarioInstance instance = scenarioToOptimiserBridge.getScenarioInstance();
					if (instance != null) {
						final ModelReference sharedReference = SSDataManager.Instance.getModelRecord(instance).getSharedReference();
						if (sharedReference != null) {
							sharedReference.setDirty();
						}
					}
				});
			}

		} finally {
			monitor.done();
		}
	}

	public static class Util<T extends SolutionOption> {
		private TraderBasedInsertionHelper tradingHelper = null;
		private SequencesToChangeDescriptionTransformer changeTransformer;
		private final LNGScenarioToOptimiserBridge bridge;
		private final Supplier<@NonNull T> optionFactory;
		private final boolean dualPNLMode;
		private BreakEvenMode breakEvenMode;
		private Long targetProfitAndLoss;
		final ScheduleModelToScheduleSpecification scheduleTransformer = new ScheduleModelToScheduleSpecification();
		private final UserSettings userSettings;
		private final boolean enableChangeDescription;

		public Util(final LNGScenarioToOptimiserBridge bridge, final UserSettings userSettings, final Supplier<@NonNull T> optionFactory, final boolean dualPNLMode,
				final boolean enableChangeDescription) {
			this.bridge = bridge;
			this.userSettings = userSettings;
			this.optionFactory = optionFactory;
			this.dualPNLMode = dualPNLMode;
			this.enableChangeDescription = enableChangeDescription;
		}

		public @Nullable T useAsBaseSolution(final ScheduleSpecification scheduleSpecification) {

			final ISequences base = specificationToSequences(scheduleSpecification);
			return useAsBaseSolution(base, scheduleSpecification);
		}

		private @NonNull ISequences specificationToSequences(final @NonNull ScheduleSpecification scheduleSpecification) {
			final Injector injector = bridge.getFullDataTransformer().getInjector();
			final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
			final ISequences base = transformer.createSequences(scheduleSpecification, bridge.getFullDataTransformer(), false);
			return base;
		}

		public @Nullable T useAsBaseSolution(final @NonNull ISequences sequences) {
			return useAsBaseSolution(sequences, null);
		}

		public @Nullable T useAsBaseSolution(@NonNull final ISequences sequences, @Nullable ScheduleSpecification scheduleSpecification) {
			if (dualPNLMode) {
				tradingHelper = new TraderBasedInsertionHelper(bridge.getScenarioDataProvider(), bridge);
				tradingHelper.prepareFromBase(sequences);
			}
			if (enableChangeDescription) {
				changeTransformer = new SequencesToChangeDescriptionTransformer(bridge.getFullDataTransformer());
				changeTransformer.prepareFromBase(sequences);
			} else {
				changeTransformer = null;
			}

			@Nullable
			T resultSet = null;
			try {

				final Schedule schedule = bridge.createSchedule(sequences, Collections.emptyMap(), null);
				schedule.getUnusedElements().removeIf(SpotSlot.class::isInstance);

				final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
				scheduleModel.setSchedule(schedule);
				scheduleModel.setDirty(false);
				if (scheduleSpecification == null) {
					scheduleSpecification = scheduleTransformer.generateScheduleSpecifications(scheduleModel);

				}
				resultSet = optionFactory.get();
				resultSet.setScheduleModel(scheduleModel);
				resultSet.setScheduleSpecification(scheduleSpecification);

				if (breakEvenMode == BreakEvenMode.PORTFOLIO && targetProfitAndLoss == null) {
					targetProfitAndLoss = ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule);
				}
			} catch (final InfeasibleSolutionException e) {
				// Ignore these errors. A typical cause of this error is sandbox forced route
				// selection with e.g. Panama when there is no Panama distance.
			} catch (final Throwable e) {
				e.printStackTrace();
				return null;
			}
			return resultSet;
		}

		public @Nullable T computeOption(@NonNull final ISequences sequences, @Nullable ScheduleSpecification scheduleSpecification) {

			@Nullable
			T resultSet = null;
			try {

				final Schedule schedule = bridge.createSchedule(sequences, Collections.emptyMap(), targetProfitAndLoss);
				// Remove open spot slots.
				schedule.getOpenSlotAllocations().removeIf(sa -> sa.getSlot() instanceof SpotSlot);
				schedule.getUnusedElements().removeIf(SpotSlot.class::isInstance);

				final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
				scheduleModel.setSchedule(schedule);
				scheduleModel.setDirty(false);

				if (scheduleSpecification == null) {
					scheduleSpecification = scheduleTransformer.generateScheduleSpecifications(scheduleModel);
				}

				resultSet = optionFactory.get();
				resultSet.setScheduleModel(scheduleModel);
				resultSet.setScheduleSpecification(scheduleSpecification);

				// Convert from period to full sequences if needed
				final ISequences seq = bridge.getTransformedOriginalRawSequences(sequences);
				if (tradingHelper != null) {
					tradingHelper.processSolution((DualModeSolutionOption) resultSet, seq, scheduleModel);
				}
				if (changeTransformer != null) {
					resultSet.setChangeDescription(changeTransformer.generateChangeDescription(seq));
				}
			} catch (final InfeasibleSolutionException e) {
				// Ignore these errors. A typical cause of this error is sandbox forced route
				// selection with e.g. Panama when there is no Panama distance.
			} catch (final Throwable e) {
				e.printStackTrace();

				return null;
			}
			return resultSet;
		}

		public @Nullable T computeOption(@NonNull final ISequences sequences) {
			return computeOption(sequences, null);
		}

		public @Nullable T computeOption(@NonNull final ScheduleSpecification scheduleSpecification) {
			final @NonNull ISequences sequences = specificationToSequences(scheduleSpecification);
			return computeOption(sequences, scheduleSpecification);
		}

		public void applyPostTasks(final @NonNull AbstractSolutionSet solutionSet) {

			if (tradingHelper != null) {
				final UserSettings copyUserSettings = EMFCopier.copy(userSettings);
				final EditingDomain originalEditingDomain = bridge.getScenarioDataProvider().getEditingDomain();
				tradingHelper.generateResults(bridge.getScenarioInstance(), copyUserSettings, originalEditingDomain, solutionSet);
			}
		}

		public void setBreakEvenMode(final BreakEvenMode breakEvenMode) {
			this.breakEvenMode = breakEvenMode;
		}
	}

	public static void convertToSimpleResult(final AbstractSolutionSet result, final boolean dualPNLMode) {
		if (dualPNLMode) {
			final OtherPNL basePNL = ScheduleFactory.eINSTANCE.createOtherPNL();
			{
				ScheduleModelKPIUtils.updateOtherPNL(basePNL, result.getBaseOption().getScheduleModel().getSchedule(), ScheduleModelKPIUtils.Mode.INCREMENT);
			}

			final long a = ScheduleModelKPIUtils.getScheduleProfitAndLoss(result.getBaseOption().getScheduleModel().getSchedule());
			for (final SolutionOption option : result.getOptions()) {

				if (option instanceof final DualModeSolutionOption dualOption) {

					final OtherPNL optionPNL = ScheduleFactory.eINSTANCE.createOtherPNL();
					ScheduleModelKPIUtils.updateOtherPNL(optionPNL, option.getScheduleModel().getSchedule(), ScheduleModelKPIUtils.Mode.INCREMENT);
					final long b = ScheduleModelKPIUtils.getScheduleProfitAndLoss(option.getScheduleModel().getSchedule());

					final OtherPNL copyBasePNL = EMFCopier.copy(basePNL);
					{
						final Schedule s = dualOption.getMicroBaseCase().getScheduleModel().getSchedule();
						ScheduleModelKPIUtils.updateOtherPNL(copyBasePNL, s, ScheduleModelKPIUtils.Mode.DECREMENT);
						s.setOtherPNL(copyBasePNL);
						final long c = ScheduleModelKPIUtils.getScheduleProfitAndLoss(s);

						assert c == a;
					}
					{
						final Schedule s = dualOption.getMicroTargetCase().getScheduleModel().getSchedule();
						ScheduleModelKPIUtils.updateOtherPNL(optionPNL, s, ScheduleModelKPIUtils.Mode.DECREMENT);
						s.setOtherPNL(optionPNL);
						final long c = ScheduleModelKPIUtils.getScheduleProfitAndLoss(s);

						assert c == b;
					}

					// Replace existing schedule with a dummy one
					{
						final Schedule tmp = ScheduleFactory.eINSTANCE.createSchedule();

						final OtherPNL optionPNL1 = ScheduleFactory.eINSTANCE.createOtherPNL();
						ScheduleModelKPIUtils.updateOtherPNL(optionPNL1, option.getScheduleModel().getSchedule(), ScheduleModelKPIUtils.Mode.INCREMENT);
						tmp.setOtherPNL(optionPNL1);
						option.getScheduleModel().setSchedule(tmp);
						final long d = ScheduleModelKPIUtils.getScheduleProfitAndLoss(tmp);

						assert d == b;
					}

				}
			}
		}
	}
}
