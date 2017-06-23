/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.manager.ModelRecord;

/**
 * This is a bodge to make the evaluation handler from the platform available here.
 * 
 * @author hinton
 *
 */
public interface IScenarioInstanceEvaluator {
	public void evaluate(final @NonNull ModelRecord modelRecord);
}
