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
import com.mmxlabs.models.lng.analytics.SlotInsertionOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class InsertionPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance instance, final SlotInsertionOptions plan, final IProgressMonitor monitor, Slot target) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final List<ScenarioResult> stages = new LinkedList<>();

		// Assuming first option is the base.
		for (final SlotInsertionOption option : plan.getInsertionOptions()) {
			stages.add(new ScenarioResult(instance, option.getScheduleModel()));
		}

		try {
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			monitor.beginTask("Opening insertion plans", stages.size());
			ScenarioResult base = null;
			for (final ScenarioResult current : stages) {
				if (base != null) {
					root.getChangeSets().add(transformer.buildChangeSet(stages.get(0), base, current, target));
				}
				if (base == null) {
					base = current;
				}
				monitor.worked(1);

			}
		} finally {
			monitor.done();
		}

		return root;

	}

}