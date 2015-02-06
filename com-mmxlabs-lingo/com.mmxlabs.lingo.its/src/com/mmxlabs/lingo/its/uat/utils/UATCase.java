/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.utils;

public class UATCase extends AbstractUATCase{

	public String cargoName;
	
	public UATCase(String lingoFilePath, String cargoName) {
		super(lingoFilePath);
		this.cargoName = cargoName;
	}

}
