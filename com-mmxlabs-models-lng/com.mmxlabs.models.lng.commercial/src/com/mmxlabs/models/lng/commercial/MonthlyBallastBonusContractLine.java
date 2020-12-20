/**
 */
package com.mmxlabs.models.lng.commercial;

import java.math.BigDecimal;

import java.time.YearMonth;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Monthly Ballast Bonus Contract Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getMonth <em>Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getBallastBonusTo <em>Ballast Bonus To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getBallastBonusPctFuel <em>Ballast Bonus Pct Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getBallastBonusPctCharter <em>Ballast Bonus Pct Charter</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContractLine()
 * @model
 * @generated
 */
public interface MonthlyBallastBonusContractLine extends NotionalJourneyBallastBonusContractLine {
	/**
	 * Returns the value of the '<em><b>Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Month</em>' attribute.
	 * @see #setMonth(YearMonth)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContractLine_Month()
	 * @model dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	YearMonth getMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getMonth <em>Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Month</em>' attribute.
	 * @see #getMonth()
	 * @generated
	 */
	void setMonth(YearMonth value);

	/**
	 * Returns the value of the '<em><b>Ballast Bonus To</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.commercial.NextPortType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus To</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.NextPortType
	 * @see #setBallastBonusTo(NextPortType)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContractLine_BallastBonusTo()
	 * @model
	 * @generated
	 */
	NextPortType getBallastBonusTo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getBallastBonusTo <em>Ballast Bonus To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus To</em>' attribute.
	 * @see com.mmxlabs.models.lng.commercial.NextPortType
	 * @see #getBallastBonusTo()
	 * @generated
	 */
	void setBallastBonusTo(NextPortType value);

	/**
	 * Returns the value of the '<em><b>Ballast Bonus Pct Fuel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus Pct Fuel</em>' attribute.
	 * @see #setBallastBonusPctFuel(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContractLine_BallastBonusPctFuel()
	 * @model
	 * @generated
	 */
	String getBallastBonusPctFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getBallastBonusPctFuel <em>Ballast Bonus Pct Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus Pct Fuel</em>' attribute.
	 * @see #getBallastBonusPctFuel()
	 * @generated
	 */
	void setBallastBonusPctFuel(String value);

	/**
	 * Returns the value of the '<em><b>Ballast Bonus Pct Charter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus Pct Charter</em>' attribute.
	 * @see #setBallastBonusPctCharter(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getMonthlyBallastBonusContractLine_BallastBonusPctCharter()
	 * @model
	 * @generated
	 */
	String getBallastBonusPctCharter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine#getBallastBonusPctCharter <em>Ballast Bonus Pct Charter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus Pct Charter</em>' attribute.
	 * @see #getBallastBonusPctCharter()
	 * @generated
	 */
	void setBallastBonusPctCharter(String value);

} // MonthlyBallastBonusContractLine
