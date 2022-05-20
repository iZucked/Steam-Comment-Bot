/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

public interface IOverliftReportCustomiser {

	String getOverliftChartLabel();

	String getDailyOverliftTableLabel();

	String getMonthlyCumulativeOverliftLabel();

	String getMonthlyOverliftLabel();

	boolean hightlightFclViolations();
}
