/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;

/**
 * Tracks all objects with a "name" or an "id" in a scenario for us
 * @author Tom Hinton
 * 
 */
public class NamedObjectRegistry {
	private Map<Pair<EClass, String>, EObject> contents = new HashMap<Pair<EClass, String>, EObject>();

	public NamedObjectRegistry() {

	}

	public void addEObjects(final EObject top) {
		addEObject(top);
		final TreeIterator<EObject> iterator = top.eAllContents();
		while (iterator.hasNext()) {
			addEObject(iterator.next());
		}
	}
	
	public void addEObject(final EObject top) {
		final EDataType stringType = EcorePackage.eINSTANCE.getEString();
		for (final EAttribute attribute : top.eClass().getEAllAttributes()) {
			if (attribute.getEAttributeType().equals(stringType)) {
				if (attribute.getName().equalsIgnoreCase("name") || attribute.getName().equalsIgnoreCase("id")) {
					// add to registry for type
					final String id = (String) top.eGet(attribute);
					contents.put(new Pair<EClass, String>(top.eClass(), id), top);
					// also add for abstract supertypes;
					// TODO check they have an id field?
					for (final EClass superclass : top.eClass().getEAllSuperTypes()) {
						if (superclass.isAbstract()) {
							contents.put(new Pair<EClass, String>(superclass, id), top);
						}
					}
				}
			}
		}
	}

	public Map<Pair<EClass, String>, EObject> getContents() {
		return Collections.unmodifiableMap(contents);
	}
}
