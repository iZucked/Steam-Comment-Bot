/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.ActualsModel#getCargoActuals <em>Cargo Actuals</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getActualsModel()
 * @model
 * @generated
 */
public interface ActualsModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Cargo Actuals</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.actuals.CargoActuals}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Actuals</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Actuals</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getActualsModel_CargoActuals()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<CargoActuals> getCargoActuals();

} // ActualsModel
