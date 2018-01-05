/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import com.mmxlabs.scenario.service.ui.ScenarioResult;

public interface IReportContentsGenerator {

	String getStringContents(ScenarioResult pin, ScenarioResult other);

}
