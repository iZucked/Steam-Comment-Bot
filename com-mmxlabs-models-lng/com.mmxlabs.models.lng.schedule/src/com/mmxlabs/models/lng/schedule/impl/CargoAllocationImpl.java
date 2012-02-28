/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotVisit;

import com.mmxlabs.models.lng.cargo.Cargo;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getLoadVisit <em>Load Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getDischargeVisit <em>Discharge Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getLoadVolume <em>Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getDischargeVolume <em>Discharge Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getInputCargo <em>Input Cargo</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoAllocationImpl extends MMXObjectImpl implements CargoAllocation {
	/**
	 * The cached value of the '{@link #getLoadVisit() <em>Load Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit loadVisit;

	/**
	 * The cached value of the '{@link #getDischargeVisit() <em>Discharge Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit dischargeVisit;

	/**
	 * The default value of the '{@link #getLoadVolume() <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int LOAD_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLoadVolume() <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected int loadVolume = LOAD_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int DISCHARGE_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected int dischargeVolume = DISCHARGE_VOLUME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInputCargo() <em>Input Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputCargo()
	 * @generated
	 * @ordered
	 */
	protected Cargo inputCargo;

	/**
	 * This is true if the Input Cargo reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean inputCargoESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CARGO_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit getLoadVisit() {
		if (loadVisit != null && loadVisit.eIsProxy()) {
			InternalEObject oldLoadVisit = (InternalEObject)loadVisit;
			loadVisit = (SlotVisit)eResolveProxy(oldLoadVisit);
			if (loadVisit != oldLoadVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__LOAD_VISIT, oldLoadVisit, loadVisit));
			}
		}
		return loadVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit basicGetLoadVisit() {
		return loadVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadVisit(SlotVisit newLoadVisit) {
		SlotVisit oldLoadVisit = loadVisit;
		loadVisit = newLoadVisit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LOAD_VISIT, oldLoadVisit, loadVisit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit getDischargeVisit() {
		if (dischargeVisit != null && dischargeVisit.eIsProxy()) {
			InternalEObject oldDischargeVisit = (InternalEObject)dischargeVisit;
			dischargeVisit = (SlotVisit)eResolveProxy(oldDischargeVisit);
			if (dischargeVisit != oldDischargeVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VISIT, oldDischargeVisit, dischargeVisit));
			}
		}
		return dischargeVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit basicGetDischargeVisit() {
		return dischargeVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeVisit(SlotVisit newDischargeVisit) {
		SlotVisit oldDischargeVisit = dischargeVisit;
		dischargeVisit = newDischargeVisit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VISIT, oldDischargeVisit, dischargeVisit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLoadVolume() {
		return loadVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadVolume(int newLoadVolume) {
		int oldLoadVolume = loadVolume;
		loadVolume = newLoadVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME, oldLoadVolume, loadVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDischargeVolume() {
		return dischargeVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeVolume(int newDischargeVolume) {
		int oldDischargeVolume = dischargeVolume;
		dischargeVolume = newDischargeVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME, oldDischargeVolume, dischargeVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo getInputCargo() {
		if (inputCargo != null && inputCargo.eIsProxy()) {
			InternalEObject oldInputCargo = (InternalEObject)inputCargo;
			inputCargo = (Cargo)eResolveProxy(oldInputCargo);
			if (inputCargo != oldInputCargo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, inputCargo));
			}
		}
		return inputCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo basicGetInputCargo() {
		return inputCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputCargo(Cargo newInputCargo) {
		Cargo oldInputCargo = inputCargo;
		inputCargo = newInputCargo;
		boolean oldInputCargoESet = inputCargoESet;
		inputCargoESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, inputCargo, !oldInputCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetInputCargo() {
		Cargo oldInputCargo = inputCargo;
		boolean oldInputCargoESet = inputCargoESet;
		inputCargo = null;
		inputCargoESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, null, oldInputCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetInputCargo() {
		return inputCargoESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VISIT:
				if (resolve) return getLoadVisit();
				return basicGetLoadVisit();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VISIT:
				if (resolve) return getDischargeVisit();
				return basicGetDischargeVisit();
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				return getLoadVolume();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				return getDischargeVolume();
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				if (resolve) return getInputCargo();
				return basicGetInputCargo();
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
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VISIT:
				setLoadVisit((SlotVisit)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VISIT:
				setDischargeVisit((SlotVisit)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				setLoadVolume((Integer)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				setDischargeVolume((Integer)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				setInputCargo((Cargo)newValue);
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
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VISIT:
				setLoadVisit((SlotVisit)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VISIT:
				setDischargeVisit((SlotVisit)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				setLoadVolume(LOAD_VOLUME_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				setDischargeVolume(DISCHARGE_VOLUME_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				unsetInputCargo();
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
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VISIT:
				return loadVisit != null;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VISIT:
				return dischargeVisit != null;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				return loadVolume != LOAD_VOLUME_EDEFAULT;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				return dischargeVolume != DISCHARGE_VOLUME_EDEFAULT;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				return isSetInputCargo();
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
		result.append(" (loadVolume: ");
		result.append(loadVolume);
		result.append(", dischargeVolume: ");
		result.append(dischargeVolume);
		result.append(')');
		return result.toString();
	}

} // end of CargoAllocationImpl

// finish type fixing
