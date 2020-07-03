/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fleet Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#getDefaultNominalMarket <em>Default Nominal Market</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile()
 * @model
 * @generated
 */
public interface FleetProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.FleetConstraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_Constraints()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<FleetConstraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Default Nominal Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Nominal Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Nominal Market</em>' reference.
	 * @see #setDefaultNominalMarket(CharterInMarket)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_DefaultNominalMarket()
	 * @model
	 * @generated
	 */
	CharterInMarket getDefaultNominalMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.FleetProfile#getDefaultNominalMarket <em>Default Nominal Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Nominal Market</em>' reference.
	 * @see #getDefaultNominalMarket()
	 * @generated
	 */
	void setDefaultNominalMarket(CharterInMarket value);

} // FleetProfile
