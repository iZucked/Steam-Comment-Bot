/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinHeel <em>Min Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getWarmingTime <em>Warming Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingVolume <em>Cooling Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getRouteParameters <em>Route Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getPilotLightRate <em>Pilot Light Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#isHasReliqCapability <em>Has Reliq Capability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getScnt <em>Scnt</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass()
 * @model
 * @generated
 */
public interface VesselClass extends AVesselSet<Vessel> {
	/**
	 * Returns the value of the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_InaccessiblePorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getInaccessiblePorts();

	/**
	 * Returns the value of the '<em><b>Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel</em>' reference.
	 * @see #setBaseFuel(BaseFuel)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_BaseFuel()
	 * @model required="true"
	 * @generated
	 */
	BaseFuel getBaseFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel</em>' reference.
	 * @see #getBaseFuel()
	 * @generated
	 */
	void setBaseFuel(BaseFuel value);

	/**
	 * Returns the value of the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity</em>' attribute.
	 * @see #setCapacity(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_Capacity()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,###'"
	 * @generated
	 */
	int getCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity</em>' attribute.
	 * @see #getCapacity()
	 * @generated
	 */
	void setCapacity(int value);

	/**
	 * Returns the value of the '<em><b>Fill Capacity</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fill Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fill Capacity</em>' attribute.
	 * @see #setFillCapacity(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_FillCapacity()
	 * @model default="1" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='###.#' unit='%' exportFormatString='#.###'"
	 * @generated
	 */
	double getFillCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fill Capacity</em>' attribute.
	 * @see #getFillCapacity()
	 * @generated
	 */
	void setFillCapacity(double value);

	/**
	 * Returns the value of the '<em><b>Laden Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Attributes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Attributes</em>' containment reference.
	 * @see #setLadenAttributes(VesselStateAttributes)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_LadenAttributes()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselStateAttributes getLadenAttributes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Attributes</em>' containment reference.
	 * @see #getLadenAttributes()
	 * @generated
	 */
	void setLadenAttributes(VesselStateAttributes value);

	/**
	 * Returns the value of the '<em><b>Ballast Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Attributes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Attributes</em>' containment reference.
	 * @see #setBallastAttributes(VesselStateAttributes)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_BallastAttributes()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselStateAttributes getBallastAttributes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Attributes</em>' containment reference.
	 * @see #getBallastAttributes()
	 * @generated
	 */
	void setBallastAttributes(VesselStateAttributes value);

	/**
	 * Returns the value of the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Speed</em>' attribute.
	 * @see #setMinSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_MinSpeed()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kts' formatString='#0.###'"
	 * @generated
	 */
	double getMinSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Speed</em>' attribute.
	 * @see #getMinSpeed()
	 * @generated
	 */
	void setMinSpeed(double value);

	/**
	 * Returns the value of the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Speed</em>' attribute.
	 * @see #setMaxSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_MaxSpeed()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kts' formatString='#0.###'"
	 * @generated
	 */
	double getMaxSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Speed</em>' attribute.
	 * @see #getMaxSpeed()
	 * @generated
	 */
	void setMaxSpeed(double value);

	/**
	 * Returns the value of the '<em><b>Min Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * This value is the minimum quantity of LNG left in the tanks after 
	 * travelling on NBO (in m3). If the quantity falls below this amount
	 * while travelling, the boiloff system stops working and requires human
	 * maintenance to restart. 
	 * (For some reason), going below the minimum heel while idling on NBO 
	 * is not problematic. 
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Heel</em>' attribute.
	 * @see #setMinHeel(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_MinHeel()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='###,##0' unit='m\263'"
	 * @generated
	 */
	int getMinHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinHeel <em>Min Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Heel</em>' attribute.
	 * @see #getMinHeel()
	 * @generated
	 */
	void setMinHeel(int value);

	/**
	 * Returns the value of the '<em><b>Warming Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Warming Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Warming Time</em>' attribute.
	 * @see #setWarmingTime(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_WarmingTime()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hrs' formatString='##0'"
	 * @generated
	 */
	int getWarmingTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getWarmingTime <em>Warming Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Warming Time</em>' attribute.
	 * @see #getWarmingTime()
	 * @generated
	 */
	void setWarmingTime(int value);

	/**
	 * Returns the value of the '<em><b>Cooling Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooling Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooling Volume</em>' attribute.
	 * @see #setCoolingVolume(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_CoolingVolume()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0'"
	 * @generated
	 */
	int getCoolingVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingVolume <em>Cooling Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooling Volume</em>' attribute.
	 * @see #getCoolingVolume()
	 * @generated
	 */
	void setCoolingVolume(int value);

	/**
	 * Returns the value of the '<em><b>Route Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Parameters</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_RouteParameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselClassRouteParameters> getRouteParameters();

	/**
	 * Returns the value of the '<em><b>Pilot Light Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pilot Light Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pilot Light Rate</em>' attribute.
	 * @see #setPilotLightRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_PilotLightRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getPilotLightRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getPilotLightRate <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pilot Light Rate</em>' attribute.
	 * @see #getPilotLightRate()
	 * @generated
	 */
	void setPilotLightRate(double value);

	/**
	 * Returns the value of the '<em><b>Min Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Base Fuel Consumption</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Base Fuel Consumption</em>' attribute.
	 * @see #setMinBaseFuelConsumption(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_MinBaseFuelConsumption()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getMinBaseFuelConsumption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Base Fuel Consumption</em>' attribute.
	 * @see #getMinBaseFuelConsumption()
	 * @generated
	 */
	void setMinBaseFuelConsumption(double value);

	/**
	 * Returns the value of the '<em><b>Has Reliq Capability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Reliq Capability</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Reliq Capability</em>' attribute.
	 * @see #setHasReliqCapability(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_HasReliqCapability()
	 * @model annotation="http://www.mmxlabs.com/models/ui/featureEnablement feature='reliq-support'"
	 * @generated
	 */
	boolean isHasReliqCapability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#isHasReliqCapability <em>Has Reliq Capability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Reliq Capability</em>' attribute.
	 * @see #isHasReliqCapability()
	 * @generated
	 */
	void setHasReliqCapability(boolean value);

	/**
	 * Returns the value of the '<em><b>Inaccessible Routes</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Routes</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Routes</em>' attribute list.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_InaccessibleRoutes()
	 * @model
	 * @generated
	 */
	EList<RouteOption> getInaccessibleRoutes();

	/**
	 * Returns the value of the '<em><b>Scnt</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scnt</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scnt</em>' attribute.
	 * @see #setScnt(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_Scnt()
	 * @model
	 * @generated
	 */
	int getScnt();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getScnt <em>Scnt</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scnt</em>' attribute.
	 * @see #getScnt()
	 * @generated
	 */
	void setScnt(int value);

} // end of  VesselClass

// finish type fixing
