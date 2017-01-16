/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class Route Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getExtraTransitTime <em>Extra Transit Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenConsumptionRate <em>Laden Consumption Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenNBORate <em>Laden NBO Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastConsumptionRate <em>Ballast Consumption Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastNBORate <em>Ballast NBO Rate</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClassRouteParameters()
 * @model
 * @generated
 */
public interface VesselClassRouteParameters extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route</em>' reference.
	 * @see #setRoute(Route)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClassRouteParameters_Route()
	 * @model required="true"
	 * @generated
	 */
	Route getRoute();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getRoute <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' reference.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(Route value);

	/**
	 * Returns the value of the '<em><b>Extra Transit Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extra Transit Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Transit Time</em>' attribute.
	 * @see #setExtraTransitTime(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClassRouteParameters_ExtraTransitTime()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hours' formatString='##0'"
	 * @generated
	 */
	int getExtraTransitTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getExtraTransitTime <em>Extra Transit Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extra Transit Time</em>' attribute.
	 * @see #getExtraTransitTime()
	 * @generated
	 */
	void setExtraTransitTime(int value);

	/**
	 * Returns the value of the '<em><b>Laden Consumption Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Consumption Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Consumption Rate</em>' attribute.
	 * @see #setLadenConsumptionRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClassRouteParameters_LadenConsumptionRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getLadenConsumptionRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenConsumptionRate <em>Laden Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Consumption Rate</em>' attribute.
	 * @see #getLadenConsumptionRate()
	 * @generated
	 */
	void setLadenConsumptionRate(double value);

	/**
	 * Returns the value of the '<em><b>Laden NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden NBO Rate</em>' attribute.
	 * @see #setLadenNBORate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClassRouteParameters_LadenNBORate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getLadenNBORate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenNBORate <em>Laden NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden NBO Rate</em>' attribute.
	 * @see #getLadenNBORate()
	 * @generated
	 */
	void setLadenNBORate(double value);

	/**
	 * Returns the value of the '<em><b>Ballast Consumption Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Consumption Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Consumption Rate</em>' attribute.
	 * @see #setBallastConsumptionRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClassRouteParameters_BallastConsumptionRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getBallastConsumptionRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastConsumptionRate <em>Ballast Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Consumption Rate</em>' attribute.
	 * @see #getBallastConsumptionRate()
	 * @generated
	 */
	void setBallastConsumptionRate(double value);

	/**
	 * Returns the value of the '<em><b>Ballast NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast NBO Rate</em>' attribute.
	 * @see #setBallastNBORate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClassRouteParameters_BallastNBORate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263/day' formatString='##0.###'"
	 * @generated
	 */
	double getBallastNBORate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastNBORate <em>Ballast NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast NBO Rate</em>' attribute.
	 * @see #getBallastNBORate()
	 * @generated
	 */
	void setBallastNBORate(double value);

} // end of  VesselClassRouteParameters

// finish type fixing
