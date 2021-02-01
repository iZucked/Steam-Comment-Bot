/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.lingo.reports.services.ScenarioNotEvaluatedException;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class InsertionPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, final SlotInsertionOptions plan, final IProgressMonitor monitor, NamedObject target) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		try {
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			monitor.beginTask("Opening solutions", plan.getOptions().size());
			UserSettings userSettings = plan.getUserSettings();

			final ScenarioResult base;
			if (plan.isUseScenarioBase() || plan.getBaseOption() == null) {
				// Hacky - compare to evaluated state
				LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(plan);

				if (scenarioModel == null || scenarioModel.getScheduleModel().getSchedule() == null) {
					throw new ScenarioNotEvaluatedException("Unable to perform comparison, scenario needs to be evaluated");
				}
				base = new ScenarioResultImpl(scenarioInstance, scenarioModel.getScheduleModel());
			} else {
				base = new ScenarioResultImpl(scenarioInstance, plan.getBaseOption().getScheduleModel());
			}

			for (final SolutionOption option : plan.getOptions()) {
				ChangeDescription changeDescription = option.getChangeDescription();
				final ScenarioResult current = new ScenarioResultImpl(scenarioInstance, option.getScheduleModel());

				{

					ScenarioResult altBase = null;
					ScenarioResult altCurrent = null;
					if (option instanceof DualModeSolutionOption) {
						final DualModeSolutionOption slotInsertionOption = (DualModeSolutionOption) option;
						if (slotInsertionOption.getMicroBaseCase() != null && slotInsertionOption.getMicroTargetCase() != null) {
							altBase = new ScenarioResultImpl(scenarioInstance, slotInsertionOption.getMicroBaseCase().getScheduleModel());
							altCurrent = new ScenarioResultImpl(scenarioInstance, slotInsertionOption.getMicroTargetCase().getScheduleModel());
						}
					}
					if (plan.isHasDualModeSolutions()) {
						root.getChangeSets().add(transformer.buildParallelDiffChangeSet(base, current, altBase, altCurrent, changeDescription, userSettings, target));
					} else {
						root.getChangeSets().add(transformer.buildSingleChangeChangeSet(base, current, changeDescription, userSettings, target));
					}
					monitor.worked(1);
				}
			}
		} finally {
			monitor.done();
		}

		return root;
	}
}