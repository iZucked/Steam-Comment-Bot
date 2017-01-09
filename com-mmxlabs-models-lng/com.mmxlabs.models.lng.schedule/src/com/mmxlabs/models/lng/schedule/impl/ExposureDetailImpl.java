/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.pricing.CommodityIndex;

import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.time.YearMonth;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exposure Detail</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getVolumeInMMBTU <em>Volume In MMBTU</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getVolumeInNativeUnits <em>Volume In Native Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getUnitPrice <em>Unit Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getNativeValue <em>Native Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl#getCurrencyUnit <em>Currency Unit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExposureDetailImpl extends EObjectImpl implements ExposureDetail {
	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected CommodityIndex index;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected YearMonth date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeInMMBTU() <em>Volume In MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInMMBTU()
	 * @generated
	 * @ordered
	 */
	protected static final double VOLUME_IN_MMBTU_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getVolumeInMMBTU() <em>Volume In MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInMMBTU()
	 * @generated
	 * @ordered
	 */
	protected double volumeInMMBTU = VOLUME_IN_MMBTU_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeInNativeUnits() <em>Volume In Native Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInNativeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final double VOLUME_IN_NATIVE_UNITS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getVolumeInNativeUnits() <em>Volume In Native Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeInNativeUnits()
	 * @generated
	 * @ordered
	 */
	protected double volumeInNativeUnits = VOLUME_IN_NATIVE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double UNIT_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected double unitPrice = UNIT_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getNativeValue() <em>Native Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNativeValue()
	 * @generated
	 * @ordered
	 */
	protected static final double NATIVE_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getNativeValue() <em>Native Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNativeValue()
	 * @generated
	 * @ordered
	 */
	protected double nativeValue = NATIVE_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeUnit() <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String VOLUME_UNIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVolumeUnit() <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnit()
	 * @generated
	 * @ordered
	 */
	protected String volumeUnit = VOLUME_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getCurrencyUnit() <em>Currency Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrencyUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String CURRENCY_UNIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCurrencyUnit() <em>Currency Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrencyUnit()
	 * @generated
	 * @ordered
	 */
	protected String currencyUnit = CURRENCY_UNIT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExposureDetailImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.EXPOSURE_DETAIL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex getIndex() {
		if (index != null && index.eIsProxy()) {
			InternalEObject oldIndex = (InternalEObject)index;
			index = (CommodityIndex)eResolveProxy(oldIndex);
			if (index != oldIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.EXPOSURE_DETAIL__INDEX, oldIndex, index));
			}
		}
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityIndex basicGetIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(CommodityIndex newIndex) {
		CommodityIndex oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public YearMonth getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDate(YearMonth newDate) {
		YearMonth oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getVolumeInMMBTU() {
		return volumeInMMBTU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeInMMBTU(double newVolumeInMMBTU) {
		double oldVolumeInMMBTU = volumeInMMBTU;
		volumeInMMBTU = newVolumeInMMBTU;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_MMBTU, oldVolumeInMMBTU, volumeInMMBTU));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getVolumeInNativeUnits() {
		return volumeInNativeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeInNativeUnits(double newVolumeInNativeUnits) {
		double oldVolumeInNativeUnits = volumeInNativeUnits;
		volumeInNativeUnits = newVolumeInNativeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS, oldVolumeInNativeUnits, volumeInNativeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnitPrice(double newUnitPrice) {
		double oldUnitPrice = unitPrice;
		unitPrice = newUnitPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__UNIT_PRICE, oldUnitPrice, unitPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getNativeValue() {
		return nativeValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNativeValue(double newNativeValue) {
		double oldNativeValue = nativeValue;
		nativeValue = newNativeValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__NATIVE_VALUE, oldNativeValue, nativeValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVolumeUnit() {
		return volumeUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeUnit(String newVolumeUnit) {
		String oldVolumeUnit = volumeUnit;
		volumeUnit = newVolumeUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__VOLUME_UNIT, oldVolumeUnit, volumeUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrencyUnit(String newCurrencyUnit) {
		String oldCurrencyUnit = currencyUnit;
		currencyUnit = newCurrencyUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EXPOSURE_DETAIL__CURRENCY_UNIT, oldCurrencyUnit, currencyUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.EXPOSURE_DETAIL__INDEX:
				if (resolve) return getIndex();
				return basicGetIndex();
			case SchedulePackage.EXPOSURE_DETAIL__DATE:
				return getDate();
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_MMBTU:
				return getVolumeInMMBTU();
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS:
				return getVolumeInNativeUnits();
			case SchedulePackage.EXPOSURE_DETAIL__UNIT_PRICE:
				return getUnitPrice();
			case SchedulePackage.EXPOSURE_DETAIL__NATIVE_VALUE:
				return getNativeValue();
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_UNIT:
				return getVolumeUnit();
			case SchedulePackage.EXPOSURE_DETAIL__CURRENCY_UNIT:
				return getCurrencyUnit();
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
			case SchedulePackage.EXPOSURE_DETAIL__INDEX:
				setIndex((CommodityIndex)newValue);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__DATE:
				setDate((YearMonth)newValue);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_MMBTU:
				setVolumeInMMBTU((Double)newValue);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS:
				setVolumeInNativeUnits((Double)newValue);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__UNIT_PRICE:
				setUnitPrice((Double)newValue);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__NATIVE_VALUE:
				setNativeValue((Double)newValue);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_UNIT:
				setVolumeUnit((String)newValue);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__CURRENCY_UNIT:
				setCurrencyUnit((String)newValue);
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
			case SchedulePackage.EXPOSURE_DETAIL__INDEX:
				setIndex((CommodityIndex)null);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_MMBTU:
				setVolumeInMMBTU(VOLUME_IN_MMBTU_EDEFAULT);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS:
				setVolumeInNativeUnits(VOLUME_IN_NATIVE_UNITS_EDEFAULT);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__UNIT_PRICE:
				setUnitPrice(UNIT_PRICE_EDEFAULT);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__NATIVE_VALUE:
				setNativeValue(NATIVE_VALUE_EDEFAULT);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_UNIT:
				setVolumeUnit(VOLUME_UNIT_EDEFAULT);
				return;
			case SchedulePackage.EXPOSURE_DETAIL__CURRENCY_UNIT:
				setCurrencyUnit(CURRENCY_UNIT_EDEFAULT);
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
			case SchedulePackage.EXPOSURE_DETAIL__INDEX:
				return index != null;
			case SchedulePackage.EXPOSURE_DETAIL__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_MMBTU:
				return volumeInMMBTU != VOLUME_IN_MMBTU_EDEFAULT;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS:
				return volumeInNativeUnits != VOLUME_IN_NATIVE_UNITS_EDEFAULT;
			case SchedulePackage.EXPOSURE_DETAIL__UNIT_PRICE:
				return unitPrice != UNIT_PRICE_EDEFAULT;
			case SchedulePackage.EXPOSURE_DETAIL__NATIVE_VALUE:
				return nativeValue != NATIVE_VALUE_EDEFAULT;
			case SchedulePackage.EXPOSURE_DETAIL__VOLUME_UNIT:
				return VOLUME_UNIT_EDEFAULT == null ? volumeUnit != null : !VOLUME_UNIT_EDEFAULT.equals(volumeUnit);
			case SchedulePackage.EXPOSURE_DETAIL__CURRENCY_UNIT:
				return CURRENCY_UNIT_EDEFAULT == null ? currencyUnit != null : !CURRENCY_UNIT_EDEFAULT.equals(currencyUnit);
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (date: ");
		result.append(date);
		result.append(", volumeInMMBTU: ");
		result.append(volumeInMMBTU);
		result.append(", volumeInNativeUnits: ");
		result.append(volumeInNativeUnits);
		result.append(", unitPrice: ");
		result.append(unitPrice);
		result.append(", nativeValue: ");
		result.append(nativeValue);
		result.append(", volumeUnit: ");
		result.append(volumeUnit);
		result.append(", currencyUnit: ");
		result.append(currencyUnit);
		result.append(')');
		return result.toString();
	}

} //ExposureDetailImpl
