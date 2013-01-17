/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getBaseFuels <em>Base Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroups <em>Vessel Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getSpecialVesselGroups <em>Special Vessel Groups</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel()
 * @model
 * @generated
 */
public interface FleetModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_Vessels()
	 * @model containment="true"
	 * @generated
	 */
	EList<Vessel> getVessels();

	/**
	 * Returns the value of the '<em><b>Vessel Classes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Classes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Classes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_VesselClasses()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselClass> getVesselClasses();

	/**
	 * Returns the value of the '<em><b>Vessel Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_VesselEvents()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselEvent> getVesselEvents();

	/**
	 * Returns the value of the '<em><b>Base Fuels</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.BaseFuel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuels</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuels</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_BaseFuels()
	 * @model containment="true"
	 * @generated
	 */
	EList<BaseFuel> getBaseFuels();

	/**
	 * Returns the value of the '<em><b>Vessel Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_VesselGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselGroup> getVesselGroups();

	/**
	 * Returns the value of the '<em><b>Special Vessel Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselTypeGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Special Vessel Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Special Vessel Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_SpecialVesselGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselTypeGroup> getSpecialVesselGroups();

} // end of  FleetModel

// finish type fixing
