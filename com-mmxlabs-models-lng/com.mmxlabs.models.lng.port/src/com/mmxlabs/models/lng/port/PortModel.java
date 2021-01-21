/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

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
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getPortGroups <em>Port Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getRoutes <em>Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getSpecialPortGroups <em>Special Port Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getPortCountryGroups <em>Port Country Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getContingencyMatrix <em>Contingency Matrix</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getPortVersionRecord <em>Port Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getPortGroupVersionRecord <em>Port Group Version Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.PortModel#getDistanceVersionRecord <em>Distance Version Record</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel()
 * @model
 * @generated
 */
public interface PortModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.Port}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_Ports()
	 * @model containment="true"
	 * @generated
	 */
	EList<Port> getPorts();

	/**
	 * Returns the value of the '<em><b>Port Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.PortGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_PortGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortGroup> getPortGroups();

	/**
	 * Returns the value of the '<em><b>Routes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.Route}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Routes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Routes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_Routes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Route> getRoutes();

	/**
	 * Returns the value of the '<em><b>Special Port Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.CapabilityGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Special Port Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Special Port Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_SpecialPortGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<CapabilityGroup> getSpecialPortGroups();

	/**
	 * Returns the value of the '<em><b>Port Country Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.PortCountryGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Country Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Country Groups</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_PortCountryGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortCountryGroup> getPortCountryGroups();

	/**
	 * Returns the value of the '<em><b>Contingency Matrix</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contingency Matrix</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contingency Matrix</em>' containment reference.
	 * @see #setContingencyMatrix(ContingencyMatrix)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_ContingencyMatrix()
	 * @model containment="true"
	 * @generated
	 */
	ContingencyMatrix getContingencyMatrix();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.PortModel#getContingencyMatrix <em>Contingency Matrix</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contingency Matrix</em>' containment reference.
	 * @see #getContingencyMatrix()
	 * @generated
	 */
	void setContingencyMatrix(ContingencyMatrix value);

	/**
	 * Returns the value of the '<em><b>Port Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Version Record</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Version Record</em>' containment reference.
	 * @see #setPortVersionRecord(VersionRecord)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_PortVersionRecord()
	 * @model containment="true"
	 * @generated
	 */
	VersionRecord getPortVersionRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.PortModel#getPortVersionRecord <em>Port Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Version Record</em>' containment reference.
	 * @see #getPortVersionRecord()
	 * @generated
	 */
	void setPortVersionRecord(VersionRecord value);

	/**
	 * Returns the value of the '<em><b>Port Group Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Group Version Record</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Group Version Record</em>' containment reference.
	 * @see #setPortGroupVersionRecord(VersionRecord)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_PortGroupVersionRecord()
	 * @model containment="true"
	 * @generated
	 */
	VersionRecord getPortGroupVersionRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.PortModel#getPortGroupVersionRecord <em>Port Group Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Group Version Record</em>' containment reference.
	 * @see #getPortGroupVersionRecord()
	 * @generated
	 */
	void setPortGroupVersionRecord(VersionRecord value);

	/**
	 * Returns the value of the '<em><b>Distance Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance Version Record</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance Version Record</em>' containment reference.
	 * @see #setDistanceVersionRecord(VersionRecord)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortModel_DistanceVersionRecord()
	 * @model containment="true"
	 * @generated
	 */
	VersionRecord getDistanceVersionRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.PortModel#getDistanceVersionRecord <em>Distance Version Record</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance Version Record</em>' containment reference.
	 * @see #getDistanceVersionRecord()
	 * @generated
	 */
	void setDistanceVersionRecord(VersionRecord value);

} // PortModel
