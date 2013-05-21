/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schedule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Schedule#getSequences <em>Sequences</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Schedule#getCargoAllocations <em>Cargo Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Schedule#getSlotAllocations <em>Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Schedule#getFitnesses <em>Fitnesses</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Schedule#getUnusedElements <em>Unused Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSchedule()
 * @model
 * @generated
 */
public interface Schedule extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Sequences</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.Sequence}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequences</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequences</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSchedule_Sequences()
	 * @model containment="true"
	 * @generated
	 */
	EList<Sequence> getSequences();

	/**
	 * Returns the value of the '<em><b>Cargo Allocations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.CargoAllocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Allocations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSchedule_CargoAllocations()
	 * @model containment="true"
	 * @generated
	 */
	EList<CargoAllocation> getCargoAllocations();

	/**
	 * Returns the value of the '<em><b>Slot Allocations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.SlotAllocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Allocations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Allocations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSchedule_SlotAllocations()
	 * @model containment="true"
	 * @generated
	 */
	EList<SlotAllocation> getSlotAllocations();

	/**
	 * Returns the value of the '<em><b>Fitnesses</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.Fitness}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fitnesses</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fitnesses</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSchedule_Fitnesses()
	 * @model containment="true"
	 * @generated
	 */
	EList<Fitness> getFitnesses();

	/**
	 * Returns the value of the '<em><b>Unused Elements</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unused Elements</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unused Elements</em>' reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSchedule_UnusedElements()
	 * @model transient="true"
	 * @generated
	 */
	EList<EObject> getUnusedElements();

} // end of  Schedule

// finish type fixing
