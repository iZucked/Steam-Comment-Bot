/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MMX Proxy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXProxy#getReferentID <em>Referent ID</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXProxy#getResolvedReferent <em>Resolved Referent</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXProxy#getReference <em>Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXProxy#getReferentOwner <em>Referent Owner</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXProxy#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXProxy#getReferentName <em>Referent Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXProxy()
 * @model
 * @generated
 */
public interface MMXProxy extends EObject {
	/**
	 * Returns the value of the '<em><b>Referent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referent ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referent ID</em>' attribute.
	 * @see #setReferentID(String)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXProxy_ReferentID()
	 * @model required="true"
	 * @generated
	 */
	String getReferentID();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXProxy#getReferentID <em>Referent ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referent ID</em>' attribute.
	 * @see #getReferentID()
	 * @generated
	 */
	void setReferentID(String value);

	/**
	 * Returns the value of the '<em><b>Resolved Referent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resolved Referent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resolved Referent</em>' reference.
	 * @see #setResolvedReferent(UUIDObject)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXProxy_ResolvedReferent()
	 * @model required="true" transient="true"
	 * @generated
	 */
	UUIDObject getResolvedReferent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXProxy#getResolvedReferent <em>Resolved Referent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resolved Referent</em>' reference.
	 * @see #getResolvedReferent()
	 * @generated
	 */
	void setResolvedReferent(UUIDObject value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' reference.
	 * @see #setReference(EReference)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXProxy_Reference()
	 * @model required="true"
	 * @generated
	 */
	EReference getReference();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXProxy#getReference <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(EReference value);

	/**
	 * Returns the value of the '<em><b>Referent Owner</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referent Owner</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referent Owner</em>' attribute.
	 * @see #setReferentOwner(String)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXProxy_ReferentOwner()
	 * @model required="true"
	 * @generated
	 */
	String getReferentOwner();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXProxy#getReferentOwner <em>Referent Owner</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referent Owner</em>' attribute.
	 * @see #getReferentOwner()
	 * @generated
	 */
	void setReferentOwner(String value);

	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #setIndex(int)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXProxy_Index()
	 * @model required="true"
	 * @generated
	 */
	int getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXProxy#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' attribute.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(int value);

	/**
	 * Returns the value of the '<em><b>Referent Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referent Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referent Name</em>' attribute.
	 * @see #setReferentName(String)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXProxy_ReferentName()
	 * @model required="true"
	 * @generated
	 */
	String getReferentName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.MMXProxy#getReferentName <em>Referent Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referent Name</em>' attribute.
	 * @see #getReferentName()
	 * @generated
	 */
	void setReferentName(String value);

} // MMXProxy
