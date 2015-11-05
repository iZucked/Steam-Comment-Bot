/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author Simon Goodall
 *
 */
public final class CopiedScenarioEntityMapping implements IScenarioEntityMapping {

	private final Map<EObject, EObject> originalToNewCopy = new HashMap<>();
	private final Map<EObject, EObject> newCopyToOriginal = new HashMap<>();
	private final IScenarioEntityMapping originalMapping;

	public CopiedScenarioEntityMapping(final IScenarioEntityMapping originalMapping, final EcoreUtil.Copier copier) {
		this.originalMapping = originalMapping;
		for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
			originalToNewCopy.put(e.getKey(), e.getValue());
			newCopyToOriginal.put(e.getValue(), e.getKey());
		}
	}

	@Override
	public void createMappings(final Map<EObject, EObject> originalToCopyMap) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createMapping(final EObject original, final EObject copy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void registerRemovedOriginal(final EObject original) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getOriginalFromCopy(final T copy) {

		final T original = originalMapping.getOriginalFromCopy(copy);
		boolean a = originalToNewCopy.containsKey(original);
		boolean b = originalToNewCopy.containsValue(original);
		assert originalToNewCopy.containsKey(original);
		return (T) originalToNewCopy.get(original);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getCopyFromOriginal(final T original) {
		assert newCopyToOriginal.containsValue(original);
		final T newCopy = (T) newCopyToOriginal.get(original);

		return originalMapping.getCopyFromOriginal(newCopy);
	}

	@Override
	public Collection<EObject> getUsedOriginalObjects() {

		return originalMapping.getUsedOriginalObjects().stream().map(t -> originalToNewCopy.get(t)).collect(Collectors.toSet());
	}

	@Override
	public Collection<EObject> getUnusedOriginalObjects() {
		return originalMapping.getUnusedOriginalObjects().stream().map(t -> originalToNewCopy.get(t)).collect(Collectors.toSet());
	}
}
