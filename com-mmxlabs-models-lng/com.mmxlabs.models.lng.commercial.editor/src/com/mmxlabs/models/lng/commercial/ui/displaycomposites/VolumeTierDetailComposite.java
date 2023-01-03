/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

/**
 * @author Simon Goodall
 * 
 */
public class VolumeTierDetailComposite extends DefaultDetailComposite {

	public VolumeTierDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {

		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withLabel("Price tiers") //
				.withFeature(CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION) //
				.withFeature(CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION) //
				.makeRow() //
				//
				.withRow() //
				.withLabel("Threshold").withFeature(CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD) //
				.withFeature(CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT) //
				.makeRow() //

				//
				.make() //
		;
	}
}
