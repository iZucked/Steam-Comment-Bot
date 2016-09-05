package com.mmxlabs.models.lng.adp.ext.impl;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.ext.IFlowTypeFactory;

public class SupplyFromProfileFlowFactory implements IFlowTypeFactory {

	@Override
	public String getName() {
		return "Supply From Profile";
	}

	@Override
	public boolean validFor(ContractProfile profile) {
		return profile instanceof SalesContractProfile;
	}

	@Override
	public FlowType createInstance() {
		return ADPFactory.eINSTANCE.createSupplyFromProfileFlow();
	}

	@Override
	public boolean isMatchForCurrent(BindingRule rule) {
		return ADPPackage.Literals.SUPPLY_FROM_PROFILE_FLOW.isInstance(rule.getFlowType());
	}

}
