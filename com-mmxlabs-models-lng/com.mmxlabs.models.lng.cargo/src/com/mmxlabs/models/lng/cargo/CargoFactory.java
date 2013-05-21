/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoPackage
 * @generated
 */
public interface CargoFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoFactory eINSTANCE = com.mmxlabs.models.lng.cargo.impl.CargoFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Cargo</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo</em>'.
	 * @generated
	 */
	Cargo createCargo();

	/**
	 * Returns a new object of class '<em>Load Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Load Slot</em>'.
	 * @generated
	 */
	LoadSlot createLoadSlot();

	/**
	 * Returns a new object of class '<em>Discharge Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Discharge Slot</em>'.
	 * @generated
	 */
	DischargeSlot createDischargeSlot();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	CargoModel createCargoModel();

	/**
	 * Returns a new object of class '<em>Spot Load Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Load Slot</em>'.
	 * @generated
	 */
	SpotLoadSlot createSpotLoadSlot();

	/**
	 * Returns a new object of class '<em>Spot Discharge Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Discharge Slot</em>'.
	 * @generated
	 */
	SpotDischargeSlot createSpotDischargeSlot();

	/**
	 * Returns a new object of class '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group</em>'.
	 * @generated
	 */
	CargoGroup createCargoGroup();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CargoPackage getCargoPackage();

} //CargoFactory
