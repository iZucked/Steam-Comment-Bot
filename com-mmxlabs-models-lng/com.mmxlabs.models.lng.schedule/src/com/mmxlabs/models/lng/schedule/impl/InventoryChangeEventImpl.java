/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.cargo.InventoryEventRow;

import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

import java.time.LocalDateTime;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory Change Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getChangeQuantity <em>Change Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getCurrentLevel <em>Current Level</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getCurrentMin <em>Current Min</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getCurrentMax <em>Current Max</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getEvent <em>Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getSlotAllocation <em>Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#getOpenSlotAllocation <em>Open Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#isBreachedMin <em>Breached Min</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.InventoryChangeEventImpl#isBreachedMax <em>Breached Max</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryChangeEventImpl extends EObjectImpl implements InventoryChangeEvent {
	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getChangeQuantity() <em>Change Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final int CHANGE_QUANTITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getChangeQuantity() <em>Change Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeQuantity()
	 * @generated
	 * @ordered
	 */
	protected int changeQuantity = CHANGE_QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getCurrentLevel() <em>Current Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentLevel()
	 * @generated
	 * @ordered
	 */
	protected static final int CURRENT_LEVEL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCurrentLevel() <em>Current Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentLevel()
	 * @generated
	 * @ordered
	 */
	protected int currentLevel = CURRENT_LEVEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getCurrentMin() <em>Current Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentMin()
	 * @generated
	 * @ordered
	 */
	protected static final int CURRENT_MIN_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCurrentMin() <em>Current Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentMin()
	 * @generated
	 * @ordered
	 */
	protected int currentMin = CURRENT_MIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getCurrentMax() <em>Current Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentMax()
	 * @generated
	 * @ordered
	 */
	protected static final int CURRENT_MAX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCurrentMax() <em>Current Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentMax()
	 * @generated
	 * @ordered
	 */
	protected int currentMax = CURRENT_MAX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEvent() <em>Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvent()
	 * @generated
	 * @ordered
	 */
	protected InventoryEventRow event;

	/**
	 * The cached value of the '{@link #getSlotAllocation() <em>Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation slotAllocation;

	/**
	 * The cached value of the '{@link #getOpenSlotAllocation() <em>Open Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenSlotAllocation()
	 * @generated
	 * @ordered
	 */
	protected OpenSlotAllocation openSlotAllocation;

	/**
	 * The default value of the '{@link #isBreachedMin() <em>Breached Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBreachedMin()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BREACHED_MIN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBreachedMin() <em>Breached Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBreachedMin()
	 * @generated
	 * @ordered
	 */
	protected boolean breachedMin = BREACHED_MIN_EDEFAULT;

	/**
	 * The default value of the '{@link #isBreachedMax() <em>Breached Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBreachedMax()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BREACHED_MAX_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBreachedMax() <em>Breached Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBreachedMax()
	 * @generated
	 * @ordered
	 */
	protected boolean breachedMax = BREACHED_MAX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryChangeEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.INVENTORY_CHANGE_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDate(LocalDateTime newDate) {
		LocalDateTime oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getChangeQuantity() {
		return changeQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setChangeQuantity(int newChangeQuantity) {
		int oldChangeQuantity = changeQuantity;
		changeQuantity = newChangeQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY, oldChangeQuantity, changeQuantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentLevel(int newCurrentLevel) {
		int oldCurrentLevel = currentLevel;
		currentLevel = newCurrentLevel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL, oldCurrentLevel, currentLevel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCurrentMin() {
		return currentMin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentMin(int newCurrentMin) {
		int oldCurrentMin = currentMin;
		currentMin = newCurrentMin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MIN, oldCurrentMin, currentMin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCurrentMax() {
		return currentMax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentMax(int newCurrentMax) {
		int oldCurrentMax = currentMax;
		currentMax = newCurrentMax;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MAX, oldCurrentMax, currentMax));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InventoryEventRow getEvent() {
		if (event != null && event.eIsProxy()) {
			InternalEObject oldEvent = (InternalEObject)event;
			event = (InventoryEventRow)eResolveProxy(oldEvent);
			if (event != oldEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.INVENTORY_CHANGE_EVENT__EVENT, oldEvent, event));
			}
		}
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InventoryEventRow basicGetEvent() {
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEvent(InventoryEventRow newEvent) {
		InventoryEventRow oldEvent = event;
		event = newEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__EVENT, oldEvent, event));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getSlotAllocation() {
		if (slotAllocation != null && slotAllocation.eIsProxy()) {
			InternalEObject oldSlotAllocation = (InternalEObject)slotAllocation;
			slotAllocation = (SlotAllocation)eResolveProxy(oldSlotAllocation);
			if (slotAllocation != oldSlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION, oldSlotAllocation, slotAllocation));
			}
		}
		return slotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetSlotAllocation() {
		return slotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSlotAllocation(SlotAllocation newSlotAllocation) {
		SlotAllocation oldSlotAllocation = slotAllocation;
		slotAllocation = newSlotAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION, oldSlotAllocation, slotAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenSlotAllocation getOpenSlotAllocation() {
		if (openSlotAllocation != null && openSlotAllocation.eIsProxy()) {
			InternalEObject oldOpenSlotAllocation = (InternalEObject)openSlotAllocation;
			openSlotAllocation = (OpenSlotAllocation)eResolveProxy(oldOpenSlotAllocation);
			if (openSlotAllocation != oldOpenSlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION, oldOpenSlotAllocation, openSlotAllocation));
			}
		}
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OpenSlotAllocation basicGetOpenSlotAllocation() {
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOpenSlotAllocation(OpenSlotAllocation newOpenSlotAllocation) {
		OpenSlotAllocation oldOpenSlotAllocation = openSlotAllocation;
		openSlotAllocation = newOpenSlotAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION, oldOpenSlotAllocation, openSlotAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isBreachedMin() {
		return breachedMin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBreachedMin(boolean newBreachedMin) {
		boolean oldBreachedMin = breachedMin;
		breachedMin = newBreachedMin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MIN, oldBreachedMin, breachedMin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isBreachedMax() {
		return breachedMax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBreachedMax(boolean newBreachedMax) {
		boolean oldBreachedMax = breachedMax;
		breachedMax = newBreachedMax;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MAX, oldBreachedMax, breachedMax));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.INVENTORY_CHANGE_EVENT__DATE:
				return getDate();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY:
				return getChangeQuantity();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL:
				return getCurrentLevel();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MIN:
				return getCurrentMin();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MAX:
				return getCurrentMax();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__EVENT:
				if (resolve) return getEvent();
				return basicGetEvent();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION:
				if (resolve) return getSlotAllocation();
				return basicGetSlotAllocation();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION:
				if (resolve) return getOpenSlotAllocation();
				return basicGetOpenSlotAllocation();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MIN:
				return isBreachedMin();
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MAX:
				return isBreachedMax();
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
			case SchedulePackage.INVENTORY_CHANGE_EVENT__DATE:
				setDate((LocalDateTime)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY:
				setChangeQuantity((Integer)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL:
				setCurrentLevel((Integer)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MIN:
				setCurrentMin((Integer)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MAX:
				setCurrentMax((Integer)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__EVENT:
				setEvent((InventoryEventRow)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION:
				setSlotAllocation((SlotAllocation)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION:
				setOpenSlotAllocation((OpenSlotAllocation)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MIN:
				setBreachedMin((Boolean)newValue);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MAX:
				setBreachedMax((Boolean)newValue);
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
			case SchedulePackage.INVENTORY_CHANGE_EVENT__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY:
				setChangeQuantity(CHANGE_QUANTITY_EDEFAULT);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL:
				setCurrentLevel(CURRENT_LEVEL_EDEFAULT);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MIN:
				setCurrentMin(CURRENT_MIN_EDEFAULT);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MAX:
				setCurrentMax(CURRENT_MAX_EDEFAULT);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__EVENT:
				setEvent((InventoryEventRow)null);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION:
				setSlotAllocation((SlotAllocation)null);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION:
				setOpenSlotAllocation((OpenSlotAllocation)null);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MIN:
				setBreachedMin(BREACHED_MIN_EDEFAULT);
				return;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MAX:
				setBreachedMax(BREACHED_MAX_EDEFAULT);
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
			case SchedulePackage.INVENTORY_CHANGE_EVENT__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY:
				return changeQuantity != CHANGE_QUANTITY_EDEFAULT;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL:
				return currentLevel != CURRENT_LEVEL_EDEFAULT;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MIN:
				return currentMin != CURRENT_MIN_EDEFAULT;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__CURRENT_MAX:
				return currentMax != CURRENT_MAX_EDEFAULT;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__EVENT:
				return event != null;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION:
				return slotAllocation != null;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION:
				return openSlotAllocation != null;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MIN:
				return breachedMin != BREACHED_MIN_EDEFAULT;
			case SchedulePackage.INVENTORY_CHANGE_EVENT__BREACHED_MAX:
				return breachedMax != BREACHED_MAX_EDEFAULT;
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
		result.append(" (date: ");
		result.append(date);
		result.append(", changeQuantity: ");
		result.append(changeQuantity);
		result.append(", currentLevel: ");
		result.append(currentLevel);
		result.append(", currentMin: ");
		result.append(currentMin);
		result.append(", currentMax: ");
		result.append(currentMax);
		result.append(", breachedMin: ");
		result.append(breachedMin);
		result.append(", breachedMax: ");
		result.append(breachedMax);
		result.append(')');
		return result.toString();
	}

} //InventoryChangeEventImpl
