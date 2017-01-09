/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.port.Route;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Voyage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.Voyage#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.Voyage#getRouteCost <em>Route Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.Voyage#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.Voyage#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.Voyage#getIdleTime <em>Idle Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.Voyage#getTravelTime <em>Travel Time</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVoyage()
 * @model
 * @generated
 */
public interface Voyage extends CostComponent {
	/**
	 * Returns the value of the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route</em>' reference.
	 * @see #setRoute(Route)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVoyage_Route()
	 * @model
	 * @generated
	 */
	Route getRoute();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.Voyage#getRoute <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' reference.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(Route value);

	/**
	 * Returns the value of the '<em><b>Route Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Cost</em>' attribute.
	 * @see #setRouteCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVoyage_RouteCost()
	 * @model required="true"
	 * @generated
	 */
	int getRouteCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.Voyage#getRouteCost <em>Route Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Cost</em>' attribute.
	 * @see #getRouteCost()
	 * @generated
	 */
	void setRouteCost(int value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVoyage_Speed()
	 * @model required="true"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.Voyage#getSpeed <em>Speed</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVoyage_Distance()
	 * @model required="true"
	 * @generated
	 */
	int getDistance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.Voyage#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(int value);

	/**
	 * Returns the value of the '<em><b>Idle Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle Time</em>' attribute.
	 * @see #setIdleTime(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVoyage_IdleTime()
	 * @model required="true"
	 * @generated
	 */
	int getIdleTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.Voyage#getIdleTime <em>Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle Time</em>' attribute.
	 * @see #getIdleTime()
	 * @generated
	 */
	void setIdleTime(int value);

	/**
	 * Returns the value of the '<em><b>Travel Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Travel Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Travel Time</em>' attribute.
	 * @see #setTravelTime(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVoyage_TravelTime()
	 * @model required="true"
	 * @generated
	 */
	int getTravelTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.Voyage#getTravelTime <em>Travel Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Travel Time</em>' attribute.
	 * @see #getTravelTime()
	 * @generated
	 */
	void setTravelTime(int value);

} // end of  Voyage

// finish type fixing
