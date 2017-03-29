/**
 */
package com.mmxlabs.models.lng.commercial;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lump Sum Ballast Bonus Contract Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getLumpSumBallastBonusContractLine()
 * @model
 * @generated
 */
public interface LumpSumBallastBonusContractLine extends BallastBonusContractLine {
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getLumpSumBallastBonusContractLine_PriceExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

} // LumpSumBallastBonusContractLine
