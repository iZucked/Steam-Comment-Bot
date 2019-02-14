/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ResultContainer;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Result Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl#getOpenSlotAllocations <em>Open Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl#getSlotAllocations <em>Slot Allocations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResultContainerImpl extends EObjectImpl implements ResultContainer {
	/**
	 * The cached value of the '{@link #getCargoAllocation() <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoAllocation()
	 * @generated
	 * @ordered
	 */
	protected CargoAllocation cargoAllocation;

	/**
	 * The cached value of the '{@link #getOpenSlotAllocations() <em>Open Slot Allocations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<OpenSlotAllocation> openSlotAllocations;

	/**
	 * The cached value of the '{@link #getSlotAllocations() <em>Slot Allocations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotAllocation> slotAllocations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResultContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.RESULT_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation getCargoAllocation() {
		if (cargoAllocation != null && cargoAllocation.eIsProxy()) {
			InternalEObject oldCargoAllocation = (InternalEObject)cargoAllocation;
			cargoAllocation = (CargoAllocation)eResolveProxy(oldCargoAllocation);
			if (cargoAllocation != oldCargoAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
			}
		}
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation basicGetCargoAllocation() {
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoAllocation(CargoAllocation newCargoAllocation) {
		CargoAllocation oldCargoAllocation = cargoAllocation;
		cargoAllocation = newCargoAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OpenSlotAllocation> getOpenSlotAllocations() {
		if (openSlotAllocations == null) {
			openSlotAllocations = new EObjectResolvingEList<OpenSlotAllocation>(OpenSlotAllocation.class, this, AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS);
		}
		return openSlotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SlotAllocation> getSlotAllocations() {
		if (slotAllocations == null) {
			slotAllocations = new EObjectResolvingEList<SlotAllocation>(SlotAllocation.class, this, AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS);
		}
		return slotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				if (resolve) return getCargoAllocation();
				return basicGetCargoAllocation();
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				return getOpenSlotAllocations();
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				return getSlotAllocations();
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
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)newValue);
				return;
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				getOpenSlotAllocations().clear();
				getOpenSlotAllocations().addAll((Collection<? extends OpenSlotAllocation>)newValue);
				return;
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				getSlotAllocations().addAll((Collection<? extends SlotAllocation>)newValue);
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
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)null);
				return;
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				getOpenSlotAllocations().clear();
				return;
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
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
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				return cargoAllocation != null;
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				return openSlotAllocations != null && !openSlotAllocations.isEmpty();
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				return slotAllocations != null && !slotAllocations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ResultContainerImpl
