/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insertion Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.InsertionOptimisationStage#getIterations <em>Iterations</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getInsertionOptimisationStage()
 * @model
 * @generated
 */
public interface InsertionOptimisationStage extends ConstraintsAndFitnessSettingsStage {
	/**
	 * Returns the value of the '<em><b>Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Iterations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Iterations</em>' attribute.
	 * @see #setIterations(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getInsertionOptimisationStage_Iterations()
	 * @model
	 * @generated
	 */
	int getIterations();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.InsertionOptimisationStage#getIterations <em>Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Iterations</em>' attribute.
	 * @see #getIterations()
	 * @generated
	 */
	void setIterations(int value);

} // InsertionOptimisationStage
