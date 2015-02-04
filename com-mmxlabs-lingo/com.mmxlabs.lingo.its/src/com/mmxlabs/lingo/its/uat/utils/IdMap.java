/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class IdMap {
	private String id;
	private EStructuralFeature feature;
	private EObject container;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public EObject getContainer() {
		return container;
	}
	public void setContainer(EObject container) {
		this.container = container;
	}
	public EStructuralFeature getFeature() {
		return feature;
	}
	public void setFeature(EStructuralFeature feature) {
		this.feature = feature;
	}
}