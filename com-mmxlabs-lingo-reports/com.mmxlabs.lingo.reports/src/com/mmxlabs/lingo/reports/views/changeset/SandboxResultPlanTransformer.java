/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.services.ScenarioNotEvaluatedException;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class SandboxResultPlanTransformer {
	private static final Logger logger = LoggerFactory.getLogger(SandboxResultPlanTransformer.class);

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
			base = new ScenarioResultImpl(scenarioInstance, scenarioModel.getScheduleModel());
		} else {
			if (plan.getBaseOption() == null) {
				return root;
			}
			base = new ScenarioResultImpl(scenarioInstance, plan.getBaseOption().getScheduleModel());
		}
		try {
			monitor.beginTask("Opening solutions", plan.getOptions().size());
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			final UserSettings userSettings = plan.getUserSettings();

			List<ChangeSet> changeSets = new LinkedList<>();
			for (final SolutionOption option : plan.getOptions()) {
				final ChangeDescription changeDescription = option.getChangeDescription();
				final ScenarioResult current = new ScenarioResultImpl(scenarioInstance, option.getScheduleModel());

				ScenarioResult altBase = null;
				ScenarioResult altCurrent = null;
				if (option instanceof DualModeSolutionOption) {
					final DualModeSolutionOption slotInsertionOption = (DualModeSolutionOption) option;
					if (slotInsertionOption.getMicroBaseCase() != null && slotInsertionOption.getMicroTargetCase() != null) {
						altBase = new ScenarioResultImpl(scenarioInstance, slotInsertionOption.getMicroBaseCase().getScheduleModel());
						altCurrent = new ScenarioResultImpl(scenarioInstance, slotInsertionOption.getMicroTargetCase().getScheduleModel());
					}
				}
				// TODO: FIX ME!!! The null guards below are a patch for when either base or
				// current do not have a scheduleModel. This patch prevents a message from
				// popping up to the user.
				if (plan.isHasDualModeSolutions()) {
					final ChangeSet changeSet = transformer.buildParallelDiffChangeSet(base, current, altBase, altCurrent, changeDescription, userSettings, null);
					if (changeSet.getMetricsToDefaultBase() != null) {
						changeSets.add(changeSet);
					} else {
						logger.error("Got null pointer when opening solution");
					}
				} else {
					final ChangeSet changeSet = transformer.buildSingleChangeChangeSet(base, current, changeDescription, userSettings, null);
					if (changeSet.getMetricsToDefaultBase() != null) {
						changeSets.add(changeSet);
					} else {
						logger.error("Got null pointer when opening solution");
					}
				}

				monitor.worked(1);
			}

			Collections.sort(changeSets, (a, b) -> -Long.compare(a.getMetricsToDefaultBase().getPnlDelta(), b.getMetricsToDefaultBase().getPnlDelta()));
			root.getChangeSets().addAll(changeSets);
		} finally {
			monitor.done();
		}

		return root;
	}

}