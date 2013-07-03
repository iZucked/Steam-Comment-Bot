/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(final ScenarioInstance instance) {

		final IScenarioService service = instance.getScenarioService();
		if (service != null) {
			try {
				final EObject object = service.load(instance);

				if (object instanceof LNGScenarioModel) {
					final LNGScenarioModel root = (LNGScenarioModel) object;

					OptimisationHelper.evaluateScenarioInstance(Activator.getDefault().getJobManager(), instance, false, false, ScenarioLock.EVALUATOR);
				}
			} catch (final IOException e) {

			}
		}
	}
}
