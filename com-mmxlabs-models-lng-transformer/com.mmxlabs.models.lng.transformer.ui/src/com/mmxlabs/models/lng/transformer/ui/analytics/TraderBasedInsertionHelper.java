/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ChangeModelToSandboxScheduleSpecification;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.models.lng.transformer.ui.common.SequencesToChangeDescriptionTransformer;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

// TODO: 
public class TraderBasedInsertionHelper {
	private final ScheduleSpecificationHelper scheduleSpecificationHelper;
	private final IScenarioDataProvider scenarioDataProvider;
	private final @NonNull LNGDataTransformer dataTransformer;
	private SequencesToChangeDescriptionTransformer sequencesToChangeDescriptionTransformer;

	public TraderBasedInsertionHelper(final IScenarioDataProvider scenarioDataProvider, final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		this.scenarioDataProvider = scenarioDataProvider;
		this.scheduleSpecificationHelper = new ScheduleSpecificationHelper(scenarioDataProvider);
		// May need full transformer (non-period) better API names
		this.dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		this.sequencesToChangeDescriptionTransformer = new SequencesToChangeDescriptionTransformer(scenarioToOptimiserBridge.getDataTransformer());
	}

	public void prepareFromBase(@NonNull final ISequences baseRawSequences) {
		sequencesToChangeDescriptionTransformer.prepareFromBase(baseRawSequences);
	}

	public void processSolution(final DualModeSolutionOption option, @NonNull final ISequences rawSequences, final ScheduleModel scheduleModel) {
		final ChangeDescription changeDescription = sequencesToChangeDescriptionTransformer.generateChangeDescription(rawSequences);

		ChangeModelToSandboxScheduleSpecification builder = new ChangeModelToSandboxScheduleSpecification();
		final Pair<Pair<ScheduleSpecification, ExtraDataProvider>, Pair<ScheduleSpecification, ExtraDataProvider>> t = builder.generateScheduleSpecifications(
				scenarioDataProvider.getTypedScenario(LNGScenarioModel.class), ScenarioModelUtil.getScheduleModel(scenarioDataProvider).getSchedule(), scheduleModel.getSchedule(), changeDescription);

		scheduleSpecificationHelper.processExtraDataProvider(t.getFirst().getSecond());
		scheduleSpecificationHelper.processExtraDataProvider(t.getSecond().getSecond());

		final BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>> r = (bridge, injector) -> {
			final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
			final ISequences base = transformer.createSequences(t.getFirst().getFirst(), bridge.getDataTransformer(), false);
			final ISequences target = transformer.createSequences(t.getSecond().getFirst(), bridge.getDataTransformer(), false);

			List<Supplier<Command>> commandSuppliers = new LinkedList<>();
			try {
				final Schedule base_schedule = bridge.createSchedule(base, Collections.emptyMap());
				final ScheduleModel scheduleModel2 = ScheduleFactory.eINSTANCE.createScheduleModel();
				final SolutionOptionMicroCase microCase = AnalyticsFactory.eINSTANCE.createSolutionOptionMicroCase();
				microCase.setScheduleSpecification(t.getFirst().getFirst());
				microCase.setScheduleModel(scheduleModel2);
				microCase.getExtraVesselCharters().addAll(t.getFirst().getSecond().extraVesselCharters);
				microCase.getCharterInMarketOverrides().addAll(t.getFirst().getSecond().extraCharterInMarketOverrides);

				scheduleModel2.setSchedule(base_schedule);

				commandSuppliers.add(() -> SetCommand.create(scenarioDataProvider.getEditingDomain(), option, AnalyticsPackage.Literals.DUAL_MODE_SOLUTION_OPTION__MICRO_BASE_CASE, microCase));

			} catch (final Throwable e) {

			}
			try {
				final Schedule schedule = bridge.createSchedule(target, Collections.emptyMap());
				final ScheduleModel scheduleModel2 = ScheduleFactory.eINSTANCE.createScheduleModel();
				scheduleModel2.setSchedule(schedule);

				final SolutionOptionMicroCase microCase = AnalyticsFactory.eINSTANCE.createSolutionOptionMicroCase();
				microCase.setScheduleSpecification(t.getSecond().getFirst());
				microCase.setScheduleModel(scheduleModel2);
				microCase.getExtraVesselCharters().addAll(t.getSecond().getSecond().extraVesselCharters);
				microCase.getCharterInMarketOverrides().addAll(t.getSecond().getSecond().extraCharterInMarketOverrides);

				commandSuppliers.add(() -> SetCommand.create(scenarioDataProvider.getEditingDomain(), option, AnalyticsPackage.Literals.DUAL_MODE_SOLUTION_OPTION__MICRO_TARGET_CASE, microCase));
			} catch (final Throwable e) {
			}

			if (commandSuppliers.isEmpty()) {
				return null;
			}
			return () -> {
				CompoundCommand cmd = new CompoundCommand();
				commandSuppliers.forEach(supplier -> cmd.append(supplier.get()));
				return cmd;
			};
		};

		scheduleSpecificationHelper.addJobs(r);
	}

	public void generateResults(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, final AbstractSolutionSet solution) {
		scheduleSpecificationHelper.processExtraData(solution);
		scheduleSpecificationHelper.generateResults(scenarioInstance, userSettings, editingDomain, new LinkedList<>(dataTransformer.getHints()), new NullProgressMonitor());
	}
}
