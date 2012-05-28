/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getVesselEvent <em>Vessel Event</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselEventVisitImpl extends EventImpl implements VesselEventVisit {
	/**
	 * The cached value of the '{@link #getVesselEvent() <em>Vessel Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvent()
	 * @generated
	 * @ordered
	 */
	protected VesselEvent vesselEvent;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.VESSEL_EVENT_VISIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselEvent getVesselEvent() {
		if (vesselEvent != null && vesselEvent.eIsProxy()) {
			InternalEObject oldVesselEvent = (InternalEObject)vesselEvent;
			vesselEvent = (VesselEvent)eResolveProxy(oldVesselEvent);
			if (vesselEvent != oldVesselEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
			}
		}
		return vesselEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselEvent basicGetVesselEvent() {
		return vesselEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselEvent(VesselEvent newVesselEvent) {
		VesselEvent oldVesselEvent = vesselEvent;
		vesselEvent = newVesselEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				if (resolve) return getVesselEvent();
				return basicGetVesselEvent();
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
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				setVesselEvent((VesselEvent)newValue);
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
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				setVesselEvent((VesselEvent)null);
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
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				return vesselEvent != null;
		}
		return super.eIsSet(featureID);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String name() {
		final VesselEvent event = getVesselEvent();
		if (event != null) {
			return event.getName();
		}
		return super.name();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String type() {
		final VesselEvent event = getVesselEvent();
		if (event != null) {
			if (event instanceof CharterOutEvent) {
				return "Charter Out";
			}
			if (event instanceof DryDockEvent) {
				return "Dry Dock";
			}
			if (event instanceof MaintenanceEvent) {
				return "Maintenance";
			}
		}
		return "Unknown Event";
	}
	
} // end of VesselEventVisitImpl

// finish type fixing
