/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
 * A representation of the model object '<em><b>Monthly Ballast Bonus Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer#getHubs <em>Hubs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer#getTerms <em>Terms</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContainer()
 * @model
 * @generated
 */
public interface MonthlyBallastBonusContainer extends IBallastBonus {
	/**
	 * Returns the value of the '<em><b>Hubs</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hubs</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContainer_Hubs()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getHubs();

	/**
	 * Returns the value of the '<em><b>Terms</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Terms</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContainer_Terms()
	 * @model containment="true"
	 * @generated
	 */
	EList<MonthlyBallastBonusTerm> getTerms();

} // MonthlyBallastBonusContainer
