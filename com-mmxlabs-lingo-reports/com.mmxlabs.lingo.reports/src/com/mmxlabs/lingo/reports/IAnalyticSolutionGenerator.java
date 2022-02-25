/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;

public interface IAnalyticSolutionGenerator {

	IReportContents getReportContents(AnalyticsSolution analyticsSolution);
}
