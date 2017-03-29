/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PortVisit#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PortVisit#getLateness <em>Lateness</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PortVisit#getHeelCost <em>Heel Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PortVisit#getHeelRevenue <em>Heel Revenue</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisit()
 * @model
 * @generated
 */
public interface PortVisit extends Event, CapacityViolationsHolder {
	/**
	 * Returns the value of the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Cost</em>' attribute.
	 * @see #setPortCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisit_PortCost()
	 * @model required="true"
	 * @generated
	 */
	int getPortCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PortVisit#getPortCost <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Cost</em>' attribute.
	 * @see #getPortCost()
	 * @generated
	 */
	void setPortCost(int value);

	/**
	 * Returns the value of the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lateness</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lateness</em>' containment reference.
	 * @see #setLateness(PortVisitLateness)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisit_Lateness()
	 * @model containment="true"
	 * @generated
	 */
	PortVisitLateness getLateness();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PortVisit#getLateness <em>Lateness</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lateness</em>' containment reference.
	 * @see #getLateness()
	 * @generated
	 */
	void setLateness(PortVisitLateness value);

	/**
	 * Returns the value of the '<em><b>Heel Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel Cost</em>' attribute.
	 * @see #setHeelCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisit_HeelCost()
	 * @model
	 * @generated
	 */
	int getHeelCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PortVisit#getHeelCost <em>Heel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel Cost</em>' attribute.
	 * @see #getHeelCost()
	 * @generated
	 */
	void setHeelCost(int value);

	/**
	 * Returns the value of the '<em><b>Heel Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel Revenue</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel Revenue</em>' attribute.
	 * @see #setHeelRevenue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisit_HeelRevenue()
	 * @model
	 * @generated
	 */
	int getHeelRevenue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.PortVisit#getHeelRevenue <em>Heel Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel Revenue</em>' attribute.
	 * @see #getHeelRevenue()
	 * @generated
	 */
	void setHeelRevenue(int value);

} // end of  PortVisit

// finish type fixing
