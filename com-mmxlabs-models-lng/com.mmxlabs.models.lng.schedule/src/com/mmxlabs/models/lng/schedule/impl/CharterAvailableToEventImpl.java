/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.CharterAvailableToEvent;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Available To Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CharterAvailableToEventImpl#getLinkedSequence <em>Linked Sequence</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterAvailableToEventImpl extends EventImpl implements CharterAvailableToEvent {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterAvailableToEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CHARTER_AVAILABLE_TO_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence getLinkedSequence() {
		if (linkedSequence != null && linkedSequence.eIsProxy()) {
			InternalEObject oldLinkedSequence = (InternalEObject)linkedSequence;
			linkedSequence = (Sequence)eResolveProxy(oldLinkedSequence);
			if (linkedSequence != oldLinkedSequence) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CHARTER_AVAILABLE_TO_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
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
	public void setLinkedSequence(Sequence newLinkedSequence) {
		Sequence oldLinkedSequence = linkedSequence;
		linkedSequence = newLinkedSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CHARTER_AVAILABLE_TO_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.CHARTER_AVAILABLE_TO_EVENT__LINKED_SEQUENCE:
				if (resolve) return getLinkedSequence();
				return basicGetLinkedSequence();
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
			case SchedulePackage.CHARTER_AVAILABLE_TO_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)newValue);
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
			case SchedulePackage.CHARTER_AVAILABLE_TO_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)null);
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
			case SchedulePackage.CHARTER_AVAILABLE_TO_EVENT__LINKED_SEQUENCE:
				return linkedSequence != null;
		}
		return super.eIsSet(featureID);
	}

} //CharterAvailableToEventImpl
