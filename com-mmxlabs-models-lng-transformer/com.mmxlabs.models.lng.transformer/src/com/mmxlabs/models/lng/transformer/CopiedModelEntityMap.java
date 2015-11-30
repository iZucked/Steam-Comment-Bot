/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class CopiedModelEntityMap implements ModelEntityMap {

	@NonNull
	private final ModelEntityMap delegate;

	private final Map<EObject, EObject> originalToNewCopy = new HashMap<>();
	private final Map<EObject, EObject> newCopyToOriginal = new HashMap<>();

	private Copier copier;

	public CopiedModelEntityMap(@NonNull final ModelEntityMap delegate, @NonNull final EcoreUtil.Copier copier) {
		this.delegate = delegate;
		this.copier = copier;
		for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
			originalToNewCopy.put(e.getKey(), e.getValue());
			newCopyToOriginal.put(e.getValue(), e.getKey());
		}

		Collection<SpotSlot> allModelObjects = delegate.getAllModelObjects(SpotSlot.class);
		for (SpotSlot ss : allModelObjects) {
			if (!originalToNewCopy.containsKey(ss)) {
				EObject u =  copier.copy(ss);
				originalToNewCopy.put(ss,u);
				newCopyToOriginal.put(u, ss);
			}
		}
		copier.copyReferences();

	}

	@Override
	public <U> U getModelObject(final Object internalObject, final Class<? extends U> clz) {
		final U original = delegate.getModelObject(internalObject, clz);
		U u = (U) originalToNewCopy.get(original);
		if (original != null && u == null) {

			assert false;
		}
		return u;
	}

	@Override
	public <U> U getModelObjectNullChecked(final Object internalObject, final Class<? extends U> clz) {
		final U original = delegate.getModelObjectNullChecked(internalObject, clz);

		boolean a = originalToNewCopy.containsKey(original);
		boolean b = originalToNewCopy.containsValue(original);
		boolean c = newCopyToOriginal.containsKey(original);
		boolean d = newCopyToOriginal.containsValue(original);

		if (!(a || b || c || d)) {
			int ii = 0;
		}

		return (U) originalToNewCopy.get(original);
	}

	@Override
	public void addModelObject(final EObject modelObject, final Object internalObject) {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T getOptimiserObjectNullChecked(final EObject modelObject, final Class<? extends T> clz) {
		final EObject original = newCopyToOriginal.get(modelObject);
		return delegate.getOptimiserObjectNullChecked(original, clz);
	}

	@Override
	public <T> T getOptimiserObject(final EObject modelObject, final Class<? extends T> clz) {
		final EObject original = newCopyToOriginal.get(modelObject);
		return delegate.getOptimiserObject(original, clz);
	}

	@Override
	public <T extends EObject> Collection<T> getAllModelObjects(final Class<? extends T> clz) {
		final Collection<? extends T> allModelObjects = delegate.getAllModelObjects(clz);
		return allModelObjects.stream().map(t -> (T) originalToNewCopy.get(t)).collect(Collectors.toList());
	}

	@Override
	public ZonedDateTime getDateFromHours(final int hours, final String tz) {
		return delegate.getDateFromHours(hours, tz);
	}

	@Override
	public ZonedDateTime getDateFromHours(final int hours, final IPort port) {
		return delegate.getDateFromHours(hours, port);
	}

	@Override
	public ZonedDateTime getDateFromHours(final int hours, final Port port) {
		return delegate.getDateFromHours(hours, port);
	}
}
