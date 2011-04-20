/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.OptimisationTransformer;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

import scenario.Scenario;

/**
 * Check that a scenario has a feasible initial solution by doing an evaluation
 * 
 * @author Tom Hinton
 * 
 */
public class InitialStateFeasibilityConstraint extends AbstractModelConstraint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse
	 * .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Scenario) {
			final Scenario scenario = (Scenario) object;

			final ModelEntityMap entities = new ModelEntityMap();
			entities.setScenario(scenario);

			final LNGScenarioTransformer lst = new LNGScenarioTransformer(
					scenario);

			final OptimisationTransformer ot = new OptimisationTransformer(
					lst.getOptimisationSettings());

			IOptimisationData<ISequenceElement> data;
			try {
				data = lst.createOptimisationData(entities);
				final Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optAndContext = ot
						.createOptimiserAndContext(data, entities);
				if (optAndContext.getFirst().getInitialSequences() == null) {
					return ctx.createFailureStatus(scenario.getName(), "no initial solution could be constructed");
				}
			} catch (final Exception ex) {
				return ctx.createFailureStatus(scenario.getName(), ex.getMessage());
			}

		}
		return ctx.createSuccessStatus();
	}

}
