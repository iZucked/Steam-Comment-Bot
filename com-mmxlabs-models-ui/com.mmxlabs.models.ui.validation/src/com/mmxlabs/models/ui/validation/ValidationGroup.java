package com.mmxlabs.models.ui.validation;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public final class ValidationGroup {
	private final String description;
	private final int priority;

	public ValidationGroup(final String description, final int priority) {
		this.description = description;
		this.priority = priority;
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
}
