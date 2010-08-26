/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
 *   <li>{@link scenario.port.Canal#getDistance <em>Distance</em>}</li>
 *   <li>{@link scenario.port.Canal#getEntryDistances <em>Entry Distances</em>}</li>
 *   <li>{@link scenario.port.Canal#getExitDistances <em>Exit Distances</em>}</li>
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
	 * Returns the value of the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance</em>' attribute.
	 * @see #setDistance(int)
	 * @see scenario.port.PortPackage#getCanal_Distance()
	 * @model
	 * @generated
	 */
	int getDistance();

	/**
	 * Sets the value of the '{@link scenario.port.Canal#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(int value);

	/**
	 * Returns the value of the '<em><b>Entry Distances</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.port.PartialDistance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entry Distances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entry Distances</em>' containment reference list.
	 * @see scenario.port.PortPackage#getCanal_EntryDistances()
	 * @model containment="true"
	 * @generated
	 */
	EList<PartialDistance> getEntryDistances();

	/**
	 * Returns the value of the '<em><b>Exit Distances</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.port.PartialDistance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exit Distances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exit Distances</em>' containment reference list.
	 * @see scenario.port.PortPackage#getCanal_ExitDistances()
	 * @model containment="true"
	 * @generated
	 */
	EList<PartialDistance> getExitDistances();

} // Canal
