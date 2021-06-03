/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canal Bookings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getCanalBookingSlots <em>Canal Booking Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getStrictBoundaryOffsetDays <em>Strict Boundary Offset Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getRelaxedBoundaryOffsetDays <em>Relaxed Boundary Offset Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getArrivalMarginHours <em>Arrival Margin Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountNorthbound <em>Flexible Booking Amount Northbound</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountSouthbound <em>Flexible Booking Amount Southbound</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getNorthboundMaxIdleDays <em>Northbound Max Idle Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getSouthboundMaxIdleDays <em>Southbound Max Idle Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getBookingExemptVessels <em>Booking Exempt Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getVesselGroupCanalParameters <em>Vessel Group Canal Parameters</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings()
 * @model
 * @generated
 */
public interface CanalBookings extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Canal Booking Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.CanalBookingSlot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Booking Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Booking Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_CanalBookingSlots()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<CanalBookingSlot> getCanalBookingSlots();

	/**
	 * Returns the value of the '<em><b>Strict Boundary Offset Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Strict Boundary Offset Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Strict Boundary Offset Days</em>' attribute.
	 * @see #setStrictBoundaryOffsetDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_StrictBoundaryOffsetDays()
	 * @model
	 * @generated
	 */
	int getStrictBoundaryOffsetDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getStrictBoundaryOffsetDays <em>Strict Boundary Offset Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Strict Boundary Offset Days</em>' attribute.
	 * @see #getStrictBoundaryOffsetDays()
	 * @generated
	 */
	void setStrictBoundaryOffsetDays(int value);

	/**
	 * Returns the value of the '<em><b>Relaxed Boundary Offset Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relaxed Boundary Offset Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relaxed Boundary Offset Days</em>' attribute.
	 * @see #setRelaxedBoundaryOffsetDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_RelaxedBoundaryOffsetDays()
	 * @model
	 * @generated
	 */
	int getRelaxedBoundaryOffsetDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getRelaxedBoundaryOffsetDays <em>Relaxed Boundary Offset Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relaxed Boundary Offset Days</em>' attribute.
	 * @see #getRelaxedBoundaryOffsetDays()
	 * @generated
	 */
	void setRelaxedBoundaryOffsetDays(int value);

	/**
	 * Returns the value of the '<em><b>Arrival Margin Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arrival Margin Hours</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Number of hours arrival margin prior Panama booking date or expected crossing time.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Arrival Margin Hours</em>' attribute.
	 * @see #setArrivalMarginHours(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_ArrivalMarginHours()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hours'"
	 * @generated
	 */
	int getArrivalMarginHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getArrivalMarginHours <em>Arrival Margin Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arrival Margin Hours</em>' attribute.
	 * @see #getArrivalMarginHours()
	 * @generated
	 */
	void setArrivalMarginHours(int value);

	/**
	 * Returns the value of the '<em><b>Flexible Booking Amount Northbound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flexible Booking Amount Northbound</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flexible Booking Amount Northbound</em>' attribute.
	 * @see #setFlexibleBookingAmountNorthbound(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_FlexibleBookingAmountNorthbound()
	 * @model
	 * @generated
	 */
	int getFlexibleBookingAmountNorthbound();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountNorthbound <em>Flexible Booking Amount Northbound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flexible Booking Amount Northbound</em>' attribute.
	 * @see #getFlexibleBookingAmountNorthbound()
	 * @generated
	 */
	void setFlexibleBookingAmountNorthbound(int value);

	/**
	 * Returns the value of the '<em><b>Flexible Booking Amount Southbound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flexible Booking Amount Southbound</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flexible Booking Amount Southbound</em>' attribute.
	 * @see #setFlexibleBookingAmountSouthbound(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_FlexibleBookingAmountSouthbound()
	 * @model
	 * @generated
	 */
	int getFlexibleBookingAmountSouthbound();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountSouthbound <em>Flexible Booking Amount Southbound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flexible Booking Amount Southbound</em>' attribute.
	 * @see #getFlexibleBookingAmountSouthbound()
	 * @generated
	 */
	void setFlexibleBookingAmountSouthbound(int value);

	/**
	 * Returns the value of the '<em><b>Northbound Max Idle Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Northbound Max Idle Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Northbound Max Idle Days</em>' attribute.
	 * @see #setNorthboundMaxIdleDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_NorthboundMaxIdleDays()
	 * @model
	 * @generated
	 */
	int getNorthboundMaxIdleDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getNorthboundMaxIdleDays <em>Northbound Max Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Northbound Max Idle Days</em>' attribute.
	 * @see #getNorthboundMaxIdleDays()
	 * @generated
	 */
	void setNorthboundMaxIdleDays(int value);

	/**
	 * Returns the value of the '<em><b>Southbound Max Idle Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Southbound Max Idle Days</em>' attribute.
	 * @see #setSouthboundMaxIdleDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_SouthboundMaxIdleDays()
	 * @model
	 * @generated
	 */
	int getSouthboundMaxIdleDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getSouthboundMaxIdleDays <em>Southbound Max Idle Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Southbound Max Idle Days</em>' attribute.
	 * @see #getSouthboundMaxIdleDays()
	 * @generated
	 */
	void setSouthboundMaxIdleDays(int value);

	/**
	 * Returns the value of the '<em><b>Booking Exempt Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Booking Exempt Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_BookingExemptVessels()
	 * @model
	 * @generated
	 */
	EList<Vessel> getBookingExemptVessels();

	/**
	 * Returns the value of the '<em><b>Vessel Group Canal Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Group Canal Parameters</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_VesselGroupCanalParameters()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselGroupCanalParameters> getVesselGroupCanalParameters();

} // CanalBookings
