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
 * A representation of the model object '<em><b>Similarity Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getLowInterval <em>Low Interval</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getMedInterval <em>Med Interval</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getHighInterval <em>High Interval</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getOutOfBoundsWeight <em>Out Of Bounds Weight</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilaritySettings()
 * @model
 * @generated
 */
public interface SimilaritySettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Low Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Low Interval</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Low Interval</em>' containment reference.
	 * @see #setLowInterval(SimilarityInterval)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilaritySettings_LowInterval()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SimilarityInterval getLowInterval();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getLowInterval <em>Low Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Low Interval</em>' containment reference.
	 * @see #getLowInterval()
	 * @generated
	 */
	void setLowInterval(SimilarityInterval value);

	/**
	 * Returns the value of the '<em><b>Med Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Med Interval</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Med Interval</em>' containment reference.
	 * @see #setMedInterval(SimilarityInterval)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilaritySettings_MedInterval()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SimilarityInterval getMedInterval();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getMedInterval <em>Med Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Med Interval</em>' containment reference.
	 * @see #getMedInterval()
	 * @generated
	 */
	void setMedInterval(SimilarityInterval value);

	/**
	 * Returns the value of the '<em><b>High Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>High Interval</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>High Interval</em>' containment reference.
	 * @see #setHighInterval(SimilarityInterval)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilaritySettings_HighInterval()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SimilarityInterval getHighInterval();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getHighInterval <em>High Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>High Interval</em>' containment reference.
	 * @see #getHighInterval()
	 * @generated
	 */
	void setHighInterval(SimilarityInterval value);

	/**
	 * Returns the value of the '<em><b>Out Of Bounds Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Out Of Bounds Weight</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Out Of Bounds Weight</em>' attribute.
	 * @see #isSetOutOfBoundsWeight()
	 * @see #unsetOutOfBoundsWeight()
	 * @see #setOutOfBoundsWeight(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilaritySettings_OutOfBoundsWeight()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getOutOfBoundsWeight();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getOutOfBoundsWeight <em>Out Of Bounds Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Out Of Bounds Weight</em>' attribute.
	 * @see #isSetOutOfBoundsWeight()
	 * @see #unsetOutOfBoundsWeight()
	 * @see #getOutOfBoundsWeight()
	 * @generated
	 */
	void setOutOfBoundsWeight(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getOutOfBoundsWeight <em>Out Of Bounds Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetOutOfBoundsWeight()
	 * @see #getOutOfBoundsWeight()
	 * @see #setOutOfBoundsWeight(int)
	 * @generated
	 */
	void unsetOutOfBoundsWeight();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getOutOfBoundsWeight <em>Out Of Bounds Weight</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Out Of Bounds Weight</em>' attribute is set.
	 * @see #unsetOutOfBoundsWeight()
	 * @see #getOutOfBoundsWeight()
	 * @see #setOutOfBoundsWeight(int)
	 * @generated
	 */
	boolean isSetOutOfBoundsWeight();

} // SimilaritySettings
