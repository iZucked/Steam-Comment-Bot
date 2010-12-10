/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.fleet;

import org.eclipse.emf.common.util.EList;
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
 *   <li>{@link scenario.fleet.Vessel#getStartRequirement <em>Start Requirement</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getEndRequirement <em>End Requirement</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#isTimeChartered <em>Time Chartered</em>}</li>
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
	 * Returns the value of the '<em><b>Start Requirement</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Requirement</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Requirement</em>' containment reference.
	 * @see #setStartRequirement(PortAndTime)
	 * @see scenario.fleet.FleetPackage#getVessel_StartRequirement()
	 * @model containment="true" required="true"
	 * @generated
	 */
	PortAndTime getStartRequirement();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getStartRequirement <em>Start Requirement</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Requirement</em>' containment reference.
	 * @see #getStartRequirement()
	 * @generated
	 */
	void setStartRequirement(PortAndTime value);

	/**
	 * Returns the value of the '<em><b>End Requirement</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Requirement</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Requirement</em>' containment reference.
	 * @see #setEndRequirement(PortAndTime)
	 * @see scenario.fleet.FleetPackage#getVessel_EndRequirement()
	 * @model containment="true" required="true"
	 * @generated
	 */
	PortAndTime getEndRequirement();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getEndRequirement <em>End Requirement</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Requirement</em>' containment reference.
	 * @see #getEndRequirement()
	 * @generated
	 */
	void setEndRequirement(PortAndTime value);

	/**
	 * Returns the value of the '<em><b>Time Chartered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Chartered</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Chartered</em>' attribute.
	 * @see #setTimeChartered(boolean)
	 * @see scenario.fleet.FleetPackage#getVessel_TimeChartered()
	 * @model required="true"
	 * @generated
	 */
	boolean isTimeChartered();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#isTimeChartered <em>Time Chartered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Chartered</em>' attribute.
	 * @see #isTimeChartered()
	 * @generated
	 */
	void setTimeChartered(boolean value);

} // Vessel
