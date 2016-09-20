/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.ui.liveeval.IScenarioInstanceEvaluator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioInstanceEvaluator implements IScenarioInstanceEvaluator {
	@Override
	public void evaluate(final ScenarioInstance instance) {

		final IScenarioService service = instance.getScenarioService();
		if (service != null) {
			try (final ModelReference modelRefence = instance.getReference("ScenarioInstanceEvaluator")) {
				final EObject object = modelRefence.getInstance();
				if (object instanceof LNGScenarioModel) {
					OptimisationHelper.evaluateScenarioInstance(Activator.getDefault().getJobManager(), instance, null, false, false, ScenarioLock.EVALUATOR, true);
				}
			} catch (final Exception e) {
				// Swallow exceptions??
			}
		}
	}
}
