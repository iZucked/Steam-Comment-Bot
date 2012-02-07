package com.mmxlabs.models.ui.registries.impl;

import java.util.WeakHashMap;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.extensions.IReferenceValueProviderExtension;
import com.mmxlabs.models.ui.registries.IReferenceValueProviderFactoryRegistry;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;


/**
 * Handles the extension point for value provider factories
 * 
 * @author hinton
 *
 */
public class ReferenceValueProviderFactoryRegistry extends AbstractRegistry<Pair<EClass, EReference>, IReferenceValueProviderFactory> implements IReferenceValueProviderFactoryRegistry {
	@Inject Iterable<IReferenceValueProviderExtension> extensions;
	
	public ReferenceValueProviderFactoryRegistry() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.registries.impl.IReferenceValueProviderFactoryRegistry#getValueProviderFactory(org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	public synchronized IReferenceValueProviderFactory getValueProviderFactory(final EClass owner, final EReference reference) {
		return get(new Pair<EClass, EReference>(owner, reference));
	}

	@Override
	protected IReferenceValueProviderFactory match(final Pair<EClass, EReference> key) {
		IReferenceValueProviderExtension winner = null;
		int minOwnerDistance = Integer.MAX_VALUE;
		int minReferenceDistance = Integer.MAX_VALUE;
		final EClass owner = key.getFirst();
		final EReference reference = key.getSecond();
		for (final IReferenceValueProviderExtension extension : extensions) {
			// match by owner eClass and reference eClass. owner eClass is more important for tie breaking
			final int ownerDistance = getMinimumGenerations(owner, extension.getOwnerEClassName());
			final int referenceDistance = getMinimumGenerations(reference.getEReferenceType(), extension.getReferenceEClassName());
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
