/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
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
 *   <li>{@link com.mmxlabs.models.lng.pricing.MarketIndex#getPricingCalendar <em>Pricing Calendar</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.MarketIndex#getFlatCurve <em>Flat Curve</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.MarketIndex#getBidCurve <em>Bid Curve</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.MarketIndex#getOfferCurve <em>Offer Curve</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Pricing Calendar</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pricing Calendar</em>' reference.
	 * @see #setPricingCalendar(PricingCalendar)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getMarketIndex_PricingCalendar()
	 * @model
	 * @generated
	 */
	PricingCalendar getPricingCalendar();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.MarketIndex#getPricingCalendar <em>Pricing Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pricing Calendar</em>' reference.
	 * @see #getPricingCalendar()
	 * @generated
	 */
	void setPricingCalendar(PricingCalendar value);

	/**
	 * Returns the value of the '<em><b>Flat Curve</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flat Curve</em>' reference.
	 * @see #setFlatCurve(CommodityCurve)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getMarketIndex_FlatCurve()
	 * @model
	 * @generated
	 */
	CommodityCurve getFlatCurve();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.MarketIndex#getFlatCurve <em>Flat Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flat Curve</em>' reference.
	 * @see #getFlatCurve()
	 * @generated
	 */
	void setFlatCurve(CommodityCurve value);

	/**
	 * Returns the value of the '<em><b>Bid Curve</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bid Curve</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bid Curve</em>' reference.
	 * @see #setBidCurve(CommodityCurve)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getMarketIndex_BidCurve()
	 * @model
	 * @generated
	 */
	CommodityCurve getBidCurve();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.MarketIndex#getBidCurve <em>Bid Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bid Curve</em>' reference.
	 * @see #getBidCurve()
	 * @generated
	 */
	void setBidCurve(CommodityCurve value);

	/**
	 * Returns the value of the '<em><b>Offer Curve</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offer Curve</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offer Curve</em>' reference.
	 * @see #setOfferCurve(CommodityCurve)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getMarketIndex_OfferCurve()
	 * @model
	 * @generated
	 */
	CommodityCurve getOfferCurve();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.MarketIndex#getOfferCurve <em>Offer Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Offer Curve</em>' reference.
	 * @see #getOfferCurve()
	 * @generated
	 */
	void setOfferCurve(CommodityCurve value);

} // MarketIndex
