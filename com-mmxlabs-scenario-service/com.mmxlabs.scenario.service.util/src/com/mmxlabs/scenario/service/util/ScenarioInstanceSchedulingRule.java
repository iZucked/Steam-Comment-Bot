/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.util.Objects;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioInstanceSchedulingRule implements ISchedulingRule {

	private final ScenarioInstance scenarioInstance;

	public ScenarioInstanceSchedulingRule(final ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
	}

	@Override
	public boolean contains(final ISchedulingRule rule) {
		return rule.equals(this);
	}

	@Override
	public boolean isConflicting(final ISchedulingRule rule) {
		return rule.equals(this);
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ScenarioInstanceSchedulingRule) {
			final ScenarioInstanceSchedulingRule scenarioInstanceSchedulingRule = (ScenarioInstanceSchedulingRule) obj;

			final ScenarioInstance si = scenarioInstanceSchedulingRule.getScenarioInstance();
			if (si == null) {
				return si == scenarioInstance;
			} else {
				return si.equals(scenarioInstance);
			}
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(scenarioInstance);
	}
}
