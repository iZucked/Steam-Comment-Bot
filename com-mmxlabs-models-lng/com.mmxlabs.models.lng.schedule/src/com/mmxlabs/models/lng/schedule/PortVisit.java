/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.PortVisit#getPortCost <em>Port Cost</em>}</li>
 * </ul>
 * </p>
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

} // end of  PortVisit

// finish type fixing
