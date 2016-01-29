/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.cases;

public class UATCase extends AbstractUATCase{

	public String cargoName;
	
	public UATCase(String lingoFilePath, String cargoName) {
		super(lingoFilePath);
		this.cargoName = cargoName;
	}

}
