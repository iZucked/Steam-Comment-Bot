/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
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

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sequence</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getVesselAvailability <em>Vessel Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getCharterInMarket <em>Charter In Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getFitnesses <em>Fitnesses</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl#getSequenceType <em>Sequence Type</em>}</li>
 * </ul>
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
	 * The cached value of the '{@link #getVesselAvailability() <em>Vessel Availability</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAvailability()
	 * @generated
	 * @ordered
	 */
	protected VesselAvailability vesselAvailability;

	/**
	 * This is true if the Vessel Availability reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean vesselAvailabilityESet;

	/**
	 * The cached value of the '{@link #getCharterInMarket() <em>Charter In Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarket()
	 * @generated
	 * @ordered
	 */
	protected CharterInMarket charterInMarket;

	/**
	 * This is true if the Charter In Market reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean charterInMarketESet;

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
	 * The default value of the '{@link #getSequenceType() <em>Sequence Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceType()
	 * @generated
	 * @ordered
	 */
	protected static final SequenceType SEQUENCE_TYPE_EDEFAULT = SequenceType.VESSEL;

	/**
	 * The cached value of the '{@link #getSequenceType() <em>Sequence Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceType()
	 * @generated
	 * @ordered
	 */
	protected SequenceType sequenceType = SEQUENCE_TYPE_EDEFAULT;

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
	public VesselAvailability getVesselAvailability() {
		if (vesselAvailability != null && vesselAvailability.eIsProxy()) {
			InternalEObject oldVesselAvailability = (InternalEObject)vesselAvailability;
			vesselAvailability = (VesselAvailability)eResolveProxy(oldVesselAvailability);
			if (vesselAvailability != oldVesselAvailability) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SEQUENCE__VESSEL_AVAILABILITY, oldVesselAvailability, vesselAvailability));
			}
		}
		return vesselAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailability basicGetVesselAvailability() {
		return vesselAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselAvailability(VesselAvailability newVesselAvailability) {
		VesselAvailability oldVesselAvailability = vesselAvailability;
		vesselAvailability = newVesselAvailability;
		boolean oldVesselAvailabilityESet = vesselAvailabilityESet;
		vesselAvailabilityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__VESSEL_AVAILABILITY, oldVesselAvailability, vesselAvailability, !oldVesselAvailabilityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetVesselAvailability() {
		VesselAvailability oldVesselAvailability = vesselAvailability;
		boolean oldVesselAvailabilityESet = vesselAvailabilityESet;
		vesselAvailability = null;
		vesselAvailabilityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SEQUENCE__VESSEL_AVAILABILITY, oldVesselAvailability, null, oldVesselAvailabilityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetVesselAvailability() {
		return vesselAvailabilityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarket getCharterInMarket() {
		if (charterInMarket != null && charterInMarket.eIsProxy()) {
			InternalEObject oldCharterInMarket = (InternalEObject)charterInMarket;
			charterInMarket = (CharterInMarket)eResolveProxy(oldCharterInMarket);
			if (charterInMarket != oldCharterInMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SEQUENCE__CHARTER_IN_MARKET, oldCharterInMarket, charterInMarket));
			}
		}
		return charterInMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarket basicGetCharterInMarket() {
		return charterInMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterInMarket(CharterInMarket newCharterInMarket) {
		CharterInMarket oldCharterInMarket = charterInMarket;
		charterInMarket = newCharterInMarket;
		boolean oldCharterInMarketESet = charterInMarketESet;
		charterInMarketESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__CHARTER_IN_MARKET, oldCharterInMarket, charterInMarket, !oldCharterInMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCharterInMarket() {
		CharterInMarket oldCharterInMarket = charterInMarket;
		boolean oldCharterInMarketESet = charterInMarketESet;
		charterInMarket = null;
		charterInMarketESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SEQUENCE__CHARTER_IN_MARKET, oldCharterInMarket, null, oldCharterInMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCharterInMarket() {
		return charterInMarketESet;
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
	 * @generated
	 */
	public SequenceType getSequenceType() {
		return sequenceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequenceType(SequenceType newSequenceType) {
		SequenceType oldSequenceType = sequenceType;
		sequenceType = newSequenceType == null ? SEQUENCE_TYPE_EDEFAULT : newSequenceType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SEQUENCE__SEQUENCE_TYPE, oldSequenceType, sequenceType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getName() {

		if (getSequenceType() == SequenceType.DES_PURCHASE) {
			return "DES Purchase";
		} else if (getSequenceType() == SequenceType.FOB_SALE) {
			return "FOB Sale";
		} else if (getSequenceType() == SequenceType.ROUND_TRIP) {
			return "Nominal Cargoes";
		} else if (isSetVesselAvailability() && getVesselAvailability() != null) {
			return getVesselAvailability().getVessel().getName();
		} else if (isSetCharterInMarket()) {
			return getCharterInMarket().getName();
		}

		return "<no vessel>";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isSpotVessel() {
		return isSetCharterInMarket();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isFleetVessel() {
		return isSetVesselAvailability() && !getVesselAvailability().isSetTimeCharterRate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isTimeCharterVessel() {
		return isSetVesselAvailability() && getVesselAvailability().isSetTimeCharterRate();
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
			case SchedulePackage.SEQUENCE__VESSEL_AVAILABILITY:
				if (resolve) return getVesselAvailability();
				return basicGetVesselAvailability();
			case SchedulePackage.SEQUENCE__CHARTER_IN_MARKET:
				if (resolve) return getCharterInMarket();
				return basicGetCharterInMarket();
			case SchedulePackage.SEQUENCE__FITNESSES:
				return getFitnesses();
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				return getSpotIndex();
			case SchedulePackage.SEQUENCE__SEQUENCE_TYPE:
				return getSequenceType();
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
			case SchedulePackage.SEQUENCE__VESSEL_AVAILABILITY:
				setVesselAvailability((VesselAvailability)newValue);
				return;
			case SchedulePackage.SEQUENCE__CHARTER_IN_MARKET:
				setCharterInMarket((CharterInMarket)newValue);
				return;
			case SchedulePackage.SEQUENCE__FITNESSES:
				getFitnesses().clear();
				getFitnesses().addAll((Collection<? extends Fitness>)newValue);
				return;
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case SchedulePackage.SEQUENCE__SEQUENCE_TYPE:
				setSequenceType((SequenceType)newValue);
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
			case SchedulePackage.SEQUENCE__VESSEL_AVAILABILITY:
				unsetVesselAvailability();
				return;
			case SchedulePackage.SEQUENCE__CHARTER_IN_MARKET:
				unsetCharterInMarket();
				return;
			case SchedulePackage.SEQUENCE__FITNESSES:
				getFitnesses().clear();
				return;
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				unsetSpotIndex();
				return;
			case SchedulePackage.SEQUENCE__SEQUENCE_TYPE:
				setSequenceType(SEQUENCE_TYPE_EDEFAULT);
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
			case SchedulePackage.SEQUENCE__VESSEL_AVAILABILITY:
				return isSetVesselAvailability();
			case SchedulePackage.SEQUENCE__CHARTER_IN_MARKET:
				return isSetCharterInMarket();
			case SchedulePackage.SEQUENCE__FITNESSES:
				return fitnesses != null && !fitnesses.isEmpty();
			case SchedulePackage.SEQUENCE__SPOT_INDEX:
				return isSetSpotIndex();
			case SchedulePackage.SEQUENCE__SEQUENCE_TYPE:
				return sequenceType != SEQUENCE_TYPE_EDEFAULT;
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
		result.append(" (spotIndex: ");
		if (spotIndexESet) result.append(spotIndex); else result.append("<unset>");
		result.append(", sequenceType: ");
		result.append(sequenceType);
		result.append(')');
		return result.toString();
	}

} // end of SequenceImpl

// finish type fixing
