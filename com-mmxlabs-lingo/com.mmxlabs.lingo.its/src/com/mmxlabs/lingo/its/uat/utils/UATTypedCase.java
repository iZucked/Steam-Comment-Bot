package com.mmxlabs.lingo.its.uat.utils;

public class UATTypedCase extends AbstractUATCase{

	public String cargoName;
	public FeatureBasedUAT featureBasedUAT;
	
	public UATTypedCase(String lingoFilePath, String cargoName, FeatureBasedUAT featureBasedUAT) {
		super(lingoFilePath);
		this.cargoName = cargoName;
		this.featureBasedUAT = featureBasedUAT;
	}

}
