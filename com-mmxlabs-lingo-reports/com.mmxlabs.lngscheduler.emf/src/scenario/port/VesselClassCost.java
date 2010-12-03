/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.port;

import org.eclipse.emf.ecore.EObject;

import scenario.fleet.VesselClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.VesselClassCost#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link scenario.port.VesselClassCost#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link scenario.port.VesselClassCost#getUnladenCost <em>Unladen Cost</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getVesselClassCost()
 * @model
 * @generated
 */
public interface VesselClassCost extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Class</em>' reference.
	 * @see #setVesselClass(VesselClass)
	 * @see scenario.port.PortPackage#getVesselClassCost_VesselClass()
	 * @model required="true"
	 * @generated
	 */
	VesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(VesselClass value);

	/**
	 * Returns the value of the '<em><b>Laden Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Cost</em>' attribute.
	 * @see #setLadenCost(int)
	 * @see scenario.port.PortPackage#getVesselClassCost_LadenCost()
	 * @model
	 * @generated
	 */
	int getLadenCost();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getLadenCost <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Cost</em>' attribute.
	 * @see #getLadenCost()
	 * @generated
	 */
	void setLadenCost(int value);

	/**
	 * Returns the value of the '<em><b>Unladen Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unladen Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unladen Cost</em>' attribute.
	 * @see #setUnladenCost(int)
	 * @see scenario.port.PortPackage#getVesselClassCost_UnladenCost()
	 * @model
	 * @generated
	 */
	int getUnladenCost();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getUnladenCost <em>Unladen Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unladen Cost</em>' attribute.
	 * @see #getUnladenCost()
	 * @generated
	 */
	void setUnladenCost(int value);

} // VesselClassCost
