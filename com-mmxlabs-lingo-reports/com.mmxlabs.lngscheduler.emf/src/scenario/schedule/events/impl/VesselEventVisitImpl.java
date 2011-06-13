/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events.impl;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.fleet.VesselEvent;

import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.VesselEventVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.events.impl.VesselEventVisitImpl#getVesselEvent <em>Vessel Event</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselEventVisitImpl extends PortVisitImpl implements VesselEventVisit {
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
		return EventsPackage.Literals.VESSEL_EVENT_VISIT;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return getVesselEvent().getId();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
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
			case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
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
			case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
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
			case EventsPackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				return vesselEvent != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == PortVisit.class) {
			switch (baseOperationID) {
				case EventsPackage.PORT_VISIT___GET_ID: return EventsPackage.VESSEL_EVENT_VISIT___GET_ID;
				default: return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EventsPackage.VESSEL_EVENT_VISIT___GET_ID:
				return getId();
		}
		return super.eInvoke(operationID, arguments);
	}

} //VesselEventVisitImpl
