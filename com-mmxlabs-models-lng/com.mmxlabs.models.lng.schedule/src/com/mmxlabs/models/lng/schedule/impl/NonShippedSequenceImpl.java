/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.NonShippedSequence;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

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
 * An implementation of the model object '<em><b>Non Shipped Sequence</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NonShippedSequenceImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NonShippedSequenceImpl#getEvents <em>Events</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NonShippedSequenceImpl extends MMXObjectImpl implements NonShippedSequence {
	/**
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<Event> events;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NonShippedSequenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.NON_SHIPPED_SEQUENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.NON_SHIPPED_SEQUENCE__VESSEL, oldVessel, vessel));
			}
		}
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetVessel() {
		return vessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NON_SHIPPED_SEQUENCE__VESSEL, oldVessel, vessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectContainmentEList<Event>(Event.class, this, SchedulePackage.NON_SHIPPED_SEQUENCE__EVENTS);
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
			case SchedulePackage.NON_SHIPPED_SEQUENCE__EVENTS:
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
			case SchedulePackage.NON_SHIPPED_SEQUENCE__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case SchedulePackage.NON_SHIPPED_SEQUENCE__EVENTS:
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
			case SchedulePackage.NON_SHIPPED_SEQUENCE__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case SchedulePackage.NON_SHIPPED_SEQUENCE__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
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
			case SchedulePackage.NON_SHIPPED_SEQUENCE__VESSEL:
				setVessel((Vessel)null);
				return;
			case SchedulePackage.NON_SHIPPED_SEQUENCE__EVENTS:
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
			case SchedulePackage.NON_SHIPPED_SEQUENCE__VESSEL:
				return vessel != null;
			case SchedulePackage.NON_SHIPPED_SEQUENCE__EVENTS:
				return events != null && !events.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //NonShippedSequenceImpl
