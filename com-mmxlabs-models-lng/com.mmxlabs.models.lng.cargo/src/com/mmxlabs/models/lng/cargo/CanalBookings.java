/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

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
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleSlotAmount <em>Flexible Slot Amount</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings()
 * @model
 * @generated
 */
public interface CanalBookings extends EObject {
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
	 * Returns the value of the '<em><b>Flexible Slot Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flexible Slot Amount</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flexible Slot Amount</em>' attribute.
	 * @see #setFlexibleSlotAmount(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_FlexibleSlotAmount()
	 * @model
	 * @generated
	 */
	int getFlexibleSlotAmount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleSlotAmount <em>Flexible Slot Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flexible Slot Amount</em>' attribute.
	 * @see #getFlexibleSlotAmount()
	 * @generated
	 */
	void setFlexibleSlotAmount(int value);

} // CanalBookings
