/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.fleet.FleetPackage
 * @generated
 */
public interface FleetFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetFactory eINSTANCE = com.mmxlabs.models.lng.fleet.impl.FleetFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Vessel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel</em>'.
	 * @generated
	 */
	Vessel createVessel();

	/**
	 * Returns a new object of class '<em>Vessel Class</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Class</em>'.
	 * @generated
	 */
	VesselClass createVesselClass();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	FleetModel createFleetModel();

	/**
	 * Returns a new object of class '<em>Base Fuel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Fuel</em>'.
	 * @generated
	 */
	BaseFuel createBaseFuel();

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
	 * Returns a new object of class '<em>Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Heel Options</em>'.
	 * @generated
	 */
	HeelOptions createHeelOptions();

	/**
	 * Returns a new object of class '<em>Vessel State Attributes</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel State Attributes</em>'.
	 * @generated
	 */
	VesselStateAttributes createVesselStateAttributes();

	/**
	 * Returns a new object of class '<em>Vessel Availability</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Availability</em>'.
	 * @generated
	 */
	VesselAvailability createVesselAvailability();

	/**
	 * Returns a new object of class '<em>Fuel Consumption</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Consumption</em>'.
	 * @generated
	 */
	FuelConsumption createFuelConsumption();

	/**
	 * Returns a new object of class '<em>Maintenance Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Maintenance Event</em>'.
	 * @generated
	 */
	MaintenanceEvent createMaintenanceEvent();

	/**
	 * Returns a new object of class '<em>Vessel Class Route Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Class Route Parameters</em>'.
	 * @generated
	 */
	VesselClassRouteParameters createVesselClassRouteParameters();

	/**
	 * Returns a new object of class '<em>Scenario Fleet Model</em>'.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scenario Fleet Model</em>'.
	 * @generated
	 */
	ScenarioFleetModel createScenarioFleetModel();

	/**
	 * Returns a new object of class '<em>Vessel Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Group</em>'.
	 * @generated
	 */
	VesselGroup createVesselGroup();

	/**
	 * Returns a new object of class '<em>Vessel Type Group</em>'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Type Group</em>'.
	 * @generated
	 */
	VesselTypeGroup createVesselTypeGroup();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	FleetPackage getFleetPackage();

} //FleetFactory
