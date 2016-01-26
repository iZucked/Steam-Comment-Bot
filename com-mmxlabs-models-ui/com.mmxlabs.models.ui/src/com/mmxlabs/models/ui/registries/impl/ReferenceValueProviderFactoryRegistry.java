/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.extensions.IReferenceValueProviderExtension;
import com.mmxlabs.models.ui.registries.IReferenceValueProviderFactoryRegistry;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.util.importer.registry.impl.AbstractRegistry;


/**
 * Handles the extension point for value provider factories
 * 
 * @author hinton
 *
 */
public class ReferenceValueProviderFactoryRegistry extends AbstractRegistry<Pair<EClass, EClass>, IReferenceValueProviderFactory> implements IReferenceValueProviderFactoryRegistry {
	@Inject Iterable<IReferenceValueProviderExtension> extensions;
	
	public ReferenceValueProviderFactoryRegistry() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.registries.impl.IReferenceValueProviderFactoryRegistry#getValueProviderFactory(org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	public synchronized IReferenceValueProviderFactory getValueProviderFactory(final EClass owner, final EReference reference) {
		return getValueProviderFactory(owner, reference.getEReferenceType());
	}

	@Override
	public synchronized IReferenceValueProviderFactory getValueProviderFactory(final EClass owner, final EClass type) {
		return get(new Pair<EClass, EClass>(owner, type));
	}
	
	@Override
	protected IReferenceValueProviderFactory match(final Pair<EClass, EClass> key) {
		IReferenceValueProviderExtension winner = null;
		int minOwnerDistance = Integer.MAX_VALUE;
		int minReferenceDistance = Integer.MAX_VALUE;
		final EClass owner = key.getFirst();
		final EClass reference = key.getSecond();
		for (final IReferenceValueProviderExtension extension : extensions) {
			final String ocn = extension.getOwnerEClassName();
			final String rcn = extension.getReferenceEClassName();

			// match by owner eClass and reference eClass. owner eClass is more important for tie breaking
			final int ownerDistance = getMinimumGenerations(owner, ocn);
			if (ownerDistance == Integer.MAX_VALUE && ocn != null) continue; // fail fast if an owner class is specified but is not a superclass of this type
			final int referenceDistance = getMinimumGenerations(reference, rcn);
			if (referenceDistance == Integer.MAX_VALUE && rcn != null) continue; // fail if reference class is specced but is not a superclass of this type.
			if ((ownerDistance < minOwnerDistance)
					|| (ownerDistance == minOwnerDistance && referenceDistance < minReferenceDistance)) {
				winner = extension;
				minOwnerDistance = ownerDistance;
				minReferenceDistance = referenceDistance;
			}
		}
		if (winner == null) return null;
		
		if (factoryExistsForID(winner.getID())) return getFactoryForID(winner.getID());
		else return cacheFactoryForID(winner.getID(), winner.instantiate());
	}
	

}
