/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;

public class IdMap {
	private final String id;
	private final EStructuralFeature feature;
	private final EObject container;

	public IdMap(@NonNull final String id, @NonNull final EStructuralFeature feature, @NonNull final EObject container) {
		this.id = id;
		this.feature = feature;
		this.container = container;
	}

	@NonNull
	public String getId() {
		return id;
	}

	@NonNull
	public EObject getContainer() {
		return container;
	}

	@NonNull
	public EStructuralFeature getFeature() {
		return feature;
	}

}