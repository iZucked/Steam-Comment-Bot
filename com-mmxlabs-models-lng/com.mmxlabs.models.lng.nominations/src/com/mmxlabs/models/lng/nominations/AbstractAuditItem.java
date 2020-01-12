/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Audit Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.AbstractAuditItem#getAuditItemType <em>Audit Item Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractAuditItem()
 * @model
 * @generated
 */
public interface AbstractAuditItem extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Audit Item Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.nominations.AuditItemType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Audit Item Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Audit Item Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.AuditItemType
	 * @see #setAuditItemType(AuditItemType)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getAbstractAuditItem_AuditItemType()
	 * @model
	 * @generated
	 */
	AuditItemType getAuditItemType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.AbstractAuditItem#getAuditItemType <em>Audit Item Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Audit Item Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.nominations.AuditItemType
	 * @see #getAuditItemType()
	 * @generated
	 */
	void setAuditItemType(AuditItemType value);

} // AbstractAuditItem
