/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class ActionableSetPlanTransformer {

	private ScenarioResultImpl make(final ScenarioInstance scenarioInstance, @Nullable ScenarioModelRecord modelRecord, ScheduleModel scheduleModel) {
		if (modelRecord != null) {
			return new ScenarioResultImpl(modelRecord, scheduleModel);
		} else {
			return new ScenarioResultImpl(scenarioInstance, scheduleModel);
		}
	}

	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, @Nullable ScenarioModelRecord modelRecord, final ActionableSetPlan plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		try {
			monitor.beginTask("Opening action plans", plan.getOptions().size());
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			final UserSettings userSettings = plan.getUserSettings();

			ScenarioResult base = null;

			boolean first = true;
			if (plan.getBaseOption() != null) {
				base = make(scenarioInstance, modelRecord, plan.getBaseOption().getScheduleModel());
				first = false;
			}

			// Assuming first option is the base.
			ScenarioResult prev = base;
			for (final SolutionOption option : plan.getOptions()) {
				final ScenarioResult current = make(scenarioInstance, modelRecord, option.getScheduleModel());
				if (first) {
					base = current;
					first = false;
				} else {
					final ChangeDescription changeDescription = option.getChangeDescription();
					root.getChangeSets().add(transformer.buildDiffToBaseChangeSet(base, prev, current, changeDescription, userSettings));
				}
				prev = current;
				monitor.worked(1);
			}

		} finally {
			monitor.done();
		}

		return root;
	}

}