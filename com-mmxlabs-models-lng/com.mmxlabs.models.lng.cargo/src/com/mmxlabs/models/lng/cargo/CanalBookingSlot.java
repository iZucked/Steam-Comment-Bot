/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Route;

import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canal Booking Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getEntryPoint <em>Entry Point</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getBookingDate <em>Booking Date</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot()
 * @model
 * @generated
 */
public interface CanalBookingSlot extends MMXObject, ITimezoneProvider {
	/**
	 * Returns the value of the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route</em>' reference.
	 * @see #setRoute(Route)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot_Route()
	 * @model required="true"
	 * @generated
	 */
	Route getRoute();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getRoute <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' reference.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(Route value);

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
	 * Returns the value of the '<em><b>Entry Point</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entry Point</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entry Point</em>' reference.
	 * @see #setEntryPoint(EntryPoint)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookingSlot_EntryPoint()
	 * @model required="true"
	 * @generated
	 */
	EntryPoint getEntryPoint();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getEntryPoint <em>Entry Point</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entry Point</em>' reference.
	 * @see #getEntryPoint()
	 * @generated
	 */
	void setEntryPoint(EntryPoint value);

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getBookingDateAsDateTime();

} // CanalBookingSlot
