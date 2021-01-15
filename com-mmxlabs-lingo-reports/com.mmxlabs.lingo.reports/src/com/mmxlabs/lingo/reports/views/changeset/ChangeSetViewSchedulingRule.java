/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Objects;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

public class ChangeSetViewSchedulingRule implements ISchedulingRule {

	private final Object view;

	public ChangeSetViewSchedulingRule(final Object view) {
		this.view = view;
	}

	@Override
	public boolean contains(final ISchedulingRule rule) {
		return rule.equals(this);
	}

	@Override
	public boolean isConflicting(final ISchedulingRule rule) {
		return rule.equals(this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ChangeSetViewSchedulingRule) {
			final ChangeSetViewSchedulingRule otherRule = (ChangeSetViewSchedulingRule) obj;
			return Objects.equals(view, otherRule.view);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(view);
	}
}
