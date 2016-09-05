package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.ext.IFlowTypeFactory;

public class DeliverToSpotFlowFactory implements IFlowTypeFactory {

	@Override
	public String getName() {
		return "Deliver To Spot";
	}

	@Override
	public boolean validFor(ContractProfile profile) {
		return profile instanceof PurchaseContractProfile;
	}

	@Override
	public FlowType createInstance() {
		return ADPFactory.eINSTANCE.createDeliverToSpotFlow();
	}
	
	@Override
	public boolean isMatchForCurrent(BindingRule rule) {
		return ADPPackage.Literals.DELIVER_TO_SPOT_FLOW.isInstance(rule.getFlowType());
	}

}
