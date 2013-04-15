/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
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
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getSlotVisit <em>Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getVolumeTransferred <em>Volume Transferred</em>}</li>
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
	protected SpotMarket spotMarket;

	/**
	 * This is true if the Spot Market reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean spotMarketESet;

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
	 * The cached value of the '{@link #getSlotVisit() <em>Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit slotVisit;

	/**
	 * This is true if the Slot Visit reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean slotVisitESet;

	/**
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected double price = PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeTransferred() <em>Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getVolumeTransferred()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_TRANSFERRED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeTransferred() <em>Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getVolumeTransferred()
	 * @generated
	 * @ordered
	 */
	protected int volumeTransferred = VOLUME_TRANSFERRED_EDEFAULT;

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
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket getSpotMarket() {
		if (spotMarket != null && spotMarket.eIsProxy()) {
			InternalEObject oldSpotMarket = (InternalEObject)spotMarket;
			spotMarket = (SpotMarket)eResolveProxy(oldSpotMarket);
			if (spotMarket != oldSpotMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET, oldSpotMarket, spotMarket));
			}
		}
		return spotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket basicGetSpotMarket() {
		return spotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotMarket(SpotMarket newSpotMarket) {
		SpotMarket oldSpotMarket = spotMarket;
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
		SpotMarket oldSpotMarket = spotMarket;
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
	public CargoAllocation getCargoAllocation() {
		if (cargoAllocation != null && cargoAllocation.eIsProxy()) {
			InternalEObject oldCargoAllocation = (InternalEObject)cargoAllocation;
			cargoAllocation = (CargoAllocation)eResolveProxy(oldCargoAllocation);
			if (cargoAllocation != oldCargoAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit getSlotVisit() {
		if (slotVisit != null && slotVisit.eIsProxy()) {
			InternalEObject oldSlotVisit = (InternalEObject)slotVisit;
			slotVisit = (SlotVisit)eResolveProxy(oldSlotVisit);
			if (slotVisit != oldSlotVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, oldSlotVisit, slotVisit));
			}
		}
		return slotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit basicGetSlotVisit() {
		return slotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSlotVisit(SlotVisit newSlotVisit, NotificationChain msgs) {
		SlotVisit oldSlotVisit = slotVisit;
		slotVisit = newSlotVisit;
		boolean oldSlotVisitESet = slotVisitESet;
		slotVisitESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, oldSlotVisit, newSlotVisit, !oldSlotVisitESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlotVisit(SlotVisit newSlotVisit) {
		if (newSlotVisit != slotVisit) {
			NotificationChain msgs = null;
			if (slotVisit != null)
				msgs = ((InternalEObject)slotVisit).eInverseRemove(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
			if (newSlotVisit != null)
				msgs = ((InternalEObject)newSlotVisit).eInverseAdd(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
			msgs = basicSetSlotVisit(newSlotVisit, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldSlotVisitESet = slotVisitESet;
			slotVisitESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, newSlotVisit, newSlotVisit, !oldSlotVisitESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetSlotVisit(NotificationChain msgs) {
		SlotVisit oldSlotVisit = slotVisit;
		slotVisit = null;
		boolean oldSlotVisitESet = slotVisitESet;
		slotVisitESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, oldSlotVisit, null, oldSlotVisitESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSlotVisit() {
		if (slotVisit != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject)slotVisit).eInverseRemove(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
			msgs = basicUnsetSlotVisit(msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldSlotVisitESet = slotVisitESet;
			slotVisitESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, null, null, oldSlotVisitESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSlotVisit() {
		return slotVisitESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrice(double newPrice) {
		double oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVolumeTransferred() {
		return volumeTransferred;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeTransferred(int newVolumeTransferred) {
		int oldVolumeTransferred = volumeTransferred;
		volumeTransferred = newVolumeTransferred;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED, oldVolumeTransferred, volumeTransferred));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Port getPort() {
		if (isSetSlot()) {
			return getSlot().getPort();
		} else if (isSetSlotVisit()) {
			return getSlotVisit().getPort();
		} else if (isSetSpotMarket()) {
			final SpotMarket market = getSpotMarket();
			if (market instanceof FOBSalesMarket) {
				return ((FOBSalesMarket) market).getLoadPort();
			} else if (market instanceof FOBPurchasesMarket) {
				return ((FOBPurchasesMarket) market).getNotionalPort();
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Calendar getLocalStart() {
		if (isSetSlotVisit()) {
			return getSlotVisit().getLocalStart();
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Calendar getLocalEnd() {
		if (isSetSlotVisit()) {
			return getSlotVisit().getLocalEnd();
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Contract getContract() {
		if (isSetSlot()) {
			return slot.getContract();
//		} else if (isSetSpotMarket()) {
//			return ((SpotMarket)getSpotMarket()).getContract();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getName() {
		if (isSetSlot()) {
			return getSlot().getName();
		} else if (isSetSpotMarket()) {
			return getSpotMarket().getName();
		} else {
			return "";
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				if (slotVisit != null)
					msgs = ((InternalEObject)slotVisit).eInverseRemove(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
				return basicSetSlotVisit((SlotVisit)otherEnd, msgs);
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				return basicUnsetSlotVisit(msgs);
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				if (resolve) return getSpotMarket();
				return basicGetSpotMarket();
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				if (resolve) return getCargoAllocation();
				return basicGetCargoAllocation();
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				if (resolve) return getSlotVisit();
				return basicGetSlotVisit();
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				return getPrice();
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				return getVolumeTransferred();
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
				setSpotMarket((SpotMarket)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				setSlotVisit((SlotVisit)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				setPrice((Double)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				setVolumeTransferred((Integer)newValue);
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
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)null);
				return;
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				unsetSlotVisit();
				return;
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				setVolumeTransferred(VOLUME_TRANSFERRED_EDEFAULT);
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
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				return cargoAllocation != null;
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				return isSetSlotVisit();
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				return price != PRICE_EDEFAULT;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				return volumeTransferred != VOLUME_TRANSFERRED_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.SLOT_ALLOCATION___GET_PORT:
				return getPort();
			case SchedulePackage.SLOT_ALLOCATION___GET_LOCAL_START:
				return getLocalStart();
			case SchedulePackage.SLOT_ALLOCATION___GET_LOCAL_END:
				return getLocalEnd();
			case SchedulePackage.SLOT_ALLOCATION___GET_CONTRACT:
				return getContract();
			case SchedulePackage.SLOT_ALLOCATION___GET_NAME:
				return getName();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (price: ");
		result.append(price);
		result.append(", volumeTransferred: ");
		result.append(volumeTransferred);
		result.append(')');
		return result.toString();
	}

} // end of SlotAllocationImpl

// finish type fixing
