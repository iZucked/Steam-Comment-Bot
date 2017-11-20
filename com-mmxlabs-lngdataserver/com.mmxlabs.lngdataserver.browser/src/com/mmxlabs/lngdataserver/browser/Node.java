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
 * </ul>
 *
 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getNode()
 * @model
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
	 * @see #setParent(Node)
	 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getNode_Parent()
	 * @model
	 * @generated
	 */
	Node getParent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lngdataserver.browser.Node#getParent <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Node value);

} // Node
