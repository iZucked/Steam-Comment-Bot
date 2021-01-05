/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.CommodityCurve;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingPackage;
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
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl#getFlatCurve <em>Flat Curve</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl#getBidCurve <em>Bid Curve</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.MarketIndexImpl#getOfferCurve <em>Offer Curve</em>}</li>
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
	 * The cached value of the '{@link #getFlatCurve() <em>Flat Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlatCurve()
	 * @generated
	 * @ordered
	 */
	protected CommodityCurve flatCurve;

	/**
	 * The cached value of the '{@link #getBidCurve() <em>Bid Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBidCurve()
	 * @generated
	 * @ordered
	 */
	protected CommodityCurve bidCurve;

	/**
	 * The cached value of the '{@link #getOfferCurve() <em>Offer Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOfferCurve()
	 * @generated
	 * @ordered
	 */
	protected CommodityCurve offerCurve;

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
	public CommodityCurve getFlatCurve() {
		if (flatCurve != null && flatCurve.eIsProxy()) {
			InternalEObject oldFlatCurve = (InternalEObject)flatCurve;
			flatCurve = (CommodityCurve)eResolveProxy(oldFlatCurve);
			if (flatCurve != oldFlatCurve) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.MARKET_INDEX__FLAT_CURVE, oldFlatCurve, flatCurve));
			}
		}
		return flatCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityCurve basicGetFlatCurve() {
		return flatCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFlatCurve(CommodityCurve newFlatCurve) {
		CommodityCurve oldFlatCurve = flatCurve;
		flatCurve = newFlatCurve;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.MARKET_INDEX__FLAT_CURVE, oldFlatCurve, flatCurve));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CommodityCurve getBidCurve() {
		if (bidCurve != null && bidCurve.eIsProxy()) {
			InternalEObject oldBidCurve = (InternalEObject)bidCurve;
			bidCurve = (CommodityCurve)eResolveProxy(oldBidCurve);
			if (bidCurve != oldBidCurve) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.MARKET_INDEX__BID_CURVE, oldBidCurve, bidCurve));
			}
		}
		return bidCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityCurve basicGetBidCurve() {
		return bidCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBidCurve(CommodityCurve newBidCurve) {
		CommodityCurve oldBidCurve = bidCurve;
		bidCurve = newBidCurve;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.MARKET_INDEX__BID_CURVE, oldBidCurve, bidCurve));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CommodityCurve getOfferCurve() {
		if (offerCurve != null && offerCurve.eIsProxy()) {
			InternalEObject oldOfferCurve = (InternalEObject)offerCurve;
			offerCurve = (CommodityCurve)eResolveProxy(oldOfferCurve);
			if (offerCurve != oldOfferCurve) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.MARKET_INDEX__OFFER_CURVE, oldOfferCurve, offerCurve));
			}
		}
		return offerCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityCurve basicGetOfferCurve() {
		return offerCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOfferCurve(CommodityCurve newOfferCurve) {
		CommodityCurve oldOfferCurve = offerCurve;
		offerCurve = newOfferCurve;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.MARKET_INDEX__OFFER_CURVE, oldOfferCurve, offerCurve));
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
			case PricingPackage.MARKET_INDEX__FLAT_CURVE:
				if (resolve) return getFlatCurve();
				return basicGetFlatCurve();
			case PricingPackage.MARKET_INDEX__BID_CURVE:
				if (resolve) return getBidCurve();
				return basicGetBidCurve();
			case PricingPackage.MARKET_INDEX__OFFER_CURVE:
				if (resolve) return getOfferCurve();
				return basicGetOfferCurve();
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
			case PricingPackage.MARKET_INDEX__FLAT_CURVE:
				setFlatCurve((CommodityCurve)newValue);
				return;
			case PricingPackage.MARKET_INDEX__BID_CURVE:
				setBidCurve((CommodityCurve)newValue);
				return;
			case PricingPackage.MARKET_INDEX__OFFER_CURVE:
				setOfferCurve((CommodityCurve)newValue);
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
			case PricingPackage.MARKET_INDEX__FLAT_CURVE:
				setFlatCurve((CommodityCurve)null);
				return;
			case PricingPackage.MARKET_INDEX__BID_CURVE:
				setBidCurve((CommodityCurve)null);
				return;
			case PricingPackage.MARKET_INDEX__OFFER_CURVE:
				setOfferCurve((CommodityCurve)null);
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
			case PricingPackage.MARKET_INDEX__FLAT_CURVE:
				return flatCurve != null;
			case PricingPackage.MARKET_INDEX__BID_CURVE:
				return bidCurve != null;
			case PricingPackage.MARKET_INDEX__OFFER_CURVE:
				return offerCurve != null;
		}
		return super.eIsSet(featureID);
	}

} //MarketIndexImpl
