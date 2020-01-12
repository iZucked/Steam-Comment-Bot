/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.lingo.reports.services.ScenarioNotEvaluatedException;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class SandboxResultPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, final SandboxResult plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final ScenarioResult base;
		if (plan.isUseScenarioBase()) {
			// Hacky - compare to evaluated state
			AnalyticsModel analyticsModel = (AnalyticsModel) plan.eContainer().eContainer();
			LNGScenarioModel scenarioModel = (LNGScenarioModel) analyticsModel.eContainer();
			if (scenarioModel.getScheduleModel().getSchedule() == null) {
				throw new ScenarioNotEvaluatedException("Unable to perform comparison, scenario needs to be evaluated");
			}
			base = new ScenarioResult(scenarioInstance, scenarioModel.getScheduleModel());
		} else {
			if (plan.getBaseOption() == null) {
				return root;
			}
			base = new ScenarioResult(scenarioInstance, plan.getBaseOption().getScheduleModel());
		}
		try {
			monitor.beginTask("Opening solutions", plan.getOptions().size());
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			final UserSettings userSettings = plan.getUserSettings();

			for (final SolutionOption option : plan.getOptions()) {
				final ChangeDescription changeDescription = option.getChangeDescription();
				final ScenarioResult current = new ScenarioResult(scenarioInstance, option.getScheduleModel());
//				root.getChangeSets().add(transformer.buildSingleChangeChangeSet(base, current, changeDescription, userSettings));
				
				ScenarioResult altBase = null;
				ScenarioResult altCurrent = null;
				if (option instanceof DualModeSolutionOption) {
					final DualModeSolutionOption slotInsertionOption = (DualModeSolutionOption) option;
					if (slotInsertionOption.getMicroBaseCase() != null && slotInsertionOption.getMicroTargetCase() != null) {
						altBase = new ScenarioResult(scenarioInstance, slotInsertionOption.getMicroBaseCase().getScheduleModel());
						altCurrent = new ScenarioResult(scenarioInstance, slotInsertionOption.getMicroTargetCase().getScheduleModel());
					}
				}
				if (plan.isHasDualModeSolutions()) {
					root.getChangeSets().add(transformer.buildParallelDiffChangeSet(base, current, altBase, altCurrent, changeDescription, userSettings, null));
				} else {
					root.getChangeSets().add(transformer.buildSingleChangeChangeSet(base, current, changeDescription, userSettings, null));
				}
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}

		return root;
	}

}