/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.commercial.SalesContract;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spacing Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.SpacingAllocation#getContract <em>Contract</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpacingAllocation()
 * @model
 * @generated
 */
public interface SpacingAllocation extends EObject {
	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(SalesContract)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpacingAllocation_Contract()
	 * @model
	 * @generated
	 */
	SalesContract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SpacingAllocation#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(SalesContract value);

} // SpacingAllocation
