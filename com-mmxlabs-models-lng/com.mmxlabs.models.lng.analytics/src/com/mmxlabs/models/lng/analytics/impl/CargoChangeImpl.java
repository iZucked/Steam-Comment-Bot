/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CargoChange;
import com.mmxlabs.models.lng.analytics.PositionDescriptor;
import com.mmxlabs.models.lng.analytics.SlotDescriptor;
import com.mmxlabs.models.lng.analytics.VesselAllocationDescriptor;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl#getSlotDescriptors <em>Slot Descriptors</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl#getVesselAllocation <em>Vessel Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.CargoChangeImpl#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoChangeImpl extends ChangeImpl implements CargoChange {
	/**
	 * The cached value of the '{@link #getSlotDescriptors() <em>Slot Descriptors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotDescriptors()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotDescriptor> slotDescriptors;

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
	protected CargoChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.CARGO_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SlotDescriptor> getSlotDescriptors() {
		if (slotDescriptors == null) {
			slotDescriptors = new EObjectContainmentEList<SlotDescriptor>(SlotDescriptor.class, this, AnalyticsPackage.CARGO_CHANGE__SLOT_DESCRIPTORS);
		}
		return slotDescriptors;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION, oldVesselAllocation, newVesselAllocation);
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
				msgs = ((InternalEObject)vesselAllocation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION, null, msgs);
			if (newVesselAllocation != null)
				msgs = ((InternalEObject)newVesselAllocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION, null, msgs);
			msgs = basicSetVesselAllocation(newVesselAllocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION, newVesselAllocation, newVesselAllocation));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.CARGO_CHANGE__POSITION, oldPosition, newPosition);
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
				msgs = ((InternalEObject)position).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.CARGO_CHANGE__POSITION, null, msgs);
			if (newPosition != null)
				msgs = ((InternalEObject)newPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.CARGO_CHANGE__POSITION, null, msgs);
			msgs = basicSetPosition(newPosition, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.CARGO_CHANGE__POSITION, newPosition, newPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.CARGO_CHANGE__SLOT_DESCRIPTORS:
				return ((InternalEList<?>)getSlotDescriptors()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION:
				return basicSetVesselAllocation(null, msgs);
			case AnalyticsPackage.CARGO_CHANGE__POSITION:
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
			case AnalyticsPackage.CARGO_CHANGE__SLOT_DESCRIPTORS:
				return getSlotDescriptors();
			case AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION:
				return getVesselAllocation();
			case AnalyticsPackage.CARGO_CHANGE__POSITION:
				return getPosition();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AnalyticsPackage.CARGO_CHANGE__SLOT_DESCRIPTORS:
				getSlotDescriptors().clear();
				getSlotDescriptors().addAll((Collection<? extends SlotDescriptor>)newValue);
				return;
			case AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION:
				setVesselAllocation((VesselAllocationDescriptor)newValue);
				return;
			case AnalyticsPackage.CARGO_CHANGE__POSITION:
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
			case AnalyticsPackage.CARGO_CHANGE__SLOT_DESCRIPTORS:
				getSlotDescriptors().clear();
				return;
			case AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION:
				setVesselAllocation((VesselAllocationDescriptor)null);
				return;
			case AnalyticsPackage.CARGO_CHANGE__POSITION:
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
			case AnalyticsPackage.CARGO_CHANGE__SLOT_DESCRIPTORS:
				return slotDescriptors != null && !slotDescriptors.isEmpty();
			case AnalyticsPackage.CARGO_CHANGE__VESSEL_ALLOCATION:
				return vesselAllocation != null;
			case AnalyticsPackage.CARGO_CHANGE__POSITION:
				return position != null;
		}
		return super.eIsSet(featureID);
	}

} //CargoChangeImpl
