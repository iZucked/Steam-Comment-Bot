/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.presentation.editors.PreDefinedDatesInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for PreDefinedDistributionModel instances
 *
 * @generated NOT
 */
public class PreDefinedDistributionModelComponentHelper extends DefaultComponentHelper {

	public PreDefinedDistributionModelComponentHelper() {
		super(ADPPackage.Literals.PRE_DEFINED_DISTRIBUTION_MODEL);

		addEditor(ADPPackage.Literals.PRE_DEFINED_DISTRIBUTION_MODEL__DATES, topClass -> new PreDefinedDatesInlineEditor(ADPPackage.Literals.PRE_DEFINED_DISTRIBUTION_MODEL__DATES));
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