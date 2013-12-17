/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profit And Loss Container</b></em>'.
 * @since 4.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGroupProfitAndLossNoTimeCharter <em>Group Profit And Loss No Time Charter</em>}</li>
 * </ul>
 * </p>
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
	 * Returns the value of the '<em><b>Group Profit And Loss No Time Charter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Profit And Loss No Time Charter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Profit And Loss No Time Charter</em>' containment reference.
	 * @see #setGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getProfitAndLossContainer_GroupProfitAndLossNoTimeCharter()
	 * @model containment="true"
	 * @generated
	 */
	GroupProfitAndLoss getGroupProfitAndLossNoTimeCharter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGroupProfitAndLossNoTimeCharter <em>Group Profit And Loss No Time Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Profit And Loss No Time Charter</em>' containment reference.
	 * @see #getGroupProfitAndLossNoTimeCharter()
	 * @generated
	 */
	void setGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss value);

} // ProfitAndLossContainer
