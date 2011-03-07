/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface IReferenceValueProvider {
	public Iterable<? extends EObject> getAllowedValues(
			final EObject target, final EStructuralFeature field);
}