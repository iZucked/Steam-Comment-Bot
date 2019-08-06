/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.PositionDescriptor;
import com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor;
import com.mmxlabs.models.lng.analytics.VesselEventChange;
import com.mmxlabs.models.lng.analytics.VesselEventDescriptor;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl#getVesselEventDescriptor <em>Vessel Event Descriptor</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl#getVesselAllocation <em>Vessel Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.VesselEventChangeImpl#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselEventChangeImpl extends ChangeImpl implements VesselEventChange {
	/**
	 * The cached value of the '{@link #getVesselEventDescriptor() <em>Vessel Event Descriptor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEventDescriptor()
	 * @generated
	 * @ordered
	 */
	protected VesselEventDescriptor vesselEventDescriptor;

	/**
	 * The cached value of the '{@link #getVesselAllocation() <em>Vessel Allocation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAllocation()
	 * @generated
	 * @ordered
	 */
	protected VesselAllocationDescriptor vesselAllocation;

	/**
	 * The cached value of the '{@link #getPosition() <em>Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPosition()
	 * @generated
	 * @ordered
	 */
	protected PositionDescriptor position;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.VESSEL_EVENT_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventDescriptor getVesselEventDescriptor() {
		return vesselEventDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetVesselEventDescriptor(VesselEventDescriptor newVesselEventDescriptor, NotificationChain msgs) {
		VesselEventDescriptor oldVesselEventDescriptor = vesselEventDescriptor;
		vesselEventDescriptor = newVesselEventDescriptor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR, oldVesselEventDescriptor, newVesselEventDescriptor);
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
	public void setVesselEventDescriptor(VesselEventDescriptor newVesselEventDescriptor) {
		if (newVesselEventDescriptor != vesselEventDescriptor) {
			NotificationChain msgs = null;
			if (vesselEventDescriptor != null)
				msgs = ((InternalEObject)vesselEventDescriptor).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR, null, msgs);
			if (newVesselEventDescriptor != null)
				msgs = ((InternalEObject)newVesselEventDescriptor).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR, null, msgs);
			msgs = basicSetVesselEventDescriptor(newVesselEventDescriptor, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR, newVesselEventDescriptor, newVesselEventDescriptor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselAllocationDescriptor getVesselAllocation() {
		return vesselAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetVesselAllocation(VesselAllocationDescriptor newVesselAllocation, NotificationChain msgs) {
		VesselAllocationDescriptor oldVesselAllocation = vesselAllocation;
		vesselAllocation = newVesselAllocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION, oldVesselAllocation, newVesselAllocation);
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
	public void setVesselAllocation(VesselAllocationDescriptor newVesselAllocation) {
		if (newVesselAllocation != vesselAllocation) {
			NotificationChain msgs = null;
			if (vesselAllocation != null)
				msgs = ((InternalEObject)vesselAllocation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION, null, msgs);
			if (newVesselAllocation != null)
				msgs = ((InternalEObject)newVesselAllocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION, null, msgs);
			msgs = basicSetVesselAllocation(newVesselAllocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION, newVesselAllocation, newVesselAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PositionDescriptor getPosition() {
		return position;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPosition(PositionDescriptor newPosition, NotificationChain msgs) {
		PositionDescriptor oldPosition = position;
		position = newPosition;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION, oldPosition, newPosition);
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
	public void setPosition(PositionDescriptor newPosition) {
		if (newPosition != position) {
			NotificationChain msgs = null;
			if (position != null)
				msgs = ((InternalEObject)position).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION, null, msgs);
			if (newPosition != null)
				msgs = ((InternalEObject)newPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION, null, msgs);
			msgs = basicSetPosition(newPosition, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION, newPosition, newPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR:
				return basicSetVesselEventDescriptor(null, msgs);
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION:
				return basicSetVesselAllocation(null, msgs);
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION:
				return basicSetPosition(null, msgs);
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
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR:
				return getVesselEventDescriptor();
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION:
				return getVesselAllocation();
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION:
				return getPosition();
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
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR:
				setVesselEventDescriptor((VesselEventDescriptor)newValue);
				return;
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION:
				setVesselAllocation((VesselAllocationDescriptor)newValue);
				return;
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION:
				setPosition((PositionDescriptor)newValue);
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
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR:
				setVesselEventDescriptor((VesselEventDescriptor)null);
				return;
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION:
				setVesselAllocation((VesselAllocationDescriptor)null);
				return;
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION:
				setPosition((PositionDescriptor)null);
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
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_EVENT_DESCRIPTOR:
				return vesselEventDescriptor != null;
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__VESSEL_ALLOCATION:
				return vesselAllocation != null;
			case AnalyticsPackage.VESSEL_EVENT_CHANGE__POSITION:
				return position != null;
		}
		return super.eIsSet(featureID);
	}

} //VesselEventChangeImpl
