/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Discharge Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl#getCargo <em>Cargo</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DischargeSlotImpl extends SlotImpl implements DischargeSlot {
	/**
	 * The cached value of the '{@link #getCargo() <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargo()
	 * @generated
	 * @ordered
	 */
	protected Cargo cargo;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DischargeSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.DISCHARGE_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo getCargo() {
		if (cargo != null && cargo.eIsProxy()) {
			InternalEObject oldCargo = (InternalEObject)cargo;
			cargo = (Cargo)eResolveProxy(oldCargo);
			if (cargo != oldCargo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.DISCHARGE_SLOT__CARGO, oldCargo, cargo));
			}
		}
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo basicGetCargo() {
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCargo(Cargo newCargo, NotificationChain msgs) {
		Cargo oldCargo = cargo;
		cargo = newCargo;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.DISCHARGE_SLOT__CARGO, oldCargo, newCargo);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargo(Cargo newCargo) {
		if (newCargo != cargo) {
			NotificationChain msgs = null;
			if (cargo != null)
				msgs = ((InternalEObject)cargo).eInverseRemove(this, CargoPackage.CARGO__DISCHARGE_SLOT, Cargo.class, msgs);
			if (newCargo != null)
				msgs = ((InternalEObject)newCargo).eInverseAdd(this, CargoPackage.CARGO__DISCHARGE_SLOT, Cargo.class, msgs);
			msgs = basicSetCargo(newCargo, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.DISCHARGE_SLOT__CARGO, newCargo, newCargo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.DISCHARGE_SLOT__CARGO:
				if (cargo != null)
					msgs = ((InternalEObject)cargo).eInverseRemove(this, CargoPackage.CARGO__DISCHARGE_SLOT, Cargo.class, msgs);
				return basicSetCargo((Cargo)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.DISCHARGE_SLOT__CARGO:
				return basicSetCargo(null, msgs);
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
			case CargoPackage.DISCHARGE_SLOT__CARGO:
				if (resolve) return getCargo();
				return basicGetCargo();
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
			case CargoPackage.DISCHARGE_SLOT__CARGO:
				setCargo((Cargo)newValue);
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
			case CargoPackage.DISCHARGE_SLOT__CARGO:
				setCargo((Cargo)null);
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
			case CargoPackage.DISCHARGE_SLOT__CARGO:
				return cargo != null;
		}
		return super.eIsSet(featureID);
	}

	@Override
	public Object getUnsetValue(EStructuralFeature feature) {
		if (feature == CargoPackage.Literals.SLOT__DURATION) {
			if (getPort() == null) return 12;
			return getPort().getDischargeDuration();
		}
		return super.getUnsetValue(feature);
	}
	
} // end of DischargeSlotImpl

// finish type fixing
