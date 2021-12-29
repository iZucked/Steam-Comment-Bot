/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Idle</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Idle#isLaden <em>Laden</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Idle#getBufferHours <em>Buffer Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Idle#getPanamaHours <em>Panama Hours</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Idle#getContingencyHours <em>Contingency Hours</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getIdle()
 * @model
 * @generated
 */
public interface Idle extends Event, FuelUsage {

	/**
	 * Returns the value of the '<em><b>Laden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden</em>' attribute.
	 * @see #setLaden(boolean)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getIdle_Laden()
	 * @model required="true"
	 * @generated
	 */
	boolean isLaden();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Idle#isLaden <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden</em>' attribute.
	 * @see #isLaden()
	 * @generated
	 */
	void setLaden(boolean value);

	/**
	 * Returns the value of the '<em><b>Buffer Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buffer Hours</em>' attribute.
	 * @see #setBufferHours(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getIdle_BufferHours()
	 * @model
	 * @generated
	 */
	int getBufferHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Idle#getBufferHours <em>Buffer Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Buffer Hours</em>' attribute.
	 * @see #getBufferHours()
	 * @generated
	 */
	void setBufferHours(int value);

	/**
	 * Returns the value of the '<em><b>Panama Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Panama Hours</em>' attribute.
	 * @see #setPanamaHours(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getIdle_PanamaHours()
	 * @model
	 * @generated
	 */
	int getPanamaHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Idle#getPanamaHours <em>Panama Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Panama Hours</em>' attribute.
	 * @see #getPanamaHours()
	 * @generated
	 */
	void setPanamaHours(int value);

	/**
	 * Returns the value of the '<em><b>Contingency Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contigency Hours</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contingency Hours</em>' attribute.
	 * @see #setContingencyHours(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getIdle_ContingencyHours()
	 * @model
	 * @generated
	 */
	int getContingencyHours();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Idle#getContingencyHours <em>Contingency Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contingency Hours</em>' attribute.
	 * @see #getContingencyHours()
	 * @generated
	 */
	void setContingencyHours(int value);
} // end of  Idle

// finish type fixing
