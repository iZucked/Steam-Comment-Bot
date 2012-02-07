/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.fleet.VesselEvent;
import scenario.schedule.VesselEventRevenue;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.VesselEventVisit;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Vessel Event Visit</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.schedule.events.impl.VesselEventVisitImpl#getVesselEvent <em>Vessel Event</em>}</li>
 * <li>{@link scenario.schedule.events.impl.VesselEventVisitImpl#getRevenue <em>Revenue</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VesselEventVisitImpl extends PortVisitImpl implements VesselEventVisit {
	/**
	 * The cached value of the '{@link #getVesselEvent() <em>Vessel Event</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVesselEvent()
	 * @generated
	 * @ordered
	 */
	protected VesselEvent vesselEvent;

	/**
	 * The cached value of the '{@link #getRevenue() <em>Revenue</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRevenue()
	 * @generated
	 * @ordered
	 */
	protected VesselEventRevenue revenue;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VesselEventVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.VESSEL_EVENT_VISIT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselEvent getVesselEvent() {
		if ((vesselEvent != null) && vesselEvent.eIsProxy()) {
			final InternalEObject oldVesselEvent = (InternalEObject) vesselEvent;
			vesselEvent = (VesselEvent) eResolveProxy(oldVesselEvent);
			if (vesselEvent != oldVesselEvent) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
				}
			}
		}
		return vesselEvent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VesselEvent basicGetVesselEvent() {
		return vesselEvent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setVesselEvent(final VesselEvent newVesselEvent) {
		final VesselEvent oldVesselEvent = vesselEvent;
		vesselEvent = newVesselEvent;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselEventRevenue getRevenue() {
		if ((revenue != null) && revenue.eIsProxy()) {
			final InternalEObject oldRevenue = (InternalEObject) revenue;
			revenue = (VesselEventRevenue) eResolveProxy(oldRevenue);
			if (revenue != oldRevenue) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.VESSEL_EVENT_VISIT__REVENUE, oldRevenue, revenue));
				}
			}
		}
		return revenue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VesselEventRevenue basicGetRevenue() {
		return revenue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setRevenue(final VesselEventRevenue newRevenue) {
		final VesselEventRevenue oldRevenue = revenue;
		revenue = newRevenue;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.VESSEL_EVENT_VISIT__REVENUE, oldRevenue, revenue));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getId() {
		return getVesselEvent().getId();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getName() {
		return getId();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getDisplayTypeName() {
		return getVesselEvent().eClass().getName();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
			if (resolve) {
				return getVesselEvent();
			}
			return basicGetVesselEvent();
		case EventsPackage.VESSEL_EVENT_VISIT__REVENUE:
			if (resolve) {
				return getRevenue();
			}
			return basicGetRevenue();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
			setVesselEvent((VesselEvent) newValue);
			return;
		case EventsPackage.VESSEL_EVENT_VISIT__REVENUE:
			setRevenue((VesselEventRevenue) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
			setVesselEvent((VesselEvent) null);
			return;
		case EventsPackage.VESSEL_EVENT_VISIT__REVENUE:
			setRevenue((VesselEventRevenue) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
			return vesselEvent != null;
		case EventsPackage.VESSEL_EVENT_VISIT__REVENUE:
			return revenue != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(final int baseOperationID, final Class<?> baseClass) {
		if (baseClass == ScheduledEvent.class) {
			switch (baseOperationID) {
			case EventsPackage.SCHEDULED_EVENT___GET_NAME:
				return EventsPackage.VESSEL_EVENT_VISIT___GET_NAME;
			case EventsPackage.SCHEDULED_EVENT___GET_DISPLAY_TYPE_NAME:
				return EventsPackage.VESSEL_EVENT_VISIT___GET_DISPLAY_TYPE_NAME;
			default:
				return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		if (baseClass == PortVisit.class) {
			switch (baseOperationID) {
			case EventsPackage.PORT_VISIT___GET_ID:
				return EventsPackage.VESSEL_EVENT_VISIT___GET_ID;
			case EventsPackage.PORT_VISIT___GET_DISPLAY_TYPE_NAME:
				return EventsPackage.VESSEL_EVENT_VISIT___GET_DISPLAY_TYPE_NAME;
			default:
				return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case EventsPackage.VESSEL_EVENT_VISIT___GET_ID:
			return getId();
		case EventsPackage.VESSEL_EVENT_VISIT___GET_NAME:
			return getName();
		case EventsPackage.VESSEL_EVENT_VISIT___GET_DISPLAY_TYPE_NAME:
			return getDisplayTypeName();
		}
		return super.eInvoke(operationID, arguments);
	}

} // VesselEventVisitImpl
