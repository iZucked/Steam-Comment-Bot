package com.mmxlabs.models.lng.adp.ext;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.SubContractProfile;

@NonNullByDefault
public interface IDistributionModelFactory {

	String getName();

	// boolean validFor(ContractProfile profile);

	boolean isMatchForCurrent(SubContractProfile<?> rule);

	DistributionModel createInstance();
}
