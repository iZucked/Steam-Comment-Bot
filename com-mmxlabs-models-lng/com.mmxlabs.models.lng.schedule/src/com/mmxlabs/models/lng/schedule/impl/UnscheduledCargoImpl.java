/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.UnscheduledCargo;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unscheduled Cargo</b></em>'.
 * @deprecated
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnscheduledCargoImpl extends MMXObjectImpl implements UnscheduledCargo {
	/**
	 * The cached value of the '{@link #getLoadAllocation() <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation loadAllocation;

	/**
	 * The cached value of the '{@link #getDischargeAllocation() <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation dischargeAllocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnscheduledCargoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.UNSCHEDULED_CARGO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getLoadAllocation() {
		if (loadAllocation != null && loadAllocation.eIsProxy()) {
			InternalEObject oldLoadAllocation = (InternalEObject)loadAllocation;
			loadAllocation = (SlotAllocation)eResolveProxy(oldLoadAllocation);
			if (loadAllocation != oldLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.UNSCHEDULED_CARGO__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
			}
		}
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetLoadAllocation() {
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadAllocation(SlotAllocation newLoadAllocation) {
		SlotAllocation oldLoadAllocation = loadAllocation;
		loadAllocation = newLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.UNSCHEDULED_CARGO__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getDischargeAllocation() {
		if (dischargeAllocation != null && dischargeAllocation.eIsProxy()) {
			InternalEObject oldDischargeAllocation = (InternalEObject)dischargeAllocation;
			dischargeAllocation = (SlotAllocation)eResolveProxy(oldDischargeAllocation);
			if (dischargeAllocation != oldDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
			}
		}
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetDischargeAllocation() {
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeAllocation(SlotAllocation newDischargeAllocation) {
		SlotAllocation oldDischargeAllocation = dischargeAllocation;
		dischargeAllocation = newDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD_ALLOCATION:
				if (resolve) return getLoadAllocation();
				return basicGetLoadAllocation();
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE_ALLOCATION:
				if (resolve) return getDischargeAllocation();
				return basicGetDischargeAllocation();
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
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)newValue);
				return;
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)newValue);
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
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)null);
				return;
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)null);
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
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD_ALLOCATION:
				return loadAllocation != null;
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE_ALLOCATION:
				return dischargeAllocation != null;
		}
		return super.eIsSet(featureID);
	}

} // end of UnscheduledCargoImpl

// finish type fixing
