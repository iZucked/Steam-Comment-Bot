/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
 * A representation of the model object '<em><b>Ballast Bonus Contract Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BallastBonusContractLine#getRedeliveryPorts <em>Redelivery Ports</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusContractLine()
 * @model abstract="true"
 * @generated
 */
public interface BallastBonusContractLine extends EObject {
	/**
	 * Returns the value of the '<em><b>Redelivery Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Redelivery Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Redelivery Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusContractLine_RedeliveryPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getRedeliveryPorts();

} // BallastBonusContractLine
