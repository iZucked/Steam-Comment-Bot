/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.modelfactories;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

public class FOBSaleFactory extends DefaultModelFactory {

	@Override
	protected void postprocess(final EObject top) {

		if (top instanceof DischargeSlot) {
			((DischargeSlot) top).setFOBSale(true);
		}
		super.postprocess(top);
	}
}
