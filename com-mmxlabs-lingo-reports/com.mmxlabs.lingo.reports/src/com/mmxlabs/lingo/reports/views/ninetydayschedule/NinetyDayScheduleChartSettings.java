/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import com.mmxlabs.widgets.schedulechart.DefaultScheduleChartSettings;

public class NinetyDayScheduleChartSettings extends DefaultScheduleChartSettings {
	
	private boolean showAnnotations;
	private boolean hasMultipleScenarios;

	@Override
	public boolean showAnnotations() {
		return showAnnotations;
	}
	
	public void setShowAnnotations(boolean showAnnotations) {
		this.showAnnotations = showAnnotations;
	}

	public boolean showNominalsByDefault() {
		return false;
	}
	
	@Override
	public boolean hasMultipleScenarios() {
		return hasMultipleScenarios;
	}

	public void sethasMultipleScenarios(boolean hasMultipleScenarios) {
		this.hasMultipleScenarios = hasMultipleScenarios;
	}

}
