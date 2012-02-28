/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl#getLoad <em>Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl#getDischarge <em>Discharge</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnscheduledCargoImpl extends MMXObjectImpl implements UnscheduledCargo {
	/**
	 * The cached value of the '{@link #getLoad() <em>Load</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoad()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation load;

	/**
	 * The cached value of the '{@link #getDischarge() <em>Discharge</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischarge()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation discharge;

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
	public SlotAllocation getLoad() {
		if (load != null && load.eIsProxy()) {
			InternalEObject oldLoad = (InternalEObject)load;
			load = (SlotAllocation)eResolveProxy(oldLoad);
			if (load != oldLoad) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.UNSCHEDULED_CARGO__LOAD, oldLoad, load));
			}
		}
		return load;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetLoad() {
		return load;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoad(SlotAllocation newLoad) {
		SlotAllocation oldLoad = load;
		load = newLoad;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.UNSCHEDULED_CARGO__LOAD, oldLoad, load));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getDischarge() {
		if (discharge != null && discharge.eIsProxy()) {
			InternalEObject oldDischarge = (InternalEObject)discharge;
			discharge = (SlotAllocation)eResolveProxy(oldDischarge);
			if (discharge != oldDischarge) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE, oldDischarge, discharge));
			}
		}
		return discharge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetDischarge() {
		return discharge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischarge(SlotAllocation newDischarge) {
		SlotAllocation oldDischarge = discharge;
		discharge = newDischarge;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE, oldDischarge, discharge));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD:
				if (resolve) return getLoad();
				return basicGetLoad();
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE:
				if (resolve) return getDischarge();
				return basicGetDischarge();
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
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD:
				setLoad((SlotAllocation)newValue);
				return;
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE:
				setDischarge((SlotAllocation)newValue);
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
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD:
				setLoad((SlotAllocation)null);
				return;
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE:
				setDischarge((SlotAllocation)null);
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
			case SchedulePackage.UNSCHEDULED_CARGO__LOAD:
				return load != null;
			case SchedulePackage.UNSCHEDULED_CARGO__DISCHARGE:
				return discharge != null;
		}
		return super.eIsSet(featureID);
	}

} // end of UnscheduledCargoImpl

// finish type fixing
