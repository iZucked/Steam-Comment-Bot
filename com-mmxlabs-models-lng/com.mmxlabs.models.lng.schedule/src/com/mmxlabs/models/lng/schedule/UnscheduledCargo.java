/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unscheduled Cargo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getLoad <em>Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getDischarge <em>Discharge</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getUnscheduledCargo()
 * @model
 * @generated
 */
public interface UnscheduledCargo extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Load</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load</em>' reference.
	 * @see #setLoad(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getUnscheduledCargo_Load()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getLoad();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getLoad <em>Load</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load</em>' reference.
	 * @see #getLoad()
	 * @generated
	 */
	void setLoad(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Discharge</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge</em>' reference.
	 * @see #setDischarge(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getUnscheduledCargo_Discharge()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getDischarge();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getDischarge <em>Discharge</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge</em>' reference.
	 * @see #getDischarge()
	 * @generated
	 */
	void setDischarge(SlotAllocation value);

} // end of  UnscheduledCargo

// finish type fixing
