/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ActionableSetPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, final ActionableSetPlan plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final List<ScenarioResult> stages = new LinkedList<>();
		if (plan.getBaseOption() != null) {
			stages.add(new ScenarioResult(scenarioInstance, plan.getBaseOption().getScheduleModel()));
		}
		// Assuming first option is the base.
		for (final SolutionOption option : plan.getOptions()) {
			stages.add(new ScenarioResult(scenarioInstance, option.getScheduleModel()));
		}

		try {
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			monitor.beginTask("Opening action plans", stages.size());
			ScenarioResult prev = null;
			for (final ScenarioResult current : stages) {
				if (prev != null) {
					root.getChangeSets().add(transformer.buildDiffToBaseChangeSet(stages.get(0), prev, current));
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