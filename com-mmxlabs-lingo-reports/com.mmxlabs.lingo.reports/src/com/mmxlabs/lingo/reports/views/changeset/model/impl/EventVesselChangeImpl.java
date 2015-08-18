/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Vessel Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getOriginalEvent <em>Original Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getNewEvent <em>New Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getOriginalVessel <em>Original Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getNewVessel <em>New Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getEventName <em>Event Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EventVesselChangeImpl extends ChangeImpl implements EventVesselChange {
	/**
	 * The cached value of the '{@link #getOriginalEvent() <em>Original Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalEvent()
	 * @generated
	 * @ordered
	 */
	protected Event originalEvent;

	/**
	 * The cached value of the '{@link #getNewEvent() <em>New Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewEvent()
	 * @generated
	 * @ordered
	 */
	protected Event newEvent;

	/**
	 * The cached value of the '{@link #getOriginalVessel() <em>Original Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVessel()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType originalVessel;

	/**
	 * The cached value of the '{@link #getNewVessel() <em>New Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewVessel()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType newVessel;

	/**
	 * The default value of the '{@link #getEventName() <em>Event Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventName()
	 * @generated
	 * @ordered
	 */
	protected static final String EVENT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEventName() <em>Event Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventName()
	 * @generated
	 * @ordered
	 */
	protected String eventName = EVENT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventVesselChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.EVENT_VESSEL_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event getOriginalEvent() {
		if (originalEvent != null && originalEvent.eIsProxy()) {
			InternalEObject oldOriginalEvent = (InternalEObject)originalEvent;
			originalEvent = (Event)eResolveProxy(oldOriginalEvent);
			if (originalEvent != oldOriginalEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_EVENT, oldOriginalEvent, originalEvent));
			}
		}
		return originalEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event basicGetOriginalEvent() {
		return originalEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalEvent(Event newOriginalEvent) {
		Event oldOriginalEvent = originalEvent;
		originalEvent = newOriginalEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_EVENT, oldOriginalEvent, originalEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event getNewEvent() {
		if (newEvent != null && newEvent.eIsProxy()) {
			InternalEObject oldNewEvent = (InternalEObject)newEvent;
			newEvent = (Event)eResolveProxy(oldNewEvent);
			if (newEvent != oldNewEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_EVENT, oldNewEvent, newEvent));
			}
		}
		return newEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event basicGetNewEvent() {
		return newEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewEvent(Event newNewEvent) {
		Event oldNewEvent = newEvent;
		newEvent = newNewEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_EVENT, oldNewEvent, newEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType getOriginalVessel() {
		if (originalVessel != null && originalVessel.eIsProxy()) {
			InternalEObject oldOriginalVessel = (InternalEObject)originalVessel;
			originalVessel = (VesselAssignmentType)eResolveProxy(oldOriginalVessel);
			if (originalVessel != oldOriginalVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL, oldOriginalVessel, originalVessel));
			}
		}
		return originalVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetOriginalVessel() {
		return originalVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalVessel(VesselAssignmentType newOriginalVessel) {
		VesselAssignmentType oldOriginalVessel = originalVessel;
		originalVessel = newOriginalVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL, oldOriginalVessel, originalVessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType getNewVessel() {
		if (newVessel != null && newVessel.eIsProxy()) {
			InternalEObject oldNewVessel = (InternalEObject)newVessel;
			newVessel = (VesselAssignmentType)eResolveProxy(oldNewVessel);
			if (newVessel != oldNewVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL, oldNewVessel, newVessel));
			}
		}
		return newVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetNewVessel() {
		return newVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewVessel(VesselAssignmentType newNewVessel) {
		VesselAssignmentType oldNewVessel = newVessel;
		newVessel = newNewVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL, oldNewVessel, newVessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEventName(String newEventName) {
		String oldEventName = eventName;
		eventName = newEventName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__EVENT_NAME, oldEventName, eventName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_EVENT:
				if (resolve) return getOriginalEvent();
				return basicGetOriginalEvent();
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_EVENT:
				if (resolve) return getNewEvent();
				return basicGetNewEvent();
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				if (resolve) return getOriginalVessel();
				return basicGetOriginalVessel();
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				if (resolve) return getNewVessel();
				return basicGetNewVessel();
			case ChangesetPackage.EVENT_VESSEL_CHANGE__EVENT_NAME:
				return getEventName();
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
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_EVENT:
				setOriginalEvent((Event)newValue);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_EVENT:
				setNewEvent((Event)newValue);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)newValue);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)newValue);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__EVENT_NAME:
				setEventName((String)newValue);
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
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_EVENT:
				setOriginalEvent((Event)null);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_EVENT:
				setNewEvent((Event)null);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)null);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)null);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__EVENT_NAME:
				setEventName(EVENT_NAME_EDEFAULT);
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
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_EVENT:
				return originalEvent != null;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_EVENT:
				return newEvent != null;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				return originalVessel != null;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				return newVessel != null;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__EVENT_NAME:
				return EVENT_NAME_EDEFAULT == null ? eventName != null : !EVENT_NAME_EDEFAULT.equals(eventName);
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (eventName: ");
		result.append(eventName);
		result.append(')');
		return result.toString();
	}

} //EventVesselChangeImpl
