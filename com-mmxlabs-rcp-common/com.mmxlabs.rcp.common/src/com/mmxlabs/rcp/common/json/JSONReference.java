/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONReference {
	private String classType;
	private String name;
	private String globalId; // E.g. mmxid

	public JSONReference() {
	}

	public JSONReference(final String classType, final String name, final String globalId) {
		this.classType = classType;
		this.name = name;
		this.globalId = globalId;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(final String classType) {
		this.classType = classType;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(final String globalId) {
		this.globalId = globalId;
	}

	public static JSONReference of(final EObject eObject) {
		final EStructuralFeature nameFeature = eObject.eClass().getEStructuralFeature("name");
		final EStructuralFeature mmxidFeature = eObject.eClass().getEStructuralFeature("mmxid");
		String name = nameFeature == null ? null : (String) eObject.eGet(nameFeature);

		// Try to find the unique ID for the object.
		String id = mmxidFeature == null ? null : (String) eObject.eGet(mmxidFeature);
		if (id == null) {
			for (final EOperation op : eObject.eClass().getEOperations()) {
				if (op.getName().equalsIgnoreCase("mmxid")) {
					try {
						id = (String) eObject.eInvoke(op, ECollections.emptyEList());
						break;
					} catch (final InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if (id == null) {
			// Does the object expose a jsonid method to obtain a reproducible identifier?
			for (final EOperation op : eObject.eClass().getEOperations()) {
				if (op.getName().equalsIgnoreCase("jsonid")) {
					try {
						id = (String) eObject.eInvoke(op, ECollections.emptyEList());
						break;
					} catch (final InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if (id == null) {
			// Finally fall back to uuid. This is not reliable across model instances.
			for (final EAttribute attrib : eObject.eClass().getEAllAttributes()) {
				if (attrib.getName().equalsIgnoreCase("uuid")) {
					id = (String) eObject.eGet(attrib);
					break;
				}
			}
		}
		String type = String.format("%s/%s", eObject.eClass().getEPackage().getNsURI(), eObject.eClass().getName());
		// Use ID as the name if not found.
		if (name == null) {
			name = id;
		}
		return new JSONReference(type, name, id);

	}
}
