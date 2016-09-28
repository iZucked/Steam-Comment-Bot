/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.models.lng.transformer.ui.ExportScheduleHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;

public class ExportChangeAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportChangeAction.class);
	private ChangeSetTableGroup changeSetTableGroup;

	public ExportChangeAction(ChangeSetTableGroup changeSetTableGroup) {
		super("Export change");
		this.changeSetTableGroup = changeSetTableGroup;
	}

	@Override
	public void run() {
		ScenarioInstance fork[] = new ScenarioInstance[1];
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		try {
			dialog.run(true, false, monitor -> {
				monitor.beginTask("Export change", IProgressMonitor.UNKNOWN);
				try {
					final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSetTableGroup.eContainer();
					String name;
					if (changeSetTableGroup.getDescription() != null && !changeSetTableGroup.getDescription().isEmpty()) {
						name = changeSetTableGroup.getDescription();
					} else {
						int idx = 0;
						if (root != null) {
							idx = root.getGroups().indexOf(changeSetTableGroup);
						}
						name = String.format("Set %d", idx);
					}
					fork[0] = ExportScheduleHelper.export(changeSetTableGroup.getChangeSet().getCurrentScenario(), name, false);
				} catch (final Exception e1) {
					e1.printStackTrace();
				} finally {
					monitor.done();
				}
			});
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		if (fork[0] != null) {
			try {
				OpenScenarioUtils.openScenarioInstance(fork[0]);
			} catch (final PartInitException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
