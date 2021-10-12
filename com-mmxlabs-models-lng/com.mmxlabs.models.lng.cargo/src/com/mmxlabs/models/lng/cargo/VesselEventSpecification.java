/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import java.time.LocalDateTime;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselEventSpecification#getVesselEvent <em>Vessel Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselEventSpecification#getArrivalDate <em>Arrival Date</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEventSpecification()
 * @model
 * @generated
 */
public interface VesselEventSpecification extends ScheduleSpecificationEvent {
	/**
	 * Returns the value of the '<em><b>Vessel Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Event</em>' reference.
	 * @see #setVesselEvent(VesselEvent)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEventSpecification_VesselEvent()
	 * @model
	 * @generated
	 */
	VesselEvent getVesselEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselEventSpecification#getVesselEvent <em>Vessel Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Event</em>' reference.
	 * @see #getVesselEvent()
	 * @generated
	 */
	void setVesselEvent(VesselEvent value);

	/**
	 * Returns the value of the '<em><b>Arrival Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arrival Date</em>' attribute.
	 * @see #setArrivalDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselEventSpecification_ArrivalDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getArrivalDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselEventSpecification#getArrivalDate <em>Arrival Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arrival Date</em>' attribute.
	 * @see #getArrivalDate()
	 * @generated
	 */
	void setArrivalDate(LocalDateTime value);

} // VesselEventSpecification
