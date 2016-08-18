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
 * A representation of the model object '<em><b>Solution Builder Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SolutionBuilderSettings#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSolutionBuilderSettings()
 * @model
 * @generated
 */
public interface SolutionBuilderSettings extends EObject {
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
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSolutionBuilderSettings_ConstraintAndFitnessSettings()
	 * @model containment="true"
	 * @generated
	 */
	ConstraintAndFitnessSettings getConstraintAndFitnessSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SolutionBuilderSettings#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint And Fitness Settings</em>' containment reference.
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 */
	void setConstraintAndFitnessSettings(ConstraintAndFitnessSettings value);

} // SolutionBuilderSettings
