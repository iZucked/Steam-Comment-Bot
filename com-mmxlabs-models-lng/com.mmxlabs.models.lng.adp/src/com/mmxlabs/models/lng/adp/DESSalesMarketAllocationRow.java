/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DES Sales Market Allocation Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow#getDesSalesMarket <em>Des Sales Market</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDESSalesMarketAllocationRow()
 * @model
 * @generated
 */
public interface DESSalesMarketAllocationRow extends MullAllocationRow {
	/**
	 * Returns the value of the '<em><b>Des Sales Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Sales Market</em>' reference.
	 * @see #setDesSalesMarket(DESSalesMarket)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDESSalesMarketAllocationRow_DesSalesMarket()
	 * @model
	 * @generated
	 */
	DESSalesMarket getDesSalesMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow#getDesSalesMarket <em>Des Sales Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Des Sales Market</em>' reference.
	 * @see #getDesSalesMarket()
	 * @generated
	 */
	void setDesSalesMarket(DESSalesMarket value);

} // DESSalesMarketAllocationRow
