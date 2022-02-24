/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.services.ScenarioNotEvaluatedException;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class OptimisationResultPlanTransformer {

	private ScenarioResultImpl make(final ScenarioInstance scenarioInstance, @Nullable ScenarioModelRecord modelRecord, ScheduleModel scheduleModel) {
		if (modelRecord != null) {
			return new ScenarioResultImpl(modelRecord, scheduleModel);
		} else {
			return new ScenarioResultImpl(scenarioInstance, scheduleModel);
		}
	}

	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, @Nullable ScenarioModelRecord modelRecord, final OptimisationResult plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		ScenarioResult base;
		if (plan.isUseScenarioBase() || plan.getBaseOption() == null) {
			// Hacky - compare to evaluated state
			LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(plan);

			if (scenarioModel == null || scenarioModel.getScheduleModel().getSchedule() == null) {
				throw new ScenarioNotEvaluatedException("Unable to perform comparison, scenario needs to be evaluated");
			}
			base = make(scenarioInstance, modelRecord, scenarioModel.getScheduleModel());
		} else {
			base = make(scenarioInstance, modelRecord, plan.getBaseOption().getScheduleModel());
		}

		try {
			monitor.beginTask("Opening solutions", plan.getOptions().size());
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			final UserSettings userSettings = plan.getUserSettings();

			for (final SolutionOption option : plan.getOptions()) {
				final ChangeDescription changeDescription = option.getChangeDescription();
				final ScenarioResult current = make(scenarioInstance, modelRecord, option.getScheduleModel());
				root.getChangeSets().add(transformer.buildSingleChangeChangeSet(base, current, changeDescription, userSettings));
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}

		return root;
	}
}