/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
		final EClass eClassifier = (EClass) ePackage.getEClassifier(className);
		assert eClassifier != null;
		return eClassifier;
	}

	public static EEnum getEEnum(final EPackage ePackage, final String className) {
		final EEnum eClassifier = (EEnum) ePackage.getEClassifier(className);
		assert eClassifier != null;
		return eClassifier;
	}

	public static EStructuralFeature getStructuralFeature(final EClass eClass, final String featureName) {
		final EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(featureName);
		assert eStructuralFeature != null;
		return eStructuralFeature;
	}

	public static EReference getReference(final EClass eClass, final String featureName) {
		final EReference eStructuralFeature = (EReference) eClass.getEStructuralFeature(featureName);
		assert eStructuralFeature != null;
		return eStructuralFeature;
	}

	public static EAttribute getAttribute(final EClass eClass, final String featureName) {
		final EAttribute eStructuralFeature = (EAttribute) eClass.getEStructuralFeature(featureName);
		assert eStructuralFeature != null;
		return eStructuralFeature;
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
	 */
	public static EEnumLiteral getEEnum_Literal(final EEnum eEnum, final String literal) {

		final EEnumLiteral eEnumLiteral = eEnum.getEEnumLiteral(literal);
		assert eEnumLiteral != null;
		return eEnumLiteral;
	}

	/**
	 * Returns the {@link EDataType} from the given package
	 * 
	 * @param ePackage
	 * @param dataTypeName
	 *            The name of an {@link EDataType}
	 * @return
	 */
	public static EDataType getEDataType(final EPackage ePackage, final String dataTypeName) {
		final EDataType eClassifier = (EDataType) ePackage.getEClassifier(dataTypeName);
		assert eClassifier != null;
		return eClassifier;
	}
}
