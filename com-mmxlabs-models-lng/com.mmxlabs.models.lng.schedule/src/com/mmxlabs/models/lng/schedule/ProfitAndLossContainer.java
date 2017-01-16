/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profit And Loss Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGeneralPNLDetails <em>General PNL Details</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getProfitAndLossContainer()
 * @model
 * @generated
 */
public interface ProfitAndLossContainer extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Profit And Loss</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Profit And Loss</em>' containment reference.
	 * @see #setGroupProfitAndLoss(GroupProfitAndLoss)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getProfitAndLossContainer_GroupProfitAndLoss()
	 * @model containment="true"
	 * @generated
	 */
	GroupProfitAndLoss getGroupProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGroupProfitAndLoss <em>Group Profit And Loss</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Profit And Loss</em>' containment reference.
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 */
	void setGroupProfitAndLoss(GroupProfitAndLoss value);

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
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getProfitAndLossContainer_GeneralPNLDetails()
	 * @model containment="true"
	 * @generated
	 */
	EList<GeneralPNLDetails> getGeneralPNLDetails();

} // ProfitAndLossContainer
