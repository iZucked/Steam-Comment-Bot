/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint And Fitness Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getSimilaritySettings <em>Similarity Settings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getConstraintAndFitnessSettings()
 * @model
 * @generated
 */
public interface ConstraintAndFitnessSettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Objectives</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Objective}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Objectives</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Objectives</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getConstraintAndFitnessSettings_Objectives()
	 * @model containment="true"
	 * @generated
	 */
	EList<Objective> getObjectives();

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Constraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getConstraintAndFitnessSettings_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Floating Days Limit</b></em>' attribute.
	 * The default value is <code>"15"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Floating Days Limit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Floating Days Limit</em>' attribute.
	 * @see #setFloatingDaysLimit(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getConstraintAndFitnessSettings_FloatingDaysLimit()
	 * @model default="15"
	 * @generated
	 */
	int getFloatingDaysLimit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Floating Days Limit</em>' attribute.
	 * @see #getFloatingDaysLimit()
	 * @generated
	 */
	void setFloatingDaysLimit(int value);

	/**
	 * Returns the value of the '<em><b>Similarity Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Similarity Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Similarity Settings</em>' containment reference.
	 * @see #setSimilaritySettings(SimilaritySettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getConstraintAndFitnessSettings_SimilaritySettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SimilaritySettings getSimilaritySettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings#getSimilaritySettings <em>Similarity Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Similarity Settings</em>' containment reference.
	 * @see #getSimilaritySettings()
	 * @generated
	 */
	void setSimilaritySettings(SimilaritySettings value);

} // ConstraintAndFitnessSettings
