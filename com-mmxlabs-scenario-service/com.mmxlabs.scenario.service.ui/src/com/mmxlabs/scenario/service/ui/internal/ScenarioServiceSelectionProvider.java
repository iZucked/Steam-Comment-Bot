/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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

	private void doSelect(@NonNull final ScenarioInstance instance, final boolean block) {
		if (!isSelected(instance)) {
			// Enforce one pin, one other scenario.
			if (pin != null && selection.size() > 1) {
				for (final ScenarioInstance scenarioInstance : new ArrayList<>(selection)) {
					assert scenarioInstance != null;
					if (scenarioInstance == instance) {
						continue;
					}
					if (scenarioInstance == pin) {
						continue;
					}
					doDeselect(scenarioInstance, block);
				}
			}
			selection.add(instance);
			fireSelectedEvent(instance, block);
		}
	}

	@Override
	public void select(final ScenarioInstance instance, final boolean block) {
		doSelect(instance, block);
		fireSelectionChangedEvent(block);
	}

	@Override
	public void deselect(final ScenarioInstance instance) {
		deselect(instance, false);
	}

	private void doDeselect(@NonNull final ScenarioInstance instance, final boolean block) {
		if (isSelected(instance)) {
			if (instance == pin) {
				doSetPinnedInstance(null, block);
			} else {
				selection.remove(instance);
				fireDeselectedEvent(instance, block);
			}
		}
	}

	@Override
	public void deselect(@NonNull final ScenarioInstance instance, final boolean block) {
		doDeselect(instance, block);
		fireSelectionChangedEvent(block);
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
			final List<ScenarioInstance> others = new LinkedList<>(selection);
			others.remove(pin);
			fireDeselectedEvent(copy, block);
		}
		fireSelectionChangedEvent(block);

	}

	@Override
	public boolean isSelected(@NonNull final ScenarioInstance instance) {
		return selection.contains(instance);
	}

	@SuppressWarnings("null")
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
	public void toggleSelection(@NonNull final ScenarioInstance instance, final boolean block) {
		if (isSelected(instance)) {
			deselect(instance, block);
		} else {
			select(instance, block);
		}
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
	public void setPinnedPair(final ScenarioInstance pinInstance, final ScenarioInstance otherInstance, final boolean block) {
		if (pin != pinInstance || !selection.contains(otherInstance)) {

			for (final ScenarioInstance scenarioInstance : new ArrayList<>(selection)) {
				assert scenarioInstance != null;
				if (scenarioInstance == pinInstance) {
					continue;
				}
				if (scenarioInstance == otherInstance) {
					continue;
				}
				doDeselect(scenarioInstance, block);
			}

			doSetPinnedInstance(pinInstance, block);
			doSelect(pinInstance, block);
			doSelect(otherInstance, block);

			fireSelectionChangedEvent(block);
		}
	}

	private void doSetPinnedInstance(@Nullable final ScenarioInstance instance, final boolean block) {
		if (pin != instance) {
			final ScenarioInstance oldPin = pin;
			pin = instance;
			if (instance != null && !isSelected(instance)) {
				doSelect(instance, block);
			} else if (oldPin != null && pin == null) {
				// deselect(instance);
			}
			// Enforce one pin, one other scenario.
			if (pin != null && selection.size() > 2) {
				for (final ScenarioInstance scenarioInstance : new ArrayList<>(selection)) {
					assert scenarioInstance != null;
					if (scenarioInstance == pin) {
						continue;
					}
					doDeselect(scenarioInstance, block);
				}
			}
			firePinnedEvent(oldPin, pin, block);
		}
	}

	@Override
	public void setPinnedInstance(@Nullable final ScenarioInstance instance, final boolean block) {
		doSetPinnedInstance(instance, block);
		fireSelectionChangedEvent(block);
	}

	private void fireSelectedEvent(@NonNull Collection<ScenarioInstance> instances, boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.selected(this, instances, block);
		}
	}

	private void fireSelectedEvent(@NonNull ScenarioInstance instance, boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.selected(this, Collections.singleton(instance), block);
		}
	}

	private void fireDeselectedEvent(@NonNull ScenarioInstance instance, boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.deselected(this, Collections.singleton(instance), block);
		}
	}

	private void fireDeselectedEvent(@NonNull Collection<ScenarioInstance> instances, boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.deselected(this, instances, block);
		}
	}

	private void firePinnedEvent(@Nullable ScenarioInstance oldPin, @Nullable ScenarioInstance newPin, boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.pinned(this, oldPin, newPin, block);
		}
	}

	private void fireSelectionChangedEvent(final boolean block) {
		final List<ScenarioInstance> others = new LinkedList<>(selection);
		others.remove(pin);
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.selectionChanged(pin, others, block);
		}
	}
}
