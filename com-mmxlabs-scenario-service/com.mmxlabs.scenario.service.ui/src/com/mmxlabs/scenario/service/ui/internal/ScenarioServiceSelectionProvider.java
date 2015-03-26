/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

/**
 * Basic scenario selection service implementation
 * 
 * @author hinton
 * 
 */
public class ScenarioServiceSelectionProvider implements IScenarioServiceSelectionProvider {
	private final List<IScenarioServiceSelectionChangedListener> listeners = new LinkedList<IScenarioServiceSelectionChangedListener>();

	private final HashSet<ScenarioInstance> selection = new HashSet<ScenarioInstance>();

	private ScenarioInstance pin = null;

	@Override
	public void select(final ScenarioInstance instance) {
		select(instance, false);
	}

	@Override
	public void select(final ScenarioInstance instance, final boolean block) {
		if (!isSelected(instance)) {
			// Enforce one pin, one other scenario.
			if (pin != null && selection.size() > 1) {
				for (final ScenarioInstance scenarioInstance : new ArrayList<>(selection)) {
					if (scenarioInstance == instance) {
						continue;
					}
					if (scenarioInstance == pin) {
						continue;
					}
					deselect(scenarioInstance, block);
				}
			}
			selection.add(instance);
			for (final IScenarioServiceSelectionChangedListener listener : listeners) {
				listener.selected(this, Collections.singleton(instance), block);
			}
		}
	}

	@Override
	public void deselect(final ScenarioInstance instance) {
		deselect(instance, false);
	}

	@Override
	public void deselect(final ScenarioInstance instance, final boolean block) {
		if (isSelected(instance)) {
			if (instance == pin) {
				setPinnedInstance(null, block);
			} else {
				selection.remove(instance);
				for (final IScenarioServiceSelectionChangedListener listener : listeners) {
					listener.deselected(this, Collections.singleton(instance), block);
				}
			}
		}
	}

	@Override
	public void deselectAll() {
		deselectAll(false);
	}

	@Override
	public void deselectAll(final boolean block) {
		if (selection.isEmpty() == false) {
			final HashSet<ScenarioInstance> copy = new HashSet<ScenarioInstance>(selection);
			selection.clear();
			setPinnedInstance(null);
			for (final IScenarioServiceSelectionChangedListener listener : listeners) {
				listener.deselected(this, copy, block);
			}
		}
	}

	@Override
	public boolean isSelected(final ScenarioInstance instance) {
		return selection.contains(instance);
	}

	@Override
	public Collection<ScenarioInstance> getSelection() {
		return Collections.unmodifiableSet(selection);
	}

	@Override
	public void addSelectionChangedListener(final IScenarioServiceSelectionChangedListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(final IScenarioServiceSelectionChangedListener listener) {
		listeners.remove(listener);
	}

	/**
	 * @param instance
	 */
	public void toggleSelection(final ScenarioInstance instance, final boolean block) {
		if (isSelected(instance))
			deselect(instance, block);
		else
			select(instance, block);
	}

	@Override
	public ScenarioInstance getPinnedInstance() {
		return pin;
	}

	@Override
	public void setPinnedInstance(final ScenarioInstance instance) {
		setPinnedInstance(instance, false);
	}

	@Override
	public void setPinnedInstance(final ScenarioInstance instance, final boolean block) {
		if (pin != instance) {
			final ScenarioInstance oldPin = pin;
			pin = instance;
			if (instance != null && !isSelected(instance)) {
				select(instance, block);
			} else if (oldPin != null && pin == null) {
				// deselect(instance);
			}
			// Enforce one pin, one other scenario.
			if (pin != null && selection.size() > 2) {
				for (final ScenarioInstance scenarioInstance : new ArrayList<>(selection)) {
					if (scenarioInstance == pin) {
						continue;
					}
					deselect(scenarioInstance, block);
				}
			}
			for (final IScenarioServiceSelectionChangedListener listener : listeners) {
				listener.pinned(this, oldPin, pin, block);
			}
		}
	}
}
