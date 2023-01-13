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
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ballast Bonus Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BallastBonusTerm#getRedeliveryPorts <em>Redelivery Ports</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusTerm()
 * @model
 * @generated
 */
public interface BallastBonusTerm extends EObject {

	/**
	 * Returns the value of the '<em><b>Redelivery Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Redelivery Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusTerm_RedeliveryPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getRedeliveryPorts();

} // BallastBonusTerm
