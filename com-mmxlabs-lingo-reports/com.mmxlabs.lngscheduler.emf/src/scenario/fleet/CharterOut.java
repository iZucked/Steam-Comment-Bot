/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import scenario.port.Port;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.CharterOut#getMaxHeelOut <em>Max Heel Out</em>}</li>
 *   <li>{@link scenario.fleet.CharterOut#getHeelCVValue <em>Heel CV Value</em>}</li>
 *   <li>{@link scenario.fleet.CharterOut#getEndPort <em>End Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getCharterOut()
 * @model
 * @generated
 */
public interface CharterOut extends VesselEvent {

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
	 * @see scenario.fleet.FleetPackage#getCharterOut_EndPort()
	 * @model required="true"
	 * @generated
	 */
	Port getEndPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.CharterOut#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Port</em>' reference.
	 * @see #getEndPort()
	 * @generated
	 */
	void setEndPort(Port value);

	/**
	 * Returns the value of the '<em><b>Max Heel Out</b></em>' attribute.
	 * The default value is <code>"2147483647"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Heel Out</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Heel Out</em>' attribute.
	 * @see #setMaxHeelOut(int)
	 * @see scenario.fleet.FleetPackage#getCharterOut_MaxHeelOut()
	 * @model default="2147483647" required="true"
	 * @generated
	 */
	int getMaxHeelOut();

	/**
	 * Sets the value of the '{@link scenario.fleet.CharterOut#getMaxHeelOut <em>Max Heel Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Heel Out</em>' attribute.
	 * @see #getMaxHeelOut()
	 * @generated
	 */
	void setMaxHeelOut(int value);

	/**
	 * Returns the value of the '<em><b>Heel CV Value</b></em>' attribute.
	 * The default value is <code>"22.8"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel CV Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel CV Value</em>' attribute.
	 * @see #setHeelCVValue(float)
	 * @see scenario.fleet.FleetPackage#getCharterOut_HeelCVValue()
	 * @model default="22.8" required="true"
	 * @generated
	 */
	float getHeelCVValue();

	/**
	 * Sets the value of the '{@link scenario.fleet.CharterOut#getHeelCVValue <em>Heel CV Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel CV Value</em>' attribute.
	 * @see #getHeelCVValue()
	 * @generated
	 */
	void setHeelCVValue(float value);
} // CharterOut
