/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.fleet.FleetPackage
 * @generated
 */
public interface FleetFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetFactory eINSTANCE = scenario.fleet.impl.FleetFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	FleetModel createFleetModel();

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
	 * Returns a new object of class '<em>Fuel Consumption Line</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Consumption Line</em>'.
	 * @generated
	 */
	FuelConsumptionLine createFuelConsumptionLine();

	/**
	 * Returns a new object of class '<em>Vessel State Attributes</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel State Attributes</em>'.
	 * @generated
	 */
	VesselStateAttributes createVesselStateAttributes();

	/**
	 * Returns a new object of class '<em>Port And Time</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port And Time</em>'.
	 * @generated
	 */
	PortAndTime createPortAndTime();

	/**
	 * Returns a new object of class '<em>Charter Out</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Out</em>'.
	 * @generated
	 */
	CharterOut createCharterOut();

	/**
	 * Returns a new object of class '<em>Drydock</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Drydock</em>'.
	 * @generated
	 */
	Drydock createDrydock();

	/**
	 * Returns a new object of class '<em>Vessel Fuel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Fuel</em>'.
	 * @generated
	 */
	VesselFuel createVesselFuel();

	/**
	 * Returns a new object of class '<em>Port Exclusion</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port Exclusion</em>'.
	 * @generated
	 */
	PortExclusion createPortExclusion();

	/**
	 * Returns a new object of class '<em>Vessel Class Cost</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Class Cost</em>'.
	 * @generated
	 */
	VesselClassCost createVesselClassCost();

	/**
	 * Returns a new object of class '<em>Port Time And Heel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port Time And Heel</em>'.
	 * @generated
	 */
	PortTimeAndHeel createPortTimeAndHeel();

	/**
	 * Returns a new object of class '<em>Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Heel Options</em>'.
	 * @generated
	 */
	HeelOptions createHeelOptions();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	FleetPackage getFleetPackage();

} //FleetFactory
