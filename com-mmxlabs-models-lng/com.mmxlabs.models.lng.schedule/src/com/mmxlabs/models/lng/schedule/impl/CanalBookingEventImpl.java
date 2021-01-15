/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.CanalBookingEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal Booking Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CanalBookingEventImpl#getLinkedSequence <em>Linked Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CanalBookingEventImpl#getLinkedJourney <em>Linked Journey</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CanalBookingEventImpl extends EventImpl implements CanalBookingEvent {
	/**
	 * The cached value of the '{@link #getLinkedSequence() <em>Linked Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedSequence()
	 * @generated
	 * @ordered
	 */
	protected Sequence linkedSequence;

	/**
	 * The cached value of the '{@link #getLinkedJourney() <em>Linked Journey</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedJourney()
	 * @generated
	 * @ordered
	 */
	protected Journey linkedJourney;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalBookingEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CANAL_BOOKING_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Sequence getLinkedSequence() {
		if (linkedSequence != null && linkedSequence.eIsProxy()) {
			InternalEObject oldLinkedSequence = (InternalEObject)linkedSequence;
			linkedSequence = (Sequence)eResolveProxy(oldLinkedSequence);
			if (linkedSequence != oldLinkedSequence) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CANAL_BOOKING_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
			}
		}
		return linkedSequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence basicGetLinkedSequence() {
		return linkedSequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLinkedSequence(Sequence newLinkedSequence) {
		Sequence oldLinkedSequence = linkedSequence;
		linkedSequence = newLinkedSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CANAL_BOOKING_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Journey getLinkedJourney() {
		if (linkedJourney != null && linkedJourney.eIsProxy()) {
			InternalEObject oldLinkedJourney = (InternalEObject)linkedJourney;
			linkedJourney = (Journey)eResolveProxy(oldLinkedJourney);
			if (linkedJourney != oldLinkedJourney) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CANAL_BOOKING_EVENT__LINKED_JOURNEY, oldLinkedJourney, linkedJourney));
			}
		}
		return linkedJourney;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey basicGetLinkedJourney() {
		return linkedJourney;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLinkedJourney(Journey newLinkedJourney) {
		Journey oldLinkedJourney = linkedJourney;
		linkedJourney = newLinkedJourney;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CANAL_BOOKING_EVENT__LINKED_JOURNEY, oldLinkedJourney, linkedJourney));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_SEQUENCE:
				if (resolve) return getLinkedSequence();
				return basicGetLinkedSequence();
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_JOURNEY:
				if (resolve) return getLinkedJourney();
				return basicGetLinkedJourney();
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
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)newValue);
				return;
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_JOURNEY:
				setLinkedJourney((Journey)newValue);
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
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)null);
				return;
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_JOURNEY:
				setLinkedJourney((Journey)null);
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
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_SEQUENCE:
				return linkedSequence != null;
			case SchedulePackage.CANAL_BOOKING_EVENT__LINKED_JOURNEY:
				return linkedJourney != null;
		}
		return super.eIsSet(featureID);
	}

} //CanalBookingEventImpl
