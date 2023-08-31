package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import com.mmxlabs.widgets.schedulechart.DefaultScheduleChartSettings;

public class NinetyDayScheduleChartSettings extends DefaultScheduleChartSettings {
	
	private boolean showWindows;

	@Override
	public boolean showWindows() {
		return showWindows;
	}
	
	public void setShowWindows(boolean showWindows) {
		this.showWindows = showWindows;
	}

}
