/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

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
	
	public void select(final ScenarioInstance instance) {
		if (!isSelected(instance)) {
			selection.add(instance);
			for (final IScenarioServiceSelectionChangedListener listener : listeners) {
				listener.selected(this, Collections.singleton(instance));
			}
		}
	}
	
	public void deselect(final ScenarioInstance instance) {
		if (isSelected(instance)) {
			selection.remove(instance);
			if (instance == pin) setPinnedInstance(null);
			for (final IScenarioServiceSelectionChangedListener listener : listeners) {
				listener.deselected(this, Collections.singleton(instance));
			}
		}
	}
	
	public void deselectAll() {
		if (selection.isEmpty() == false) {
			final HashSet<ScenarioInstance> copy = new HashSet<ScenarioInstance>(selection);
			selection.clear();
			setPinnedInstance(null);
			for (final IScenarioServiceSelectionChangedListener listener : listeners) {
				listener.deselected(this, copy);
			}
		}
	}
	
	public boolean isSelected(final ScenarioInstance instance) {
		return selection.contains(instance);
	}
	
	@Override
	public Collection<ScenarioInstance> getSelection() {
		return Collections.unmodifiableSet(selection);
	}

	@Override
	public void addSelectionChangedListener(IScenarioServiceSelectionChangedListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeSelectionChangedListener(IScenarioServiceSelectionChangedListener listener) {
		listeners.remove(listener);
	}

	/**
	 * @param instance
	 */
	public void toggleSelection(ScenarioInstance instance) {
		if (isSelected(instance)) deselect(instance);
		else select(instance);
	}

	@Override
	public ScenarioInstance getPinnedInstance() {
		return pin;
	}
	
	public void setPinnedInstance(final ScenarioInstance instance) {
		if (pin != instance) {
			final ScenarioInstance oldPin = pin;
			pin = instance;
			if (instance != null && !isSelected(instance)) {
				select(instance);
			}
			for (final IScenarioServiceSelectionChangedListener listener : listeners) {
				listener.pinned(this, oldPin, pin);
			}
		}
	}
}
