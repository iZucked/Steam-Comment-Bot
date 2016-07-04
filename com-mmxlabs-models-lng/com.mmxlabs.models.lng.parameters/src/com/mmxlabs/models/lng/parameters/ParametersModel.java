/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ParametersModel#getSettings <em>Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ParametersModel#getActiveSetting <em>Active Setting</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getParametersModel()
 * @model
 * @generated
 */
public interface ParametersModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Settings</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.OptimiserSettings}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settings</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getParametersModel_Settings()
	 * @model containment="true"
	 * @generated
	 */
	EList<OptimiserSettings> getSettings();

	/**
	 * Returns the value of the '<em><b>Active Setting</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Active Setting</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Active Setting</em>' reference.
	 * @see #setActiveSetting(OptimiserSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getParametersModel_ActiveSetting()
	 * @model required="true"
	 * @generated
	 */
	OptimiserSettings getActiveSetting();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ParametersModel#getActiveSetting <em>Active Setting</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Active Setting</em>' reference.
	 * @see #getActiveSetting()
	 * @generated
	 */
	void setActiveSetting(OptimiserSettings value);

} // end of  OptimiserModel

// finish type fixing
