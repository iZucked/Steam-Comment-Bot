/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.cargo.Slot;
import scenario.schedule.CargoAllocation;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.events.impl.SlotVisitImpl#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.SlotVisitImpl#getSlot <em>Slot</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SlotVisitImpl extends PortVisitImpl implements SlotVisit {
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
	 * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot slot;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.SLOT_VISIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Slot)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.SLOT_VISIT__SLOT, oldSlot, slot));
			}
		}
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetSlot() {
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlot(Slot newSlot) {
		Slot oldSlot = slot;
		slot = newSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.SLOT_VISIT__SLOT, oldSlot, slot));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.SLOT_VISIT__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.SLOT_VISIT__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return getSlot().getId();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDisplayTypeName() {
		if (getSlot() instanceof scenario.cargo.LoadSlot) return "Load";
		else return "Discharge";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return getCargoAllocation().getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EventsPackage.SLOT_VISIT__CARGO_ALLOCATION:
				if (resolve) return getCargoAllocation();
				return basicGetCargoAllocation();
			case EventsPackage.SLOT_VISIT__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
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
			case EventsPackage.SLOT_VISIT__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)newValue);
				return;
			case EventsPackage.SLOT_VISIT__SLOT:
				setSlot((Slot)newValue);
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
			case EventsPackage.SLOT_VISIT__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)null);
				return;
			case EventsPackage.SLOT_VISIT__SLOT:
				setSlot((Slot)null);
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
			case EventsPackage.SLOT_VISIT__CARGO_ALLOCATION:
				return cargoAllocation != null;
			case EventsPackage.SLOT_VISIT__SLOT:
				return slot != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ScheduledEvent.class) {
			switch (baseOperationID) {
				case EventsPackage.SCHEDULED_EVENT___GET_NAME: return EventsPackage.SLOT_VISIT___GET_NAME;
				case EventsPackage.SCHEDULED_EVENT___GET_DISPLAY_TYPE_NAME: return EventsPackage.SLOT_VISIT___GET_DISPLAY_TYPE_NAME;
				default: return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		if (baseClass == PortVisit.class) {
			switch (baseOperationID) {
				case EventsPackage.PORT_VISIT___GET_ID: return EventsPackage.SLOT_VISIT___GET_ID;
				case EventsPackage.PORT_VISIT___GET_DISPLAY_TYPE_NAME: return EventsPackage.SLOT_VISIT___GET_DISPLAY_TYPE_NAME;
				default: return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EventsPackage.SLOT_VISIT___GET_ID:
				return getId();
			case EventsPackage.SLOT_VISIT___GET_DISPLAY_TYPE_NAME:
				return getDisplayTypeName();
			case EventsPackage.SLOT_VISIT___GET_NAME:
				return getName();
		}
		return super.eInvoke(operationID, arguments);
	}

} //SlotVisitImpl
