/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.valueproviders;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

public class EntityProvider implements IReferenceValueProvider {
	public EntityProvider(final MMXRootObject rootObject) {
		
	}
	
	@Override
	public List<Pair<String, EObject>> getAllowedValues(EObject target,
			EStructuralFeature field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(EObject referer, EReference feature,
			EObject referenceValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer,
			EReference feature, EObject referenceValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateOnChangeToFeature(Object changedFeature) {
		// TODO Auto-generated method stub
		return false;
	}
}
