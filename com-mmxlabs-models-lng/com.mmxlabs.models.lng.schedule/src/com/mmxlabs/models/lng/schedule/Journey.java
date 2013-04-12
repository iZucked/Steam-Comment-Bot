/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Journey</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getDestination <em>Destination</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#isLaden <em>Laden</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getToll <em>Toll</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getSpeed <em>Speed</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney()
 * @model
 * @generated
 */
public interface Journey extends Event, FuelUsage {
	/**
	 * Returns the value of the '<em><b>Destination</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination</em>' reference.
	 * @see #setDestination(Port)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_Destination()
	 * @model required="true"
	 * @generated
	 */
	Port getDestination();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getDestination <em>Destination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination</em>' reference.
	 * @see #getDestination()
	 * @generated
	 */
	void setDestination(Port value);

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
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_Laden()
	 * @model required="true"
	 * @generated
	 */
	boolean isLaden();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#isLaden <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden</em>' attribute.
	 * @see #isLaden()
	 * @generated
	 */
	void setLaden(boolean value);

	/**
	 * Returns the value of the '<em><b>Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route</em>' attribute.
	 * @see #setRoute(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_Route()
	 * @model required="true"
	 * @generated
	 */
	String getRoute();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getRoute <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' attribute.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(String value);

	/**
	 * Returns the value of the '<em><b>Toll</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Toll</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Toll</em>' attribute.
	 * @see #setToll(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_Toll()
	 * @model required="true"
	 * @generated
	 */
	int getToll();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getToll <em>Toll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Toll</em>' attribute.
	 * @see #getToll()
	 * @generated
	 */
	void setToll(int value);

	/**
	 * Returns the value of the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance</em>' attribute.
	 * @see #setDistance(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_Distance()
	 * @model required="true"
	 * @generated
	 */
	int getDistance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(int value);

	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #setSpeed(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_Speed()
	 * @model required="true"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(double value);

} // end of  Journey

// finish type fixing
