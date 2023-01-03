/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Nomination Audit Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationAuditItem#getNomination <em>Nomination</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationAuditItem()
 * @model
 * @generated
 */
public interface NominationAuditItem extends AbstractAuditItem {
	/**
	 * Returns the value of the '<em><b>Nomination</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nomination</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nomination</em>' reference.
	 * @see #setNomination(AbstractNomination)
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationAuditItem_Nomination()
	 * @model
	 * @generated
	 */
	AbstractNomination getNomination();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.nominations.NominationAuditItem#getNomination <em>Nomination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nomination</em>' reference.
	 * @see #getNomination()
	 * @generated
	 */
	void setNomination(AbstractNomination value);

} // NominationAuditItem
