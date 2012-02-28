/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.ArrayList;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * A reference value provider which is like simple reference value provider, but merges
 * the values of several containment references.
 * 
 * @author hinton
 *
 */
public class MergedReferenceValueProvider extends SimpleReferenceValueProvider {
	private final ArrayList<EReference> extraReferences;
	public MergedReferenceValueProvider(EObject container,
			EReference... containingReferences) {
		super(container, containingReferences[0]);
		extraReferences = new ArrayList<EReference>(containingReferences.length-1);
		for (int i = 1; i<containingReferences.length; i++) {
			extraReferences.add(containingReferences[i]);
		}
	}
	@Override
	protected boolean isRelevantTarget(Object target, Object feature) {
		return super.isRelevantTarget(target, feature)
				|| (target == container && extraReferences.contains(feature));
	}
	
	@Override
	protected EList<? extends EObject> getObjects() {
		final EList<EObject> values = new BasicEList<EObject>(super.getObjects());
		for (final EReference reference : extraReferences) {
			values.addAll((EList) container.eGet(reference));
		}
		return values;
	}
}
