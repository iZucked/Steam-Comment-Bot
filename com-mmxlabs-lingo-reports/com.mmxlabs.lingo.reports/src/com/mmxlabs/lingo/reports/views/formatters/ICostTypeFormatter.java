/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.formatters;

/**
 */
public interface ICostTypeFormatter {

	public enum Type {
		COST, REVENUE, OTHER, UNSET
	}

	Type getType();

}