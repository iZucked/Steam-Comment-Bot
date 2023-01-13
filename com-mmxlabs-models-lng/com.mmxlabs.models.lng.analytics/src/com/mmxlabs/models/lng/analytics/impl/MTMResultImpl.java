/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MTMResult;
import com.mmxlabs.models.lng.analytics.ShippingOption;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MTM Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getEarliestETA <em>Earliest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getEarliestVolume <em>Earliest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getEarliestPrice <em>Earliest Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getShippingCost <em>Shipping Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getOriginalVolume <em>Original Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getOriginalPrice <em>Original Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMResultImpl#getTotalShippingCost <em>Total Shipping Cost</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MTMResultImpl extends EObjectImpl implements MTMResult {
	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected SpotMarket target;

	/**
	 * The default value of the '{@link #getEarliestETA() <em>Earliest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestETA()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate EARLIEST_ETA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEarliestETA() <em>Earliest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestETA()
	 * @generated
	 * @ordered
	 */
	protected LocalDate earliestETA = EARLIEST_ETA_EDEFAULT;

	/**
	 * The default value of the '{@link #getEarliestVolume() <em>Earliest Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int EARLIEST_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEarliestVolume() <em>Earliest Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestVolume()
	 * @generated
	 * @ordered
	 */
	protected int earliestVolume = EARLIEST_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getEarliestPrice() <em>Earliest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double EARLIEST_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getEarliestPrice() <em>Earliest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestPrice()
	 * @generated
	 * @ordered
	 */
	protected double earliestPrice = EARLIEST_PRICE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getShipping() <em>Shipping</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShipping()
	 * @generated
	 * @ordered
	 */
	protected ShippingOption shipping;

	/**
	 * The default value of the '{@link #getShippingCost() <em>Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingCost()
	 * @generated
	 * @ordered
	 */
	protected static final double SHIPPING_COST_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getShippingCost() <em>Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingCost()
	 * @generated
	 * @ordered
	 */
	protected double shippingCost = SHIPPING_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginalVolume() <em>Original Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int ORIGINAL_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOriginalVolume() <em>Original Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVolume()
	 * @generated
	 * @ordered
	 */
	protected int originalVolume = ORIGINAL_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginalPrice() <em>Original Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double ORIGINAL_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getOriginalPrice() <em>Original Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalPrice()
	 * @generated
	 * @ordered
	 */
	protected double originalPrice = ORIGINAL_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalShippingCost() <em>Total Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalShippingCost()
	 * @generated
	 * @ordered
	 */
	protected static final int TOTAL_SHIPPING_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTotalShippingCost() <em>Total Shipping Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalShippingCost()
	 * @generated
	 * @ordered
	 */
	protected int totalShippingCost = TOTAL_SHIPPING_COST_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MTMResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MTM_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotMarket getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = (SpotMarket)eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MTM_RESULT__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTarget(SpotMarket newTarget) {
		SpotMarket oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getEarliestETA() {
		return earliestETA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEarliestETA(LocalDate newEarliestETA) {
		LocalDate oldEarliestETA = earliestETA;
		earliestETA = newEarliestETA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__EARLIEST_ETA, oldEarliestETA, earliestETA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getEarliestVolume() {
		return earliestVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEarliestVolume(int newEarliestVolume) {
		int oldEarliestVolume = earliestVolume;
		earliestVolume = newEarliestVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__EARLIEST_VOLUME, oldEarliestVolume, earliestVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getEarliestPrice() {
		return earliestPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEarliestPrice(double newEarliestPrice) {
		double oldEarliestPrice = earliestPrice;
		earliestPrice = newEarliestPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__EARLIEST_PRICE, oldEarliestPrice, earliestPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ShippingOption getShipping() {
		return shipping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetShipping(ShippingOption newShipping, NotificationChain msgs) {
		ShippingOption oldShipping = shipping;
		shipping = newShipping;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__SHIPPING, oldShipping, newShipping);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShipping(ShippingOption newShipping) {
		if (newShipping != shipping) {
			NotificationChain msgs = null;
			if (shipping != null)
				msgs = ((InternalEObject)shipping).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.MTM_RESULT__SHIPPING, null, msgs);
			if (newShipping != null)
				msgs = ((InternalEObject)newShipping).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.MTM_RESULT__SHIPPING, null, msgs);
			msgs = basicSetShipping(newShipping, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__SHIPPING, newShipping, newShipping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getShippingCost() {
		return shippingCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShippingCost(double newShippingCost) {
		double oldShippingCost = shippingCost;
		shippingCost = newShippingCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__SHIPPING_COST, oldShippingCost, shippingCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getOriginalVolume() {
		return originalVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOriginalVolume(int newOriginalVolume) {
		int oldOriginalVolume = originalVolume;
		originalVolume = newOriginalVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__ORIGINAL_VOLUME, oldOriginalVolume, originalVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getOriginalPrice() {
		return originalPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOriginalPrice(double newOriginalPrice) {
		double oldOriginalPrice = originalPrice;
		originalPrice = newOriginalPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__ORIGINAL_PRICE, oldOriginalPrice, originalPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTotalShippingCost() {
		return totalShippingCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTotalShippingCost(int newTotalShippingCost) {
		int oldTotalShippingCost = totalShippingCost;
		totalShippingCost = newTotalShippingCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MTM_RESULT__TOTAL_SHIPPING_COST, oldTotalShippingCost, totalShippingCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MTM_RESULT__SHIPPING:
				return basicSetShipping(null, msgs);
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
			case AnalyticsPackage.MTM_RESULT__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case AnalyticsPackage.MTM_RESULT__EARLIEST_ETA:
				return getEarliestETA();
			case AnalyticsPackage.MTM_RESULT__EARLIEST_VOLUME:
				return getEarliestVolume();
			case AnalyticsPackage.MTM_RESULT__EARLIEST_PRICE:
				return getEarliestPrice();
			case AnalyticsPackage.MTM_RESULT__SHIPPING:
				return getShipping();
			case AnalyticsPackage.MTM_RESULT__SHIPPING_COST:
				return getShippingCost();
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_VOLUME:
				return getOriginalVolume();
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_PRICE:
				return getOriginalPrice();
			case AnalyticsPackage.MTM_RESULT__TOTAL_SHIPPING_COST:
				return getTotalShippingCost();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AnalyticsPackage.MTM_RESULT__TARGET:
				setTarget((SpotMarket)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_ETA:
				setEarliestETA((LocalDate)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_VOLUME:
				setEarliestVolume((Integer)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_PRICE:
				setEarliestPrice((Double)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__SHIPPING:
				setShipping((ShippingOption)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__SHIPPING_COST:
				setShippingCost((Double)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_VOLUME:
				setOriginalVolume((Integer)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_PRICE:
				setOriginalPrice((Double)newValue);
				return;
			case AnalyticsPackage.MTM_RESULT__TOTAL_SHIPPING_COST:
				setTotalShippingCost((Integer)newValue);
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
			case AnalyticsPackage.MTM_RESULT__TARGET:
				setTarget((SpotMarket)null);
				return;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_ETA:
				setEarliestETA(EARLIEST_ETA_EDEFAULT);
				return;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_VOLUME:
				setEarliestVolume(EARLIEST_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_PRICE:
				setEarliestPrice(EARLIEST_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.MTM_RESULT__SHIPPING:
				setShipping((ShippingOption)null);
				return;
			case AnalyticsPackage.MTM_RESULT__SHIPPING_COST:
				setShippingCost(SHIPPING_COST_EDEFAULT);
				return;
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_VOLUME:
				setOriginalVolume(ORIGINAL_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_PRICE:
				setOriginalPrice(ORIGINAL_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.MTM_RESULT__TOTAL_SHIPPING_COST:
				setTotalShippingCost(TOTAL_SHIPPING_COST_EDEFAULT);
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
			case AnalyticsPackage.MTM_RESULT__TARGET:
				return target != null;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_ETA:
				return EARLIEST_ETA_EDEFAULT == null ? earliestETA != null : !EARLIEST_ETA_EDEFAULT.equals(earliestETA);
			case AnalyticsPackage.MTM_RESULT__EARLIEST_VOLUME:
				return earliestVolume != EARLIEST_VOLUME_EDEFAULT;
			case AnalyticsPackage.MTM_RESULT__EARLIEST_PRICE:
				return earliestPrice != EARLIEST_PRICE_EDEFAULT;
			case AnalyticsPackage.MTM_RESULT__SHIPPING:
				return shipping != null;
			case AnalyticsPackage.MTM_RESULT__SHIPPING_COST:
				return shippingCost != SHIPPING_COST_EDEFAULT;
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_VOLUME:
				return originalVolume != ORIGINAL_VOLUME_EDEFAULT;
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_PRICE:
				return originalPrice != ORIGINAL_PRICE_EDEFAULT;
			case AnalyticsPackage.MTM_RESULT__TOTAL_SHIPPING_COST:
				return totalShippingCost != TOTAL_SHIPPING_COST_EDEFAULT;
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
		result.append(" (earliestETA: ");
		result.append(earliestETA);
		result.append(", earliestVolume: ");
		result.append(earliestVolume);
		result.append(", earliestPrice: ");
		result.append(earliestPrice);
		result.append(", shippingCost: ");
		result.append(shippingCost);
		result.append(", originalVolume: ");
		result.append(originalVolume);
		result.append(", originalPrice: ");
		result.append(originalPrice);
		result.append(", totalShippingCost: ");
		result.append(totalShippingCost);
		result.append(')');
		return result.toString();
	}

} //MTMResultImpl
