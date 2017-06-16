/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getLoadSlots <em>Load Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getDischargeSlots <em>Discharge Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargoes <em>Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargoGroups <em>Cargo Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getVesselAvailabilities <em>Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getVesselTypeGroups <em>Vessel Type Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getInventoryModels <em>Inventory Models</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoModel#getCanalBookings <em>Canal Bookings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel()
 * @model
 * @generated
 */
public interface CargoModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Load Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.LoadSlot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_LoadSlots()
	 * @model containment="true" resolveProxies="true"
	 *        annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUniqueChildren='true'"
	 * @generated
	 */
	EList<LoadSlot> getLoadSlots();

	/**
	 * Returns the value of the '<em><b>Discharge Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.DischargeSlot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_DischargeSlots()
	 * @model containment="true" resolveProxies="true"
	 *        annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUniqueChildren='true'"
	 * @generated
	 */
	EList<DischargeSlot> getDischargeSlots();

	/**
	 * Returns the value of the '<em><b>Cargoes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Cargo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargoes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargoes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_Cargoes()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Cargo> getCargoes();

	/**
	 * Returns the value of the '<em><b>Cargo Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.CargoGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_CargoGroups()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<CargoGroup> getCargoGroups();

	/**
	 * Returns the value of the '<em><b>Vessel Availabilities</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselAvailability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availabilities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Availabilities</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_VesselAvailabilities()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselAvailability> getVesselAvailabilities();

	/**
	 * Returns the value of the '<em><b>Vessel Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_VesselEvents()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselEvent> getVesselEvents();

	/**
	 * Returns the value of the '<em><b>Vessel Type Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselTypeGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Type Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Type Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_VesselTypeGroups()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselTypeGroup> getVesselTypeGroups();

	/**
	 * Returns the value of the '<em><b>Inventory Models</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Inventory}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inventory Models</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inventory Models</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_InventoryModels()
	 * @model
	 * @generated
	 */
	EList<Inventory> getInventoryModels();

	/**
	 * Returns the value of the '<em><b>Canal Bookings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Bookings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Bookings</em>' containment reference.
	 * @see #setCanalBookings(CanalBookings)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoModel_CanalBookings()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	CanalBookings getCanalBookings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCanalBookings <em>Canal Bookings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Bookings</em>' containment reference.
	 * @see #getCanalBookings()
	 * @generated
	 */
	void setCanalBookings(CanalBookings value);

} // end of  CargoModel

// finish type fixing
