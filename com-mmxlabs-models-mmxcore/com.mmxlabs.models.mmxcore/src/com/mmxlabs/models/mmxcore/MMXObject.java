/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import java.util.Map;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MMX Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXObject#getExtensions <em>Extensions</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXObject#getProxies <em>Proxies</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXObject()
 * @model
 * @generated
 */
public interface MMXObject extends EObject {
	/**
	 * Returns the value of the '<em><b>Extensions</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.mmxcore.UUIDObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extensions</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extensions</em>' reference list.
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXObject_Extensions()
	 * @model ordered="false"
	 * @generated
	 */
	EList<UUIDObject> getExtensions();

	/**
	 * Returns the value of the '<em><b>Proxies</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.mmxcore.MMXProxy}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proxies</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proxies</em>' containment reference list.
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXObject_Proxies()
	 * @model containment="true"
	 * @generated
	 */
	EList<MMXProxy> getProxies();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void makeProxies();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model objectsByUUIDRequired="true"
	 * @generated
	 */
	void resolveProxies(Map<String, UUIDObject> objectsByUUID);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void restoreProxies();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model objectsByUUIDRequired="true"
	 * @generated
	 */
	void collectUUIDObjects(Map<String, UUIDObject> objectsByUUID);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	Map<String, UUIDObject> collectUUIDObjects();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" featureRequired="true"
	 * @generated
	 */
	Object getUnsetValue(EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" featureRequired="true"
	 * @generated
	 */
	Object eGetWithDefault(EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	EObject eContainerOp();

} // MMXObject
