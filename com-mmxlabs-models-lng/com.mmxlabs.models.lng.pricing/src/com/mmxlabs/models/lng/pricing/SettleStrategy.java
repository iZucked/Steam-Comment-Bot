/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.ecore.EObject;

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
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#isUseCalendarMonth <em>Use Calendar Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriod <em>Settle Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriodUnit <em>Settle Period Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettleStartMonthsPrior <em>Settle Start Months Prior</em>}</li>
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
	 * @model
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
	 * Returns the value of the '<em><b>Settle Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settle Period</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settle Period</em>' attribute.
	 * @see #setSettlePeriod(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_SettlePeriod()
	 * @model
	 * @generated
	 */
	int getSettlePeriod();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriod <em>Settle Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Settle Period</em>' attribute.
	 * @see #getSettlePeriod()
	 * @generated
	 */
	void setSettlePeriod(int value);

	/**
	 * Returns the value of the '<em><b>Settle Period Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settle Period Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settle Period Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setSettlePeriodUnit(TimePeriod)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_SettlePeriodUnit()
	 * @model
	 * @generated
	 */
	TimePeriod getSettlePeriodUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettlePeriodUnit <em>Settle Period Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Settle Period Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getSettlePeriodUnit()
	 * @generated
	 */
	void setSettlePeriodUnit(TimePeriod value);

	/**
	 * Returns the value of the '<em><b>Settle Start Months Prior</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settle Start Months Prior</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settle Start Months Prior</em>' attribute.
	 * @see #setSettleStartMonthsPrior(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_SettleStartMonthsPrior()
	 * @model
	 * @generated
	 */
	int getSettleStartMonthsPrior();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#getSettleStartMonthsPrior <em>Settle Start Months Prior</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Settle Start Months Prior</em>' attribute.
	 * @see #getSettleStartMonthsPrior()
	 * @generated
	 */
	void setSettleStartMonthsPrior(int value);

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

	/**
	 * Returns the value of the '<em><b>Use Calendar Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Calendar Month</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Calendar Month</em>' attribute.
	 * @see #setUseCalendarMonth(boolean)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSettleStrategy_UseCalendarMonth()
	 * @model
	 * @generated
	 */
	boolean isUseCalendarMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SettleStrategy#isUseCalendarMonth <em>Use Calendar Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Calendar Month</em>' attribute.
	 * @see #isUseCalendarMonth()
	 * @generated
	 */
	void setUseCalendarMonth(boolean value);

} // SettleStrategy
