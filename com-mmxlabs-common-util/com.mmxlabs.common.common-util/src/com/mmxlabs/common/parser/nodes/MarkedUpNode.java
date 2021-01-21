/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface MarkedUpNode {

	void addChildNode(MarkedUpNode n);

	List<MarkedUpNode> getChildren();

}
