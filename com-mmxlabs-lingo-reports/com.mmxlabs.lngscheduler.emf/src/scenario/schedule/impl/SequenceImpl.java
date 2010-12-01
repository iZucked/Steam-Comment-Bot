/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.impl;

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

import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;

import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;

import scenario.schedule.events.ScheduledEvent;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.impl.SequenceImpl#getFleetVessel <em>Fleet Vessel</em>}</li>
 *   <li>{@link scenario.schedule.impl.SequenceImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link scenario.schedule.impl.SequenceImpl#getCharterVesselClass <em>Charter Vessel Class</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SequenceImpl extends EObjectImpl implements Sequence {
	/**
	 * The cached value of the '{@link #getFleetVessel() <em>Fleet Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleetVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel fleetVessel;

	/**
	 * This is true if the Fleet Vessel reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fleetVesselESet;

	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<ScheduledEvent> events;

	/**
	 * The cached value of the '{@link #getCharterVesselClass() <em>Charter Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterVesselClass()
	 * @generated
	 * @ordered
	 */
	protected VesselClass charterVesselClass;

	/**
	 * This is true if the Charter Vessel Class reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean charterVesselClassESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SequenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.SEQUENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getFleetVessel() {
		if (fleetVessel != null && fleetVessel.eIsProxy()) {
			InternalEObject oldFleetVessel = (InternalEObject)fleetVessel;
			fleetVessel = (Vessel)eResolveProxy(oldFleetVessel);
			if (fleetVessel != oldFleetVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SEQUENCE__FLEET_VESSEL, oldFleetVessel, fleetVessel));
			}
		}
		return fleetVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetFleetVessel() {
		return fleetVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFleetVessel(Vessel newFleetVessel) {
		Vessel oldFleetVessel = fleetVessel;
		fleetVessel = newFleetVessel;
		boolean oldFleetVesselESet = fleetVesselESet;
		fleetVesselESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__FLEET_VESSEL, oldFleetVessel, fleetVessel, !oldFleetVesselESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFleetVessel() {
		Vessel oldFleetVessel = fleetVessel;
		boolean oldFleetVesselESet = fleetVesselESet;
		fleetVessel = null;
		fleetVesselESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SEQUENCE__FLEET_VESSEL, oldFleetVessel, null, oldFleetVesselESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFleetVessel() {
		return fleetVesselESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ScheduledEvent> getEvents() {
		if (events == null) {
			events = new EObjectContainmentEList<ScheduledEvent>(ScheduledEvent.class, this, SchedulePackage.SEQUENCE__EVENTS);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass getCharterVesselClass() {
		if (charterVesselClass != null && charterVesselClass.eIsProxy()) {
			InternalEObject oldCharterVesselClass = (InternalEObject)charterVesselClass;
			charterVesselClass = (VesselClass)eResolveProxy(oldCharterVesselClass);
			if (charterVesselClass != oldCharterVesselClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SEQUENCE__CHARTER_VESSEL_CLASS, oldCharterVesselClass, charterVesselClass));
			}
		}
		return charterVesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetCharterVesselClass() {
		return charterVesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterVesselClass(VesselClass newCharterVesselClass) {
		VesselClass oldCharterVesselClass = charterVesselClass;
		charterVesselClass = newCharterVesselClass;
		boolean oldCharterVesselClassESet = charterVesselClassESet;
		charterVesselClassESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__CHARTER_VESSEL_CLASS, oldCharterVesselClass, charterVesselClass, !oldCharterVesselClassESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCharterVesselClass() {
		VesselClass oldCharterVesselClass = charterVesselClass;
		boolean oldCharterVesselClassESet = charterVesselClassESet;
		charterVesselClass = null;
		charterVesselClassESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SEQUENCE__CHARTER_VESSEL_CLASS, oldCharterVesselClass, null, oldCharterVesselClassESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCharterVesselClass() {
		return charterVesselClassESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SEQUENCE__EVENTS:
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
			case SchedulePackage.SEQUENCE__FLEET_VESSEL:
				if (resolve) return getFleetVessel();
				return basicGetFleetVessel();
			case SchedulePackage.SEQUENCE__EVENTS:
				return getEvents();
			case SchedulePackage.SEQUENCE__CHARTER_VESSEL_CLASS:
				if (resolve) return getCharterVesselClass();
				return basicGetCharterVesselClass();
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
			case SchedulePackage.SEQUENCE__FLEET_VESSEL:
				setFleetVessel((Vessel)newValue);
				return;
			case SchedulePackage.SEQUENCE__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends ScheduledEvent>)newValue);
				return;
			case SchedulePackage.SEQUENCE__CHARTER_VESSEL_CLASS:
				setCharterVesselClass((VesselClass)newValue);
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
			case SchedulePackage.SEQUENCE__FLEET_VESSEL:
				unsetFleetVessel();
				return;
			case SchedulePackage.SEQUENCE__EVENTS:
				getEvents().clear();
				return;
			case SchedulePackage.SEQUENCE__CHARTER_VESSEL_CLASS:
				unsetCharterVesselClass();
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
			case SchedulePackage.SEQUENCE__FLEET_VESSEL:
				return isSetFleetVessel();
			case SchedulePackage.SEQUENCE__EVENTS:
				return events != null && !events.isEmpty();
			case SchedulePackage.SEQUENCE__CHARTER_VESSEL_CLASS:
				return isSetCharterVesselClass();
		}
		return super.eIsSet(featureID);
	}

} //SequenceImpl
