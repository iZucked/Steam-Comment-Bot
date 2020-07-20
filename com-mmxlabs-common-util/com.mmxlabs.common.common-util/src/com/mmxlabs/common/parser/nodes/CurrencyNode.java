/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class CurrencyNode extends AbstractMarkedUpNode {

	private String name;

	public CurrencyNode(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
