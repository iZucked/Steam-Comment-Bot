/**
 */
package com.mmxlabs.models.lng.commercial;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Date Shift Expression Price Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#isSpecificDay <em>Specific Day</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getDateShiftExpressionPriceParameters()
 * @model
 * @generated
 */
public interface DateShiftExpressionPriceParameters extends LNGPriceCalculatorParameters {
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getDateShiftExpressionPriceParameters_PriceExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Specific Day</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Specific Day</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Specific Day</em>' attribute.
	 * @see #setSpecificDay(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getDateShiftExpressionPriceParameters_SpecificDay()
	 * @model
	 * @generated
	 */
	boolean isSpecificDay();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#isSpecificDay <em>Specific Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Specific Day</em>' attribute.
	 * @see #isSpecificDay()
	 * @generated
	 */
	void setSpecificDay(boolean value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getDateShiftExpressionPriceParameters_Value()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='-#0'"
	 * @generated
	 */
	int getValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(int value);

} // DateShiftExpressionPriceParameters
