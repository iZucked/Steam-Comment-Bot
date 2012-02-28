/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerModule;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;

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
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof MMXRootObject) {
			final MMXRootObject scenario = (MMXRootObject) object;

			final ModelEntityMap entities = new ResourcelessModelEntityMap();
			entities.setScenario(scenario);

			final LNGScenarioTransformer lst = LNGTransformerModule.createLNGScenarioTransformer(scenario);

			final OptimisationTransformer ot = new OptimisationTransformer(lst.getOptimisationSettings());

			IOptimisationData data;
			try {
				lst.addPlatformTransformerExtensions();
				data = lst.createOptimisationData(entities);
				final Pair<IOptimisationContext, LocalSearchOptimiser> optAndContext = ot.createOptimiserAndContext(data, entities);
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
