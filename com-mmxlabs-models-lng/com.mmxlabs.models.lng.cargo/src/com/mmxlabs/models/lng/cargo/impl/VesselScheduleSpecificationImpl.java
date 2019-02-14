/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;

import com.mmxlabs.models.lng.types.VesselAssignmentType;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Schedule Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselScheduleSpecificationImpl#getVesselAllocation <em>Vessel Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselScheduleSpecificationImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselScheduleSpecificationImpl#getEvents <em>Events</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselScheduleSpecificationImpl extends EObjectImpl implements VesselScheduleSpecification {
	/**
	 * The cached value of the '{@link #getVesselAllocation() <em>Vessel Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAllocation()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType vesselAllocation;

	/**
	 * The default value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected int spotIndex = SPOT_INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<ScheduleSpecificationEvent> events;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselScheduleSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.VESSEL_SCHEDULE_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselAssignmentType getVesselAllocation() {
		if (vesselAllocation != null && vesselAllocation.eIsProxy()) {
			InternalEObject oldVesselAllocation = (InternalEObject)vesselAllocation;
			vesselAllocation = (VesselAssignmentType)eResolveProxy(oldVesselAllocation);
			if (vesselAllocation != oldVesselAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION, oldVesselAllocation, vesselAllocation));
			}
		}
		return vesselAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetVesselAllocation() {
		return vesselAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselAllocation(VesselAssignmentType newVesselAllocation) {
		VesselAssignmentType oldVesselAllocation = vesselAllocation;
		vesselAllocation = newVesselAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION, oldVesselAllocation, vesselAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ScheduleSpecificationEvent> getEvents() {
		if (events == null) {
			events = new EObjectContainmentEList.Resolving<ScheduleSpecificationEvent>(ScheduleSpecificationEvent.class, this, CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__EVENTS);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__EVENTS:
				return ((InternalEList<?>)getEvents()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION:
				if (resolve) return getVesselAllocation();
				return basicGetVesselAllocation();
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX:
				return getSpotIndex();
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__EVENTS:
				return getEvents();
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
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION:
				setVesselAllocation((VesselAssignmentType)newValue);
				return;
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends ScheduleSpecificationEvent>)newValue);
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
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION:
				setVesselAllocation((VesselAssignmentType)null);
				return;
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
				return;
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__EVENTS:
				getEvents().clear();
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
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION:
				return vesselAllocation != null;
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION__EVENTS:
				return events != null && !events.isEmpty();
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
		result.append(" (spotIndex: ");
		result.append(spotIndex);
		result.append(')');
		return result.toString();
	}

} //VesselScheduleSpecificationImpl
