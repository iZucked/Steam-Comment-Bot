/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.csvconv;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CSVConsumptionCurves {
	@JsonProperty("class")
	public String vesselName;
	public String state;
	
	public Map<Double, Double> conumptionValues = new HashMap<>(); 
}
