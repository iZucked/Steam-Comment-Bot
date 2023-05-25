package com.mmxlabs.lingo.reports.scheduleview.views.positionssequences;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.ui.IMemento;

public class EnabledProviderTracker<E extends Exception> {
	
	/**
	 * A map from some provider ID to an optional exception.
	 * 
	 * If a provider is enabled with no errors, it maps to an empty optional else 
	 * it maps to an exception for error reporting.
	 */
	private final Map<String, Optional<E>> enabledExtensionProviders = new HashMap<>();
	
	/**
	 * A boolean flag that is set to be true when the associated content provider's input changes.
	 */
	private boolean inputChanged = true;
	
	public void enable(String providerId) {
		enabledExtensionProviders.put(providerId, Optional.empty());
	}
	
	public void enableIfNoError(String providerId) {
		enabledExtensionProviders.putIfAbsent(providerId, Optional.empty());
	}
	
	public void addError(String providerId, E e) {
		enabledExtensionProviders.put(providerId, Optional.of(e));
	}
	
	public void disable(String providerId) {
		enabledExtensionProviders.remove(providerId);
	}
	
	public Optional<E> disableOrGetError(String providerId) {
		if (!enabledExtensionProviders.containsKey(providerId)) {
			return Optional.empty();
		}

		Optional<E> optError = enabledExtensionProviders.get(providerId);
		if (optError.isEmpty()) {
			this.disable(providerId);
		}

		return optError;
	}
	
	public void clearErrors() {
		enabledExtensionProviders.values().removeIf(v -> v.isPresent());
	}
	
	public boolean isEnabled(String providerId) {
		return enabledExtensionProviders.containsKey(providerId);
	}
	
	public boolean isEnabledWithNoError(String providerId) {
		return enabledExtensionProviders.containsKey(providerId) && enabledExtensionProviders.get(providerId).isEmpty();
	}
	
	public boolean hasNoCorrectlyEnabledExtensions() {
		return enabledExtensionProviders.values().stream().noneMatch(Optional::isEmpty);
	}
	
	public void saveToMemento(IMemento memento) {
		if (memento == null) return;
		for (var entry: enabledExtensionProviders.entrySet()) {
			if (entry.getValue().isEmpty()) {
				memento.putBoolean(entry.getKey(), true);
			}
		}
	}
	
	public boolean hasInputChanged() {
		return inputChanged;
	}
	
	public void setInputChanged(boolean b) {
		inputChanged = b;
	}
}
