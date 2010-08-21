/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

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
	 * @see #setMinSpeed(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_MinSpeed()
	 * @model
	 * @generated
	 */
	int getMinSpeed();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Speed</em>' attribute.
	 * @see #getMinSpeed()
	 * @generated
	 */
	void setMinSpeed(int value);

	/**
	 * Returns the value of the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Speed</em>' attribute.
	 * @see #setMaxSpeed(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_MaxSpeed()
	 * @model
	 * @generated
	 */
	int getMaxSpeed();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Speed</em>' attribute.
	 * @see #getMaxSpeed()
	 * @generated
	 */
	void setMaxSpeed(int value);

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
	 * @return the value of the '<em>Min Heel Volume</em>' attribute.
	 * @see #setMinHeelVolume(int)
	 * @see scenario.fleet.FleetPackage#getVesselClass_MinHeelVolume()
	 * @model
	 * @generated
	 */
	int getMinHeelVolume();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselClass#getMinHeelVolume <em>Min Heel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Heel Volume</em>' attribute.
	 * @see #getMinHeelVolume()
	 * @generated
	 */
	void setMinHeelVolume(int value);

} // VesselClass
