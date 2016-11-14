/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public abstract class AbstractMarkedUpNode implements MarkedUpNode {

	public void addChildNode(MarkedUpNode n) {
		getChildren().add(n);
	}

	public List<MarkedUpNode> getChildren() {
//		throw new UnsupportedOperationException();
		return Collections.EMPTY_LIST;
	}
}
