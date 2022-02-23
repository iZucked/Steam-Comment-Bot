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
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getArrivalMarginHours <em>Arrival Margin Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getVesselGroupCanalParameters <em>Vessel Group Canal Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CanalBookings#getPanamaSeasonalityRecords <em>Panama Seasonality Records</em>}</li>
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
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hours' formatString='##,##0'"
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

	/**
	 * Returns the value of the '<em><b>Panama Seasonality Records</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Panama Seasonality Records</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCanalBookings_PanamaSeasonalityRecords()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<PanamaSeasonalityRecord> getPanamaSeasonalityRecords();

} // CanalBookings
