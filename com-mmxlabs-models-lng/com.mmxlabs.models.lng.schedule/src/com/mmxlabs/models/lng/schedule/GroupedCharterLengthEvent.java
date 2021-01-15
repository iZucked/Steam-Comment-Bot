/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grouped Charter Length Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent#getLinkedSequence <em>Linked Sequence</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGroupedCharterLengthEvent()
 * @model
 * @generated
 */
public interface GroupedCharterLengthEvent extends Event, EventGrouping, ProfitAndLossContainer {
	/**
	 * Returns the value of the '<em><b>Linked Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked Sequence</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Linked Sequence</em>' reference.
	 * @see #setLinkedSequence(Sequence)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGroupedCharterLengthEvent_LinkedSequence()
	 * @model
	 * @generated
	 */
	Sequence getLinkedSequence();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent#getLinkedSequence <em>Linked Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Linked Sequence</em>' reference.
	 * @see #getLinkedSequence()
	 * @generated
	 */
	void setLinkedSequence(Sequence value);

} // GroupedCharterLengthEvent
