/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.CharterOut#getMinHeelOut <em>Min Heel Out</em>}</li>
 *   <li>{@link scenario.fleet.CharterOut#getMaxHeelOut <em>Max Heel Out</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getCharterOut()
 * @model
 * @generated
 */
public interface CharterOut extends VesselEvent {

	/**
	 * Returns the value of the '<em><b>Min Heel Out</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Heel Out</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Heel Out</em>' attribute.
	 * @see #setMinHeelOut(int)
	 * @see scenario.fleet.FleetPackage#getCharterOut_MinHeelOut()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getMinHeelOut();

	/**
	 * Sets the value of the '{@link scenario.fleet.CharterOut#getMinHeelOut <em>Min Heel Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Heel Out</em>' attribute.
	 * @see #getMinHeelOut()
	 * @generated
	 */
	void setMinHeelOut(int value);

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
} // CharterOut
