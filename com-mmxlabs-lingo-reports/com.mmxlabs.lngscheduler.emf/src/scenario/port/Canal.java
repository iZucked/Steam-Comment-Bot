/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.port;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.Canal#getName <em>Name</em>}</li>
 *   <li>{@link scenario.port.Canal#getClassCosts <em>Class Costs</em>}</li>
 *   <li>{@link scenario.port.Canal#getDefaultCost <em>Default Cost</em>}</li>
 *   <li>{@link scenario.port.Canal#getDistanceModel <em>Distance Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getCanal()
 * @model
 * @generated
 */
public interface Canal extends EObject {
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
	 * @see scenario.port.PortPackage#getCanal_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.port.Canal#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Class Costs</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.port.VesselClassCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class Costs</em>' containment reference list.
	 * @see scenario.port.PortPackage#getCanal_ClassCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselClassCost> getClassCosts();

	/**
	 * Returns the value of the '<em><b>Default Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Cost</em>' attribute.
	 * @see #setDefaultCost(int)
	 * @see scenario.port.PortPackage#getCanal_DefaultCost()
	 * @model
	 * @generated
	 */
	int getDefaultCost();

	/**
	 * Sets the value of the '{@link scenario.port.Canal#getDefaultCost <em>Default Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Cost</em>' attribute.
	 * @see #getDefaultCost()
	 * @generated
	 */
	void setDefaultCost(int value);

	/**
	 * Returns the value of the '<em><b>Distance Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance Model</em>' containment reference.
	 * @see #setDistanceModel(DistanceModel)
	 * @see scenario.port.PortPackage#getCanal_DistanceModel()
	 * @model containment="true" required="true"
	 * @generated
	 */
	DistanceModel getDistanceModel();

	/**
	 * Sets the value of the '{@link scenario.port.Canal#getDistanceModel <em>Distance Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance Model</em>' containment reference.
	 * @see #getDistanceModel()
	 * @generated
	 */
	void setDistanceModel(DistanceModel value);

} // Canal
