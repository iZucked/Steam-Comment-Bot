/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;

import scenario.NamedObject;

import com.mmxlabs.common.Pair;

/**
 * Tracks all objects with a "name" or an "id" in a scenario for us
 * 
 * @author Tom Hinton
 * 
 */
public class NamedObjectRegistry {
	private final Map<Pair<EClass, String>, EObject> contents = new HashMap<Pair<EClass, String>, EObject>();

	public NamedObjectRegistry() {

	}

	/**
	 * Add an EObject to the registry ({@link #addEObject(EObject)}, and add all its contents.
	 * 
	 * @param top
	 */
	public void addEObjects(final EObject top) {
		if (top == null) {
			return;
		}
		addEObject(top);
		final TreeIterator<EObject> iterator = top.eAllContents();
		while (iterator.hasNext()) {
			addEObject(iterator.next());
		}
	}

	private void addAbstractSuperclasses(final EClass superClass, final EObject object, final String id) {
		if (superClass.isAbstract()) {
			contents.put(new Pair<EClass, String>(superClass, id), object);
		}

		for (final EClass superSuperClass : superClass.getEAllSuperTypes()) {
			addAbstractSuperclasses(superSuperClass, object, id);
		}
	}

	/**
	 * Add an EObject to the registry, but ignore its contents.
	 * 
	 * @param top
	 */
	public void addEObject(final EObject top) {
		if (top == null) {
			return;
		}
		final String id = getName(top);
		if ((id != null) && (id.isEmpty() == false)) {
			contents.put(new Pair<EClass, String>(top.eClass(), id), top);

			// process superclasses
			for (final EClass superclass : top.eClass().getEAllSuperTypes()) {
				addAbstractSuperclasses(superclass, top, id);
			}
		}
	}

	/**
	 * Get the name / id field of an object.
	 * 
	 * @param top
	 * @return
	 */
	public static String getName(final EObject top) {
		if (top == null) {
			return null;
		}
		if (top instanceof NamedObject) {
			return ((NamedObject) top).getName();
		}
		final EDataType stringType = EcorePackage.eINSTANCE.getEString();
		for (final EAttribute attribute : top.eClass().getEAllAttributes()) {
			if (attribute.getEAttributeType().equals(stringType)) {
				if (attribute.getName().equalsIgnoreCase("name") || attribute.getName().equalsIgnoreCase("id")) {
					final String id = (String) top.eGet(attribute);
					return id;
				}
			}
		}
		return null;
	}

	public Map<Pair<EClass, String>, EObject> getContents() {
		return Collections.unmodifiableMap(contents);
	}
}
