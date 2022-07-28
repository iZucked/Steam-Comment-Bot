/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
 * A representation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getShortName <em>Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getIMO <em>IMO</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getReference <em>Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getScnt <em>Scnt</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getBaseFuel <em>Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getInPortBaseFuel <em>In Port Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightBaseFuel <em>Pilot Light Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getIdleBaseFuel <em>Idle Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightRate <em>Pilot Light Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getSafetyHeel <em>Safety Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getWarmingTime <em>Warming Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingVolume <em>Cooling Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingTime <em>Cooling Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeVolume <em>Purge Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeTime <em>Purge Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isInaccessiblePortsOverride <em>Inaccessible Ports Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isInaccessibleRoutesOverride <em>Inaccessible Routes Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isRouteParametersOverride <em>Route Parameters Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getRouteParameters <em>Route Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapabilityOverride <em>Has Reliq Capability Override</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapability <em>Has Reliq Capability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#getMmxId <em>Mmx Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isReferenceVessel <em>Reference Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isMmxReference <em>Mmx Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.Vessel#isMarker <em>Marker</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel()
 * @model annotation="http://www.mmxlabs.com/models/featureOverride"
 * @generated
 */
public interface Vessel extends AVesselSet<Vessel> {
	/**
	 * Returns the value of the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Short Name</em>' attribute.
	 * @see #setShortName(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_ShortName()
	 * @model
	 * @generated
	 */
	String getShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getShortName <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Short Name</em>' attribute.
	 * @see #getShortName()
	 * @generated
	 */
	void setShortName(String value);

	/**
	 * Returns the value of the '<em><b>IMO</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>IMO</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>IMO</em>' attribute.
	 * @see #setIMO(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_IMO()
	 * @model
	 * @generated
	 */
	String getIMO();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getIMO <em>IMO</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>IMO</em>' attribute.
	 * @see #getIMO()
	 * @generated
	 */
	void setIMO(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' reference.
	 * @see #setReference(Vessel)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Reference()
	 * @model
	 * @generated
	 */
	Vessel getReference();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getReference <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(Vessel value);

	/**
	 * Returns the value of the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_InaccessiblePorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getInaccessiblePorts();

	/**
	 * Returns the value of the '<em><b>Inaccessible Routes Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Routes Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Routes Override</em>' attribute.
	 * @see #setInaccessibleRoutesOverride(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_InaccessibleRoutesOverride()
	 * @model
	 * @generated
	 */
	boolean isInaccessibleRoutesOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isInaccessibleRoutesOverride <em>Inaccessible Routes Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inaccessible Routes Override</em>' attribute.
	 * @see #isInaccessibleRoutesOverride()
	 * @generated
	 */
	void setInaccessibleRoutesOverride(boolean value);

	/**
	 * Returns the value of the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity</em>' attribute.
	 * @see #isSetCapacity()
	 * @see #unsetCapacity()
	 * @see #setCapacity(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Capacity()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0'"
	 * @generated
	 */
	int getCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity</em>' attribute.
	 * @see #isSetCapacity()
	 * @see #unsetCapacity()
	 * @see #getCapacity()
	 * @generated
	 */
	void setCapacity(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCapacity()
	 * @see #getCapacity()
	 * @see #setCapacity(int)
	 * @generated
	 */
	void unsetCapacity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Capacity</em>' attribute is set.
	 * @see #unsetCapacity()
	 * @see #getCapacity()
	 * @see #setCapacity(int)
	 * @generated
	 */
	boolean isSetCapacity();

	/**
	 * Returns the value of the '<em><b>Fill Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fill Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fill Capacity</em>' attribute.
	 * @see #isSetFillCapacity()
	 * @see #unsetFillCapacity()
	 * @see #setFillCapacity(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_FillCapacity()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='##0.#' unit='%' exportFormatString='#.###'"
	 * @generated
	 */
	double getFillCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fill Capacity</em>' attribute.
	 * @see #isSetFillCapacity()
	 * @see #unsetFillCapacity()
	 * @see #getFillCapacity()
	 * @generated
	 */
	void setFillCapacity(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFillCapacity()
	 * @see #getFillCapacity()
	 * @see #setFillCapacity(double)
	 * @generated
	 */
	void unsetFillCapacity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fill Capacity</em>' attribute is set.
	 * @see #unsetFillCapacity()
	 * @see #getFillCapacity()
	 * @see #setFillCapacity(double)
	 * @generated
	 */
	boolean isSetFillCapacity();

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
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_LadenAttributes()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselStateAttributes getLadenAttributes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getLadenAttributes <em>Laden Attributes</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_BallastAttributes()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselStateAttributes getBallastAttributes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getBallastAttributes <em>Ballast Attributes</em>}' containment reference.
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
	 * @see #isSetMinSpeed()
	 * @see #unsetMinSpeed()
	 * @see #setMinSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_MinSpeed()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kts' formatString='#0.###'"
	 * @generated
	 */
	double getMinSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinSpeed <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Speed</em>' attribute.
	 * @see #isSetMinSpeed()
	 * @see #unsetMinSpeed()
	 * @see #getMinSpeed()
	 * @generated
	 */
	void setMinSpeed(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinSpeed <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinSpeed()
	 * @see #getMinSpeed()
	 * @see #setMinSpeed(double)
	 * @generated
	 */
	void unsetMinSpeed();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinSpeed <em>Min Speed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Speed</em>' attribute is set.
	 * @see #unsetMinSpeed()
	 * @see #getMinSpeed()
	 * @see #setMinSpeed(double)
	 * @generated
	 */
	boolean isSetMinSpeed();

	/**
	 * Returns the value of the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Speed</em>' attribute.
	 * @see #isSetMaxSpeed()
	 * @see #unsetMaxSpeed()
	 * @see #setMaxSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_MaxSpeed()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kts' formatString='#0.###'"
	 * @generated
	 */
	double getMaxSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMaxSpeed <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Speed</em>' attribute.
	 * @see #isSetMaxSpeed()
	 * @see #unsetMaxSpeed()
	 * @see #getMaxSpeed()
	 * @generated
	 */
	void setMaxSpeed(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMaxSpeed <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxSpeed()
	 * @see #getMaxSpeed()
	 * @see #setMaxSpeed(double)
	 * @generated
	 */
	void unsetMaxSpeed();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMaxSpeed <em>Max Speed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Speed</em>' attribute is set.
	 * @see #unsetMaxSpeed()
	 * @see #getMaxSpeed()
	 * @see #setMaxSpeed(double)
	 * @generated
	 */
	boolean isSetMaxSpeed();

	/**
	 * Returns the value of the '<em><b>Safety Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Safety Heel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Safety Heel</em>' attribute.
	 * @see #isSetSafetyHeel()
	 * @see #unsetSafetyHeel()
	 * @see #setSafetyHeel(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_SafetyHeel()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='###,##0' unit='m\263'"
	 * @generated
	 */
	int getSafetyHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getSafetyHeel <em>Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Safety Heel</em>' attribute.
	 * @see #isSetSafetyHeel()
	 * @see #unsetSafetyHeel()
	 * @see #getSafetyHeel()
	 * @generated
	 */
	void setSafetyHeel(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getSafetyHeel <em>Safety Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSafetyHeel()
	 * @see #getSafetyHeel()
	 * @see #setSafetyHeel(int)
	 * @generated
	 */
	void unsetSafetyHeel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getSafetyHeel <em>Safety Heel</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Safety Heel</em>' attribute is set.
	 * @see #unsetSafetyHeel()
	 * @see #getSafetyHeel()
	 * @see #setSafetyHeel(int)
	 * @generated
	 */
	boolean isSetSafetyHeel();

	/**
	 * Returns the value of the '<em><b>Warming Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Warming Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Warming Time</em>' attribute.
	 * @see #isSetWarmingTime()
	 * @see #unsetWarmingTime()
	 * @see #setWarmingTime(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_WarmingTime()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hrs' formatString='##0'"
	 * @generated
	 */
	int getWarmingTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getWarmingTime <em>Warming Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Warming Time</em>' attribute.
	 * @see #isSetWarmingTime()
	 * @see #unsetWarmingTime()
	 * @see #getWarmingTime()
	 * @generated
	 */
	void setWarmingTime(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getWarmingTime <em>Warming Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetWarmingTime()
	 * @see #getWarmingTime()
	 * @see #setWarmingTime(int)
	 * @generated
	 */
	void unsetWarmingTime();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getWarmingTime <em>Warming Time</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Warming Time</em>' attribute is set.
	 * @see #unsetWarmingTime()
	 * @see #getWarmingTime()
	 * @see #setWarmingTime(int)
	 * @generated
	 */
	boolean isSetWarmingTime();

	/**
	 * Returns the value of the '<em><b>Purge Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purge Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purge Time</em>' attribute.
	 * @see #isSetPurgeTime()
	 * @see #unsetPurgeTime()
	 * @see #setPurgeTime(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_PurgeTime()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hrs' formatString='##0'"
	 *        annotation="http://www.mmxlabs.com/models/ui/featureEnablement feature='purge'"
	 * @generated
	 */
	int getPurgeTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeTime <em>Purge Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Purge Time</em>' attribute.
	 * @see #isSetPurgeTime()
	 * @see #unsetPurgeTime()
	 * @see #getPurgeTime()
	 * @generated
	 */
	void setPurgeTime(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeTime <em>Purge Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPurgeTime()
	 * @see #getPurgeTime()
	 * @see #setPurgeTime(int)
	 * @generated
	 */
	void unsetPurgeTime();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeTime <em>Purge Time</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Purge Time</em>' attribute is set.
	 * @see #unsetPurgeTime()
	 * @see #getPurgeTime()
	 * @see #setPurgeTime(int)
	 * @generated
	 */
	boolean isSetPurgeTime();

	/**
	 * Returns the value of the '<em><b>Cooling Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooling Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooling Volume</em>' attribute.
	 * @see #isSetCoolingVolume()
	 * @see #unsetCoolingVolume()
	 * @see #setCoolingVolume(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_CoolingVolume()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0'"
	 * @generated
	 */
	int getCoolingVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingVolume <em>Cooling Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooling Volume</em>' attribute.
	 * @see #isSetCoolingVolume()
	 * @see #unsetCoolingVolume()
	 * @see #getCoolingVolume()
	 * @generated
	 */
	void setCoolingVolume(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingVolume <em>Cooling Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCoolingVolume()
	 * @see #getCoolingVolume()
	 * @see #setCoolingVolume(int)
	 * @generated
	 */
	void unsetCoolingVolume();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingVolume <em>Cooling Volume</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cooling Volume</em>' attribute is set.
	 * @see #unsetCoolingVolume()
	 * @see #getCoolingVolume()
	 * @see #setCoolingVolume(int)
	 * @generated
	 */
	boolean isSetCoolingVolume();

	/**
	 * Returns the value of the '<em><b>Cooling Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooling Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooling Time</em>' attribute.
	 * @see #isSetCoolingTime()
	 * @see #unsetCoolingTime()
	 * @see #setCoolingTime(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_CoolingTime()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hrs' formatString='##0'"
	 *        annotation="http://www.mmxlabs.com/models/ui/featureEnablement feature='purge'"
	 * @generated
	 */
	int getCoolingTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingTime <em>Cooling Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooling Time</em>' attribute.
	 * @see #isSetCoolingTime()
	 * @see #unsetCoolingTime()
	 * @see #getCoolingTime()
	 * @generated
	 */
	void setCoolingTime(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingTime <em>Cooling Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCoolingTime()
	 * @see #getCoolingTime()
	 * @see #setCoolingTime(int)
	 * @generated
	 */
	void unsetCoolingTime();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingTime <em>Cooling Time</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cooling Time</em>' attribute is set.
	 * @see #unsetCoolingTime()
	 * @see #getCoolingTime()
	 * @see #setCoolingTime(int)
	 * @generated
	 */
	boolean isSetCoolingTime();

	/**
	 * Returns the value of the '<em><b>Purge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purge Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purge Volume</em>' attribute.
	 * @see #isSetPurgeVolume()
	 * @see #unsetPurgeVolume()
	 * @see #setPurgeVolume(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_PurgeVolume()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0'"
	 * @generated
	 */
	int getPurgeVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeVolume <em>Purge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Purge Volume</em>' attribute.
	 * @see #isSetPurgeVolume()
	 * @see #unsetPurgeVolume()
	 * @see #getPurgeVolume()
	 * @generated
	 */
	void setPurgeVolume(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeVolume <em>Purge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPurgeVolume()
	 * @see #getPurgeVolume()
	 * @see #setPurgeVolume(int)
	 * @generated
	 */
	void unsetPurgeVolume();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeVolume <em>Purge Volume</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Purge Volume</em>' attribute is set.
	 * @see #unsetPurgeVolume()
	 * @see #getPurgeVolume()
	 * @see #setPurgeVolume(int)
	 * @generated
	 */
	boolean isSetPurgeVolume();

	/**
	 * Returns the value of the '<em><b>Route Parameters Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Parameters Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Parameters Override</em>' attribute.
	 * @see #setRouteParametersOverride(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_RouteParametersOverride()
	 * @model
	 * @generated
	 */
	boolean isRouteParametersOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isRouteParametersOverride <em>Route Parameters Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Parameters Override</em>' attribute.
	 * @see #isRouteParametersOverride()
	 * @generated
	 */
	void setRouteParametersOverride(boolean value);

	/**
	 * Returns the value of the '<em><b>Route Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselRouteParameters}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Parameters</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_RouteParameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselRouteParameters> getRouteParameters();

	/**
	 * Returns the value of the '<em><b>Pilot Light Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pilot Light Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pilot Light Rate</em>' attribute.
	 * @see #isSetPilotLightRate()
	 * @see #unsetPilotLightRate()
	 * @see #setPilotLightRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_PilotLightRate()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.##'"
	 * @generated
	 */
	double getPilotLightRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightRate <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pilot Light Rate</em>' attribute.
	 * @see #isSetPilotLightRate()
	 * @see #unsetPilotLightRate()
	 * @see #getPilotLightRate()
	 * @generated
	 */
	void setPilotLightRate(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightRate <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPilotLightRate()
	 * @see #getPilotLightRate()
	 * @see #setPilotLightRate(double)
	 * @generated
	 */
	void unsetPilotLightRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightRate <em>Pilot Light Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Pilot Light Rate</em>' attribute is set.
	 * @see #unsetPilotLightRate()
	 * @see #getPilotLightRate()
	 * @see #setPilotLightRate(double)
	 * @generated
	 */
	boolean isSetPilotLightRate();

	/**
	 * Returns the value of the '<em><b>Min Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Base Fuel Consumption</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Base Fuel Consumption</em>' attribute.
	 * @see #isSetMinBaseFuelConsumption()
	 * @see #unsetMinBaseFuelConsumption()
	 * @see #setMinBaseFuelConsumption(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_MinBaseFuelConsumption()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/day' formatString='##0.###'"
	 * @generated
	 */
	double getMinBaseFuelConsumption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Base Fuel Consumption</em>' attribute.
	 * @see #isSetMinBaseFuelConsumption()
	 * @see #unsetMinBaseFuelConsumption()
	 * @see #getMinBaseFuelConsumption()
	 * @generated
	 */
	void setMinBaseFuelConsumption(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinBaseFuelConsumption()
	 * @see #getMinBaseFuelConsumption()
	 * @see #setMinBaseFuelConsumption(double)
	 * @generated
	 */
	void unsetMinBaseFuelConsumption();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Base Fuel Consumption</em>' attribute is set.
	 * @see #unsetMinBaseFuelConsumption()
	 * @see #getMinBaseFuelConsumption()
	 * @see #setMinBaseFuelConsumption(double)
	 * @generated
	 */
	boolean isSetMinBaseFuelConsumption();

	/**
	 * Returns the value of the '<em><b>Has Reliq Capability Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Reliq Capability Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Reliq Capability Override</em>' attribute.
	 * @see #setHasReliqCapabilityOverride(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_HasReliqCapabilityOverride()
	 * @model
	 * @generated
	 */
	boolean isHasReliqCapabilityOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapabilityOverride <em>Has Reliq Capability Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Reliq Capability Override</em>' attribute.
	 * @see #isHasReliqCapabilityOverride()
	 * @generated
	 */
	void setHasReliqCapabilityOverride(boolean value);

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
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_HasReliqCapability()
	 * @model annotation="http://www.mmxlabs.com/models/ui/featureEnablement feature='reliq-support'"
	 * @generated
	 */
	boolean isHasReliqCapability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapability <em>Has Reliq Capability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Reliq Capability</em>' attribute.
	 * @see #isHasReliqCapability()
	 * @generated
	 */
	void setHasReliqCapability(boolean value);

	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Notes()
	 * @model annotation="http://www.mmxlabs.com/models/validation ignore='true'"
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

	/**
	 * Returns the value of the '<em><b>Mmx Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mmx Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mmx Id</em>' attribute.
	 * @see #setMmxId(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_MmxId()
	 * @model
	 * @generated
	 */
	String getMmxId();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getMmxId <em>Mmx Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mmx Id</em>' attribute.
	 * @see #getMmxId()
	 * @generated
	 */
	void setMmxId(String value);

	/**
	 * Returns the value of the '<em><b>Reference Vessel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Vessel</em>' attribute.
	 * @see #setReferenceVessel(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_ReferenceVessel()
	 * @model
	 * @generated
	 */
	boolean isReferenceVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isReferenceVessel <em>Reference Vessel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Vessel</em>' attribute.
	 * @see #isReferenceVessel()
	 * @generated
	 */
	void setReferenceVessel(boolean value);

	/**
	 * Returns the value of the '<em><b>Mmx Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mmx Reference</em>' attribute.
	 * @see #setMmxReference(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_MmxReference()
	 * @model
	 * @generated
	 */
	boolean isMmxReference();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isMmxReference <em>Mmx Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mmx Reference</em>' attribute.
	 * @see #isMmxReference()
	 * @generated
	 */
	void setMmxReference(boolean value);

	/**
	 * Returns the value of the '<em><b>Marker</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Marker</em>' attribute.
	 * @see #setMarker(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Marker()
	 * @model
	 * @generated
	 */
	boolean isMarker();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isMarker <em>Marker</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Marker</em>' attribute.
	 * @see #isMarker()
	 * @generated
	 */
	void setMarker(boolean value);

	/**
	 * Returns the value of the '<em><b>Scnt</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scnt</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scnt</em>' attribute.
	 * @see #isSetScnt()
	 * @see #unsetScnt()
	 * @see #setScnt(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_Scnt()
	 * @model unsettable="true"
	 * @generated
	 */
	int getScnt();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getScnt <em>Scnt</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scnt</em>' attribute.
	 * @see #isSetScnt()
	 * @see #unsetScnt()
	 * @see #getScnt()
	 * @generated
	 */
	void setScnt(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getScnt <em>Scnt</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetScnt()
	 * @see #getScnt()
	 * @see #setScnt(int)
	 * @generated
	 */
	void unsetScnt();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getScnt <em>Scnt</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Scnt</em>' attribute is set.
	 * @see #unsetScnt()
	 * @see #getScnt()
	 * @see #setScnt(int)
	 * @generated
	 */
	boolean isSetScnt();

	/**
	 * Returns the value of the '<em><b>Inaccessible Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Ports Override</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Ports Override</em>' attribute.
	 * @see #setInaccessiblePortsOverride(boolean)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_InaccessiblePortsOverride()
	 * @model
	 * @generated
	 */
	boolean isInaccessiblePortsOverride();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#isInaccessiblePortsOverride <em>Inaccessible Ports Override</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inaccessible Ports Override</em>' attribute.
	 * @see #isInaccessiblePortsOverride()
	 * @generated
	 */
	void setInaccessiblePortsOverride(boolean value);

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
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_InaccessibleRoutes()
	 * @model
	 * @generated
	 */
	EList<RouteOption> getInaccessibleRoutes();

	/**
	 * Returns the value of the '<em><b>Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel</em>' reference.
	 * @see #isSetBaseFuel()
	 * @see #unsetBaseFuel()
	 * @see #setBaseFuel(BaseFuel)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_BaseFuel()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	BaseFuel getBaseFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getBaseFuel <em>Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel</em>' reference.
	 * @see #isSetBaseFuel()
	 * @see #unsetBaseFuel()
	 * @see #getBaseFuel()
	 * @generated
	 */
	void setBaseFuel(BaseFuel value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getBaseFuel <em>Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBaseFuel()
	 * @see #getBaseFuel()
	 * @see #setBaseFuel(BaseFuel)
	 * @generated
	 */
	void unsetBaseFuel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getBaseFuel <em>Base Fuel</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Base Fuel</em>' reference is set.
	 * @see #unsetBaseFuel()
	 * @see #getBaseFuel()
	 * @see #setBaseFuel(BaseFuel)
	 * @generated
	 */
	boolean isSetBaseFuel();

	/**
	 * Returns the value of the '<em><b>In Port Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Port Base Fuel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Port Base Fuel</em>' reference.
	 * @see #isSetInPortBaseFuel()
	 * @see #unsetInPortBaseFuel()
	 * @see #setInPortBaseFuel(BaseFuel)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_InPortBaseFuel()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	BaseFuel getInPortBaseFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getInPortBaseFuel <em>In Port Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Port Base Fuel</em>' reference.
	 * @see #isSetInPortBaseFuel()
	 * @see #unsetInPortBaseFuel()
	 * @see #getInPortBaseFuel()
	 * @generated
	 */
	void setInPortBaseFuel(BaseFuel value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getInPortBaseFuel <em>In Port Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInPortBaseFuel()
	 * @see #getInPortBaseFuel()
	 * @see #setInPortBaseFuel(BaseFuel)
	 * @generated
	 */
	void unsetInPortBaseFuel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getInPortBaseFuel <em>In Port Base Fuel</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>In Port Base Fuel</em>' reference is set.
	 * @see #unsetInPortBaseFuel()
	 * @see #getInPortBaseFuel()
	 * @see #setInPortBaseFuel(BaseFuel)
	 * @generated
	 */
	boolean isSetInPortBaseFuel();

	/**
	 * Returns the value of the '<em><b>Pilot Light Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pilot Light Base Fuel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pilot Light Base Fuel</em>' reference.
	 * @see #isSetPilotLightBaseFuel()
	 * @see #unsetPilotLightBaseFuel()
	 * @see #setPilotLightBaseFuel(BaseFuel)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_PilotLightBaseFuel()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	BaseFuel getPilotLightBaseFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightBaseFuel <em>Pilot Light Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pilot Light Base Fuel</em>' reference.
	 * @see #isSetPilotLightBaseFuel()
	 * @see #unsetPilotLightBaseFuel()
	 * @see #getPilotLightBaseFuel()
	 * @generated
	 */
	void setPilotLightBaseFuel(BaseFuel value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightBaseFuel <em>Pilot Light Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPilotLightBaseFuel()
	 * @see #getPilotLightBaseFuel()
	 * @see #setPilotLightBaseFuel(BaseFuel)
	 * @generated
	 */
	void unsetPilotLightBaseFuel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightBaseFuel <em>Pilot Light Base Fuel</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Pilot Light Base Fuel</em>' reference is set.
	 * @see #unsetPilotLightBaseFuel()
	 * @see #getPilotLightBaseFuel()
	 * @see #setPilotLightBaseFuel(BaseFuel)
	 * @generated
	 */
	boolean isSetPilotLightBaseFuel();

	/**
	 * Returns the value of the '<em><b>Idle Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle Base Fuel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle Base Fuel</em>' reference.
	 * @see #isSetIdleBaseFuel()
	 * @see #unsetIdleBaseFuel()
	 * @see #setIdleBaseFuel(BaseFuel)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVessel_IdleBaseFuel()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	BaseFuel getIdleBaseFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getIdleBaseFuel <em>Idle Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle Base Fuel</em>' reference.
	 * @see #isSetIdleBaseFuel()
	 * @see #unsetIdleBaseFuel()
	 * @see #getIdleBaseFuel()
	 * @generated
	 */
	void setIdleBaseFuel(BaseFuel value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getIdleBaseFuel <em>Idle Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIdleBaseFuel()
	 * @see #getIdleBaseFuel()
	 * @see #setIdleBaseFuel(BaseFuel)
	 * @generated
	 */
	void unsetIdleBaseFuel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.Vessel#getIdleBaseFuel <em>Idle Base Fuel</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Idle Base Fuel</em>' reference is set.
	 * @see #unsetIdleBaseFuel()
	 * @see #getIdleBaseFuel()
	 * @see #setIdleBaseFuel(BaseFuel)
	 * @generated
	 */
	boolean isSetIdleBaseFuel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getShortenedName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<APortSet<Port>> getVesselOrDelegateInaccessiblePorts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<RouteOption> getVesselOrDelegateInaccessibleRoutes();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	BaseFuel getVesselOrDelegateBaseFuel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	BaseFuel getVesselOrDelegateIdleBaseFuel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	BaseFuel getVesselOrDelegatePilotLightBaseFuel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	BaseFuel getVesselOrDelegateInPortBaseFuel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegateCapacity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateFillCapacity();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateMinSpeed();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateMaxSpeed();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegateSafetyHeel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegatePurgeTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegatePurgeVolume();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegateWarmingTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegateCoolingTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegateCoolingVolume();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<VesselRouteParameters> getVesselOrDelegateRouteParameters();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegatePilotLightRate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getVesselOrDelegateMinBaseFuelConsumption();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean getVesselOrDelegateHasReliqCapability();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	int getVesselOrDelegateSCNT();

} // end of  Vessel

// finish type fixing
