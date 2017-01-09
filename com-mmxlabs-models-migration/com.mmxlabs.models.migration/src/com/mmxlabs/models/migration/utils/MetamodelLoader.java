/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.scenario.service.util.EncryptingXMIResourceFactory;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.util.encryption.impl.PassthroughCipherProvider;

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
		factoryRegistry.getExtensionToFactoryMap().put("*", new EncryptingXMIResourceFactory(getScenarioCipherProvider().getSharedCipher()));

		// Create empty package registry to manipulate and register default ecore package
		resourceSet = new ResourceSetImpl();
		final EPackageRegistryImpl packageRegistry = new EPackageRegistryImpl();
		packageRegistry.put("http://www.eclipse.org/emf/2002/Ecore", EcorePackage.eINSTANCE);
		resourceSet.setPackageRegistry(packageRegistry);

		resourceSet.setResourceFactoryRegistry(factoryRegistry);

		// Set Default Load Options
		{
			resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
			resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
			resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
		}
	}

	/**
	 * Load an EPackage from the given location and register it under it's nsURI in the package registry
	 * 
	 * @param location
	 *            Location to read ecore model from
	 * @param string
	 * @return
	 */
	public EPackage loadEPackage(final URI location) {

		final Resource rs = resourceSet.getResource(location, true);
		final EPackage ePackage = (EPackage) rs.getContents().get(0);

		if (ePackage == null) {
			throw new RuntimeException("Unable to load package");
		}

		// Override the default EFactory to return one which creates our DynamicEObjectWrapperImpl instances.
		ePackage.setEFactoryInstance(new EFactoryImpl() {
			@Override
			protected EObject basicCreate(EClass eClass) {
				return eClass.getInstanceClassName() == "java.util.Map$Entry" ? new DynamicEObjectImpl.BasicEMapEntry<String, String>(eClass) : new DynamicEObjectWrapperImpl(eClass);
			}

			@Override
			public Object createFromString(EDataType eDataType, String stringValue) {
				if (eDataType.getInstanceClassName() != null) {
					if (eDataType.getInstanceClassName().equals("java.time.YearMonth")) {
						return YearMonth.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM"));
					} else if (eDataType.getInstanceClassName().equals("java.time.LocalDate")) {
						return LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
					} else if (eDataType.getInstanceClassName().equals("java.time.LocalDateTime")) {
						return LocalDateTime.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
					} else if (eDataType.getInstanceClassName().equals("java.time.ZonedDateTime")) {
						try {
							return ZonedDateTime.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm VV"));
						} catch (DateTimeParseException e) {
							// Fallback
							return ZonedDateTime.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm Z"));
						}
					}
				}
				return super.createFromString(eDataType, stringValue);
			}

			@Override
			public String convertToString(EDataType eDataType, Object objectValue) {
				if (eDataType.getInstanceClassName() != null) {
					if (eDataType.getInstanceClassName().equals("java.time.YearMonth")) {
						return ((YearMonth) objectValue).format(DateTimeFormatter.ofPattern("yyyy/MM"));
					} else if (eDataType.getInstanceClassName().equals("java.time.LocalDate")) {
						return ((LocalDate) objectValue).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
					} else if (eDataType.getInstanceClassName().equals("java.time.LocalDateTime")) {
						return ((LocalDateTime) objectValue).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
					} else if (eDataType.getInstanceClassName().equals("java.time.ZonedDateTime")) {
						return ((ZonedDateTime) objectValue).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm VV"));
					}
				}
				return super.convertToString(eDataType, objectValue);
			}

		});

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

	@NonNull
	private static IScenarioCipherProvider getScenarioCipherProvider() {
		final Bundle bundle = FrameworkUtil.getBundle(MetamodelLoader.class);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
			if (serviceReference != null) {
				final IScenarioCipherProvider service = bundleContext.getService(serviceReference);
				if (service != null) {
					return service;
				}
			}
		}

		return new PassthroughCipherProvider();
	}
}
