/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
 * A representation of the model object '<em><b>Monthly Ballast Bonus Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract#getHubs <em>Hubs</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContract()
 * @model
 * @generated
 */
public interface MonthlyBallastBonusContract extends RuleBasedBallastBonusContract {
	/**
	 * Returns the value of the '<em><b>Hubs</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hubs</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContract_Hubs()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getHubs();

} // MonthlyBallastBonusContract
