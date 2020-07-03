/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lngdataserver.browser;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.Node#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.Node#getParent <em>Parent</em>}</li>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.Node#isPublished <em>Published</em>}</li>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.Node#getVersionIdentifier <em>Version Identifier</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getNode()
 * @model abstract="true"
 * @generated
 */
public interface Node extends EObject {
	/**
	 * Returns the value of the '<em><b>Display Name</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Display Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Display Name</em>' attribute.
	 * @see #setDisplayName(String)
	 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getNode_DisplayName()
	 * @model default=""
	 * @generated
	 */
	String getDisplayName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lngdataserver.browser.Node#getDisplayName <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Display Name</em>' attribute.
	 * @see #getDisplayName()
	 * @generated
	 */
	void setDisplayName(String value);

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' reference.
	 * @see #setParent(CompositeNode)
	 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getNode_Parent()
	 * @model
	 * @generated
	 */
	CompositeNode getParent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lngdataserver.browser.Node#getParent <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(CompositeNode value);

	/**
	 * Returns the value of the '<em><b>Published</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Published</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Published</em>' attribute.
	 * @see #setPublished(boolean)
	 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getNode_Published()
	 * @model
	 * @generated
	 */
	boolean isPublished();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lngdataserver.browser.Node#isPublished <em>Published</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Published</em>' attribute.
	 * @see #isPublished()
	 * @generated
	 */
	void setPublished(boolean value);

	/**
	 * Returns the value of the '<em><b>Version Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version Identifier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version Identifier</em>' attribute.
	 * @see #setVersionIdentifier(String)
	 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getNode_VersionIdentifier()
	 * @model
	 * @generated
	 */
	String getVersionIdentifier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lngdataserver.browser.Node#getVersionIdentifier <em>Version Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version Identifier</em>' attribute.
	 * @see #getVersionIdentifier()
	 * @generated
	 */
	void setVersionIdentifier(String value);

} // Node
