/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.types;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ALocated</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.types.ALocated#getPort <em>Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getALocated()
 * @model abstract="true"
 * @generated
 */
public interface ALocated extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(APort)
	 * @see com.mmxlabs.models.lng.types.TypesPackage#getALocated_Port()
	 * @model required="true"
	 * @generated
	 */
	APort getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.types.ALocated#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(APort value);

} // ALocated
