/**
 */
package com.mmxlabs.lngdataserver.browser;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.CompositeNode#getChildren <em>Children</em>}</li>
 *   <li>{@link com.mmxlabs.lngdataserver.browser.CompositeNode#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getCompositeNode()
 * @model
 * @generated
 */
public interface CompositeNode extends Node {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lngdataserver.browser.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getCompositeNode_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getChildren();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see com.mmxlabs.lngdataserver.browser.BrowserPackage#getCompositeNode_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lngdataserver.browser.CompositeNode#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

} // CompositeNode
