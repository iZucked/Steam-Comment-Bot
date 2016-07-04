/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage
 * @generated
 */
public interface ActualsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ActualsFactory eINSTANCE = com.mmxlabs.models.lng.actuals.impl.ActualsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	ActualsModel createActualsModel();

	/**
	 * Returns a new object of class '<em>Cargo Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Actuals</em>'.
	 * @generated
	 */
	CargoActuals createCargoActuals();

	/**
	 * Returns a new object of class '<em>Load Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Load Actuals</em>'.
	 * @generated
	 */
	LoadActuals createLoadActuals();

	/**
	 * Returns a new object of class '<em>Discharge Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Discharge Actuals</em>'.
	 * @generated
	 */
	DischargeActuals createDischargeActuals();

	/**
	 * Returns a new object of class '<em>Return Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Return Actuals</em>'.
	 * @generated
	 */
	ReturnActuals createReturnActuals();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ActualsPackage getActualsPackage();

} //ActualsFactory
