/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getLoadSlots <em>Load Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getDischargeSlots <em>Discharge Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCargoes <em>Cargoes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoModelImpl extends UUIDObjectImpl implements CargoModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLoadSlots() <em>Load Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<LoadSlot> loadSlots;

	/**
	 * The cached value of the '{@link #getDischargeSlots() <em>Discharge Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<DischargeSlot> dischargeSlots;

	/**
	 * The cached value of the '{@link #getCargoes() <em>Cargoes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoes()
	 * @generated
	 * @ordered
	 */
	protected EList<Cargo> cargoes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CARGO_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LoadSlot> getLoadSlots() {
		if (loadSlots == null) {
			loadSlots = new EObjectContainmentEList<LoadSlot>(LoadSlot.class, this, CargoPackage.CARGO_MODEL__LOAD_SLOTS);
		}
		return loadSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DischargeSlot> getDischargeSlots() {
		if (dischargeSlots == null) {
			dischargeSlots = new EObjectContainmentEList<DischargeSlot>(DischargeSlot.class, this, CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS);
		}
		return dischargeSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Cargo> getCargoes() {
		if (cargoes == null) {
			cargoes = new EObjectContainmentEList<Cargo>(Cargo.class, this, CargoPackage.CARGO_MODEL__CARGOES);
		}
		return cargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return ((InternalEList<?>)getLoadSlots()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return ((InternalEList<?>)getDischargeSlots()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__CARGOES:
				return ((InternalEList<?>)getCargoes()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.CARGO_MODEL__NAME:
				return getName();
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return getLoadSlots();
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return getDischargeSlots();
			case CargoPackage.CARGO_MODEL__CARGOES:
				return getCargoes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__NAME:
				setName((String)newValue);
				return;
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				getLoadSlots().clear();
				getLoadSlots().addAll((Collection<? extends LoadSlot>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				getDischargeSlots().clear();
				getDischargeSlots().addAll((Collection<? extends DischargeSlot>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__CARGOES:
				getCargoes().clear();
				getCargoes().addAll((Collection<? extends Cargo>)newValue);
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
			case CargoPackage.CARGO_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				getLoadSlots().clear();
				return;
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				getDischargeSlots().clear();
				return;
			case CargoPackage.CARGO_MODEL__CARGOES:
				getCargoes().clear();
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
			case CargoPackage.CARGO_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return loadSlots != null && !loadSlots.isEmpty();
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return dischargeSlots != null && !dischargeSlots.isEmpty();
			case CargoPackage.CARGO_MODEL__CARGOES:
				return cargoes != null && !cargoes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case CargoPackage.CARGO_MODEL__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return CargoPackage.CARGO_MODEL__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} // end of CargoModelImpl

// finish type fixing
