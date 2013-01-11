/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ScheduleModel#getInitialSchedule <em>Initial Schedule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ScheduleModel#getOptimisedSchedule <em>Optimised Schedule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ScheduleModel#isDirty <em>Dirty</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getScheduleModel()
 * @model
 * @generated
 */
public interface ScheduleModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Initial Schedule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Schedule</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Schedule</em>' containment reference.
	 * @see #setInitialSchedule(Schedule)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getScheduleModel_InitialSchedule()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Schedule getInitialSchedule();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ScheduleModel#getInitialSchedule <em>Initial Schedule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Schedule</em>' containment reference.
	 * @see #getInitialSchedule()
	 * @generated
	 */
	void setInitialSchedule(Schedule value);

	/**
	 * Returns the value of the '<em><b>Optimised Schedule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optimised Schedule</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * @deprecated
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optimised Schedule</em>' containment reference.
	 * @see #setOptimisedSchedule(Schedule)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getScheduleModel_OptimisedSchedule()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Schedule getOptimisedSchedule();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ScheduleModel#getOptimisedSchedule <em>Optimised Schedule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @deprecated
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optimised Schedule</em>' containment reference.
	 * @see #getOptimisedSchedule()
	 * @generated
	 */
	void setOptimisedSchedule(Schedule value);

	/**
	 * Returns the value of the '<em><b>Dirty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dirty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dirty</em>' attribute.
	 * @see #setDirty(boolean)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getScheduleModel_Dirty()
	 * @model required="true"
	 * @generated
	 */
	boolean isDirty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ScheduleModel#isDirty <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dirty</em>' attribute.
	 * @see #isDirty()
	 * @generated
	 */
	void setDirty(boolean value);

} // end of  ScheduleModel

// finish type fixing
