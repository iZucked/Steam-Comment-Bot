/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class MinFunctionNode extends AbstractMarkedUpNode {
	List<MarkedUpNode> children = new ArrayList<>(2);

	public List<MarkedUpNode> getChildren() {
		return children;
	}
}
