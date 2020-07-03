/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.nodes.AbstractMarkedUpNode;

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
