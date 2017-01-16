/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
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
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCurrencyIndices <em>Currency Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCommodityIndices <em>Commodity Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCharterIndices <em>Charter Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getBaseFuelPrices <em>Base Fuel Prices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getConversionFactors <em>Conversion Factors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PricingModelImpl extends UUIDObjectImpl implements PricingModel {
	/**
	 * The cached value of the '{@link #getCurrencyIndices() <em>Currency Indices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrencyIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<CurrencyIndex> currencyIndices;

	/**
	 * The cached value of the '{@link #getCommodityIndices() <em>Commodity Indices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommodityIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<CommodityIndex> commodityIndices;

	/**
	 * The cached value of the '{@link #getCharterIndices() <em>Charter Indices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterIndex> charterIndices;

	/**
	 * The cached value of the '{@link #getBaseFuelPrices() <em>Base Fuel Prices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrices()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseFuelIndex> baseFuelPrices;

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
	public EList<CurrencyIndex> getCurrencyIndices() {
		if (currencyIndices == null) {
			currencyIndices = new EObjectContainmentEList<CurrencyIndex>(CurrencyIndex.class, this, PricingPackage.PRICING_MODEL__CURRENCY_INDICES);
		}
		return currencyIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CommodityIndex> getCommodityIndices() {
		if (commodityIndices == null) {
			commodityIndices = new EObjectContainmentEList<CommodityIndex>(CommodityIndex.class, this, PricingPackage.PRICING_MODEL__COMMODITY_INDICES);
		}
		return commodityIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CharterIndex> getCharterIndices() {
		if (charterIndices == null) {
			charterIndices = new EObjectContainmentEList<CharterIndex>(CharterIndex.class, this, PricingPackage.PRICING_MODEL__CHARTER_INDICES);
		}
		return charterIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BaseFuelIndex> getBaseFuelPrices() {
		if (baseFuelPrices == null) {
			baseFuelPrices = new EObjectContainmentEList<BaseFuelIndex>(BaseFuelIndex.class, this, PricingPackage.PRICING_MODEL__BASE_FUEL_PRICES);
		}
		return baseFuelPrices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.PRICING_MODEL__CURRENCY_INDICES:
				return ((InternalEList<?>)getCurrencyIndices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				return ((InternalEList<?>)getCommodityIndices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				return ((InternalEList<?>)getCharterIndices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__BASE_FUEL_PRICES:
				return ((InternalEList<?>)getBaseFuelPrices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				return ((InternalEList<?>)getConversionFactors()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.PRICING_MODEL__CURRENCY_INDICES:
				return getCurrencyIndices();
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				return getCommodityIndices();
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				return getCharterIndices();
			case PricingPackage.PRICING_MODEL__BASE_FUEL_PRICES:
				return getBaseFuelPrices();
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				return getConversionFactors();
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
			case PricingPackage.PRICING_MODEL__CURRENCY_INDICES:
				getCurrencyIndices().clear();
				getCurrencyIndices().addAll((Collection<? extends CurrencyIndex>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				getCommodityIndices().clear();
				getCommodityIndices().addAll((Collection<? extends CommodityIndex>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				getCharterIndices().clear();
				getCharterIndices().addAll((Collection<? extends CharterIndex>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__BASE_FUEL_PRICES:
				getBaseFuelPrices().clear();
				getBaseFuelPrices().addAll((Collection<? extends BaseFuelIndex>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				getConversionFactors().clear();
				getConversionFactors().addAll((Collection<? extends UnitConversion>)newValue);
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
			case PricingPackage.PRICING_MODEL__CURRENCY_INDICES:
				getCurrencyIndices().clear();
				return;
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				getCommodityIndices().clear();
				return;
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				getCharterIndices().clear();
				return;
			case PricingPackage.PRICING_MODEL__BASE_FUEL_PRICES:
				getBaseFuelPrices().clear();
				return;
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				getConversionFactors().clear();
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
			case PricingPackage.PRICING_MODEL__CURRENCY_INDICES:
				return currencyIndices != null && !currencyIndices.isEmpty();
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				return commodityIndices != null && !commodityIndices.isEmpty();
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				return charterIndices != null && !charterIndices.isEmpty();
			case PricingPackage.PRICING_MODEL__BASE_FUEL_PRICES:
				return baseFuelPrices != null && !baseFuelPrices.isEmpty();
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
				return conversionFactors != null && !conversionFactors.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of PricingModelImpl

// finish type fixing
