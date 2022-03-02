/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.lng.cargo.PaperDeal;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Paper Deal Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocation#getPaperDeal <em>Paper Deal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PaperDealAllocation#getEntries <em>Entries</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocation()
 * @model
 * @generated
 */
public interface PaperDealAllocation extends ProfitAndLossContainer {
	/**
	 * Returns the value of the '<em><b>Paper Deal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Paper Deal</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Paper Deal</em>' reference.
	 * @see #setPaperDeal(PaperDeal)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocation_PaperDeal()
	 * @model
	 * @generated
	 */
	PaperDeal getPaperDeal();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PaperDealAllocation#getPaperDeal <em>Paper Deal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Paper Deal</em>' reference.
	 * @see #getPaperDeal()
	 * @generated
	 */
	void setPaperDeal(PaperDeal value);

	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPaperDealAllocation_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<PaperDealAllocationEntry> getEntries();

} // PaperDealAllocation
