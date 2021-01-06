/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Abstract {@link IScenarioService} implementation handling just the {@link IScenarioServiceListener} elements of the interface to provide a simple base class for other {@link IScenarioService}
 * implementations to base off. Sub-classes need to call {@link #fireEvent(ScenarioServiceEvent, ScenarioInstance)} to notify the registered listeners.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractScenarioServiceListenerHandler implements IScenarioService {
	@NonNullByDefault
	protected enum ScenarioServiceEvent {
		PRE_DELETE, PRE_UNLOAD,
	}

	private final Set<IScenarioServiceListener> scenarioServiceListeners = new HashSet<>();

	@Override
	public void addScenarioServiceListener(@NonNull IScenarioServiceListener listener) {
		scenarioServiceListeners.add(listener);
	}

	@Override
	public void removeScenarioServiceListener(@NonNull IScenarioServiceListener listener) {
		scenarioServiceListeners.remove(listener);
	}

	protected void fireEvent(@NonNull final ScenarioServiceEvent event, @NonNull final ScenarioInstance scenarioInstance) {

		// Break out early if there are no listeners
		if (scenarioServiceListeners.isEmpty()) {
			return;
		}

		final List<IScenarioServiceListener> listeners = new ArrayList<>(scenarioServiceListeners);
		switch (event) {
		case PRE_DELETE:
			for (final IScenarioServiceListener l : listeners) {
				l.onPreScenarioInstanceDelete(this, scenarioInstance);
			}
			break;
		case PRE_UNLOAD:
			for (final IScenarioServiceListener l : listeners) {
				l.onPreScenarioInstanceUnload(this, scenarioInstance);
			}
			break;
		}
	}
}
