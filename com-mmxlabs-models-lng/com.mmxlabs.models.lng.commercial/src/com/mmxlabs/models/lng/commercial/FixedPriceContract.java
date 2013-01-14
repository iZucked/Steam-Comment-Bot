/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fixed Price Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.FixedPriceContract#getPricePerMMBTU <em>Price Per MMBTU</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getFixedPriceContract()
 * @model
 * @generated
 */
public interface FixedPriceContract extends SalesContract, PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Price Per MMBTU</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Per MMBTU</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Per MMBTU</em>' attribute.
	 * @see #setPricePerMMBTU(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getFixedPriceContract_PricePerMMBTU()
	 * @model required="true"
	 * @generated
	 */
	double getPricePerMMBTU();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.FixedPriceContract#getPricePerMMBTU <em>Price Per MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Per MMBTU</em>' attribute.
	 * @see #getPricePerMMBTU()
	 * @generated
	 */
	void setPricePerMMBTU(double value);

} // end of  FixedPriceContract

// finish type fixing
