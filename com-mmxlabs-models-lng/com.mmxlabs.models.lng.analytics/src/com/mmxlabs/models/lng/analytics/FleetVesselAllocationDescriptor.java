/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fleet Vessel Allocation Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getCharterIndex <em>Charter Index</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getFleetVesselAllocationDescriptor()
 * @model
 * @generated
 */
public interface FleetVesselAllocationDescriptor extends VesselAllocationDescriptor {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getFleetVesselAllocationDescriptor_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Charter Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Index</em>' attribute.
	 * @see #setCharterIndex(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getFleetVesselAllocationDescriptor_CharterIndex()
	 * @model
	 * @generated
	 */
	int getCharterIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.FleetVesselAllocationDescriptor#getCharterIndex <em>Charter Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Index</em>' attribute.
	 * @see #getCharterIndex()
	 * @generated
	 */
	void setCharterIndex(int value);

} // FleetVesselAllocationDescriptor
