/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;

import com.mmxlabs.models.lng.types.VolumeUnits;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Volume Tier Price Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.VolumeTierPriceParametersImpl#getLowExpression <em>Low Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.VolumeTierPriceParametersImpl#getHighExpression <em>High Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.VolumeTierPriceParametersImpl#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.VolumeTierPriceParametersImpl#getVolumeLimitsUnit <em>Volume Limits Unit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VolumeTierPriceParametersImpl extends LNGPriceCalculatorParametersImpl implements VolumeTierPriceParameters {
	/**
	 * The default value of the '{@link #getLowExpression() <em>Low Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String LOW_EXPRESSION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getLowExpression() <em>Low Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowExpression()
	 * @generated
	 * @ordered
	 */
	protected String lowExpression = LOW_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getHighExpression() <em>High Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String HIGH_EXPRESSION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getHighExpression() <em>High Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighExpression()
	 * @generated
	 * @ordered
	 */
	protected String highExpression = HIGH_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThreshold()
	 * @generated
	 * @ordered
	 */
	protected static final double THRESHOLD_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThreshold()
	 * @generated
	 * @ordered
	 */
	protected double threshold = THRESHOLD_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeLimitsUnit() <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 * @ordered
	 */
	protected static final VolumeUnits VOLUME_LIMITS_UNIT_EDEFAULT = VolumeUnits.M3;

	/**
	 * The cached value of the '{@link #getVolumeLimitsUnit() <em>Volume Limits Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLimitsUnit()
	 * @generated
	 * @ordered
	 */
	protected VolumeUnits volumeLimitsUnit = VOLUME_LIMITS_UNIT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VolumeTierPriceParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.VOLUME_TIER_PRICE_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLowExpression() {
		return lowExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLowExpression(String newLowExpression) {
		String oldLowExpression = lowExpression;
		lowExpression = newLowExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION, oldLowExpression, lowExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getHighExpression() {
		return highExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHighExpression(String newHighExpression) {
		String oldHighExpression = highExpression;
		highExpression = newHighExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION, oldHighExpression, highExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getThreshold() {
		return threshold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setThreshold(double newThreshold) {
		double oldThreshold = threshold;
		threshold = newThreshold;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD, oldThreshold, threshold));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VolumeUnits getVolumeLimitsUnit() {
		return volumeLimitsUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeLimitsUnit(VolumeUnits newVolumeLimitsUnit) {
		VolumeUnits oldVolumeLimitsUnit = volumeLimitsUnit;
		volumeLimitsUnit = newVolumeLimitsUnit == null ? VOLUME_LIMITS_UNIT_EDEFAULT : newVolumeLimitsUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT, oldVolumeLimitsUnit, volumeLimitsUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION:
				return getLowExpression();
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION:
				return getHighExpression();
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD:
				return getThreshold();
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT:
				return getVolumeLimitsUnit();
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
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION:
				setLowExpression((String)newValue);
				return;
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION:
				setHighExpression((String)newValue);
				return;
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD:
				setThreshold((Double)newValue);
				return;
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT:
				setVolumeLimitsUnit((VolumeUnits)newValue);
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
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION:
				setLowExpression(LOW_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION:
				setHighExpression(HIGH_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD:
				setThreshold(THRESHOLD_EDEFAULT);
				return;
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT:
				setVolumeLimitsUnit(VOLUME_LIMITS_UNIT_EDEFAULT);
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
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION:
				return LOW_EXPRESSION_EDEFAULT == null ? lowExpression != null : !LOW_EXPRESSION_EDEFAULT.equals(lowExpression);
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION:
				return HIGH_EXPRESSION_EDEFAULT == null ? highExpression != null : !HIGH_EXPRESSION_EDEFAULT.equals(highExpression);
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD:
				return threshold != THRESHOLD_EDEFAULT;
			case CommercialPackage.VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT:
				return volumeLimitsUnit != VOLUME_LIMITS_UNIT_EDEFAULT;
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
		result.append(" (lowExpression: ");
		result.append(lowExpression);
		result.append(", highExpression: ");
		result.append(highExpression);
		result.append(", threshold: ");
		result.append(threshold);
		result.append(", volumeLimitsUnit: ");
		result.append(volumeLimitsUnit);
		result.append(')');
		return result.toString();
	}

} //VolumeTierPriceParametersImpl
