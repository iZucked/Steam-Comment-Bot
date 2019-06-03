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
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsModel#getSlotNominationSpecs <em>Slot Nomination Specs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsModel#getSlotNominations <em>Slot Nominations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsModel#getContractNominationSpecs <em>Contract Nomination Specs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.NominationsModel#getContractNominations <em>Contract Nominations</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel()
 * @model
 * @generated
 */
public interface NominationsModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Slot Nomination Specs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.nominations.SlotNominationSpec}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Nomination Specs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Nomination Specs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel_SlotNominationSpecs()
	 * @model containment="true"
	 * @generated
	 */
	EList<SlotNominationSpec> getSlotNominationSpecs();

	/**
	 * Returns the value of the '<em><b>Slot Nominations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.nominations.SlotNomination}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Nominations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Nominations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel_SlotNominations()
	 * @model containment="true"
	 * @generated
	 */
	EList<SlotNomination> getSlotNominations();

	/**
	 * Returns the value of the '<em><b>Contract Nomination Specs</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.nominations.ContractNominationSpec}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Nomination Specs</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Nomination Specs</em>' reference list.
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel_ContractNominationSpecs()
	 * @model
	 * @generated
	 */
	EList<ContractNominationSpec> getContractNominationSpecs();

	/**
	 * Returns the value of the '<em><b>Contract Nominations</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.nominations.ContractNomination}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Nominations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Nominations</em>' reference list.
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#getNominationsModel_ContractNominations()
	 * @model
	 * @generated
	 */
	EList<ContractNomination> getContractNominations();

} // NominationsModel
