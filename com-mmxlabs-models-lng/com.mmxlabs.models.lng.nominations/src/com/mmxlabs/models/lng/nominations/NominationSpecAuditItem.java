/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Nomination Spec Audit Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationSpecAuditItem#getNominationSpec <em>Nomination Spec</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationSpecAuditItem()
 * @model
 * @generated
 */
public interface NominationSpecAuditItem extends AbstractAuditItem {
	/**
	 * Returns the value of the '<em><b>Nomination Spec</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nomination Spec</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nomination Spec</em>' reference.
	 * @see #setNominationSpec(AbstractNominationSpec)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationSpecAuditItem_NominationSpec()
	 * @model
	 * @generated
	 */
	AbstractNominationSpec getNominationSpec();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.NominationSpecAuditItem#getNominationSpec <em>Nomination Spec</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nomination Spec</em>' reference.
	 * @see #getNominationSpec()
	 * @generated
	 */
	void setNominationSpec(AbstractNominationSpec value);

} // NominationSpecAuditItem
