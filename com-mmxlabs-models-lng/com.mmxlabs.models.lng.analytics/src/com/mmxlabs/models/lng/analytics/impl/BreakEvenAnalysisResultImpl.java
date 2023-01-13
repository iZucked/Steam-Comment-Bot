/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Break Even Analysis Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl#getEta <em>Eta</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultImpl#getReferencePrice <em>Reference Price</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BreakEvenAnalysisResultImpl extends EObjectImpl implements BreakEvenAnalysisResult {
	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject target;

	/**
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected double price = PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEta() <em>Eta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEta()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate ETA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEta() <em>Eta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEta()
	 * @generated
	 * @ordered
	 */
	protected LocalDate eta = ETA_EDEFAULT;

	/**
	 * The default value of the '{@link #getReferencePrice() <em>Reference Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencePrice()
	 * @generated
	 * @ordered
	 */
	protected static final double REFERENCE_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getReferencePrice() <em>Reference Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencePrice()
	 * @generated
	 * @ordered
	 */
	protected double referencePrice = REFERENCE_PRICE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BreakEvenAnalysisResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTarget(EObject newTarget) {
		EObject oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPrice(double newPrice) {
		double oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getEta() {
		return eta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEta(LocalDate newEta) {
		LocalDate oldEta = eta;
		eta = newEta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__ETA, oldEta, eta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getReferencePrice() {
		return referencePrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferencePrice(double newReferencePrice) {
		double oldReferencePrice = referencePrice;
		referencePrice = newReferencePrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE, oldReferencePrice, referencePrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__PRICE:
				return getPrice();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__ETA:
				return getEta();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE:
				return getReferencePrice();
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__TARGET:
				setTarget((EObject)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__PRICE:
				setPrice((Double)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__ETA:
				setEta((LocalDate)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE:
				setReferencePrice((Double)newValue);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__TARGET:
				setTarget((EObject)null);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__ETA:
				setEta(ETA_EDEFAULT);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE:
				setReferencePrice(REFERENCE_PRICE_EDEFAULT);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__TARGET:
				return target != null;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__PRICE:
				return price != PRICE_EDEFAULT;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__ETA:
				return ETA_EDEFAULT == null ? eta != null : !ETA_EDEFAULT.equals(eta);
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT__REFERENCE_PRICE:
				return referencePrice != REFERENCE_PRICE_EDEFAULT;
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
		result.append(" (price: ");
		result.append(price);
		result.append(", eta: ");
		result.append(eta);
		result.append(", referencePrice: ");
		result.append(referencePrice);
		result.append(')');
		return result.toString();
	}

} //BreakEvenAnalysisResultImpl
