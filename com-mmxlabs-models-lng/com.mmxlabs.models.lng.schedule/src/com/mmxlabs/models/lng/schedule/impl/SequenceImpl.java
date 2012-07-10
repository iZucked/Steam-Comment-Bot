/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getFitnesses <em>Fitnesses</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getDailyHireRate <em>Daily Hire Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getSpotIndex <em>Spot Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SequenceImpl extends MMXObjectImpl implements Sequence {
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
	 * The cached value of the '{@link #getVessel() <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel vessel;

	/**
	 * This is true if the Vessel reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean vesselESet;

	/**
	 * The cached value of the '{@link #getVesselClass() <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClass()
	 * @generated
	 * @ordered
	 */
	protected VesselClass vesselClass;

	/**
	 * This is true if the Vessel Class reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean vesselClassESet;

	/**
	 * The cached value of the '{@link #getFitnesses() <em>Fitnesses</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFitnesses()
	 * @generated
	 * @ordered
	 */
	protected EList<Fitness> fitnesses;

	/**
	 * The default value of the '{@link #getDailyHireRate() <em>Daily Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyHireRate()
	 * @generated
	 * @ordered
	 */
	protected static final int DAILY_HIRE_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDailyHireRate() <em>Daily Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDailyHireRate()
	 * @generated
	 * @ordered
	 */
	protected int dailyHireRate = DAILY_HIRE_RATE_EDEFAULT;

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
	 * This is true if the Spot Index attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean spotIndexESet;

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
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectContainmentWithInverseEList<Event>(Event.class, this, SchedulePackage.SEQUENCE__EVENTS, SchedulePackage.EVENT__SEQUENCE);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getVessel() {
		if (vessel != null && vessel.eIsProxy()) {
			InternalEObject oldVessel = (InternalEObject)vessel;
			vessel = (Vessel)eResolveProxy(oldVessel);
			if (vessel != oldVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SEQUENCE__VESSEL, oldVessel, vessel));
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
	public void setVessel(Vessel newVessel) {
		Vessel oldVessel = vessel;
		vessel = newVessel;
		boolean oldVesselESet = vesselESet;
		vesselESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__VESSEL, oldVessel, vessel, !oldVesselESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetVessel() {
		Vessel oldVessel = vessel;
		boolean oldVesselESet = vesselESet;
		vessel = null;
		vesselESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SEQUENCE__VESSEL, oldVessel, null, oldVesselESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetVessel() {
		return vesselESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass getVesselClass() {
		if (vesselClass != null && vesselClass.eIsProxy()) {
			InternalEObject oldVesselClass = (InternalEObject)vesselClass;
			vesselClass = (VesselClass)eResolveProxy(oldVesselClass);
			if (vesselClass != oldVesselClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SEQUENCE__VESSEL_CLASS, oldVesselClass, vesselClass));
			}
		}
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetVesselClass() {
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselClass(VesselClass newVesselClass) {
		VesselClass oldVesselClass = vesselClass;
		vesselClass = newVesselClass;
		boolean oldVesselClassESet = vesselClassESet;
		vesselClassESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__VESSEL_CLASS, oldVesselClass, vesselClass, !oldVesselClassESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetVesselClass() {
		VesselClass oldVesselClass = vesselClass;
		boolean oldVesselClassESet = vesselClassESet;
		vesselClass = null;
		vesselClassESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SEQUENCE__VESSEL_CLASS, oldVesselClass, null, oldVesselClassESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetVesselClass() {
		return vesselClassESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Fitness> getFitnesses() {
		if (fitnesses == null) {
			fitnesses = new EObjectContainmentEList<Fitness>(Fitness.class, this, SchedulePackage.SEQUENCE__FITNESSES);
		}
		return fitnesses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDailyHireRate() {
		return dailyHireRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDailyHireRate(int newDailyHireRate) {
		int oldDailyHireRate = dailyHireRate;
		dailyHireRate = newDailyHireRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__DAILY_HIRE_RATE, oldDailyHireRate, dailyHireRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		boolean oldSpotIndexESet = spotIndexESet;
		spotIndexESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__SPOT_INDEX, oldSpotIndex, spotIndex, !oldSpotIndexESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSpotIndex() {
		int oldSpotIndex = spotIndex;
		boolean oldSpotIndexESet = spotIndexESet;
		spotIndex = SPOT_INDEX_EDEFAULT;
		spotIndexESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SEQUENCE__SPOT_INDEX, oldSpotIndex, SPOT_INDEX_EDEFAULT, oldSpotIndexESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSpotIndex() {
		return spotIndexESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getName() {
		if (isSetVessel()) {
			return getVessel().getName();
		} else if (isSetVesselClass()) {
			return getVesselClass().getName();
		} else {
			for (final Event e : getEvents()) {
				if (e instanceof SlotVisit) {
					Slot slot = ((SlotVisit) e).getSlotAllocation().getSlot();
					if (slot != null) {
						if (slot instanceof LoadSlot) {
							if (((LoadSlot) slot).isDESPurchase()) {
								return "DES Purchase";
							}
						}
						if (slot instanceof DischargeSlot) {
							if (((DischargeSlot) slot).isFOBSale()) {
								return "FOB Sale";
							}
						}
					}
				}
			}
		}
		
		return "<no vessel>";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isSpotVessel() {
		return isSetVesselClass();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isFleetVessel() {
		return isSetVessel() && !getVessel().isSetTimeCharterRate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isTimeCharterVessel() {
		return isSetVessel() && getVessel().isSetTimeCharterRate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SEQUENCE__EVENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEvents()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
			case SchedulePackage.SEQUENCE__FITNESSES:
				return ((InternalEList<?>)getFitnesses()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.SEQUENCE__EVENTS:
				return getEvents();
			case SchedulePackage.SEQUENCE__VESSEL:
				if (resolve) return getVessel();
				return basicGetVessel();
			case SchedulePackage.SEQUENCE__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
			case SchedulePackage.SEQUENCE__FITNESSES:
				return getFitnesses();
			case SchedulePackage.SEQUENCE__DAILY_HIRE_RATE:
				return getDailyHireRate();
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				return getSpotIndex();
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
			case SchedulePackage.SEQUENCE__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
				return;
			case SchedulePackage.SEQUENCE__VESSEL:
				setVessel((Vessel)newValue);
				return;
			case SchedulePackage.SEQUENCE__VESSEL_CLASS:
				setVesselClass((VesselClass)newValue);
				return;
			case SchedulePackage.SEQUENCE__FITNESSES:
				getFitnesses().clear();
				getFitnesses().addAll((Collection<? extends Fitness>)newValue);
				return;
			case SchedulePackage.SEQUENCE__DAILY_HIRE_RATE:
				setDailyHireRate((Integer)newValue);
				return;
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
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
			case SchedulePackage.SEQUENCE__EVENTS:
				getEvents().clear();
				return;
			case SchedulePackage.SEQUENCE__VESSEL:
				unsetVessel();
				return;
			case SchedulePackage.SEQUENCE__VESSEL_CLASS:
				unsetVesselClass();
				return;
			case SchedulePackage.SEQUENCE__FITNESSES:
				getFitnesses().clear();
				return;
			case SchedulePackage.SEQUENCE__DAILY_HIRE_RATE:
				setDailyHireRate(DAILY_HIRE_RATE_EDEFAULT);
				return;
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				unsetSpotIndex();
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
			case SchedulePackage.SEQUENCE__EVENTS:
				return events != null && !events.isEmpty();
			case SchedulePackage.SEQUENCE__VESSEL:
				return isSetVessel();
			case SchedulePackage.SEQUENCE__VESSEL_CLASS:
				return isSetVesselClass();
			case SchedulePackage.SEQUENCE__FITNESSES:
				return fitnesses != null && !fitnesses.isEmpty();
			case SchedulePackage.SEQUENCE__DAILY_HIRE_RATE:
				return dailyHireRate != DAILY_HIRE_RATE_EDEFAULT;
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				return isSetSpotIndex();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.SEQUENCE___GET_NAME:
				return getName();
			case SchedulePackage.SEQUENCE___IS_SPOT_VESSEL:
				return isSpotVessel();
			case SchedulePackage.SEQUENCE___IS_FLEET_VESSEL:
				return isFleetVessel();
			case SchedulePackage.SEQUENCE___IS_TIME_CHARTER_VESSEL:
				return isTimeCharterVessel();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (dailyHireRate: ");
		result.append(dailyHireRate);
		result.append(", spotIndex: ");
		if (spotIndexESet) result.append(spotIndex); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of SequenceImpl

// finish type fixing
