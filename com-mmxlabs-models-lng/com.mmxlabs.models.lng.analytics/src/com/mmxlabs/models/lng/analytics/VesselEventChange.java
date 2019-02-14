/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselEventDescriptor <em>Vessel Event Descriptor</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselAllocation <em>Vessel Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVesselEventChange()
 * @model
 * @generated
 */
public interface VesselEventChange extends Change {
	/**
	 * Returns the value of the '<em><b>Vessel Event Descriptor</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Event Descriptor</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Event Descriptor</em>' containment reference.
	 * @see #setVesselEventDescriptor(VesselEventDescriptor)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVesselEventChange_VesselEventDescriptor()
	 * @model containment="true"
	 * @generated
	 */
	VesselEventDescriptor getVesselEventDescriptor();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselEventDescriptor <em>Vessel Event Descriptor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Event Descriptor</em>' containment reference.
	 * @see #getVesselEventDescriptor()
	 * @generated
	 */
	void setVesselEventDescriptor(VesselEventDescriptor value);

	/**
	 * Returns the value of the '<em><b>Vessel Allocation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Allocation</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Allocation</em>' containment reference.
	 * @see #setVesselAllocation(VesselAllocationDescriptor)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVesselEventChange_VesselAllocation()
	 * @model containment="true"
	 * @generated
	 */
	VesselAllocationDescriptor getVesselAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getVesselAllocation <em>Vessel Allocation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Allocation</em>' containment reference.
	 * @see #getVesselAllocation()
	 * @generated
	 */
	void setVesselAllocation(VesselAllocationDescriptor value);

	/**
	 * Returns the value of the '<em><b>Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Position</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Position</em>' containment reference.
	 * @see #setPosition(PositionDescriptor)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVesselEventChange_Position()
	 * @model containment="true"
	 * @generated
	 */
	PositionDescriptor getPosition();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.VesselEventChange#getPosition <em>Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position</em>' containment reference.
	 * @see #getPosition()
	 * @generated
	 */
	void setPosition(PositionDescriptor value);

} // VesselEventChange
