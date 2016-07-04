/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	 * Returns a new object of class '<em>Vessel Availability</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Availability</em>'.
	 * @generated
	 */
	VesselAvailability createVesselAvailability();

	/**
	 * Returns a new object of class '<em>Maintenance Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Maintenance Event</em>'.
	 * @generated
	 */
	MaintenanceEvent createMaintenanceEvent();

	/**
	 * Returns a new object of class '<em>Dry Dock Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dry Dock Event</em>'.
	 * @generated
	 */
	DryDockEvent createDryDockEvent();

	/**
	 * Returns a new object of class '<em>Charter Out Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Out Event</em>'.
	 * @generated
	 */
	CharterOutEvent createCharterOutEvent();

	/**
	 * Returns a new object of class '<em>Vessel Type Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Type Group</em>'.
	 * @generated
	 */
	VesselTypeGroup createVesselTypeGroup();

	/**
	 * Returns a new object of class '<em>End Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>End Heel Options</em>'.
	 * @generated
	 */
	EndHeelOptions createEndHeelOptions();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CargoPackage getCargoPackage();

} //CargoFactory
