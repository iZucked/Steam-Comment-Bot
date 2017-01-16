/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.Slot;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot PNL Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotPNLDetails#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotPNLDetails#getGeneralPNLDetails <em>General PNL Details</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotPNLDetails()
 * @model
 * @generated
 */
public interface SlotPNLDetails extends GeneralPNLDetails {

	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotPNLDetails_Slot()
	 * @model
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotPNLDetails#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Returns the value of the '<em><b>General PNL Details</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.GeneralPNLDetails}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>General PNL Details</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>General PNL Details</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotPNLDetails_GeneralPNLDetails()
	 * @model containment="true"
	 * @generated
	 */
	EList<GeneralPNLDetails> getGeneralPNLDetails();
} // SlotPNLDetails
