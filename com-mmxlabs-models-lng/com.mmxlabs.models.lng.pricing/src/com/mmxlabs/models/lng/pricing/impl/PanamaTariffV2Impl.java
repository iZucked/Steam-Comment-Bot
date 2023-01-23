/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.PanamaTariffV2;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Panama Tariff V2</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaTariffV2Impl#getFixedFee <em>Fixed Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaTariffV2Impl#getCapacityTariff <em>Capacity Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PanamaTariffV2Impl#getEffectiveFrom <em>Effective From</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PanamaTariffV2Impl extends EObjectImpl implements PanamaTariffV2 {
	/**
	 * The default value of the '{@link #getFixedFee() <em>Fixed Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFixedFee()
	 * @generated
	 * @ordered
	 */
	protected static final double FIXED_FEE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getFixedFee() <em>Fixed Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFixedFee()
	 * @generated
	 * @ordered
	 */
	protected double fixedFee = FIXED_FEE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCapacityTariff() <em>Capacity Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacityTariff()
	 * @generated
	 * @ordered
	 */
	protected static final double CAPACITY_TARIFF_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCapacityTariff() <em>Capacity Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacityTariff()
	 * @generated
	 * @ordered
	 */
	protected double capacityTariff = CAPACITY_TARIFF_EDEFAULT;

	/**
	 * The default value of the '{@link #getEffectiveFrom() <em>Effective From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffectiveFrom()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate EFFECTIVE_FROM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEffectiveFrom() <em>Effective From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffectiveFrom()
	 * @generated
	 * @ordered
	 */
	protected LocalDate effectiveFrom = EFFECTIVE_FROM_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaTariffV2Impl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PANAMA_TARIFF_V2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getFixedFee() {
		return fixedFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFixedFee(double newFixedFee) {
		double oldFixedFee = fixedFee;
		fixedFee = newFixedFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_TARIFF_V2__FIXED_FEE, oldFixedFee, fixedFee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCapacityTariff() {
		return capacityTariff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCapacityTariff(double newCapacityTariff) {
		double oldCapacityTariff = capacityTariff;
		capacityTariff = newCapacityTariff;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_TARIFF_V2__CAPACITY_TARIFF, oldCapacityTariff, capacityTariff));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getEffectiveFrom() {
		return effectiveFrom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEffectiveFrom(LocalDate newEffectiveFrom) {
		LocalDate oldEffectiveFrom = effectiveFrom;
		effectiveFrom = newEffectiveFrom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PANAMA_TARIFF_V2__EFFECTIVE_FROM, oldEffectiveFrom, effectiveFrom));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.PANAMA_TARIFF_V2__FIXED_FEE:
				return getFixedFee();
			case PricingPackage.PANAMA_TARIFF_V2__CAPACITY_TARIFF:
				return getCapacityTariff();
			case PricingPackage.PANAMA_TARIFF_V2__EFFECTIVE_FROM:
				return getEffectiveFrom();
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
			case PricingPackage.PANAMA_TARIFF_V2__FIXED_FEE:
				setFixedFee((Double)newValue);
				return;
			case PricingPackage.PANAMA_TARIFF_V2__CAPACITY_TARIFF:
				setCapacityTariff((Double)newValue);
				return;
			case PricingPackage.PANAMA_TARIFF_V2__EFFECTIVE_FROM:
				setEffectiveFrom((LocalDate)newValue);
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
			case PricingPackage.PANAMA_TARIFF_V2__FIXED_FEE:
				setFixedFee(FIXED_FEE_EDEFAULT);
				return;
			case PricingPackage.PANAMA_TARIFF_V2__CAPACITY_TARIFF:
				setCapacityTariff(CAPACITY_TARIFF_EDEFAULT);
				return;
			case PricingPackage.PANAMA_TARIFF_V2__EFFECTIVE_FROM:
				setEffectiveFrom(EFFECTIVE_FROM_EDEFAULT);
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
			case PricingPackage.PANAMA_TARIFF_V2__FIXED_FEE:
				return fixedFee != FIXED_FEE_EDEFAULT;
			case PricingPackage.PANAMA_TARIFF_V2__CAPACITY_TARIFF:
				return capacityTariff != CAPACITY_TARIFF_EDEFAULT;
			case PricingPackage.PANAMA_TARIFF_V2__EFFECTIVE_FROM:
				return EFFECTIVE_FROM_EDEFAULT == null ? effectiveFrom != null : !EFFECTIVE_FROM_EDEFAULT.equals(effectiveFrom);
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
		result.append(" (fixedFee: ");
		result.append(fixedFee);
		result.append(", capacityTariff: ");
		result.append(capacityTariff);
		result.append(", effectiveFrom: ");
		result.append(effectiveFrom);
		result.append(')');
		return result.toString();
	}

} //PanamaTariffV2Impl
