/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.util.Collections;

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
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class ExportChangeAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportChangeAction.class);
	private final ChangeSetTableGroup changeSetTableGroup;
	private final String label;

	public ExportChangeAction(final ChangeSetTableGroup changeSetTableGroup, final String label) {
		super("Export solution");
		this.changeSetTableGroup = changeSetTableGroup;
		this.label = label;
	}

	@Override
	public void run() {
		final ScenarioInstance fork[] = new ScenarioInstance[1];
		final String name = ScenarioServiceModelUtils.openNewNameForForkPrompt(label, label, Collections.emptySet());
		if (name == null) {
			return;
		}
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		try {
			dialog.run(true, false, monitor -> {
				monitor.beginTask("Export solution", IProgressMonitor.UNKNOWN);
				try {
					fork[0] = ExportScheduleHelper.export(changeSetTableGroup.getChangeSet().getCurrentScenario(), name, false);
				} catch (final Exception e1) {
					LOGGER.error(e1.getMessage(), e1);
				} finally {
					monitor.done();
				}
			});
		} catch (final Exception e) {
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
