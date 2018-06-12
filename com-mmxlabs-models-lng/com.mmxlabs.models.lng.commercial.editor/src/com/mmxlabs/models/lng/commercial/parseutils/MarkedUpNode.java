/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface MarkedUpNode {

	void addChildNode(MarkedUpNode n);

	List<MarkedUpNode> getChildren();

}
