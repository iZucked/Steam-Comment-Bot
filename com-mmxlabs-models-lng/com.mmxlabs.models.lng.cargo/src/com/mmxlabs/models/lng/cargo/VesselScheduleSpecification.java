/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.types.VesselAssignmentType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Schedule Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getVesselAllocation <em>Vessel Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getEvents <em>Events</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselScheduleSpecification()
 * @model
 * @generated
 */
public interface VesselScheduleSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Allocation</em>' reference.
	 * @see #setVesselAllocation(VesselAssignmentType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselScheduleSpecification_VesselAllocation()
	 * @model
	 * @generated
	 */
	VesselAssignmentType getVesselAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getVesselAllocation <em>Vessel Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Allocation</em>' reference.
	 * @see #getVesselAllocation()
	 * @generated
	 */
	void setVesselAllocation(VesselAssignmentType value);

	/**
	 * Returns the value of the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Index</em>' attribute.
	 * @see #setSpotIndex(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselScheduleSpecification_SpotIndex()
	 * @model
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselScheduleSpecification_Events()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<ScheduleSpecificationEvent> getEvents();

} // VesselScheduleSpecification
