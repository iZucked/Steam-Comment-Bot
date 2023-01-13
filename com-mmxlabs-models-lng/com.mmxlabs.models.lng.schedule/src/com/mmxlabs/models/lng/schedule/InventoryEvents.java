/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.cargo.Inventory;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Events</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryEvents#getFacility <em>Facility</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.InventoryEvents#getEvents <em>Events</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryEvents()
 * @model
 * @generated
 */
public interface InventoryEvents extends EObject {
	/**
	 * Returns the value of the '<em><b>Facility</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Facility</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Facility</em>' reference.
	 * @see #setFacility(Inventory)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryEvents_Facility()
	 * @model
	 * @generated
	 */
	Inventory getFacility();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.InventoryEvents#getFacility <em>Facility</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Facility</em>' reference.
	 * @see #getFacility()
	 * @generated
	 */
	void setFacility(Inventory value);

	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.InventoryChangeEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getInventoryEvents_Events()
	 * @model containment="true"
	 * @generated
	 */
	EList<InventoryChangeEvent> getEvents();

} // InventoryEvents
