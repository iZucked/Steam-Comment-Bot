/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.breakeven.BreakEvenTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scheduler.optimiser.insertion.SequencesUndoSpotHelper;

public class SolutionSetExporterUnit {

	private static final Logger LOG = LoggerFactory.getLogger(SolutionSetExporterUnit.class);

	public static IChainLink exportMultipleSolutions(final ChainBuilder chainBuilder, final int progressTicks, @NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge,
			final Supplier<AbstractSolutionSet> solutionSetFactory, final OptionalLong portfolioBreakEvenTarget) {

		final IChainLink link = new IChainLink() {

			@Override
			public @NonNull IMultiStateResult run(@NonNull final SequencesContainer initialSequences, @NonNull final IMultiStateResult inputState, @NonNull final IProgressMonitor monitor) {
				final List<NonNullPair<ISequences, Map<String, Object>>> solutions = inputState.getSolutions();

				solutions.add(0, initialSequences.getSequencesPair());
				monitor.beginTask("Export", solutions.size());

				final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
				Injector injector = dataTransformer.getInjector().createChildInjector();
				SequencesUndoSpotHelper spotUndoHelper = injector.getInstance(SequencesUndoSpotHelper.class);

				try {
					final AbstractSolutionSet plan = solutionSetFactory.get();
					boolean firstSolution = true;
					for (final NonNullPair<ISequences, Map<String, Object>> changeSet : solutions) {
						if (changeSet.getFirst() == null) {
							continue;
						}
						try {
							ISequences sequences = spotUndoHelper.undoSpotMarketSwaps(initialSequences.getSequences(), changeSet.getFirst());

							// Perform a portfolio break-even if requested.
							if (portfolioBreakEvenTarget.isPresent()) {

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

							final Schedule child_schedule = scenarioToOptimiserBridge.createSchedule(sequences, changeSet.getSecond());

							final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
							scheduleModel.setSchedule(child_schedule);

							// New spot slots etc will need to be contained here.
							for (final SlotAllocation a : child_schedule.getSlotAllocations()) {
								final Slot slot = a.getSlot();
								if (slot != null && slot.eContainer() == null) {
									plan.getExtraSlots().add(slot);
								}
							}

							final SolutionOption option = AnalyticsFactory.eINSTANCE.createSolutionOption();
							option.setScheduleModel(scheduleModel);

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

				} finally {
					monitor.done();
				}
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
}
