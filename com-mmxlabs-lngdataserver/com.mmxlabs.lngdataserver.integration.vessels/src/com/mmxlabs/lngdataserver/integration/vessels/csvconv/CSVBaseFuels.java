/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.csvconv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "kind" })
public class CSVBaseFuels {
	public String name;
	public String expression;
	public double equivalenceFactor;
}
