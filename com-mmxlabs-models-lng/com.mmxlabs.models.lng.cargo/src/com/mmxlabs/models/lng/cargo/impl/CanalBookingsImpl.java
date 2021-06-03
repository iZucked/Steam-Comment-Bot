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
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getStrictBoundaryOffsetDays <em>Strict Boundary Offset Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getRelaxedBoundaryOffsetDays <em>Relaxed Boundary Offset Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getArrivalMarginHours <em>Arrival Margin Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getFlexibleBookingAmountNorthbound <em>Flexible Booking Amount Northbound</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getFlexibleBookingAmountSouthbound <em>Flexible Booking Amount Southbound</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getNorthboundMaxIdleDays <em>Northbound Max Idle Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getSouthboundMaxIdleDays <em>Southbound Max Idle Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getBookingExemptVessels <em>Booking Exempt Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl#getVesselGroupCanalParameters <em>Vessel Group Canal Parameters</em>}</li>
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
	 * The default value of the '{@link #getStrictBoundaryOffsetDays() <em>Strict Boundary Offset Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrictBoundaryOffsetDays()
	 * @generated
	 * @ordered
	 */
	protected static final int STRICT_BOUNDARY_OFFSET_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStrictBoundaryOffsetDays() <em>Strict Boundary Offset Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrictBoundaryOffsetDays()
	 * @generated
	 * @ordered
	 */
	protected int strictBoundaryOffsetDays = STRICT_BOUNDARY_OFFSET_DAYS_EDEFAULT;

	/**
	 * The default value of the '{@link #getRelaxedBoundaryOffsetDays() <em>Relaxed Boundary Offset Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelaxedBoundaryOffsetDays()
	 * @generated
	 * @ordered
	 */
	protected static final int RELAXED_BOUNDARY_OFFSET_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRelaxedBoundaryOffsetDays() <em>Relaxed Boundary Offset Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelaxedBoundaryOffsetDays()
	 * @generated
	 * @ordered
	 */
	protected int relaxedBoundaryOffsetDays = RELAXED_BOUNDARY_OFFSET_DAYS_EDEFAULT;

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
	 * The default value of the '{@link #getFlexibleBookingAmountNorthbound() <em>Flexible Booking Amount Northbound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlexibleBookingAmountNorthbound()
	 * @generated
	 * @ordered
	 */
	protected static final int FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFlexibleBookingAmountNorthbound() <em>Flexible Booking Amount Northbound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlexibleBookingAmountNorthbound()
	 * @generated
	 * @ordered
	 */
	protected int flexibleBookingAmountNorthbound = FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #getFlexibleBookingAmountSouthbound() <em>Flexible Booking Amount Southbound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlexibleBookingAmountSouthbound()
	 * @generated
	 * @ordered
	 */
	protected static final int FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFlexibleBookingAmountSouthbound() <em>Flexible Booking Amount Southbound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlexibleBookingAmountSouthbound()
	 * @generated
	 * @ordered
	 */
	protected int flexibleBookingAmountSouthbound = FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #getNorthboundMaxIdleDays() <em>Northbound Max Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNorthboundMaxIdleDays()
	 * @generated
	 * @ordered
	 */
	protected static final int NORTHBOUND_MAX_IDLE_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNorthboundMaxIdleDays() <em>Northbound Max Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNorthboundMaxIdleDays()
	 * @generated
	 * @ordered
	 */
	protected int northboundMaxIdleDays = NORTHBOUND_MAX_IDLE_DAYS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSouthboundMaxIdleDays() <em>Southbound Max Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSouthboundMaxIdleDays()
	 * @generated
	 * @ordered
	 */
	protected static final int SOUTHBOUND_MAX_IDLE_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSouthboundMaxIdleDays() <em>Southbound Max Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSouthboundMaxIdleDays()
	 * @generated
	 * @ordered
	 */
	protected int southboundMaxIdleDays = SOUTHBOUND_MAX_IDLE_DAYS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBookingExemptVessels() <em>Booking Exempt Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBookingExemptVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<Vessel> bookingExemptVessels;

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
	public int getStrictBoundaryOffsetDays() {
		return strictBoundaryOffsetDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStrictBoundaryOffsetDays(int newStrictBoundaryOffsetDays) {
		int oldStrictBoundaryOffsetDays = strictBoundaryOffsetDays;
		strictBoundaryOffsetDays = newStrictBoundaryOffsetDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS, oldStrictBoundaryOffsetDays, strictBoundaryOffsetDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getRelaxedBoundaryOffsetDays() {
		return relaxedBoundaryOffsetDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRelaxedBoundaryOffsetDays(int newRelaxedBoundaryOffsetDays) {
		int oldRelaxedBoundaryOffsetDays = relaxedBoundaryOffsetDays;
		relaxedBoundaryOffsetDays = newRelaxedBoundaryOffsetDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS, oldRelaxedBoundaryOffsetDays, relaxedBoundaryOffsetDays));
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
	public int getFlexibleBookingAmountNorthbound() {
		return flexibleBookingAmountNorthbound;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFlexibleBookingAmountNorthbound(int newFlexibleBookingAmountNorthbound) {
		int oldFlexibleBookingAmountNorthbound = flexibleBookingAmountNorthbound;
		flexibleBookingAmountNorthbound = newFlexibleBookingAmountNorthbound;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND, oldFlexibleBookingAmountNorthbound, flexibleBookingAmountNorthbound));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFlexibleBookingAmountSouthbound() {
		return flexibleBookingAmountSouthbound;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFlexibleBookingAmountSouthbound(int newFlexibleBookingAmountSouthbound) {
		int oldFlexibleBookingAmountSouthbound = flexibleBookingAmountSouthbound;
		flexibleBookingAmountSouthbound = newFlexibleBookingAmountSouthbound;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND, oldFlexibleBookingAmountSouthbound, flexibleBookingAmountSouthbound));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getNorthboundMaxIdleDays() {
		return northboundMaxIdleDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNorthboundMaxIdleDays(int newNorthboundMaxIdleDays) {
		int oldNorthboundMaxIdleDays = northboundMaxIdleDays;
		northboundMaxIdleDays = newNorthboundMaxIdleDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS, oldNorthboundMaxIdleDays, northboundMaxIdleDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSouthboundMaxIdleDays() {
		return southboundMaxIdleDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSouthboundMaxIdleDays(int newSouthboundMaxIdleDays) {
		int oldSouthboundMaxIdleDays = southboundMaxIdleDays;
		southboundMaxIdleDays = newSouthboundMaxIdleDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKINGS__SOUTHBOUND_MAX_IDLE_DAYS, oldSouthboundMaxIdleDays, southboundMaxIdleDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Vessel> getBookingExemptVessels() {
		if (bookingExemptVessels == null) {
			bookingExemptVessels = new EObjectResolvingEList<Vessel>(Vessel.class, this, CargoPackage.CANAL_BOOKINGS__BOOKING_EXEMPT_VESSELS);
		}
		return bookingExemptVessels;
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS:
				return ((InternalEList<?>)getCanalBookingSlots()).basicRemove(otherEnd, msgs);
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				return ((InternalEList<?>)getVesselGroupCanalParameters()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS:
				return getStrictBoundaryOffsetDays();
			case CargoPackage.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS:
				return getRelaxedBoundaryOffsetDays();
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				return getArrivalMarginHours();
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND:
				return getFlexibleBookingAmountNorthbound();
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND:
				return getFlexibleBookingAmountSouthbound();
			case CargoPackage.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS:
				return getNorthboundMaxIdleDays();
			case CargoPackage.CANAL_BOOKINGS__SOUTHBOUND_MAX_IDLE_DAYS:
				return getSouthboundMaxIdleDays();
			case CargoPackage.CANAL_BOOKINGS__BOOKING_EXEMPT_VESSELS:
				return getBookingExemptVessels();
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				return getVesselGroupCanalParameters();
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
			case CargoPackage.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS:
				setStrictBoundaryOffsetDays((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS:
				setRelaxedBoundaryOffsetDays((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				setArrivalMarginHours((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND:
				setFlexibleBookingAmountNorthbound((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND:
				setFlexibleBookingAmountSouthbound((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS:
				setNorthboundMaxIdleDays((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__SOUTHBOUND_MAX_IDLE_DAYS:
				setSouthboundMaxIdleDays((Integer)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__BOOKING_EXEMPT_VESSELS:
				getBookingExemptVessels().clear();
				getBookingExemptVessels().addAll((Collection<? extends Vessel>)newValue);
				return;
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				getVesselGroupCanalParameters().clear();
				getVesselGroupCanalParameters().addAll((Collection<? extends VesselGroupCanalParameters>)newValue);
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
			case CargoPackage.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS:
				setStrictBoundaryOffsetDays(STRICT_BOUNDARY_OFFSET_DAYS_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS:
				setRelaxedBoundaryOffsetDays(RELAXED_BOUNDARY_OFFSET_DAYS_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				setArrivalMarginHours(ARRIVAL_MARGIN_HOURS_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND:
				setFlexibleBookingAmountNorthbound(FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND:
				setFlexibleBookingAmountSouthbound(FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS:
				setNorthboundMaxIdleDays(NORTHBOUND_MAX_IDLE_DAYS_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__SOUTHBOUND_MAX_IDLE_DAYS:
				setSouthboundMaxIdleDays(SOUTHBOUND_MAX_IDLE_DAYS_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKINGS__BOOKING_EXEMPT_VESSELS:
				getBookingExemptVessels().clear();
				return;
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				getVesselGroupCanalParameters().clear();
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
			case CargoPackage.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS:
				return strictBoundaryOffsetDays != STRICT_BOUNDARY_OFFSET_DAYS_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS:
				return relaxedBoundaryOffsetDays != RELAXED_BOUNDARY_OFFSET_DAYS_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS:
				return arrivalMarginHours != ARRIVAL_MARGIN_HOURS_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND:
				return flexibleBookingAmountNorthbound != FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND:
				return flexibleBookingAmountSouthbound != FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS:
				return northboundMaxIdleDays != NORTHBOUND_MAX_IDLE_DAYS_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__SOUTHBOUND_MAX_IDLE_DAYS:
				return southboundMaxIdleDays != SOUTHBOUND_MAX_IDLE_DAYS_EDEFAULT;
			case CargoPackage.CANAL_BOOKINGS__BOOKING_EXEMPT_VESSELS:
				return bookingExemptVessels != null && !bookingExemptVessels.isEmpty();
			case CargoPackage.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS:
				return vesselGroupCanalParameters != null && !vesselGroupCanalParameters.isEmpty();
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
		result.append(" (strictBoundaryOffsetDays: ");
		result.append(strictBoundaryOffsetDays);
		result.append(", relaxedBoundaryOffsetDays: ");
		result.append(relaxedBoundaryOffsetDays);
		result.append(", arrivalMarginHours: ");
		result.append(arrivalMarginHours);
		result.append(", flexibleBookingAmountNorthbound: ");
		result.append(flexibleBookingAmountNorthbound);
		result.append(", flexibleBookingAmountSouthbound: ");
		result.append(flexibleBookingAmountSouthbound);
		result.append(", northboundMaxIdleDays: ");
		result.append(northboundMaxIdleDays);
		result.append(", southboundMaxIdleDays: ");
		result.append(southboundMaxIdleDays);
		result.append(')');
		return result.toString();
	}

} //CanalBookingsImpl
