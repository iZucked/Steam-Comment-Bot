/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.modelfactories;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.NetbackPurchaseContract;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

/**
 * Extended {@link DefaultModelFactory} to create a default {@link NotionalBallastParameters} when a new {@link NetbackPurchaseContract} is created.
 * 
 * @author Simon Goodall
 * 
 */
public class NetbackPurchaseContractFactory extends DefaultModelFactory {

	public NetbackPurchaseContractFactory() {
		super();
	}

	@Override
	protected void postprocess(final EObject top) {
		super.postprocess(top);

		if (top instanceof NetbackPurchaseContract) {
			final NetbackPurchaseContract netbackPurchaseContract = (NetbackPurchaseContract) top;
			final NotionalBallastParameters params = CommercialFactory.eINSTANCE.createNotionalBallastParameters();

			params.setName("Default");

			netbackPurchaseContract.getNotionalBallastParameters().add(params);
		}
	}

}