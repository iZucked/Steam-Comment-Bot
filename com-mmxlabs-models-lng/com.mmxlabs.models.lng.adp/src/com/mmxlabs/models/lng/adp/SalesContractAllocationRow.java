/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.commercial.SalesContract;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sales Contract Allocation Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.SalesContractAllocationRow#getContract <em>Contract</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSalesContractAllocationRow()
 * @model
 * @generated
 */
public interface SalesContractAllocationRow extends MullAllocationRow {
	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(SalesContract)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSalesContractAllocationRow_Contract()
	 * @model
	 * @generated
	 */
	SalesContract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SalesContractAllocationRow#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(SalesContract value);

} // SalesContractAllocationRow
