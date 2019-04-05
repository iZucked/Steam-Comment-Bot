/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ViabilityResult;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Viability Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl#getEarliestETA <em>Earliest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl#getLatestETA <em>Latest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl#getEarliestVolume <em>Earliest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl#getLatestVolume <em>Latest Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl#getEarliestPrice <em>Earliest Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityResultImpl#getLatestPrice <em>Latest Price</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ViabilityResultImpl extends EObjectImpl implements ViabilityResult {
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
	 * The default value of the '{@link #getLatestETA() <em>Latest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestETA()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate LATEST_ETA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLatestETA() <em>Latest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestETA()
	 * @generated
	 * @ordered
	 */
	protected LocalDate latestETA = LATEST_ETA_EDEFAULT;

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
	 * The default value of the '{@link #getLatestVolume() <em>Latest Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int LATEST_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLatestVolume() <em>Latest Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestVolume()
	 * @generated
	 * @ordered
	 */
	protected int latestVolume = LATEST_VOLUME_EDEFAULT;

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
	 * The default value of the '{@link #getLatestPrice() <em>Latest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double LATEST_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLatestPrice() <em>Latest Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestPrice()
	 * @generated
	 * @ordered
	 */
	protected double latestPrice = LATEST_PRICE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ViabilityResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.VIABILITY_RESULT;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.VIABILITY_RESULT__TARGET, oldTarget, target));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_RESULT__TARGET, oldTarget, target));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_RESULT__EARLIEST_ETA, oldEarliestETA, earliestETA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getLatestETA() {
		return latestETA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatestETA(LocalDate newLatestETA) {
		LocalDate oldLatestETA = latestETA;
		latestETA = newLatestETA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_RESULT__LATEST_ETA, oldLatestETA, latestETA));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_RESULT__EARLIEST_VOLUME, oldEarliestVolume, earliestVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getLatestVolume() {
		return latestVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatestVolume(int newLatestVolume) {
		int oldLatestVolume = latestVolume;
		latestVolume = newLatestVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_RESULT__LATEST_VOLUME, oldLatestVolume, latestVolume));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_RESULT__EARLIEST_PRICE, oldEarliestPrice, earliestPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getLatestPrice() {
		return latestPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatestPrice(double newLatestPrice) {
		double oldLatestPrice = latestPrice;
		latestPrice = newLatestPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_RESULT__LATEST_PRICE, oldLatestPrice, latestPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.VIABILITY_RESULT__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_ETA:
				return getEarliestETA();
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_ETA:
				return getLatestETA();
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_VOLUME:
				return getEarliestVolume();
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_VOLUME:
				return getLatestVolume();
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_PRICE:
				return getEarliestPrice();
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_PRICE:
				return getLatestPrice();
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
			case AnalyticsPackage.VIABILITY_RESULT__TARGET:
				setTarget((SpotMarket)newValue);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_ETA:
				setEarliestETA((LocalDate)newValue);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_ETA:
				setLatestETA((LocalDate)newValue);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_VOLUME:
				setEarliestVolume((Integer)newValue);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_VOLUME:
				setLatestVolume((Integer)newValue);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_PRICE:
				setEarliestPrice((Double)newValue);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_PRICE:
				setLatestPrice((Double)newValue);
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
			case AnalyticsPackage.VIABILITY_RESULT__TARGET:
				setTarget((SpotMarket)null);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_ETA:
				setEarliestETA(EARLIEST_ETA_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_ETA:
				setLatestETA(LATEST_ETA_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_VOLUME:
				setEarliestVolume(EARLIEST_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_VOLUME:
				setLatestVolume(LATEST_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_PRICE:
				setEarliestPrice(EARLIEST_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_PRICE:
				setLatestPrice(LATEST_PRICE_EDEFAULT);
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
			case AnalyticsPackage.VIABILITY_RESULT__TARGET:
				return target != null;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_ETA:
				return EARLIEST_ETA_EDEFAULT == null ? earliestETA != null : !EARLIEST_ETA_EDEFAULT.equals(earliestETA);
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_ETA:
				return LATEST_ETA_EDEFAULT == null ? latestETA != null : !LATEST_ETA_EDEFAULT.equals(latestETA);
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_VOLUME:
				return earliestVolume != EARLIEST_VOLUME_EDEFAULT;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_VOLUME:
				return latestVolume != LATEST_VOLUME_EDEFAULT;
			case AnalyticsPackage.VIABILITY_RESULT__EARLIEST_PRICE:
				return earliestPrice != EARLIEST_PRICE_EDEFAULT;
			case AnalyticsPackage.VIABILITY_RESULT__LATEST_PRICE:
				return latestPrice != LATEST_PRICE_EDEFAULT;
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
		result.append(", latestETA: ");
		result.append(latestETA);
		result.append(", earliestVolume: ");
		result.append(earliestVolume);
		result.append(", latestVolume: ");
		result.append(latestVolume);
		result.append(", earliestPrice: ");
		result.append(earliestPrice);
		result.append(", latestPrice: ");
		result.append(latestPrice);
		result.append(')');
		return result.toString();
	}

} //ViabilityResultImpl
