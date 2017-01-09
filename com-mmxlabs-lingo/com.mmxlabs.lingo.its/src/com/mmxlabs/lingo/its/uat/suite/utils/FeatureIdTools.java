/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.utils;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;

public class FeatureIdTools {
	public static String getId(@NonNull final EStructuralFeature feature) {
		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/comparison/sheetId");
		String id = null;
		if (annotation != null) {
			id = (String) annotation.getDetails().get("id");
		}
		return id;
	}

	public static Integer getMultiplier(@NonNull final EStructuralFeature feature) {
		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/comparison/sheetId");
		Integer multiplier = null;
		if (annotation != null) {
			final String multiplier_s = (String) annotation.getDetails().get("multiplier");
			if (multiplier_s == null) {
				return null;
			}
			multiplier = Integer.valueOf((String) annotation.getDetails().get("multiplier"));
		}
		return multiplier;
	}

}
