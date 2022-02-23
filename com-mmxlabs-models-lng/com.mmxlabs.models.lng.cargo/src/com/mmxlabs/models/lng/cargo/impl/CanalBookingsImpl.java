/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal Bookings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getCanalBookingSlots <em>Canal Booking Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getArrivalMarginHours <em>Arrival Margin Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getVesselGroupCanalParameters <em>Vessel Group Canal Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getPanamaSeasonalityRecords <em>Panama Seasonality Records</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CanalBookingsImpl extends MMXObjectImpl implements CanalBookings {
	/**
	 * The cached value of the '{@link #getCanalBookingSlots() <em>Canal Booking Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalBookingSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<CanalBookingSlot> canalBookingSlots;

	/**
	 * The default value of the '{@link #getArrivalMarginHours() <em>Arrival Margin Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArrivalMarginHours()
	 * @generated
	 * @ordered
	 */
	protected static final int ARRIVAL_MARGIN_HOURS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getArrivalMarginHours() <em>Arrival Margin Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArrivalMarginHours()
	 * @generated
	 * @ordered
	 */
	protected int arrivalMarginHours = ARRIVAL_MARGIN_HOURS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVesselGroupCanalParameters() <em>Vessel Group Canal Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselGroupCanalParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselGroupCanalParameters> vesselGroupCanalParameters;

	/**
	 * The cached value of the '{@link #getPanamaSeasonalityRecords() <em>Panama Seasonality Records</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPanamaSeasonalityRecords()
	 * @generated
	 * @ordered
	 */
	protected EList<PanamaSeasonalityRecord> panamaSeasonalityRecords;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalBookingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CANAL_BOOKINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CanalBookingSlot> getCanalBookingSlots() {
		if (canalBookingSlots == null) {
			canalBookingSlots = new EObjectContainmentEList.Resolving<CanalBookingSlot>(CanalBookingSlot.class, this, CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);
		}
		return canalBookingSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getArrivalMarginHours() {
		return arrivalMarginHours;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setArrivalMarginHours(int newArrivalMarginHours) {
		int oldArrivalMarginHours = arrivalMarginHours;
		arrivalMarginHours = newArrivalMarginHours;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS, oldArrivalMarginHours, arrivalMarginHours));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselGroupCanalParameters> getVesselGroupCanalParameters() {
		if (vesselGroupCanalParameters == null) {
			vesselGroupCanalParameters = new EObjectContainmentEList.Resolving<VesselGroupCanalParameters>(VesselGroupCanalParameters.class, this, CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS);
		}
		return vesselGroupCanalParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PanamaSeasonalityRecord> getPanamaSeasonalityRecords() {
		if (panamaSeasonalityRecords == null) {
			panamaSeasonalityRecords = new EObjectContainmentEList.Resolving<PanamaSeasonalityRecord>(PanamaSeasonalityRecord.class, this, CargoPackage.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS);
		}
		return panamaSeasonalityRecords;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS:
				return ((InternalEList<?>)getCanalBookingSlots()).basicRemove(otherEnd, msgs);
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				return ((InternalEList<?>)getVesselGroupCanalParameters()).basicRemove(otherEnd, msgs);
			case CargoPackage.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS:
				return ((InternalEList<?>)getPanamaSeasonalityRecords()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS:
				return getCanalBookingSlots();
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				return getArrivalMarginHours();
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				return getVesselGroupCanalParameters();
			case CargoPackage.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS:
				return getPanamaSeasonalityRecords();
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
			case CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS:
				getCanalBookingSlots().clear();
				getCanalBookingSlots().addAll((Collection<? extends CanalBookingSlot>)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				setArrivalMarginHours((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				getVesselGroupCanalParameters().clear();
				getVesselGroupCanalParameters().addAll((Collection<? extends VesselGroupCanalParameters>)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS:
				getPanamaSeasonalityRecords().clear();
				getPanamaSeasonalityRecords().addAll((Collection<? extends PanamaSeasonalityRecord>)newValue);
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
			case CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS:
				getCanalBookingSlots().clear();
				return;
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				setArrivalMarginHours(ARRIVAL_MARGIN_HOURS_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				getVesselGroupCanalParameters().clear();
				return;
			case CargoPackage.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS:
				getPanamaSeasonalityRecords().clear();
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
			case CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS:
				return canalBookingSlots != null && !canalBookingSlots.isEmpty();
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				return arrivalMarginHours != ARRIVAL_MARGIN_HOURS_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				return vesselGroupCanalParameters != null && !vesselGroupCanalParameters.isEmpty();
			case CargoPackage.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS:
				return panamaSeasonalityRecords != null && !panamaSeasonalityRecords.isEmpty();
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
		result.append(" (arrivalMarginHours: ");
		result.append(arrivalMarginHours);
		result.append(')');
		return result.toString();
	}

} //CanalBookingsImpl
