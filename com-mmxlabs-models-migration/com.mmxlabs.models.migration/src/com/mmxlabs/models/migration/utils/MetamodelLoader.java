/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.mmxlabs.models.migration.PackageData;

/**
 * A class to manage loading a set of ecore models into {@link EPackage} instances.
 * 
 * @author Simon Goodall
 * 
 */
public class MetamodelLoader {

	private final Resource.Factory ecoreResourceFactory;
	private final Resource.Factory.Registry factoryRegistry;
	private final ResourceSet resourceSet;

	public MetamodelLoader() {
		ecoreResourceFactory = new XMIResourceFactoryImpl();
		factoryRegistry = new ResourceFactoryRegistryImpl();

		factoryRegistry.getExtensionToFactoryMap().put("ecore", ecoreResourceFactory);
		factoryRegistry.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

		// Create empty package registry to manipulate and register default ecore package
		resourceSet = new ResourceSetImpl();
		final EPackageRegistryImpl packageRegistry = new EPackageRegistryImpl();
		packageRegistry.put("http://www.eclipse.org/emf/2002/Ecore", EcorePackage.eINSTANCE);
		resourceSet.setPackageRegistry(packageRegistry);

		resourceSet.setResourceFactoryRegistry(factoryRegistry);
	}

	/**
	 * Load an EPackage from the given location and register it under it's nsURI in the package registry
	 * 
	 * @param location
	 *            Location to read ecore model from
	 * @param string
	 * @return
	 * @since 3.0
	 */
	public EPackage loadEPackage(final URI location) {

		final Resource rs = resourceSet.getResource(location, true);
		final EPackage ePackage = (EPackage) rs.getContents().get(0);

		if (ePackage == null) {
			throw new RuntimeException("Unable to load package");
		}

		// Register the package
		resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);

		// Map this package name space to the underlying location
		resourceSet.getURIConverter().getURIMap().put(URI.createURI(ePackage.getNsURI()), location);

		return ePackage;
	}

	public EPackage getPackageByNSURI(final String nsURI) {
		return resourceSet.getPackageRegistry().getEPackage(nsURI);
	}

	public EClassifier getClassifier(final EPackage ePackage, final String classifierName) {
		return ePackage.getEClassifier(classifierName);
	}

	/**
	 * Returns the {@link ResourceSet} used by this metamodel loader. Be careful about reusing this to load model instances.
	 * 
	 * @return
	 */
	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	/**
	 * Loads and registers an ePackage from the given location. Use the {@link PackageData#getResourceURIs()} list as extra paths to map to the package location.
	 * 
	 * @param location
	 * @param packageData
	 * @return
	 */
	public EPackage loadEPackage(final URI location, final PackageData packageData) {
		final EPackage pkg = loadEPackage(location);

		// Add in extra resource mappings
		for (final String resourceURI : packageData.getResourceURIs()) {
			resourceSet.getURIConverter().getURIMap().put(URI.createURI(resourceURI), location);
		}

		return pkg;
	}

	/**
	 * Loads and registers an ePackage from the given location. Use the resourceURI as an extra path to map to the package location.
	 * 
	 * @param location
	 * @param resourceURI
	 * @return
	 */
	public EPackage loadEPackage(final URI location, final String resourceURI) {
		final EPackage pkg = loadEPackage(location);

		// Add in extra resource mapping
		resourceSet.getURIConverter().getURIMap().put(URI.createURI(resourceURI), location);

		return pkg;
	}

	/**
	 * Loads and registers an ePackage from the given location. Use the resourceURI as an extra path to map to the package location. nsURI is ignored
	 * 
	 * @param location
	 * @param nsURI
	 *            - ignored
	 * @param resourceURI
	 * @return
	 * @deprecated - Use {@link #loadEPackage(URI, String)} instead
	 */
	@Deprecated
	public EPackage loadEPackage(final URI location, final String nsURI, final String resourceURI) {
		return loadEPackage(location, resourceURI);
	}
}
