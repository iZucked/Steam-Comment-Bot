package com.mmxlabs.models.ui.valueproviders;

import java.util.HashMap;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;


/**
 * Handles the extension point for value provider factories
 * 
 * @author hinton
 *
 */
public class ReferenceValueProviderFactoryRegistry {
	private final WeakHashMap<EClass, WeakHashMap<EReference, IReferenceValueProviderFactory>> cache = 
			new WeakHashMap<EClass, WeakHashMap<EReference, IReferenceValueProviderFactory>>();
	
	private final HashMap<String, IReferenceValueProviderFactory> cacheById = new HashMap<String, IReferenceValueProviderFactory>();

	private final String extensionPointID;
	
	public ReferenceValueProviderFactoryRegistry(final String extensionPointID) {
		this.extensionPointID = extensionPointID;
	}
	
	public synchronized IReferenceValueProviderFactory getValueProviderFactory(final EClass owner, final EReference reference) {
		WeakHashMap<EReference, IReferenceValueProviderFactory> subCache = cache.get(owner);
		if (subCache == null) {
			subCache = new WeakHashMap<EReference, IReferenceValueProviderFactory>();
			cache.put(owner, subCache);
		}
		
		if (!subCache.containsKey(reference)) {
			// create factory
			IReferenceValueProviderFactory factory = createFactory(owner, reference);
			subCache.put(reference, factory);
			return factory;
		} else {
			return subCache.get(reference);
		}		
	}

	/**
	 * Find a matching extension and create a factory, or return an existing factory with the same ID
	 * @param owner
	 * @param reference
	 * @return
	 */
	private IReferenceValueProviderFactory createFactory(final EClass owner,
			final EReference reference) {
		IConfigurationElement winner = null;
		int minOwnerDistance = Integer.MAX_VALUE;
		int minReferenceDistance = Integer.MAX_VALUE;
		for (final IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(extensionPointID)) {
			// match by owner eClass and reference eClass. owner eClass is more important for tie breaking
			final int ownerDistance = getMinimumGenerations(owner, element.getAttribute("ownerEClass"));
			final int referenceDistance = getMinimumGenerations(reference.getEReferenceType(), element.getAttribute("referenceEClass"));
			if ((ownerDistance < minOwnerDistance)
					|| (ownerDistance == minOwnerDistance && referenceDistance < minReferenceDistance)) {
				winner = element;
				minOwnerDistance = ownerDistance;
				minReferenceDistance = referenceDistance;
			}
		}
		IReferenceValueProviderFactory factory;
		if (winner != null) {
			final String id = winner.getAttribute("id");
			if (cacheById.containsKey(id)) {
				factory = cacheById.get(id);
			} else {
				try {
					factory = (IReferenceValueProviderFactory) winner.createExecutableExtension("factory");
				} catch (CoreException e) {
					factory = null;
				}
				cacheById.put(id, factory);
			}
		} else {
			factory = null;
		}
		
		return factory;
	}
	

	private int getMinimumGenerations(final EClass eClass,
			final String canonicalNameOfSuper) {
		if (eClass.getInstanceClass().getCanonicalName()
				.equals(canonicalNameOfSuper)) {
			return 0;
		}
		int result = Integer.MAX_VALUE;
		for (final EClass superClass : eClass.getESuperTypes()) {
			final int d = getMinimumGenerations(superClass,
					canonicalNameOfSuper);
			if (d - 1 < result - 1) {
				result = d + 1;
			}
		}
		return result;
	}
}
