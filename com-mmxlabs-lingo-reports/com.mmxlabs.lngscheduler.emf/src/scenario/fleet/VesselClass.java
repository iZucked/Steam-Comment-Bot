/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.VesselClass#getName <em>Name</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getBaseFuelUnitPrice <em>Base Fuel Unit Price</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getMinHeelVolume <em>Min Heel Volume</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getDailyCharterPrice <em>Daily Charter Price</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getBaseFuelEquivalenceFactor <em>Base Fuel Equivalence Factor</em>}</li>
 *   <li>{@link scenario.fleet.VesselClass#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVesselClass()
 * @model
 * @generated
 */
public interface VesselClass extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see scenario.fleet.FleetPackage#getVesselClass_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * Returns the value of the '<em><b>Base Fuel Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Unit Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Unit Price</em>' attribute.
	 * @see #setBaseFuelUnitPrice(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_BaseFuelUnitPrice()
	 * @model
	 * @generated
	 */
	int getBaseFuelUnitPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getBaseFuelUnitPrice <em>Base Fuel Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Unit Price</em>' attribute.
	 * @see #getBaseFuelUnitPrice()
	 * @generated
	 */
	void setBaseFuelUnitPrice(int value);

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
	 * @model containment="true"
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
	 * @model containment="true"
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
	 * <p>
	 * If the meaning of the '<em>Min Heel Volume</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
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
	 * @see #setFillCapacity(float)
	 * @see scenario.fleet.FleetPackage#getVesselClass_FillCapacity()
	 * @model default="0.958"
	 * @generated
	 */
	float getFillCapacity();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fill Capacity</em>' attribute.
	 * @see #getFillCapacity()
	 * @generated
	 */
	void setFillCapacity(float value);

	/**
	 * Returns the value of the '<em><b>Daily Charter Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The cost per day of spot chartering vessels of this class, expressed in dollars
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Daily Charter Price</em>' attribute.
	 * @see #setDailyCharterPrice(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_DailyCharterPrice()
	 * @model
	 * @generated
	 */
	int getDailyCharterPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getDailyCharterPrice <em>Daily Charter Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Daily Charter Price</em>' attribute.
	 * @see #getDailyCharterPrice()
	 * @generated
	 */
	void setDailyCharterPrice(int value);

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
	 * Returns the value of the '<em><b>Base Fuel Equivalence Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The factor relating boiloff to base fuel; one unit of boiloff is equivalent to this many units of base fuel.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Base Fuel Equivalence Factor</em>' attribute.
	 * @see #setBaseFuelEquivalenceFactor(double)
	 * @see scenario.fleet.FleetPackage#getVesselClass_BaseFuelEquivalenceFactor()
	 * @model
	 * @generated
	 */
	double getBaseFuelEquivalenceFactor();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getBaseFuelEquivalenceFactor <em>Base Fuel Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Equivalence Factor</em>' attribute.
	 * @see #getBaseFuelEquivalenceFactor()
	 * @generated
	 */
	void setBaseFuelEquivalenceFactor(double value);

	/**
	 * Returns the value of the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * The list contents are of type {@link scenario.port.Port}.
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
	EList<Port> getInaccessiblePorts();

} // VesselClass
