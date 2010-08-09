/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import org.eclipse.emf.ecore.EObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.Vessel#getName <em>Name</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getClass_ <em>Class</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getStartPort <em>Start Port</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getEndPort <em>End Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVessel()
 * @model
 * @generated
 */
public interface Vessel extends EObject {
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
	 * @see scenario.fleet.FleetPackage#getVessel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class</em>' reference.
	 * @see #setClass(VesselClass)
	 * @see scenario.fleet.FleetPackage#getVessel_Class()
	 * @model
	 * @generated
	 */
	VesselClass getClass_();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getClass_ <em>Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' reference.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(VesselClass value);

	/**
	 * Returns the value of the '<em><b>Start Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Port</em>' reference.
	 * @see #setStartPort(Port)
	 * @see scenario.fleet.FleetPackage#getVessel_StartPort()
	 * @model
	 * @generated
	 */
	Port getStartPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getStartPort <em>Start Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Port</em>' reference.
	 * @see #getStartPort()
	 * @generated
	 */
	void setStartPort(Port value);

	/**
	 * Returns the value of the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Port</em>' reference.
	 * @see #setEndPort(Port)
	 * @see scenario.fleet.FleetPackage#getVessel_EndPort()
	 * @model
	 * @generated
	 */
	Port getEndPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Port</em>' reference.
	 * @see #getEndPort()
	 * @generated
	 */
	void setEndPort(Port value);

} // Vessel
