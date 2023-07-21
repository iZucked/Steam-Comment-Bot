/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generated Charter Length Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent#isLaden <em>Laden</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent#getContingencyHours <em>Contingency Hours</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterLengthEvent()
 * @model
 * @generated
 */
public interface GeneratedCharterLengthEvent extends PortVisit, ProfitAndLossContainer, EventGrouping, FuelUsage {
	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterLengthEvent_Duration()
	 * @model
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

	/**
	 * Returns the value of the '<em><b>Laden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden</em>' attribute.
	 * @see #setLaden(boolean)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterLengthEvent_Laden()
	 * @model required="true"
	 * @generated
	 */
	boolean isLaden();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent#isLaden <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden</em>' attribute.
	 * @see #isLaden()
	 * @generated
	 */
	void setLaden(boolean value);

	/**
	 * Returns the value of the '<em><b>Contingency Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contingency Hours</em>' attribute.
	 * @see #setContingencyHours(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getGeneratedCharterLengthEvent_ContingencyHours()
	 * @model
	 * @generated
	 */
	int getContingencyHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent#getContingencyHours <em>Contingency Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contingency Hours</em>' attribute.
	 * @see #getContingencyHours()
	 * @generated
	 */
	void setContingencyHours(int value);

} // GeneratedCharterLengthEvent
