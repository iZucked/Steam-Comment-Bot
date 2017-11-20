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

} // CompositeNode
