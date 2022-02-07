/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CargoIntervalDistributionModel instances
 *
 * @generated NOT
 */
public class CargoIntervalDistributionModelComponentHelper extends DefaultComponentHelper {

	public CargoIntervalDistributionModelComponentHelper() {
		super(ADPPackage.Literals.CARGO_INTERVAL_DISTRIBUTION_MODEL);
	}

	@Override
	protected void addSuperClassHelpers(final EClass targetClass) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		for (final EClass parent : targetClass.getESuperTypes()) {
			if (parent != ADPPackage.Literals.DISTRIBUTION_MODEL) {
				superClassesHelpers.addAll(registry.getComponentHelpers(parent));
			}
		}
	}
	
}