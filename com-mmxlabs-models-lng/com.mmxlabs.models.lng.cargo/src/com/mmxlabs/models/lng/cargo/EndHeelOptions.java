/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>End Heel Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#isEndCold <em>End Cold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel <em>Target End Heel</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions()
 * @model
 * @generated
 */
public interface EndHeelOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>End Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Cold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Cold</em>' attribute.
	 * @see #setEndCold(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions_EndCold()
	 * @model
	 * @generated
	 */
	boolean isEndCold();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#isEndCold <em>End Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Cold</em>' attribute.
	 * @see #isEndCold()
	 * @generated
	 */
	void setEndCold(boolean value);

	/**
	 * Returns the value of the '<em><b>Target End Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target End Heel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target End Heel</em>' attribute.
	 * @see #setTargetEndHeel(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions_TargetEndHeel()
	 * @model
	 * @generated
	 */
	int getTargetEndHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel <em>Target End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target End Heel</em>' attribute.
	 * @see #getTargetEndHeel()
	 * @generated
	 */
	void setTargetEndHeel(int value);

} // EndHeelOptions
