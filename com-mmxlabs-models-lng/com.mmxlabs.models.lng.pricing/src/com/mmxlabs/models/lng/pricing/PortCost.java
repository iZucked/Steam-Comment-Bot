/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.lng.types.AVesselSet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getVesselPortCosts <em>Vessel Port Costs</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost()
 * @model
 * @generated
 */
public interface PortCost extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(APortSet)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_Port()
	 * @model required="true"
	 * @generated
	 */
	APortSet getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortCost#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(APortSet value);

	/**
	 * Returns the value of the '<em><b>Vessel Port Costs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PortCostVessels}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Port Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Port Costs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_VesselPortCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortCostVessels> getVesselPortCosts();

} // end of  PortCost

// finish type fixing
