/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

/**
 */
public class RouteCostModelFactory extends DefaultModelFactory {

	@Override
	protected EObject constructInstance(final EClass eClass) {
		final EObject instance = super.constructInstance(eClass);

		if (instance instanceof RouteCost) {
			final RouteCost routeCost = (RouteCost) instance;
			routeCost.setRouteOption(RouteOption.SUEZ);
		}

		return instance;
	}
}
