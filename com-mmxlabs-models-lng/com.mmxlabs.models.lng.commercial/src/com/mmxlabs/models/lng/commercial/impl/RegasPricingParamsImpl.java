/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.RegasPricingParams;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Regas Pricing Params</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.RegasPricingParamsImpl#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.RegasPricingParamsImpl#getNumPricingDays <em>Num Pricing Days</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RegasPricingParamsImpl extends LNGPriceCalculatorParametersImpl implements RegasPricingParams {
	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumPricingDays() <em>Num Pricing Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumPricingDays()
	 * @generated
	 * @ordered
	 */
	protected static final int NUM_PRICING_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumPricingDays() <em>Num Pricing Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumPricingDays()
	 * @generated
	 * @ordered
	 */
	protected int numPricingDays = NUM_PRICING_DAYS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RegasPricingParamsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.REGAS_PRICING_PARAMS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.REGAS_PRICING_PARAMS__PRICE_EXPRESSION, oldPriceExpression, priceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getNumPricingDays() {
		return numPricingDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNumPricingDays(int newNumPricingDays) {
		int oldNumPricingDays = numPricingDays;
		numPricingDays = newNumPricingDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.REGAS_PRICING_PARAMS__NUM_PRICING_DAYS, oldNumPricingDays, numPricingDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.REGAS_PRICING_PARAMS__PRICE_EXPRESSION:
				return getPriceExpression();
			case CommercialPackage.REGAS_PRICING_PARAMS__NUM_PRICING_DAYS:
				return getNumPricingDays();
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
			case CommercialPackage.REGAS_PRICING_PARAMS__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
				return;
			case CommercialPackage.REGAS_PRICING_PARAMS__NUM_PRICING_DAYS:
				setNumPricingDays((Integer)newValue);
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
			case CommercialPackage.REGAS_PRICING_PARAMS__PRICE_EXPRESSION:
				setPriceExpression(PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.REGAS_PRICING_PARAMS__NUM_PRICING_DAYS:
				setNumPricingDays(NUM_PRICING_DAYS_EDEFAULT);
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
			case CommercialPackage.REGAS_PRICING_PARAMS__PRICE_EXPRESSION:
				return PRICE_EXPRESSION_EDEFAULT == null ? priceExpression != null : !PRICE_EXPRESSION_EDEFAULT.equals(priceExpression);
			case CommercialPackage.REGAS_PRICING_PARAMS__NUM_PRICING_DAYS:
				return numPricingDays != NUM_PRICING_DAYS_EDEFAULT;
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
		result.append(" (priceExpression: ");
		result.append(priceExpression);
		result.append(", numPricingDays: ");
		result.append(numPricingDays);
		result.append(')');
		return result.toString();
	}

} //RegasPricingParamsImpl
