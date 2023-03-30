/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MarketabilityResult;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Marketability Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultImpl#getEarliestETA <em>Earliest ETA</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultImpl#getLatestETA <em>Latest ETA</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketabilityResultImpl extends EObjectImpl implements MarketabilityResult {
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
	protected static final ZonedDateTime EARLIEST_ETA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEarliestETA() <em>Earliest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestETA()
	 * @generated
	 * @ordered
	 */
	protected ZonedDateTime earliestETA = EARLIEST_ETA_EDEFAULT;

	/**
	 * The default value of the '{@link #getLatestETA() <em>Latest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestETA()
	 * @generated
	 * @ordered
	 */
	protected static final ZonedDateTime LATEST_ETA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLatestETA() <em>Latest ETA</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestETA()
	 * @generated
	 * @ordered
	 */
	protected ZonedDateTime latestETA = LATEST_ETA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketabilityResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MARKETABILITY_RESULT;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_RESULT__TARGET, oldTarget, target));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ZonedDateTime getEarliestETA() {
		return earliestETA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEarliestETA(ZonedDateTime newEarliestETA) {
		ZonedDateTime oldEarliestETA = earliestETA;
		earliestETA = newEarliestETA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT__EARLIEST_ETA, oldEarliestETA, earliestETA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ZonedDateTime getLatestETA() {
		return latestETA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatestETA(ZonedDateTime newLatestETA) {
		ZonedDateTime oldLatestETA = latestETA;
		latestETA = newLatestETA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT__LATEST_ETA, oldLatestETA, latestETA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.MARKETABILITY_RESULT__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case AnalyticsPackage.MARKETABILITY_RESULT__EARLIEST_ETA:
				return getEarliestETA();
			case AnalyticsPackage.MARKETABILITY_RESULT__LATEST_ETA:
				return getLatestETA();
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
			case AnalyticsPackage.MARKETABILITY_RESULT__TARGET:
				setTarget((SpotMarket)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT__EARLIEST_ETA:
				setEarliestETA((ZonedDateTime)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT__LATEST_ETA:
				setLatestETA((ZonedDateTime)newValue);
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
			case AnalyticsPackage.MARKETABILITY_RESULT__TARGET:
				setTarget((SpotMarket)null);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT__EARLIEST_ETA:
				setEarliestETA(EARLIEST_ETA_EDEFAULT);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT__LATEST_ETA:
				setLatestETA(LATEST_ETA_EDEFAULT);
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
			case AnalyticsPackage.MARKETABILITY_RESULT__TARGET:
				return target != null;
			case AnalyticsPackage.MARKETABILITY_RESULT__EARLIEST_ETA:
				return EARLIEST_ETA_EDEFAULT == null ? earliestETA != null : !EARLIEST_ETA_EDEFAULT.equals(earliestETA);
			case AnalyticsPackage.MARKETABILITY_RESULT__LATEST_ETA:
				return LATEST_ETA_EDEFAULT == null ? latestETA != null : !LATEST_ETA_EDEFAULT.equals(latestETA);
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
		result.append(')');
		return result.toString();
	}

} //MarketabilityResultImpl
