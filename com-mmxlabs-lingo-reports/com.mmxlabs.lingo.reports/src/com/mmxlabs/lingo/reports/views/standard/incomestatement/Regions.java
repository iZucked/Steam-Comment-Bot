/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

public enum Regions {
	FAR_EAST_AND_MIDDLE_EAST("Far East and Middle East"), AMERICAS("Americas"), EUROPE("Europe"), OTHER("Other");

	private String name;

	private Regions(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}