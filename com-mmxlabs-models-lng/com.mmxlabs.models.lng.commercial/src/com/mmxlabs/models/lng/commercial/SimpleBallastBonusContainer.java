/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Standard Ballast Bonus Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer#getTerms <em>Terms</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getSimpleBallastBonusContainer()
 * @model
 * @generated
 */
public interface SimpleBallastBonusContainer extends IBallastBonus {
	/**
	 * Returns the value of the '<em><b>Terms</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.BallastBonusTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Terms</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getSimpleBallastBonusContainer_Terms()
	 * @model containment="true"
	 * @generated
	 */
	EList<BallastBonusTerm> getTerms();

} // StandardBallastBonusContainer
