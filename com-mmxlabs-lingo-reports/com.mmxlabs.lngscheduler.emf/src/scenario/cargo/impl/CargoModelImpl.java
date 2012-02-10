/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.cargo.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.cargo.Cargo;
import scenario.cargo.CargoModel;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Model</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.CargoModelImpl#getCargoes <em>Cargoes</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoModelImpl#getSpareDischargeSlots <em>Spare Discharge Slots</em>}</li>
 *   <li>{@link scenario.cargo.impl.CargoModelImpl#getSpareLoadSlots <em>Spare Load Slots</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoModelImpl extends EObjectImpl implements CargoModel {
	/**
	 * The cached value of the '{@link #getCargoes() <em>Cargoes</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getCargoes()
	 * @generated
	 * @ordered
	 */
	protected EList<Cargo> cargoes;

	/**
	 * The cached value of the '{@link #getSpareDischargeSlots() <em>Spare Discharge Slots</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSpareDischargeSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> spareDischargeSlots;

	/**
	 * The cached value of the '{@link #getSpareLoadSlots() <em>Spare Load Slots</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSpareLoadSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<LoadSlot> spareLoadSlots;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CARGO_MODEL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Cargo> getCargoes() {
		if (cargoes == null) {
			cargoes = new EObjectContainmentEList.Resolving<Cargo>(Cargo.class, this, CargoPackage.CARGO_MODEL__CARGOES);
		}
		return cargoes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Slot> getSpareDischargeSlots() {
		if (spareDischargeSlots == null) {
			spareDischargeSlots = new EObjectContainmentEList.Resolving<Slot>(Slot.class, this, CargoPackage.CARGO_MODEL__SPARE_DISCHARGE_SLOTS);
		}
		return spareDischargeSlots;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<LoadSlot> getSpareLoadSlots() {
		if (spareLoadSlots == null) {
			spareLoadSlots = new EObjectContainmentEList.Resolving<LoadSlot>(LoadSlot.class, this, CargoPackage.CARGO_MODEL__SPARE_LOAD_SLOTS);
		}
		return spareLoadSlots;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__CARGOES:
				return ((InternalEList<?>)getCargoes()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__SPARE_DISCHARGE_SLOTS:
				return ((InternalEList<?>)getSpareDischargeSlots()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__SPARE_LOAD_SLOTS:
				return ((InternalEList<?>)getSpareLoadSlots()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__CARGOES:
				return getCargoes();
			case CargoPackage.CARGO_MODEL__SPARE_DISCHARGE_SLOTS:
				return getSpareDischargeSlots();
			case CargoPackage.CARGO_MODEL__SPARE_LOAD_SLOTS:
				return getSpareLoadSlots();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__CARGOES:
				getCargoes().clear();
				getCargoes().addAll((Collection<? extends Cargo>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__SPARE_DISCHARGE_SLOTS:
				getSpareDischargeSlots().clear();
				getSpareDischargeSlots().addAll((Collection<? extends Slot>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__SPARE_LOAD_SLOTS:
				getSpareLoadSlots().clear();
				getSpareLoadSlots().addAll((Collection<? extends LoadSlot>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__CARGOES:
				getCargoes().clear();
				return;
			case CargoPackage.CARGO_MODEL__SPARE_DISCHARGE_SLOTS:
				getSpareDischargeSlots().clear();
				return;
			case CargoPackage.CARGO_MODEL__SPARE_LOAD_SLOTS:
				getSpareLoadSlots().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__CARGOES:
				return cargoes != null && !cargoes.isEmpty();
			case CargoPackage.CARGO_MODEL__SPARE_DISCHARGE_SLOTS:
				return spareDischargeSlots != null && !spareDischargeSlots.isEmpty();
			case CargoPackage.CARGO_MODEL__SPARE_LOAD_SLOTS:
				return spareLoadSlots != null && !spareLoadSlots.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // CargoModelImpl
