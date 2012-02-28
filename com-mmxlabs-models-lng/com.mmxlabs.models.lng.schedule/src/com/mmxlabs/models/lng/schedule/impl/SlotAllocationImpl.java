/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getSpotMarket <em>Spot Market</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SlotAllocationImpl extends MMXObjectImpl implements SlotAllocation {
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
	 * This is true if the Slot reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean slotESet;

	/**
	 * The cached value of the '{@link #getSpotMarket() <em>Spot Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected ASpotMarket spotMarket;

	/**
	 * This is true if the Spot Market reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean spotMarketESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.SLOT_ALLOCATION;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__SLOT, oldSlot, slot));
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
		boolean oldSlotESet = slotESet;
		slotESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SLOT, oldSlot, slot, !oldSlotESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSlot() {
		Slot oldSlot = slot;
		boolean oldSlotESet = slotESet;
		slot = null;
		slotESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SLOT, oldSlot, null, oldSlotESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSlot() {
		return slotESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ASpotMarket getSpotMarket() {
		if (spotMarket != null && spotMarket.eIsProxy()) {
			InternalEObject oldSpotMarket = (InternalEObject)spotMarket;
			spotMarket = (ASpotMarket)eResolveProxy(oldSpotMarket);
			if (spotMarket != oldSpotMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET, oldSpotMarket, spotMarket));
			}
		}
		return spotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ASpotMarket basicGetSpotMarket() {
		return spotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotMarket(ASpotMarket newSpotMarket) {
		ASpotMarket oldSpotMarket = spotMarket;
		spotMarket = newSpotMarket;
		boolean oldSpotMarketESet = spotMarketESet;
		spotMarketESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET, oldSpotMarket, spotMarket, !oldSpotMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSpotMarket() {
		ASpotMarket oldSpotMarket = spotMarket;
		boolean oldSpotMarketESet = spotMarketESet;
		spotMarket = null;
		spotMarketESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET, oldSpotMarket, null, oldSpotMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSpotMarket() {
		return spotMarketESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				if (resolve) return getSpotMarket();
				return basicGetSpotMarket();
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				setSlot((Slot)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				setSpotMarket((ASpotMarket)newValue);
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				unsetSlot();
				return;
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				unsetSpotMarket();
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				return isSetSlot();
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				return isSetSpotMarket();
		}
		return super.eIsSet(featureID);
	}

} // end of SlotAllocationImpl

// finish type fixing
