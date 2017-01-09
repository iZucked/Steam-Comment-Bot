/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.parser;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Simple tree class because Java utils inexplicably doesn't provide one
 * 
 * @author Simon McGregor
 * 
 */
public final class Node {
	public final @NonNull String token;
	public final @NonNull Node[] children;

	public Node(final @NonNull String token, final @NonNull Node[] children) {
		this.token = token;
		this.children = children;
	}
}