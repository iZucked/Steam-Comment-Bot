/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.financial.settled;

import java.util.LinkedList;
import java.util.List;

public class SettledPriceCurve {
	private String name;
	private List<SettledPriceEntry> entries = new LinkedList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SettledPriceEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<SettledPriceEntry> entries) {
		this.entries = entries;
	}
}
