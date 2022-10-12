/**
 */
package com.mmxlabs.models.lng.commercial;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Regas Pricing Params</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RegasPricingParams#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RegasPricingParams#getNumPricingDays <em>Num Pricing Days</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRegasPricingParams()
 * @model
 * @generated
 */
public interface RegasPricingParams extends LNGPriceCalculatorParameters {
	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRegasPricingParams_PriceExpression()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RegasPricingParams#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Num Pricing Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Num Pricing Days</em>' attribute.
	 * @see #setNumPricingDays(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRegasPricingParams_NumPricingDays()
	 * @model
	 * @generated
	 */
	int getNumPricingDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RegasPricingParams#getNumPricingDays <em>Num Pricing Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Num Pricing Days</em>' attribute.
	 * @see #getNumPricingDays()
	 * @generated
	 */
	void setNumPricingDays(int value);

} // RegasPricingParams
