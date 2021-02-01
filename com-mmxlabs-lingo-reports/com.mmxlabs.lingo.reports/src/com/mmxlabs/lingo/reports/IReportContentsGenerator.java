/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import com.mmxlabs.scenario.service.ScenarioResult;

public interface IReportContentsGenerator {

	String getStringContents(ScenarioResult pin, ScenarioResult other);

}
