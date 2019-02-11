/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCurrencyCurves <em>Currency Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCommodityCurves <em>Commodity Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCharterCurves <em>Charter Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getBunkerFuelCurves <em>Bunker Fuel Curves</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getConversionFactors <em>Conversion Factors</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getMarketCurveDataVersion <em>Market Curve Data Version</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getSettledPrices <em>Settled Prices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getMarketIndices <em>Market Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getHolidayCalendars <em>Holiday Calendars</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getSettleStrategies <em>Settle Strategies</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PricingModelImpl extends UUIDObjectImpl implements PricingModel {
	/**
	 * The cached value of the '{@link #getCurrencyCurves() <em>Currency Curves</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrencyCurves()
	 * @generated
	 * @ordered
	 */
	protected EList<CurrencyCurve> currencyCurves;

	/**
	 * The cached value of the '{@link #getCommodityCurves() <em>Commodity Curves</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommodityCurves()
	 * @generated
	 * @ordered
	 */
	protected EList<CommodityCurve> commodityCurves;

	/**
	 * The cached value of the '{@link #getCharterCurves() <em>Charter Curves</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterCurves()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterCurve> charterCurves;

	/**
	 * The cached value of the '{@link #getBunkerFuelCurves() <em>Bunker Fuel Curves</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBunkerFuelCurves()
	 * @generated
	 * @ordered
	 */
	protected EList<BunkerFuelCurve> bunkerFuelCurves;

	/**
	 * The cached value of the '{@link #getConversionFactors() <em>Conversion Factors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConversionFactors()
	 * @generated
	 * @ordered
	 */
	protected EList<UnitConversion> conversionFactors;

	/**
	 * The default value of the '{@link #getMarketCurveDataVersion() <em>Market Curve Data Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketCurveDataVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String MARKET_CURVE_DATA_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMarketCurveDataVersion() <em>Market Curve Data Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketCurveDataVersion()
	 * @generated
	 * @ordered
	 */
	protected String marketCurveDataVersion = MARKET_CURVE_DATA_VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSettledPrices() <em>Settled Prices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettledPrices()
	 * @generated
	 * @ordered
	 */
	protected EList<DatePointContainer> settledPrices;

	/**
	 * The cached value of the '{@link #getMarketIndices() <em>Market Indices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<MarketIndex> marketIndices;

	/**
	 * The cached value of the '{@link #getHolidayCalendars() <em>Holiday Calendars</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHolidayCalendars()
	 * @generated
	 * @ordered
	 */
	protected EList<HolidayCalendar> holidayCalendars;

	/**
	 * The cached value of the '{@link #getSettleStrategies() <em>Settle Strategies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettleStrategies()
	 * @generated
	 * @ordered
	 */
	protected EList<SettleStrategy> settleStrategies;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PricingModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PRICING_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CurrencyCurve> getCurrencyCurves() {
		if (currencyCurves == null) {
			currencyCurves = new EObjectContainmentEList<CurrencyCurve>(CurrencyCurve.class, this, PricingPackage.PRICING_MODEL__CURRENCY_CURVES);
		}
		return currencyCurves;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CommodityCurve> getCommodityCurves() {
		if (commodityCurves == null) {
			commodityCurves = new EObjectContainmentEList<CommodityCurve>(CommodityCurve.class, this, PricingPackage.PRICING_MODEL__COMMODITY_CURVES);
		}
		return commodityCurves;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CharterCurve> getCharterCurves() {
		if (charterCurves == null) {
			charterCurves = new EObjectContainmentEList<CharterCurve>(CharterCurve.class, this, PricingPackage.PRICING_MODEL__CHARTER_CURVES);
		}
		return charterCurves;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BunkerFuelCurve> getBunkerFuelCurves() {
		if (bunkerFuelCurves == null) {
			bunkerFuelCurves = new EObjectContainmentEList<BunkerFuelCurve>(BunkerFuelCurve.class, this, PricingPackage.PRICING_MODEL__BUNKER_FUEL_CURVES);
		}
		return bunkerFuelCurves;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<UnitConversion> getConversionFactors() {
		if (conversionFactors == null) {
			conversionFactors = new EObjectContainmentEList<UnitConversion>(UnitConversion.class, this, PricingPackage.PRICING_MODEL__CONVERSION_FACTORS);
		}
		return conversionFactors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMarketCurveDataVersion() {
		return marketCurveDataVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketCurveDataVersion(String newMarketCurveDataVersion) {
		String oldMarketCurveDataVersion = marketCurveDataVersion;
		marketCurveDataVersion = newMarketCurveDataVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__MARKET_CURVE_DATA_VERSION, oldMarketCurveDataVersion, marketCurveDataVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DatePointContainer> getSettledPrices() {
		if (settledPrices == null) {
			settledPrices = new EObjectContainmentEList<DatePointContainer>(DatePointContainer.class, this, PricingPackage.PRICING_MODEL__SETTLED_PRICES);
		}
		return settledPrices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MarketIndex> getMarketIndices() {
		if (marketIndices == null) {
			marketIndices = new EObjectContainmentEList<MarketIndex>(MarketIndex.class, this, PricingPackage.PRICING_MODEL__MARKET_INDICES);
		}
		return marketIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<HolidayCalendar> getHolidayCalendars() {
		if (holidayCalendars == null) {
			holidayCalendars = new EObjectContainmentEList<HolidayCalendar>(HolidayCalendar.class, this, PricingPackage.PRICING_MODEL__HOLIDAY_CALENDARS);
		}
		return holidayCalendars;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SettleStrategy> getSettleStrategies() {
		if (settleStrategies == null) {
			settleStrategies = new EObjectContainmentEList<SettleStrategy>(SettleStrategy.class, this, PricingPackage.PRICING_MODEL__SETTLE_STRATEGIES);
		}
		return settleStrategies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.PRICING_MODEL__CURRENCY_CURVES:
				return ((InternalEList<?>)getCurrencyCurves()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__COMMODITY_CURVES:
				return ((InternalEList<?>)getCommodityCurves()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__CHARTER_CURVES:
				return ((InternalEList<?>)getCharterCurves()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__BUNKER_FUEL_CURVES:
				return ((InternalEList<?>)getBunkerFuelCurves()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				return ((InternalEList<?>)getConversionFactors()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__SETTLED_PRICES:
				return ((InternalEList<?>)getSettledPrices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__MARKET_INDICES:
				return ((InternalEList<?>)getMarketIndices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__HOLIDAY_CALENDARS:
				return ((InternalEList<?>)getHolidayCalendars()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__SETTLE_STRATEGIES:
				return ((InternalEList<?>)getSettleStrategies()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.PRICING_MODEL__CURRENCY_CURVES:
				return getCurrencyCurves();
			case PricingPackage.PRICING_MODEL__COMMODITY_CURVES:
				return getCommodityCurves();
			case PricingPackage.PRICING_MODEL__CHARTER_CURVES:
				return getCharterCurves();
			case PricingPackage.PRICING_MODEL__BUNKER_FUEL_CURVES:
				return getBunkerFuelCurves();
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				return getConversionFactors();
			case PricingPackage.PRICING_MODEL__MARKET_CURVE_DATA_VERSION:
				return getMarketCurveDataVersion();
			case PricingPackage.PRICING_MODEL__SETTLED_PRICES:
				return getSettledPrices();
			case PricingPackage.PRICING_MODEL__MARKET_INDICES:
				return getMarketIndices();
			case PricingPackage.PRICING_MODEL__HOLIDAY_CALENDARS:
				return getHolidayCalendars();
			case PricingPackage.PRICING_MODEL__SETTLE_STRATEGIES:
				return getSettleStrategies();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PricingPackage.PRICING_MODEL__CURRENCY_CURVES:
				getCurrencyCurves().clear();
				getCurrencyCurves().addAll((Collection<? extends CurrencyCurve>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__COMMODITY_CURVES:
				getCommodityCurves().clear();
				getCommodityCurves().addAll((Collection<? extends CommodityCurve>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__CHARTER_CURVES:
				getCharterCurves().clear();
				getCharterCurves().addAll((Collection<? extends CharterCurve>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__BUNKER_FUEL_CURVES:
				getBunkerFuelCurves().clear();
				getBunkerFuelCurves().addAll((Collection<? extends BunkerFuelCurve>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				getConversionFactors().clear();
				getConversionFactors().addAll((Collection<? extends UnitConversion>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__MARKET_CURVE_DATA_VERSION:
				setMarketCurveDataVersion((String)newValue);
				return;
			case PricingPackage.PRICING_MODEL__SETTLED_PRICES:
				getSettledPrices().clear();
				getSettledPrices().addAll((Collection<? extends DatePointContainer>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__MARKET_INDICES:
				getMarketIndices().clear();
				getMarketIndices().addAll((Collection<? extends MarketIndex>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__HOLIDAY_CALENDARS:
				getHolidayCalendars().clear();
				getHolidayCalendars().addAll((Collection<? extends HolidayCalendar>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__SETTLE_STRATEGIES:
				getSettleStrategies().clear();
				getSettleStrategies().addAll((Collection<? extends SettleStrategy>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PricingPackage.PRICING_MODEL__CURRENCY_CURVES:
				getCurrencyCurves().clear();
				return;
			case PricingPackage.PRICING_MODEL__COMMODITY_CURVES:
				getCommodityCurves().clear();
				return;
			case PricingPackage.PRICING_MODEL__CHARTER_CURVES:
				getCharterCurves().clear();
				return;
			case PricingPackage.PRICING_MODEL__BUNKER_FUEL_CURVES:
				getBunkerFuelCurves().clear();
				return;
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				getConversionFactors().clear();
				return;
			case PricingPackage.PRICING_MODEL__MARKET_CURVE_DATA_VERSION:
				setMarketCurveDataVersion(MARKET_CURVE_DATA_VERSION_EDEFAULT);
				return;
			case PricingPackage.PRICING_MODEL__SETTLED_PRICES:
				getSettledPrices().clear();
				return;
			case PricingPackage.PRICING_MODEL__MARKET_INDICES:
				getMarketIndices().clear();
				return;
			case PricingPackage.PRICING_MODEL__HOLIDAY_CALENDARS:
				getHolidayCalendars().clear();
				return;
			case PricingPackage.PRICING_MODEL__SETTLE_STRATEGIES:
				getSettleStrategies().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PricingPackage.PRICING_MODEL__CURRENCY_CURVES:
				return currencyCurves != null && !currencyCurves.isEmpty();
			case PricingPackage.PRICING_MODEL__COMMODITY_CURVES:
				return commodityCurves != null && !commodityCurves.isEmpty();
			case PricingPackage.PRICING_MODEL__CHARTER_CURVES:
				return charterCurves != null && !charterCurves.isEmpty();
			case PricingPackage.PRICING_MODEL__BUNKER_FUEL_CURVES:
				return bunkerFuelCurves != null && !bunkerFuelCurves.isEmpty();
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				return conversionFactors != null && !conversionFactors.isEmpty();
			case PricingPackage.PRICING_MODEL__MARKET_CURVE_DATA_VERSION:
				return MARKET_CURVE_DATA_VERSION_EDEFAULT == null ? marketCurveDataVersion != null : !MARKET_CURVE_DATA_VERSION_EDEFAULT.equals(marketCurveDataVersion);
			case PricingPackage.PRICING_MODEL__SETTLED_PRICES:
				return settledPrices != null && !settledPrices.isEmpty();
			case PricingPackage.PRICING_MODEL__MARKET_INDICES:
				return marketIndices != null && !marketIndices.isEmpty();
			case PricingPackage.PRICING_MODEL__HOLIDAY_CALENDARS:
				return holidayCalendars != null && !holidayCalendars.isEmpty();
			case PricingPackage.PRICING_MODEL__SETTLE_STRATEGIES:
				return settleStrategies != null && !settleStrategies.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (marketCurveDataVersion: ");
		result.append(marketCurveDataVersion);
		result.append(')');
		return result.toString();
	}

} // end of PricingModelImpl

// finish type fixing
