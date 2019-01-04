/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.lingo.reports.services.ScenarioNotEvaluatedException;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class OptimisationResultPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance instance, final OptimisationResult plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final List<ScenarioResult> stages = new LinkedList<>();

		// Hacky - compare to evaluated state
		AnalyticsModel analyticsModel = (AnalyticsModel) plan.eContainer();
		LNGScenarioModel scenarioModel = (LNGScenarioModel) analyticsModel.eContainer();
		if (scenarioModel.getScheduleModel().getSchedule() == null) {
			throw new ScenarioNotEvaluatedException("Unable to perform comparison, scenario needs to be evaluated");
		}
		ScenarioResult base = new ScenarioResult(instance, scenarioModel.getScheduleModel());

		for (final SolutionOption option : plan.getOptions()) {
			stages.add(new ScenarioResult(instance, option.getScheduleModel()));
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