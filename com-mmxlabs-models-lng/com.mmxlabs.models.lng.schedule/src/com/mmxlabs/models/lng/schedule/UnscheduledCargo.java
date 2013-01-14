/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unscheduled Cargo</b></em>'.
 * @deprecated
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getUnscheduledCargo()
 * @model
 * @generated
 */
public interface UnscheduledCargo extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Allocation</em>' reference.
	 * @see #setLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getUnscheduledCargo_LoadAllocation()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getLoadAllocation <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Allocation</em>' reference.
	 * @see #getLoadAllocation()
	 * @generated
	 */
	void setLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Allocation</em>' reference.
	 * @see #setDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getUnscheduledCargo_DischargeAllocation()
	 * @model required="true"
	 * @generated
	 */
	SlotAllocation getDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getDischargeAllocation <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Allocation</em>' reference.
	 * @see #getDischargeAllocation()
	 * @generated
	 */
	void setDischargeAllocation(SlotAllocation value);

} // end of  UnscheduledCargo

// finish type fixing
