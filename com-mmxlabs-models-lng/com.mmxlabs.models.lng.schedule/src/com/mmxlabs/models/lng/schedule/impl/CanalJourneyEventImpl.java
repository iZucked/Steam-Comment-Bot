/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal Journey Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CanalJourneyEventImpl#getLinkedSequence <em>Linked Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CanalJourneyEventImpl#getLinkedJourney <em>Linked Journey</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CanalJourneyEventImpl#getPanamaWaitingTimeHours <em>Panama Waiting Time Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CanalJourneyEventImpl#getMaxAvailablePanamaWaitingTimeHours <em>Max Available Panama Waiting Time Hours</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CanalJourneyEventImpl extends EventImpl implements CanalJourneyEvent {
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
	 * The default value of the '{@link #getPanamaWaitingTimeHours() <em>Panama Waiting Time Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPanamaWaitingTimeHours()
	 * @generated
	 * @ordered
	 */
	protected static final int PANAMA_WAITING_TIME_HOURS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPanamaWaitingTimeHours() <em>Panama Waiting Time Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPanamaWaitingTimeHours()
	 * @generated
	 * @ordered
	 */
	protected int panamaWaitingTimeHours = PANAMA_WAITING_TIME_HOURS_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxAvailablePanamaWaitingTimeHours() <em>Max Available Panama Waiting Time Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxAvailablePanamaWaitingTimeHours()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxAvailablePanamaWaitingTimeHours() <em>Max Available Panama Waiting Time Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxAvailablePanamaWaitingTimeHours()
	 * @generated
	 * @ordered
	 */
	protected int maxAvailablePanamaWaitingTimeHours = MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalJourneyEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CANAL_JOURNEY_EVENT;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_JOURNEY, oldLinkedJourney, linkedJourney));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_JOURNEY, oldLinkedJourney, linkedJourney));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPanamaWaitingTimeHours() {
		return panamaWaitingTimeHours;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPanamaWaitingTimeHours(int newPanamaWaitingTimeHours) {
		int oldPanamaWaitingTimeHours = panamaWaitingTimeHours;
		panamaWaitingTimeHours = newPanamaWaitingTimeHours;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS, oldPanamaWaitingTimeHours, panamaWaitingTimeHours));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxAvailablePanamaWaitingTimeHours() {
		return maxAvailablePanamaWaitingTimeHours;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxAvailablePanamaWaitingTimeHours(int newMaxAvailablePanamaWaitingTimeHours) {
		int oldMaxAvailablePanamaWaitingTimeHours = maxAvailablePanamaWaitingTimeHours;
		maxAvailablePanamaWaitingTimeHours = newMaxAvailablePanamaWaitingTimeHours;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS, oldMaxAvailablePanamaWaitingTimeHours, maxAvailablePanamaWaitingTimeHours));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE:
				if (resolve) return getLinkedSequence();
				return basicGetLinkedSequence();
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_JOURNEY:
				if (resolve) return getLinkedJourney();
				return basicGetLinkedJourney();
			case SchedulePackage.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS:
				return getPanamaWaitingTimeHours();
			case SchedulePackage.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS:
				return getMaxAvailablePanamaWaitingTimeHours();
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
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)newValue);
				return;
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_JOURNEY:
				setLinkedJourney((Journey)newValue);
				return;
			case SchedulePackage.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS:
				setPanamaWaitingTimeHours((Integer)newValue);
				return;
			case SchedulePackage.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS:
				setMaxAvailablePanamaWaitingTimeHours((Integer)newValue);
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
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)null);
				return;
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_JOURNEY:
				setLinkedJourney((Journey)null);
				return;
			case SchedulePackage.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS:
				setPanamaWaitingTimeHours(PANAMA_WAITING_TIME_HOURS_EDEFAULT);
				return;
			case SchedulePackage.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS:
				setMaxAvailablePanamaWaitingTimeHours(MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS_EDEFAULT);
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
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE:
				return linkedSequence != null;
			case SchedulePackage.CANAL_JOURNEY_EVENT__LINKED_JOURNEY:
				return linkedJourney != null;
			case SchedulePackage.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS:
				return panamaWaitingTimeHours != PANAMA_WAITING_TIME_HOURS_EDEFAULT;
			case SchedulePackage.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS:
				return maxAvailablePanamaWaitingTimeHours != MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS_EDEFAULT;
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
		result.append(" (panamaWaitingTimeHours: ");
		result.append(panamaWaitingTimeHours);
		result.append(", maxAvailablePanamaWaitingTimeHours: ");
		result.append(maxAvailablePanamaWaitingTimeHours);
		result.append(')');
		return result.toString();
	}

} //CanalJourneyEventImpl
