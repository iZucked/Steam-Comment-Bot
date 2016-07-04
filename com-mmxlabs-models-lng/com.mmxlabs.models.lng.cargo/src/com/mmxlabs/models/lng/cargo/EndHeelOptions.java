/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
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
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel <em>Target End Heel</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions()
 * @model
 * @generated
 */
public interface EndHeelOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Target End Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target End Heel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target End Heel</em>' attribute.
	 * @see #isSetTargetEndHeel()
	 * @see #unsetTargetEndHeel()
	 * @see #setTargetEndHeel(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions_TargetEndHeel()
	 * @model unsettable="true"
	 * @generated
	 */
	int getTargetEndHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel <em>Target End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target End Heel</em>' attribute.
	 * @see #isSetTargetEndHeel()
	 * @see #unsetTargetEndHeel()
	 * @see #getTargetEndHeel()
	 * @generated
	 */
	void setTargetEndHeel(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel <em>Target End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTargetEndHeel()
	 * @see #getTargetEndHeel()
	 * @see #setTargetEndHeel(int)
	 * @generated
	 */
	void unsetTargetEndHeel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel <em>Target End Heel</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Target End Heel</em>' attribute is set.
	 * @see #unsetTargetEndHeel()
	 * @see #getTargetEndHeel()
	 * @see #setTargetEndHeel(int)
	 * @generated
	 */
	boolean isSetTargetEndHeel();

} // EndHeelOptions
