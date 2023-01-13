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

} // NotionalJourneyBallastBonusTerm
