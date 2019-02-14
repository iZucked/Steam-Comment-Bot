/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parseutils;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface MarkedUpNode {

	void addChildNode(MarkedUpNode n);

	List<MarkedUpNode> getChildren();

}
