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
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ActionSetTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance instance, final IProgressMonitor monitor) {

		final List<ScenarioResult> stages = new LinkedList<>();

		// Try forks
		{
			final Container c = (ScenarioInstance) instance;
			boolean foundBase = false;
			int i = 1;
			while (true) {
				boolean found = false;
				for (final Container cc : c.getElements()) {
					if (!foundBase && (cc.getName().equals("base") || cc.getName().equalsIgnoreCase(String.format("ActionSet-base")))) {
						if (cc instanceof ScenarioInstance) {
							stages.add(0, new ScenarioResult((ScenarioInstance) cc));
							foundBase = true;
						}
					}
					if (cc.getName().equals(Integer.toString(i)) || cc.getName().equalsIgnoreCase(String.format("ActionSet-%d", i))) {
						if (cc instanceof ScenarioInstance) {
							stages.add(new ScenarioResult((ScenarioInstance) cc));
							found = true;
							i++;
						}
					}
				}
				if (!found) {
					break;
				}
			}
		}
		if (stages.isEmpty()) {
			// Try parent folder
			final Container c = ((ScenarioInstance) instance).getParent();
			int i = 1;
			boolean foundBase = false;
			while (true) {
				boolean found = false;
				for (final Container cc : c.getElements()) {
					if (!foundBase && (cc.getName().equals("base") || cc.getName().equalsIgnoreCase(String.format("ActionSet-base")))) {
						if (cc instanceof ScenarioInstance) {
							stages.add(0, new ScenarioResult((ScenarioInstance) cc));
							foundBase = true;
						}
					}

					if (cc.getName().equals(Integer.toString(i)) || cc.getName().equalsIgnoreCase(String.format("ActionSet-%d", i))) {
						if (cc instanceof ScenarioInstance) {
							stages.add(new ScenarioResult((ScenarioInstance) cc));
							found = true;
							i++;
						}
					}
				}
				if (!found) {
					break;
				}
			}
		}

		return createDataModel(monitor, stages);

	}

	public ChangeSetRoot createDataModel(final IProgressMonitor monitor, final List<ScenarioResult> stages) {
		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		try {
			ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			monitor.beginTask("Opening action sets", stages.size());
			ScenarioResult prev = null;
			for (final ScenarioResult current : stages) {
				if (prev != null) {
					root.getChangeSets().add(transformer.buildChangeSet(stages.get(0), prev, current));
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