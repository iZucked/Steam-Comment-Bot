/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Price Expression Contract</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.PriceExpressionContract#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getPriceExpressionContract()
 * @model
 * @generated
 */
public interface PriceExpressionContract extends SalesContract, PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getPriceExpressionContract_PriceExpression()
	 * @model default=""
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.PriceExpressionContract#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

} // end of  PriceExpressionContract

// finish type fixing
