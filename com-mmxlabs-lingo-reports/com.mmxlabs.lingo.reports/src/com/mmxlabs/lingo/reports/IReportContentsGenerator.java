/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.ScenarioResult;

public interface IReportContentsGenerator {

	IReportContents getReportContents(ScenarioResult pin, ScenarioResult other, final @Nullable List<Object> selectedObjects);
}
