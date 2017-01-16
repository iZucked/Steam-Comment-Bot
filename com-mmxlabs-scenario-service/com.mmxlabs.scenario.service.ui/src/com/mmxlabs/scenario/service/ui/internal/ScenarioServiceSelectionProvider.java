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
import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Basic scenario selection service implementation
 * 
 * @author hinton
 * 
 */
public class ScenarioServiceSelectionProvider implements IScenarioServiceSelectionProvider {
	private final List<IScenarioServiceSelectionChangedListener> listeners = new LinkedList<IScenarioServiceSelectionChangedListener>();

	private final Set<ScenarioResult> selection = new HashSet<>();

	private @Nullable ScenarioResult pin = null;

	@Override
	public void select(final ScenarioResult instance) {
		select(instance, false);
	}

	private void doSelect(@NonNull final ScenarioResult instance, final boolean block) {
		if (!isSelected(instance)) {
			// Enforce one pin, one other scenario.
			if (pin != null && selection.size() > 1) {
				for (final ScenarioResult scenarioInstance : new ArrayList<>(selection)) {
					assert scenarioInstance != null;
					if (Objects.equals(scenarioInstance, instance)) {
						continue;
					}
					if (Objects.equals(scenarioInstance, pin)) {
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
	public void select(final ScenarioResult instance, final boolean block) {
		doSelect(instance, block);
		fireSelectionChangedEvent(block);
	}

	@Override
	public void deselect(final ScenarioResult instance) {
		deselect(instance, false);
	}

	@Override
	public void deselect(final ScenarioInstance instance) {
		deselect(instance, false);
	}

	private void doDeselect(@NonNull final ScenarioResult instance, final boolean block) {
		if (isSelected(instance)) {
			if (Objects.equals(instance, pin)) {
				doSetPinnedInstance(null, block);
			} else {
				selection.remove(instance);
				fireDeselectedEvent(instance, block);
			}
		}
	}

	@Override
	public void deselect(@NonNull final ScenarioResult instance, final boolean block) {
		doDeselect(instance, block);
		fireSelectionChangedEvent(block);
	}

	@Override
	public void deselect(@NonNull final ScenarioInstance instance, final boolean block) {
		if (selection.isEmpty() == false) {
			final Set<ScenarioResult> copy = new HashSet<>(selection);
			for (final ScenarioResult result : copy) {
				if (result.getScenarioInstance() == instance) {
					doDeselect(result, block);
				}
			}
		}
		fireSelectionChangedEvent(block);
	}

	@Override
	public void deselectAll() {
		deselectAll(false);
	}

	@Override
	public void deselectAll(final boolean block) {
		if (selection.isEmpty() == false) {
			final Set<ScenarioResult> copy = new HashSet<>(selection);
			selection.clear();
			setPinnedInstance((ScenarioResult) null);
			final List<ScenarioResult> others = new LinkedList<>(selection);
			others.remove(pin);
			fireDeselectedEvent(copy, block);
		}
		fireSelectionChangedEvent(block);

	}

	@Override
	public boolean isSelected(@NonNull final ScenarioResult instance) {
		return selection.contains(instance);
	}

	@Override
	public boolean isSelected(@NonNull final ScenarioInstance instance) {
		for (final ScenarioResult result : selection) {
			if (result.getScenarioInstance() == instance) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("null")
	@Override
	public Collection<ScenarioResult> getSelection() {
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
	public void toggleSelection(@NonNull final ScenarioResult instance, final boolean block) {
		if (isSelected(instance)) {
			deselect(instance, block);
		} else {
			select(instance, block);
		}
	}

	@Override
	public ScenarioResult getPinnedInstance() {
		return pin;
	}

	@Override
	public void setPinnedInstance(final ScenarioInstance instance) {
		ScenarioResult scenarioResult = new ScenarioResult(instance);
		setPinnedInstance(scenarioResult, false);
	}

	@Override
	public void setPinnedInstance(final ScenarioResult instance) {
		setPinnedInstance(instance, false);
	}

	@Override
	public void setPinnedPair(final ScenarioResult pinInstance, final ScenarioResult otherInstance, final boolean block) {
		if (!Objects.equals(pin, pinInstance) || !selection.contains(otherInstance)) {

			// Deselect results which are not part of the selection pair.
			for (final ScenarioResult scenarioInstance : new ArrayList<>(selection)) {
				assert scenarioInstance != null;
				if (Objects.equals(scenarioInstance, pinInstance)) {
					continue;
				}
				if (Objects.equals(scenarioInstance, otherInstance)) {
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

	private void doSetPinnedInstance(@Nullable final ScenarioResult instance, final boolean block) {
		if (!Objects.equals(pin, instance)) {
			final ScenarioResult oldPin = pin;
			pin = instance;
			if (instance != null && !isSelected(instance)) {
				doSelect(instance, block);
			} else if (oldPin != null && pin == null) {
				// deselect(instance);
			}
			// Enforce one pin, one other scenario.
			if (pin != null && selection.size() > 2) {
				for (final ScenarioResult scenarioInstance : new ArrayList<>(selection)) {
					assert scenarioInstance != null;
					if (Objects.equals(scenarioInstance, pin)) {
						continue;
					}
					doDeselect(scenarioInstance, block);
				}
			}
			firePinnedEvent(oldPin, pin, block);
		}
	}

	@Override
	public void setPinnedInstance(@Nullable final ScenarioResult instance, final boolean block) {
		doSetPinnedInstance(instance, block);
		fireSelectionChangedEvent(block);
	}

	private void fireSelectedEvent(@NonNull final Collection<ScenarioResult> instances, final boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.selected(this, instances, block);
		}
	}

	private void fireSelectedEvent(@NonNull final ScenarioResult instance, final boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.selected(this, Collections.singleton(instance), block);
		}
	}

	private void fireDeselectedEvent(@NonNull final ScenarioResult instance, final boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.deselected(this, Collections.singleton(instance), block);
		}
	}

	private void fireDeselectedEvent(@NonNull final Collection<ScenarioResult> instances, final boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.deselected(this, instances, block);
		}
	}

	private void firePinnedEvent(@Nullable final ScenarioResult oldPin, @Nullable final ScenarioResult newPin, final boolean block) {
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.pinned(this, oldPin, newPin, block);
		}
	}

	private void fireSelectionChangedEvent(final boolean block) {
		final List<ScenarioResult> others = new LinkedList<>(selection);
		others.remove(pin);
		for (final IScenarioServiceSelectionChangedListener listener : listeners) {
			listener.selectionChanged(pin, others, block);
		}
	}

	@Override
	public boolean isPinned(@NonNull final ScenarioResult result) {
		return pin != null && Objects.equals(pin, result);
	}

	@Override
	public boolean isPinned(@NonNull final ScenarioInstance instance) {
		if (pin != null) {
			if (pin.getScenarioInstance() == instance) {
				return true;
			}
		}
		return false;
	}

}
