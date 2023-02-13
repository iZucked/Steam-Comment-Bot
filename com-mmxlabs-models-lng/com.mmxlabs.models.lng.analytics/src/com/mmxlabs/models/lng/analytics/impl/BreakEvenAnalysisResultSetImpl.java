/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet;
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
 * An implementation of the model object '<em><b>Break Even Analysis Result Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultSetImpl#getBasedOn <em>Based On</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultSetImpl#getResults <em>Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisResultSetImpl#getPrice <em>Price</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BreakEvenAnalysisResultSetImpl extends EObjectImpl implements BreakEvenAnalysisResultSet {
	/**
	 * The cached value of the '{@link #getBasedOn() <em>Based On</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasedOn()
	 * @generated
	 * @ordered
	 */
	protected BreakEvenAnalysisResult basedOn;

	/**
	 * The cached value of the '{@link #getResults() <em>Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResults()
	 * @generated
	 * @ordered
	 */
	protected EList<BreakEvenAnalysisResult> results;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BreakEvenAnalysisResultSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_RESULT_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenAnalysisResult getBasedOn() {
		if (basedOn != null && basedOn.eIsProxy()) {
			InternalEObject oldBasedOn = (InternalEObject)basedOn;
			basedOn = (BreakEvenAnalysisResult)eResolveProxy(oldBasedOn);
			if (basedOn != oldBasedOn) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON, oldBasedOn, basedOn));
			}
		}
		return basedOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BreakEvenAnalysisResult basicGetBasedOn() {
		return basedOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBasedOn(BreakEvenAnalysisResult newBasedOn) {
		BreakEvenAnalysisResult oldBasedOn = basedOn;
		basedOn = newBasedOn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON, oldBasedOn, basedOn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BreakEvenAnalysisResult> getResults() {
		if (results == null) {
			results = new EObjectContainmentEList<BreakEvenAnalysisResult>(BreakEvenAnalysisResult.class, this, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS);
		}
		return results;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS:
				return ((InternalEList<?>)getResults()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON:
				if (resolve) return getBasedOn();
				return basicGetBasedOn();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS:
				return getResults();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE:
				return getPrice();
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON:
				setBasedOn((BreakEvenAnalysisResult)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS:
				getResults().clear();
				getResults().addAll((Collection<? extends BreakEvenAnalysisResult>)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE:
				setPrice((Double)newValue);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON:
				setBasedOn((BreakEvenAnalysisResult)null);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS:
				getResults().clear();
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE:
				setPrice(PRICE_EDEFAULT);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__BASED_ON:
				return basedOn != null;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__RESULTS:
				return results != null && !results.isEmpty();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_RESULT_SET__PRICE:
				return price != PRICE_EDEFAULT;
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
		result.append(')');
		return result.toString();
	}

} //BreakEvenAnalysisResultSetImpl
