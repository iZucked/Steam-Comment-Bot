/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;

import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Panama Seasonality Record</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PanamaSeasonalityRecordImpl#getVesselGroupCanalParameter <em>Vessel Group Canal Parameter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PanamaSeasonalityRecordImpl#getStartDay <em>Start Day</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PanamaSeasonalityRecordImpl#getStartMonth <em>Start Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PanamaSeasonalityRecordImpl#getStartYear <em>Start Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PanamaSeasonalityRecordImpl#getNorthboundWaitingDays <em>Northbound Waiting Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PanamaSeasonalityRecordImpl#getSouthboundWaitingDays <em>Southbound Waiting Days</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PanamaSeasonalityRecordImpl extends EObjectImpl implements PanamaSeasonalityRecord {
	/**
	 * The cached value of the '{@link #getVesselGroupCanalParameter() <em>Vessel Group Canal Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselGroupCanalParameter()
	 * @generated
	 * @ordered
	 */
	protected VesselGroupCanalParameters vesselGroupCanalParameter;

	/**
	 * The default value of the '{@link #getStartDay() <em>Start Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDay()
	 * @generated
	 * @ordered
	 */
	protected static final int START_DAY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStartDay() <em>Start Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDay()
	 * @generated
	 * @ordered
	 */
	protected int startDay = START_DAY_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartMonth() <em>Start Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartMonth()
	 * @generated
	 * @ordered
	 */
	protected static final int START_MONTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStartMonth() <em>Start Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartMonth()
	 * @generated
	 * @ordered
	 */
	protected int startMonth = START_MONTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartYear() <em>Start Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartYear()
	 * @generated
	 * @ordered
	 */
	protected static final int START_YEAR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStartYear() <em>Start Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartYear()
	 * @generated
	 * @ordered
	 */
	protected int startYear = START_YEAR_EDEFAULT;

	/**
	 * The default value of the '{@link #getNorthboundWaitingDays() <em>Northbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNorthboundWaitingDays()
	 * @generated
	 * @ordered
	 */
	protected static final int NORTHBOUND_WAITING_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNorthboundWaitingDays() <em>Northbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNorthboundWaitingDays()
	 * @generated
	 * @ordered
	 */
	protected int northboundWaitingDays = NORTHBOUND_WAITING_DAYS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSouthboundWaitingDays() <em>Southbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSouthboundWaitingDays()
	 * @generated
	 * @ordered
	 */
	protected static final int SOUTHBOUND_WAITING_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSouthboundWaitingDays() <em>Southbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSouthboundWaitingDays()
	 * @generated
	 * @ordered
	 */
	protected int southboundWaitingDays = SOUTHBOUND_WAITING_DAYS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaSeasonalityRecordImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.PANAMA_SEASONALITY_RECORD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselGroupCanalParameters getVesselGroupCanalParameter() {
		if (vesselGroupCanalParameter != null && vesselGroupCanalParameter.eIsProxy()) {
			InternalEObject oldVesselGroupCanalParameter = (InternalEObject)vesselGroupCanalParameter;
			vesselGroupCanalParameter = (VesselGroupCanalParameters)eResolveProxy(oldVesselGroupCanalParameter);
			if (vesselGroupCanalParameter != oldVesselGroupCanalParameter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER, oldVesselGroupCanalParameter, vesselGroupCanalParameter));
			}
		}
		return vesselGroupCanalParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselGroupCanalParameters basicGetVesselGroupCanalParameter() {
		return vesselGroupCanalParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselGroupCanalParameter(VesselGroupCanalParameters newVesselGroupCanalParameter) {
		VesselGroupCanalParameters oldVesselGroupCanalParameter = vesselGroupCanalParameter;
		vesselGroupCanalParameter = newVesselGroupCanalParameter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER, oldVesselGroupCanalParameter, vesselGroupCanalParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getStartDay() {
		return startDay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartDay(int newStartDay) {
		int oldStartDay = startDay;
		startDay = newStartDay;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PANAMA_SEASONALITY_RECORD__START_DAY, oldStartDay, startDay));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getStartMonth() {
		return startMonth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartMonth(int newStartMonth) {
		int oldStartMonth = startMonth;
		startMonth = newStartMonth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PANAMA_SEASONALITY_RECORD__START_MONTH, oldStartMonth, startMonth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getStartYear() {
		return startYear;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartYear(int newStartYear) {
		int oldStartYear = startYear;
		startYear = newStartYear;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PANAMA_SEASONALITY_RECORD__START_YEAR, oldStartYear, startYear));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getNorthboundWaitingDays() {
		return northboundWaitingDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNorthboundWaitingDays(int newNorthboundWaitingDays) {
		int oldNorthboundWaitingDays = northboundWaitingDays;
		northboundWaitingDays = newNorthboundWaitingDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS, oldNorthboundWaitingDays, northboundWaitingDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSouthboundWaitingDays() {
		return southboundWaitingDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSouthboundWaitingDays(int newSouthboundWaitingDays) {
		int oldSouthboundWaitingDays = southboundWaitingDays;
		southboundWaitingDays = newSouthboundWaitingDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS, oldSouthboundWaitingDays, southboundWaitingDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER:
				if (resolve) return getVesselGroupCanalParameter();
				return basicGetVesselGroupCanalParameter();
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_DAY:
				return getStartDay();
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_MONTH:
				return getStartMonth();
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_YEAR:
				return getStartYear();
			case CargoPackage.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS:
				return getNorthboundWaitingDays();
			case CargoPackage.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS:
				return getSouthboundWaitingDays();
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
			case CargoPackage.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER:
				setVesselGroupCanalParameter((VesselGroupCanalParameters)newValue);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_DAY:
				setStartDay((Integer)newValue);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_MONTH:
				setStartMonth((Integer)newValue);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_YEAR:
				setStartYear((Integer)newValue);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS:
				setNorthboundWaitingDays((Integer)newValue);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS:
				setSouthboundWaitingDays((Integer)newValue);
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
			case CargoPackage.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER:
				setVesselGroupCanalParameter((VesselGroupCanalParameters)null);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_DAY:
				setStartDay(START_DAY_EDEFAULT);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_MONTH:
				setStartMonth(START_MONTH_EDEFAULT);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_YEAR:
				setStartYear(START_YEAR_EDEFAULT);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS:
				setNorthboundWaitingDays(NORTHBOUND_WAITING_DAYS_EDEFAULT);
				return;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS:
				setSouthboundWaitingDays(SOUTHBOUND_WAITING_DAYS_EDEFAULT);
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
			case CargoPackage.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER:
				return vesselGroupCanalParameter != null;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_DAY:
				return startDay != START_DAY_EDEFAULT;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_MONTH:
				return startMonth != START_MONTH_EDEFAULT;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_YEAR:
				return startYear != START_YEAR_EDEFAULT;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS:
				return northboundWaitingDays != NORTHBOUND_WAITING_DAYS_EDEFAULT;
			case CargoPackage.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS:
				return southboundWaitingDays != SOUTHBOUND_WAITING_DAYS_EDEFAULT;
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
		result.append(" (startDay: ");
		result.append(startDay);
		result.append(", startMonth: ");
		result.append(startMonth);
		result.append(", startYear: ");
		result.append(startYear);
		result.append(", northboundWaitingDays: ");
		result.append(northboundWaitingDays);
		result.append(", southboundWaitingDays: ");
		result.append(southboundWaitingDays);
		result.append(')');
		return result.toString();
	}

} //PanamaSeasonalityRecordImpl
