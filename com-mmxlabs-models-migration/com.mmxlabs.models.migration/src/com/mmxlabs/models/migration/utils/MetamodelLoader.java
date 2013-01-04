package com.mmxlabs.models.migration.utils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * A class to manage loading a set of ecore models into {@link EPackage} instances.
 * 
 * @author Simon Goodall
 * 
 */
public class MetamodelLoader {

	private final Resource.Factory ecoreResourceFactory;
	private final Resource.Factory.Registry factoryRegistry = Resource.Factory.Registry.INSTANCE;
	private final ResourceSet resourceSet;

	public MetamodelLoader() {
		ecoreResourceFactory = new XMIResourceFactoryImpl();

		factoryRegistry.getExtensionToFactoryMap().put("ecore", ecoreResourceFactory);
		factoryRegistry.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		resourceSet = new ResourceSetImpl();
		resourceSet.setResourceFactoryRegistry(factoryRegistry);
	}

	/**
	 * 
	 * @param location
	 *            Location to read ecore model from
	 * @param string 
	 * @return
	 */
	public EPackage loadEPackage(final URI location, String nsURI) {

		final Resource rs = resourceSet.getResource(location, true);
		final EPackage ePackage = (EPackage) rs.getContents().get(0);
		
		resourceSet.getPackageRegistry().put(nsURI, ePackage);
		resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		return ePackage;
	}

//	public EFactory loadEFactory(final URI location) {
//
//		final EPackage ePackage = loadEPackage(location);
//		if (ePackage != null) {
//			return ePackage.getEFactoryInstance();
//		}
//		return null;
//	}

	public EClassifier getClassifier(EPackage ePackage, String classifierName) {
		return ePackage.getEClassifier(classifierName);
	}

	public ResourceSet getResourceSet() {
		return resourceSet;
	}
}
