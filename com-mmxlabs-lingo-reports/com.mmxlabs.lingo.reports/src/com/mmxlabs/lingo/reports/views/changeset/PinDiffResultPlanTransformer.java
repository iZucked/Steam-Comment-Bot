/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.scenario.service.ScenarioResult;

public class PinDiffResultPlanTransformer {

	public ChangeSetRoot createDataModel(ScenarioResult pin, ScenarioResult other, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		try {
			monitor.beginTask("Comparing solutions", 1);
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			root.getChangeSets().add(transformer.buildSingleChangeChangeSet(pin, other, null, null));
			monitor.worked(1);
		} finally {
			monitor.done();
		}

		return root;
	}
}