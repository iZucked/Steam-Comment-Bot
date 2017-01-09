/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.cases;

import com.mmxlabs.lingo.its.uat.suite.utils.FeatureBasedUAT;

public class UATTypedCase extends AbstractUATCase{

	public String cargoName;
	public FeatureBasedUAT featureBasedUAT;
	
	public UATTypedCase(String lingoFilePath, String cargoName, FeatureBasedUAT featureBasedUAT) {
		super(lingoFilePath);
		this.cargoName = cargoName;
		this.featureBasedUAT = featureBasedUAT;
	}

}
