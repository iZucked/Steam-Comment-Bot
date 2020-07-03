/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public final class ValidationGroup {
	private final String description;
	private final int priority;
	private final boolean ignoreError;

	public ValidationGroup(final String description, final short priority) {
		this(description, priority, false);
	}

	public ValidationGroup(final String description, final short priority, boolean ignoreError) {
		this.description = description;
		this.priority = priority;
		this.ignoreError = ignoreError;
	}

	public String getDescription() {
		return description;
	}

	public int getPriority() {
		return priority;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	public boolean isIgnoreError() {
		return ignoreError;
	}
}
