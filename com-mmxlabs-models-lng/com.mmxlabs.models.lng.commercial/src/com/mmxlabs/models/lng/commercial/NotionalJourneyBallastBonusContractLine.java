/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notional Journey Ballast Bonus Contract Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getFuelPriceExpression <em>Fuel Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getHirePriceExpression <em>Hire Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getReturnPorts <em>Return Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#isIncludeCanal <em>Include Canal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#isIncludeCanalTime <em>Include Canal Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getLumpSumPriceExpression <em>Lump Sum Price Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine()
 * @model
 * @generated
 */
public interface NotionalJourneyBallastBonusContractLine extends BallastBonusContractLine {
	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #setSpeed(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine_Speed()
	 * @model default="0" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(double value);

	/**
	 * Returns the value of the '<em><b>Fuel Price Expression</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Price Expression</em>' attribute.
	 * @see #setFuelPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine_FuelPriceExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='basefuel'"
	 * @generated
	 */
	String getFuelPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getFuelPriceExpression <em>Fuel Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Price Expression</em>' attribute.
	 * @see #getFuelPriceExpression()
	 * @generated
	 */
	void setFuelPriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Hire Price Expression</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Price Expression</em>' attribute.
	 * @see #setHirePriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine_HirePriceExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 * @generated
	 */
	String getHirePriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getHirePriceExpression <em>Hire Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Price Expression</em>' attribute.
	 * @see #getHirePriceExpression()
	 * @generated
	 */
	void setHirePriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Return Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine_ReturnPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getReturnPorts();

	/**
	 * Returns the value of the '<em><b>Include Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Include Canal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include Canal</em>' attribute.
	 * @see #setIncludeCanal(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine_IncludeCanal()
	 * @model
	 * @generated
	 */
	boolean isIncludeCanal();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#isIncludeCanal <em>Include Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Canal</em>' attribute.
	 * @see #isIncludeCanal()
	 * @generated
	 */
	void setIncludeCanal(boolean value);

	/**
	 * Returns the value of the '<em><b>Include Canal Time</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Include Canal Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include Canal Time</em>' attribute.
	 * @see #setIncludeCanalTime(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine_IncludeCanalTime()
	 * @model default="true"
	 * @generated
	 */
	boolean isIncludeCanalTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#isIncludeCanalTime <em>Include Canal Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Canal Time</em>' attribute.
	 * @see #isIncludeCanalTime()
	 * @generated
	 */
	void setIncludeCanalTime(boolean value);

	/**
	 * Returns the value of the '<em><b>Lump Sum Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lump Sum Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lump Sum Price Expression</em>' attribute.
	 * @see #setLumpSumPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyBallastBonusContractLine_LumpSumPriceExpression()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getLumpSumPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine#getLumpSumPriceExpression <em>Lump Sum Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lump Sum Price Expression</em>' attribute.
	 * @see #getLumpSumPriceExpression()
	 * @generated
	 */
	void setLumpSumPriceExpression(String value);

} // NotionalJourneyBallastBonusContractLine
