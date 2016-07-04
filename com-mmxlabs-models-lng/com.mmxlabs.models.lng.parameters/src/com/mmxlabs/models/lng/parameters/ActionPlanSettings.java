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
 * A representation of the model object '<em><b>Action Plan Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getTotalEvaluations <em>Total Evaluations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getInRunEvaluations <em>In Run Evaluations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getSearchDepth <em>Search Depth</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getActionPlanSettings()
 * @model
 * @generated
 */
public interface ActionPlanSettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Total Evaluations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Evaluations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Evaluations</em>' attribute.
	 * @see #setTotalEvaluations(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getActionPlanSettings_TotalEvaluations()
	 * @model required="true"
	 * @generated
	 */
	int getTotalEvaluations();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getTotalEvaluations <em>Total Evaluations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Evaluations</em>' attribute.
	 * @see #getTotalEvaluations()
	 * @generated
	 */
	void setTotalEvaluations(int value);

	/**
	 * Returns the value of the '<em><b>In Run Evaluations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Run Evaluations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Run Evaluations</em>' attribute.
	 * @see #setInRunEvaluations(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getActionPlanSettings_InRunEvaluations()
	 * @model required="true"
	 * @generated
	 */
	int getInRunEvaluations();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getInRunEvaluations <em>In Run Evaluations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Run Evaluations</em>' attribute.
	 * @see #getInRunEvaluations()
	 * @generated
	 */
	void setInRunEvaluations(int value);

	/**
	 * Returns the value of the '<em><b>Search Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Search Depth</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Search Depth</em>' attribute.
	 * @see #setSearchDepth(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getActionPlanSettings_SearchDepth()
	 * @model required="true"
	 * @generated
	 */
	int getSearchDepth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ActionPlanSettings#getSearchDepth <em>Search Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Search Depth</em>' attribute.
	 * @see #getSearchDepth()
	 * @generated
	 */
	void setSearchDepth(int value);

} // ActionPlanSettings
