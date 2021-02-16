/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.pricing.SuezCanalTariffBand;
import com.mmxlabs.models.lng.pricing.SuezCanalTugBand;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Suez Canal Tariff</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getBands <em>Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getTugBands <em>Tug Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getRouteRebates <em>Route Rebates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getTugCost <em>Tug Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getFixedCosts <em>Fixed Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getDiscountFactor <em>Discount Factor</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalTariffImpl#getSdrToUSD <em>Sdr To USD</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SuezCanalTariffImpl extends EObjectImpl implements SuezCanalTariff {
	/**
	 * The cached value of the '{@link #getBands() <em>Bands</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBands()
	 * @generated
	 * @ordered
	 */
	protected EList<SuezCanalTariffBand> bands;

	/**
	 * The cached value of the '{@link #getTugBands() <em>Tug Bands</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugBands()
	 * @generated
	 * @ordered
	 */
	protected EList<SuezCanalTugBand> tugBands;

	/**
	 * The cached value of the '{@link #getRouteRebates() <em>Route Rebates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteRebates()
	 * @generated
	 * @ordered
	 */
	protected EList<SuezCanalRouteRebate> routeRebates;

	/**
	 * The default value of the '{@link #getTugCost() <em>Tug Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugCost()
	 * @generated
	 * @ordered
	 */
	protected static final double TUG_COST_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTugCost() <em>Tug Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTugCost()
	 * @generated
	 * @ordered
	 */
	protected double tugCost = TUG_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getFixedCosts() <em>Fixed Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFixedCosts()
	 * @generated
	 * @ordered
	 */
	protected static final double FIXED_COSTS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getFixedCosts() <em>Fixed Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFixedCosts()
	 * @generated
	 * @ordered
	 */
	protected double fixedCosts = FIXED_COSTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getDiscountFactor() <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscountFactor()
	 * @generated
	 * @ordered
	 */
	protected static final double DISCOUNT_FACTOR_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDiscountFactor() <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscountFactor()
	 * @generated
	 * @ordered
	 */
	protected double discountFactor = DISCOUNT_FACTOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getSdrToUSD() <em>Sdr To USD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSdrToUSD()
	 * @generated
	 * @ordered
	 */
	protected static final String SDR_TO_USD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSdrToUSD() <em>Sdr To USD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSdrToUSD()
	 * @generated
	 * @ordered
	 */
	protected String sdrToUSD = SDR_TO_USD_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SuezCanalTariffImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SUEZ_CANAL_TARIFF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SuezCanalTariffBand> getBands() {
		if (bands == null) {
			bands = new EObjectContainmentEList<SuezCanalTariffBand>(SuezCanalTariffBand.class, this, PricingPackage.SUEZ_CANAL_TARIFF__BANDS);
		}
		return bands;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SuezCanalTugBand> getTugBands() {
		if (tugBands == null) {
			tugBands = new EObjectContainmentEList<SuezCanalTugBand>(SuezCanalTugBand.class, this, PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS);
		}
		return tugBands;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SuezCanalRouteRebate> getRouteRebates() {
		if (routeRebates == null) {
			routeRebates = new EObjectContainmentEList<SuezCanalRouteRebate>(SuezCanalRouteRebate.class, this, PricingPackage.SUEZ_CANAL_TARIFF__ROUTE_REBATES);
		}
		return routeRebates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getTugCost() {
		return tugCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTugCost(double newTugCost) {
		double oldTugCost = tugCost;
		tugCost = newTugCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST, oldTugCost, tugCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getFixedCosts() {
		return fixedCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFixedCosts(double newFixedCosts) {
		double oldFixedCosts = fixedCosts;
		fixedCosts = newFixedCosts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__FIXED_COSTS, oldFixedCosts, fixedCosts));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getDiscountFactor() {
		return discountFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDiscountFactor(double newDiscountFactor) {
		double oldDiscountFactor = discountFactor;
		discountFactor = newDiscountFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR, oldDiscountFactor, discountFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSdrToUSD() {
		return sdrToUSD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSdrToUSD(String newSdrToUSD) {
		String oldSdrToUSD = sdrToUSD;
		sdrToUSD = newSdrToUSD;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD, oldSdrToUSD, sdrToUSD));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				return ((InternalEList<?>)getBands()).basicRemove(otherEnd, msgs);
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				return ((InternalEList<?>)getTugBands()).basicRemove(otherEnd, msgs);
			case PricingPackage.SUEZ_CANAL_TARIFF__ROUTE_REBATES:
				return ((InternalEList<?>)getRouteRebates()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				return getBands();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				return getTugBands();
			case PricingPackage.SUEZ_CANAL_TARIFF__ROUTE_REBATES:
				return getRouteRebates();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				return getTugCost();
			case PricingPackage.SUEZ_CANAL_TARIFF__FIXED_COSTS:
				return getFixedCosts();
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				return getDiscountFactor();
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				return getSdrToUSD();
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
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				getBands().clear();
				getBands().addAll((Collection<? extends SuezCanalTariffBand>)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				getTugBands().clear();
				getTugBands().addAll((Collection<? extends SuezCanalTugBand>)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__ROUTE_REBATES:
				getRouteRebates().clear();
				getRouteRebates().addAll((Collection<? extends SuezCanalRouteRebate>)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				setTugCost((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__FIXED_COSTS:
				setFixedCosts((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				setDiscountFactor((Double)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				setSdrToUSD((String)newValue);
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
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				getBands().clear();
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				getTugBands().clear();
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__ROUTE_REBATES:
				getRouteRebates().clear();
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				setTugCost(TUG_COST_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__FIXED_COSTS:
				setFixedCosts(FIXED_COSTS_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				setDiscountFactor(DISCOUNT_FACTOR_EDEFAULT);
				return;
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				setSdrToUSD(SDR_TO_USD_EDEFAULT);
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
			case PricingPackage.SUEZ_CANAL_TARIFF__BANDS:
				return bands != null && !bands.isEmpty();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_BANDS:
				return tugBands != null && !tugBands.isEmpty();
			case PricingPackage.SUEZ_CANAL_TARIFF__ROUTE_REBATES:
				return routeRebates != null && !routeRebates.isEmpty();
			case PricingPackage.SUEZ_CANAL_TARIFF__TUG_COST:
				return tugCost != TUG_COST_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__FIXED_COSTS:
				return fixedCosts != FIXED_COSTS_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__DISCOUNT_FACTOR:
				return discountFactor != DISCOUNT_FACTOR_EDEFAULT;
			case PricingPackage.SUEZ_CANAL_TARIFF__SDR_TO_USD:
				return SDR_TO_USD_EDEFAULT == null ? sdrToUSD != null : !SDR_TO_USD_EDEFAULT.equals(sdrToUSD);
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
		result.append(" (tugCost: ");
		result.append(tugCost);
		result.append(", fixedCosts: ");
		result.append(fixedCosts);
		result.append(", discountFactor: ");
		result.append(discountFactor);
		result.append(", sdrToUSD: ");
		result.append(sdrToUSD);
		result.append(')');
		return result.toString();
	}

} //SuezCanalTariffImpl
