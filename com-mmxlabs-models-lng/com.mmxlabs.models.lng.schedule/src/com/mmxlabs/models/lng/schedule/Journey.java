/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import java.time.LocalDate;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Journey</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getDestination <em>Destination</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#isLaden <em>Laden</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getToll <em>Toll</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getCanalEntrance <em>Canal Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getCanalDate <em>Canal Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getCanalBooking <em>Canal Booking</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getLatestPossibleCanalDate <em>Latest Possible Canal Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getCanalArrival <em>Canal Arrival</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getCanalBookingPeriod <em>Canal Booking Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Journey#getCanalEntrancePort <em>Canal Entrance Port</em>}</li>
 * </ul>
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
	 * Returns the value of the '<em><b>Route Option</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Option</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #setRouteOption(RouteOption)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_RouteOption()
	 * @model required="true"
	 * @generated
	 */
	RouteOption getRouteOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getRouteOption <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #getRouteOption()
	 * @generated
	 */
	void setRouteOption(RouteOption value);

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

	/**
	 * Returns the value of the '<em><b>Canal Entrance</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.CanalEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Entrance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Entrance</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.CanalEntry
	 * @see #setCanalEntrance(CanalEntry)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_CanalEntrance()
	 * @model
	 * @generated
	 */
	CanalEntry getCanalEntrance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getCanalEntrance <em>Canal Entrance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Entrance</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.CanalEntry
	 * @see #getCanalEntrance()
	 * @generated
	 */
	void setCanalEntrance(CanalEntry value);

	/**
	 * Returns the value of the '<em><b>Canal Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Date</em>' attribute.
	 * @see #setCanalDate(LocalDate)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_CanalDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getCanalDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getCanalDate <em>Canal Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Date</em>' attribute.
	 * @see #getCanalDate()
	 * @generated
	 */
	void setCanalDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Canal Booking</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Booking</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Booking</em>' reference.
	 * @see #setCanalBooking(CanalBookingSlot)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_CanalBooking()
	 * @model
	 * @generated
	 */
	CanalBookingSlot getCanalBooking();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getCanalBooking <em>Canal Booking</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Booking</em>' reference.
	 * @see #getCanalBooking()
	 * @generated
	 */
	void setCanalBooking(CanalBookingSlot value);

	/**
	 * Returns the value of the '<em><b>Latest Possible Canal Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Latest Possible Canal Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest Possible Canal Date</em>' attribute.
	 * @see #setLatestPossibleCanalDate(LocalDate)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_LatestPossibleCanalDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getLatestPossibleCanalDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getLatestPossibleCanalDate <em>Latest Possible Canal Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest Possible Canal Date</em>' attribute.
	 * @see #getLatestPossibleCanalDate()
	 * @generated
	 */
	void setLatestPossibleCanalDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Canal Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Arrival</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Arrival</em>' attribute.
	 * @see #setCanalArrival(LocalDate)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_CanalArrival()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getCanalArrival();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getCanalArrival <em>Canal Arrival</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Arrival</em>' attribute.
	 * @see #getCanalArrival()
	 * @generated
	 */
	void setCanalArrival(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Canal Booking Period</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.schedule.PanamaBookingPeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Booking Period</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Booking Period</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.PanamaBookingPeriod
	 * @see #setCanalBookingPeriod(PanamaBookingPeriod)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_CanalBookingPeriod()
	 * @model
	 * @generated
	 */
	PanamaBookingPeriod getCanalBookingPeriod();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getCanalBookingPeriod <em>Canal Booking Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Booking Period</em>' attribute.
	 * @see com.mmxlabs.models.lng.schedule.PanamaBookingPeriod
	 * @see #getCanalBookingPeriod()
	 * @generated
	 */
	void setCanalBookingPeriod(PanamaBookingPeriod value);

	/**
	 * Returns the value of the '<em><b>Canal Entrance Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Entrance Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Entrance Port</em>' reference.
	 * @see #setCanalEntrancePort(Port)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getJourney_CanalEntrancePort()
	 * @model
	 * @generated
	 */
	Port getCanalEntrancePort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Journey#getCanalEntrancePort <em>Canal Entrance Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Entrance Port</em>' reference.
	 * @see #getCanalEntrancePort()
	 * @generated
	 */
	void setCanalEntrancePort(Port value);

} // end of  Journey

// finish type fixing
