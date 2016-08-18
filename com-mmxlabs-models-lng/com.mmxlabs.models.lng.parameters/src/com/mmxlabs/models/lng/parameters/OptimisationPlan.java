/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Optimisation Plan</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getUserSettings <em>User Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getStages <em>Stages</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getSolutionBuilderSettings <em>Solution Builder Settings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationPlan()
 * @model
 * @generated
 */
public interface OptimisationPlan extends EObject {
	/**
	 * Returns the value of the '<em><b>User Settings</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Settings</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Settings</em>' reference.
	 * @see #setUserSettings(UserSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationPlan_UserSettings()
	 * @model
	 * @generated
	 */
	UserSettings getUserSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getUserSettings <em>User Settings</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Settings</em>' reference.
	 * @see #getUserSettings()
	 * @generated
	 */
	void setUserSettings(UserSettings value);

	/**
	 * Returns the value of the '<em><b>Stages</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.OptimisationStage}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stages</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stages</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationPlan_Stages()
	 * @model containment="true"
	 * @generated
	 */
	EList<OptimisationStage> getStages();

	/**
	 * Returns the value of the '<em><b>Solution Builder Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Solution Builder Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Solution Builder Settings</em>' containment reference.
	 * @see #setSolutionBuilderSettings(SolutionBuilderSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationPlan_SolutionBuilderSettings()
	 * @model containment="true"
	 * @generated
	 */
	SolutionBuilderSettings getSolutionBuilderSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationPlan#getSolutionBuilderSettings <em>Solution Builder Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Solution Builder Settings</em>' containment reference.
	 * @see #getSolutionBuilderSettings()
	 * @generated
	 */
	void setSolutionBuilderSettings(SolutionBuilderSettings value);

} // OptimisationPlan
