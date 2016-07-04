/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Market Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl#getMarket <em>Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl#getSlotAllocation <em>Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl#getSlotVisit <em>Slot Visit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketAllocationImpl extends ProfitAndLossContainerImpl implements MarketAllocation {
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
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarket market;

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
	 * The cached value of the '{@link #getSlotVisit() <em>Slot Visit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit slotVisit;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.MARKET_ALLOCATION;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.MARKET_ALLOCATION__SLOT, oldSlot, slot));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.MARKET_ALLOCATION__SLOT, oldSlot, slot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (SpotMarket)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.MARKET_ALLOCATION__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarket(SpotMarket newMarket) {
		SpotMarket oldMarket = market;
		market = newMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.MARKET_ALLOCATION__MARKET, oldMarket, market));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getSlotAllocation() {
		if (slotAllocation != null && slotAllocation.eIsProxy()) {
			InternalEObject oldSlotAllocation = (InternalEObject)slotAllocation;
			slotAllocation = (SlotAllocation)eResolveProxy(oldSlotAllocation);
			if (slotAllocation != oldSlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION, oldSlotAllocation, slotAllocation));
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
	public NotificationChain basicSetSlotAllocation(SlotAllocation newSlotAllocation, NotificationChain msgs) {
		SlotAllocation oldSlotAllocation = slotAllocation;
		slotAllocation = newSlotAllocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION, oldSlotAllocation, newSlotAllocation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlotAllocation(SlotAllocation newSlotAllocation) {
		if (newSlotAllocation != slotAllocation) {
			NotificationChain msgs = null;
			if (slotAllocation != null)
				msgs = ((InternalEObject)slotAllocation).eInverseRemove(this, SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION, SlotAllocation.class, msgs);
			if (newSlotAllocation != null)
				msgs = ((InternalEObject)newSlotAllocation).eInverseAdd(this, SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION, SlotAllocation.class, msgs);
			msgs = basicSetSlotAllocation(newSlotAllocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION, newSlotAllocation, newSlotAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.MARKET_ALLOCATION__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit getSlotVisit() {
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
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT, oldSlotVisit, newSlotVisit);
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
				msgs = ((InternalEObject)slotVisit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT, null, msgs);
			if (newSlotVisit != null)
				msgs = ((InternalEObject)newSlotVisit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT, null, msgs);
			msgs = basicSetSlotVisit(newSlotVisit, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT, newSlotVisit, newSlotVisit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION:
				if (slotAllocation != null)
					msgs = ((InternalEObject)slotAllocation).eInverseRemove(this, SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION, SlotAllocation.class, msgs);
				return basicSetSlotAllocation((SlotAllocation)otherEnd, msgs);
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
			case SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION:
				return basicSetSlotAllocation(null, msgs);
			case SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT:
				return basicSetSlotVisit(null, msgs);
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
			case SchedulePackage.MARKET_ALLOCATION__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case SchedulePackage.MARKET_ALLOCATION__MARKET:
				if (resolve) return getMarket();
				return basicGetMarket();
			case SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION:
				if (resolve) return getSlotAllocation();
				return basicGetSlotAllocation();
			case SchedulePackage.MARKET_ALLOCATION__PRICE:
				return getPrice();
			case SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT:
				return getSlotVisit();
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
			case SchedulePackage.MARKET_ALLOCATION__SLOT:
				setSlot((Slot)newValue);
				return;
			case SchedulePackage.MARKET_ALLOCATION__MARKET:
				setMarket((SpotMarket)newValue);
				return;
			case SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION:
				setSlotAllocation((SlotAllocation)newValue);
				return;
			case SchedulePackage.MARKET_ALLOCATION__PRICE:
				setPrice((Double)newValue);
				return;
			case SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT:
				setSlotVisit((SlotVisit)newValue);
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
			case SchedulePackage.MARKET_ALLOCATION__SLOT:
				setSlot((Slot)null);
				return;
			case SchedulePackage.MARKET_ALLOCATION__MARKET:
				setMarket((SpotMarket)null);
				return;
			case SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION:
				setSlotAllocation((SlotAllocation)null);
				return;
			case SchedulePackage.MARKET_ALLOCATION__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT:
				setSlotVisit((SlotVisit)null);
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
			case SchedulePackage.MARKET_ALLOCATION__SLOT:
				return slot != null;
			case SchedulePackage.MARKET_ALLOCATION__MARKET:
				return market != null;
			case SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION:
				return slotAllocation != null;
			case SchedulePackage.MARKET_ALLOCATION__PRICE:
				return price != PRICE_EDEFAULT;
			case SchedulePackage.MARKET_ALLOCATION__SLOT_VISIT:
				return slotVisit != null;
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
		result.append(" (price: ");
		result.append(price);
		result.append(')');
		return result.toString();
	}

} //MarketAllocationImpl
