/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.scenario.service.ScenarioResult;

@NonNullByDefault
public interface IPinDiffResultPlanTransformer {

	ChangeSetRoot createDataModel(ScenarioResult pin, ScenarioResult other, IProgressMonitor monitor);
}
