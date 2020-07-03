/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import java.time.LocalDate;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal Booking Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getCanalEntrance <em>Canal Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getBookingDate <em>Booking Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl#getNotes <em>Notes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CanalBookingSlotImpl extends MMXObjectImpl implements CanalBookingSlot {
	/**
	 * The default value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected static final RouteOption ROUTE_OPTION_EDEFAULT = RouteOption.DIRECT;

	/**
	 * The cached value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected RouteOption routeOption = ROUTE_OPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getCanalEntrance() <em>Canal Entrance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalEntrance()
	 * @generated
	 * @ordered
	 */
	protected static final CanalEntry CANAL_ENTRANCE_EDEFAULT = CanalEntry.NORTHSIDE;

	/**
	 * The cached value of the '{@link #getCanalEntrance() <em>Canal Entrance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalEntrance()
	 * @generated
	 * @ordered
	 */
	protected CanalEntry canalEntrance = CANAL_ENTRANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBookingDate() <em>Booking Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBookingDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate BOOKING_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBookingDate() <em>Booking Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBookingDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate bookingDate = BOOKING_DATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot slot;

	/**
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected String notes = NOTES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalBookingSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CANAL_BOOKING_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RouteOption getRouteOption() {
		return routeOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRouteOption(RouteOption newRouteOption) {
		RouteOption oldRouteOption = routeOption;
		routeOption = newRouteOption == null ? ROUTE_OPTION_EDEFAULT : newRouteOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__ROUTE_OPTION, oldRouteOption, routeOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getBookingDate() {
		return bookingDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBookingDate(LocalDate newBookingDate) {
		LocalDate oldBookingDate = bookingDate;
		bookingDate = newBookingDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE, oldBookingDate, bookingDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalEntry getCanalEntrance() {
		return canalEntrance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalEntrance(CanalEntry newCanalEntrance) {
		CanalEntry oldCanalEntrance = canalEntrance;
		canalEntrance = newCanalEntrance == null ? CANAL_ENTRANCE_EDEFAULT : newCanalEntrance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__CANAL_ENTRANCE, oldCanalEntrance, canalEntrance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Slot getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Slot)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CANAL_BOOKING_SLOT__SLOT, oldSlot, slot));
			}
		}
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetSlot() {
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSlot(Slot newSlot) {
		Slot oldSlot = slot;
		slot = newSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__SLOT, oldSlot, slot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNotes(String newNotes) {
		String oldNotes = notes;
		notes = newNotes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CANAL_BOOKING_SLOT__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE_OPTION:
				return getRouteOption();
			case CargoPackage.CANAL_BOOKING_SLOT__CANAL_ENTRANCE:
				return getCanalEntrance();
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				return getBookingDate();
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case CargoPackage.CANAL_BOOKING_SLOT__NOTES:
				return getNotes();
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
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE_OPTION:
				setRouteOption((RouteOption)newValue);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__CANAL_ENTRANCE:
				setCanalEntrance((CanalEntry)newValue);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				setBookingDate((LocalDate)newValue);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				setSlot((Slot)newValue);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__NOTES:
				setNotes((String)newValue);
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
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE_OPTION:
				setRouteOption(ROUTE_OPTION_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__CANAL_ENTRANCE:
				setCanalEntrance(CANAL_ENTRANCE_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				setBookingDate(BOOKING_DATE_EDEFAULT);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				setSlot((Slot)null);
				return;
			case CargoPackage.CANAL_BOOKING_SLOT__NOTES:
				setNotes(NOTES_EDEFAULT);
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
			case CargoPackage.CANAL_BOOKING_SLOT__ROUTE_OPTION:
				return routeOption != ROUTE_OPTION_EDEFAULT;
			case CargoPackage.CANAL_BOOKING_SLOT__CANAL_ENTRANCE:
				return canalEntrance != CANAL_ENTRANCE_EDEFAULT;
			case CargoPackage.CANAL_BOOKING_SLOT__BOOKING_DATE:
				return BOOKING_DATE_EDEFAULT == null ? bookingDate != null : !BOOKING_DATE_EDEFAULT.equals(bookingDate);
			case CargoPackage.CANAL_BOOKING_SLOT__SLOT:
				return slot != null;
			case CargoPackage.CANAL_BOOKING_SLOT__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
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
		result.append(" (routeOption: ");
		result.append(routeOption);
		result.append(", canalEntrance: ");
		result.append(canalEntrance);
		result.append(", bookingDate: ");
		result.append(bookingDate);
		result.append(", notes: ");
		result.append(notes);
		result.append(')');
		return result.toString();
	}

} //CanalBookingSlotImpl
