/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.cases;

public class UATMultiCargoCase extends AbstractUATCase{
	public UATTypedCase[] cases;
	
	public UATMultiCargoCase(String lingoFilePath, UATTypedCase[] cases) {
		super(lingoFilePath);
		this.cases = cases;
	}

}
