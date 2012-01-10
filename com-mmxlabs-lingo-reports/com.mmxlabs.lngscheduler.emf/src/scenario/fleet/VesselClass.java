/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import org.eclipse.emf.common.util.EList;

import scenario.AnnotatedObject;
import scenario.NamedObject;
import scenario.port.PortSelection;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.VesselClass#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getMinHeelVolume <em>Min Heel Volume</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getDailyCharterInPrice <em>Daily Charter In Price</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getPilotLightRate <em>Pilot Light Rate</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getCanalCosts <em>Canal Costs</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getWarmupTime <em>Warmup Time</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getCooldownTime <em>Cooldown Time</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getCooldownVolume <em>Cooldown Volume</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVesselClass()
 * @model
 * @generated
 */
public interface VesselClass extends NamedObject, AnnotatedObject {
	/**
	 * Returns the value of the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity</em>' attribute.
	 * @see #setCapacity(long)
	 * @see scenario.fleet.FleetPackage#getVesselClass_Capacity()
	 * @model
	 * @generated
	 */
	long getCapacity();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity</em>' attribute.
	 * @see #getCapacity()
	 * @generated
	 */
	void setCapacity(long value);

	/**
	 * Returns the value of the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Speed</em>' attribute.
	 * @see #setMinSpeed(float)
	 * @see scenario.fleet.FleetPackage#getVesselClass_MinSpeed()
	 * @model
	 * @generated
	 */
	float getMinSpeed();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Speed</em>' attribute.
	 * @see #getMinSpeed()
	 * @generated
	 */
	void setMinSpeed(float value);

	/**
	 * Returns the value of the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Speed</em>' attribute.
	 * @see #setMaxSpeed(float)
	 * @see scenario.fleet.FleetPackage#getVesselClass_MaxSpeed()
	 * @model
	 * @generated
	 */
	float getMaxSpeed();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Speed</em>' attribute.
	 * @see #getMaxSpeed()
	 * @generated
	 */
	void setMaxSpeed(float value);

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
	 * @see scenario.fleet.FleetPackage#getVesselClass_LadenAttributes()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	VesselStateAttributes getLadenAttributes();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}' containment reference.
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
	 * @see scenario.fleet.FleetPackage#getVesselClass_BallastAttributes()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	VesselStateAttributes getBallastAttributes();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Attributes</em>' containment reference.
	 * @see #getBallastAttributes()
	 * @generated
	 */
	void setBallastAttributes(VesselStateAttributes value);

	/**
	 * Returns the value of the '<em><b>Min Heel Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The volume of cargo below which boil-off cannot be used for fuel. Some vessels cannot access LNG below a certain level in their tanks for fuel.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min Heel Volume</em>' attribute.
	 * @see #setMinHeelVolume(long)
	 * @see scenario.fleet.FleetPackage#getVesselClass_MinHeelVolume()
	 * @model
	 * @generated
	 */
	long getMinHeelVolume();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getMinHeelVolume <em>Min Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Heel Volume</em>' attribute.
	 * @see #getMinHeelVolume()
	 * @generated
	 */
	void setMinHeelVolume(long value);

	/**
	 * Returns the value of the '<em><b>Fill Capacity</b></em>' attribute.
	 * The default value is <code>"0.958"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The ship fill capacity, as a proportion (from 0 to 1). Ship fill capacity is the percentage of vessel cargo capacity that can actually be used. Typically this is 98.5%.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fill Capacity</em>' attribute.
	 * @see #setFillCapacity(Double)
	 * @see scenario.fleet.FleetPackage#getVesselClass_FillCapacity()
	 * @model default="0.958" dataType="scenario.Percentage"
	 * @generated
	 */
	Double getFillCapacity();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fill Capacity</em>' attribute.
	 * @see #getFillCapacity()
	 * @generated
	 */
	void setFillCapacity(Double value);

	/**
	 * Returns the value of the '<em><b>Daily Charter Out Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The cost per day of spot chartering vessels of this class, expressed in dollars
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Daily Charter Out Price</em>' attribute.
	 * @see #isSetDailyCharterOutPrice()
	 * @see #unsetDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_DailyCharterOutPrice()
	 * @model unsettable="true"
	 * @generated
	 */
	int getDailyCharterOutPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Daily Charter Out Price</em>' attribute.
	 * @see #isSetDailyCharterOutPrice()
	 * @see #unsetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 */
	void setDailyCharterOutPrice(int value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.VesselClass#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @generated
	 */
	void unsetDailyCharterOutPrice();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.VesselClass#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Daily Charter Out Price</em>' attribute is set.
	 * @see #unsetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @generated
	 */
	boolean isSetDailyCharterOutPrice();

	/**
	 * Returns the value of the '<em><b>Spot Charter Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Charter Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #setSpotCharterCount(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_SpotCharterCount()
	 * @model
	 * @generated
	 */
	int getSpotCharterCount();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getSpotCharterCount <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #getSpotCharterCount()
	 * @generated
	 */
	void setSpotCharterCount(int value);

	/**
	 * Returns the value of the '<em><b>Daily Charter In Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The cost per day of spot chartering vessels of this class, expressed in dollars
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Daily Charter In Price</em>' attribute.
	 * @see #setDailyCharterInPrice(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_DailyCharterInPrice()
	 * @model
	 * @generated
	 */
	int getDailyCharterInPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getDailyCharterInPrice <em>Daily Charter In Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Daily Charter In Price</em>' attribute.
	 * @see #getDailyCharterInPrice()
	 * @generated
	 */
	void setDailyCharterInPrice(int value);

	/**
	 * Returns the value of the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * The list contents are of type {@link scenario.port.PortSelection}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A list of ports where this vessel can't dock because it is too large in some way.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Inaccessible Ports</em>' reference list.
	 * @see scenario.fleet.FleetPackage#getVesselClass_InaccessiblePorts()
	 * @model
	 * @generated
	 */
	EList<PortSelection> getInaccessiblePorts();

	/**
	 * Returns the value of the '<em><b>Canal Costs</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.fleet.VesselClassCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Costs</em>' containment reference list.
	 * @see scenario.fleet.FleetPackage#getVesselClass_CanalCosts()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselClassCost> getCanalCosts();

	/**
	 * Returns the value of the '<em><b>Warmup Time</b></em>' attribute.
	 * The default value is <code>"24"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Warmup Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Warmup Time</em>' attribute.
	 * @see #setWarmupTime(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_WarmupTime()
	 * @model default="24" required="true"
	 * @generated
	 */
	int getWarmupTime();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getWarmupTime <em>Warmup Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Warmup Time</em>' attribute.
	 * @see #getWarmupTime()
	 * @generated
	 */
	void setWarmupTime(int value);

	/**
	 * Returns the value of the '<em><b>Cooldown Time</b></em>' attribute.
	 * The default value is <code>"12"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooldown Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooldown Time</em>' attribute.
	 * @see #setCooldownTime(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_CooldownTime()
	 * @model default="12" required="true"
	 * @generated
	 */
	int getCooldownTime();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getCooldownTime <em>Cooldown Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooldown Time</em>' attribute.
	 * @see #getCooldownTime()
	 * @generated
	 */
	void setCooldownTime(int value);

	/**
	 * Returns the value of the '<em><b>Cooldown Volume</b></em>' attribute.
	 * The default value is <code>"500"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooldown Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooldown Volume</em>' attribute.
	 * @see #setCooldownVolume(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_CooldownVolume()
	 * @model default="500" required="true"
	 * @generated
	 */
	int getCooldownVolume();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getCooldownVolume <em>Cooldown Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooldown Volume</em>' attribute.
	 * @see #getCooldownVolume()
	 * @generated
	 */
	void setCooldownVolume(int value);

	/**
	 * Returns the value of the '<em><b>Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel</em>' reference.
	 * @see #setBaseFuel(VesselFuel)
	 * @see scenario.fleet.FleetPackage#getVesselClass_BaseFuel()
	 * @model required="true"
	 * @generated
	 */
	VesselFuel getBaseFuel();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel</em>' reference.
	 * @see #getBaseFuel()
	 * @generated
	 */
	void setBaseFuel(VesselFuel value);

	/**
	 * Returns the value of the '<em><b>Pilot Light Rate</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pilot Light Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pilot Light Rate</em>' attribute.
	 * @see #setPilotLightRate(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_PilotLightRate()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getPilotLightRate();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getPilotLightRate <em>Pilot Light Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pilot Light Rate</em>' attribute.
	 * @see #getPilotLightRate()
	 * @generated
	 */
	void setPilotLightRate(int value);

} // VesselClass
