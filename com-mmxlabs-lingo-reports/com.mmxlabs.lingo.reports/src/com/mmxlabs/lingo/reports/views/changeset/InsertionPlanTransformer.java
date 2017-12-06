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
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class InsertionPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, final SlotInsertionOptions plan, final IProgressMonitor monitor, NamedObject target) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final List<ScenarioResult> stages = new LinkedList<>();

		// Hacky - compare to evaluated state
		AnalyticsModel analyticsModel = (AnalyticsModel) plan.eContainer();
		LNGScenarioModel scenarioModel = (LNGScenarioModel) analyticsModel.eContainer();
		ScenarioResult base = new ScenarioResult(scenarioInstance, scenarioModel.getScheduleModel());

		boolean first = true;
		for (final SolutionOption option : plan.getOptions()) {
			if (first) {
				// Skip first solution as it should be the original base case
				first = false;
				continue;
			}
			stages.add(new ScenarioResult(scenarioInstance, option.getScheduleModel()));
		}
		try {
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			monitor.beginTask("Opening solutions", stages.size());
			for (final ScenarioResult current : stages) {
				root.getChangeSets().add(transformer.buildSingleChangeChangeSet(base, current));
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}

		return root;

	}

}