/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.modelfactories;


/**
 */
public class SalesContractModelFactory extends ContractModelFactory {
	@Override
	public void initFromExtension(final String ID, final String label, final String prototype, String replacementReference, String replacementClass) {
		super.initFromExtension(ID, label, "com.mmxlabs.models.lng.commercial.SalesContract", replacementReference, replacementClass);
		this.priceInfoClassName = prototype;
	}
	

}
