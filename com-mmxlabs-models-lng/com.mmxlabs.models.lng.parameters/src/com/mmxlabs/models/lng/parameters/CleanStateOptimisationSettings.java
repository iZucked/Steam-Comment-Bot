/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Clean State Optimisation Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getGlobalIterations <em>Global Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getLocalIterations <em>Local Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getTabuSize <em>Tabu Size</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationSettings()
 * @model
 * @generated
 */
public interface CleanStateOptimisationSettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Global Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Global Iterations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Global Iterations</em>' attribute.
	 * @see #setGlobalIterations(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationSettings_GlobalIterations()
	 * @model required="true"
	 * @generated
	 */
	int getGlobalIterations();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getGlobalIterations <em>Global Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Global Iterations</em>' attribute.
	 * @see #getGlobalIterations()
	 * @generated
	 */
	void setGlobalIterations(int value);

	/**
	 * Returns the value of the '<em><b>Local Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Local Iterations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Local Iterations</em>' attribute.
	 * @see #setLocalIterations(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationSettings_LocalIterations()
	 * @model required="true"
	 * @generated
	 */
	int getLocalIterations();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getLocalIterations <em>Local Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Local Iterations</em>' attribute.
	 * @see #getLocalIterations()
	 * @generated
	 */
	void setLocalIterations(int value);

	/**
	 * Returns the value of the '<em><b>Tabu Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tabu Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tabu Size</em>' attribute.
	 * @see #setTabuSize(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationSettings_TabuSize()
	 * @model required="true"
	 * @generated
	 */
	int getTabuSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getTabuSize <em>Tabu Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tabu Size</em>' attribute.
	 * @see #getTabuSize()
	 * @generated
	 */
	void setTabuSize(int value);

	/**
	 * Returns the value of the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Seed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Seed</em>' attribute.
	 * @see #setSeed(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationSettings_Seed()
	 * @model required="true"
	 * @generated
	 */
	int getSeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings#getSeed <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Seed</em>' attribute.
	 * @see #getSeed()
	 * @generated
	 */
	void setSeed(int value);

} // CleanStateOptimisationSettings
