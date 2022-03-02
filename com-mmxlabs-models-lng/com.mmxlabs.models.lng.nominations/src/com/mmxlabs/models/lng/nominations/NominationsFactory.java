/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage
 * @generated
 */
public interface NominationsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	NominationsFactory eINSTANCE = com.mmxlabs.models.lng.nominations.impl.NominationsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	NominationsModel createNominationsModel();

	/**
	 * Returns a new object of class '<em>Slot Nomination Spec</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot Nomination Spec</em>'.
	 * @generated
	 */
	SlotNominationSpec createSlotNominationSpec();

	/**
	 * Returns a new object of class '<em>Slot Nomination</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot Nomination</em>'.
	 * @generated
	 */
	SlotNomination createSlotNomination();

	/**
	 * Returns a new object of class '<em>Contract Nomination</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Contract Nomination</em>'.
	 * @generated
	 */
	ContractNomination createContractNomination();

	/**
	 * Returns a new object of class '<em>Contract Nomination Spec</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Contract Nomination Spec</em>'.
	 * @generated
	 */
	ContractNominationSpec createContractNominationSpec();

	/**
	 * Returns a new object of class '<em>Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameters</em>'.
	 * @generated
	 */
	NominationsParameters createNominationsParameters();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	NominationsPackage getNominationsPackage();

} //NominationsFactory
