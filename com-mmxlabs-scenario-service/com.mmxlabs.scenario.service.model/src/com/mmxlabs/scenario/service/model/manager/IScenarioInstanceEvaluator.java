/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.jdt.annotation.NonNull;

/**
 * This is a bodge to make the evaluation handler from the platform available here.
 * 
 * @author hinton
 *
 */
public interface IScenarioInstanceEvaluator {
	void evaluate(final @NonNull ScenarioModelRecord modelRecord);
}
