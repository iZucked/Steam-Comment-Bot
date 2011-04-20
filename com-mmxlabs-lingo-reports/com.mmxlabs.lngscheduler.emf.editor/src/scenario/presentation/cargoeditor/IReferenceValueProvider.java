/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;

public interface IReferenceValueProvider {
	public List<Pair<String, EObject>> getAllowedValues(
			final EObject target, final EStructuralFeature field);
//	public String getName(final EObject target);
}