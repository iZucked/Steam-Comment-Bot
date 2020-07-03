/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.mmxcore.MMXObject;
import java.time.LocalDate;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canal Booking Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getCanalEntrance <em>Canal Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getBookingDate <em>Booking Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getNotes <em>Notes</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot()
 * @model
 * @generated
 */
public interface CanalBookingSlot extends MMXObject {
	
	/**
	 * Returns the value of the '<em><b>Route Option</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Option</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #setRouteOption(RouteOption)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot_RouteOption()
	 * @model required="true"
	 * @generated
	 */
	RouteOption getRouteOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getRouteOption <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #getRouteOption()
	 * @generated
	 */
	void setRouteOption(RouteOption value);

	/**
	 * @generated NOT
	 */
	public static int BOOKING_HOURS_OFFSET = 3;
	
	/**
	 * Returns the value of the '<em><b>Booking Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Booking Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Booking Date</em>' attribute.
	 * @see #setBookingDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot_BookingDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate" required="true"
	 * @generated
	 */
	LocalDate getBookingDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getBookingDate <em>Booking Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Booking Date</em>' attribute.
	 * @see #getBookingDate()
	 * @generated
	 */
	void setBookingDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Canal Entrance</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.CanalEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Entrance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Entrance</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.CanalEntry
	 * @see #setCanalEntrance(CanalEntry)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot_CanalEntrance()
	 * @model
	 * @generated
	 */
	CanalEntry getCanalEntrance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getCanalEntrance <em>Canal Entrance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Entrance</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.CanalEntry
	 * @see #getCanalEntrance()
	 * @generated
	 */
	void setCanalEntrance(CanalEntry value);

	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot_Slot()
	 * @model
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot_Notes()
	 * @model
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

} // CanalBookingSlot
