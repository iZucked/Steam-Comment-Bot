/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CargoChange#getSlotDescriptors <em>Slot Descriptors</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CargoChange#getVesselAllocation <em>Vessel Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CargoChange#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoChange()
 * @model
 * @generated
 */
public interface CargoChange extends Change {
	/**
	 * Returns the value of the '<em><b>Slot Descriptors</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.SlotDescriptor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Descriptors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Descriptors</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoChange_SlotDescriptors()
	 * @model containment="true"
	 * @generated
	 */
	EList<SlotDescriptor> getSlotDescriptors();

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoChange_VesselAllocation()
	 * @model containment="true"
	 * @generated
	 */
	VesselAllocationDescriptor getVesselAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CargoChange#getVesselAllocation <em>Vessel Allocation</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoChange_Position()
	 * @model containment="true"
	 * @generated
	 */
	PositionDescriptor getPosition();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CargoChange#getPosition <em>Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position</em>' containment reference.
	 * @see #getPosition()
	 * @generated
	 */
	void setPosition(PositionDescriptor value);

} // CargoChange
