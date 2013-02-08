package com.mmxlabs.models.lng.commercial.ui.modelfactories;


/**
 * @since 2.0
 */
public class PurchaseContractModelFactory extends ContractModelFactory {
	@Override
	public void initFromExtension(final String ID, final String label, final String prototype) {
		super.initFromExtension(ID, label, "com.mmxlabs.models.lng.commercial.PurchaseContract");
		this.priceInfoClassName = prototype;
	}
	

}
