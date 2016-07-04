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
 * A representation of the model object '<em><b>Individual Solution Improvement Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#getIterations <em>Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#isImprovingSolutions <em>Improving Solutions</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getIndividualSolutionImprovementSettings()
 * @model
 * @generated
 */
public interface IndividualSolutionImprovementSettings extends EObject {
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
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getIndividualSolutionImprovementSettings_Iterations()
	 * @model required="true"
	 * @generated
	 */
	int getIterations();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#getIterations <em>Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Iterations</em>' attribute.
	 * @see #getIterations()
	 * @generated
	 */
	void setIterations(int value);

	/**
	 * Returns the value of the '<em><b>Improving Solutions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Improving Solutions</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Improving Solutions</em>' attribute.
	 * @see #setImprovingSolutions(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getIndividualSolutionImprovementSettings_ImprovingSolutions()
	 * @model
	 * @generated
	 */
	boolean isImprovingSolutions();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings#isImprovingSolutions <em>Improving Solutions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Improving Solutions</em>' attribute.
	 * @see #isImprovingSolutions()
	 * @generated
	 */
	void setImprovingSolutions(boolean value);

} // IndividualSolutionImprovementSettings
