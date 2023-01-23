/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.mmxcore;

import java.time.Instant;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Version Record</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.VersionRecord#getCreatedBy <em>Created By</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.VersionRecord#getCreatedAt <em>Created At</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.VersionRecord#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getVersionRecord()
 * @model
 * @generated
 */
public interface VersionRecord extends EObject {
	/**
	 * Returns the value of the '<em><b>Created By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Created By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Created By</em>' attribute.
	 * @see #setCreatedBy(String)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getVersionRecord_CreatedBy()
	 * @model
	 * @generated
	 */
	String getCreatedBy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.VersionRecord#getCreatedBy <em>Created By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Created By</em>' attribute.
	 * @see #getCreatedBy()
	 * @generated
	 */
	void setCreatedBy(String value);

	/**
	 * Returns the value of the '<em><b>Created At</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Created At</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Created At</em>' attribute.
	 * @see #setCreatedAt(Instant)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getVersionRecord_CreatedAt()
	 * @model dataType="com.mmxlabs.models.datetime.Instant"
	 * @generated
	 */
	Instant getCreatedAt();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.VersionRecord#getCreatedAt <em>Created At</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Created At</em>' attribute.
	 * @see #getCreatedAt()
	 * @generated
	 */
	void setCreatedAt(Instant value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getVersionRecord_Version()
	 * @model
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.mmxcore.VersionRecord#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

} // VersionRecord
