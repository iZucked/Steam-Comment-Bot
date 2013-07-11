/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite to show a commodity index (not including its index data object)
 * @author Simon McGregor
 * 
 */
public class CommodityIndexTopLevelComposite extends DefaultTopLevelComposite {

	public CommodityIndexTopLevelComposite(final Composite parent, final int style, final IScenarioEditingLocation location) {
		super(parent, style, location);
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref) && ref != PricingPackage.Literals.COMMODITY_INDEX__DATA;
	}

}
