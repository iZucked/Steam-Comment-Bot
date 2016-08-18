/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraints And Fitness Settings Stage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getConstraintsAndFitnessSettingsStage()
 * @model abstract="true"
 * @generated
 */
public interface ConstraintsAndFitnessSettingsStage extends OptimisationStage {
	/**
	 * Returns the value of the '<em><b>Constraint And Fitness Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraint And Fitness Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint And Fitness Settings</em>' containment reference.
	 * @see #setConstraintAndFitnessSettings(ConstraintAndFitnessSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getConstraintsAndFitnessSettingsStage_ConstraintAndFitnessSettings()
	 * @model containment="true"
	 * @generated
	 */
	ConstraintAndFitnessSettings getConstraintAndFitnessSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint And Fitness Settings</em>' containment reference.
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 */
	void setConstraintAndFitnessSettings(ConstraintAndFitnessSettings value);

} // ConstraintsAndFitnessSettingsStage
