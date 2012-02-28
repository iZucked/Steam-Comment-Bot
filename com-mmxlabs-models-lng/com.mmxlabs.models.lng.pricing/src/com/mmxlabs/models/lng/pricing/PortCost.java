/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.types.APortSet;
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
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getBallastCost <em>Ballast Cost</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost()
 * @model
 * @generated
 */
public interface PortCost extends EObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_Ports()
	 * @model
	 * @generated
	 */
	EList<APortSet> getPorts();

	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_Vessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getVessels();

	/**
	 * Returns the value of the '<em><b>Laden Cost</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Cost</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Cost</em>' reference.
	 * @see #setLadenCost(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_LadenCost()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" required="true"
	 * @generated
	 */
	Index<Integer> getLadenCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortCost#getLadenCost <em>Laden Cost</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Cost</em>' reference.
	 * @see #getLadenCost()
	 * @generated
	 */
	void setLadenCost(Index<Integer> value);

	/**
	 * Returns the value of the '<em><b>Ballast Cost</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Cost</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Cost</em>' reference.
	 * @see #setBallastCost(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_BallastCost()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" required="true"
	 * @generated
	 */
	Index<Integer> getBallastCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortCost#getBallastCost <em>Ballast Cost</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Cost</em>' reference.
	 * @see #getBallastCost()
	 * @generated
	 */
	void setBallastCost(Index<Integer> value);

} // end of  PortCost

// finish type fixing
