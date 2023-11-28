/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import com.mmxlabs.widgets.schedulechart.DefaultScheduleChartSettings;

public class NinetyDayScheduleChartSettings extends DefaultScheduleChartSettings {
	
	private boolean showAnnotations;
	private boolean hasMultipleScenarios;
	private boolean showLegend;
	private boolean inCompareMode;
	
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
	
	@Override
	public boolean showLegend() {
		return showLegend;
	}

	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}
	
	public void setInCompareMode(boolean inCompareMode) {
		this.inCompareMode = inCompareMode;
	}
	
	@Override
	public boolean inCompareMode() {
		return inCompareMode;
	}

}
