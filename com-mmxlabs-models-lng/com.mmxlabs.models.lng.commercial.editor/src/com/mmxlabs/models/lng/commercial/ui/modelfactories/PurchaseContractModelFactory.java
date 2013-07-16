/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.modelfactories;


/**
 * @since 3.0
 */
public class PurchaseContractModelFactory extends ContractModelFactory {
	@Override
	public void initFromExtension(final String ID, final String label, final String prototype, final String replacementReference, final String replacementClass) {
		super.initFromExtension(ID, label, "com.mmxlabs.models.lng.commercial.PurchaseContract", replacementReference, replacementClass);
		this.priceInfoClassName = prototype;
	}
	

}
