/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schedule Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.ScheduleSpecification#getVesselScheduleSpecifications <em>Vessel Schedule Specifications</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.ScheduleSpecification#getNonShippedCargoSpecifications <em>Non Shipped Cargo Specifications</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.ScheduleSpecification#getOpenEvents <em>Open Events</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getScheduleSpecification()
 * @model
 * @generated
 */
public interface ScheduleSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel Schedule Specifications</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Schedule Specifications</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Schedule Specifications</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getScheduleSpecification_VesselScheduleSpecifications()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselScheduleSpecification> getVesselScheduleSpecifications();

	/**
	 * Returns the value of the '<em><b>Non Shipped Cargo Specifications</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Non Shipped Cargo Specifications</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Non Shipped Cargo Specifications</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getScheduleSpecification_NonShippedCargoSpecifications()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<NonShippedCargoSpecification> getNonShippedCargoSpecifications();

	/**
	 * Returns the value of the '<em><b>Open Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getScheduleSpecification_OpenEvents()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<ScheduleSpecificationEvent> getOpenEvents();

} // ScheduleSpecification
