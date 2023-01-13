/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;

public class FilterScheduleChartBySelectionAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	private IAction action;

	@Override
	public void init(final IWorkbenchWindow window) {
		this.window = window;
		updateActionState();
	}

	@Override
	public void dispose() {
		window = null;
		action = null;
	}

	@Override
	public void run(final IAction action) {

		final ScenarioComparisonService scenarioComparisonService = (ScenarioComparisonService) window.getWorkbench().getService(ScenarioComparisonService.class);
		if (scenarioComparisonService != null) {
			scenarioComparisonService.toggleDiffOption(EDiffOption.FILTER_SCHEDULE_CHART_BY_SELECTION);
			setActionChecked(scenarioComparisonService.getDiffOptions().isFilterSelectedSequences());
		}
	}

	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {
		this.action = action;
		updateActionState();
	}

	private void updateActionState() {

		final ScenarioComparisonService scenarioComparisonService = (ScenarioComparisonService) window.getWorkbench().getService(ScenarioComparisonService.class);
		if (scenarioComparisonService != null) {
			setActionChecked(scenarioComparisonService.getDiffOptions().isFilterSelectedSequences());
		}
	}

	private void setActionChecked(final boolean checked) {
		if (action != null) {
			action.setChecked(checked);
		}
	}

}
