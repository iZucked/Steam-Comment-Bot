/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.VersionRecord;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getBaseFuels <em>Base Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroups <em>Vessel Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getFleetVersionRecord <em>Fleet Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroupVersionRecord <em>Vessel Group Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getBunkerFuelsVersionRecord <em>Bunker Fuels Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FleetModel#getMMXVesselDBVersion <em>MMX Vessel DB Version</em>}</li>
 * </ul>
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
	 * Returns the value of the '<em><b>Fleet Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet Version Record</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet Version Record</em>' containment reference.
	 * @see #setFleetVersionRecord(VersionRecord)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_FleetVersionRecord()
	 * @model containment="true"
	 * @generated
	 */
	VersionRecord getFleetVersionRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FleetModel#getFleetVersionRecord <em>Fleet Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fleet Version Record</em>' containment reference.
	 * @see #getFleetVersionRecord()
	 * @generated
	 */
	void setFleetVersionRecord(VersionRecord value);

	/**
	 * Returns the value of the '<em><b>Vessel Group Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Group Version Record</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Group Version Record</em>' containment reference.
	 * @see #setVesselGroupVersionRecord(VersionRecord)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_VesselGroupVersionRecord()
	 * @model containment="true"
	 * @generated
	 */
	VersionRecord getVesselGroupVersionRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroupVersionRecord <em>Vessel Group Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Group Version Record</em>' containment reference.
	 * @see #getVesselGroupVersionRecord()
	 * @generated
	 */
	void setVesselGroupVersionRecord(VersionRecord value);

	/**
	 * Returns the value of the '<em><b>Bunker Fuels Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bunker Fuels Version Record</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bunker Fuels Version Record</em>' containment reference.
	 * @see #setBunkerFuelsVersionRecord(VersionRecord)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_BunkerFuelsVersionRecord()
	 * @model containment="true"
	 * @generated
	 */
	VersionRecord getBunkerFuelsVersionRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FleetModel#getBunkerFuelsVersionRecord <em>Bunker Fuels Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bunker Fuels Version Record</em>' containment reference.
	 * @see #getBunkerFuelsVersionRecord()
	 * @generated
	 */
	void setBunkerFuelsVersionRecord(VersionRecord value);

	/**
	 * Returns the value of the '<em><b>MMX Vessel DB Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>MMX Vessel DB Version</em>' attribute.
	 * @see #setMMXVesselDBVersion(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFleetModel_MMXVesselDBVersion()
	 * @model
	 * @generated
	 */
	String getMMXVesselDBVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FleetModel#getMMXVesselDBVersion <em>MMX Vessel DB Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>MMX Vessel DB Version</em>' attribute.
	 * @see #getMMXVesselDBVersion()
	 * @generated
	 */
	void setMMXVesselDBVersion(String value);

} // end of  FleetModel

// finish type fixing
