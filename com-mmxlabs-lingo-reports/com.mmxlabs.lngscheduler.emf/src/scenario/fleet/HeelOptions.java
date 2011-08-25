/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Heel Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.HeelOptions#getHeelLimit <em>Heel Limit</em>}</li>
 *   <li>{@link scenario.fleet.HeelOptions#getHeelCVValue <em>Heel CV Value</em>}</li>
 *   <li>{@link scenario.fleet.HeelOptions#getHeelUnitPrice <em>Heel Unit Price</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getHeelOptions()
 * @model
 * @generated
 */
public interface HeelOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Heel Limit</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel Limit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel Limit</em>' attribute.
	 * @see #isSetHeelLimit()
	 * @see #unsetHeelLimit()
	 * @see #setHeelLimit(int)
	 * @see scenario.fleet.FleetPackage#getHeelOptions_HeelLimit()
	 * @model default="0" unsettable="true" required="true"
	 * @generated
	 */
	int getHeelLimit();

	/**
	 * Sets the value of the '{@link scenario.fleet.HeelOptions#getHeelLimit <em>Heel Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel Limit</em>' attribute.
	 * @see #isSetHeelLimit()
	 * @see #unsetHeelLimit()
	 * @see #getHeelLimit()
	 * @generated
	 */
	void setHeelLimit(int value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.HeelOptions#getHeelLimit <em>Heel Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetHeelLimit()
	 * @see #getHeelLimit()
	 * @see #setHeelLimit(int)
	 * @generated
	 */
	void unsetHeelLimit();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.HeelOptions#getHeelLimit <em>Heel Limit</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Heel Limit</em>' attribute is set.
	 * @see #unsetHeelLimit()
	 * @see #getHeelLimit()
	 * @see #setHeelLimit(int)
	 * @generated
	 */
	boolean isSetHeelLimit();

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
	 * @see scenario.fleet.FleetPackage#getHeelOptions_HeelCVValue()
	 * @model default="22.8" required="true"
	 * @generated
	 */
	float getHeelCVValue();

	/**
	 * Sets the value of the '{@link scenario.fleet.HeelOptions#getHeelCVValue <em>Heel CV Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel CV Value</em>' attribute.
	 * @see #getHeelCVValue()
	 * @generated
	 */
	void setHeelCVValue(float value);

	/**
	 * Returns the value of the '<em><b>Heel Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heel Unit Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel Unit Price</em>' attribute.
	 * @see #setHeelUnitPrice(float)
	 * @see scenario.fleet.FleetPackage#getHeelOptions_HeelUnitPrice()
	 * @model required="true"
	 * @generated
	 */
	float getHeelUnitPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.HeelOptions#getHeelUnitPrice <em>Heel Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel Unit Price</em>' attribute.
	 * @see #getHeelUnitPrice()
	 * @generated
	 */
	void setHeelUnitPrice(float value);

} // HeelOptions
