/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.lingo.reports.services.ScenarioNotEvaluatedException;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class OptimisationResultPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, final OptimisationResult plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		// Hacky - compare to evaluated state
		AnalyticsModel analyticsModel = (AnalyticsModel) plan.eContainer();
		LNGScenarioModel scenarioModel = (LNGScenarioModel) analyticsModel.eContainer();
		if (scenarioModel.getScheduleModel().getSchedule() == null) {
			throw new ScenarioNotEvaluatedException("Unable to perform comparison, scenario needs to be evaluated");
		}
		ScenarioResult base = new ScenarioResult(scenarioInstance, scenarioModel.getScheduleModel());

		try {
			monitor.beginTask("Opening solutions", plan.getOptions().size());
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			final UserSettings userSettings = plan.getUserSettings();

			for (final SolutionOption option : plan.getOptions()) {
				final ChangeDescription changeDescription = option.getChangeDescription();
				final ScenarioResult current = new ScenarioResult(scenarioInstance, option.getScheduleModel());
				root.getChangeSets().add(transformer.buildSingleChangeChangeSet(base, current, changeDescription, userSettings));
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}

		return root;
	}

}