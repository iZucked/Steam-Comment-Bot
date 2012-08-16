/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.modelfactories;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

public class DESPurchaseFactory extends DefaultModelFactory {

	@Override
	protected void postprocess(final EObject top) {

		if (top instanceof LoadSlot) {
			((LoadSlot) top).setDESPurchase(true);
		}
		super.postprocess(top);
	}
}
