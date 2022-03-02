/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Route</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.Route#getLines <em>Lines</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Route#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Route#getNorthEntrance <em>North Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Route#getSouthEntrance <em>South Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Route#getDistance <em>Distance</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getRoute()
 * @model
 * @generated
 */
public interface Route extends NamedObject, UUIDObject {
	/**
	 * Returns the value of the '<em><b>Lines</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.RouteLine}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lines</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lines</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getRoute_Lines()
	 * @model containment="true"
	 * @generated
	 */
	EList<RouteLine> getLines();

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
	 * @see com.mmxlabs.models.lng.port.PortPackage#getRoute_RouteOption()
	 * @model
	 * @generated
	 */
	RouteOption getRouteOption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Route#getRouteOption <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Option</em>' attribute.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see #getRouteOption()
	 * @generated
	 */
	void setRouteOption(RouteOption value);

	/**
	 * Returns the value of the '<em><b>North Entrance</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>North Entrance</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>North Entrance</em>' containment reference.
	 * @see #setNorthEntrance(EntryPoint)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getRoute_NorthEntrance()
	 * @model containment="true"
	 * @generated
	 */
	EntryPoint getNorthEntrance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Route#getNorthEntrance <em>North Entrance</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>North Entrance</em>' containment reference.
	 * @see #getNorthEntrance()
	 * @generated
	 */
	void setNorthEntrance(EntryPoint value);

	/**
	 * Returns the value of the '<em><b>South Entrance</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>South Entrance</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>South Entrance</em>' containment reference.
	 * @see #setSouthEntrance(EntryPoint)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getRoute_SouthEntrance()
	 * @model containment="true"
	 * @generated
	 */
	EntryPoint getSouthEntrance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Route#getSouthEntrance <em>South Entrance</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>South Entrance</em>' containment reference.
	 * @see #getSouthEntrance()
	 * @generated
	 */
	void setSouthEntrance(EntryPoint value);

	/**
	 * Returns the value of the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance</em>' attribute.
	 * @see #setDistance(double)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getRoute_Distance()
	 * @model required="true"
	 * @generated
	 */
	double getDistance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Route#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(double value);

} // Route
