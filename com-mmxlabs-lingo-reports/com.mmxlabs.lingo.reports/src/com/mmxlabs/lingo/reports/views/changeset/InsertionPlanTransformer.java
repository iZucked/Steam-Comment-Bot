/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.Nullable;

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
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class InsertionPlanTransformer extends AbstractSolutionSetTransformer<SlotInsertionOptions> {

	@Override
	public ChangeSetRoot createDataModel(final ScenarioInstance scenarioInstance, @Nullable ScenarioModelRecord modelRecord, final SlotInsertionOptions plan, final IProgressMonitor monitor,
			NamedObject target) {

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
				base = make(scenarioInstance, modelRecord, scenarioModel.getScheduleModel());
			} else {
				base = make(scenarioInstance, modelRecord, plan.getBaseOption().getScheduleModel());
			}

			for (final SolutionOption option : plan.getOptions()) {
				ChangeDescription changeDescription = option.getChangeDescription();
				final ScenarioResult current = make(scenarioInstance, modelRecord, option.getScheduleModel());

				{

					ScenarioResult altBase = null;
					ScenarioResult altCurrent = null;
					if (option instanceof DualModeSolutionOption slotInsertionOption) {
						if (slotInsertionOption.getMicroBaseCase() != null && slotInsertionOption.getMicroTargetCase() != null) {
							altBase = make(scenarioInstance, modelRecord, slotInsertionOption.getMicroBaseCase().getScheduleModel());
							altCurrent = make(scenarioInstance, modelRecord, slotInsertionOption.getMicroTargetCase().getScheduleModel());
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