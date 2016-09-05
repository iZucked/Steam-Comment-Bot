package com.mmxlabs.models.lng.adp.ext;

import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FlowType;

public interface IFlowTypeFactory {

	String getName();

	boolean validFor(ContractProfile profile);

	boolean isMatchForCurrent(BindingRule rule);

	FlowType createInstance();
}
