/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.port;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Other Identifiers</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.OtherIdentifiers#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.OtherIdentifiers#getProvider <em>Provider</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getOtherIdentifiers()
 * @model
 * @generated
 */
public interface OtherIdentifiers extends EObject {
	/**
	 * Returns the value of the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Identifier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Identifier</em>' attribute.
	 * @see #setIdentifier(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getOtherIdentifiers_Identifier()
	 * @model
	 * @generated
	 */
	String getIdentifier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.OtherIdentifiers#getIdentifier <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identifier</em>' attribute.
	 * @see #getIdentifier()
	 * @generated
	 */
	void setIdentifier(String value);

	/**
	 * Returns the value of the '<em><b>Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provider</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provider</em>' attribute.
	 * @see #setProvider(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getOtherIdentifiers_Provider()
	 * @model
	 * @generated
	 */
	String getProvider();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.OtherIdentifiers#getProvider <em>Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provider</em>' attribute.
	 * @see #getProvider()
	 * @generated
	 */
	void setProvider(String value);

} // OtherIdentifiers
