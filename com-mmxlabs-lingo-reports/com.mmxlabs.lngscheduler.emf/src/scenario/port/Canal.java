/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import scenario.NamedObject;
import scenario.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.Canal#getDefaultCost <em>Default Cost</em>}</li>
 *   <li>{@link scenario.port.Canal#getDistanceModel <em>Distance Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getCanal()
 * @model
 * @generated
 */
public interface Canal extends UUIDObject, NamedObject {
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
	 * @model containment="true" resolveProxies="true" required="true"
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
