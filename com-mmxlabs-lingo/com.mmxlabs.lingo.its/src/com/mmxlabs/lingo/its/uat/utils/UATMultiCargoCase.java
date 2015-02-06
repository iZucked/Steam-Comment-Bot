package com.mmxlabs.lingo.its.uat.utils;

public class UATMultiCargoCase extends AbstractUATCase{
	public UATTypedCase[] cases;
	
	public UATMultiCargoCase(String lingoFilePath, UATTypedCase[] cases) {
		super(lingoFilePath);
		this.cases = cases;
	}

}
