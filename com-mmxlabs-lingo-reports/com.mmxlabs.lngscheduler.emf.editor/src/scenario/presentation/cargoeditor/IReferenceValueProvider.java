/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;

public interface IReferenceValueProvider {
	public List<Pair<String, EObject>> getAllowedValues(final EObject target,
			final EStructuralFeature field);

	public String getName(final EObject referer, final EReference feature,
			final EObject referenceValue);

	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(
			final EObject referer, final EReference feature,
			final EObject referenceValue);
}