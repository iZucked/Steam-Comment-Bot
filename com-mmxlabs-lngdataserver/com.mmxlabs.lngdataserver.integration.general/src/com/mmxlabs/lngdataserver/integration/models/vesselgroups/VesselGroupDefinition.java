/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.vesselgroups;

import java.util.LinkedList;
import java.util.List;

public class VesselGroupDefinition {

	private String name;
	private List<String> entries = new LinkedList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getEntries() {
		return entries;
	}

	public void setEntries(List<String> entries) {
		this.entries = entries;
	}
}
