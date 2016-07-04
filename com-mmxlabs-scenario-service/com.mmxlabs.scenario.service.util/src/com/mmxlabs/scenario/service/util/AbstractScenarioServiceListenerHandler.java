/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
		PRE_LOAD, POST_LOAD, PRE_SAVE, POST_SAVE, PRE_DELETE, POST_DELETE,
		/**
		 */
		PRE_UNLOAD,
		/**
		*/
		POST_UNLOAD

	}

	private final Set<IScenarioServiceListener> scenarioServiceListeners = new HashSet<IScenarioServiceListener>();

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

		final List<IScenarioServiceListener> listeners = new ArrayList<IScenarioServiceListener>(scenarioServiceListeners);
		switch (event) {
		case POST_DELETE:
			for (final IScenarioServiceListener l : listeners) {
				l.onPostScenarioInstanceDelete(this, scenarioInstance);
			}
			break;
		case POST_LOAD:
			for (final IScenarioServiceListener l : listeners) {
				l.onPostScenarioInstanceLoad(this, scenarioInstance);
			}
			break;
		case POST_UNLOAD:
			for (final IScenarioServiceListener l : listeners) {
				l.onPostScenarioInstanceUnload(this, scenarioInstance);
			}
			break;
		case POST_SAVE:
			for (final IScenarioServiceListener l : listeners) {
				l.onPostScenarioInstanceSave(this, scenarioInstance);
			}
			break;
		case PRE_DELETE:
			for (final IScenarioServiceListener l : listeners) {
				l.onPreScenarioInstanceDelete(this, scenarioInstance);
			}
			break;
		case PRE_LOAD:
			for (final IScenarioServiceListener l : listeners) {
				l.onPreScenarioInstanceLoad(this, scenarioInstance);
			}
			break;
		case PRE_UNLOAD:
			for (final IScenarioServiceListener l : listeners) {
				l.onPreScenarioInstanceUnload(this, scenarioInstance);
			}
			break;
		case PRE_SAVE:
			for (final IScenarioServiceListener l : listeners) {
				l.onPreScenarioInstanceSave(this, scenarioInstance);
			}
			break;

		}
	}
}
