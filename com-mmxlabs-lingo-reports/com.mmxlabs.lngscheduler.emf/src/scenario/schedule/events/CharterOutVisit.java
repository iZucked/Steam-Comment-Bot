/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.events;

import scenario.fleet.CharterOut;
import scenario.schedule.CharterOutRevenue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.CharterOutVisit#getCharterOut <em>Charter Out</em>}</li>
 *   <li>{@link scenario.schedule.events.CharterOutVisit#getRevenue <em>Revenue</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getCharterOutVisit()
 * @model
 * @generated
 */
public interface CharterOutVisit extends PortVisit {
	/**
	 * Returns the value of the '<em><b>Charter Out</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out</em>' reference.
	 * @see #setCharterOut(CharterOut)
	 * @see scenario.schedule.events.EventsPackage#getCharterOutVisit_CharterOut()
	 * @model required="true"
	 * @generated
	 */
	CharterOut getCharterOut();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.CharterOutVisit#getCharterOut <em>Charter Out</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out</em>' reference.
	 * @see #getCharterOut()
	 * @generated
	 */
	void setCharterOut(CharterOut value);

	/**
	 * Returns the value of the '<em><b>Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Revenue</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Revenue</em>' reference.
	 * @see #setRevenue(CharterOutRevenue)
	 * @see scenario.schedule.events.EventsPackage#getCharterOutVisit_Revenue()
	 * @model required="true"
	 * @generated
	 */
	CharterOutRevenue getRevenue();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.CharterOutVisit#getRevenue <em>Revenue</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Revenue</em>' reference.
	 * @see #getRevenue()
	 * @generated
	 */
	void setRevenue(CharterOutRevenue value);

	
} // CharterOutVisit
