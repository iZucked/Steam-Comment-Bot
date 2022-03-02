/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.util.Collections;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ui.ExportScheduleHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class ExportChangeAction extends Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportChangeAction.class);
	private final ChangeSetTableGroup changeSetTableGroup;
	private BiConsumer<LNGScenarioModel, Schedule> modelCustomiser;
	private boolean bothCases;
	private String label;

	public ExportChangeAction(ChangeSetTableGroup changeSetTableGroup, String label, @Nullable BiConsumer<LNGScenarioModel, Schedule> modelCustomiser) {
		this(changeSetTableGroup, label, false, modelCustomiser);
	}

	public ExportChangeAction(ChangeSetTableGroup changeSetTableGroup, String label, boolean bothCases, @Nullable BiConsumer<LNGScenarioModel, Schedule> modelCustomiser) {
		super(bothCases ? "Export mini scenarios " : "Export solution");
		this.changeSetTableGroup = changeSetTableGroup;
		this.label = label;
		this.bothCases = bothCases;
		this.modelCustomiser = modelCustomiser;
	}

	@Override
	public void run() {
		final ScenarioInstance fork[] = new ScenarioInstance[2];
		final String name = ScenarioServiceModelUtils.openNewNameForForkPrompt(label, label, Collections.emptySet());
		if (name == null) {
			return;
		}
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		try {
			dialog.run(true, false, monitor -> {
				monitor.beginTask("Export solution", IProgressMonitor.UNKNOWN);
				try {
					if (bothCases) {
						fork[0] = ExportScheduleHelper.export(changeSetTableGroup.getCurrentScenario(), name + "-After", false, modelCustomiser);
						fork[1] = ExportScheduleHelper.export(changeSetTableGroup.getBaseScenario(), name + "-Before", false, modelCustomiser);
					} else {
						fork[0] = ExportScheduleHelper.export(changeSetTableGroup.getCurrentScenario(), name, false, modelCustomiser);
					}
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
