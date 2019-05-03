/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Market Index</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl#getSettleCalendar <em>Settle Calendar</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl#getPricingCalendar <em>Pricing Calendar</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketIndexImpl extends NamedObjectImpl implements MarketIndex {
	/**
	 * The cached value of the '{@link #getSettleCalendar() <em>Settle Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettleCalendar()
	 * @generated
	 * @ordered
	 */
	protected HolidayCalendar settleCalendar;

	/**
	 * The cached value of the '{@link #getPricingCalendar() <em>Pricing Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingCalendar()
	 * @generated
	 * @ordered
	 */
	protected PricingCalendar pricingCalendar;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketIndexImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.MARKET_INDEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HolidayCalendar getSettleCalendar() {
		if (settleCalendar != null && settleCalendar.eIsProxy()) {
			InternalEObject oldSettleCalendar = (InternalEObject)settleCalendar;
			settleCalendar = (HolidayCalendar)eResolveProxy(oldSettleCalendar);
			if (settleCalendar != oldSettleCalendar) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.MARKET_INDEX__SETTLE_CALENDAR, oldSettleCalendar, settleCalendar));
			}
		}
		return settleCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HolidayCalendar basicGetSettleCalendar() {
		return settleCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSettleCalendar(HolidayCalendar newSettleCalendar) {
		HolidayCalendar oldSettleCalendar = settleCalendar;
		settleCalendar = newSettleCalendar;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.MARKET_INDEX__SETTLE_CALENDAR, oldSettleCalendar, settleCalendar));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingCalendar getPricingCalendar() {
		if (pricingCalendar != null && pricingCalendar.eIsProxy()) {
			InternalEObject oldPricingCalendar = (InternalEObject)pricingCalendar;
			pricingCalendar = (PricingCalendar)eResolveProxy(oldPricingCalendar);
			if (pricingCalendar != oldPricingCalendar) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.MARKET_INDEX__PRICING_CALENDAR, oldPricingCalendar, pricingCalendar));
			}
		}
		return pricingCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingCalendar basicGetPricingCalendar() {
		return pricingCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingCalendar(PricingCalendar newPricingCalendar) {
		PricingCalendar oldPricingCalendar = pricingCalendar;
		pricingCalendar = newPricingCalendar;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.MARKET_INDEX__PRICING_CALENDAR, oldPricingCalendar, pricingCalendar));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.MARKET_INDEX__SETTLE_CALENDAR:
				if (resolve) return getSettleCalendar();
				return basicGetSettleCalendar();
			case PricingPackage.MARKET_INDEX__PRICING_CALENDAR:
				if (resolve) return getPricingCalendar();
				return basicGetPricingCalendar();
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
			case PricingPackage.MARKET_INDEX__SETTLE_CALENDAR:
				setSettleCalendar((HolidayCalendar)newValue);
				return;
			case PricingPackage.MARKET_INDEX__PRICING_CALENDAR:
				setPricingCalendar((PricingCalendar)newValue);
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
			case PricingPackage.MARKET_INDEX__SETTLE_CALENDAR:
				setSettleCalendar((HolidayCalendar)null);
				return;
			case PricingPackage.MARKET_INDEX__PRICING_CALENDAR:
				setPricingCalendar((PricingCalendar)null);
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
			case PricingPackage.MARKET_INDEX__SETTLE_CALENDAR:
				return settleCalendar != null;
			case PricingPackage.MARKET_INDEX__PRICING_CALENDAR:
				return pricingCalendar != null;
		}
		return super.eIsSet(featureID);
	}

} //MarketIndexImpl
