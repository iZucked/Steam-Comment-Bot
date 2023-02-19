/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notional Journey Ballast Bonus Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm#getReturnPorts <em>Return Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm#isIsFirstLoadPort <em>Is First Load Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusTerm()
 * @model
 * @generated
 */
public interface NotionalJourneyBallastBonusTerm extends BallastBonusTerm, NotionalJourneyTerm {
	/**
	 * Returns the value of the '<em><b>Return Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusTerm_ReturnPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getReturnPorts();

	/**
	 * Returns the value of the '<em><b>Is First Load Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is First Load Port</em>' attribute.
	 * @see #setIsFirstLoadPort(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusTerm_IsFirstLoadPort()
	 * @model
	 * @generated
	 */
	boolean isIsFirstLoadPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm#isIsFirstLoadPort <em>Is First Load Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is First Load Port</em>' attribute.
	 * @see #isIsFirstLoadPort()
	 * @generated
	 */
	void setIsFirstLoadPort(boolean value);

} // NotionalJourneyBallastBonusTerm
