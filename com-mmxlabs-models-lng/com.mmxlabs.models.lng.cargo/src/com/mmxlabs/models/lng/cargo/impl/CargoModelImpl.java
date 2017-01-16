/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getLoadSlots <em>Load Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getDischargeSlots <em>Discharge Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCargoes <em>Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCargoGroups <em>Cargo Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getVesselAvailabilities <em>Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getVesselTypeGroups <em>Vessel Type Groups</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoModelImpl extends UUIDObjectImpl implements CargoModel {
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
	 * The cached value of the '{@link #getCargoGroups() <em>Cargo Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<CargoGroup> cargoGroups;

	/**
	 * The cached value of the '{@link #getVesselAvailabilities() <em>Vessel Availabilities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAvailabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselAvailability> vesselAvailabilities;

	/**
	 * The cached value of the '{@link #getVesselEvents() <em>Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> vesselEvents;

	/**
	 * The cached value of the '{@link #getVesselTypeGroups() <em>Vessel Type Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselTypeGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselTypeGroup> vesselTypeGroups;

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
	public EList<LoadSlot> getLoadSlots() {
		if (loadSlots == null) {
			loadSlots = new EObjectContainmentEList.Resolving<LoadSlot>(LoadSlot.class, this, CargoPackage.CARGO_MODEL__LOAD_SLOTS);
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
			dischargeSlots = new EObjectContainmentEList.Resolving<DischargeSlot>(DischargeSlot.class, this, CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS);
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
			cargoes = new EObjectContainmentEList.Resolving<Cargo>(Cargo.class, this, CargoPackage.CARGO_MODEL__CARGOES);
		}
		return cargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CargoGroup> getCargoGroups() {
		if (cargoGroups == null) {
			cargoGroups = new EObjectContainmentEList.Resolving<CargoGroup>(CargoGroup.class, this, CargoPackage.CARGO_MODEL__CARGO_GROUPS);
		}
		return cargoGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselAvailability> getVesselAvailabilities() {
		if (vesselAvailabilities == null) {
			vesselAvailabilities = new EObjectContainmentEList.Resolving<VesselAvailability>(VesselAvailability.class, this, CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES);
		}
		return vesselAvailabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselEvent> getVesselEvents() {
		if (vesselEvents == null) {
			vesselEvents = new EObjectContainmentEList.Resolving<VesselEvent>(VesselEvent.class, this, CargoPackage.CARGO_MODEL__VESSEL_EVENTS);
		}
		return vesselEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselTypeGroup> getVesselTypeGroups() {
		if (vesselTypeGroups == null) {
			vesselTypeGroups = new EObjectContainmentEList.Resolving<VesselTypeGroup>(VesselTypeGroup.class, this, CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS);
		}
		return vesselTypeGroups;
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
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				return ((InternalEList<?>)getCargoGroups()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				return ((InternalEList<?>)getVesselAvailabilities()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				return ((InternalEList<?>)getVesselEvents()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				return ((InternalEList<?>)getVesselTypeGroups()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return getLoadSlots();
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return getDischargeSlots();
			case CargoPackage.CARGO_MODEL__CARGOES:
				return getCargoes();
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				return getCargoGroups();
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				return getVesselAvailabilities();
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				return getVesselEvents();
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				return getVesselTypeGroups();
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
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				getCargoGroups().clear();
				getCargoGroups().addAll((Collection<? extends CargoGroup>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				getVesselAvailabilities().addAll((Collection<? extends VesselAvailability>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				getVesselEvents().addAll((Collection<? extends VesselEvent>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				getVesselTypeGroups().clear();
				getVesselTypeGroups().addAll((Collection<? extends VesselTypeGroup>)newValue);
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
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				getLoadSlots().clear();
				return;
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				getDischargeSlots().clear();
				return;
			case CargoPackage.CARGO_MODEL__CARGOES:
				getCargoes().clear();
				return;
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				getCargoGroups().clear();
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				getVesselTypeGroups().clear();
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
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return loadSlots != null && !loadSlots.isEmpty();
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return dischargeSlots != null && !dischargeSlots.isEmpty();
			case CargoPackage.CARGO_MODEL__CARGOES:
				return cargoes != null && !cargoes.isEmpty();
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				return cargoGroups != null && !cargoGroups.isEmpty();
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				return vesselAvailabilities != null && !vesselAvailabilities.isEmpty();
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				return vesselEvents != null && !vesselEvents.isEmpty();
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				return vesselTypeGroups != null && !vesselTypeGroups.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of CargoModelImpl

// finish type fixing
