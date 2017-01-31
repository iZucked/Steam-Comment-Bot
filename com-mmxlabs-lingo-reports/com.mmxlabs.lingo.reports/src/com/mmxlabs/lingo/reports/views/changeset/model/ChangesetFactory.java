/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage
 * @generated
 */
public interface ChangesetFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ChangesetFactory eINSTANCE = com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Change Set Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set Root</em>'.
	 * @generated
	 */
	ChangeSetRoot createChangeSetRoot();

	/**
	 * Returns a new object of class '<em>Change Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set</em>'.
	 * @generated
	 */
	ChangeSet createChangeSet();

	/**
	 * Returns a new object of class '<em>Metrics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Metrics</em>'.
	 * @generated
	 */
	Metrics createMetrics();

	/**
	 * Returns a new object of class '<em>Delta Metrics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Delta Metrics</em>'.
	 * @generated
	 */
	DeltaMetrics createDeltaMetrics();

	/**
	 * Returns a new object of class '<em>Change Set Row Data Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set Row Data Group</em>'.
	 * @generated
	 */
	ChangeSetRowDataGroup createChangeSetRowDataGroup();

	/**
	 * Returns a new object of class '<em>Change Set Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set Row</em>'.
	 * @generated
	 */
	ChangeSetRow createChangeSetRow();

	/**
	 * Returns a new object of class '<em>Change Set Row Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set Row Data</em>'.
	 * @generated
	 */
	ChangeSetRowData createChangeSetRowData();

	/**
	 * Returns a new object of class '<em>Change Set Table Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set Table Group</em>'.
	 * @generated
	 */
	ChangeSetTableGroup createChangeSetTableGroup();

	/**
	 * Returns a new object of class '<em>Change Set Table Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set Table Row</em>'.
	 * @generated
	 */
	ChangeSetTableRow createChangeSetTableRow();

	/**
	 * Returns a new object of class '<em>Change Set Table Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Set Table Root</em>'.
	 * @generated
	 */
	ChangeSetTableRoot createChangeSetTableRoot();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ChangesetPackage getChangesetPackage();

} //ChangesetFactory
