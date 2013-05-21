/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annealing Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getIterations <em>Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getEpochLength <em>Epoch Length</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getCooling <em>Cooling</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getInitialTemperature <em>Initial Temperature</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getAnnealingSettings()
 * @model
 * @generated
 */
public interface AnnealingSettings extends EObject {
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
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getAnnealingSettings_Iterations()
	 * @model required="true"
	 * @generated
	 */
	int getIterations();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getIterations <em>Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Iterations</em>' attribute.
	 * @see #getIterations()
	 * @generated
	 */
	void setIterations(int value);

	/**
	 * Returns the value of the '<em><b>Epoch Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Epoch Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Epoch Length</em>' attribute.
	 * @see #setEpochLength(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getAnnealingSettings_EpochLength()
	 * @model required="true"
	 * @generated
	 */
	int getEpochLength();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getEpochLength <em>Epoch Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Epoch Length</em>' attribute.
	 * @see #getEpochLength()
	 * @generated
	 */
	void setEpochLength(int value);

	/**
	 * Returns the value of the '<em><b>Cooling</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooling</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooling</em>' attribute.
	 * @see #setCooling(double)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getAnnealingSettings_Cooling()
	 * @model required="true"
	 * @generated
	 */
	double getCooling();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getCooling <em>Cooling</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooling</em>' attribute.
	 * @see #getCooling()
	 * @generated
	 */
	void setCooling(double value);

	/**
	 * Returns the value of the '<em><b>Initial Temperature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Temperature</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Temperature</em>' attribute.
	 * @see #setInitialTemperature(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getAnnealingSettings_InitialTemperature()
	 * @model required="true"
	 * @generated
	 */
	int getInitialTemperature();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.AnnealingSettings#getInitialTemperature <em>Initial Temperature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Temperature</em>' attribute.
	 * @see #getInitialTemperature()
	 * @generated
	 */
	void setInitialTemperature(int value);

} // end of  AnnealingSettings

// finish type fixing
