/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events;

import scenario.fleet.VesselState;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Journey</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.Journey#getToPort <em>To Port</em>}</li>
 *   <li>{@link scenario.schedule.events.Journey#getVesselState <em>Vessel State</em>}</li>
 *   <li>{@link scenario.schedule.events.Journey#getRoute <em>Route</em>}</li>
 *   <li>{@link scenario.schedule.events.Journey#getSpeed <em>Speed</em>}</li>
 *   <li>{@link scenario.schedule.events.Journey#getDistance <em>Distance</em>}</li>
 *   <li>{@link scenario.schedule.events.Journey#getFromPort <em>From Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getJourney()
 * @model
 * @generated
 */
public interface Journey extends ScheduledEvent, FuelMixture {
	/**
	 * Returns the value of the '<em><b>To Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Port</em>' reference.
	 * @see #setToPort(Port)
	 * @see scenario.schedule.events.EventsPackage#getJourney_ToPort()
	 * @model required="true"
	 * @generated
	 */
	Port getToPort();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.Journey#getToPort <em>To Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Port</em>' reference.
	 * @see #getToPort()
	 * @generated
	 */
	void setToPort(Port value);

	/**
	 * Returns the value of the '<em><b>Vessel State</b></em>' attribute.
	 * The literals are from the enumeration {@link scenario.fleet.VesselState}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel State</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel State</em>' attribute.
	 * @see scenario.fleet.VesselState
	 * @see #setVesselState(VesselState)
	 * @see scenario.schedule.events.EventsPackage#getJourney_VesselState()
	 * @model required="true"
	 * @generated
	 */
	VesselState getVesselState();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.Journey#getVesselState <em>Vessel State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel State</em>' attribute.
	 * @see scenario.fleet.VesselState
	 * @see #getVesselState()
	 * @generated
	 */
	void setVesselState(VesselState value);

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
	 * @see scenario.schedule.events.EventsPackage#getJourney_Route()
	 * @model required="true"
	 * @generated
	 */
	String getRoute();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.Journey#getRoute <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' attribute.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(String value);

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
	 * @see scenario.schedule.events.EventsPackage#getJourney_Speed()
	 * @model required="true"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.Journey#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(double value);

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
	 * @see scenario.schedule.events.EventsPackage#getJourney_Distance()
	 * @model required="true"
	 * @generated
	 */
	int getDistance();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.Journey#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(int value);

	/**
	 * Returns the value of the '<em><b>From Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Port</em>' reference.
	 * @see #setFromPort(Port)
	 * @see scenario.schedule.events.EventsPackage#getJourney_FromPort()
	 * @model required="true"
	 * @generated
	 */
	Port getFromPort();

	/**
	 * Sets the value of the '{@link scenario.schedule.events.Journey#getFromPort <em>From Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Port</em>' reference.
	 * @see #getFromPort()
	 * @generated
	 */
	void setFromPort(Port value);

} // Journey
