/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>UUID Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.UUIDObject#getUUID <em>UUID</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.ScenarioPackage#getUUIDObject()
 * @model abstract="true"
 * @generated
 */
public interface UUIDObject extends EObject {
	/**
	 * Returns the value of the '<em><b>UUID</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>UUID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>UUID</em>' attribute.
	 * @see #setUUID(String)
	 * @see scenario.ScenarioPackage#getUUIDObject_UUID()
	 * @model default="" id="true" required="true"
	 * @generated
	 */
	String getUUID();

	/**
	 * Sets the value of the '{@link scenario.UUIDObject#getUUID <em>UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UUID</em>' attribute.
	 * @see #getUUID()
	 * @generated
	 */
	void setUUID(String value);

} // UUIDObject
