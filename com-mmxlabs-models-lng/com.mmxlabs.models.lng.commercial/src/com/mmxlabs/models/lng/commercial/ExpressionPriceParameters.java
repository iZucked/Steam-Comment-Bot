/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression Price Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ExpressionPriceParameters#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getExpressionPriceParameters()
 * @model
 * @generated
 */
public interface ExpressionPriceParameters extends LNGPriceCalculatorParameters {
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getExpressionPriceParameters_PriceExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ExpressionPriceParameters#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

} // end of  ExpressionPriceParameters

// finish type fixing
