package com.mmxlabs.lingo.its.uat.utils;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;

public class FeatureIdTools {
	public static String getId(final EStructuralFeature feature) {
		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/comparison/sheetId");
		String id = null;
		if (annotation != null) {
			id = (String) annotation.getDetails().get("id");
		}
		return id;
	}
	
	public static Integer getMultiplier(final EStructuralFeature feature) {
		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/comparison/sheetId");
		Integer multiplier = null;
		if (annotation != null) {
			String multiplier_s = (String) annotation.getDetails().get("multiplier");
			if (multiplier_s == null) {
				return null;
			}
			multiplier = Integer.valueOf((String) annotation.getDetails().get("multiplier"));
		}
		return multiplier;
	}

}
