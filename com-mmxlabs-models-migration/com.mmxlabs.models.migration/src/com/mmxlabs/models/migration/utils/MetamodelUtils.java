/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A set of helper methods to make manipulating metamodels simpler.
 * 
 * @author Simon Goodall
 * 
 */
public class MetamodelUtils {

	public static EClass getEClass(final EPackage ePackage, final String className) {
		return (EClass) ePackage.getEClassifier(className);
	}

	public static EEnum getEEnum(final EPackage ePackage, final String className) {
		return (EEnum) ePackage.getEClassifier(className);
	}

	public static EStructuralFeature getStructuralFeature(final EClass eClass, final String featureName) {
		return eClass.getEStructuralFeature(featureName);
	}

	public static EReference getReference(final EClass eClass, final String featureName) {
		return (EReference) eClass.getEStructuralFeature(featureName);
	}

	public static EAttribute getAttribute(final EClass eClass, final String featureName) {
		return (EAttribute) eClass.getEStructuralFeature(featureName);
	}

	@SuppressWarnings("unchecked")
	public static <T> EList<T> getValueAsTypedList(final EObject owner, final EStructuralFeature feature) {
		if (owner.eIsSet(feature)) {
			final Object obj = owner.eGet(feature);
			if (obj instanceof EList<?>) {
				return (EList<T>) obj;
			}
		}
		return null;
	}

	/**
	 * @since 2.0
	 */
	public static EEnumLiteral getEEnum_Literal(final EEnum eEnum, final String literal) {

		return eEnum.getEEnumLiteral(literal);
	}
}
