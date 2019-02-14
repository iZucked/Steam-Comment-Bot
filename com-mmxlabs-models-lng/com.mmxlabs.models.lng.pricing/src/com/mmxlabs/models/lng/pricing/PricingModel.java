/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCurrencyCurves <em>Currency Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCommodityCurves <em>Commodity Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCharterCurves <em>Charter Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getBunkerFuelCurves <em>Bunker Fuel Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getConversionFactors <em>Conversion Factors</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getMarketCurveDataVersion <em>Market Curve Data Version</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getSettledPrices <em>Settled Prices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getMarketIndices <em>Market Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getHolidayCalendars <em>Holiday Calendars</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getSettleStrategies <em>Settle Strategies</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel()
 * @model
 * @generated
 */
public interface PricingModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Currency Curves</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CurrencyCurve}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Currency Curves</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Currency Curves</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CurrencyCurves()
	 * @model containment="true"
	 * @generated
	 */
	EList<CurrencyCurve> getCurrencyCurves();

	/**
	 * Returns the value of the '<em><b>Commodity Curves</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CommodityCurve}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commodity Curves</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commodity Curves</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CommodityCurves()
	 * @model containment="true"
	 * @generated
	 */
	EList<CommodityCurve> getCommodityCurves();

	/**
	 * Returns the value of the '<em><b>Charter Curves</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CharterCurve}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Curves</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Curves</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CharterCurves()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterCurve> getCharterCurves();

	/**
	 * Returns the value of the '<em><b>Bunker Fuel Curves</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.BunkerFuelCurve}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bunker Fuel Curves</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bunker Fuel Curves</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_BunkerFuelCurves()
	 * @model containment="true"
	 * @generated
	 */
	EList<BunkerFuelCurve> getBunkerFuelCurves();

	/**
	 * Returns the value of the '<em><b>Conversion Factors</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.UnitConversion}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conversion Factors</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conversion Factors</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_ConversionFactors()
	 * @model containment="true"
	 * @generated
	 */
	EList<UnitConversion> getConversionFactors();

	/**
	 * Returns the value of the '<em><b>Market Curve Data Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Curve Data Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Curve Data Version</em>' attribute.
	 * @see #setMarketCurveDataVersion(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_MarketCurveDataVersion()
	 * @model
	 * @generated
	 */
	String getMarketCurveDataVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PricingModel#getMarketCurveDataVersion <em>Market Curve Data Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Curve Data Version</em>' attribute.
	 * @see #getMarketCurveDataVersion()
	 * @generated
	 */
	void setMarketCurveDataVersion(String value);

	/**
	 * Returns the value of the '<em><b>Settled Prices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.DatePointContainer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settled Prices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settled Prices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_SettledPrices()
	 * @model containment="true"
	 * @generated
	 */
	EList<DatePointContainer> getSettledPrices();

	/**
	 * Returns the value of the '<em><b>Market Indices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.MarketIndex}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Indices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Indices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_MarketIndices()
	 * @model containment="true"
	 * @generated
	 */
	EList<MarketIndex> getMarketIndices();

	/**
	 * Returns the value of the '<em><b>Holiday Calendars</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.HolidayCalendar}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Holiday Calendars</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Holiday Calendars</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_HolidayCalendars()
	 * @model containment="true"
	 * @generated
	 */
	EList<HolidayCalendar> getHolidayCalendars();

	/**
	 * Returns the value of the '<em><b>Settle Strategies</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.SettleStrategy}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settle Strategies</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settle Strategies</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_SettleStrategies()
	 * @model containment="true"
	 * @generated
	 */
	EList<SettleStrategy> getSettleStrategies();

} // end of  PricingModel

// finish type fixing
