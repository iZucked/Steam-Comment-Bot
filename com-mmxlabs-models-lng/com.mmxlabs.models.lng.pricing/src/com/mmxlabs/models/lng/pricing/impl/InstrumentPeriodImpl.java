/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.InstrumentPeriod;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.types.PricingPeriod;
import com.mmxlabs.models.lng.types.TimePeriod;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Instrument Period</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.InstrumentPeriodImpl#getPeriodSize <em>Period Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.InstrumentPeriodImpl#getPeriodSizeUnit <em>Period Size Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.InstrumentPeriodImpl#getPeriodOffset <em>Period Offset</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InstrumentPeriodImpl extends EObjectImpl implements InstrumentPeriod {
	/**
	 * The default value of the '{@link #getPeriodSize() <em>Period Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodSize()
	 * @generated
	 * @ordered
	 */
	protected static final int PERIOD_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPeriodSize() <em>Period Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodSize()
	 * @generated
	 * @ordered
	 */
	protected int periodSize = PERIOD_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPeriodSizeUnit() <em>Period Size Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodSizeUnit()
	 * @generated
	 * @ordered
	 */
	protected static final PricingPeriod PERIOD_SIZE_UNIT_EDEFAULT = PricingPeriod.DAYS;

	/**
	 * The cached value of the '{@link #getPeriodSizeUnit() <em>Period Size Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodSizeUnit()
	 * @generated
	 * @ordered
	 */
	protected PricingPeriod periodSizeUnit = PERIOD_SIZE_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getPeriodOffset() <em>Period Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodOffset()
	 * @generated
	 * @ordered
	 */
	protected static final int PERIOD_OFFSET_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPeriodOffset() <em>Period Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodOffset()
	 * @generated
	 * @ordered
	 */
	protected int periodOffset = PERIOD_OFFSET_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InstrumentPeriodImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.INSTRUMENT_PERIOD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPeriodSize() {
		return periodSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPeriodSize(int newPeriodSize) {
		int oldPeriodSize = periodSize;
		periodSize = newPeriodSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE, oldPeriodSize, periodSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingPeriod getPeriodSizeUnit() {
		return periodSizeUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPeriodSizeUnit(PricingPeriod newPeriodSizeUnit) {
		PricingPeriod oldPeriodSizeUnit = periodSizeUnit;
		periodSizeUnit = newPeriodSizeUnit == null ? PERIOD_SIZE_UNIT_EDEFAULT : newPeriodSizeUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE_UNIT, oldPeriodSizeUnit, periodSizeUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPeriodOffset() {
		return periodOffset;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPeriodOffset(int newPeriodOffset) {
		int oldPeriodOffset = periodOffset;
		periodOffset = newPeriodOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.INSTRUMENT_PERIOD__PERIOD_OFFSET, oldPeriodOffset, periodOffset));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE:
				return getPeriodSize();
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE_UNIT:
				return getPeriodSizeUnit();
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_OFFSET:
				return getPeriodOffset();
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
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE:
				setPeriodSize((Integer)newValue);
				return;
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE_UNIT:
				setPeriodSizeUnit((PricingPeriod)newValue);
				return;
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_OFFSET:
				setPeriodOffset((Integer)newValue);
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
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE:
				setPeriodSize(PERIOD_SIZE_EDEFAULT);
				return;
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE_UNIT:
				setPeriodSizeUnit(PERIOD_SIZE_UNIT_EDEFAULT);
				return;
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_OFFSET:
				setPeriodOffset(PERIOD_OFFSET_EDEFAULT);
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
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE:
				return periodSize != PERIOD_SIZE_EDEFAULT;
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_SIZE_UNIT:
				return periodSizeUnit != PERIOD_SIZE_UNIT_EDEFAULT;
			case PricingPackage.INSTRUMENT_PERIOD__PERIOD_OFFSET:
				return periodOffset != PERIOD_OFFSET_EDEFAULT;
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
		result.append(" (periodSize: ");
		result.append(periodSize);
		result.append(", periodSizeUnit: ");
		result.append(periodSizeUnit);
		result.append(", periodOffset: ");
		result.append(periodOffset);
		result.append(')');
		return result.toString();
	}

} //InstrumentPeriodImpl
