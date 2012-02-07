/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule;

import scenario.schedule.events.CharterOutVisit;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Charter Out Revenue</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.schedule.CharterOutRevenue#getCharterOut <em>Charter Out</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.schedule.SchedulePackage#getCharterOutRevenue()
 * @model
 * @generated
 */
public interface CharterOutRevenue extends BookedRevenue {
	/**
	 * Returns the value of the '<em><b>Charter Out</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Charter Out</em>' reference.
	 * @see #setCharterOut(CharterOutVisit)
	 * @see scenario.schedule.SchedulePackage#getCharterOutRevenue_CharterOut()
	 * @model required="true"
	 * @generated
	 */
	CharterOutVisit getCharterOut();

	/**
	 * Sets the value of the '{@link scenario.schedule.CharterOutRevenue#getCharterOut <em>Charter Out</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Charter Out</em>' reference.
	 * @see #getCharterOut()
	 * @generated
	 */
	void setCharterOut(CharterOutVisit value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true" annotation="http://www.eclipse.org/emf/2002/GenModel body='return getCharterOut().getCharterOut().getId();'"
	 * @generated
	 */
	@Override
	String getName();

} // CharterOutRevenue
