package com.mmxlabs.models.lng.adp.ext.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.ext.IDistributionModelFactory;

@NonNullByDefault
public class CargoNumberFactory implements IDistributionModelFactory {
	@Override
	public String getName() {
		return "Number per year";
	}

	@Override
	public boolean isMatchForCurrent(SubContractProfile<?> rule) {

		return ADPPackage.Literals.CARGO_NUMBER_DISTRIBUTION_MODEL.isInstance(rule.getDistributionModel());
	}

	@Override
	public DistributionModel createInstance() {
		return ADPFactory.eINSTANCE.createCargoNumberDistributionModel();
	}
}
