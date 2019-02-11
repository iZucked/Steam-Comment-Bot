/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.NamedObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Market Index</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.MarketIndex#getSettleCalendar <em>Settle Calendar</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getMarketIndex()
 * @model
 * @generated
 */
public interface MarketIndex extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Settle Calendar</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settle Calendar</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settle Calendar</em>' reference.
	 * @see #setSettleCalendar(HolidayCalendar)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getMarketIndex_SettleCalendar()
	 * @model
	 * @generated
	 */
	HolidayCalendar getSettleCalendar();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.MarketIndex#getSettleCalendar <em>Settle Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Settle Calendar</em>' reference.
	 * @see #getSettleCalendar()
	 * @generated
	 */
	void setSettleCalendar(HolidayCalendar value);

} // MarketIndex
