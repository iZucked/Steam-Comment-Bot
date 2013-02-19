/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Simon Goodall
 * @noinstantiate This class is not intended to be instantiated by clients.
 */

public class EcoreHelper {

	/**
	 * Method to extract the package namespace URI from an XMI based ecore model
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String getPackageNS(final URI uri) throws Exception {

		// Use a single element array to store the result from parsing
		final String[] value = new String[1];

		// Use an URIConverter to get an InputStream from a URI
		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
		InputStream inputStream = null;
		try {
			inputStream = uc.createInputStream(uri, Collections.emptyMap());

			// TODO: There may well be a better way to do this. (I hope so...)

			// Use an XML parser to parse document
			final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			// Use handler to intercept the parsing an extract the NS URI attribute from document root.
			final DefaultHandler handler = new DefaultHandler() {

				private boolean firstElement = true;

				@Override
				public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {

					if (firstElement) {
						firstElement = false;

						if (qName.contains(":")) {
							final String[] split = qName.split(":");
							final String value2 = attributes.getValue("xmlns:" + split[0]);
							if (value2 != null) {
								value[0] = value2;
								// TODO: Break out of parsing early?
							}
						}
					}

					if (qName.equals("ecore:EPackage")) {
						// Store the result
						value[0] = attributes.getValue("nsURI");
						// TODO: Break out of parsing early?
					}

					super.startElement(uri, localName, qName, attributes);
				}

			};

			// Parse the document!
			parser.parse(inputStream, handler);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {

				}
			}
		}

		return value[0];
	}

	/**
	 * When an EClass has been moved between metamodels, this method will perform a deep copy of the given feature from the source EObject reference to the destination eobject. It is assumed both the
	 * source and the destination eobjects are the same EClass, but in different metamodels. Perform a deep copy of the
	 * 
	 * @since 2.0
	 */
	public static void copyEObjectFeature(final EObject sourceContainer, final EObject destinationContainer, final EStructuralFeature sourceFeature) {

		if (sourceContainer.eIsSet(sourceFeature) == false) {
			return;
		}

		final EPackage destPackage = destinationContainer.eClass().getEPackage();
		if (sourceFeature instanceof EReference) {
			final EReference sourceReference = (EReference) sourceFeature;

			if (sourceReference.isMany()) {
				// Hmm, might be primitives -- not as reference?
				final List<EObject> subRefs = MetamodelUtils.getValueAsTypedList(sourceContainer, sourceReference);
				final List<EObject> newSubRefs = new ArrayList<EObject>(subRefs.size());
				for (final EObject subRef : subRefs) {
					final EObject newSubRef = copyIfRequired(sourceContainer, destPackage, subRef);
					newSubRefs.add(newSubRef);
				}
				destinationContainer.eSet(MetamodelUtils.getStructuralFeature(destinationContainer.eClass(), sourceFeature.getName()), newSubRefs);
			} else {
				final EObject subRef = (EObject) sourceContainer.eGet(sourceReference);
				final EObject newSubRef = copyIfRequired(sourceContainer, destPackage, subRef);
				destinationContainer.eSet(MetamodelUtils.getStructuralFeature(destinationContainer.eClass(), sourceFeature.getName()), newSubRef);
			}

		} else if (sourceFeature instanceof EAttribute) {
			EAttribute eAttribute = (EAttribute) sourceFeature;
			EDataType eAttributeType = eAttribute.getEAttributeType();
			EPackage attribPackage = eAttributeType.getEPackage();
			EPackage containerPackage = sourceContainer.eClass().getEPackage();
			if (attribPackage.equals(containerPackage)) {
				EClassifier destClassifier = destPackage.getEClassifier(eAttributeType.getName());
				if (destClassifier instanceof EDataType) {
					Object destValue = destPackage.getEFactoryInstance().createFromString((EDataType)destClassifier, sourceContainer.eGet(eAttribute).toString());
					destinationContainer.eSet(MetamodelUtils.getStructuralFeature(destinationContainer.eClass(), sourceFeature.getName()), destValue);
				} else {
					destinationContainer.eSet(MetamodelUtils.getStructuralFeature(destinationContainer.eClass(), sourceFeature.getName()), sourceContainer.eGet(sourceFeature));
				}
			} else {
			destinationContainer.eSet(MetamodelUtils.getStructuralFeature(destinationContainer.eClass(), sourceFeature.getName()), sourceContainer.eGet(sourceFeature));
			}
		} else {
			destinationContainer.eSet(MetamodelUtils.getStructuralFeature(destinationContainer.eClass(), sourceFeature.getName()), sourceContainer.eGet(sourceFeature));
		}
	}

	/**
	 * This method will copy the EObject instance from one metamodel to another metamodel, if present, otherwise it will return the original object reference.
	 * 
	 * @since 2.0
	 */
	public static EObject copyIfRequired(final EObject sourceContainer, final EPackage destPackage, final EObject subRef) {
		EObject newSubRef = subRef;
		// Parent and child (or other relation) are in the same package -- probably needs to be converted.
		if (subRef.eClass().getEPackage().equals(sourceContainer.eClass().getEPackage())) {
			final EFactory destFactory = destPackage.getEFactoryInstance();
			// TODO: What if a datatype e.g. spot type?
			final EClass destClass = MetamodelUtils.getEClass(destPackage, subRef.eClass().getName());
			if (destClass != null) {
				final EObject clone = destFactory.create(destClass);
				// Deep copy of the object
				copyEObject(subRef, clone);
				newSubRef = clone;
			}
		}
		return newSubRef;
	}

	/**
	 * 
	 * @param source
	 * @param destination
	 * @since 2.0
	 */
	public static void copyEObject(final EObject source, final EObject destination) {
		// Copy feature data
		for (final EStructuralFeature f : source.eClass().getEAllStructuralFeatures()) {
			// Copy data
			copyEObjectFeature(source, destination, f);
		}
	}
}
