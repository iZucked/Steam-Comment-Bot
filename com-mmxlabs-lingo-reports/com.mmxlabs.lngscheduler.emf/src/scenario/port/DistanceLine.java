/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Distance Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.DistanceLine#getFromPort <em>From Port</em>}</li>
 *   <li>{@link scenario.port.DistanceLine#getToPort <em>To Port</em>}</li>
 *   <li>{@link scenario.port.DistanceLine#getDistance <em>Distance</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getDistanceLine()
 * @model
 * @generated
 */
public interface DistanceLine extends EObject {
	/**
	 * Returns the value of the '<em><b>From Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Port</em>' reference.
	 * @see #setFromPort(Port)
	 * @see scenario.port.PortPackage#getDistanceLine_FromPort()
	 * @model
	 * @generated
	 */
	Port getFromPort();

	/**
	 * Sets the value of the '{@link scenario.port.DistanceLine#getFromPort <em>From Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Port</em>' reference.
	 * @see #getFromPort()
	 * @generated
	 */
	void setFromPort(Port value);

	/**
	 * Returns the value of the '<em><b>To Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Port</em>' reference.
	 * @see #setToPort(Port)
	 * @see scenario.port.PortPackage#getDistanceLine_ToPort()
	 * @model
	 * @generated
	 */
	Port getToPort();

	/**
	 * Sets the value of the '{@link scenario.port.DistanceLine#getToPort <em>To Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Port</em>' reference.
	 * @see #getToPort()
	 * @generated
	 */
	void setToPort(Port value);

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
	 * @see scenario.port.PortPackage#getDistanceLine_Distance()
	 * @model
	 * @generated
	 */
	int getDistance();

	/**
	 * Sets the value of the '{@link scenario.port.DistanceLine#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(int value);

} // DistanceLine
