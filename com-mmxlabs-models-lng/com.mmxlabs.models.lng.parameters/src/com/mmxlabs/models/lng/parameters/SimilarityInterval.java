/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Similarity Interval</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SimilarityInterval#getWeight <em>Weight</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SimilarityInterval#getThreshold <em>Threshold</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilarityInterval()
 * @model
 * @generated
 */
public interface SimilarityInterval extends EObject {
	/**
	 * Returns the value of the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Weight</em>' attribute.
	 * @see #setWeight(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilarityInterval_Weight()
	 * @model required="true"
	 * @generated
	 */
	int getWeight();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilarityInterval#getWeight <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Weight</em>' attribute.
	 * @see #getWeight()
	 * @generated
	 */
	void setWeight(int value);

	/**
	 * Returns the value of the '<em><b>Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Threshold</em>' attribute.
	 * @see #setThreshold(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilarityInterval_Threshold()
	 * @model required="true"
	 * @generated
	 */
	int getThreshold();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilarityInterval#getThreshold <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Threshold</em>' attribute.
	 * @see #getThreshold()
	 * @generated
	 */
	void setThreshold(int value);

} // SimilarityInterval
