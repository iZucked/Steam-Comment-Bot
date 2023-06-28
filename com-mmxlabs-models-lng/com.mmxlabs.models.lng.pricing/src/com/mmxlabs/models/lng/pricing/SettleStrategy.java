/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Settle Strategy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getDayOfTheMonth <em>Day Of The Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#isLastDayOfTheMonth <em>Last Day Of The Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getPricingCalendar <em>Pricing Calendar</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getPricingPeriod <em>Pricing Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getHedgingPeriod <em>Hedging Period</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy()
 * @model
 * @generated
 */
public interface SettleStrategy extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Day Of The Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Day Of The Month</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Day Of The Month</em>' attribute.
	 * @see #setDayOfTheMonth(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_DayOfTheMonth()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0'"
	 * @generated
	 */
	int getDayOfTheMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getDayOfTheMonth <em>Day Of The Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Day Of The Month</em>' attribute.
	 * @see #getDayOfTheMonth()
	 * @generated
	 */
	void setDayOfTheMonth(int value);

	/**
	 * Returns the value of the '<em><b>Pricing Calendar</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Calendar</em>' reference.
	 * @see #isSetPricingCalendar()
	 * @see #unsetPricingCalendar()
	 * @see #setPricingCalendar(PricingCalendar)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_PricingCalendar()
	 * @model unsettable="true"
	 * @generated
	 */
	PricingCalendar getPricingCalendar();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getPricingCalendar <em>Pricing Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Calendar</em>' reference.
	 * @see #isSetPricingCalendar()
	 * @see #unsetPricingCalendar()
	 * @see #getPricingCalendar()
	 * @generated
	 */
	void setPricingCalendar(PricingCalendar value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getPricingCalendar <em>Pricing Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPricingCalendar()
	 * @see #getPricingCalendar()
	 * @see #setPricingCalendar(PricingCalendar)
	 * @generated
	 */
	void unsetPricingCalendar();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getPricingCalendar <em>Pricing Calendar</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Pricing Calendar</em>' reference is set.
	 * @see #unsetPricingCalendar()
	 * @see #getPricingCalendar()
	 * @see #setPricingCalendar(PricingCalendar)
	 * @generated
	 */
	boolean isSetPricingCalendar();

	/**
	 * Returns the value of the '<em><b>Pricing Period</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Period</em>' containment reference.
	 * @see #setPricingPeriod(InstrumentPeriod)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_PricingPeriod()
	 * @model containment="true"
	 * @generated
	 */
	InstrumentPeriod getPricingPeriod();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getPricingPeriod <em>Pricing Period</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Period</em>' containment reference.
	 * @see #getPricingPeriod()
	 * @generated
	 */
	void setPricingPeriod(InstrumentPeriod value);

	/**
	 * Returns the value of the '<em><b>Hedging Period</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hedging Period</em>' containment reference.
	 * @see #setHedgingPeriod(InstrumentPeriod)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_HedgingPeriod()
	 * @model containment="true"
	 * @generated
	 */
	InstrumentPeriod getHedgingPeriod();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getHedgingPeriod <em>Hedging Period</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hedging Period</em>' containment reference.
	 * @see #getHedgingPeriod()
	 * @generated
	 */
	void setHedgingPeriod(InstrumentPeriod value);

	/**
	 * Returns the value of the '<em><b>Last Day Of The Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Day Of The Month</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Day Of The Month</em>' attribute.
	 * @see #setLastDayOfTheMonth(boolean)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_LastDayOfTheMonth()
	 * @model
	 * @generated
	 */
	boolean isLastDayOfTheMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#isLastDayOfTheMonth <em>Last Day Of The Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Day Of The Month</em>' attribute.
	 * @see #isLastDayOfTheMonth()
	 * @generated
	 */
	void setLastDayOfTheMonth(boolean value);

} // SettleStrategy
