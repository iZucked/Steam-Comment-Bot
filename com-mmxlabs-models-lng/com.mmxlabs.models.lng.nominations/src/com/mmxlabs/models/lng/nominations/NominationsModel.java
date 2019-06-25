/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsModel#getNominationSpecs <em>Nomination Specs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsModel#getNominations <em>Nominations</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel()
 * @model
 * @generated
 */
public interface NominationsModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Nomination Specs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.nominations.AbstractNominationSpec}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nomination Specs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nomination Specs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel_NominationSpecs()
	 * @model containment="true"
	 * @generated
	 */
	EList<AbstractNominationSpec> getNominationSpecs();

	/**
	 * Returns the value of the '<em><b>Nominations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.nominations.AbstractNomination}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel_Nominations()
	 * @model containment="true"
	 * @generated
	 */
	EList<AbstractNomination> getNominations();

} // NominationsModel
