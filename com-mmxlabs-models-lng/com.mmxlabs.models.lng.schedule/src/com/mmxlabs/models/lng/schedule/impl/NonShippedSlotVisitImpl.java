/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Non Shipped Slot Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NonShippedSlotVisitImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NonShippedSlotVisitImpl#getLateness <em>Lateness</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NonShippedSlotVisitImpl extends EventImpl implements NonShippedSlotVisit {
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
	 * The cached value of the '{@link #getLateness() <em>Lateness</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLateness()
	 * @generated
	 * @ordered
	 */
	protected PortVisitLateness lateness;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NonShippedSlotVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.NON_SHIPPED_SLOT_VISIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Slot getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Slot)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.NON_SHIPPED_SLOT_VISIT__SLOT, oldSlot, slot));
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
	@Override
	public void setSlot(Slot newSlot) {
		Slot oldSlot = slot;
		slot = newSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NON_SHIPPED_SLOT_VISIT__SLOT, oldSlot, slot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortVisitLateness getLateness() {
		return lateness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLateness(PortVisitLateness newLateness, NotificationChain msgs) {
		PortVisitLateness oldLateness = lateness;
		lateness = newLateness;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS, oldLateness, newLateness);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLateness(PortVisitLateness newLateness) {
		if (newLateness != lateness) {
			NotificationChain msgs = null;
			if (lateness != null)
				msgs = ((InternalEObject)lateness).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS, null, msgs);
			if (newLateness != null)
				msgs = ((InternalEObject)newLateness).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS, null, msgs);
			msgs = basicSetLateness(newLateness, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS, newLateness, newLateness));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS:
				return basicSetLateness(null, msgs);
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
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS:
				return getLateness();
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
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__SLOT:
				setSlot((Slot)newValue);
				return;
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS:
				setLateness((PortVisitLateness)newValue);
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
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__SLOT:
				setSlot((Slot)null);
				return;
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS:
				setLateness((PortVisitLateness)null);
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
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__SLOT:
				return slot != null;
			case SchedulePackage.NON_SHIPPED_SLOT_VISIT__LATENESS:
				return lateness != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String name() {
		final Slot<?> thisSlot = getSlot();
		return thisSlot != null ? thisSlot.getName() : super.name();
	}
} //NonShippedSlotVisitImpl
